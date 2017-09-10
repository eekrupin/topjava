<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="resources/js/datatablesUtil.js" defer></script>
<script type="text/javascript" src="resources/js/mealDatatables.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>


<div class="jumbotron">
    <div class="container">
        <div class="shadow">
            <h3><spring:message code="meal.title"/></h3>
            <br/>
            <div class="view-box">

                <form class="form-horizontal" id="filter">

                    <div class="form-group">
                        <label for="dateTime" class="control-label col-xs-3"><spring:message code="meal.startDate"/></label>
                        <div class="col-xs-9">
                            <input type="date" class="form-control" id="startDate" name="startDate" placeholder="<spring:message code="meal.startDate"/>">
                        </div>

                        <label for="dateTime" class="control-label col-xs-3"><spring:message code="meal.startTime"/></label>
                        <div class="col-xs-9">
                            <input type="time" class="form-control" id="startTime" name="startTime" placeholder="<spring:message code="meal.startTime"/>">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="dateTime" class="control-label col-xs-3"><spring:message code="meal.endDate"/></label>
                        <div class="col-xs-9">
                            <input type="date" class="form-control" id="endDate" name="endDate" placeholder="<spring:message code="meal.endDate"/>">
                        </div>

                        <label for="dateTime" class="control-label col-xs-3"><spring:message code="meal.endTime"/></label>
                        <div class="col-xs-9">
                            <input type="time" class="form-control" id="endTime" name="endTime" placeholder="<spring:message code="meal.endTime"/>">
                        </div>
                    </div>
                    <a class="btn btn-primary" type="button" onclick="filterTable()"><spring:message code="meal.filter"/></a>
                </form>

                <a class="btn btn-primary" onclick="add()">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                    <spring:message code="meal.add"/>
                </a>
                <table class="table table-striped display" id="datatable">
                    <thead>
                    <tr>
                        <th><spring:message code="meal.dateTime"/></th>
                        <th><spring:message code="meal.description"/></th>
                        <th><spring:message code="meal.calories"/></th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <c:forEach items="${meals}" var="meal">
                        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
                        <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                            <td>${fn:formatDateTime(meal.dateTime)}</td>
                            <td>${meal.description}</td>
                            <td>${meal.calories}</td>
                            <td><a><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></td>
                            <td><a onclick="deleteRow(${meal.id})"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>
                        </tr>
                    </c:forEach>
                </table>
            </form>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>

<div class="modal fade" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title"><spring:message code="meal.add"/></h2>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="detailsForm">
                    <input type="hidden" id="id" name="id">

                    <div class="form-group">
                        <label for="dateTime" class="control-label col-xs-3"><spring:message code="meal.dateTime"/></label>

                        <div class="col-xs-9">
                            <input type="datetime-local" class="form-control" id="dateTime" name="dateTime" placeholder="<spring:message code="meal.dateTime"/>">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="description" class="control-label col-xs-3"><spring:message code="meal.description"/></label>

                        <div class="col-xs-9">
                            <input type="text" class="form-control" id="description" name="description" placeholder="<spring:message code="meal.description"/>">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="calories" class="control-label col-xs-3"><spring:message code="meal.calories"/></label>

                        <div class="col-xs-9">
                            <input type="number" class="form-control" id="calories" name="calories" placeholder="<spring:message code="meal.calories"/>">
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-offset-3 col-xs-9">
                            <button type="submit" class="btn btn-primary">
                                <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%--<section>--%>
    <%--<h3><spring:message code="meal.title"/></h3>--%>

    <%--<form method="post" action="meals/filter">--%>
        <%--<dl>--%>
            <%--<dt><spring:message code="meal.startDate"/>:</dt>--%>
            <%--<dd><input type="date" name="startDate" value="${param.startDate}"></dd>--%>
        <%--</dl>--%>
        <%--<dl>--%>
            <%--<dt><spring:message code="meal.endDate"/>:</dt>--%>
            <%--<dd><input type="date" name="endDate" value="${param.endDate}"></dd>--%>
        <%--</dl>--%>
        <%--<dl>--%>
            <%--<dt><spring:message code="meal.startTime"/>:</dt>--%>
            <%--<dd><input type="time" name="startTime" value="${param.startTime}"></dd>--%>
        <%--</dl>--%>
        <%--<dl>--%>
            <%--<dt><spring:message code="meal.endTime"/>:</dt>--%>
            <%--<dd><input type="time" name="endTime" value="${param.endTime}"></dd>--%>
        <%--</dl>--%>
        <%--<button type="submit"><spring:message code="meal.filter"/></button>--%>
    <%--</form>--%>
       <%----%>
    <%--<hr>--%>
    <%--<a href="meals/create"><spring:message code="meal.add"/></a>--%>
    <%--<hr>--%>
    <%--<table border="1" cellpadding="8" cellspacing="0">--%>
        <%--<thead>--%>
        <%--<tr>--%>
            <%--<th><spring:message code="meal.dateTime"/></th>--%>
            <%--<th><spring:message code="meal.description"/></th>--%>
            <%--<th><spring:message code="meal.calories"/></th>--%>
            <%--<th colspan="2"><spring:message code="common.actions"/></th>--%>
        <%--</tr>--%>
        <%--</thead>--%>
        <%--<c:forEach items="${meals}" var="meal">--%>
            <%--<jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>--%>
            <%--<tr class="${meal.exceed ? 'exceeded' : 'normal'}">--%>
                <%--<td>--%>
                        <%--&lt;%&ndash;${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}&ndash;%&gt;--%>
                        <%--&lt;%&ndash;<%=TimeUtil.toString(meal.getDateTime())%>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;${fn:replace(meal.dateTime, 'T', ' ')}&ndash;%&gt;--%>
                        <%--${fn:formatDateTime(meal.dateTime)}--%>
                <%--</td>--%>
                <%--<td>${meal.description}</td>--%>
                <%--<td>${meal.calories}</td>--%>
                <%--<td><a href="meals/update?id=${meal.id}"><spring:message code="common.update"/></a></td>--%>
                <%--<td><a href="meals/delete?id=${meal.id}"><spring:message code="common.delete"/></a></td>--%>
            <%--</tr>--%>
        <%--</c:forEach>--%>
    <%--</table>--%>
<%--</section>--%>

</body>
</html>