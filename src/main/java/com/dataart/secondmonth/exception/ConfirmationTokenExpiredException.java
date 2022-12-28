package com.dataart.secondmonth.exception;

public class ConfirmationTokenExpiredException extends RuntimeException {

    public ConfirmationTokenExpiredException() {
        super();
    }

    public ConfirmationTokenExpiredException(String message) {
        super(message);
    }

}
