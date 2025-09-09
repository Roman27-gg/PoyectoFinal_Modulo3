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

/**
 * Servicio encargado de la gestión de estudiantes dentro del sistema.
 * Permite registrar, buscar, modificar y listar estudiantes, así como
 * administrar su relación con cursos.
 */
public class StudentService {
    private static final Logger logger = LogManager.getLogger(StudentService.class);

    private final Map<String, Student> students;

    /**
     * Constructor que inicializa el contenedor de estudiantes.
     */
    public StudentService() {
        students = new HashMap<>();
    }

    /**
     * Agrega un estudiante al sistema si no existe previamente.
     *
     * @param student objeto Student a agregar
     * @throws Exception si el estudiante ya existe en el sistema
     */
    public void addStudent(Student student) throws Exception {
        if (!students.containsKey(student.getCode()) && !students.containsValue(student)) {
            students.put(student.getCode(), student);
        } else {
            logger.warn("No se pudo añadir el estudiante");
            throw new Exception("El estudiante no fue añadido debido a que ya existe");
        }
    }

    /**
     * Crea un nuevo estudiante validando nombre y correo electrónico.
     *
     * @param name  nombre del estudiante
     * @param email correo electrónico del estudiante
     * @return el estudiante creado
     * @throws Exception           si ocurre un error al agregar el estudiante
     * @throws InvalidDataException si los datos son inválidos (nombre/email duplicado o no válido)
     */
    public Student createStudent(String name, String email) throws Exception, InvalidDataException {
        if (!Validator.validateName(name)) {
            logger.warn("No se pudo crear el estudiante debido a nombre no valido");
            throw new InvalidDataException("Nombre no valido");
        } else if (!Validator.validateEmail(email)) {
            logger.warn("No se pudo crear el estudiante debido a email no valido");
            throw new InvalidDataException("Email no valido");
        } else if (!isNameAvaiable(name)) {
            logger.warn("No se pudo crear el estudiante debido a que el nombre ya esta en uso");
            throw new InvalidDataException("Nombre ya en uso");
        } else if (!isEmailAvaiable(email)) {
            logger.warn("No se pudo crear el estudiante debido a que el email ya esta en uso");
            throw new InvalidDataException("Email ya en uso");
        } else {
            String id = Validator.createId();
            while (!hasNotStudents()) {
                Boolean close = true;
                for (Student student1 : students.values()) {
                    if (id.equals(student1.getCode())) {
                        id = Validator.createId();
                        close = false;
                    }
                }
                if (close) {
                    break;
                }
            }
            Student student = new Student(id, name, email);
            addStudent(student);
            logger.info("Se creo el estudiante {} con el codigo {} ", student.getName(), student.getCode());
            return student;
        }
    }

    /**
     * Busca un estudiante por su código único.
     *
     * @param id código del estudiante
     * @return el estudiante encontrado
     * @throws StudentNotFoundException si no existe un estudiante con ese código
     */
    public Student findStudentById(String id) throws StudentNotFoundException {
        if (hasNotStudents()) {
            logger.warn("No existen actualmente datos de ningun estudiante");
            throw new StudentNotFoundException("No existen datos de ningun estudiante actualmente");
        } else if (students.containsKey(id)) {
            logger.info("Se busco el estudiante {} con el codigo {}", students.get(id).getName(), students.get(id).getCode());
            return students.get(id);
        } else {
            logger.warn("No se encontro a ningun estudiante ");
            throw new StudentNotFoundException("No se encontro ningun estudiante con ese codigo");
        }
    }

    /**
     * Busca un estudiante por su nombre.
     *
     * @param name nombre del estudiante
     * @return el estudiante encontrado
     * @throws StudentNotFoundException si no existe un estudiante con ese nombre
     */
    public Student findStudentByName(String name) throws StudentNotFoundException {
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

    /**
     * Lista todos los cursos a los que está inscrito un estudiante.
     *
     * @param student estudiante a consultar
     * @return mapa de cursos en los que está inscrito
     * @throws StudentNotFoundException si el estudiante no existe
     * @throws CourseNotFoundException  si el estudiante no está inscrito en ningún curso
     */
    public Map<String, Course> listCoursesByStudent(Student student) throws StudentNotFoundException, CourseNotFoundException {
        if (hasNotStudents()) {
            logger.warn("No existen actualmente datos de ningun estudiante");
            throw new StudentNotFoundException("No existen datos de ningun estudiante actualmente");
        } else if (!students.containsValue(student)) {
            logger.warn("Estudiante no encontrado para mostrar sus cursos");
            throw new StudentNotFoundException("No se encontro ningun estudiante ");
        } else if (student.getCourses().isEmpty()) {
            logger.warn("El estudiante no esta inscrito a ningun curso actualmente ");
            throw new CourseNotFoundException("El estudiante no se encuentra inscrito a ningun curso actualmente");
        }
        return student.getCourses();
    }

    /**
     * Lista todos los estudiantes registrados en el sistema.
     *
     * @return mapa de estudiantes
     * @throws StudentNotFoundException si no hay estudiantes registrados
     */
    public Map<String, Student> listStudents() throws StudentNotFoundException {
        if (hasNotStudents()) {
            logger.warn("No existen actualmente datos de ningun estudiante");
            throw new StudentNotFoundException("No existen datos de ningun estudiante actualmente");
        }
        return students;
    }

    /**
     * Elimina un curso de un estudiante y viceversa.
     *
     * @param student estudiante a modificar
     * @param course  curso a remover
     * @throws StudentNotFoundException si el estudiante no existe
     * @throws CourseNotFoundException  si el curso no está asociado al estudiante
     */
    public void removeCourseFromStudent(Student student, Course course) throws StudentNotFoundException, CourseNotFoundException {
        if (hasNotStudents()) {
            logger.warn("No existen actualmente datos de ningun estudiante");
            throw new StudentNotFoundException("No existen datos de ningun estudiante actualmente");
        } else if (!student.getCourses().containsValue(course)) {
            logger.warn("El estudiante {} con el codigo {} no se encuentra inscrito en el curso {} con codigo {}", student.getName(), student.getCode(), course.getName(), course.getCode());
            throw new CourseNotFoundException("El estudiante no esta inscrito actualmente en ese curso");
        } else {
            course.removeStudent(student.getCode());
            student.removeCourse(course.getCode());
            logger.info("El estudiante {} con el codigo {} removio el curso {} con codigo {} de su lista de cursos y viceversa", student.getName(), student.getCode(), course.getName(), course.getCode());
        }
    }

    /**
     * Cambia el nombre de un estudiante validando que no esté repetido ni sea inválido.
     *
     * @param student estudiante a modificar
     * @param name    nuevo nombre
     * @throws StudentNotFoundException si el estudiante no existe
     * @throws InvalidDataException     si el nuevo nombre no es válido
     */
    public void setNewName(Student student, String name) throws StudentNotFoundException, InvalidDataException {
        if (hasNotStudents()) {
            logger.warn("No existen actualmente datos de ningun estudiante");
            throw new StudentNotFoundException("No existen datos de ningun estudiante actualmente");
        } else if (!Validator.validateName(name)) {
            logger.warn("No se pudo cambiar el nombre del estudiante {} con el codigo {} debido a que el nombre no es valido", student.getName(), student.getCode());
            throw new InvalidDataException("Nombre no valido");
        } else if (!students.containsValue(student)) {
            logger.warn("Estudiante no encontrado para cambiar el nombre");
            throw new StudentNotFoundException("No se encontro ningun estudiante ");
        } else if (!isNameAvaiable(name)) {
            logger.warn("El nombre ya esta en uso");
            throw new InvalidDataException("Nombre ya esta siendo usado");
        } else {
            student.setName(name);
            logger.info("El estudiante {} con codigo {} cambio su nombre", student.getName(), student.getCode());
        }
    }

    /**
     * Cambia el email de un estudiante validando que no esté repetido ni sea inválido.
     *
     * @param student estudiante a modificar
     * @param email   nuevo email
     * @throws StudentNotFoundException si el estudiante no existe
     * @throws InvalidDataException     si el nuevo email no es válido
     */
    public void setNewEmail(Student student, String email) throws StudentNotFoundException, InvalidDataException {
        if (hasNotStudents()) {
            logger.warn("No existen actualmente datos de ningun estudiante");
            throw new StudentNotFoundException("No existen datos de ningun estudiante actualmente");
        } else if (!Validator.validateEmail(email)) {
            logger.warn("No se pudo cambiar el nombre del estudiante {} con el codigo {} debido a que el emaile es no valido", student.getName(), student.getCode());
            throw new InvalidDataException("Nombre no valido");
        } else if (!students.containsValue(student)) {
            logger.warn("Estudiante no encontrado para cambiar el nombre");
            throw new StudentNotFoundException("No se encontro ningun estudiante ");
        } else if (!isEmailAvaiable(email)) {
            logger.warn("El email ya esta en uso");
            throw new InvalidDataException("Email ya esta siendo usado");
        } else {
            student.setEmail(email);
            logger.info("El estudiante {} con codigo {} cambio su email", student.getName(), student.getCode());
        }
    }

    /**
     * Verifica si un nombre está disponible en el sistema.
     *
     * @param name nombre a verificar
     * @return true si el nombre no está en uso, false en caso contrario
     */
    public Boolean isNameAvaiable(String name) {
        for (Student student : students.values()) {
            if (student.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica si un correo electrónico está disponible en el sistema.
     *
     * @param email correo a verificar
     * @return true si el correo no está en uso, false en caso contrario
     */
    public Boolean isEmailAvaiable(String email) {
        for (Student student : students.values()) {
            if (student.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica si no hay estudiantes registrados.
     *
     * @return true si no existen estudiantes, false en caso contrario
     */
    public boolean hasNotStudents() {
        return students.isEmpty();
    }
}
