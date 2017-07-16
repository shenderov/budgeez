package com.budgeez.common.http;

import com.budgeez.common.TestConfiguration;
import com.budgeez.common.entities.HttpResponse;
import com.budgeez.model.entities.external.EGeneralResponse;
import com.budgeez.model.entities.external.EUserDetails;
import com.budgeez.security.entities.ChangeEmailWrapper;
import com.budgeez.security.entities.JwtAuthenticationRequest;
import com.budgeez.security.entities.SignUpWrapper;
import com.budgeez.security.service.JwtAuthenticationResponse;

public class AuthenticationRestControllerConnectorHelper extends HttpConnectorGeneral {

    public AuthenticationRestControllerConnectorHelper(String hostname, int port, String basePath) {
        super(hostname, port, basePath);
    }

    public HttpResponse createAuthenticationTokenPositive(JwtAuthenticationRequest authenticationRequest) {
        HttpResponse response = sendPostRequest(TestConfiguration.LOGIN, authenticationRequest);
        response.setObject(testTools.stringToObject((String) response.getObject(), JwtAuthenticationResponse.class));
        return response;
    }

    public HttpResponse createAuthenticationTokenNegative(String jsonString) {
        return sendPostRequest(TestConfiguration.LOGIN, jsonString) ;
    }

    public HttpResponse createUserPositive(SignUpWrapper wrapper) {
        HttpResponse response = sendPostRequest(TestConfiguration.SIGNUP, wrapper);
        response.setObject(testTools.stringToObject((String) response.getObject(), JwtAuthenticationResponse.class));
        return response;
    }

    public HttpResponse createUserNegative(String jsonString) {
        return sendPostRequest(TestConfiguration.SIGNUP, jsonString);
    }

    public HttpResponse refreshAndGetAuthenticationTokenPositive(String token) {
        HttpResponse response = sendGetRequest(TestConfiguration.REFRESH, token);
        response.setObject(testTools.stringToObject((String) response.getObject(), JwtAuthenticationResponse.class));
        return response;
    }

    public HttpResponse refreshAndGetAuthenticationTokenNegative(String token) {
        return sendGetRequest(TestConfiguration.REFRESH, token);
    }

    public HttpResponse getUserDetailsPositive(String token) {
        HttpResponse response = sendGetRequest(TestConfiguration.GET_USER_DETAILS, token);
        response.setObject(testTools.stringToObject((String) response.getObject(), EUserDetails.class));
        return response;
    }

    public HttpResponse getUserDetailsNegative(String token) {
        return sendGetRequest(TestConfiguration.GET_USER_DETAILS, token);
    }

    public HttpResponse isEmailRegistered(String email) {
        return sendPostRequest(TestConfiguration.IS_EMAIL_REGISTERED, email);
    }

    public HttpResponse changeEmailPositive(ChangeEmailWrapper wrapper, String token) {
        HttpResponse response = sendPostRequest(TestConfiguration.CHANGE_EMAIL, wrapper, token);
        response.setObject(testTools.stringToObject((String) response.getObject(), EGeneralResponse.class));
        return response;
    }

    public HttpResponse changeEmailNegative(ChangeEmailWrapper wrapper, String token) {
        return sendPostRequest(TestConfiguration.CHANGE_EMAIL, wrapper, token);
    }


}
