<%--
  Created by IntelliJ IDEA.
  User: nazar
  Date: 10.07.2020
  Time: 18:13
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="ls" uri="/WEB-INF/tag/userListTag.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>Home</title>
</head>
<style type="text/css">
    table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
        width: 80%;
    }

    th {
        background-color: #9e9e9e;
    }

    td, th {
        border: 3px solid gray;
        text-align: left;
        padding: 8px;
    }

    tr:nth-child(odd) {
        background-color: #dddddd;
    }

    a {

    }
</style>
<body>
<jsp:include page="_header.jsp"/>
<div align="center">
    <c:set var="user" value="${pageContext.session.getAttribute('loginedUser')}"/>

    <h2>Hello
        <security:authentication property="principal.username"/>
    </h2>

    <security:authorize ifAnyGranted="ADMIN">
        <a href="${pageContext.request.contextPath}/addUser">Add new user</a>
        <ls:userList request="${pageContext.request}" response="${pageContext.response}"/>

        <script type="text/javascript">
            function confirm_click() {
                return confirm("Are you sure ?");
            }
        </script>
    </security:authorize>
</div>
</body>
</html>
