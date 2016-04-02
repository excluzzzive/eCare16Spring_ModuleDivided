<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<%@ page import="ru.simflex.ex.entities.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
Employee welcome page.
--%>
<html>
<head>
    <title>eCare16 | Welcome page</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <spring:url value="/resources/css/my.css" var="myCss" />
    <spring:url value="/resources/css/bootstrap.min.css" var="bootstrapMinCss" />
    <spring:url value="/resources/img/favicon.ico" var="favicon" />
    <spring:url value="/resources/js/bootstrap.min.js" var="bootstrapMinJs" />
    <link href="${favicon}" rel="shortcut icon" type="image/x-icon">
    <link href="${myCss}" rel="stylesheet" />
    <link href="${bootstrapMinCss}" rel="stylesheet" />
</head>
<body>
<%@include file="employee_header.jsp"%>

<div class="container">
    <div class="jumbotron">
        <blockquote class="blockquote-reverse">
            <spring:message code="label.hello"/>, <c:out value="${sessionScope.user.firstName}"/><br>
            <spring:message code="label.greetingMessage"/>
            <footer><spring:message code="label.greetingMessageSign"/></footer>
        </blockquote>
        <br>
        <spring:message code="label.welcomeText01"/>
        <br>
        <spring:message code="label.welcomeText02"/>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="${bootstrapMinJs}"></script>
</body>
</html>
