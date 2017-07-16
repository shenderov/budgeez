package com.budgeez.security.entities;

import javax.persistence.Embeddable;

@Embeddable
public class Token {

    private String tokenUuid;

    private String userUuid;

    public Token() {
    }

    public Token(String tokenUuid, String userUuid) {
        this.tokenUuid = tokenUuid;
        this.userUuid = userUuid;
    }

    public String getTokenUuid() {
        return tokenUuid;
    }

    public void setTokenUuid(String tokenUuid) {
        this.tokenUuid = tokenUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenUuid='" + tokenUuid + '\'' +
                ", userUuid='" + userUuid + '\'' +
                '}';
    }
}
