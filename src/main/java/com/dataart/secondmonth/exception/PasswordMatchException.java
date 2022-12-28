package com.dataart.secondmonth.exception;

public class PasswordMatchException extends RuntimeException {

    public PasswordMatchException() {
        super();
    }

    public PasswordMatchException(String message) {
        super(message);
    }

}
