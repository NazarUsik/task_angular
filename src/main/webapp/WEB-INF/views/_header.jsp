<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Nazar
  Date: 17.05.2020
  Time: 00:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<body>

<c:set var="user" value="${pageContext.session.getAttribute('loginedUser')}"/>

<var class="navbar" style="float: left; position: fixed;">
    <a href="${pageContext.request.contextPath}/">Home</a>
    |||
    <sec:authorize access="!isAuthenticated()">
        <a href="${pageContext.request.contextPath}/login">Login</a>
        |||
        <a href="${pageContext.request.contextPath}/registration">Registration</a>
    </sec:authorize>
    <sec:authorize access="isAuthenticated()">
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </sec:authorize>
</var>

</body>
</html>
