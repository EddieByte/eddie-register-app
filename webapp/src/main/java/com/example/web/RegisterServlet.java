package com.example.web;

import com.example.db.Database;
import com.example.security.Csrf;
import com.example.security.PasswordHasher;
import com.example.user.DuplicateEmailException;
import com.example.user.User;
import com.example.user.UserRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** GET shows the registration form; POST creates the account and logs the user in. */
public class RegisterServlet extends HttpServlet {

    private final UserRepository users = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (Sessions.currentUserId(request) != null) {
            Sessions.redirect(request, response, "/account");
            return;
        }
        Sessions.render(request, response, "register.jsp");
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

        String name = Validation.clean(request.getParameter("name"));
        String mobile = Validation.clean(request.getParameter("mobile"));
        String email = Validation.normaliseEmail(request.getParameter("email"));
        String password = request.getParameter("psw");
        String repeat = request.getParameter("psw-repeat");

        Map<String, String> errors = Validation.registration(name, mobile, email, password, repeat);
        if (errors.isEmpty()) {
            try {
                User user = users.create(name, mobile, email, PasswordHasher.hash(password));
                Sessions.logIn(request, user.getId(), user.getFirstName());
                Sessions.redirect(request, response, "/account?welcome=1");
                return;
            } catch (DuplicateEmailException e) {
                errors.put("email", e.getMessage() + " Log in instead?");
            } catch (SQLException e) {
                Sessions.databaseUnavailable(request, response, e);
                return;
            }
        }

        // Show the form again with the user's input (never the passwords) and the errors.
        request.setAttribute("errors", errors);
        request.setAttribute("name", name);
        request.setAttribute("mobile", mobile);
        request.setAttribute("email", email);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Sessions.render(request, response, "register.jsp");
    }
}
