<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<%@ page import="ru.simflex.ex.entities.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
Employee option management page.
--%>
<html>
<head>
    <title>eCare16 | Employee Option Management</title>
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
<c:set var="optionsActive" value="active"/>
<c:set var="createOptionActive" value="active"/>
<%@include file="employee_header.jsp" %>


<div class="container">
    <div class="jumbotron">

        <%--Create Option Form--%>
        <form data-toggle="validator" class="form-horizontal" action="/employeeCreateOption" method="post">

            <div class="form-group">
                <label for="updateOptionName" class="col-sm-4 control-label"><spring:message
                        code="label.optionName"/></label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" id="updateOptionName" name="name"
                           maxlength="30" pattern="<spring:message code="input.optionNamePattern"/>"
                           title="<spring:message code="input.optionNameTitle"/>"
                           data-error="<spring:message code="input.optionNameTitle"/>"
                           placeholder="<spring:message code="label.optionName"/>" value="${newOption.name}"
                           required>
                </div>
                <div class="help-block with-errors"></div>
                <div class="text-danger">
                    <c:if test="${!empty notUniqueOptionName}">
                        <spring:message code="label.${notUniqueOptionName}"/>
                    </c:if>
                </div>
            </div>

            <div class="form-group">
                <label for="updateOptionPrice" class="col-sm-4 control-label"><spring:message
                        code="label.optionConnectionPrice"/></label>
                <div class="col-sm-4">
                    <input type="number" class="form-control" id="updateOptionPrice" name="connectionPrice"
                           min="0" max="999" title="<spring:message code="input.optionPriceTitle"/>"
                           data-error="<spring:message code="input.optionPriceTitle"/>"
                           placeholder="<spring:message code="label.optionConnectionPrice"/>"
                           value="${newOption.connectionPrice}" required>
                </div>
                <div class="help-block with-errors"></div>
            </div>

            <div class="form-group">
                <label for="updateOptionMonthlyPayment" class="col-sm-4 control-label"><spring:message
                        code="label.optionMonthlyPayment"/></label>
                <div class="col-sm-4">
                    <input type="number" class="form-control" id="updateOptionMonthlyPayment"
                           min="0" max="999"
                           title="<spring:message code="input.optionMonthlyPaymentTitle"/>"
                           data-error="<spring:message code="input.optionMonthlyPaymentTitle"/>"
                           name="monthlyPayment" placeholder="<spring:message code="label.optionMonthlyPayment"/>"
                           value="${newOption.monthlyPayment}" required>
                </div>
                <div class="help-block with-errors"></div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-6 text-center">
                    <table class="table table-condensed table-hover">
                        <thead>
                        <tr>
                            <th width="90" class="text-center"><spring:message code="label.incompatible"/></th>
                            <th width="180" class="text-center"><spring:message code="label.optionName"/></th>
                            <th width="90" class="text-center"><spring:message code="label.joint"/></th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach var="option" items="${optionList}" varStatus="loop">


                            <c:set var="checkedIncompatibleOption" value=""/>
                            <c:forEach var="incompatibleOption"
                                       items="${newOption.incompatibleOptions}">
                                <c:if test="${incompatibleOption.id == option.id}">
                                    <c:set var="checkedIncompatibleOption" value="checked"/>
                                </c:if>
                            </c:forEach>

                            <c:set var="checkedJointOption" value=""/>
                            <c:forEach var="jointOption"
                                       items="${newOption.jointOptions}">
                                <c:if test="${jointOption.id == option.id}">
                                    <c:set var="checkedJointOption" value="checked"/>
                                </c:if>
                            </c:forEach>

                            <tr>
                                <td class="text-center">
                                    <input type="checkbox" name="incompatibleOptions"
                                           value="${option.id}" ${checkedIncompatibleOption}/>
                                </td>
                                <td class="text-center">${option.name}</td>
                                <td class="text-center">
                                    <input type="checkbox" name="jointOptions"
                                           value="${option.id}" ${checkedJointOption} />
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
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
