package com.devsenior.servicetest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.devsenior.exception.CourseNotFoundException;
import com.devsenior.exception.InvalidDataException;
import com.devsenior.exception.StudentNotFoundException;
import com.devsenior.model.Course;
import com.devsenior.model.Student;
import com.devsenior.service.StudentService;
import com.devsenior.util.Validator;

public class StudentServiceTest {

    @Test
    void addStudentTest() {
        StudentService studentservice = new StudentService();
        Student student = new Student(Validator.createId(), "Andres Gomez", "Gomez@gmail.com");
        try {
            studentservice.addStudent(student);
        } catch (Exception ex) {
        }
        Exception e = assertThrows(Exception.class, () -> {
            studentservice.addStudent(student);
        });
        assertEquals("El estudiante no fue añadido debido a que ya existe", e.getMessage());
    }

    @Test
    void testCreateStudent_ok() throws Exception {
        StudentService service = new StudentService();
        Student s = service.createStudent("Maria Lopez", "maria@example.com");

        assertEquals("Maria Lopez", s.getName());
        assertEquals("maria@example.com", s.getEmail());
    }

    @Test
    void testCreateStudent_nombreInvalido() {
        StudentService service = new StudentService();
        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            service.createStudent("", "test@example.com");
        });
        assertEquals("Nombre no valido", ex.getMessage());
    }

    @Test
    void testCreateStudent_emailInvalido() {
        StudentService service = new StudentService();
        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            service.createStudent("Carlos", "correo-malo");
        });
        assertEquals("Email no valido", ex.getMessage());
    }

    @Test
    void testFindStudentById_ok() throws Exception {
        StudentService service = new StudentService();
        Student student = service.createStudent("Juan Perez", "juan@example.com");

        Student found = service.findStudentById(student.getCode());
        assertEquals(student, found);
    }

    @Test
    void testFindStudentById_notFound() throws Exception {
        StudentService service = new StudentService();
        service.createStudent("Juan Ramon", "R@gmail.com");
        StudentNotFoundException ex = assertThrows(StudentNotFoundException.class, () -> {
            service.findStudentById("id-falso");
        });
        assertEquals("No se encontro ningun estudiante con ese codigo", ex.getMessage());
    }

    @Test
    void testFindStudentByName_ok() throws Exception {
        StudentService service = new StudentService();
        Student student = service.createStudent("Juan Perez", "juan@example.com");

        Student found = service.findStudentByName("Juan Perez");
        assertEquals(student, found);
    }

    @Test
    void testFindStudentByName_notFound() throws Exception {
        StudentService service = new StudentService();
        service.createStudent("Otro", "otro@example.com");

        StudentNotFoundException ex = assertThrows(StudentNotFoundException.class, () -> {
            service.findStudentByName("Nombre Falso");
        });
        assertEquals("No se encontro ningun estudiante con ese nombre ", ex.getMessage());
    }

    @Test
    void testListStudents_ok() throws Exception {
        StudentService service = new StudentService();
        Student student = service.createStudent("Ana", "ana@example.com");

        Map<String, Student> map = service.listStudents();
        assertTrue(map.containsKey(student.getCode()));
    }

    @Test
    void testSetNewName_ok() throws Exception {
        StudentService service = new StudentService();
        Student student = service.createStudent("Juan", "juan@example.com");

        service.setNewName(student, "Juanito");
        assertEquals("Juanito", student.getName());
    }

    @Test
    void testSetNewName_invalido() throws Exception {
        StudentService service = new StudentService();
        Student student = service.createStudent("Juan", "juan@example.com");

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            service.setNewName(student, "");
        });
        assertEquals("Nombre no valido", ex.getMessage());
    }

    @Test
    void testSetNewEmail_ok() throws Exception {
        StudentService service = new StudentService();
        Student student = service.createStudent("Juan", "juan@example.com");

        service.setNewEmail(student, "nuevo@example.com");
        assertEquals("nuevo@example.com", student.getEmail());
    }

    @Test
    void testSetNewEmail_invalido() throws Exception {
        StudentService service = new StudentService();
        Student student = service.createStudent("Juan", "juan@example.com");

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            service.setNewEmail(student, "correo-malo");
        });
        assertEquals("Nombre no valido", ex.getMessage()); // mensaje definido en tu método
    }

    @Test
    void testRemoveCourseFromStudent_cursoNoInscrito() throws Exception {
        StudentService service = new StudentService();
        Student student = service.createStudent("Pedro", "pedro@example.com");
        Course c = new Course("123", "POO", 30);

        CourseNotFoundException ex = assertThrows(CourseNotFoundException.class, () -> {
            service.removeCourseFromStudent(student, c);
        });
        assertEquals("El estudiante no esta inscrito actualmente en ese curso", ex.getMessage());
    }

}
