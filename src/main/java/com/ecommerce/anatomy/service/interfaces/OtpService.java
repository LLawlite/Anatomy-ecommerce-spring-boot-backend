package com.ecommerce.anatomy.service.interfaces;

public interface OtpService {
    void generateAndSendOtp(String mobile);

    void sendSms(String to, String message);

    boolean verifyOtp(String mobile, String otp);

    void clearOtp(String mobile);
}
