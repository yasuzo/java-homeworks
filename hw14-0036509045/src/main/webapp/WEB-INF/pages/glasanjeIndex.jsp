<%@ page import="java.util.Collection" %>
<%@ page import="hr.fer.zemris.java.hw14.models.PollOption" %>
<%@ page import="hr.fer.zemris.java.hw14.models.Poll" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Collection<PollOption> options = (Collection<PollOption>) request.getAttribute("options");
    Poll poll = (Poll) request.getAttribute("poll");
%>
<html>
<head>
    <title>Glasanje</title>
</head>
<body>
    <h1><%= poll.getTitle() %></h1>
    <p><%= poll.getMessage() %></p>
    <ol>
        <% for(PollOption option : options) { %>
        <li><a href="glasanje-glasaj?optionID=<%= option.getId() %>"><%= option.getTitle() %></a></li>
        <% } %>
    </ol>
</body>
</html>