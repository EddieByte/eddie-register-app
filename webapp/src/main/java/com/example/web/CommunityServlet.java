package com.example.web;

import com.example.db.Database;
import com.example.user.UserRepository;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Member count and recent sign-ups ("Jane D. joined 2 hours ago"); no private details. */
public class CommunityServlet extends HttpServlet {

    private final UserRepository users = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!Database.isConfigured()) {
            Sessions.databaseUnavailable(request, response, null);
            return;
        }
        try {
            request.setAttribute("memberCount", users.count());
            request.setAttribute("recent", users.recent(12));
            Sessions.render(request, response, "community.jsp");
        } catch (SQLException e) {
            Sessions.databaseUnavailable(request, response, e);
        }
    }
}
