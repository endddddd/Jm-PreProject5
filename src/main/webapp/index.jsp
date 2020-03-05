<%--
  Created by IntelliJ IDEA.
  User: andrei
  Date: 03.03.2020
  Time: 1:15
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/login.css">
</head>
<body>
<h1>Sign In Form</h1>
<div id="wrapper">
    <form id="signin" action="/login" method="post" autocomplete="off">
        <input type="text"  name="name" placeholder="username" />
        <input type="password"  name="pass" placeholder="password" />
        <button type="submit">&#xf0da;</button>
    </form>
</div>

</body>
</html>
