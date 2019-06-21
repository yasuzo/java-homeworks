<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h3>Blogs from <c:out value="${nick}"/></h3>
<c:if test="${showEditOption}">
    <a href="<%= request.getContextPath() + "/servleti/author/" + session.getAttribute("current.user.nick") + "/new"%>">
        _new blog_
    </a><br><br>
</c:if>
<c:forEach var="blog" items="${blogs}">
    <div>
        <b><a href="<%= request.getContextPath() + "/servleti/author/" + request.getAttribute("nick") + "/"%><c:out value="${blog.id}"/>">
            <c:out value="${blog.title}"/>
        </a></b>&nbsp;&nbsp;
        <c:if test="${showEditOption}">
            <small>
                <a href="<%= request.getContextPath() + "/servleti/author/" + session.getAttribute("current.user.nick") + "/edit/"%><c:out value="${blog.id}"/>">
                    edit
                </a>
            </small>
        </c:if>
        <br>
    </div>
</c:forEach>