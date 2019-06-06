<%@ page import="hr.fer.zemris.java.webapp2.servlets.voting.VotingDBUtil" %>
<%@ page import="java.util.Collection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Collection<VotingDBUtil.BandData> bands = (Collection<VotingDBUtil.BandData>) request.getAttribute("bands");
%>
<h1>Glasanje za omiljeni bend:</h1>
<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste
    glasali!</p>
<ol>
    <% for(VotingDBUtil.BandData band : bands) { %>
    <li><a href="glasanje-glasaj?id=<%= Integer.toString(band.getId()) %>"><%= band.getName() %></a></li>
    <% } %>
</ol>