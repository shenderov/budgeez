package com.budgeez.services;

import com.budgeez.BudgeezBootApplicationTests;
import com.budgeez.common.entities.HttpResponse;
import com.budgeez.model.entities.dao.User;
import com.budgeez.security.entities.SignUpWrapper;
import com.budgeez.security.service.JwtAuthenticationResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

public class VerificationServiceTest extends BudgeezBootApplicationTests {

    private String name = "Test";
    private String email = "activation@budgeez.com";
    private String password = "qwerty";
    private String token;
    private User user;

    @BeforeClass
    public void setup() {
        SignUpWrapper wrapper = new SignUpWrapper();
        wrapper.setName(name);
        wrapper.setEmail(email);
        wrapper.setPassword(password);
        HttpResponse response = authenticationRestControllerConnectorHelper.createUserPositive(wrapper);
        JwtAuthenticationResponse token = (JwtAuthenticationResponse) response.getObject();
        this.token = token.getToken();
        user = userRepository.findByUsername(email);
    }

//    @Test
//    public void activationToken() {
//        String aToken = UUID.randomUUID().toString();
//        ActivationToken activationToken = new ActivationToken(aToken, user);
//        activationTokenRepository.save(activationToken);
//        Iterable<ActivationToken> res = activationTokenRepository.findAll();
//        for (ActivationToken at : res)
//            System.out.println(at.toString());
//    }
//
//    @Test
//    public void verificationToken() {
//        String aToken = UUID.randomUUID().toString();
//        VerificationToken verificationToken = new VerificationToken(aToken, user, "", new Date());
//        verificationTokenRepository.save(verificationToken);
//        Iterable<VerificationToken> res = verificationTokenRepository.findAll();
//        for (VerificationToken vt : res)
//            System.out.println(vt.toString());
//    }

    @Test
    public void tttt(){
        String s = "wwrbnwrjbnrjtb;rw";
        String [] r = s.split("##");
        System.out.println(UUID.fromString("3c672b36-460b-42ca-a54f-420b4de81310").toString());
    }
}
