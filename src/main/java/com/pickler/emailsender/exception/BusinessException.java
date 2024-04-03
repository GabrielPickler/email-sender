package com.pickler.emailsender.exception;

public class BusinessException extends RuntimeException {

    private final String message;

    public BusinessException(String message) {
        this.message = message;
    }
}
