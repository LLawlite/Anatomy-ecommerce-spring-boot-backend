package com.ecommerce.anatomy.payload.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerifyDto {
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobile;

    @NotBlank(message = "OTP is required")
    private String otp;

    public  String getMobile() {
        return mobile;
    }

    public void setMobile( String mobile) {
        this.mobile = mobile;
    }

    public  String getOtp() {
        return otp;
    }

    public void setOtp (String otp) {
        this.otp = otp;
    }

    // Getters and Setters
}