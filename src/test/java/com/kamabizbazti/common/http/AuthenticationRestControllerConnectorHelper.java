package com.kamabizbazti.common.http;

import com.kamabizbazti.common.TestConfiguration;
import com.kamabizbazti.common.entities.HttpResponse;
import com.kamabizbazti.security.entities.JwtAuthenticationRequest;
import com.kamabizbazti.security.entities.SignUpWrapper;
import com.kamabizbazti.security.service.JwtAuthenticationResponse;

public class AuthenticationRestControllerConnectorHelper extends HttpConnectorGeneral {

    public AuthenticationRestControllerConnectorHelper(int port) {
        super(port);
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
}
