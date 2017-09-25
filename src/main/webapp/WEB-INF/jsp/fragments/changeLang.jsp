<%@page contentType="text/html" pageEncoding="UTF-8" %>
<li class="dropdown">
    <a href="#" class="dropdown-toggle" data-toggle="dropdown">${pageContext.response.locale}<b class="caret"></b></a>
    <ul class="dropdown-menu">
        <%--https://stackoverflow.com/questions/2989888/get-request-url-in-jsp-which-is-forwarded-by-servlet--%>
        <li><a href="${requestScope['javax.servlet.forward.request_uri']}?lang=en">English</a></li>
        <li><a href="${requestScope['javax.servlet.forward.request_uri']}?lang=ru">Русский</a></li>
    </ul>
</li>