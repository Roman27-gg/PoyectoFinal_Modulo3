package com.devsenior.model;

import java.util.HashMap;
import java.util.Map;

public class Course {
    private String code;
    private String name;
    private Integer maxCapacity;
    private Map<Integer, Student> students;

    public Course(String code, String name, Integer maxCapacity) {
        this.code = code;
        this.name = name;
        this.maxCapacity = maxCapacity;
        students = new HashMap<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Map<Integer, Student> getStudents() {
        return students;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void addStudent(Student student, Integer key){
        students.put(key, student);
    }
    
    public void removeStudent(Integer key){
        students.remove(key);
    }

    

    
    
}
