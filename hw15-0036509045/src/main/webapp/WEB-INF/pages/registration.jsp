<%@ page import="hr.fer.zemris.java.hw15.forms.UserForm" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    UserForm form = (UserForm) request.getAttribute("userForm");
    if (form == null) {
        form = new UserForm();
    }
%>

<div>
    <h3>Register</h3>
    <% for(String message : form.getMessages()) { %>
        <p><%= message %></p>
    <% } %>

    <form method="post" action="<%= request.getContextPath() + "/servleti/register" %>">
        <div>
            <label>First name</label>
            <input name="firstName" type="text" value="<%= form.getFirstName() %>">
        </div>
        <div>
            <label>Last name</label>
            <input name="lastName" type="text" value="<%= form.getLastName() %>">
        </div>
        <div>
            <label>E-mail</label>
            <input name="email" type="email" value="<%= form.getEmail() %>">
        </div>
        <div>
            <label>Nickname</label>
            <input name="nick" type="text" value="<%= form.getNick() %>">
        </div>
        <div>
            <label>Password</label>
            <input name="password" type="password">
        </div>
        <hr>
        <div>
            <input type="submit" value="Register">
        </div>
    </form>
</div>