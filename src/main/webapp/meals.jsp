<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
    <title>Meals</title>
</head>
<body>

<style type="text/css">
    .norm {color: green;}
    .exceed {color: red;}
</style>

<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
    <table border="1" cellpadding="4">
        <tr>
            <th width="30">Дата</th>
            <th width="120">Описание</th>
            <th width="30">Калории</th>
            <th colspan=2>Действия</th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <tr class="${meal.exceed ? 'exceed' : 'norm'}">
                <td>
                    <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
                    <fmt:formatDate value="${parsedDate}" pattern="dd.MM.yyyy HH:mm:ss"/>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=edit&mealId=<c:out value="${meal.id}"/>">Обновить</a></td>
                <td><a href="meals?action=delete&mealId=<c:out value="${meal.id}"/>">Удалить</a></td>
            </tr>
        </c:forEach>
    </table>

<br/>
<br/>

<td><a href="meals">Добавить еду</a></td>
<br/>
<br/>
<hl>Добавить еду</hl>

<form method="POST" action='meals' name="frmAddMeal">
    <input type="hidden" readonly="readonly" name="id"
                     value="<c:out value="${meal.id}" />" /> <br />
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />" /> <br />
    Caloriese : <input
        type= "number" name="calories"
        value="<c:out value="${meal.calories}" />" /> <br />
    Date : <input
        type="datetime-local" class= "date" name = "dateTime"
        value = ${meal.dateTime} /> <br />
    <input type="submit" value="Submit" />
</form>



</body>
</html>
