<%@ include file="/header.jsp" %>
<%!
  private static String fieldError(java.util.Map<String, String> errors, String field) {
    if (errors == null || !errors.containsKey(field)) return "";
    return "<p class=\"field-error\">" + com.example.web.Html.esc(errors.get(field)) + "</p>";
  }
%>
<%
  @SuppressWarnings("unchecked")
  java.util.Map<String, String> errors = (java.util.Map<String, String>) request.getAttribute("errors");
%>
<main class="auth">

  <section class="auth-brand">
    <div class="auth-brand-inner">
      <p class="eyebrow eyebrow-light">How this page ships</p>
      <h1>A small app on a real delivery pipeline.</h1>
      <p class="auth-lead">Every change to this page is built, scanned, deployed and monitored automatically.</p>
      <ul class="auth-points">
        <li><span class="tick">&#10003;</span> Built and tested by Jenkins</li>
        <li><span class="tick">&#10003;</span> Quality-checked by SonarQube, scanned by Trivy</li>
        <li><span class="tick">&#10003;</span> Deployed to Kubernetes on EKS by ArgoCD</li>
        <li><span class="tick">&#10003;</span> Stored in Amazon RDS PostgreSQL</li>
        <li><span class="tick">&#10003;</span> Watched by Prometheus and Grafana</li>
      </ul>
    </div>
  </section>

  <section class="auth-panel">
    <div class="auth-form">
      <p class="eyebrow">Register</p>
      <h2>Create your account</h2>
      <p class="auth-sub">It takes less than a minute.</p>
      <p class="demo-note">Demo app: please don't reuse a real password.</p>

      <form action="<%= ctx %>/register" method="post" id="register-form" novalidate>
        <input type="hidden" name="<%= Csrf.FIELD %>" value="<%= Csrf.token(request) %>">

        <div class="field">
          <label for="name">Full name</label>
          <input type="text" name="name" id="name" placeholder="Jane Doe" autocomplete="name" maxlength="100" required value="<%= Html.esc(request.getAttribute("name")) %>">
          <%= fieldError(errors, "name") %>
        </div>

        <div class="field-row">
          <div class="field">
            <label for="mobile">Mobile number</label>
            <input type="tel" name="mobile" id="mobile" placeholder="+237 6xx xxx xxx" autocomplete="tel" maxlength="20" required value="<%= Html.esc(request.getAttribute("mobile")) %>">
            <%= fieldError(errors, "mobile") %>
          </div>
          <div class="field">
            <label for="email">Email</label>
            <input type="email" name="email" id="email" placeholder="jane@example.com" autocomplete="email" maxlength="254" required value="<%= Html.esc(request.getAttribute("email")) %>">
            <%= fieldError(errors, "email") %>
          </div>
        </div>

        <div class="field-row">
          <div class="field">
            <label for="psw">Password</label>
            <input type="password" name="psw" id="psw" minlength="8" maxlength="128" autocomplete="new-password" required>
            <%= fieldError(errors, "psw") %>
          </div>
          <div class="field">
            <label for="psw-repeat">Repeat password</label>
            <input type="password" name="psw-repeat" id="psw-repeat" maxlength="128" autocomplete="new-password" required>
            <%= fieldError(errors, "psw-repeat") %>
          </div>
        </div>
        <p class="hint">At least 8 characters.</p>

        <button type="submit" class="btn btn-block">Create account</button>
        <p class="switch">Already have an account? <a href="<%= ctx %>/login">Log in</a></p>
        <p class="fine-print">By creating an account you agree to the Terms and Privacy Policy.</p>
      </form>
    </div>
  </section>

</main>
<script>
  // Browser-side check that the passwords match (the server checks again).
  (function () {
    var pw = document.getElementById('psw');
    var repeat = document.getElementById('psw-repeat');
    function check() {
      repeat.setCustomValidity(repeat.value && repeat.value !== pw.value ? 'Passwords do not match.' : '');
    }
    pw.addEventListener('input', check);
    repeat.addEventListener('input', check);
    document.getElementById('register-form').addEventListener('submit', function (e) {
      check();
      if (!this.checkValidity()) { e.preventDefault(); this.reportValidity(); }
    });
  })();
</script>
<%@ include file="/footer.jsp" %>
