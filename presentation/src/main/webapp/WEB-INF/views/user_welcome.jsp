<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<%@ page import="ru.simflex.ex.entities.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
User welcome page.
--%>
<html>
<head>
    <title>eCare16 | Welcome page</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <spring:url value="/resources/css/my.css" var="myCss"/>
    <spring:url value="/resources/css/bootstrap.min.css" var="bootstrapMinCss"/>
    <spring:url value="/resources/img/favicon.ico" var="favicon"/>
    <spring:url value="/resources/js/bootstrap.min.js" var="bootstrapMinJs"/>
    <link href="${favicon}" rel="shortcut icon" type="image/x-icon">
    <link href="${myCss}" rel="stylesheet"/>
    <link href="${bootstrapMinCss}" rel="stylesheet"/>
</head>
<body>
<%@include file="user_header.jsp" %>

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
        <br>
        <br>
        <br>
        <button class="btn btn-sm btn-default btn-block" data-toggle="collapse" data-target="#tariffs">
            <spring:message code="label.tariffs"/>
        </button>
        <div id="tariffs" class="collapse">
            <table class="table table-condensed table-hover">
                <thead>
                <tr>
                    <th><spring:message code="label.tariffName"/></th>
                    <th><spring:message code="label.tariffPrice"/></th>
                    <th><spring:message code="label.tariffPossibleOptions"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="tariff" items="${tariffList}">
                    <tr>
                        <td>
                            <c:out value="${tariff.name}"/>
                        </td>
                        <td>
                            <c:out value="${tariff.monthlyPayment}"/>
                        </td>
                        <td>
                            <c:forEach var="option" items="${tariff.possibleOptions}" varStatus="loop">
                                <c:out value="${option.name}"/><c:if test="${!loop.last}">, </c:if>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <button class="btn btn-sm btn-default btn-block" data-toggle="collapse" data-target="#options">
            <spring:message code="label.options"/>
        </button>
        <div id="options" class="collapse">
            <table class="table table-condensed table-hover">
                <thead>
                <tr>
                    <th><spring:message code="label.optionName"/></th>
                    <th><spring:message code="label.price"/></th>
                    <th><spring:message code="label.monthlyPayment"/></th>
                    <th><spring:message code="label.incompatibleOptions"/></th>
                    <th><spring:message code="label.jointOptions"/></th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="option" items="${optionList}">
                    <tr>
                        <td>
                            <c:out value="${option.name}"/>
                        </td>
                        <td>
                            <c:out value="${option.connectionPrice}"/>
                        </td>
                        <td>
                            <c:out value="${option.monthlyPayment}"/>
                        </td>
                        <td>
                            <c:forEach var="incompatibleOption" items="${option.incompatibleOptions}" varStatus="loop">
                                <c:out value="${incompatibleOption.name}"/><c:if test="${!loop.last}">, </c:if>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach var="jointOption" items="${option.jointOptions}" varStatus="loop">
                                <c:out value="${jointOption.name}"/><c:if test="${!loop.last}">, </c:if>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="${bootstrapMinJs}"></script>
</body>
</html>
