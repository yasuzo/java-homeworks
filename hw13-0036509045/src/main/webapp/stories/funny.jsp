<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%!
    private List<String> colors = Arrays.asList("#000000", "#ff0000", "#0ff00f");
%>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Funny</title>
    <style>
        body {
            background-color: <%= (session.getAttribute("pickedBgCol") != null ? "#" + session.getAttribute("pickedBgCol") : "#ffffff") %>;
        }

        p {
            color: <%= colors.get((int)(Math.random() * colors.size()))%>;
        }
    </style>
</head>
<body>
<p>Humour, also spelt as humor, is the tendency of experiences to provoke laughter and provide amusement. The term
    derives from the humoral medicine of the ancient Greeks, which taught that the balance of fluids in the human body,
    known as humours, controlled human health and emotion.</p>
</body>
</html>
