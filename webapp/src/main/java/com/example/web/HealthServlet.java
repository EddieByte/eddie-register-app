package com.example.web;

import com.example.user.UserRepository;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** /health: 200 when the database answers, 503 when it doesn't. Used by monitoring. */
public class HealthServlet extends HttpServlet {

    private final UserRepository users = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        try {
            users.ping();
            response.getWriter().println("OK: database reachable");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().println("UNAVAILABLE: database not reachable");
        }
    }
}
