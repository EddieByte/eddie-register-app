<%@ include file="/header.jsp" %>
<main class="page">
  <p class="eyebrow">Temporarily unavailable</p>
  <h1>We can't reach the database right now</h1>
  <p class="lead">Accounts are stored in PostgreSQL, which isn't connected or isn't responding at the moment. The rest of the site still works. Please try again in a few minutes.</p>
  <a class="btn" href="<%= ctx %>/home.jsp">Back to home</a>
</main>
<%@ include file="/footer.jsp" %>
