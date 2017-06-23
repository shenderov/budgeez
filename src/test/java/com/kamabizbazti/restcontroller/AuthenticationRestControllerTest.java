package com.kamabizbazti.restcontroller;

import com.google.gson.JsonObject;
import com.kamabizbazti.KamaBizbaztiBootApplicationTests;
import com.kamabizbazti.common.entities.HttpResponse;
import com.kamabizbazti.common.entities.HttpResponseJson;
import com.kamabizbazti.model.exceptions.codes.AuthenticationErrorCode;
import com.kamabizbazti.model.exceptions.codes.DataIntegrityErrorCode;
import com.kamabizbazti.model.exceptions.codes.UserRegistrationErrorCode;
import com.kamabizbazti.security.entities.JwtAuthenticationRequest;
import com.kamabizbazti.security.entities.SignUpWrapper;
import com.kamabizbazti.model.entities.dao.User;
import com.kamabizbazti.security.service.JwtAuthenticationResponse;

import static org.testng.Assert.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AuthenticationRestControllerTest extends KamaBizbaztiBootApplicationTests {

    private String name = "Test";
    private String email1 = "test1@kamabizbazti.com";
    private String email2 = "test2@kamabizbazti.com";
    private String email3 = "test3@kamabizbazti.com";
    private String email4 = "test4@kamabizbazti.com";
    private String emailMixed = "tEsTmIxEd@KaMaBiZbAzTi.CoM";
    private String email5 = "test5@kamabizbazti.com";
    private String minLengthEmail = "ab@c";
    private String maxLengthEmail = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb.cc";
    private String invalidEmail = "kamabizbazti.com";
    private String minLengthName = "ab";
    private String maxLengthName = "MaxName MaxName MaxName MaxName MaxName MaxName MaxName MaxName MaxNam";
    private String password = "aBc_123_DeF";
    private String cyrilicPwd = "пароль123";
    private String minLengthPassword = "passwd";
    private String maxLengthPassword = "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword";
    private String token;

    @BeforeClass
    public void setup() {
        SignUpWrapper wrapper = new SignUpWrapper();
        wrapper.setName(name);
        wrapper.setEmail(email1);
        wrapper.setPassword(password);
        authenticationRestControllerConnectorHelper.createUserPositive(wrapper);
        wrapper.setEmail(email3);
        HttpResponse response = authenticationRestControllerConnectorHelper.createUserPositive(wrapper);
        JwtAuthenticationResponse token = (JwtAuthenticationResponse) response.getObject();
        this.token = token.getToken();
    }

    @AfterClass
    public void tearDown() {
        userRepository.delete(userRepository.findByUsername(email1).getId());
        userRepository.delete(userRepository.findByUsername(email2).getId());
        userRepository.delete(userRepository.findByUsername(email3).getId());
        userRepository.delete(userRepository.findByUsername(emailMixed.toLowerCase()).getId());
    }

    @Test
    public void testCreateUser() {
        SignUpWrapper wrapper = new SignUpWrapper();
        wrapper.setName(name);
        wrapper.setEmail(email2);
        wrapper.setPassword(password);
        HttpResponse response = authenticationRestControllerConnectorHelper.createUserPositive(wrapper);
        JwtAuthenticationResponse token = (JwtAuthenticationResponse) response.getObject();
        assertNotNull(token.getToken());
        assertNotNull(userRepository.findByUsername(email2));
        assertEquals(response.getHttpStatusCode(), 200);
    }

    @Test
    public void testLogin() {
        validateLogin(email1, password);
    }

    @Test
    public void testRefreshToken() {
        HttpResponse response = authenticationRestControllerConnectorHelper.refreshAndGetAuthenticationTokenPositive(token);
        JwtAuthenticationResponse token = (JwtAuthenticationResponse) response.getObject();
        assertNotNull(token.getToken());
        assertEquals(response.getHttpStatusCode(), 200);
    }

    @Test
    public void testRefreshInvalidToken() {
        String invalidToken = "eyJzdWIiOiJ0ZXN0QGthbWFiaXpiYXp0aS5jb20iLCJhdWRpZW5jZSI6IndlYiIsImNyZWF0ZWQiOjE0NzkyMzU1NzYyODYsImV4cCI6MTQ3OTIzNjc1Nn0.5cUplDQqiZf8Z_rZp1VZe3fNfsVQoLNsmFHKgq8GTo0";
        HttpResponseJson response = refreshTokenJson(invalidToken);
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testRefreshExpiredToken() {
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGthbWFiaXpiYXp0aS5jb20iLCJhdWRpZW5jZSI6IndlYiIsImNyZWF0ZWQiOjE0NzkyMzMxNjA0OTcsImV4cCI6MTQ3OTIzMzM0MH0.WdTasVlFcNOzGiRA0gD-f7ExW-DcUWdiudozVmk_xGVU-Y3gzf7bPjNmJe5QALiwneHlsNrqVBnd5pqp6tlNzQ";
        HttpResponseJson response = refreshTokenJson(expiredToken);
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testLoginUpperCasePassword() {
        JwtAuthenticationRequest request = new JwtAuthenticationRequest(email1, password.toUpperCase());
        HttpResponseJson response = authenticationRestControllerConnectorHelper.createAuthenticationTokenNegative(testTools.objectToJson(request)).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), AuthenticationErrorCode.INVALID_USERNAME_OR_PASSWORD.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Username or Password");
    }

    @Test(dependsOnMethods = {"testCreateUser"})
    public void testCreateUserWithExistingCredentials() {
        SignUpWrapper wrapper = new SignUpWrapper();
        wrapper.setName(name);
        wrapper.setEmail(email2);
        wrapper.setPassword(password);
        HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), UserRegistrationErrorCode.EMAIL_ALREADY_REGISTERED.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Email Already Registered");
    }

    @Test
    public void testCreateUserWithCyrilicPassword() {
        SignUpWrapper wrapper = new SignUpWrapper();
        wrapper.setName(name);
        wrapper.setEmail(email4);
        wrapper.setPassword(cyrilicPwd);
        HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), UserRegistrationErrorCode.PASSWORD_CONTAINS_NOT_ASCII_CHARACTERS.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Password should contains only latin symbols");
    }

    @Test
    public void testCreateUserMixedEmail() {
        SignUpWrapper wrapper = new SignUpWrapper();
        wrapper.setName(name);
        wrapper.setEmail(emailMixed);
        wrapper.setPassword(password);
        HttpResponse response = authenticationRestControllerConnectorHelper.createUserPositive(wrapper);
        JwtAuthenticationResponse token = (JwtAuthenticationResponse) response.getObject();
        assertNotNull(token.getToken());
        User user = userRepository.findByUsername(emailMixed.toLowerCase());
        assertNotNull(user);
        assertEquals(user.getUsername(), emailMixed.toLowerCase());
        assertEquals(response.getHttpStatusCode(), 200);
    }

    @Test(dependsOnMethods = {"testCreateUserMixedEmail"})
    public void testLoginWithMixedEmail() {
        validateLogin(emailMixed, password);
    }

    @Test(dependsOnMethods = {"testCreateUserMixedEmail"})
    public void testLoginWithoutPassword() {
        JsonObject request = new JsonObject();
        request.addProperty("username", emailMixed);
        HttpResponseJson response = authenticationRestControllerConnectorHelper.createAuthenticationTokenNegative(testTools.objectToJson(request)).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Password can't be blank");
    }

    @Test(dependsOnMethods = {"testCreateUserMixedEmail"})
    public void testLoginBlankPassword() {
        JsonObject request = new JsonObject();
        request.addProperty("username", emailMixed);
        request.addProperty("password", "      ");
        HttpResponseJson response = authenticationRestControllerConnectorHelper.createAuthenticationTokenNegative(testTools.objectToJson(request)).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Password can't be blank");
    }

    @Test
    public void testLoginWithInvalidEmail() {
        JwtAuthenticationRequest request = new JwtAuthenticationRequest();
        request.setUsername(invalidEmail);
        request.setPassword(password);
        HttpResponseJson response = authenticationRestControllerConnectorHelper.createAuthenticationTokenNegative(testTools.objectToJson(request)).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid email format");
    }

    @Test
    public void testCreateUserWithInvalidEmail() {
        try {
            SignUpWrapper wrapper = new SignUpWrapper();
            wrapper.setName(name);
            wrapper.setEmail(invalidEmail);
            wrapper.setPassword(password);
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString(), "Invalid email format");
            assertNull(userRepository.findByUsername(invalidEmail));
        } finally {
            User user = userRepository.findByUsername(invalidEmail);
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserWithoutEmail() {
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("name", name);
        wrapper.addProperty("password", password);
        HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Email can't be blank");
    }

    @Test
    public void testCreateUserWithBlankEmail() {
        try {
            JsonObject wrapper = new JsonObject();
            wrapper.addProperty("name", name);
            wrapper.addProperty("password", password);
            wrapper.addProperty("email", "    ");
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString().equals("Invalid email format") || response.getObject().get("message").getAsString().equals("Email can't be blank"), true);
            assertNull(userRepository.findByUsername("    "));
        } finally {
            User user = userRepository.findByUsername("    ");
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserWithShortEmail() {
        try {
            SignUpWrapper wrapper = new SignUpWrapper();
            wrapper.setName(name);
            wrapper.setEmail(minLengthEmail.substring(1));
            wrapper.setPassword(password);
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString(), "Email length should be between 4 and 254 chars");
            assertNull(userRepository.findByUsername(minLengthEmail.substring(1)));
        } finally {
            User user = userRepository.findByUsername(minLengthEmail.substring(1));
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserWithLongEmail() {
        try {
            SignUpWrapper wrapper = new SignUpWrapper();
            wrapper.setName(name);
            wrapper.setEmail(maxLengthEmail + "c");
            wrapper.setPassword(password);
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString(), "Email length should be between 4 and 254 chars");
            assertNull(userRepository.findByUsername(maxLengthEmail + "c"));
        } finally {
            User user = userRepository.findByUsername(maxLengthEmail + "c");
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserMinEmail() {
        validateUserCreated(name, minLengthEmail, password);
    }

    @Test
    public void testCreateUserMaxEmail() {
        validateUserCreated(name, maxLengthEmail, password);
    }

    @Test
    public void testCreateUserWithoutName() {
        try {
            JsonObject wrapper = new JsonObject();
            wrapper.addProperty("email", email5);
            wrapper.addProperty("password", password);
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString(), "Name can't be blank");
            assertNull(userRepository.findByUsername(email5));
        } finally {
            User user = userRepository.findByUsername(email5);
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserWithBlankName() {
        try {
            JsonObject wrapper = new JsonObject();
            wrapper.addProperty("email", email5);
            wrapper.addProperty("password", password);
            wrapper.addProperty("name", "  ");
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString(), "Name can't be blank");
            assertNull(userRepository.findByUsername(email5));
        } finally {
            User user = userRepository.findByUsername(email5);
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserWithShortName() {
        try {
            SignUpWrapper wrapper = new SignUpWrapper();
            wrapper.setName(minLengthName.substring(1));
            wrapper.setEmail(email5);
            wrapper.setPassword(password);
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString(), "Name length should be between 2 and 70 chars");
            assertNull(userRepository.findByUsername(email5));
        } finally {
            User user = userRepository.findByUsername(email5);
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserWithLongName() {
        try {
            SignUpWrapper wrapper = new SignUpWrapper();
            wrapper.setName(maxLengthName + "e");
            wrapper.setEmail(email5);
            wrapper.setPassword(password);
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString(), "Name length should be between 2 and 70 chars");
            assertNull(userRepository.findByUsername(email5));
        } finally {
            User user = userRepository.findByUsername(email5);
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserMinName() {
        validateUserCreated(minLengthName, email5, password);
    }

    @Test
    public void testCreateUserMaxName() {
        validateUserCreated(maxLengthName, email5, password);
    }

    @Test
    public void testCreateUserWithoutPassword() {
        try {
            JsonObject wrapper = new JsonObject();
            wrapper.addProperty("email", email5);
            wrapper.addProperty("name", name);
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString(), "Password can't be blank");
            assertNull(userRepository.findByUsername(email5));
        } finally {
            User user = userRepository.findByUsername(email5);
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserWithBlankPassword() {
        try {
            JsonObject wrapper = new JsonObject();
            wrapper.addProperty("email", email5);
            wrapper.addProperty("name", name);
            wrapper.addProperty("password", "      ");
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString(), "Password can't be blank");
            assertNull(userRepository.findByUsername(email5));
        } finally {
            User user = userRepository.findByUsername(email5);
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserWithShortPassword() {
        try {
            SignUpWrapper wrapper = new SignUpWrapper();
            wrapper.setName(name);
            wrapper.setEmail(email5);
            wrapper.setPassword(minLengthPassword.substring(1));
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString(), "Password length should be between 6 and 128 chars");
            assertNull(userRepository.findByUsername(email5));
        } finally {
            User user = userRepository.findByUsername(email5);
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserWithLongPassword() {
        try {
            SignUpWrapper wrapper = new SignUpWrapper();
            wrapper.setName(name);
            wrapper.setEmail(email5);
            wrapper.setPassword(maxLengthPassword + "d");
            HttpResponseJson response = authenticationRestControllerConnectorHelper.createUserNegative(testTools.objectToJson(wrapper)).convertToHttpResponseJson();
            assertEquals(response.getHttpStatusCode(), 500);
            assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
            assertEquals(response.getObject().get("message").getAsString(), "Password length should be between 6 and 128 chars");
            assertNull(userRepository.findByUsername(email5));
        } finally {
            User user = userRepository.findByUsername(email5);
            if (user != null)
                userRepository.delete(user.getId());
        }
    }

    @Test
    public void testCreateUserMinPassword() {
        validateUserCreated(name, email5, minLengthPassword);
    }

    @Test
    public void testCreateUserMaxPassword() {
        validateUserCreated(maxLengthName, email5, maxLengthPassword);
    }

    private HttpResponseJson refreshTokenJson(String token) {
        return authenticationRestControllerConnectorHelper.refreshAndGetAuthenticationTokenNegative(token).convertToHttpResponseJson();
    }

    private void validateLogin(String username, String password) {
        JwtAuthenticationRequest request = new JwtAuthenticationRequest(username, password);
        HttpResponse response = authenticationRestControllerConnectorHelper.createAuthenticationTokenPositive(request);
        JwtAuthenticationResponse token = (JwtAuthenticationResponse) response.getObject();
        assertNotNull(token.getToken());
        assertEquals(response.getHttpStatusCode(), 200);
    }

    private void validateUserCreated(String name, String email, String password) {
        try {
            SignUpWrapper wrapper = new SignUpWrapper();
            wrapper.setName(name);
            wrapper.setEmail(email);
            wrapper.setPassword(password);
            HttpResponse response = authenticationRestControllerConnectorHelper.createUserPositive(wrapper);
            JwtAuthenticationResponse token = (JwtAuthenticationResponse) response.getObject();
            assertEquals(response.getHttpStatusCode(), 200);
            assertNotNull(token.getToken());
            User user = userRepository.findByUsername(email);
            assertNotNull(user);
            assertEquals(user.getUsername(), email);
            assertEquals(user.getName(), name);
            validateLogin(email, password);
        } finally {
            User user = userRepository.findByUsername(email);
            if (user != null)
                userRepository.delete(user.getId());
        }
    }
}
