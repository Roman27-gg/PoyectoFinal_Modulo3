package com.devsenior.model;

import java.util.HashMap;
import java.util.Map;

public class Course {
    private final String code;
    private String name;
    private int maxCapacity;
    private final Map<String, Student> students;

    

    public Course(String code, String name, int maxCapacity) {
        this.code = code;
        this.name = name;
        this.maxCapacity = maxCapacity;
        students = new HashMap<>();
    }

    public Course() {
        this.code = null;
        this.students = null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public Map<String, Student> getStudents() {
        return students;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void addStudent(Student student, String key){
        students.put(key, student);
    }
    
    public void removeStudent(String key){
        students.remove(key);
    }

    @Override
    public String toString() {
        return String.format("""
                Nombre del curso: %s 
                Capacidad: %d
                Codigo: %s
                """, name, maxCapacity, code);
    }
}
