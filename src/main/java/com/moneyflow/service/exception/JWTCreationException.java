package com.moneyflow.service.exception;

public class JWTCreationException extends RuntimeException {
    public JWTCreationException(String msg) {
        super(msg);
    }
}
