package com.devsenior.exception;

/**
 * Se lanza cuando un curso alcanza su capacidad máxima de estudiantes.
 */
public class CourseFullException extends RuntimeException {
    
    public CourseFullException (String message){
        super(message);
    }
}
