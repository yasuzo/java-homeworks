<%--
  Created by IntelliJ IDEA.
  User: yasuzo
  Date: 6/11/19
  Time: 1:35 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <p>
        <%= request.getAttribute("message") %>
    </p>
</body>
</html>
