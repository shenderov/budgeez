package com.budgeez.security.service;

import com.budgeez.model.entities.dao.User;
import com.budgeez.model.entities.external.EGeneralResponse;
import com.budgeez.model.enumerations.ResponseStatus;
import com.budgeez.model.interfaces.IMailingHelper;
import com.budgeez.model.interfaces.IMailingService;
import com.budgeez.model.interfaces.ITextHelper;
import com.budgeez.model.interfaces.IValidationHelper;
import com.budgeez.security.entities.PasswordResetToken;
import com.budgeez.security.entities.Token;
import com.budgeez.security.interfaces.IPasswordResetService;
import com.budgeez.security.repository.PasswordResetTokenRepository;
import com.budgeez.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PasswordResetService implements IPasswordResetService {

    private static final long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(90);

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ITextHelper textHelper;

    @Autowired
    private IMailingService mailingService;

    @Autowired
    private IMailingHelper mailingHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IValidationHelper validationHelper;

    public EGeneralResponse createPasswordResetToken(User user) {
        EGeneralResponse response;
        PasswordResetToken token = new PasswordResetToken(textHelper.generateToken(user), user, new Date());
        try {
            if(tokenRepository.findByUser(user) != null){
                tokenRepository.deleteByUser(user);
            }
            tokenRepository.save(token);
            response = new EGeneralResponse(ResponseStatus.SUCCESS, "Reset Password email has been sent", "Please, check your email for password reset instructions");
            mailingService.sendHtmlMessage(mailingHelper.generatePasswordResetPassword(token.getToken(), user));
        } catch (Exception e) {
            response = new EGeneralResponse(ResponseStatus.ERROR, "Error", "Reset Password email can't be send. Please, contact administrator");
            e.printStackTrace();
        }
        return response;
    }

    public EGeneralResponse resetPassword(String password, Token token){
        EGeneralResponse response;
        PasswordResetToken savedToken = tokenRepository.findByToken(token);
        if(savedToken == null){
            response = new EGeneralResponse(ResponseStatus.ERROR, "Error", "Reset Password token can't be find");
        }else if (savedToken.getTimestamp() + EXPIRATION_TIME < System.currentTimeMillis()){
            response = new EGeneralResponse(ResponseStatus.ERROR, "Error", "Reset Password token is expired");
        }else{
            User user = userRepository.findByUuid(token.getUserUuid());
            userRepository.updatePassword(passwordEncoder.encode(password), user.getId());
            response = new EGeneralResponse(ResponseStatus.SUCCESS, "Password has been reset", "Your password has been reset");
        }
        tokenRepository.deleteByToken(token);
        return response;
    }
}
