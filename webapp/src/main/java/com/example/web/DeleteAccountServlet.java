package com.example.web;

import com.example.security.Csrf;
import com.example.security.PasswordHasher;
import com.example.user.UserRepository;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Deletes the logged-in user's account after they confirm their password. */
public class DeleteAccountServlet extends HttpServlet {

    private final UserRepository users = new UserRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!Csrf.isValid(request)) {
            Sessions.forbidden(response);
            return;
        }
        long id = Sessions.currentUserId(request);
        try {
            String hash = users.findPasswordHash(id);
            if (hash == null || !PasswordHasher.verify(request.getParameter("psw"), hash)) {
                request.setAttribute("user", users.findById(id));
                request.setAttribute("deleteError", "That password is not correct. Your account was not deleted.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Sessions.render(request, response, "account.jsp");
                return;
            }
            users.delete(id);
            Sessions.logOut(request);
            Sessions.redirect(request, response, "/home.jsp?deleted=1");
        } catch (SQLException e) {
            Sessions.databaseUnavailable(request, response, e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Sessions.redirect(request, response, "/account");
    }
}
