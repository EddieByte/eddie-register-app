<%@ include file="/header.jsp" %>
<%
  String loginError = (String) request.getAttribute("error");
%>
<main class="auth">

  <section class="auth-brand">
    <div class="auth-brand-inner">
      <p class="eyebrow eyebrow-light">Welcome back</p>
      <h1>Good to see you again.</h1>
      <p class="auth-lead">Log in to see your account, or delete it whenever you like. Your password is stored only as a salted PBKDF2 hash.</p>
    </div>
  </section>

  <section class="auth-panel">
    <div class="auth-form auth-form-narrow">
      <p class="eyebrow">Log in</p>
      <h2>Log in to your account</h2>
      <p class="auth-sub">Use the email and password you registered with.</p>

<% if (loginError != null) { %>
      <div class="alert alert-error" role="alert"><%= Html.esc(loginError) %></div>
<% } %>

      <form action="<%= ctx %>/login" method="post">
        <input type="hidden" name="<%= Csrf.FIELD %>" value="<%= Csrf.token(request) %>">
        <div class="field">
          <label for="email">Email</label>
          <input type="email" name="email" id="email" placeholder="jane@example.com" autocomplete="email" required value="<%= Html.esc(request.getAttribute("email")) %>">
        </div>
        <div class="field">
          <label for="psw">Password</label>
          <input type="password" name="psw" id="psw" autocomplete="current-password" required>
        </div>
        <button type="submit" class="btn btn-block">Log in</button>
        <p class="switch">New here? <a href="<%= ctx %>/register">Create an account</a></p>
      </form>
    </div>
  </section>

</main>
<%@ include file="/footer.jsp" %>
