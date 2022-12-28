package com.dataart.secondmonth.exception;

public class ConfirmationTokenNotFound extends RuntimeException {

    public ConfirmationTokenNotFound() {
        super();
    }

    public ConfirmationTokenNotFound(String message) {
        super(message);
    }

}
