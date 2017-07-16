package com.budgeez.security.interfaces;

import com.budgeez.model.entities.dao.User;
import com.budgeez.model.entities.external.EGeneralResponse;
import com.budgeez.security.entities.Token;

public interface IVerificationService {

    EGeneralResponse createAccountActivationToken(User user);

    EGeneralResponse createEmailVerificationToken(User user, String email);

    EGeneralResponse confirmAccount(Token token);

    EGeneralResponse verifyEmail(Token token);

    void declineEmailToken(Token token);

}
