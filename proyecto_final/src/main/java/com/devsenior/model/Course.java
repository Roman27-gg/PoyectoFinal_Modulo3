package com.devsenior.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa un curso dentro del sistema.
 * Un curso tiene un código único, un nombre, una capacidad máxima
 * y una lista de estudiantes inscritos.
 */
public class Course {

    private final String code;
    private String name;
    private int maxCapacity;
    private final Map<String, Student> students;

    /**
     * Crea un nuevo curso con código, nombre y capacidad máxima.
     *
     * @param code        identificador único del curso
     * @param name        nombre del curso
     * @param maxCapacity número máximo de estudiantes permitidos
     */
    public Course(String code, String name, int maxCapacity) {
        this.code = code;
        this.name = name;
        this.maxCapacity = maxCapacity;
        students = new HashMap<>();
    }

    /**
     * Constructor vacío para inicializar un curso sin datos.
     * El código será {@code null}.
     */
    public Course() {
        this.code = null;
        this.students = new HashMap<>();
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

    /**
     * Agrega un estudiante a la lista de inscritos en el curso.
     *
     * @param student estudiante a agregar
     * @param key     código del estudiante
     */
    public void addStudent(Student student, String key) {
        students.put(key, student);
    }

    /**
     * Elimina un estudiante de la lista de inscritos.
     *
     * @param key código del estudiante a eliminar
     */
    public void removeStudent(String key) {
        students.remove(key);
    }

    /**
     * Verifica si el curso alcanzó su capacidad máxima.
     *
     * @return {@code true} si el curso está lleno, de lo contrario {@code false}
     */
    public Boolean isFull() {
        return students.size() == maxCapacity;
    }

    /**
     * Retorna una representación en texto del curso.
     *
     * @return cadena con nombre, capacidad y código
     */
    @Override
    public String toString() {
        return String.format("""
                Nombre del curso: %s 
                Capacidad: %d
                Codigo: %s
                """, name, maxCapacity, code);
    }
}
