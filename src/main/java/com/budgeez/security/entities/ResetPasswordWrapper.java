package com.budgeez.security.entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class ResetPasswordWrapper {

    @NotBlank(message = "error.password.notblank")
    @Size(min = 6, max = 128, message = "error.password.size")
    private String newPassword;

    @NotBlank(message = "error.token.notnull")
    private String token;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
