package com.example.user;

/** Thrown when someone registers with an email that already has an account. */
public class DuplicateEmailException extends Exception {
    public DuplicateEmailException() {
        super("An account with this email already exists.");
    }
}
