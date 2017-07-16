package com.budgeez.model.interfaces;

import com.budgeez.model.entities.dao.User;
import com.budgeez.model.entities.internal.MessageWrapper;
import com.budgeez.security.entities.Token;

public interface IMailingHelper {

    MessageWrapper generateAccountActivationMessage(Token token, User user);

    MessageWrapper generateEmailVerificationMessage(Token token, User user, String email);
}
