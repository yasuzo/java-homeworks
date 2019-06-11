<%@ page import="java.util.Collection" %>
<%@ page import="hr.fer.zemris.java.hw14.models.PollOption" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Collection<PollOption> options = (Collection<PollOption>) request.getAttribute("options");
    Collection<PollOption> winners = (Collection<PollOption>) request.getAttribute("winners");
%>
<html>
    <head>
        <title>Glasanje rezulati</title>
    </head>
    <body>
        <h1>Rezultati glasanja</h1>
        <p>Ovo su rezultati glasanja.</p>
        <table border="1" cellspacing="0" class="rez">
            <thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
            <tbody>
            <% for(PollOption option : options) { %>
            <tr><td><%= option.getTitle() %></td><td><%= option.getVoteCount() %></td></tr>
            <% } %>
            </tbody>
        </table>

        <h2>Grafički prikaz rezultata</h2>
        <img alt="Pie-chart" src="glasanje-grafika?pollID=<%= request.getParameter("pollID") %>" width="700" height="400" />

        <h2>Rezultati u XLS formatu</h2>
        <p>Rezultati u XLS formatu dostupni su <a href="glasanje-xls?pollID=<%= request.getParameter("pollID") %>">ovdje</a></p>

        <h2>Razno</h2>
        <p>Primjeri pjesama pobjedničkih bendova:</p>
        <ul>
            <% for(PollOption option : winners) { %>
            <li><a href="<%= option.getLink() %>" target="_blank"><%= option.getTitle() %></a></li>
            <% } %>
        </ul>
    </body>
</html>