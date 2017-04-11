package com.kamabizbazti.restcontroller;

import com.kamabizbazti.model.entities.dao.User;
import com.kamabizbazti.model.exceptions.UserRegistrationException;
import com.kamabizbazti.model.exceptions.codes.UserRegistrationErrorCode;
import com.kamabizbazti.model.interfaces.IAuthenticationRestController;
import com.kamabizbazti.model.interfaces.IExceptionMessagesHelper;
import com.kamabizbazti.model.interfaces.IValidationHelper;
import com.kamabizbazti.model.repository.CurrencyRepository;
import com.kamabizbazti.model.repository.LanguageRepository;
import com.kamabizbazti.security.JwtTokenUtil;
import com.kamabizbazti.security.entities.*;
import com.kamabizbazti.security.repository.AuthorityRepository;
import com.kamabizbazti.security.repository.UserRepository;
import com.kamabizbazti.security.service.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;
import java.util.TreeSet;

@RestController
public class AuthenticationRestController implements IAuthenticationRestController {

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
    private IExceptionMessagesHelper exceptionMessagesHelper;

    @Autowired
    private IValidationHelper validationHelper;

    @Value("${security.header}")
    private String tokenHeader;
    private static final String DEFAULT_LANGUAGE = "ENG";
    private static final String DEFAULT_CURRENCY = "USD";

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "${security.route.authentication.login}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid JwtAuthenticationRequest authenticationRequest, Device device) {
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

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "${security.route.authentication.signup}", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody @Valid SignUpWrapper wrapper, Device device) throws UserRegistrationException {
        validationHelper.validateStringIsPureAscii(wrapper.getPassword());
        if (userRepository.findByUsername(wrapper.getEmail()) != null)
            throw new UserRegistrationException(UserRegistrationErrorCode.EMAIL_ALREADY_REGISTERED, exceptionMessagesHelper.getLocalizedMessage("error.registration.email"));
        else {
            User user = new User(wrapper.getEmail(), passwordEncoder.encode(wrapper.getPassword()), wrapper.getName());
            user.setAuthorities(setUserAuthorities());
            user.setLanguage(languageRepository.findByLanguageCode(DEFAULT_LANGUAGE));
            user.setCurrency(currencyRepository.findByCurrencyCode(DEFAULT_CURRENCY));
            userRepository.save(user);
        }
        return createAuthenticationToken(new JwtAuthenticationRequest(wrapper.getEmail(), wrapper.getPassword()), device);
    }

    @RequestMapping(value = "${security.route.authentication.refresh}", method = RequestMethod.GET)
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

    private Set<Authority> setUserAuthorities() {
        Set<Authority> userAuthorities = new TreeSet<>();
        userAuthorities.add(authorityRepository.findByName(AuthorityName.ROLE_USER));
        return userAuthorities;
    }
}