package com.devsenior.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.devsenior.exception.CourseFullException;
import com.devsenior.exception.CourseNotFoundException;
import com.devsenior.exception.InvalidDataException;
import com.devsenior.exception.StudentNotFoundException;
import com.devsenior.model.Course;
import com.devsenior.model.Student;
import com.devsenior.util.Validator;


/**
 * Servicio encargado de gestionar los cursos del sistema.
 * Permite crear, modificar, eliminar y buscar cursos, así como gestionar
 * la inscripción y desinscripción de estudiantes en los mismos.
 */
public class CourseService {
    private static final Logger logger = LogManager.getLogger(CourseService.class);


    private final Map<String, Course> courses;

    /**
     * Crea un nuevo servicio de cursos con un repositorio vacío.
     */
    public CourseService() {
        courses = new HashMap<>();
    }

    /**
     * Agrega un curso al sistema si no existe previamente.
     *
     * @param course curso a agregar
     * @throws Exception si el curso ya está registrado
     */
    public void addCourse(Course course) throws Exception {
        if (!courses.containsKey(course.getCode()) && !courses.containsValue(course)) {
            courses.put(course.getCode(), course);
        } else {
            logger.warn("El curso no pudo ser añadido");
            throw new Exception("El curso ya se encuentra registrado");
        }
    }

    /**
     * Crea un nuevo curso validando nombre y capacidad.
     *
     * @param name     nombre del curso
     * @param capacity capacidad máxima en formato String
     * @return el curso creado
     * @throws Exception            si ocurre un error al registrar el curso
     * @throws InvalidDataException si el nombre o la capacidad no son válidos
     */
    public Course createCourse(String name, String capacity) throws Exception, InvalidDataException {
        if (!Validator.validateName(name)) {
            logger.warn("No se pudo crear el curso debido a nombre no valido");
            throw new InvalidDataException("Nombre no valido");
        } else if (!Validator.validateCapacity(capacity)) {
            logger.warn("No se pudo crear el curso debido a que se digito una capacidad invalida");
            throw new InvalidDataException("capacidad no valida");
        } else if (!isNameAviable(name)) {
            logger.warn("No se pudo crear el curso debido a que su nombre ya esta en uso");
            throw new InvalidDataException("Nombre ya en uso");
        } else {
            String id = Validator.createId();
            while (!hasNotCourses()) {
                Boolean close = true;
                for (Course courses1 : courses.values()) {
                    if (id.equals(courses1.getCode())) {
                        id = Validator.createId();
                        close = false;
                    }
                }
                if (close) {
                    break;
                }
            }
            Course course = new Course(id, name, Integer.parseInt(capacity));
            addCourse(course);
            logger.info("Se creo el curso {} con el codigo {}", course.getName(), course.getCode());
            return course;
        }
    }

    /**
     * Lista todos los cursos registrados.
     *
     * @return mapa de cursos registrados
     * @throws CourseNotFoundException si no existen cursos registrados
     */
    public Map<String, Course> listCourses() throws CourseNotFoundException {
        if (hasNotCourses()) {
            logger.warn("No existen actualmente datos de ningun curso");
            throw new CourseNotFoundException("No existen datos de ningun curso actualmente");
        }
        return courses;
    }

    /**
     * Busca un curso por su código.
     *
     * @param code código del curso
     * @return curso encontrado
     * @throws CourseNotFoundException si no existe el curso
     */
    public Course findCourseByCode(String code) throws CourseNotFoundException {
        if (hasNotCourses()) {
            logger.warn("No existen actualmente datos de ningun curso");
            throw new CourseNotFoundException("No existen datos de ningun curso actualmente");
        } else if (courses.containsKey(code)) {
            logger.info("Se busco el curso {} con el codigo {}", courses.get(code).getName(), courses.get(code).getCode());
            return courses.get(code);
        } else {
            logger.warn("No se encontro a ningun curso ");
            throw new CourseNotFoundException("No se encontro ningun curso con ese codigo ");
        }
    }

    /**
     * Busca un curso por su nombre.
     *
     * @param name nombre del curso
     * @return curso encontrado
     * @throws CourseNotFoundException si no existe el curso
     */
    public Course findCourseByName(String name) throws CourseNotFoundException {
        if (hasNotCourses()) {
            logger.warn("No existen actualmente datos de ningun curso");
            throw new CourseNotFoundException("No existen datos de ningun curso actualmente");
        }
        for (String code : courses.keySet()) {
            if (courses.get(code).getName().equals(name)) {
                logger.info("Se busco el curso {} con el codigo {}", courses.get(code).getName(), courses.get(code).getCode());
                return courses.get(code);
            }
        }
        logger.warn("No se encontro a ningun curso con ese nombre ");
        throw new CourseNotFoundException("No se encontro ningun curso con ese nombre");
    }

    /**
     * Inscribe un estudiante en un curso.
     *
     * @param student estudiante a inscribir
     * @param course  curso en el cual inscribir
     * @throws Exception             si el estudiante ya está inscrito
     * @throws CourseFullException   si el curso ya está en su capacidad máxima
     * @throws CourseNotFoundException si no existen cursos
     */
    public void enrrolStudent(Student student, Course course) throws Exception, CourseFullException, CourseNotFoundException {
        if (course.isFull()) {
            logger.warn("El curso ya alcanzo su maxima capacidad ");
            throw new CourseFullException("El curso ya esta en su maxima capacidad ");
        } else if (hasNotCourses()) {
            logger.warn("No existen actualmente datos de ningun curso");
            throw new CourseNotFoundException("No existen datos de ningun curso actualmente");
        } else if (student.getCourses().containsValue(course)) {
            logger.warn("El estudiante {} con codigo {} actualmente ya esta inscrito en el curso {} con el codigo {}", student.getName(), student.getCode(), course.getName(), course.getCode());
            throw new Exception("El estudiante ya esta inscrito en este curso ");
        } else if (course.getStudents().containsValue(student)) {
            logger.warn("El curso {} con el codigo {} ya tiene actualmente inscrito a el estudiante {} con codigo {}", course.getName(), course.getCode(), student.getName(), student.getCode());
            throw new Exception("El curso ya tiene actualmente inscrito a el estudiante");
        } else {
            student.addCourse(course, course.getCode());
            course.addStudent(student, student.getCode());
            logger.info("El estudiante {} con codigo {} se inscribio en el curso {} con el codigo {}", student.getName(), student.getCode(), course.getName(), course.getCode());
        }
    }

    /**
     * Remueve un estudiante de un curso.
     *
     * @param course  curso del cual remover al estudiante
     * @param student estudiante a remover
     * @throws StudentNotFoundException si el estudiante no está inscrito en el curso
     * @throws CourseNotFoundException  si el curso no existe
     */
    public void removeStudentFromCourse(Course course, Student student) throws StudentNotFoundException, CourseNotFoundException {
        if (hasNotCourses()) {
            logger.warn("No existen actualmente datos de ningun curso");
            throw new CourseNotFoundException("No existen datos de ningun curso actualmente");
        } else if (!course.getStudents().containsValue(student)) {
            logger.warn("El curso {} con el codigo {} no cuenta con el estudiante {} con el codigo {} inscrito en el", course.getName(), course.getCode(), student.getName(), student.getCode());
            throw new StudentNotFoundException("El curso no tiene inscrito actualmente a el estudiante");
        } else {
            student.removeCourse(course.getCode());
            course.removeStudent(student.getCode());
            logger.info("El curso {} con codigo {} removio a el estudiante {} con el codigo {} de su lista de estudiantes y viceversa", course.getName(), course.getCode(), student.getName(), student.getCode());
        }
    }

    /**
     * Cambia el nombre de un curso.
     *
     * @param course curso al cual cambiar el nombre
     * @param name   nuevo nombre
     * @throws InvalidDataException   si el nombre no es válido
     * @throws CourseNotFoundException si el curso no existe
     */
    public void setNewName(Course course, String name) throws InvalidDataException, CourseNotFoundException {
        if (hasNotCourses()) {
            logger.warn("No existen actualmente datos de ningun curso");
            throw new CourseNotFoundException("No existen datos de ningun curso actualmente");
        } else if (!Validator.validateName(name)) {
            logger.warn("No se pudo cambiar el nombre del curso {} con el codigo {} debido a que el nombre no es valido", course.getName(), course.getCode());
            throw new InvalidDataException("Nombre no valido");
        } else if (!courses.containsValue(course)) {
            logger.warn("Curso no encontrado para cambiar el nombre");
            throw new CourseNotFoundException("No se encontro ningun curso");
        } else if (!isNameAviable(name)) {
            logger.warn("El nombre ya esta en uso");
            throw new InvalidDataException("Nombre ya esta siendo usado");
        } else {
            course.setName(name);
            logger.info("El curso {} con codigo {} cambio su nombre", course.getName(), course.getCode());
        }
    }

    /**
     * Cambia la capacidad máxima de un curso.
     *
     * @param course   curso al cual cambiar la capacidad
     * @param capacity nueva capacidad en formato String
     * @throws InvalidDataException   si la capacidad no es válida
     * @throws CourseNotFoundException si el curso no existe
     */
    public void setNewCapacity(Course course, String capacity) throws InvalidDataException, CourseNotFoundException {
        if (hasNotCourses()) {
            logger.warn("No existen actualmente datos de ningun curso");
            throw new CourseNotFoundException("No existen datos de ningun curso actualmente");
        } else if (!Validator.validateCapacity(capacity)) {
            logger.warn("No se pudo crear el curso debido a que se digito una capacidad invalida");
            throw new InvalidDataException("capacidad no valida");
        } else if (!courses.containsValue(course)) {
            logger.warn("Curso no encontrado para cambiar el nombre");
            throw new CourseNotFoundException("No se encontro ningun curso");
        } else if (course.getStudents().size() > Integer.parseInt(capacity)) {
            logger.warn("La nueva capacidad que se quiere implementar a el curso {} con el codigo {}, no se puede implementar debido a que tiene demasiados estudiantes inscritos", course.getName(), course.getCode());
            throw new InvalidDataException("Capacidad no valida, actualmente el curso tiene " + course.getStudents().size() + " estudiantes, remover estudiantes y volver a intentar");
        } else if (course.getMaxCapacity() == Integer.parseInt(capacity)) {
            logger.warn("El curso {} con el codigo {} ya posee esa capacidad", course.getName(), course.getCode());
            throw new InvalidDataException("El curso ya posee actualmente esa capacidad");
        } else {
            course.setMaxCapacity(Integer.parseInt(capacity));
            logger.info("El curso {} con el codigo {} cambio su capacidad", course.getName(), course.getCode());
        }
    }

    /**
     * Lista todos los estudiantes inscritos en un curso.
     *
     * @param course curso del cual obtener estudiantes
     * @return mapa de estudiantes inscritos
     * @throws StudentNotFoundException si no hay estudiantes inscritos
     * @throws CourseNotFoundException  si el curso no existe
     */
    public Map<String, Student> listStudentsByCourse(Course course) throws StudentNotFoundException, CourseNotFoundException {
        if (hasNotCourses()) {
            logger.warn("No existen actualmente datos de ningun curso");
            throw new CourseNotFoundException("No existen datos de ningun curso actualmente");
        } else if (!courses.containsValue(course)) {
            logger.warn("Curso no encontrado para mostrar sus estudiantes");
            throw new CourseNotFoundException("No se encontro ningun curso");
        } else if (course.getStudents().isEmpty()) {
            logger.warn("El curso {} con el codigo {} no tiene estudiantes inscritos actualmente", course.getName(), course.getCode());
            throw new StudentNotFoundException("El curso no tiene estudiantes inscritos actualmente");
        }
        return course.getStudents();
    }

    /**
     * Verifica si un nombre de curso está disponible.
     *
     * @param name nombre a verificar
     * @return true si está disponible, false si ya está en uso
     */
    public Boolean isNameAviable(String name) {
        for (Course course : courses.values()) {
            if (course.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica si no existen cursos registrados.
     *
     * @return true si no hay cursos, false en caso contrario
     */
    public Boolean hasNotCourses() {
        return courses.isEmpty();
    }

}