package com.example.web;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlAndTimeTest {

    @Test
    void escapesScriptTags() {
        assertEquals("&lt;script&gt;alert(1)&lt;/script&gt;", Html.esc("<script>alert(1)</script>"));
        assertEquals("Tom &amp; &quot;Jerry&quot; O&#39;Neil", Html.esc("Tom & \"Jerry\" O'Neil"));
        assertEquals("", Html.esc(null));
    }

    @Test
    void relativeTimes() {
        Instant now = Instant.parse("2026-11-11T12:00:00Z");
        assertEquals("just now", RelativeTime.since(now.minusSeconds(10), now));
        assertEquals("1 minute ago", RelativeTime.since(now.minusSeconds(60), now));
        assertEquals("3 hours ago", RelativeTime.since(now.minusSeconds(3 * 3600), now));
        assertEquals("2 days ago", RelativeTime.since(now.minusSeconds(2 * 86400), now));
    }
}
