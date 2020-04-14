<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="BASE" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>用户信息</title>
</head>
<body>
<h1>用户信息</h1>
<table>
    <tr>
        <th>用户id</th>
        <th>名称</th>
        <th>年龄</th>
    </tr>
    <tr>
        <td>${userinfo.id}</td>
        <td>${userinfo.name}</td>
        <td>${userinfo.age}</td>
    </tr>
</table>
</body>
</html>
