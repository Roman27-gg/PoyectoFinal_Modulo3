package com.devsenior.app;

import java.util.Arrays;

import javax.swing.JOptionPane;

import com.devsenior.controller.CourseController;
import com.devsenior.controller.StudentController;
import com.devsenior.exception.CourseNotFoundException;
import com.devsenior.exception.StudentNotFoundException;

/**
 * Clase principal de la aplicación que muestra un menú para gestionar
 * cursos y estudiantes mediante JOptionPane y los controladores.
 */
public class MenuApp {

    /** Controlador para manejar las operaciones de cursos. */
    private final CourseController coursecontroller;

    /** Controlador para manejar las operaciones de estudiantes. */
    private final StudentController studentcontroller;

    /**
     * Constructor que inicializa los controladores.
     */
    public MenuApp() {
        coursecontroller = new CourseController();
        studentcontroller = new StudentController();
    }

    /**
     * Muestra el menú de opciones y devuelve la opción seleccionada.
     * 
     * @return El índice de la opción seleccionada, o -1 si se cancela.
     */
    private int showMenu() {
        String message[] = { "1. Crear un nuevo curso", "2. Crear un nuevo estudiante", "3. Buscar un curso",
                "4. Buscar un estudiante", "5. Mostrar todos los cursos", "6. Mostrar todos los estudiantes",
                "7. Cambiar el nombre de un curso", "8. Cambiar el nombre de un estudiante",
                "9. Cambiar la capacidad de un curso", "10. Cambiar el email de un estudiante",
                "11. Remover un estudiante de un curso", "12. Remover un curso de un estudiante",
                "13. Mostrar los estudiantes de un curso", "14. Mostrar los cursos de un estudiante",
                "15. Inscribir un estudiante en un curso", "0. Salir" };
        String selection = (String) JOptionPane.showInputDialog(null, "Seleccione una opción", "MENU",
                JOptionPane.PLAIN_MESSAGE, null, message, message[0]);
        if (selection == null) {
            return -1;
        }
        return Arrays.asList(message).indexOf(selection);
    }

    /**
     * Ejecuta la acción correspondiente a la opción seleccionada.
     * 
     * @param option La opción seleccionada.
     * @return La opción ejecutada, o -1 si se cancela.
     */
    private int handleOption(int option) {
        if (option == -1) {
            return -1;
        }
        option = (option == 15) ? 0 : option + 1;
        switch (option) {
            case 1 -> coursecontroller.createCourse();
            case 2 -> studentcontroller.createStudent();
            case 3 -> {
                try {
                    coursecontroller.searchCourse();
                } catch (CourseNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            case 4 -> {
                try {
                    studentcontroller.searchStudent();
                } catch (StudentNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error: " + e.getMessage(), "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            case 5 -> coursecontroller.listCourses();
            case 6 -> studentcontroller.listStudents();
            case 7 -> coursecontroller.changeNameCourse();
            case 8 -> studentcontroller.changeNameStudent();
            case 9 -> coursecontroller.changeCapacityCourse();
            case 10 -> studentcontroller.changeEmailStudent();
            case 11 -> coursecontroller.removeStudentFromCourse();
            case 12 -> studentcontroller.removeCourseFromStudent();
            case 13 -> coursecontroller.listStudentsByCourse();
            case 14 -> studentcontroller.listCoursesByStudent();
            case 15 -> coursecontroller.enrollStudent(studentcontroller);
            default -> JOptionPane.showMessageDialog(null, "Cerrando el programa... hasta pronto", "CERRANDO PROGRAMA",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        return option;
    }

    /**
     * Inicia la aplicación y mantiene el bucle del menú hasta que el usuario salga.
     */
    public void start() {
        int option;
        do {
            option = showMenu();
            option = handleOption(option);
        } while (option != 0);
    }
}
