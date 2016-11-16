package com.kamabizbazti.security.entities;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignUpWrapper {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 70)
    private String name;

    @NotNull
    @NotBlank
    @Email
    @Size(min = 4, max = 254)
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 128)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "SignUpWrapper{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
