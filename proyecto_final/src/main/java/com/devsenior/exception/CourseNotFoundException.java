package com.devsenior.exception;

/**
 * Se lanza cuando no se encuentra un curso en el sistema.
 */
public class CourseNotFoundException extends Exception{
    public CourseNotFoundException(String message){
        super(message);
    }
}
