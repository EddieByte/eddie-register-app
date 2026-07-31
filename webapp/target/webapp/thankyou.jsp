<%@ include file="header.jsp" %>
<div class="page">
  <p class="eyebrow">Success</p>
  <h1>You're all set, <%= request.getAttribute("name") %></h1>
  <p class="lead">Your account has been created. Thanks for trying this out.</p>
  <a class="btn" href="home.jsp">Back to home</a>
</div>
<%@ include file="footer.jsp" %>
