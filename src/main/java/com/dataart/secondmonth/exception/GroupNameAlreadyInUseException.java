package com.dataart.secondmonth.exception;

public class GroupNameAlreadyInUseException extends RuntimeException {

    public GroupNameAlreadyInUseException() {
        super();
    }

    public GroupNameAlreadyInUseException(String message) {
        super(message);
    }

}
