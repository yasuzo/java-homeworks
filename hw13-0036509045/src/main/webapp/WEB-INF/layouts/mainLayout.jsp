<%@ page import="hr.fer.zemris.java.webapp2.GlobalConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
    <meta charset="UTF-8" />
    <% if(request.getAttribute(GlobalConstants.TITLE) != null) { %>
    <title><%= request.getAttribute(GlobalConstants.TITLE) %></title>
    <% } %>
    <style>
        body {
            background-color: <%= (session.getAttribute("pickedBgCol") != null ? "#" + session.getAttribute("pickedBgCol") : "#ffffff") %>;
        }
    </style>
</head>
<body>
    <jsp:include page="<%= (String)request.getAttribute(GlobalConstants.INCLUDE_PAGE_BODY) %>"/>
</body>
</html>
