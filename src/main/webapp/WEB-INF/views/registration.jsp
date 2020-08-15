<%--
  Created by IntelliJ IDEA.
  User: nazar
  Date: 20.07.2020
  Time: 04:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Registration</title>
</head>
<body>

<jsp:include page="add_user.jsp"/>

<script type="text/javascript">
    document.getElementById("page-header").innerText = "Registration";
    document.l_form.action = "registration";
    document.getElementById("cap").innerHTML = "<fmt:bundle basename='application' prefix='google.recaptcha.key.'>" +
        "                    <div class='g-recaptcha'" +
        "                         data-sitekey='<fmt:message key='site'/>'></div>" +
        "                </fmt:bundle>";
</script>

</body>
</html>
