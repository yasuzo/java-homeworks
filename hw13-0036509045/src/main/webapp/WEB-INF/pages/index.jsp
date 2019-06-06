<%@ page contentType="text/html;charset=UTF-8" %>
<a href="colors">Background color chooser</a><br>
<a href="trigonometric?a=0&b=90">Trigonometric</a>
<form action="trigonometric" method="GET">
    Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
    Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
    <input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
</form>
<a href="stories/funny.jsp">Funny</a><br>
<a href="powers?a=1&b=100&n=3">Powers</a><br>
<a href="appinfo.jsp">App info</a><br>
<a href="glasanje">Glasanje</a>