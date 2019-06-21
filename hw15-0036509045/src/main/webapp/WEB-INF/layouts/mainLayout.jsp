<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="hr.fer.zemris.java.hw15.GlobalConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
    <head>
        <meta charset="UTF-8"/>
        <% if (request.getAttribute(GlobalConstants.TITLE) != null) { %>
        <title><%= request.getAttribute(GlobalConstants.TITLE) %>
        </title>
        <% } %>
    </head>
    <body>
        <c:if test="${not empty sessionScope.get('current.user.id')}">
            <%= session.getAttribute("current.user.firstName") + " " + session.getAttribute("current.user.lastName") %>
            <form action="<%= request.getContextPath() %>/servleti/logout" method="post">
                <input type="submit" value="logout">
            </form>
        </c:if>
        <c:if test="${empty sessionScope.get('current.user.id')}">
            Not logged in
        </c:if>
        <hr>
        <jsp:include page="<%= (String)request.getAttribute(GlobalConstants.INCLUDE_PAGE_BODY) %>"/>
        <hr>
        <div><a href="<%= request.getContextPath() + "/servleti/main" %>">Home</a></div>
    </body>
</html>
