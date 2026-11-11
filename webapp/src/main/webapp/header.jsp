<%@ page contentType="text/html;charset=UTF-8" session="false" import="com.example.web.Html,com.example.web.Sessions,com.example.security.Csrf" %>
<%
  // session="false": pages don't create a session just by being viewed (keeps health
  // checks cheap). Links use the context path so they work from any URL depth.
  String ctx = request.getContextPath();
  javax.servlet.http.HttpSession navSession = request.getSession(false);
  String navUser = navSession == null ? null : (String) navSession.getAttribute(Sessions.USER_NAME);
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>eddie.dev</title>
  <link rel="stylesheet" href="<%= ctx %>/style.css">
</head>
<body>
<nav class="nav">
  <a class="brand" href="<%= ctx %>/home.jsp">eddie.dev</a>
  <div class="links">
    <a href="<%= ctx %>/home.jsp">Home</a>
    <a href="<%= ctx %>/community">Community</a>
<% if (navUser == null) { %>
    <a href="<%= ctx %>/register">Register</a>
    <a href="<%= ctx %>/login">Log in</a>
<% } else { %>
    <a href="<%= ctx %>/account">My account</a>
    <form class="nav-form" method="post" action="<%= ctx %>/logout">
      <input type="hidden" name="<%= Csrf.FIELD %>" value="<%= Csrf.token(request) %>">
      <button type="submit" class="link-button">Log out</button>
    </form>
<% } %>
  </div>
</nav>
