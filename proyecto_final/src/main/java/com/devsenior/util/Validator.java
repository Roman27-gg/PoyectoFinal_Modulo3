package com.devsenior.util;

import java.util.Random;

/**
 * Clase de utilidades para validaciones comunes en el sistema.
 * Incluye validación de nombres, correos electrónicos,
 * capacidades numéricas y la generación de identificadores.
 *
 * Esta clase es {@code final} y no puede instanciarse.
 */
public final class Validator {

    /**
     * Constructor privado para evitar instanciación.
     */
    private Validator() {}

    /**
     * Verifica si un nombre es válido.
     * El nombre debe contener solo letras y espacios, 
     * no estar vacío ni compuesto únicamente por espacios.
     *
     * @param name nombre a validar
     * @return {@code true} si el nombre es válido, de lo contrario {@code false}
     */
    public static Boolean validateName(String name) {
        return (name.matches("[a-zA-Z\\s]*") && name.length() > 0 && !name.matches("\\s*"));
    }

    /**
     * Verifica si un correo electrónico es válido.
     * Debe contener caracteres alfanuméricos antes del {@code @},
     * seguido de un dominio válido y terminar opcionalmente en {@code .co} o {@code .com}.
     *
     * @param email correo a validar
     * @return {@code true} si el correo es válido, de lo contrario {@code false}
     */
    public static Boolean validateEmail(String email) {
        return (email.matches("[a-zA-Z0-9]*\\@[a-zA-Z]*(\\.[a-zA-Z]*)?(.co||.com)?") && email.length() > 0 && !email.matches("\\s*"));
    }

    /**
     * Verifica si una capacidad ingresada es un número válido.
     * Solo acepta dígitos.
     *
     * @param capacity cadena a validar
     * @return {@code true} si la capacidad es un número válido, de lo contrario {@code false}
     */
    public static Boolean validateCapacity(String capacity) {
        return (capacity.matches("[0-9]*"));
    }

    /**
     * Genera un identificador aleatorio de 6 dígitos.
     *
     * @return identificador único como cadena
     */
    public static String createId() {
        Random random = new Random();
        return String.valueOf(random.nextInt(100000, 1000000));
    }
}
