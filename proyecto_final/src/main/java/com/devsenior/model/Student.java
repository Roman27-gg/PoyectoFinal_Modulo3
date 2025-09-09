package com.devsenior.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa a un estudiante dentro del sistema.
 * Un estudiante tiene un código único, un nombre, un correo electrónico
 * y una lista de cursos en los que está inscrito.
 */
public class Student {

    private final String code;
    private String name;
    private String email;
    private final Map<String, Course> courses;

    /**
     * Crea un nuevo estudiante con código, nombre y correo.
     *
     * @param code  identificador único del estudiante
     * @param name  nombre del estudiante
     * @param email correo electrónico del estudiante
     */
    public Student(String code, String name, String email) {
        this.code = code;
        this.name = name;
        this.email = email;
        courses = new HashMap<>();
    }

    /**
     * Constructor vacío para inicializar un estudiante sin datos.
     * El código será {@code null}.
     */
    public Student() {
        this.code = null;
        this.courses = new HashMap<>();
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

    /**
     * Agrega un curso a la lista de cursos del estudiante.
     *
     * @param course curso a agregar
     * @param key    código del curso
     */
    public void addCourse(Course course, String key) {
        courses.put(key, course);
    }

    /**
     * Elimina un curso de la lista de cursos del estudiante.
     *
     * @param key código del curso a eliminar
     */
    public void removeCourse(String key) {
        courses.remove(key);
    }

    /**
     * Retorna una representación en texto del estudiante.
     *
     * @return cadena con nombre, correo y código
     */
    @Override
    public String toString() {
        return String.format("""
            Nombre: %s
            Email: %s
            Codigo: %s
        """, name, email, code);
    }
}

