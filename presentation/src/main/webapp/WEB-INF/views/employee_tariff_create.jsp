<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<%@ page import="ru.simflex.ex.entities.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
Employee tariff management page.
--%>
<html>
<head>
    <title>eCare16 | Employee Tariff Management</title>
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
<c:set var="tariffsActive" value="active"/>
<c:set var="createTariffActive" value="active"/>
<%@include file="employee_header.jsp" %>

<div class="container">
    <div class="jumbotron">

        <%--Create Tariff Form--%>
        <form data-toggle="validator" class="form-horizontal" action="/employeeCreateTariff" method="post">

            <div class="form-group">
                <label for="createTariffName" class="col-sm-4 control-label">
                    <spring:message code="label.tariffName"/></label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" id="createTariffName" name="name"
                           maxlength="30" pattern="<spring:message code="input.tariffNamePattern"/>"
                           title="<spring:message code="input.tariffNameTitle"/>"
                           data-error="<spring:message code="input.tariffNameTitle"/>"
                           placeholder="<spring:message code="label.tariffName"/>" value="${newTariff.name}" required>
                </div>
                <div class="help-block with-errors"></div>
                <div class="text-danger">
                    <c:if test="${!empty notUniqueTariffName}">
                        <spring:message code="label.${notUniqueTariffName}"/>
                    </c:if>
                </div>
            </div>
            <div class="form-group">
                <label for="createTariffMonthlyPayment" class="col-sm-4 control-label">
                    <spring:message code="label.tariffMonthlyPayment"/>
                </label>
                <div class="col-sm-4">
                    <input type="number" class="form-control" id="createTariffMonthlyPayment"
                           min="0" max="999"
                           title="<spring:message code="input.tariffPriceTitle"/>"
                           data-error="<spring:message code="input.tariffPriceTitle"/>"
                           name="monthlyPayment" value="${newTariff.monthlyPayment}"
                           placeholder="<spring:message code="label.tariffMonthlyPayment"/>"
                           required>
                </div>
                <div class="help-block with-errors"></div>
            </div>

            <%--Foreach for optionList--%>
            <c:forEach var="option" items="${optionList}" varStatus="loop">
                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-4">
                        <div class="checkbox">
                            <c:set var="optionChecked" value="false"/>
                            <c:forEach var="possibleOption" items="${newTariff.possibleOptions}">
                                <c:if test="${possibleOption.id == option.id}">
                                    <c:set var="optionChecked" value="checked"/>
                                </c:if>
                            </c:forEach>
                            <label>
                                <input type="checkbox" name="possibleOptions"
                                       value="${option.id}" ${optionChecked}/> ${option.name}
                            </label>
                        </div>
                    </div>
                </div>
            </c:forEach>

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
