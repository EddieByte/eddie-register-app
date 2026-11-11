package com.example.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Small helpers shared by the servlets. */
public final class Sessions {

    private static final Logger LOG = Logger.getLogger(Sessions.class.getName());

    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userFirstName";

    private Sessions() { }

    public static Long currentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Long) session.getAttribute(USER_ID);
    }

    /** Starts a fresh session for the user (a new session id prevents session fixation). */
    public static void logIn(HttpServletRequest request, long userId, String firstName) {
        HttpSession old = request.getSession(false);
        if (old != null) {
            old.invalidate();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_ID, userId);
        session.setAttribute(USER_NAME, firstName);
    }

    public static void logOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static void redirect(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    public static void render(HttpServletRequest request, HttpServletResponse response, String view)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/" + view).forward(request, response);
    }

    /** Shown when the database can't be reached or isn't configured yet. */
    public static void databaseUnavailable(HttpServletRequest request, HttpServletResponse response, Exception cause)
            throws ServletException, IOException {
        if (cause != null) {
            LOG.log(Level.WARNING, "Database error: " + cause.getMessage(), cause);   // the password is never logged
        }
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        render(request, response, "db-unavailable.jsp");
    }

    public static void forbidden(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Your form expired. Go back, refresh the page and try again.");
    }
}
