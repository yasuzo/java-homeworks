<%@ page import="hr.fer.zemris.java.hw15.forms.CommentForm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    CommentForm commentForm = (CommentForm) request.getAttribute("commentForm");
    if (commentForm == null) {
        commentForm = new CommentForm();
    }
%>
<h3>${blog.title}</h3>
<p>
    ${blog.text}
</p>
<hr>
<div>
    <h5>Add comment</h5>
    <% for(String message : commentForm.getMessages()) { %>
    <p><%= message %></p>
    <% } %>
    <form method="post" action="<%= request.getContextPath() %>/servleti/author/${nick}/${blogId}">
        <c:if test="${sessionScope.get('current.user.id') == null}">
            <label for="email">E-mail</label><br>
            <input id="email" type="text" name="email" value="${commentForm.email}"><br>
        </c:if>
        <label for="message">Message</label><br>
        <textarea id="message" name="message" cols="50" rows="7">${commentForm.message}</textarea><br>
        <input type="submit" value="Post">
    </form>
</div>
<c:forEach var="comment" items="${blog.comments}">
    <hr>
    <div>
        <small>${comment.usersEMail}</small>
        <p>
            ${comment.message}
        </p>
    </div>
</c:forEach>