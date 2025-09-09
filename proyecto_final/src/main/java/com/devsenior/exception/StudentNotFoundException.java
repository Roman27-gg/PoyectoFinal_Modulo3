package com.devsenior.exception;

/**
 * Se lanza cuando no se encuentra un estudiante en el sistema o en un curso.
 */
public class StudentNotFoundException extends Exception{
    
    public StudentNotFoundException(String message){
        super(message);
    }
}