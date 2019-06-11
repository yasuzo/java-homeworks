<%@ page import="java.util.Collection" %>
<%@ page import="hr.fer.zemris.java.hw14.models.PollOption" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Collection<PollOption> options = (Collection<PollOption>) request.getAttribute("options");
%>
<html>
<head>
    <title>Glasanje</title>
</head>
<body>
    <h1>Glasanje za omiljeni bend:</h1>
    <p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste
        glasali!</p>
    <ol>
        <% for(PollOption option : options) { %>
        <li><a href="glasanje-glasaj?optionID=<%= option.getId() %>&pollID=<%= option.getPollId() %>"><%= option.getTitle() %></a></li>
        <% } %>
    </ol>
</body>
</html>