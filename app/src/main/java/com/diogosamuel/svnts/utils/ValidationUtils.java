package com.diogosamuel.svnts.utils;

import android.util.Patterns;

public class ValidationUtils {
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    public static boolean isValidWeight(String weight) {
        try {
            double value = Double.parseDouble(weight);
            return value > 0 && value < 1000;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isValidReps(String reps) {
        try {
            int value = Integer.parseInt(reps);
            return value > 0 && value <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }
} 