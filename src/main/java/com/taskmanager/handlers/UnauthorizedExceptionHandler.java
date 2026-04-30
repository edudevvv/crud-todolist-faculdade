package com.taskmanager.handlers;

public class UnauthorizedExceptionHandler extends RuntimeException {
    public UnauthorizedExceptionHandler(String message) {
        super(message);
    }
}
