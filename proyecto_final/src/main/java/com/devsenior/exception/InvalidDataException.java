package com.devsenior.exception;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException (String message) {
        super(message);
    }
}
