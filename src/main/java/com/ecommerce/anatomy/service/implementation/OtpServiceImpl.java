package com.ecommerce.anatomy.service.implementation;


import com.ecommerce.anatomy.exceptions.OtpVerificationException;
import com.ecommerce.anatomy.model.OtpToken;
import com.ecommerce.anatomy.repositories.OtpTokenRepository;
import com.ecommerce.anatomy.service.interfaces.OtpService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpTokenRepository otpRepo;

    @Value("${twilio.phoneNumber}")
    private String fromPhoneNumber;

    @Override
    @Transactional
    public void generateAndSendOtp(String mobile) {
        String otp = String.valueOf(new Random().nextInt(899999) + 100000);
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);

        // Remove old OTP if any
        otpRepo.deleteByMobile(mobile);

        // Save new OTP
        OtpToken token = new OtpToken();
        token.setMobile(mobile);
        token.setOtp(otp);
        token.setExpiresAt(expiresAt);
        otpRepo.save(token);
        // Ensure the mobile number starts with +91
        if (!mobile.startsWith("+91")) {
            mobile = "+91" + mobile;
        }
        sendSms(mobile, "Your OTP is: " + otp);
    }

    @Override
    public void sendSms(String to, String message) {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(fromPhoneNumber),

                // 📝 Alphanumeric sender ID (e.g., "MYSHOP") — only supported in some regions.
                // To enable, uncomment below and comment the above:
                // new PhoneNumber("MYSHOP"),

                message
        ).create();
    }

    @Override
    @Transactional
    public boolean verifyOtp(String mobile, String otp) {

        Optional<OtpToken> tokenOpt = otpRepo.findByMobile(mobile);
        if (tokenOpt.isEmpty()) {
            throw new OtpVerificationException("OTP not found for mobile number");
        }

        OtpToken token = tokenOpt.get();

        if (!token.getOtp().equals(otp)) {
            throw new OtpVerificationException("Invalid OTP");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpRepo.deleteByMobile(mobile);
            throw new OtpVerificationException("OTP has expired");
        }

        otpRepo.deleteByMobile(mobile); // clear OTP on success
        return true;
    }

    @Override
    @Transactional
    public void clearOtp(String mobile) {
        otpRepo.deleteByMobile(mobile);
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    @Transactional(propagation = Propagation.REQUIRES_NEW)  // 👈 Ensures a new transaction
    public void cleanExpiredOtps() {
        otpRepo.deleteExpiredOtps(LocalDateTime.now());
    }
}
