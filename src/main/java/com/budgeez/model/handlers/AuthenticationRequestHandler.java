package com.budgeez.model.handlers;

import com.budgeez.model.entities.dao.User;
import com.budgeez.model.entities.external.EGeneralResponse;
import com.budgeez.model.entities.external.EUserDetails;
import com.budgeez.model.entities.internal.MessageWrapper;
import com.budgeez.model.enumerations.ResponseStatus;
import com.budgeez.model.exceptions.UserRegistrationException;
import com.budgeez.model.exceptions.codes.UserRegistrationErrorCode;
import com.budgeez.model.interfaces.*;
import com.budgeez.model.repository.CurrencyRepository;
import com.budgeez.model.repository.LanguageRepository;
import com.budgeez.security.JwtTokenUtil;
import com.budgeez.security.entities.*;
import com.budgeez.security.interfaces.IVerificationService;
import com.budgeez.security.repository.AuthorityRepository;
import com.budgeez.security.repository.UserRepository;
import com.budgeez.security.service.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.TreeSet;

@Component
public class AuthenticationRequestHandler implements IAuthenticationRequestHandler {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private IVerificationService verificationService;

    @Autowired
    private IMailingService mailingService;

    @Autowired
    private IExceptionMessagesHelper exceptionMessagesHelper;

    @Autowired
    private IValidationHelper validationHelper;

    @Autowired
    private ITextHelper textHelper;

    @Value("${security.header}")
    private String tokenHeader;
    private static final String DEFAULT_LANGUAGE = "ENG";
    private static final String DEFAULT_CURRENCY = "USD";

    public ResponseEntity<?> createAuthenticationToken(JwtAuthenticationRequest authenticationRequest, Device device) {
        final Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authenticationRequest.getUsername(),
                                authenticationRequest.getPassword()
                        )
                );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails, device);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    public ResponseEntity<?> createUser(SignUpWrapper wrapper, Device device) {
        validationHelper.validateStringIsPureAscii(wrapper.getPassword());
        if (userRepository.findByUsername(wrapper.getEmail()) != null)
            throw new UserRegistrationException(UserRegistrationErrorCode.EMAIL_ALREADY_REGISTERED, exceptionMessagesHelper.getLocalizedMessage("error.registration.email"));
        else {
            User user = new User(wrapper.getEmail(), passwordEncoder.encode(wrapper.getPassword()), wrapper.getName());
            user.setAuthorities(setUserAuthorities());
            user.setLanguage(languageRepository.findByLanguageCode(DEFAULT_LANGUAGE));
            user.setCurrency(currencyRepository.findByCurrencyCode(DEFAULT_CURRENCY));
            verificationService.createAccountActivationToken(userRepository.save(user));
        }
        return createAuthenticationToken(new JwtAuthenticationRequest(wrapper.getEmail(), wrapper.getPassword()), device);
    }

    public ResponseEntity<?> reviveUser(JwtAuthenticationRequest authenticationRequest, Device device) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        if(!passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }else{
            userRepository.reviveUser(userDetails.getUsername());
        }
        return ResponseEntity.ok(true);
    }

    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public EUserDetails getUserDetails(HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        EUserDetails userDetails = new EUserDetails(userRepository.findByUsername(username));
        userDetails.setEmail(textHelper.getSecretEmail(userDetails.getEmail()));
        return userDetails;
    }

    public EUserDetails updateUserDetails(EUserDetails details, HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        userRepository.updateUserDetails(details.getName(), details.getLanguage(), details.getCurrency(), details.getStartDay(), username);
        EUserDetails userDetails = new EUserDetails(userRepository.findByUsername(username));
        userDetails.setEmail(textHelper.getSecretEmail(userDetails.getEmail()));
        return userDetails;
    }

    public EGeneralResponse changeEmail(ChangeEmailWrapper wrapper, HttpServletRequest request, Device device) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        User user = userRepository.findByUsername(username);
        JwtAuthenticationRequest authenticationRequest = new JwtAuthenticationRequest(username, wrapper.getPassword());
        EGeneralResponse response;
        try {
            createAuthenticationToken(authenticationRequest, device);
            if(isEmailRegistered(wrapper.getEmail())){
                response = new EGeneralResponse(ResponseStatus.ERROR, "Error", "Email already registered");
            }else{
                verificationService.createEmailVerificationToken(user, wrapper.getEmail());
                response = new EGeneralResponse(ResponseStatus.SUCCESS, "Success", "Your request is recieved. Check email for further information");
            }
        } catch (BadCredentialsException e) {
            response = new EGeneralResponse(ResponseStatus.ERROR, "Error", "Wrong password");
        }
        return response;
    }

    public EGeneralResponse changePassword(ChangePasswordWrapper wrapper, HttpServletRequest request, Device device) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        User user = userRepository.findByUsername(username);
        JwtAuthenticationRequest authenticationRequest = new JwtAuthenticationRequest(username, wrapper.getCurrentPassword());
        EGeneralResponse response;
        try {
            createAuthenticationToken(authenticationRequest, device);
            userRepository.updatePassword(passwordEncoder.encode(wrapper.getNewPassword()), user.getId());
            mailingService.sendTextMessage(new MessageWrapper(username, "Password has been changed", "Your password has bees successfully changed"));
            response = new EGeneralResponse(ResponseStatus.SUCCESS, "Success", "Password has been changed");
        } catch (BadCredentialsException e) {
            response = new EGeneralResponse(ResponseStatus.ERROR, "Error", "Wrong password");
        }
        return response;
    }

    public EGeneralResponse deleteAccount(JwtAuthenticationRequest credentials, HttpServletRequest request, Device device) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        User user = userRepository.findByUsername(username);
        JwtAuthenticationRequest authenticationRequest = new JwtAuthenticationRequest(credentials.getUsername(), credentials.getPassword());
        EGeneralResponse response;
        try {
            if(!username.equals(credentials.getUsername())){
                throw new BadCredentialsException("Wrong email");
            }
            createAuthenticationToken(authenticationRequest, device);
            userRepository.disableUser(user.getId());
            mailingService.sendTextMessage(new MessageWrapper(username, "Account has been deleted", "Your your account has been deactivated. It will be permanently deleted in 30 days. If you want to revive your account, click on the link: <link>"));
            response = new EGeneralResponse(ResponseStatus.SUCCESS, "Success", "Account has been deleted");
        } catch (BadCredentialsException e) {
            response = new EGeneralResponse(ResponseStatus.ERROR, "Error", "Wrong email or password");
        } catch (DisabledException e1) {
            response = new EGeneralResponse(ResponseStatus.ERROR, "Error", "Account has already been deactivated");
        }
        return response;
    }

    public Boolean isEmailRegistered(String email) {
        return userRepository.findByUsername(email) != null;
    }

    public EGeneralResponse verify(String token) {
        validationHelper.validateTokenString(token);
        return verificationService.verifyEmail(textHelper.stringToToken(token));
    }

    public EGeneralResponse confirm(String token) {
        validationHelper.validateTokenString(token);
        return verificationService.confirmAccount(textHelper.stringToToken(token));
    }

    private Set<Authority> setUserAuthorities() {
        Set<Authority> userAuthorities = new TreeSet<>();
        userAuthorities.add(authorityRepository.findByName(AuthorityName.ROLE_USER));
        return userAuthorities;
    }
}
