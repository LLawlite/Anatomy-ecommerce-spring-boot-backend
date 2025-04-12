package com.ecommerce.anatomy.exceptions;

public class OtpVerificationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public OtpVerificationException() {

    }
    public OtpVerificationException(String message) {
        super(message);
    }
}