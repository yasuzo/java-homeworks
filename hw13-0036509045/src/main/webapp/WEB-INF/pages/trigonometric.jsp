<%@ page import="java.util.List" %>
<table border="1">
    <tr>
        <th>Angle</th>
        <th>Sin</th>
        <th>Cos</th>
    </tr>
    <%
        int a = (Integer)request.getAttribute("a");
        int b = (Integer)request.getAttribute("b");
        List<Double> sinList = (List<Double>) request.getAttribute("sinList");
        List<Double> cosList = (List<Double>) request.getAttribute("cosList");
    %>
    <% for(int i = a; i <= b; i++) {%>
    <tr>
        <th><%= i %></th>
        <th><%= sinList.get(i - a) %></th>
        <th><%= cosList.get(i - a) %></th>
    </tr>
    <% } %>
</table>