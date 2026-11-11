package com.example.web;

import com.example.security.Csrf;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Logs out (POST only, so a link on another site can't log you out). */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!Csrf.isValid(request)) {
            Sessions.forbidden(response);
            return;
        }
        Sessions.logOut(request);
        Sessions.redirect(request, response, "/home.jsp?loggedout=1");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Sessions.redirect(request, response, "/account");
    }
}
