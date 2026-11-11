package com.example.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

/**
 * Slows down password guessing: after 5 failed logins for the same email within
 * 15 minutes, further attempts are refused until the window passes.
 * Kept in memory (per pod), which is enough for this demo.
 */
public final class LoginRateLimiter {

    public static final int MAX_FAILURES = 5;
    public static final long WINDOW_MILLIS = 15 * 60 * 1000L;

    private static final LoginRateLimiter INSTANCE = new LoginRateLimiter(System::currentTimeMillis);

    private final Map<String, long[]> failures = new ConcurrentHashMap<>();   // key -> {count, windowStart}
    private final LongSupplier clock;

    LoginRateLimiter(LongSupplier clock) {
        this.clock = clock;
    }

    public static LoginRateLimiter get() {
        return INSTANCE;
    }

    public boolean isBlocked(String key) {
        long[] entry = failures.get(key);
        if (entry == null) {
            return false;
        }
        if (clock.getAsLong() - entry[1] > WINDOW_MILLIS) {
            failures.remove(key);
            return false;
        }
        return entry[0] >= MAX_FAILURES;
    }

    public void recordFailure(String key) {
        long now = clock.getAsLong();
        failures.compute(key, (k, entry) -> {
            if (entry == null || now - entry[1] > WINDOW_MILLIS) {
                return new long[] {1, now};
            }
            entry[0]++;
            return entry;
        });
    }

    public void reset(String key) {
        failures.remove(key);
    }
}
