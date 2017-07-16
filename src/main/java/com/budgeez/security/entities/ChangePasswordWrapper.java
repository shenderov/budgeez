package com.budgeez.security.entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class ChangePasswordWrapper {

    @NotBlank(message = "error.password.notblank")
    @Size(min = 6, max = 128, message = "error.password.size")
    private String currentPassword;

    @NotBlank(message = "error.password.notblank")
    @Size(min = 6, max = 128, message = "error.password.size")
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
