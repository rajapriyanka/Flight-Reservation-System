package com.flight.dto;

import jakarta.validation.constraints.Size;

public class ForgotPasswordRequestDto {

    private String username;
    private String otp;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String confirmPassword; 

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
