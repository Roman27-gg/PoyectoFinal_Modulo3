package com.devsenior.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.devsenior.exception.CourseNotFoundException;
import com.devsenior.exception.InvalidDataException;
import com.devsenior.exception.StudentNotFoundException;
import com.devsenior.model.Course;
import com.devsenior.model.Student;
import com.devsenior.util.Validator;

public class StudentService {
    private static final Logger logger = LogManager.getLogger(StudentService.class);

    private final Map<String, Student> students;

    public StudentService (){
        students = new HashMap<>();
    }

    public void addStudent(Student student) throws Exception{
        if (!students.containsKey(student.getCode()) && !students.containsValue(student)) {
            students.put(student.getCode(), student);
        } else {
            logger.warn("No se pudo añadir el estudiante");
            throw new Exception("El estudiante no fue añadido debido a que ya existe");
        }
    }

    public Student createStudent(String name, String email) throws Exception, InvalidDataException{
        if(!Validator.validateName(name)){
            logger.warn("No se pudo crear el estudiante debido a nombre no valido");
            throw new InvalidDataException("Nombre no valido");
        } else if (!Validator.validateEmail(email)){
            logger.warn("No se pudo crear el estudiante debido a email no valido");
            throw new InvalidDataException("Email no valido");
        } else if (!isNameAvaiable(name)) {
            logger.warn("No se pudo crear el estudiante debido a que el nombre ya esta en uso");
            throw new InvalidDataException("Nombre ya en uso");
        } else if (isEmailAvaiable(email)) {
            logger.warn("No se pudo crear el estudiante debido a que el email ya esta en uso");
            throw new InvalidDataException("Email ya en uso");
        } else {
            String id= Validator.createId();
            do {
                if (!id.equals(findStudentById(id).getCode())) {
                    break;
                }
                id= Validator.createId();
            } while (true);
            Student student = new Student(id, name, email);
            addStudent(student);
            logger.info("Se creo el estudiante {} con el codigo {} ",student.getName(), student.getCode());
            return student;
        }
    }

    public Student findStudentById(String id) throws StudentNotFoundException{
        if (hasNotStudents()) {
            logger.warn("No existen actualmente datos de ningun estudiante");
            throw new StudentNotFoundException("No existen datos de ningun estudiante actualmente");
        }else if (students.containsKey(id)) {
            logger.info("Se busco el estudiante {} con el codigo {}", students.get(id).getName(), students.get(id).getCode());
            return students.get(id);
        } else {
            logger.warn("No se encontro a ningun estudiante ");
            throw new StudentNotFoundException("No se encontro ningun estudiante con ese codigo");
        }
    }

    public Student findStudentByName(String name) throws StudentNotFoundException{
        if (hasNotStudents()) {
            logger.warn("No existen actualmente datos de ningun estudiante");
            throw new StudentNotFoundException("No existen datos de ningun estudiante actualmente");
        }
        for (String code : students.keySet()) {
            if (students.get(code).getName().equals(name)) {
                logger.info("Se busco el estudiante {} con el codigo {}", students.get(code).getName(), students.get(code).getCode());
                return students.get(code);
            }
        }
        logger.warn("No se encontro a ningun estudiante con ese nombre ");
        throw new StudentNotFoundException("No se encontro ningun estudiante con ese nombre ");
    }

    public Map<String,Course> listCoursesByStudent(Student student) throws StudentNotFoundException, CourseNotFoundException{
        if (hasNotStudents()) {
            logger.warn("No existen actualmente datos de ningun estudiante");
            throw new StudentNotFoundException("No existen datos de ningun estudiante actualmente");
        } else if (!students.containsValue(student)) {
            logger.warn("Estudiante no encontrado para mostrar sus cursos");
            throw new StudentNotFoundException("No se encontro ningun estudiante ");
        } else if (student.getCourses().isEmpty()){
            logger.warn("El estudiante no esta inscrito a ningun curo actualmente ");
            throw new CourseNotFoundException("El estudiante no se encuentra inscrito a ningun curso actualmente"); 
        }
        return student.getCourses();
    }

    public Map<String,Student> listStudents() throws StudentNotFoundException{
        if (hasNotStudents()) {
            logger.warn("No existen actualmente datos de ningun estudiante");
            throw new StudentNotFoundException("No existen datos de ningun estudiante actualmente");
        }
        return students;
    }

    public void removeCourseFromStudent(Student student, Course course) throws StudentNotFoundException, CourseNotFoundException{
        if (hasNotStudents()) {
            logger.warn("No existen actualmente datos de ningun estudiante");
            throw new StudentNotFoundException("No existen datos de ningun estudiante actualmente");
        } else if(!student.getCourses().containsValue(course)){
            logger.warn("El estudiante {} con el codigo {} no se encuentra inscrito en el curso {} con codigo {}",student.getName(),student.getCode(),course.getName(),course.getCode());
            throw new CourseNotFoundException("El estudiante no esta inscrito actualmente en ese curso");
        } else {
            course.removeStudent(student.getCode());
            student.removeCourse(course.getCode());
            logger.info("El estudiante {} con el codigo {} removio el curso {} con codigo {} de su lista de cursos y viceversa",student.getName(),student.getCode(),course.getName(),course.getCode());
        }
    }

    public Boolean isNameAvaiable(String name){
        for (Student student : students.values()) {
            if (student.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public Boolean isEmailAvaiable(String email){
        for (Student student : students.values()) {
            if (student.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

     public boolean hasNotStudents() {
        return students.isEmpty();
    }
}
