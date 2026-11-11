package com.example.web;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationTest {

    @Test
    void validRegistrationHasNoErrors() {
        assertTrue(Validation.registration("Jane Doe", "+237 650 123 456", "jane@example.com",
            "password123", "password123").isEmpty());
    }

    @Test
    void reportsEachProblem() {
        Map<String, String> errors = Validation.registration("J", "12", "not-an-email", "short", "short");
        assertTrue(errors.containsKey("name"));
        assertTrue(errors.containsKey("mobile"));
        assertTrue(errors.containsKey("email"));
        assertTrue(errors.containsKey("psw"));
    }

    @Test
    void passwordsMustMatch() {
        Map<String, String> errors = Validation.registration("Jane Doe", "+237 650 123 456", "jane@example.com",
            "password123", "password124");
        assertTrue(errors.containsKey("psw-repeat"));
    }

    @Test
    void emailIsTrimmedAndLowercased() {
        assertEquals("jane@example.com", Validation.normaliseEmail("  Jane@Example.COM "));
    }
}
