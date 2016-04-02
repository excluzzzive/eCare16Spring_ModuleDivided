<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<%@ page import="ru.simflex.ex.entities.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
User contract management page.
--%>
<html>
<head>
    <title>eCare16 | User Contract Management</title>
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
<%@include file="user_header.jsp" %>

<%--Block is showed if editing in process--%>
<c:if test="${editedContract != null}">
    <div class="container">
        <div class="jumbotron">

                <%--Cancel button--%>
            <form action="/showUserContractListPage" method="post">
                <p class="text-left">
                    <input type="submit" value="<spring:message code="label.cancel"/>" class="btn btn-warning"/>
                </p>
            </form>
            <hr width="150px" align="left">

                <%--Update Contract Form--%>
            <form class="form-horizontal" action="/userUpdateContract" method="post">

                <input type="hidden" name="id" value="${editedContract.id}"/>

                <div class="form-group">
                    <label for="owner" class="col-sm-4 control-label">
                        <spring:message code="label.owner"/></label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="owner"
                               value="${editedContract.user.firstName} ${editedContract.user.lastName}" disabled/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="phoneNumber" class="col-sm-4 control-label">
                        <spring:message code="label.phoneNumber"/></label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="phoneNumber"
                               value="${editedContract.phoneNumber.phoneNumberString}" disabled/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="tariffSelect" class="col-sm-4 control-label">
                        <spring:message code="label.tariff"/></label>
                    <div class="col-sm-4">
                        <select class="form-control" id="tariffSelect" name="tariffId">
                            <option value="${editedContract.tariff.id}" selected>${editedContract.tariff.name}</option>
                            <c:forEach var="tariff" items="${tariffList}">
                                <c:if test="${editedContract.tariff.id != tariff.id}">
                                    <option value="${tariff.id}">${tariff.name}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <c:forEach var="tariff" items="${tariffList}">

                    <%--Display selected tariff option block--%>
                    <c:set var="tariffDisplay" value="display: none;"/>
                    <c:if test="${editedContract.tariff.id == tariff.id}">
                        <c:set var="tariffDisplay" value="display: block;"/>
                    </c:if>

                    <div class="form-group" id="${tariff.id}" style="${tariffDisplay}">

                        <c:if test="${editedContract.tariff.id == tariff.id}">
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


                        <c:forEach var="possibleOption" items="${tariff.possibleOptions}">

                            <%--Check selected tariff chosen options--%>
                            <c:set var="checkedChosenOption" value=""/>
                            <c:if test="${editedContract.tariff.id == tariff.id}">
                                <c:forEach var="chosenOption" items="${editedContract.chosenOptions}">
                                    <c:if test="${possibleOption.id == chosenOption.id}">
                                        <c:set var="checkedChosenOption" value="checked"/>
                                    </c:if>
                                </c:forEach>
                            </c:if>

                            <div class="col-sm-offset-4 col-sm-8">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="${tariff.id}_chosenOptions"
                                               id="${tariff.id}"
                                               value="${possibleOption.id}" ${checkedChosenOption}/> ${possibleOption.name}
                                    </label>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:forEach>

                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-8">
                        <input type="submit" class="btn btn-success" value="<spring:message code="label.update"/>"/>
                    </div>
                </div>
            </form>
            <br>
        </div>
    </div>
</c:if>

<div class="container">
    <div class="jumbotron">
        <h3><p class="text-right"><spring:message code="label.contractManagement"/></p></h3>
        <hr width="200px" align="right">
        <br>

        <table class="table table-condensed table-hover">
            <thead>
            <tr>
                <th></th>
                <th></th>
                <th><spring:message code="label.phoneNumber"/></th>
                <th><spring:message code="label.owner"/></th>
                <th><spring:message code="label.tariff"/></th>
                <th><spring:message code="label.chosenOptions"/></th>
            </tr>
            </thead>
            <tbody>

            <%--If any Contract is edited in a moment, all edit buttons must be disabled--%>
            <c:if test="${editedContract != null}">
                <c:set var="disabled" value="disabled"/>
            </c:if>

            <c:forEach var="contract" items="${contractList}">

                <%--Block and shadow edit button if contract is blocked--%>
                <c:set var="blocked" value=""/>
                <c:set var="shadowed" value=""/>
                <c:set var="highlightSuccess" value=""/>
                <c:set var="blockedByEmployee" value=""/>
                <c:if test="${contract.blocked == true}">
                    <c:set var="blocked" value="disabled"/>
                    <c:set var="shadowed" value="opacity: 0.4;"/>
                </c:if>
                <c:if test="${contract.blockedByEmployeeId > 0}">
                    <c:set var="blockedByEmployee" value="disabled"/>
                </c:if>
                <c:if test="${contract.id == highlightSuccessId}">
                    <c:set var="highlightSuccess" value="background-color: #D7F0F3;"/>
                </c:if>

                <form action="/showUserEditContractByIdPage" method="post">
                    <tr style="${shadowed} ${highlightSuccess}">
                        <td>
                            <input type="hidden" name="id" value="${contract.id}"/>
                            <input type="submit" name="btnEdit" value="<spring:message code="label.edit"/>"
                                   class="btn btn-info btn-xs" ${disabled} ${blocked}/>
                        </td>
                        <td>
                            <c:if test="${contract.blocked == true}">
                                <button name="btnBlock" value="true" class="btn btn-success btn-xs"
                                        style="width: 55px;" ${disabled} ${blockedByEmployee}>
                                    <spring:message code="label.unblock"/>
                                </button>
                            </c:if>
                            <c:if test="${contract.blocked == false}">
                                <button name="btnBlock" value="true" class="btn btn-danger btn-xs"
                                        style="width: 55px;" ${disabled} ${blockedByEmployee}>
                                    <spring:message code="label.block"/>
                                </button>
                            </c:if>
                        </td>
                        <td>
                            <c:out value="${contract.phoneNumber.phoneNumberString}"/>
                        </td>
                        <td>
                            <c:out value="${contract.user.firstName} ${contract.user.lastName}"/>
                        </td>
                        <td>
                            <c:out value="${contract.tariff.name}"/>
                        </td>
                        <td>
                            <c:forEach var="option" items="${contract.chosenOptions}" varStatus="loop">
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
