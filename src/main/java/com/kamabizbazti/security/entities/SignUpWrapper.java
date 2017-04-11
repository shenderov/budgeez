package com.kamabizbazti.security.entities;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignUpWrapper {
    //@NotNull(message = "error.name.notnull")
    @NotBlank(message = "error.name.notblank")
    @Size(min = 2, max = 70, message = "error.name.size")
    private String name;

    //@NotNull(message = "error.email.notnull")
    @NotBlank(message = "error.email.notblank")
    @Size(min = 4, max = 254, message = "error.email.size")
    @Email(message = "error.email.format")
    private String email;

    //@NotNull(message = "error.password.notnull")
    @NotBlank(message = "error.password.notblank")
    @Size(min = 6, max = 128, message = "error.password.size")
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
        this.email = email.toLowerCase();
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
