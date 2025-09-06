package com.devsenior.service;

import java.util.HashMap;
import java.util.Map;

import com.devsenior.exception.InvalidDataException;
import com.devsenior.model.Student;
import com.devsenior.util.Validator;

public class StudentService {
    private Map<Integer, Student> students;
    private Integer key;

    public StudentService (){
        students= new HashMap<>();
        key=1;
    }

    public void addStudent(Student student){
        if (!students.containsKey(key) && !students.containsValue(student)) {
            students.put(key, student);
            key++;
        } 
    }

    public Student createStudent(String name, String email) throws InvalidDataException{
        if(!Validator.validateName(name)){
            throw new InvalidDataException("Nombre no valido");
        } else if (!Validator.validateEmail(email)){
            throw new InvalidDataException("Email no valido");
        } else {
            Student student = new Student(Validator.createId(), name, email);
            addStudent(student);
            return student;
        }
    }

    public Student findStudent(String id) {
        
    }
}
