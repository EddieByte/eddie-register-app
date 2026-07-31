<%@ include file="header.jsp" %>
<div class="page">
  <div class="card">
    <p class="eyebrow">Register</p>
    <h2>Create your account</h2>
    <form action="register" method="post">
      <label for="name">Full name</label>
      <input type="text" name="name" id="name" placeholder="Jane Doe" required>

      <label for="mobile">Mobile number</label>
      <input type="text" name="mobile" id="mobile" placeholder="+237 6xx xxx xxx" required>

      <label for="email">Email</label>
      <input type="email" name="email" id="email" placeholder="jane@example.com" required>

      <label for="psw">Password</label>
      <input type="password" name="psw" id="psw" required>

      <label for="psw-repeat">Repeat password</label>
      <input type="password" name="psw-repeat" id="psw-repeat" required>

      <button type="submit" class="btn">Register</button>
      <p class="fine-print">By creating an account you agree to the Terms and Privacy Policy.</p>
    </form>
  </div>
</div>
<%@ include file="footer.jsp" %>
