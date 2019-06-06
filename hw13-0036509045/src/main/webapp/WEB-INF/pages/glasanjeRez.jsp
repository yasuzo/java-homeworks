<%@ page import="hr.fer.zemris.java.webapp2.servlets.voting.VotingDBUtil" %>
<%@ page import="java.util.Collection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Collection<VotingDBUtil.BandData> bands = (Collection<VotingDBUtil.BandData>) request.getAttribute("bands");
    Collection<VotingDBUtil.BandData> winners = (Collection<VotingDBUtil.BandData>) request.getAttribute("winners");
%>

<h1>Rezultati glasanja</h1>
<p>Ovo su rezultati glasanja.</p>
<table border="1" cellspacing="0" class="rez">
    <thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
    <tbody>
    <% for(VotingDBUtil.BandData band : bands) { %>
        <tr><td><%= band.getName() %></td><td><%= band.getVotes() %></td></tr>
    <% } %>
    </tbody>
</table>

<h2>Grafički prikaz rezultata</h2>
<img alt="Pie-chart" src="glasanje-grafika" width="700" height="400" />

<h2>Rezultati u XLS formatu</h2>
<p>Rezultati u XLS formatu dostupni su <a href="glasanje-xls">ovdje</a></p>

<h2>Razno</h2>
<p>Primjeri pjesama pobjedničkih bendova:</p>
<ul>
    <% for(VotingDBUtil.BandData band : winners) { %>
        <li><a href="<%= band.getVideoLink() %>" target="_blank"><%= band.getName() %></a></li>
    <% } %>
</ul>