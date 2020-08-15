<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <title>Login</title>
</head>

<body>

<jsp:include page="_header.jsp"/>

<div align="center">
    <h2>Login</h2>

    <c:if test="${param.error != null}">
        <p style="color: red">Invalid login or password!</p>
    </c:if>
    <c:url value="/j_spring_security_check" var="loginUrl"/>

    <form action="${loginUrl}" method="post">
        <h4>Please sign in</h4>
        <c:if test="${param.error != null}">
            <p>Incorrect login or password!</p>
        </c:if>
        <table border="0" cellpadding="4">
            <tr>
                <td>Login:</td>
                <td><input type="text" name='j_username' value="" required="true"/></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td>
                    <input type="password" name='j_password' required="true"/>
                </td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="Login"></td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>
