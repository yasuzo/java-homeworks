<%@ page import="hr.fer.zemris.java.hw15.forms.BlogForm" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    BlogForm blogForm = (BlogForm) request.getAttribute("blogForm");
    if (blogForm == null) {
        blogForm = new BlogForm();
    }
%>

<% for(String message : blogForm.getMessages()) { %>
    <p><%= message %></p>
<% } %>
<form method="post" action="<%= request.getContextPath() + "/servleti/author/" + session.getAttribute("current.user.nick") + "/new" %>">
    <label for="title">Title</label><br>
    <input id="title" type="text" name="title" value="<%= blogForm.getTitle() %>"><br>
    <label for="text">Text</label><br>
    <textarea id="text" name="text" cols="200" rows="15"><%= blogForm.getText() %></textarea><br>
    <input type="submit" value="Create">
</form>