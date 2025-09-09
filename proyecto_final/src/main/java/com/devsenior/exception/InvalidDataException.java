package com.devsenior.exception;

/**
 * Se lanza cuando se proporcionan datos inválidos al sistema,
 * como nombre, correo o capacidad incorrecta.
 */
public class InvalidDataException extends RuntimeException{
    public InvalidDataException (String message) {
        super(message);
    }
}
