<%--
  Created by IntelliJ IDEA.
  User: nazar
  Date: 11.07.2020
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01
Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Access Denied</title>
</head>
<body>
<jsp:include page="_header.jsp"/>

<div align="center">
    <h2>Access Denied</h2>
    <p style="color: red">You do not have access to the page, or an error occurred on the page: ${error}</p>
</div>
</body>
</html>
