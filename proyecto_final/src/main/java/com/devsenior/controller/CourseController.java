package com.devsenior.controller;

import java.awt.HeadlessException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.devsenior.exception.CourseNotFoundException;
import com.devsenior.exception.InvalidDataException;
import com.devsenior.exception.StudentNotFoundException;
import com.devsenior.model.Course;
import com.devsenior.model.Student;
import com.devsenior.service.CourseService;

public class CourseController {
    private final CourseService courseservice;

    public CourseController() {
        courseservice = new CourseService();
    }

    public void createCourse() {
        try {
            JTextField nametext = new JTextField(10);
            JTextField capacitytext = new JTextField(10);
            JPanel panel = new JPanel();
            panel.add(new JLabel("Nombre:"));
            panel.add(nametext);
            panel.add(new JLabel("Capacidad:"));
            panel.add(capacitytext);
            int option = JOptionPane.showConfirmDialog(null, panel, "INGRESE LOS DATOS DEL CURSO",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                String name = nametext.getText();
                String capacity = capacitytext.getText();
                Course course = courseservice.createCourse(name, capacity);
                JOptionPane.showMessageDialog(null,
                        "Se ha creado el curso correctamente, con el codigo: " + course.getCode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public Course searchCourse() throws CourseNotFoundException {
        try {
            String options[] = { "Nombre", "Codigo" };
            int option = JOptionPane.showOptionDialog(null, "¿Como desea buscar el curso?",
                    "BUSQUEDA CURSO", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
                    options[0]);
            if (option == 0) {
                String name = JOptionPane.showInputDialog(null, "Digite el nombre del curso",
                        "NOMBRE DEL CURSO", JOptionPane.PLAIN_MESSAGE);
                Course course = courseservice.findCourseByName(name);
                JOptionPane.showMessageDialog(null, "Curso encontrado:\n " + course, "CURSO ENCONTRADO",
                        JOptionPane.INFORMATION_MESSAGE);
                return course;
            } else {
                String code = JOptionPane.showInputDialog(null, "Digite el codigo del curso",
                        "CODIGO DEL CURSO", JOptionPane.PLAIN_MESSAGE);
                Course course = courseservice.findCourseByCode(code);
                JOptionPane.showMessageDialog(null, "Curso encontrado: " + course, "CURSO ENCONTRADO",
                        JOptionPane.INFORMATION_MESSAGE);
                return course;
            }
        } catch (CourseNotFoundException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            throw new CourseNotFoundException("Curso no encontrado");
        }
    }

    public void listCourses() {
        try {
            String message = "";
            for (Course course : courseservice.listCourses().values()) {
                message += course.toString() + "\n" + "\n";
            }
            JOptionPane.showMessageDialog(null, message, "CURSOS ACTUALES", JOptionPane.INFORMATION_MESSAGE);
        } catch (CourseNotFoundException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void changeNameCourse() {
        try {
            Course course = searchCourse();
            String name = JOptionPane.showInputDialog(null, "Digite el nuevo nombre del curso", "CAMBIO DE NOMBRE",
                    JOptionPane.PLAIN_MESSAGE);
            courseservice.setNewName(course, name);
            JOptionPane.showMessageDialog(null, "Nombre cambiado con exito", "NOMBRE CAMBIADO",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (CourseNotFoundException | HeadlessException | InvalidDataException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void changeCapacityCourse() {
        try {
            Course course = searchCourse();
            String capacity = JOptionPane.showInputDialog(null, "Digite cual sera la nueva capacidad del curso",
                    "NUEVA CAPACIDAD DEL CURSO",
                    JOptionPane.PLAIN_MESSAGE);
            courseservice.setNewCapacity(course, capacity);
            JOptionPane.showMessageDialog(null, "Capacidad cambiada con eito", "CAPACIDAD CAMBIADA",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (CourseNotFoundException | InvalidDataException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void enrollStudent(StudentController studentcontroller) {
        try {
            Student student = studentcontroller.searchStudent();
            String message[] = new String[courseservice.listCourses().size()];
            int i = 0;
            for (Course course : courseservice.listCourses().values()) {
                message[i] = course.getName();
                i++;
            }
            int option = JOptionPane.showOptionDialog(null, "¿A que curso desea inscribir a el estudiante?",
                    "INSCRIPCION CURSO", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, message,
                    message[0]);
            Course course = new Course();
            for (Course courses : courseservice.listCourses().values()) {
                if (courses.getName().equals(message[option])) {
                    course = courses;
                }
            }
            courseservice.enrrolStudent(student, course);
            JOptionPane.showMessageDialog(null, "El estudiante ha sido inscrito correctamente en el curso",
                    "INSCRIPCION", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeStudentFromCourse() {
        try {
            Course course = searchCourse();
            String student[] = new String[course.getStudents().size()];
            int i = 0;
            for (Student students : course.getStudents().values()) {
                student[i] = students.getName();
                i++;
            }
            int option = JOptionPane.showOptionDialog(null, "¿Qué estudiante desea eliminar?",
                    "ESTUDIANTES DEL CURSO", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, student,
                    student[0]);
            Student removeStudent = new Student();
            for (Student students : course.getStudents().values()) {
                if (student[option].equals(students.getName())) {
                    removeStudent = students;
                }
            }
            courseservice.removeStudentFromCourse(course, removeStudent);
            JOptionPane.showMessageDialog(null, "Estudiante removido con éxito", "ÉXITO",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (CourseNotFoundException | StudentNotFoundException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void listStudentsByCourse() {
        try {
            Course course = searchCourse();
            String message = "";
            for (Student student : courseservice.listStudentsByCourse(course).values()) {
                message += student.toString() + "\n\n";
            }
            JOptionPane.showMessageDialog(null, message, "ESTUDIANTES ACTUALES DEL CURSO",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (StudentNotFoundException | CourseNotFoundException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
