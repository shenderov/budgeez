package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.exceptions.UserRegistrationException;
import com.kamabizbazti.security.entities.JwtAuthenticationRequest;
import com.kamabizbazti.security.entities.SignUpWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;

import javax.servlet.http.HttpServletRequest;

public interface IAuthenticationRestController {

    ResponseEntity<?> createAuthenticationToken(JwtAuthenticationRequest authenticationRequest, Device device);

    ResponseEntity<?> createUser(SignUpWrapper wrapper, Device device) throws UserRegistrationException;

    ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request);


}
