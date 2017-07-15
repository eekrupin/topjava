<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<c:forEach items="${meals}" var="meal">
    <table>
        <tr>

            <td>${meal.dateTime}</td>
            <td>${meal.description}</td>

        </tr>
    </table>
</c:forEach>

</body>
</html>
