<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<html>
<head>
    <title>Edit user</title>
</head>
<body>

<jsp:include page="add_user.jsp"/>

<script type="text/javascript">
    document.getElementById("page-header").innerText = "Edit user";
    document.l_form.action = "${pageContext.request.contextPath}/editUser";
    document.getElementById("log_input").setAttribute("readonly", "true")
</script>

</body>
</html>
