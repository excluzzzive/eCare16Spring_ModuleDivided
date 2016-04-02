<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<%@ page import="ru.simflex.ex.entities.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
Employee contract management page.
--%>
<html>
<head>
    <title>eCare16 | Employee Contract Management</title>
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
<c:set var="contractsActive" value="active"/>
<c:set var="createContractActive" value="active"/>
<%@include file="employee_header.jsp" %>

<div class="container">
    <div class="jumbotron">
        <form class="form-horizontal" action="/showEmployeeContractCreatePage" method="post">

            <c:set var="newContract" value="${sessionScope.newContract}"/>

            <table width="100%">
                <tr>
                    <td align="left">
                        <c:if test="${currentStep != 'stepOne'}">
                            <input type="submit" name="btnBack" value="<spring:message code="label.back"/>"
                                   class="btn btn-warning"/>
                        </c:if>
                    </td>
                    <td align="right">
                        <c:if test="${currentStep != 'stepSummary'}">
                            <input type="submit" name="btnNext" value="<spring:message code="label.next"/>"
                                   class="btn btn-warning"/>
                        </c:if>
                        <c:if test="${currentStep == 'stepSummary'}">
                            <c:if test="${canFinish == false}">
                                <c:set var="finishDisabled" value="disabled"/>
                            </c:if>
                            <input type="submit" name="btnNext" value="<spring:message code="label.finish"/>"
                                ${finishDisabled} class="btn btn-success"/>
                        </c:if>
                    </td>
                </tr>
            </table>
            <br>


            <%--USER--%>
            <div class="form-group">
                <label for="owner" class="col-sm-4 control-label">
                    <spring:message code="label.owner"/></label>
                <div class="col-sm-4">

                    <c:if test="${currentStep == 'stepOne'}">
                        <input type="hidden" name="currentStep" value="stepOne">
                        <select class="form-control" id="owner" name="userId">
                            <c:if test="${newContract.user == null}">
                                <option value="" disabled selected><spring:message code="label.selectAUser"/></option>
                            </c:if>
                            <c:if test="${newContract.user != null}">
                                <option value="${newContract.user.id}" selected>
                                        ${newContract.user.firstName} ${newContract.user.lastName}
                                </option>
                            </c:if>
                            <c:forEach var="user" items="${userList}">
                                <c:if test="${newContract.user.id != user.id}">
                                    <option value="${user.id}">${user.firstName} ${user.lastName}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </c:if>

                    <c:if test="${currentStep != 'stepOne'}">
                        <input type="text" class="form-control"
                               value="${newContract.user.firstName} ${newContract.user.lastName}"
                               disabled>
                    </c:if>
                </div>
                <div class="col-sm-4 text-danger" style="padding-top: 7px">
                    <c:if test="${userNotSelected == true}">
                        Please select a user
                    </c:if>
                </div>
            </div>


            <%--PHONE NUMBER--%>
            <div class="form-group">
                <label for="phoneNumber" class="col-sm-4 control-label">
                    <spring:message code="label.phoneNumber"/></label>
                <div class="col-sm-4">

                    <c:if test="${currentStep == 'stepTwo'}">
                        <input type="hidden" name="currentStep" value="stepTwo">
                        <select class="form-control" id="phoneNumber" name="phoneNumberId">
                            <c:if test="${newContract.phoneNumber == null}">
                                <option value="" disabled selected><spring:message
                                        code="label.selectAPhoneNumber"/></option>
                            </c:if>
                            <c:if test="${newContract.phoneNumber != null}">
                                <option value="${newContract.phoneNumber.id}" selected>
                                        ${newContract.phoneNumber.phoneNumberString}
                                </option>
                            </c:if>
                            <c:forEach var="phoneNumber" items="${phoneNumberList}">
                                <c:if test="${newContract.phoneNumber.id != phoneNumber.id}">
                                    <option value="${phoneNumber.id}">${phoneNumber.phoneNumberString}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </c:if>

                    <c:if test="${currentStep != 'stepTwo'}">
                        <input type="text" class="form-control"
                               value="${newContract.phoneNumber.phoneNumberString}"
                               disabled>
                    </c:if>

                </div>
                <div class="col-sm-4 text-danger" style="padding-top: 7px">
                    <c:if test="${!empty notAvailablePhoneNumber}">
                        <spring:message code="label.${notAvailablePhoneNumber}"/>
                    </c:if>
                    <c:if test="${phoneNumberNotSelected == true}">
                        Please select a phone number
                    </c:if>
                </div>
            </div>

            <%--TARIFF AND OPTIONS--%>
            <div class="form-group">
                <label for="tariffSelect" class="col-sm-4 control-label">
                    <spring:message code="label.tariff"/></label>
                <div class="col-sm-4">

                    <c:if test="${currentStep == 'stepThree'}">
                        <input type="hidden" name="currentStep" value="stepThree">
                        <select class="form-control" id="tariffSelect" name="tariffId">
                            <c:if test="${newContract.tariff == null}">
                                <option value="" disabled selected><spring:message code="label.selectATariff"/></option>
                            </c:if>
                            <c:if test="${newContract.tariff != null}">
                                <option value="${newContract.tariff.id}" selected>${newContract.tariff.name}</option>
                            </c:if>
                            <c:forEach var="phoneNumber" items="${tariffList}">
                                <c:if test="${newContract.tariff.id != phoneNumber.id}">
                                    <option value="${phoneNumber.id}">${phoneNumber.name}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </c:if>

                    <c:if test="${currentStep != 'stepThree'}">
                        <input type="text" class="form-control"
                               value="${newContract.tariff.name}"
                               disabled>
                    </c:if>
                </div>
                <div class="col-sm-4 text-danger" style="padding-top: 7px">
                    <c:if test="${tariffNotSelected == true}">
                        Please select a tariff
                    </c:if>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <spring:message code="label.chosenOptions"/></label>
                <div class="col-sm-8">
                    <h5>
                        <c:forEach var="option" items="${newContract.chosenOptions}" varStatus="loop">
                            <c:out value="${option.name}"/><c:if test="${!loop.last}">, </c:if>
                        </c:forEach>
                    </h5>
                </div>
            </div>

            <c:if test="${currentStep == 'stepThree'}">
                <c:forEach var="phoneNumber" items="${tariffList}">

                    <%--Display selected tariff option block--%>
                    <c:set var="tariffDisplay" value="display: none;"/>
                    <c:if test="${newContract.tariff.id == phoneNumber.id}">
                        <c:set var="tariffDisplay" value="display: block;"/>
                    </c:if>

                    <div class="form-group" id="${phoneNumber.id}" style="${tariffDisplay}">

                        <c:if test="${newContract.tariff.id == phoneNumber.id}">
                            <c:if test="${!empty incompatibleOptionsSet}">
                                <div class="col-sm-offset-4 col-sm-8 text-danger">
                                    <spring:message code="label.contractIncompatibleOptionsMessage"/>
                                    <br> -
                                    <c:forEach var="incompatibleOption" items="${incompatibleOptionsSet}"
                                               varStatus="loop">
                                        <c:out value="${incompatibleOption.name}"/><c:if test="${!loop.last}">, </c:if>
                                    </c:forEach>
                                </div>
                            </c:if>

                            <c:if test="${!empty jointOptionsSet}">
                                <div class="col-sm-offset-4 col-sm-8 text-danger">
                                    <spring:message code="label.contractJointOptionsMessage"/>
                                    <c:forEach var="jointOption" items="${jointOptionsSet}"
                                               varStatus="loop">
                                        <br> - <c:out value="${jointOption.name}"/> <spring:message code="label.needs"/>
                                        <c:forEach var="option" items="${jointOption.jointOptions}" varStatus="loop">
                                            <c:out value="${option.name}"/><c:if test="${!loop.last}"> or </c:if>
                                        </c:forEach>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </c:if>

                        <c:forEach var="possibleOption" items="${phoneNumber.possibleOptions}">
                            <%--Check selected tariff chosen options--%>
                            <c:set var="checkedChosenOption" value=""/>
                            <c:if test="${newContract.tariff.id == phoneNumber.id}">
                                <c:forEach var="chosenOption" items="${newContract.chosenOptions}">
                                    <c:if test="${possibleOption.id == chosenOption.id}">
                                        <c:set var="checkedChosenOption" value="checked"/>
                                    </c:if>
                                </c:forEach>
                            </c:if>

                            <div class="col-sm-offset-4 col-sm-8">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="${phoneNumber.id}_chosenOptions"
                                               id="${phoneNumber.id}"
                                               value="${possibleOption.id}" ${checkedChosenOption}/> ${possibleOption.name}
                                    </label>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:forEach>
            </c:if>

            <c:if test="${currentStep == 'stepSummary'}">
                <input type="hidden" name="currentStep" value="stepSummary">
                <c:if test="${exception != null}">
                    <div class="form-group">
                        <div class="col-sm-offset-4 col-sm-8 text-danger">
                            <spring:message code="label.errorDuringContractCreating"/>
                            <br>
                            <strong>Exception Message</strong>: ${exception.getMessage()}
                        </div>
                    </div>
                </c:if>
            </c:if>
        </form>
    </div>
</div>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="${bootstrapMinJs}"></script>

<script type="text/javascript">
    $(function () {
        document.getElementById('tariffSelect').addEventListener('change', function () {
            <c:forEach var="tariffOuter" items="${tariffList}">
            if (this.value == ${tariffOuter.id}) {
                <c:forEach var="tariffInner" items="${tariffList}">
                <c:if test="${tariffInner.id == tariffOuter.id}">
                document.getElementById('${tariffInner.id}').style.display = 'block';
                </c:if>
                <c:if test="${tariffInner.id != tariffOuter.id}">
                document.getElementById('${tariffInner.id}').style.display = 'none';
                </c:if>
                </c:forEach>
            }
            </c:forEach>
        });
    });
</script>
</body>
</html>
