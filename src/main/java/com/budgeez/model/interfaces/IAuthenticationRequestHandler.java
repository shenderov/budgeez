package com.budgeez.model.interfaces;

import com.budgeez.model.entities.external.EGeneralResponse;
import com.budgeez.model.entities.external.EUserDetails;
import com.budgeez.security.entities.ChangeEmailWrapper;
import com.budgeez.security.entities.ChangePasswordWrapper;
import com.budgeez.security.entities.JwtAuthenticationRequest;
import com.budgeez.security.entities.SignUpWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;

import javax.servlet.http.HttpServletRequest;

public interface IAuthenticationRequestHandler {

    ResponseEntity<?> createAuthenticationToken(JwtAuthenticationRequest authenticationRequest, Device device);

    ResponseEntity<?> createUser(SignUpWrapper wrapper, Device device);

    ResponseEntity<?> reviveUser(JwtAuthenticationRequest authenticationRequest, Device device);

    ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request);

    EUserDetails getUserDetails(HttpServletRequest request);

    EUserDetails updateUserDetails(EUserDetails details, HttpServletRequest request);

    EGeneralResponse changeEmail(ChangeEmailWrapper wrapper, HttpServletRequest request, Device device);

    EGeneralResponse changePassword(ChangePasswordWrapper wrapper, HttpServletRequest request, Device device);

    EGeneralResponse deleteAccount(JwtAuthenticationRequest credentials, HttpServletRequest request, Device device);

    Boolean isEmailRegistered(String email);

    EGeneralResponse verify(String token);

    EGeneralResponse confirm(String token);
}
