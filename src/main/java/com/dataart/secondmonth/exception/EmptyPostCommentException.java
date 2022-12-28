package com.dataart.secondmonth.exception;

public class EmptyPostCommentException extends RuntimeException {

    public EmptyPostCommentException() {
        super();
    }

    public EmptyPostCommentException(String message) {
        super(message);
    }

}
