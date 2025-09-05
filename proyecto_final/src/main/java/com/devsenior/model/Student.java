package com.devsenior.model;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String code;
    private String name;
    private String email;
    private Map<Integer,Course> courses;

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

    public Map<Integer, Course> getCourses() {
        return courses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addCourse(Course course, Integer key){
        courses.put(key, course);
    }

    public void removeCourse(Integer key){
        courses.remove(key);
    }

    
    
    
}
