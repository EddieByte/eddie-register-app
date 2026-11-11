<%@ include file="/header.jsp" %>
<%@ page import="com.example.user.User,com.example.web.RelativeTime,java.util.List" %>
<%
  long memberCount = (Long) request.getAttribute("memberCount");
  @SuppressWarnings("unchecked")
  List<User> recent = (List<User>) request.getAttribute("recent");
%>
<main class="page community">
  <p class="eyebrow">Community</p>
  <h1><%= memberCount %> <%= memberCount == 1 ? "member" : "members" %> and counting</h1>
  <p class="lead">Every account below is a real row in Amazon RDS PostgreSQL, created through this pipeline. Only first names and initials are shown.</p>

<% if (recent.isEmpty()) { %>
  <div class="panel empty">
    <p>No one has joined yet. <a href="<%= ctx %>/register">Be the first.</a></p>
  </div>
<% } else { %>
  <ul class="member-list">
<%   for (User member : recent) {
       String publicName = member.getPublicName(); %>
    <li class="member">
      <span class="avatar" aria-hidden="true"><%= Html.esc(publicName.substring(0, 1).toUpperCase()) %></span>
      <span class="member-name"><%= Html.esc(publicName) %></span>
      <span class="member-when">joined <%= RelativeTime.since(member.getCreatedAt()) %></span>
    </li>
<%   } %>
  </ul>
<% } %>

<% if (navUser == null) { %>
  <p class="community-cta"><a class="btn" href="<%= ctx %>/register">Join them</a></p>
<% } %>
</main>
<%@ include file="/footer.jsp" %>
