package com.example.user;

import java.time.Instant;

/** A registered user, without the password hash. */
public final class User {

    private final long id;
    private final String fullName;
    private final String mobile;
    private final String email;
    private final Instant createdAt;

    public User(long id, String fullName, String mobile, String email, Instant createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.mobile = mobile;
        this.email = email;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getMobile() { return mobile; }
    public String getEmail() { return email; }
    public Instant getCreatedAt() { return createdAt; }

    /** "Jane" from "Jane Doe". */
    public String getFirstName() {
        String[] parts = fullName.trim().split("\\s+");
        return parts[0];
    }

    /** "Jane D." from "Jane Doe": what other people see on the Community page. */
    public String getPublicName() {
        String[] parts = fullName.trim().split("\\s+");
        if (parts.length < 2) {
            return parts[0];
        }
        return parts[0] + " " + parts[parts.length - 1].charAt(0) + ".";
    }

    /** "+237 6** *** 789": only the start and the last three digits are shown. */
    public String getMaskedMobile() {
        StringBuilder out = new StringBuilder();
        int digitsSeen = 0;
        int totalDigits = (int) mobile.chars().filter(Character::isDigit).count();
        for (char c : mobile.toCharArray()) {
            if (Character.isDigit(c)) {
                digitsSeen++;
                boolean keep = digitsSeen <= 4 || digitsSeen > totalDigits - 3;
                out.append(keep ? c : '*');
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
