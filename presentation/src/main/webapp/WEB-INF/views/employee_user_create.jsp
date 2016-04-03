<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<%@ page import="ru.simflex.ex.entities.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
Employee user management page.
--%>
<html>
<head>
    <title>eCare16 | Employee User Management</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <spring:url value="/resources/css/my.css" var="myCss"/>
    <spring:url value="/resources/css/bootstrap.min.css" var="bootstrapMinCss"/>
    <spring:url value="/resources/img/favicon.ico" var="favicon"/>
    <spring:url value="/resources/js/bootstrap.min.js" var="bootstrapMinJs"/>
    <spring:url value="/resources/js/validator.js" var="validatorJs"/>
    <link href="${favicon}" rel="shortcut icon" type="image/x-icon">
    <link href="${myCss}" rel="stylesheet"/>
    <link href="${bootstrapMinCss}" rel="stylesheet"/>
</head>
<body>
<c:set var="usersActive" value="active"/>
<c:set var="createUserActive" value="active"/>
<%@include file="employee_header.jsp" %>
<div class="container">
    <div class="jumbotron">

        <%--Create User Form--%>
        <form data-toggle="validator" class="form-horizontal" action="/employeeCreateUser" method="post">

            <div class="form-group">
                <label for="userFirstName" class="col-sm-4 control-label"><spring:message
                        code="label.userFirstName"/></label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" id="userFirstName" name="firstName"
                           maxlength="20" pattern="<spring:message code="input.userFirstNamePattern"/>"
                           title="<spring:message code="input.userFirstNameTitle"/>"
                           data-error="<spring:message code="input.userFirstNameTitle"/>"
                           placeholder="<spring:message code="label.userFirstName"/>" value="${newUser.firstName}"
                           required>
                </div>
                <div class="help-block with-errors"></div>
            </div>

            <div class="form-group">
                <label for="userLastName" class="col-sm-4 control-label"><spring:message
                        code="label.userLastName"/></label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" id="userLastName" name="lastName"
                           maxlength="20" pattern="<spring:message code="input.userLastNamePattern"/>"
                           title="<spring:message code="input.userLastNameTitle"/>"
                           data-error="<spring:message code="input.userLastNameTitle"/>"
                           placeholder="<spring:message code="label.userLastName"/>" value="${newUser.lastName}"
                           required>
                </div>
                <div class="help-block with-errors"></div>
            </div>

            <div class="form-group">
                <label for="userBirthDate" class="col-sm-4 control-label"><spring:message
                        code="label.userBirthDate"/></label>
                <div class="col-sm-4">
                    <input type="date" class="form-control" id="userBirthDate" name="birthDate"
                           placeholder="<spring:message code="label.userBirthDate"/>" value="${newUserBirthDate}"
                           required>
                </div>
                <div class="help-block">
                    <form:errors path="newUser.birthDate" class="text-danger"/>
                </div>
            </div>

            <div class="form-group">
                <label for="userPassportData" class="col-sm-4 control-label"><spring:message
                        code="label.userPassportData"/></label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" id="userPassportData" name="passportData"
                           maxlength="40" pattern="<spring:message code="input.userPassportDataPattern"/>"
                           title="<spring:message code="input.userPassportDataTitle"/>"
                           data-error="<spring:message code="input.userPassportDataTitle"/>"
                           placeholder="<spring:message code="label.userPassportData"/>" value="${newUser.passportData}"
                           required>
                </div>
                <div class="help-block with-errors"></div>
                <div class="text-danger">
                    <c:if test="${!empty notUniquePassportData}">
                        <spring:message code="label.${notUniquePassportData}"/>
                    </c:if>
                </div>
            </div>

            <div class="form-group">
                <label for="userEmail" class="col-sm-4 control-label"><spring:message code="label.userEmail"/></label>
                <div class="col-sm-4">
                    <input type="email" class="form-control" id="userEmail" name="email"
                           title="<spring:message code="input.userEmailTitle"/>"
                           data-error="<spring:message code="input.userEmailTitle"/>"
                           placeholder="<spring:message code="label.userEmail"/>" value="${newUser.email}" required>
                </div>
                <div class="help-block with-errors"></div>
                <div class="text-danger">
                    <c:if test="${!empty notUniqueEmail}">
                        <spring:message code="label.${notUniqueEmail}"/>
                    </c:if>
                </div>
            </div>

            <div class="form-group">
                <label for="userPassword" class="col-sm-4 control-label"><spring:message
                        code="label.userPassword"/></label>
                <div class="col-sm-4">
                    <input type="password" class="form-control" id="userPassword" name="password"
                           placeholder="<spring:message code="label.userPassword"/>" required>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-4">
                    <div class="checkbox">
                        <label>
                            <c:if test="${newUser.employee == true}">
                                <c:set var="employeeChecked" value="checked"/>
                            </c:if>
                            <input type="checkbox" name="employee"
                                   value="true" ${employeeChecked}/> <spring:message code="label.isEmployee"/>
                        </label>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-8">
                    <input type="submit" class="btn btn-success" value="<spring:message code="label.create"/>"/>
                </div>
            </div>

        </form>
    </div>
</div>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="${bootstrapMinJs}"></script>
<script src="${validatorJs}"></script>
</body>
</html>
