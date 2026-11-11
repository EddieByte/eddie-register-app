package com.example.web;

/** HTML escaping for anything user-supplied that is printed into a page (prevents XSS). */
public final class Html {

    private Html() { }

    public static String esc(Object value) {
        if (value == null) {
            return "";
        }
        String text = value.toString();
        StringBuilder out = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            switch (c) {
                case '&': out.append("&amp;"); break;
                case '<': out.append("&lt;"); break;
                case '>': out.append("&gt;"); break;
                case '"': out.append("&quot;"); break;
                case '\'': out.append("&#39;"); break;
                default: out.append(c);
            }
        }
        return out.toString();
    }
}
