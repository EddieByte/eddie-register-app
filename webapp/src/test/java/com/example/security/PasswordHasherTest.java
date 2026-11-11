package com.example.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHasherTest {

    @Test
    void correctPasswordVerifies() {
        String hash = PasswordHasher.hash("correct horse battery");
        assertTrue(PasswordHasher.verify("correct horse battery", hash));
    }

    @Test
    void wrongPasswordFails() {
        String hash = PasswordHasher.hash("correct horse battery");
        assertFalse(PasswordHasher.verify("wrong password", hash));
    }

    @Test
    void samePasswordGetsDifferentSalt() {
        assertNotEquals(PasswordHasher.hash("same password"), PasswordHasher.hash("same password"));
    }

    @Test
    void hashNeverContainsThePassword() {
        assertFalse(PasswordHasher.hash("supersecret123").contains("supersecret123"));
    }

    @Test
    void malformedHashesFailSafely() {
        assertFalse(PasswordHasher.verify("x", null));
        assertFalse(PasswordHasher.verify("x", "not-a-hash"));
        assertFalse(PasswordHasher.verify("x", "pbkdf2_sha256$abc$###$###"));
    }
}
