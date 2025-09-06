package com.devsenior.util;

import java.util.Random;

public final class Validator {
    private Validator () {}

    public static Boolean validateName(String name){
        if(name.matches("[a-zA-Z\\s]*") && name.length()>0 && !name.matches("\\s*")){
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validateEmail(String email){
        if (email.matches("[a-zA-Z]*\\@[a-zA-Z]*") && email.length()>0 && !email.matches("\\s*")) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validateCapacity(String capacity){
        if (capacity.matches("[\\d]")) {
            return true;
        } else {
            return false;
        }
    }

    public static String createId(){
        Random random = new Random();
        return String.valueOf(random.nextInt(100000, 1000000));
    }
}
