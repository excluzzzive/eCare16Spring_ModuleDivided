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
<c:set var="optionListActive" value="active"/>
<%@include file="employee_header.jsp" %>

<%--Block is showed if editing in process--%>
<c:if test="${editedOption != null}">
    <div class="container">
        <div class="jumbotron">

                <%--Cancel button--%>
            <form action="/showEmployeeOptionListPage" method="post">
                <p class="text-left">
                    <input type="submit" value="<spring:message code="label.cancel"/>" class="btn btn-warning"/>
                </p>
            </form>
            <hr width="150px" align="left">

                <%--Update Option Form--%>
            <form data-toggle="validator" class="form-horizontal" action="/employeeUpdateOption" method="post">

                <input type="hidden" name="id" value="${editedOption.id}"/>

                <div class="form-group">
                    <label for="updateOptionName" class="col-sm-4 control-label"><spring:message
                            code="label.optionName"/></label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="updateOptionName" name="name"
                               maxlength="30" pattern="<spring:message code="input.optionNamePattern"/>"
                               title="<spring:message code="input.optionNameTitle"/>"
                               data-error="<spring:message code="input.optionNameTitle"/>"
                               placeholder="<spring:message code="label.optionName"/>" value="${editedOption.name}"
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
                               value="${editedOption.connectionPrice}" required>
                    </div>
                    <div class="help-block with-errors"></div>
                </div>

                <div class="form-group">
                    <label for="updateOptionMonthlyPayment" class="col-sm-4 control-label"><spring:message
                            code="label.optionMonthlyPayment"/></label>
                    <div class="col-sm-4">
                        <input type="number" class="form-control" id="updateOptionMonthlyPayment" name="monthlyPayment"
                               min="0" max="999" title="<spring:message code="input.optionMonthlyPaymentTitle"/>"
                               data-error="<spring:message code="input.optionMonthlyPaymentTitle"/>"
                               placeholder="<spring:message code="label.optionMonthlyPayment"/>"
                               value="${editedOption.monthlyPayment}" required>
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

                            <c:if test="${!empty optionUsedByContractList}">
                                <c:set var="disabledIncompatibleOption" value="disabled"/>
                                <c:set var="disabledJointOption" value="disabled"/>
                            </c:if>
                            <c:forEach var="option" items="${optionList}" varStatus="loop">
                                <c:if test="${editedOption.id != option.id}">

                                    <c:set var="checkedIncompatibleOption" value=""/>
                                    <c:forEach var="incompatibleOption"
                                               items="${editedOption.incompatibleOptions}">
                                        <c:if test="${incompatibleOption.id == option.id}">
                                            <c:set var="checkedIncompatibleOption" value="checked"/>
                                        </c:if>
                                    </c:forEach>

                                    <c:set var="checkedJointOption" value=""/>
                                    <c:forEach var="jointOption"
                                               items="${editedOption.jointOptions}">
                                        <c:if test="${jointOption.id == option.id}">
                                            <c:set var="checkedJointOption" value="checked"/>
                                        </c:if>
                                    </c:forEach>

                                    <tr>
                                        <td class="text-center">

                                            <input type="checkbox" name="incompatibleOptions"
                                                   value="${option.id}" ${checkedIncompatibleOption}
                                                ${disabledIncompatibleOption}/>

                                            <c:if test="${!empty disabledIncompatibleOption
                                             && !empty checkedIncompatibleOption}">
                                                <input type="hidden" name="incompatibleOptions" value="${option.id}"/>
                                            </c:if>

                                        </td>
                                        <td class="text-center">${option.name}</td>
                                        <td class="text-center">

                                            <input type="checkbox" name="jointOptions"
                                                   value="${option.id}" ${checkedJointOption} ${disabledJointOption}/>

                                            <c:if test="${!empty disabledJointOption && !empty checkedJointOption}">
                                                <input type="hidden" name="jointOptions" value="${option.id}"/>
                                            </c:if>

                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-8">
                        <input type="submit" class="btn btn-success" value="<spring:message code="label.update"/>"/>
                    </div>
                </div>

            </form>
            <br>

                <%--"Used by" table--%>
            <table class="table table-condensed table-hover">
                <thead>
                <tr>
                    <th><spring:message code="label.usedBy"/></th>
                    <th><spring:message code="label.instanceName"/></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <spring:message code="label.aJointOptionFor"/>:
                    </td>
                    <td>
                        <c:forEach var="jointOption" items="${optionUsedAsJointOptionList}"
                                   varStatus="loop">
                            <c:out value="${jointOption.name}"/><c:if test="${!loop.last}">, </c:if>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>
                        <spring:message code="label.usedByTariff"/>:
                    </td>
                    <td>
                        <c:forEach var="phoneNumber" items="${optionUsedByTariffList}"
                                   varStatus="loop">
                            <c:out value="${phoneNumber.name}"/><c:if test="${!loop.last}">, </c:if>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <td>
                        <spring:message code="label.usedByContracts"/>:
                    </td>
                    <td>
                        <c:forEach var="contract" items="${optionUsedByContractList}"
                                   varStatus="loop">
                            <c:out value="${contract.phoneNumber.phoneNumberString}"/><c:if
                                test="${!loop.last}">, </c:if>
                        </c:forEach>
                    </td>
                </tr>
                </tbody>
            </table>

                <%--Delete Option Button--%>
            <c:if test="${(empty optionUsedAsJointOptionList
             && empty optionUsedByContractList) == false}">
                <c:set var="disabledDelete" value="disabled"/>
            </c:if>
            <hr width="150px" align="right">
            <form action="/employeeDeleteOption" method="post">
                <p class="text-right">
                    <input type="hidden" name="id" value="${editedOption.id}"/>
                    <input type="submit" value="<spring:message code="label.delete"/>"
                           class="btn btn-danger" ${disabledDelete}/>
                </p>
            </form>
        </div>
    </div>
</c:if>

<div class="container">
    <div class="jumbotron">
        <h3><p class="text-right"><spring:message code="label.optionManagement"/></p></h3>
        <hr width="200px" align="right">
        <br>

        <table class="table table-condensed table-hover">
            <thead>
            <tr>
                <th></th>
                <th><spring:message code="label.optionName"/></th>
                <th><spring:message code="label.price"/></th>
                <th><spring:message code="label.monthlyPayment"/></th>
                <th><spring:message code="label.incompatibleOptions"/></th>
                <th><spring:message code="label.jointOptions"/></th>
            </tr>
            </thead>
            <tbody>

            <%--If any Option is edited in a moment, all edit buttons must be disabled--%>
            <c:if test="${editedOption != null}">
                <c:set var="disabled" value="disabled"/>
            </c:if>

            <c:forEach var="option" items="${optionList}">
                <c:set var="highlightSuccess" value=""/>
                <c:if test="${option.id == highlightSuccessId}">
                    <c:set var="highlightSuccess" value="background-color: #D7F0F3;"/>
                </c:if>
                <form action="/showEmployeeEditOptionByIdPage" method="post">
                    <tr style="${highlightSuccess}">
                        <td>
                            <input type="hidden" name="editedOptionId" value="${option.id}"/>
                            <input type="submit" value="<spring:message code="label.edit"/>"
                                   class="btn btn-info btn-xs" ${disabled}/>
                        </td>
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
