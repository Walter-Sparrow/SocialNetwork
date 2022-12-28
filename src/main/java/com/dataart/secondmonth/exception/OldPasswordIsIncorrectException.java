package com.dataart.secondmonth.exception;

public class OldPasswordIsIncorrectException extends RuntimeException {

    public OldPasswordIsIncorrectException() {
        super();
    }

    public OldPasswordIsIncorrectException(String message) {
        super(message);
    }

}
