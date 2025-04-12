package com.ecommerce.anatomy.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    // Runs after dependency injection is done to perform any initialization
    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }
}