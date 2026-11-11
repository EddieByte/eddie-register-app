package com.example.security;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginRateLimiterTest {

    @Test
    void blocksAfterFiveFailuresAndRecoversAfterWindow() {
        AtomicLong now = new AtomicLong(1_000_000L);
        LoginRateLimiter limiter = new LoginRateLimiter(now::get);

        for (int i = 0; i < LoginRateLimiter.MAX_FAILURES - 1; i++) {
            limiter.recordFailure("jane@example.com");
        }
        assertFalse(limiter.isBlocked("jane@example.com"));

        limiter.recordFailure("jane@example.com");
        assertTrue(limiter.isBlocked("jane@example.com"));
        assertFalse(limiter.isBlocked("someone-else@example.com"));

        now.addAndGet(LoginRateLimiter.WINDOW_MILLIS + 1);
        assertFalse(limiter.isBlocked("jane@example.com"));
    }

    @Test
    void successfulLoginResetsTheCount() {
        LoginRateLimiter limiter = new LoginRateLimiter(() -> 0L);
        for (int i = 0; i < LoginRateLimiter.MAX_FAILURES; i++) {
            limiter.recordFailure("a@b.co");
        }
        limiter.reset("a@b.co");
        assertFalse(limiter.isBlocked("a@b.co"));
    }
}
