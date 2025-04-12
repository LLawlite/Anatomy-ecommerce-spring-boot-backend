package com.ecommerce.anatomy.payload.Response;

import com.ecommerce.anatomy.exceptions.OtpVerificationException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor

public class OtpVerificationResponse {
    public String message;
    public boolean status;

    public OtpVerificationResponse(String message, boolean status){
        this.message=message;
        this.status=status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
