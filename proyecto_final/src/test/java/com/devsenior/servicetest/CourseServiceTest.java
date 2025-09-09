package com.devsenior.servicetest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.devsenior.exception.CourseFullException;
import com.devsenior.exception.CourseNotFoundException;
import com.devsenior.exception.InvalidDataException;
import com.devsenior.exception.StudentNotFoundException;
import com.devsenior.model.Course;
import com.devsenior.model.Student;
import com.devsenior.service.CourseService;

public class CourseServiceTest {

    @Test
    void testAddCourseSuccess() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("639487", "Matematicas", 2);

        service.addCourse(course);

        Map<String, Course> all = service.listCourses();
        assertTrue(all.containsKey(course.getCode()));
    }

    @Test
    void testAddCourseDuplicate() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("000333", "Matematicas", 2);

        service.addCourse(course);

        Exception ex = assertThrows(Exception.class, () -> {
            service.addCourse(course);
        });
        assertEquals("El curso ya se encuentra registrado", ex.getMessage());
    }

    @Test
    void testCreateCourseSuccess() throws Exception {
        CourseService service = new CourseService();

        Course course = service.createCourse("Programacion", "30");

        assertNotNull(course);
        assertEquals("Programacion", course.getName());
    }

    @Test
    void testCreateCourseInvalidName() {
        CourseService service = new CourseService();

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            service.createCourse("", "30");
        });
        assertEquals("Nombre no valido", ex.getMessage());
    }

    @Test
    void testCreateCourseInvalidCapacity() {
        CourseService service = new CourseService();

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            service.createCourse("Algoritmos", "capacidad-no-valida");
        });
        assertEquals("capacidad no valida", ex.getMessage());
    }

    @Test
    void testCreateCourseNameInUse() throws Exception {
        CourseService service = new CourseService();

        Course first = service.createCourse("Calculo", "10");
        assertNotNull(first);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            service.createCourse("Calculo", "20");
        });
        assertEquals("Nombre ya en uso", ex.getMessage());
    }

    @Test
    void testListCourses_noCourses() {
        CourseService service = new CourseService();

        CourseNotFoundException ex = assertThrows(CourseNotFoundException.class, () -> {
            service.listCourses();
        });
        assertEquals("No existen datos de ningun curso actualmente", ex.getMessage());
    }

    @Test
    void testFindCourseByCodeNotFound() throws Exception {
        CourseService service = new CourseService();
        Course c = new Course("197364", "Matematicas", 2);
        service.addCourse(c);

        CourseNotFoundException ex = assertThrows(CourseNotFoundException.class, () -> {
            service.findCourseByCode("XXXX");
        });
        assertEquals("No se encontro ningun curso con ese codigo ", ex.getMessage());
    }

    @Test
    void testFindCourseByNameNotFound() throws Exception {
        CourseService service = new CourseService();
        Course c = new Course("123456", "Matematicas", 2);
        service.addCourse(c);

        CourseNotFoundException ex = assertThrows(CourseNotFoundException.class, () -> {
            service.findCourseByName("Nombre Inexistente");
        });
        assertEquals("No se encontro ningun curso con ese nombre", ex.getMessage());
    }

    @Test
    void testEnrollStudentSuccess() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("231654", "Historia", 2);
        Student student = new Student("123654", "Juan", "juan@hotmail.com");

        service.addCourse(course);
        service.enrrolStudent(student, course);

        assertTrue(student.getCourses().containsValue(course));
        assertTrue(course.getStudents().containsValue(student));
    }

    @Test
    void testEnrollStudentCourseFull() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("951234", "Quimica", 1);
        Student s1 = new Student("546694", "Ana", "ana@gmail.com");
        Student s2 = new Student("654987", "Pedro", "pedro@gmail.com");

        service.addCourse(course);
        service.enrrolStudent(s1, course);


        CourseFullException ex = assertThrows(CourseFullException.class, () -> {
            service.enrrolStudent(s2, course);
        });
        assertEquals("El curso ya esta en su maxima capacidad ", ex.getMessage());
    }

    @Test
    void testEnrollStudentAlreadyEnrolledByStudent() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("231564", "Filosofia", 2);
        Student s = new Student("159876", "Lucia", "lucia@gmail.com");

        service.addCourse(course);
        service.enrrolStudent(s, course);

        Exception ex = assertThrows(Exception.class, () -> {
            service.enrrolStudent(s, course);
        });
        assertEquals("El estudiante ya esta inscrito en este curso ", ex.getMessage());
    }

    @Test
    void testRemoveStudentFromCourseNotEnrolled() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("324569", "Arte", 2);
        Student s = new Student("502364", "Mario", "mario@hotmail.com");

        service.addCourse(course);

        StudentNotFoundException ex = assertThrows(StudentNotFoundException.class, () -> {
            service.removeStudentFromCourse(course, s);
        });
        assertEquals("El curso no tiene inscrito actualmente a el estudiante", ex.getMessage());
    }

    @Test
    void testRemoveStudentFromCourseSuccess() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("000123", "Ingles", 2);
        Student s = new Student("147852", "Paula", "paula@gmail.com");

        service.addCourse(course);
        service.enrrolStudent(s, course);

        service.removeStudentFromCourse(course, s);

        assertFalse(course.getStudents().containsValue(s));
        assertFalse(s.getCourses().containsValue(course));
    }

    @Test
    void testSetNewNameInvalid() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("123456", "Fisica", 2);

        service.addCourse(course);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            service.setNewName(course, "");
        });
        assertEquals("Nombre no valido", ex.getMessage());
    }

    @Test
    void testSetNewNameCourseNotFound() throws Exception {
        CourseService service = new CourseService();
        Course existing = new Course("111222", "Historia", 2);
        Course notAdded = new Course("123456", "Geografia", 2);

        service.addCourse(existing);

        CourseNotFoundException ex = assertThrows(CourseNotFoundException.class, () -> {
            service.setNewName(notAdded, "Geo Avanzada");
        });
        assertEquals("No se encontro ningun curso", ex.getMessage());
    }

    @Test
    void testSetNewNameSuccess() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("753918", "Literatura", 2);

        service.addCourse(course);
        service.setNewName(course, "Literatura Avanzada");

        assertEquals("Literatura Avanzada", course.getName());
    }

    @Test
    void testSetNewCapacityInvalidFormat() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("123698", "Dibujo", 2);

        service.addCourse(course);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            service.setNewCapacity(course, "no-numerico");
        });
        assertEquals("capacidad no valida", ex.getMessage());
    }

    @Test
    void testSetNewCapacityLessThanStudents() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("123456", "Musica", 5);
        Student s1 = new Student("777888", "A", "a@example.com");
        Student s2 = new Student("111111", "B", "b@example.com");

        service.addCourse(course);
        service.enrrolStudent(s1, course);
        service.enrrolStudent(s2, course);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            service.setNewCapacity(course, "1");
        });

        String expected = "Capacidad no valida, actualmente el curso tiene "
                + course.getStudents().size()
                + " estudiantes, remover estudiantes y volver a intentar";
        assertEquals(expected, ex.getMessage());
    }

    @Test
    void testSetNewCapacitySameAsCurrent() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("123456", "Programacion", 2);

        service.addCourse(course);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            service.setNewCapacity(course, "2");
        });
        assertEquals("El curso ya posee actualmente esa capacidad", ex.getMessage());
    }

    @Test
    void testListStudentsByCourseEmpty() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("723569", "Estadística", 2);

        service.addCourse(course);

        StudentNotFoundException ex = assertThrows(StudentNotFoundException.class, () -> {
            service.listStudentsByCourse(course);
        });
        assertEquals("El curso no tiene estudiantes inscritos actualmente", ex.getMessage());
    }

    @Test
    void testListStudentsByCourseSuccess() throws Exception {
        CourseService service = new CourseService();
        Course course = new Course("555555", "Teatro", 2);
        Student s = new Student("123647", "Nora", "nora@example.com");

        service.addCourse(course);
        service.enrrolStudent(s, course);

        Map<String, Student> students = service.listStudentsByCourse(course);
        assertEquals(1, students.size());
        assertTrue(students.containsKey(s.getCode()));
    }

}
