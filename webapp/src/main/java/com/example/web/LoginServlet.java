package com.example.web;

import com.example.db.Database;
import com.example.security.Csrf;
import com.example.security.LoginRateLimiter;
import com.example.security.PasswordHasher;
import com.example.user.User;
import com.example.user.UserRepository;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** GET shows the login form; POST checks the email and password. */
public class LoginServlet extends HttpServlet {

    private static final String GENERIC_ERROR = "Invalid email or password.";
    private final UserRepository users = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (Sessions.currentUserId(request) != null) {
            Sessions.redirect(request, response, "/account");
            return;
        }
        Sessions.render(request, response, "login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!Csrf.isValid(request)) {
            Sessions.forbidden(response);
            return;
        }
        if (!Database.isConfigured()) {
            Sessions.databaseUnavailable(request, response, null);
            return;
        }

        String email = Validation.normaliseEmail(request.getParameter("email"));
        String password = request.getParameter("psw");
        LoginRateLimiter limiter = LoginRateLimiter.get();

        if (limiter.isBlocked(email)) {
            showError(request, response, email, "Too many failed attempts. Please wait 15 minutes and try again.", 429);
            return;
        }

        try {
            UserRepository.Credentials credentials = users.findCredentials(email);
            boolean ok;
            if (credentials == null) {
                PasswordHasher.verifyDummy(password);   // same timing whether or not the email exists
                ok = false;
            } else {
                ok = PasswordHasher.verify(password, credentials.getPasswordHash());
            }

            if (!ok) {
                limiter.recordFailure(email);
                showError(request, response, email, GENERIC_ERROR, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            limiter.reset(email);
            User user = users.findById(credentials.getId());
            Sessions.logIn(request, user.getId(), user.getFirstName());
            Sessions.redirect(request, response, "/account");
        } catch (SQLException e) {
            Sessions.databaseUnavailable(request, response, e);
        }
    }

    private static void showError(HttpServletRequest request, HttpServletResponse response,
                                  String email, String message, int status) throws ServletException, IOException {
        request.setAttribute("error", message);
        request.setAttribute("email", email);
        response.setStatus(status);
        Sessions.render(request, response, "login.jsp");
    }
}
