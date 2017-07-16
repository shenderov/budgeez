package com.budgeez.model.interfaces;

import com.budgeez.model.entities.dao.User;
import com.budgeez.security.entities.Token;

public interface ITextHelper {

    String getSecretEmail(String email);

    Token generateToken(User user);

    String tokenToString(Token token);

    Token stringToToken(String string);

}
