package com.dataart.secondmonth.exception;

public class EmptyPostException extends RuntimeException {

    public EmptyPostException() {
        super();
    }

    public EmptyPostException(String message) {
        super(message);
    }

}
