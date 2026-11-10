<%@ include file="header.jsp" %>
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
        <li><span class="tick">&#10003;</span> Watched by Prometheus and Grafana</li>
      </ul>
    </div>
  </section>

  <section class="auth-panel">
    <div class="auth-form">
      <p class="eyebrow">Register</p>
      <h2>Create your account</h2>
      <p class="auth-sub">It takes less than a minute.</p>

      <form action="register" method="post" id="register-form">
        <div class="field">
          <label for="name">Full name</label>
          <input type="text" name="name" id="name" placeholder="Jane Doe" autocomplete="name" required>
        </div>

        <div class="field-row">
          <div class="field">
            <label for="mobile">Mobile number</label>
            <input type="tel" name="mobile" id="mobile" placeholder="+237 6xx xxx xxx" autocomplete="tel" required>
          </div>
          <div class="field">
            <label for="email">Email</label>
            <input type="email" name="email" id="email" placeholder="jane@example.com" autocomplete="email" required>
          </div>
        </div>

        <div class="field-row">
          <div class="field">
            <label for="psw">Password</label>
            <input type="password" name="psw" id="psw" minlength="8" autocomplete="new-password" required>
          </div>
          <div class="field">
            <label for="psw-repeat">Repeat password</label>
            <input type="password" name="psw-repeat" id="psw-repeat" autocomplete="new-password" required>
          </div>
        </div>
        <p class="hint">At least 8 characters.</p>

        <button type="submit" class="btn btn-block">Create account</button>
        <p class="fine-print">By creating an account you agree to the Terms and Privacy Policy.</p>
      </form>
    </div>
  </section>

</main>
<script>
  // Stop the form from submitting when the two passwords don't match.
  (function () {
    var pw = document.getElementById('psw');
    var repeat = document.getElementById('psw-repeat');
    function check() {
      repeat.setCustomValidity(repeat.value && repeat.value !== pw.value ? 'Passwords do not match.' : '');
    }
    pw.addEventListener('input', check);
    repeat.addEventListener('input', check);
  })();
</script>
<%@ include file="footer.jsp" %>
