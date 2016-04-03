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
<c:set var="tariffListActive" value="active"/>
<%@include file="employee_header.jsp" %>

<%--Block is showed if editing in process--%>
<c:if test="${editedTariff != null}">
    <div class="container">
        <div class="jumbotron">

                <%--Cancel button--%>
            <form action="/showEmployeeTariffListPage" method="post">
                <p class="text-left">
                    <input type="submit" value="<spring:message code="label.cancel"/>" class="btn btn-warning"/>
                </p>
            </form>
            <hr width="150px" align="left">

                <%--Update Tariff Form--%>
            <form data-toggle="validator" class="form-horizontal" action="/employeeUpdateTariff" method="post">

                <input type="hidden" name="id" value="${editedTariff.id}"/>

                <div class="form-group">
                    <label for="createTariffName" class="col-sm-4 control-label">
                        <spring:message code="label.tariffName"/></label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="createTariffName" name="name"
                               maxlength="30" pattern="<spring:message code="input.tariffNamePattern"/>"
                               title="<spring:message code="input.tariffNameTitle"/>"
                               data-error="<spring:message code="input.tariffNameTitle"/>"
                               placeholder="<spring:message code="label.tariffName"/>" value="${editedTariff.name}"
                               required>
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
                               name="monthlyPayment" value="${editedTariff.monthlyPayment}"
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
                                <c:forEach var="possibleOption" items="${editedTariff.possibleOptions}">
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
                        <input type="submit" class="btn btn-success" value="<spring:message code="label.update"/>"/>
                    </div>
                </div>
            </form>
            <br>

                <%--"Used by" table--%>
            <table class="table table-condensed table-hover">
                <tbody>
                <tr>
                    <td>
                        <spring:message code="label.usedByContracts"/>:
                    </td>
                    <td>
                        <c:forEach var="contract" items="${contractListByTariff}"
                                   varStatus="loop">
                            <c:out value="${contract.phoneNumber.phoneNumberString}"/><c:if
                                test="${!loop.last}">, </c:if>
                        </c:forEach>
                    </td>
                </tr>
                </tbody>
            </table>

                <%--Delete Tariff Button--%>
            <c:if test="${!empty contractListByTariff}">
                <c:set var="disabledDelete" value="disabled"/>
            </c:if>
            <hr width="150px" align="right">
            <form action="/employeeDeleteTariff" method="post">
                <p class="text-right">
                    <input type="hidden" name="id" value="${editedTariff.id}"/>
                    <input type="submit" value="<spring:message code="label.delete"/>"
                           class="btn btn-danger" ${disabledDelete}/>
                </p>
            </form>
        </div>
    </div>
</c:if>

<div class="container">
    <div class="jumbotron">
        <h3><p class="text-right"><spring:message code="label.tariffManagement"/></p></h3>
        <hr width="200px" align="right">
        <br>

        <table class="table table-condensed table-hover">
            <thead>
            <tr>
                <th></th>
                <th><spring:message code="label.tariffName"/></th>
                <th><spring:message code="label.tariffPrice"/></th>
                <th><spring:message code="label.tariffPossibleOptions"/></th>
            </tr>
            </thead>
            <tbody>

            <%--If any Tariff is edited in a moment, all edit buttons must be disabled--%>
            <c:if test="${editedTariff != null}">
                <c:set var="disabled" value="disabled"/>
            </c:if>

            <c:forEach var="tariff" items="${tariffList}">
                <c:set var="highlightSuccess" value=""/>
                <c:if test="${tariff.id == highlightSuccessId}">
                    <c:set var="highlightSuccess" value="background-color: #D7F0F3;"/>
                </c:if>
                <form action="/showEmployeeEditTariffByIdPage" method="post">
                    <tr style="${highlightSuccess}">
                        <td>
                            <input type="hidden" name="id" value="${tariff.id}"/>
                            <input type="submit" value="Edit" class="btn btn-info btn-xs" ${disabled}/>
                        </td>
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
                </form>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="${bootstrapMinJs}"></script>
<script src="${validatorJs}"></script>
</body>
</html>
