package com.kamabizbazti.restcontroller;

import com.kamabizbazti.security.entities.*;
import com.kamabizbazti.model.repository.CurrencyRepository;
import com.kamabizbazti.model.repository.LanguageRepository;
import com.kamabizbazti.security.exceptions.EmailAlreadyRegisteredException;
import com.kamabizbazti.security.repository.AuthorityRepository;
import com.kamabizbazti.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.kamabizbazti.security.JwtTokenUtil;
import com.kamabizbazti.security.service.JwtAuthenticationResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RestController
public class AuthenticationRestController {

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

    @Value("${security.header}")
    private String tokenHeader;
    private static final String DEFAULT_LANGUAGE = "ENG";
    private static final String DEFAULT_CURRENCY = "USD";

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "${security.route.authentication.login}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {
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
    public ResponseEntity<?> createUser(@RequestBody @Valid SignUpWrapper wrapper, Device device) throws EmailAlreadyRegisteredException {
        if (userRepository.findByUsername(wrapper.getEmail()) != null)
            throw new EmailAlreadyRegisteredException();
        else {
            User user = new User(wrapper.getEmail().toLowerCase(), passwordEncoder.encode(wrapper.getPassword()), wrapper.getName());
            user.setAuthorities(setUserAuthorities());
            user.setLanguage(languageRepository.findByLanguageCode(DEFAULT_LANGUAGE));
            user.setCurrency(currencyRepository.findByCurrencyCode(DEFAULT_CURRENCY));
            userRepository.save(user);
        }
        return createAuthenticationToken(new JwtAuthenticationRequest(wrapper.getEmail().toLowerCase(), wrapper.getPassword()), device);
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