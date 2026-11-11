<%@ include file="header.jsp" %>
<%-- Japanese text is written as HTML character codes so it displays correctly
     no matter how the JSP file's encoding is detected. --%>
<div class="page">
<% if (request.getParameter("deleted") != null) { %>
  <div class="alert alert-success" role="status">Your account has been deleted. Everything we stored about you is gone.</div>
<% } else if (request.getParameter("loggedout") != null) { %>
  <div class="alert alert-success" role="status">You've been logged out. See you soon.</div>
<% } %>
  <p class="eyebrow">Welcome</p>
  <h1>A small app, a real pipeline</h1>
  <p class="lead">This is a lightweight registration app used to exercise a fully automated CI/CD pipeline &mdash; Jenkins, SonarQube, Docker, and ArgoCD deploying to Kubernetes on EKS, with accounts stored in Amazon RDS.</p>
  <% if (navUser == null) { %>
  <a class="btn" href="<%= ctx %>/register">Create your account</a>
  <a class="btn btn-secondary" href="<%= ctx %>/login">Log in</a>
<% } else { %>
  <a class="btn" href="<%= ctx %>/account">Go to my account</a>
<% } %>
</div>

<section class="quote-band">
  <div class="quote-inner">
    <p class="quote-jp" lang="ja">&#19971;&#36578;&#12403;&#20843;&#36215;&#12365;</p>
    <p class="quote-romaji">Nana korobi, ya oki</p>
    <blockquote class="quote-en">&ldquo;Fall seven times, stand up eight.&rdquo;</blockquote>
    <p class="quote-source">Traditional Japanese proverb &middot; progress comes from getting back up, every time</p>
  </div>
</section>

<div class="page virtues">
  <p class="eyebrow">Principles</p>
  <h2>Three old virtues for modern work</h2>
  <p class="lead">Long honoured in Japanese tradition, and a good fit for how software should be built and shipped.</p>

  <div class="virtue-grid">
    <article class="virtue">
      <p class="virtue-kanji" lang="ja">&#35488;</p>
      <p class="virtue-name">Makoto &middot; Truth</p>
      <p>Sincerity: say what is true and do what you say. Here, every change is recorded in Git for anyone to see.</p>
      <p class="virtue-proverb"><span lang="ja">&#27491;&#30452;&#12398;&#38957;&#12395;&#31070;&#23487;&#12427;</span><br>&ldquo;The gods dwell in the head of the honest.&rdquo;</p>
    </article>

    <article class="virtue">
      <p class="virtue-kanji" lang="ja">&#24544;</p>
      <p class="virtue-name">Ch&#363; &middot; Loyalty</p>
      <p>Faithfulness to the people you work with and the commitments you make, especially when things go wrong.</p>
    </article>

    <article class="virtue">
      <p class="virtue-kanji" lang="ja">&#20449;</p>
      <p class="virtue-name">Shin &middot; Trust</p>
      <p>Trust is earned by keeping your word. Here it is earned by tests, scans and checks on every single change.</p>
    </article>
  </div>
</div>
<%@ include file="footer.jsp" %>
