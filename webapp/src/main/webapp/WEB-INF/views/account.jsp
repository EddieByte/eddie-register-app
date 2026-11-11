<%@ include file="/header.jsp" %>
<%@ page import="com.example.user.User,com.example.web.RelativeTime,java.time.ZoneOffset,java.time.format.DateTimeFormatter" %>
<%
  User user = (User) request.getAttribute("user");
  boolean welcome = Boolean.TRUE.equals(request.getAttribute("welcome"));
  String deleteError = (String) request.getAttribute("deleteError");
  String memberSince = DateTimeFormatter.ofPattern("d MMMM yyyy").format(user.getCreatedAt().atZone(ZoneOffset.UTC));
%>
<main class="page account">

<% if (welcome) { %>
  <div class="alert alert-success" role="status">You're all set, <%= Html.esc(user.getFirstName()) %>! Your account has been created.</div>
<% } %>

  <p class="eyebrow">My account</p>
  <h1>Welcome, <%= Html.esc(user.getFirstName()) %></h1>
  <p class="lead">Joined <%= RelativeTime.since(user.getCreatedAt()) %>. Here's what we store about you.</p>

  <div class="account-grid">
    <section class="panel">
      <h2>Profile</h2>
      <dl class="details">
        <dt>Full name</dt><dd><%= Html.esc(user.getFullName()) %></dd>
        <dt>Email</dt><dd><%= Html.esc(user.getEmail()) %></dd>
        <dt>Mobile</dt><dd><%= Html.esc(user.getMaskedMobile()) %></dd>
        <dt>Member since</dt><dd><%= memberSince %></dd>
      </dl>
    </section>

    <section class="panel">
      <h2>Security</h2>
      <p>Your password is stored as a salted PBKDF2-SHA256 hash, never as plain text.</p>
      <p>Others on the <a href="<%= ctx %>/community">Community</a> page only see <strong><%= Html.esc(user.getPublicName()) %></strong>.</p>
      <form method="post" action="<%= ctx %>/logout">
        <input type="hidden" name="<%= Csrf.FIELD %>" value="<%= Csrf.token(request) %>">
        <button type="submit" class="btn btn-secondary">Log out</button>
      </form>
    </section>
  </div>

  <section class="panel danger-zone" id="delete">
    <h2>Delete account</h2>
    <p>This permanently removes your account and everything stored about you. It can't be undone.</p>
<% if (deleteError != null) { %>
    <div class="alert alert-error" role="alert"><%= Html.esc(deleteError) %></div>
<% } %>
    <form method="post" action="<%= ctx %>/account/delete" class="delete-form"
          onsubmit="return confirm('Delete your account permanently?');">
      <input type="hidden" name="<%= Csrf.FIELD %>" value="<%= Csrf.token(request) %>">
      <div class="field">
        <label for="delete-psw">Confirm with your password</label>
        <input type="password" name="psw" id="delete-psw" autocomplete="current-password" required>
      </div>
      <button type="submit" class="btn btn-danger">Delete my account</button>
    </form>
  </section>

</main>
<%@ include file="/footer.jsp" %>
