package com.internal.feature.auth.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordRequestDto {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    private String otp;
    
    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String newPassword;
    
    @NotBlank(message = "Confirm password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String confirmPassword;
}