package com.budgeez.model.helpers;

import com.budgeez.model.entities.dao.User;
import com.budgeez.model.interfaces.ITextHelper;
import com.budgeez.security.entities.Token;

import java.util.Base64;
import java.util.UUID;

public class TextHelper implements ITextHelper {

    private static final String tokenDelimiter = "##";

    public String getSecretEmail(String email) {
        String [] parsedEmail = email.split("@");
        if(parsedEmail[0].length() == 1){
            return "*" + "@" + parsedEmail[1];
        }else if (parsedEmail[0].length() == 2){
            return parsedEmail[0].charAt(0) + "*" + "@" + parsedEmail[1];
        }else {
            return parsedEmail[0].charAt(0) + "..." + parsedEmail[0].charAt(parsedEmail[0].length()-1) + "@" + parsedEmail[1];
        }
    }

    public Token generateToken(User user) {
        return new Token(UUID.randomUUID().toString(), user.getUuid());
    }

    public String tokenToString(Token token){
        String tokenString = token.getTokenUuid() + tokenDelimiter + token.getUserUuid();
        return Base64.getEncoder().encodeToString(tokenString.getBytes());
    }

    public Token stringToToken(String string){
        byte[] decoded = Base64.getDecoder().decode(string);
        String [] token = new String(decoded).split(tokenDelimiter);
        return new Token(token[0], token[1]);
    }
}
