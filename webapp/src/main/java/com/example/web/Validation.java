package com.example.web;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/** Server-side checks for the registration form (the browser checks can be bypassed). */
public final class Validation {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern MOBILE = Pattern.compile("^\\+?[0-9 ()\\-]{7,20}$");

    public static final int MIN_PASSWORD = 8;
    public static final int MAX_PASSWORD = 128;

    private Validation() { }

    public static String normaliseEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(java.util.Locale.ROOT);
    }

    public static String clean(String value) {
        return value == null ? "" : value.trim().replaceAll("\\s+", " ");
    }

    /** Returns field name -> error message; empty means everything is valid. */
    public static Map<String, String> registration(String name, String mobile, String email,
                                                   String password, String repeat) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (name.length() < 2 || name.length() > 100) {
            errors.put("name", "Enter your full name (2 to 100 characters).");
        }
        long digits = mobile.chars().filter(Character::isDigit).count();
        if (!MOBILE.matcher(mobile).matches() || digits < 7) {
            errors.put("mobile", "Enter a valid mobile number.");
        }
        if (email.length() > 254 || !EMAIL.matcher(email).matches()) {
            errors.put("email", "Enter a valid email address.");
        }
        if (password == null || password.length() < MIN_PASSWORD || password.length() > MAX_PASSWORD) {
            errors.put("psw", "Use 8 to 128 characters for your password.");
        } else if (!password.equals(repeat)) {
            errors.put("psw-repeat", "The passwords do not match.");
        }
        return errors;
    }
}
