package com.example.user;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    private final User user = new User(1, "Jane Mary Doe", "+237 650 123 456", "jane@example.com", Instant.now());

    @Test
    void publicNameShowsFirstNameAndLastInitial() {
        assertEquals("Jane D.", user.getPublicName());
        assertEquals("Jane", user.getFirstName());
    }

    @Test
    void mobileIsMaskedExceptStartAndEnd() {
        assertEquals("+237 6** *** 456", user.getMaskedMobile());
    }

    @Test
    void singleNameHasNoInitial() {
        assertEquals("Cher", new User(2, "Cher", "1234567", "c@x.io", Instant.now()).getPublicName());
    }
}
