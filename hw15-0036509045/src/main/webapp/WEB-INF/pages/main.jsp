<%@ page import="hr.fer.zemris.java.hw15.forms.LoginForm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:if test="${empty sessionScope.get('current.user.id')}">
    <%
        LoginForm loginForm = (LoginForm) request.getAttribute("loginForm");
        if (loginForm == null) {
            loginForm = new LoginForm();
        }
    %>
<div>
    <h4>Log in</h4>
    <% for(String message : loginForm.getMessages()) { %>
    <p><%= message %></p>
    <% } %>
    <form method="post" action="<%= request.getContextPath() + "/servleti/main" %>">
        <div>
            <label>Nickname</label>
            <input name="nick" type="text">
        </div>
        <div>
            <label>Password</label>
            <input name="password" type="password">
        </div>
        <div>
            <input type="submit">
        </div>
    </form>
    <a href="<%= request.getContextPath() + "/servleti/register" %>">Register as a new user</a>
</div>
</c:if>

<div>
    <h4>Registered authors</h4>
    <c:forEach var="author" items="${authors}">
        <a href="<%= request.getContextPath() %><c:out value="/servleti/author/${author.nick}"/>">
            <c:out value="${author.nick}"/>
        </a><br>
    </c:forEach>
</div>