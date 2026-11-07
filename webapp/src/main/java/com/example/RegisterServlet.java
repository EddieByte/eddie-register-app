package com.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String name = request.getParameter("name");
    // thankyou.jsp prints this value into the page, so escape it to prevent
    // script injection (XSS), e.g. a name of <script>alert(1)</script>.
    request.setAttribute("name", escapeHtml(name == null ? "" : name.trim()));
    request.getRequestDispatcher("/thankyou.jsp").forward(request, response);
  }

  /** Replaces the characters that are special in HTML with their safe entities. */
  static String escapeHtml(String value) {
    StringBuilder out = new StringBuilder(value.length());
    for (char c : value.toCharArray()) {
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
