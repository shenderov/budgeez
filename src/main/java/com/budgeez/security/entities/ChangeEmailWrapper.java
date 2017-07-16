package com.budgeez.security.entities;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class ChangeEmailWrapper {

    @NotBlank(message = "error.email.notblank")
    @Size(min = 4, max = 254, message = "error.email.size")
    @Email(message = "error.email.format")
    private String email;

    @NotBlank(message = "error.password.notblank")
    @Size(min = 6, max = 128, message = "error.password.size")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
