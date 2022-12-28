package com.dataart.secondmonth.exception;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException() {
        super();
    }

    public EmailAlreadyInUseException(String message) {
        super(message);
    }

}
