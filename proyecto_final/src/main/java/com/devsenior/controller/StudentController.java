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
import com.devsenior.service.StudentService;

/**
 * Controlador para gestionar las operaciones relacionadas con los estudiantes.
 * Se encarga de interactuar con el usuario mediante JOptionPane y de invocar
 * los métodos del {@link StudentService}.
 */
public class StudentController {

    /** Servicio que maneja la lógica de los estudiantes. */
    private final StudentService studentservice;

    /**
     * Constructor de la clase. Inicializa el servicio de estudiantes.
     */
    public StudentController() {
        studentservice = new StudentService();
    }

    /**
     * Permite crear un nuevo estudiante solicitando al usuario el nombre y el email.
     * Muestra un mensaje de éxito o error según corresponda.
     */
    public void createStudent() {
        try {
            JTextField nametext = new JTextField(10);
            JTextField emailtext = new JTextField(10);
            JPanel panel = new JPanel();
            panel.add(new JLabel("Nombre:"));
            panel.add(nametext);
            panel.add(new JLabel("Email:"));
            panel.add(emailtext);
            int option = JOptionPane.showConfirmDialog(null, panel, "INGRESE LOS DATOS DEL ESTUDIANTE",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                String name = nametext.getText();
                String email = emailtext.getText();
                Student student = studentservice.createStudent(name, email);
                JOptionPane.showMessageDialog(null,
                        "Se ha creado el estudiante correctamente, con el codigo: " + student.getCode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite buscar un estudiante por nombre o código.
     * Muestra el estudiante encontrado en un mensaje.
     *
     * @return El estudiante encontrado.
     * @throws StudentNotFoundException Si no se encuentra el estudiante.
     */
    public Student searchStudent() throws StudentNotFoundException {
        try {
            String options[] = { "Nombre", "Codigo" };
            int option = JOptionPane.showOptionDialog(null, "¿Como desea buscar a el estudiante?",
                    "BUSQUEDA ESTUDIANTE", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
                    options[0]);
            if (option == 0) {
                String name = JOptionPane.showInputDialog(null, "Digite el nombre del estudiante",
                        "NOMBRE DEL ESTUDIANTE", JOptionPane.PLAIN_MESSAGE);
                Student student = studentservice.findStudentByName(name);
                JOptionPane.showMessageDialog(null, "Estudiante encontrado:\n " + student, "ESTUDIANTE ENCONTRADO",
                        JOptionPane.INFORMATION_MESSAGE);
                return student;
            } else {
                String code = JOptionPane.showInputDialog(null, "Digite el codigo del estudiante",
                        "CODIGO DEL ESTUDIANTE", JOptionPane.PLAIN_MESSAGE);
                Student student = studentservice.findStudentById(code);
                JOptionPane.showMessageDialog(null, "Estudiante encontrado: " + student, "ESTUDIANTE ENCONTRADO",
                        JOptionPane.INFORMATION_MESSAGE);
                return student;
            }
        } catch (StudentNotFoundException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            throw new StudentNotFoundException("Estudiante no encontrado");
        }
    }

    /**
     * Lista todos los estudiantes existentes mostrando sus detalles en un mensaje.
     */
    public void listStudents() {
        try {
            String message = "";
            for (Student student : studentservice.listStudents().values()) {
                message += student.toString() + "\n\n";
            }
            JOptionPane.showMessageDialog(null, message, "ESTUDIANTES ACTUALES", JOptionPane.INFORMATION_MESSAGE);
        } catch (StudentNotFoundException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite cambiar el nombre de un estudiante existente.
     * Muestra un mensaje de confirmación o error.
     */
    public void changeNameStudent() {
        try {
            Student student = searchStudent();
            String name = JOptionPane.showInputDialog(null, "Digite el nuevo nombre del estudiante", "CAMBIO DE NOMBRE",
                    JOptionPane.PLAIN_MESSAGE);
            studentservice.setNewName(student, name);
            JOptionPane.showMessageDialog(null, "Nombre cambiado con exito", "NOMBRE CAMBIADO",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (StudentNotFoundException | HeadlessException | InvalidDataException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite cambiar el email de un estudiante existente.
     * Muestra un mensaje de confirmación o error.
     */
    public void changeEmailStudent() {
        try {
            Student student = searchStudent();
            String email = JOptionPane.showInputDialog(null, "Digite el nuevo email del estudiante", "CAMBIO DE EMAIL",
                    JOptionPane.PLAIN_MESSAGE);
            studentservice.setNewEmail(student, email);
            JOptionPane.showMessageDialog(null, "Email cambiado con exito", "EMAIL CAMBIADO",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (StudentNotFoundException | HeadlessException | InvalidDataException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Permite remover un curso de un estudiante.
     * Muestra un mensaje de confirmación o error.
     */
    public void removeCourseFromStudent() {
        try {
            Student student = searchStudent();
            String course[] = new String[student.getCourses().size()];
            int i = 0;
            for (Course courses : student.getCourses().values()) {
                course[i] = courses.getName();
                i++;
            }
            int option = JOptionPane.showOptionDialog(null, "¿Que curso desea eliminar?",
                    "CURSOS DEL ESTUDIANTE", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, course,
                    course[0]);
            Course removecourse = new Course();
            for (Course courses : student.getCourses().values()) {
                if (course[option].equals(courses.getName())) {
                    removecourse = courses;
                }
            }
            studentservice.removeCourseFromStudent(student, removecourse);
            JOptionPane.showMessageDialog(null, "Curso removido con exito", "EXITO", JOptionPane.INFORMATION_MESSAGE);
        } catch (CourseNotFoundException | StudentNotFoundException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lista todos los cursos en los que un estudiante está inscrito.
     * Muestra sus detalles en un mensaje.
     */
    public void listCoursesByStudent() {
        try {
            Student student = searchStudent();
            String message = "";
            for (Course courses : studentservice.listCoursesByStudent(student).values()) {
                message += courses.toString() + "\n\n";
            }
            JOptionPane.showMessageDialog(null, message, "CURSOS ACTUALES DEL ESTUDIANTE",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (CourseNotFoundException | StudentNotFoundException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
