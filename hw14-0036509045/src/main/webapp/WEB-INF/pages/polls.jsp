<%@ page import="hr.fer.zemris.java.hw14.models.Poll" %>
<%@ page import="java.util.Collection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Polls</title>
</head>
<body>
    <ul>
        <% for(Poll p : (Collection<Poll>)request.getAttribute("polls")) { %>
        <li><a href="glasanje?pollID=<%= p.getId() %>"><%= p.getTitle() %></a></li>
        <% } %>
    </ul>
</body>
</html>
