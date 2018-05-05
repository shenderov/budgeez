package com.budgeez.model.interfaces;

import com.budgeez.model.entities.external.EGeneralResponse;
import com.budgeez.model.entities.external.EUserDetails;
import com.budgeez.model.exceptions.UserRegistrationException;
import com.budgeez.security.entities.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;

import javax.servlet.http.HttpServletRequest;

public interface IAuthenticationRestController {

    ResponseEntity<?> createAuthenticationToken(JwtAuthenticationRequest authenticationRequest, Device device);

    ResponseEntity<?> createUser(SignUpWrapper wrapper, Device device) throws UserRegistrationException;

    ResponseEntity<?> reviveUser(JwtAuthenticationRequest authenticationRequest, Device device);

    ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request);

    EGeneralResponse forgotPassword(String email);

    EGeneralResponse resetPassword(ResetPasswordWrapper wrapper);

    Boolean isEmailRegistered(String email);

    EUserDetails getUserDetails(HttpServletRequest request);

    EUserDetails updateUserDetails(EUserDetails details, HttpServletRequest request);

    EGeneralResponse changeEmail(ChangeEmailWrapper wrapper, HttpServletRequest request, Device device);

    EGeneralResponse changePassword(ChangePasswordWrapper wrapper, HttpServletRequest request, Device device);

    EGeneralResponse deleteAccount(JwtAuthenticationRequest credentials, HttpServletRequest request, Device device);

    EGeneralResponse verify(String token);

    EGeneralResponse confirm(String token);
}
