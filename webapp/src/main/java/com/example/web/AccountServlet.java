package com.example.web;

import com.example.user.User;
import com.example.user.UserRepository;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** The logged-in user's account page. AuthFilter guarantees someone is logged in. */
public class AccountServlet extends HttpServlet {

    private final UserRepository users = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = users.findById(Sessions.currentUserId(request));
            if (user == null) {                       // account no longer exists
                Sessions.logOut(request);
                Sessions.redirect(request, response, "/login");
                return;
            }
            request.setAttribute("user", user);
            request.setAttribute("welcome", request.getParameter("welcome") != null);
            Sessions.render(request, response, "account.jsp");
        } catch (SQLException e) {
            Sessions.databaseUnavailable(request, response, e);
        }
    }
}
