package com.devsenior.model;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private final String code;
    private String name;
    private String email;
    private final Map<String,Course> courses;

    public Student(String code, String name, String email) {
        this.code = code;
        this.name = name;
        this.email = email;
        courses= new HashMap<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, Course> getCourses() {
        return courses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addCourse(Course course, String key){
        courses.put(key, course);
    }

    public void removeCourse(String key){
        courses.remove(key);
    }

    @Override
    public String toString() {
        return String.format("""
            Nombre: %s
            Email: %s
            Codigo: %s
        """, name, email, code);
    }

    
    
    
}
