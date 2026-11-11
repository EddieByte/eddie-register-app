package com.example.web;

import java.time.Duration;
import java.time.Instant;

/** "just now", "5 minutes ago", "3 days ago". */
public final class RelativeTime {

    private RelativeTime() { }

    public static String since(Instant then, Instant now) {
        long seconds = Math.max(0, Duration.between(then, now).getSeconds());
        if (seconds < 60) return "just now";
        long minutes = seconds / 60;
        if (minutes < 60) return plural(minutes, "minute");
        long hours = minutes / 60;
        if (hours < 24) return plural(hours, "hour");
        long days = hours / 24;
        if (days < 30) return plural(days, "day");
        long months = days / 30;
        if (months < 12) return plural(months, "month");
        return plural(days / 365, "year");
    }

    public static String since(Instant then) {
        return since(then, Instant.now());
    }

    private static String plural(long n, String unit) {
        return n + " " + unit + (n == 1 ? "" : "s") + " ago";
    }
}
