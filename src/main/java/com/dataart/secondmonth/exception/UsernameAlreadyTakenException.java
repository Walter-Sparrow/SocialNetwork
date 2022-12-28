package com.dataart.secondmonth.exception;

public class UsernameAlreadyTakenException extends RuntimeException {

    public UsernameAlreadyTakenException() {
        super();
    }

    public UsernameAlreadyTakenException(String message) {
        super(message);
    }

}
