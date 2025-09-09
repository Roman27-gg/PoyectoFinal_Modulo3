package com.devsenior.util;

import java.util.Random;

public final class Validator {
    private Validator () {}

    public static Boolean validateName(String name){
        return (name.matches("[a-zA-Z\\s]*") && name.length()>0 && !name.matches("\\s*"));
    }

    public static Boolean validateEmail(String email){
        return (email.matches("[a-zA-Z0-9]*\\@[a-zA-Z]*(\\.[a-zA-Z]*)?(.co||.com)?") && email.length()>0 && !email.matches("\\s*"));
    }

    public static Boolean validateCapacity(String capacity){
        return (capacity.matches("[0-9]*"));
    }

    public static String createId(){
        Random random = new Random();
        return String.valueOf(random.nextInt(100000, 1000000));
    }
}
