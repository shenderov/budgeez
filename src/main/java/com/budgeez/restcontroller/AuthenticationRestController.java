package com.budgeez.restcontroller;

import com.budgeez.model.entities.external.EGeneralResponse;
import com.budgeez.model.entities.external.EUserDetails;
import com.budgeez.model.exceptions.UserRegistrationException;
import com.budgeez.model.interfaces.IAuthenticationRequestHandler;
import com.budgeez.model.interfaces.IAuthenticationRestController;
import com.budgeez.security.entities.ChangeEmailWrapper;
import com.budgeez.security.entities.ChangePasswordWrapper;
import com.budgeez.security.entities.JwtAuthenticationRequest;
import com.budgeez.security.entities.SignUpWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AuthenticationRestController implements IAuthenticationRestController {

    @Autowired
    private IAuthenticationRequestHandler handler;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "${security.route.authentication.login}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid JwtAuthenticationRequest authenticationRequest, Device device) {
        return handler.createAuthenticationToken(authenticationRequest, device);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "${security.route.authentication.signup}", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody @Valid SignUpWrapper wrapper, Device device) throws UserRegistrationException {
        return handler.createUser(wrapper, device);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "${security.route.account.reviveUser}", method = RequestMethod.POST)
    public ResponseEntity<?> reviveUser(@RequestBody @Valid JwtAuthenticationRequest authenticationRequest, Device device) {
        return handler.reviveUser(authenticationRequest, device);
    }

    @RequestMapping(value = "${security.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        return handler.refreshAndGetAuthenticationToken(request);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "${security.route.userDetails.isEmailRegistered}", method = RequestMethod.POST)
    public Boolean isEmailRegistered(@RequestBody String email) {
        return handler.isEmailRegistered(email);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${security.route.userDetails.getUserDetails}", method = RequestMethod.GET)
    public EUserDetails getUserDetails(HttpServletRequest request) {
        return handler.getUserDetails(request);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "${security.route.userDetails.updateUserDetails}", method = RequestMethod.POST)
    public EUserDetails updateUserDetails(@RequestBody @Valid EUserDetails details, HttpServletRequest request) {
        return handler.updateUserDetails(details, request);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "${security.route.userDetails.changeEmail}", method = RequestMethod.POST)
    public EGeneralResponse changeEmail(@RequestBody @Valid ChangeEmailWrapper wrapper, HttpServletRequest request, Device device) {
        return handler.changeEmail(wrapper, request, device);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "${security.route.userDetails.changePassword}", method = RequestMethod.POST)
    public EGeneralResponse changePassword(@RequestBody @Valid ChangePasswordWrapper wrapper, HttpServletRequest request, Device device) {
        return handler.changePassword(wrapper, request, device);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "${security.route.account.deleteAccount}", method = RequestMethod.POST)
    public EGeneralResponse deleteAccount(@RequestBody @Valid JwtAuthenticationRequest credentials, HttpServletRequest request, Device device) {
        return handler.deleteAccount(credentials, request, device);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${security.route.authentication.confirm}", method = RequestMethod.GET)
    public EGeneralResponse confirm(@RequestParam("token") String token) {
        return handler.confirm(token);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${security.route.authentication.verify}", method = RequestMethod.GET)
    public EGeneralResponse verify(@RequestParam("token") String token) {
        return handler.verify(token);
    }
}