<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<%@ page import="ru.simflex.ex.entities.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
Employee phone number management page.
--%>
<html>
<head>
    <title>eCare16 | Employee Phonenumber Management</title>
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
<c:set var="phoneNumbersActive" value="active"/>
<%@include file="employee_header.jsp" %>

<div class="container">
    <div class="jumbotron">
        <h3><p class="text-right"><spring:message code="label.phoneNumberManagement"/></p></h3>
        <hr width="200px" align="right">
        <br>
        <h5><b><spring:message code="label.addNewPhoneNumber"/></b>
            <small><i><spring:message code="label.contains11DigitsStartWith7"/></i></small>
        </h5>

        <form role="form" class="form-inline" action="/employeeCreatePhoneNumber" method="post">
            <div class="form-group">
                <input type="text" pattern="<spring:message code="input.phoneNumberPattern"/>"
                       title="<spring:message code="input.phoneNumberTitle"/>"
                       maxlength="11" name="newPhoneNumber" class="form-control" id="newPhoneNumber"
                       placeholder="<spring:message code="label.newPhoneNumber"/>" required autofocus/>
            </div>
            <button type="submit" class="btn btn-success"><spring:message code="label.add"/></button>
            <c:if test="${phoneNumberInvalid == true}">
                <h7 class="text-danger"><spring:message code="label.invalidNumber"/></h7>
            </c:if>
            <c:if test="${!empty notUniquePhoneNumber}">
                <h7 class="text-danger"><spring:message code="label.${notUniquePhoneNumber}"/></h7>
            </c:if>
        </form>
        <br>
        <table class="table table-condensed table-hover">
            <thead>
            <tr>
                <th><spring:message code="label.phoneNumber"/></th>
                <th><spring:message code="label.availability"/></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="phoneNumber" items="${phoneNumberList}">
                <form action="/employeeDeletePhoneNumber" method="post">

                    <c:set var="highlightSuccess" value=""/>
                    <c:if test="${phoneNumber.phoneNumberString == highlightSuccessPhoneNumber}">
                        <c:set var="highlightSuccess" value="background-color: #D7F0F3;"/>
                    </c:if>
                    <tr style="${highlightSuccess}">
                        <td>
                            <c:out value="${phoneNumber.phoneNumberString}"/>
                        </td>
                        <td>
                            <c:out value="${phoneNumber.available}"/>
                        </td>
                        <td>
                            <c:set var="disabled" value=""/>
                            <c:if test="${phoneNumber.available != true}">
                                <c:set var="disabled" value="disabled"/>
                            </c:if>
                            <input type="hidden" name="id" value="${phoneNumber.id}"/>
                            <input type="submit" value="<spring:message code="label.delete"/>"
                                   class="btn btn-danger btn-xs" ${disabled}/>

                        </td>
                    </tr>
                </form>

            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="${bootstrapMinJs}"></script>
</body>
</html>
