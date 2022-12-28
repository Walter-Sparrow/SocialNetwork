package com.dataart.secondmonth.exception;

public class OldPasswordSameAsNewException extends RuntimeException {

    public OldPasswordSameAsNewException() {
        super();
    }

    public OldPasswordSameAsNewException(String message) {
        super(message);
    }

}
