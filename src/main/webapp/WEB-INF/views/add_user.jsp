<%--
  Created by IntelliJ IDEA.
  User: nazar
  Date: 10.07.2020
  Time: 12:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Add user</title>
</head>
<body>
<script type="text/javascript" src='https://www.google.com/recaptcha/api.js'></script>

<jsp:include page="_header.jsp"/>

<div align="center">
    <h2 id="page-header">Add user</h2>
    <p style="color: red">${error}</p>

    <form:form name="l_form" action="addUser" method="post" modelAttribute="user" enctype="utf8">
        <table border="0" cellpadding="6">
            <tr>
                <td>Login:</td>
                <td><form:input id="log_input" type="text" name="login"
                                value="${us.login}" required="true" path="login"/></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><form:input type="password" name="password"
                                value="${us.password}" required="true" path="password"/></td>
            </tr>
            <tr>
                <td>Password again:</td>
                <td><input type="password" name="confirmPassword" required="true"/></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><form:input type="text" name="email" required="true"
                                value="${us.email}"
                                pattern="^\w+([.-]?\w+)*@\w+([.-]?\w+)*(.\w{2,3})+$"
                                title="Email address is in the wrong format!" path="email"/></td>
            </tr>
            <tr>
                <td>First Name:</td>
                <td><form:input type="text" name="firstName"
                                value="${us.firstName}" required="true" path="firstName"/></td>
            </tr>
            <tr>
                <td>Last Name:</td>
                <td><form:input type="text" name="lastName" value="${us.lastName}" required="true"
                                path="lastName"/></td>
            </tr>
            <tr>
                <td>Birthday:</td>
                <td><form:input type="date" name="birthday" value="${us.birthday}" required="true"
                                min="1920-01-01" max="2018-12-31" path="birthday"/></td>
            </tr>
            <tr>
                <td>Role:</td>
                <td>
                    <form:select name="role" required="true" path="roleId">
                        <c:choose>
                            <c:when test="${us.roleId == '1'}">
                                <option value="1">Admin</option>
                                <option value="2">User</option>
                            </c:when>
                            <c:otherwise>
                                <option value="2">User</option>
                                <option value="1">Admin</option>
                            </c:otherwise>
                        </c:choose>
                    </form:select>

                </td>
            </tr>
            <tr id="cap">
            </tr>
            <tr>
                <td><input type="submit" value="Ok"></td>
                <td><a href="${pageContext.request.contextPath}/home">Cancel</a></td>
            </tr>
        </table>
    </form:form>

    <script type="text/javascript">
        let input = "${input}";

        if (input === "login") {
            document.l_form.login.style.borderColor = "red";
        } else if (input === "password") {
            document.l_form.password.style.borderColor = "red";
            document.l_form.password_again.style.borderColor = "red";
        } else if (input === "email") {
            document.l_form.email.style.borderColor = "red";
        }
    </script>
</div>

</body>
</html>
