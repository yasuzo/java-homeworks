<%@ page import="hr.fer.zemris.java.webapp2.ContextConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    int interval = (int)(System.currentTimeMillis() - (Long)request.getServletContext().getAttribute(ContextConstants.START_TIME));
    int days = interval / 1000 / 3600 / 24;
    int hours = interval % (1000 * 3600 * 24) / 1000 / 3600;
    int minutes = interval % (1000 * 3600) / 1000 / 60;
    int seconds = interval % (1000 * 60) / 1000;
    int millis = interval % 1000;
    String info = String.format("%d days %d hours %d minutes %d seconds and %d milliseconds", days, hours, minutes, seconds, millis);
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>App Info</title>
    <style>
        body {
            background-color: <%= (session.getAttribute("pickedBgCol") != null ? "#" + session.getAttribute("pickedBgCol") : "#ffffff") %>;
        }
    </style>
</head>
<body>
<h1>App Info</h1>
    <p><%= info %></p>
</body>
</html>