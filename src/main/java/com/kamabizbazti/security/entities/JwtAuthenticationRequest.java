package com.kamabizbazti.security.entities;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    @NotNull(message = "error.email.notnull")
    @NotBlank(message = "error.email.notblank")
    @Email(message = "error.email.format")
    @Size(min = 4, max = 254, message = "error.email.size")
    private String username;

    @NotNull(message = "error.password.notnull")
    @NotBlank(message = "error.password.notblank")
    @Size(min = 6, max = 128, message = "error.password.size")
    private String password;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
