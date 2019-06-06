<%@ page import="hr.fer.zemris.java.webapp2.GlobalConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>OS Report</title>
    <style>
        body {
            background-color: <%= (session.getAttribute("pickedBgCol") != null ? "#" + session.getAttribute("pickedBgCol") : "#ffffff") %>;
        }
    </style>
</head>
<body>
    <h1>OS usage</h1>
    <p>Here are the results of OS usage in survey that we completed</p>
    <img src="reportImage">
</body>
</html>
