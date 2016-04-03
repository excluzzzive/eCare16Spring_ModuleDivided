<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
Error page.
--%>
<html>
<head>
    <title>eCare16 | Error :(</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <spring:url value="/resources/css/my.css" var="myCss"/>
    <spring:url value="/resources/css/bootstrap.min.css" var="bootstrapMinCss"/>
    <spring:url value="/resources/img/favicon.ico" var="favicon"/>
    <spring:url value="/resources/img/something_gone_wrong.jpg" var="smthGoneWrong"/>
    <spring:url value="/resources/js/bootstrap.min.js" var="bootstrapMinJs"/>
    <link href="${favicon}" rel="shortcut icon" type="image/x-icon">
    <link href="${myCss}" rel="stylesheet"/>
    <link href="${bootstrapMinCss}" rel="stylesheet"/>
</head>
<body>
<c:if test="${user.employee != true || sessionScope.employeeView != true}">
    <%@include file="user_header.jsp" %>
</c:if>
<c:if test="${user.employee == true && sessionScope.employeeView == true}">
    <%@include file="employee_header.jsp" %>
</c:if>
<div class="container">
    <div class="jumbotron">

        <h3><p class="text-right"><spring:message code="label.smthGoneWrong"/></p></h3>
        <hr width="200px" align="right">
        <br>

        <c:if test="${errorCode == 404}">
            <h3>Error Occurred</h3>
            <strong>Status Code</strong>: ${errorCode} <br>
            <strong>Message</strong>: Page not found <br>
        </c:if>

        <c:if test="${exception != null}">
            <h3>Exception Details</h3>
            <strong>Status Code</strong>: 500 <br>
            <strong>Requested URI</strong>: ${uri} <br>
            <strong>Exception Message</strong>: ${exception.getMessage()} <br>
        </c:if>
        <%--
        <br>
        <div class="row">
            <div class="text-center">
                <img src="${smthGoneWrong}" width="400"/>
            </div>
        </div>
        <br>
        <strong>Exception Stack Trace</strong>:
        <br>
        <c:forEach items="${exception.stackTrace}" var="ste">
            ${ste}
        </c:forEach>
        --%>


    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="${bootstrapMinJs}"></script>
</body>
</html>