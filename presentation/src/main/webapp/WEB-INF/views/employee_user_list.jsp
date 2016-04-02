<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<%@ page import="ru.simflex.ex.entities.*"%>
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
    <link href="${favicon}" rel="shortcut icon" type="image/x-icon">
    <link href="${myCss}" rel="stylesheet"/>
    <link href="${bootstrapMinCss}" rel="stylesheet"/>
</head>
<body>
<c:set var="usersActive" value="active"/>
<c:set var="userListActive" value="active"/>
<%@include file="employee_header.jsp" %>

<%--Block is showed if editing in process--%>
<c:if test="${editedUser != null}">
    <div class="container">
        <div class="jumbotron">

                <%--Checkbox checked if user is employee--%>
            <c:if test="${editedUser.employee == true}">
                <c:set var="employeeChecked" value="checked"/>
            </c:if>

                <%--User cannot to delete or disable his employee functions--%>
            <c:if test="${editedUser.id == sessionScope.user.id}">
                <c:set var="disabledEmployeeCheckbox" value="disabled"/>
                <c:set var="disabledDelete" value="disabled"/>
            </c:if>

                <%--Used user cannot be deleted--%>
            <c:if test="${!empty userContractList}">
                <c:set var="disabledDelete" value="disabled"/>
            </c:if>

                <%--Cancel button--%>
            <form action="/showEmployeeUserListPage" method="post">
                <p class="text-left">
                    <input type="submit" name="btnCancel" value="<spring:message code="label.cancel"/>"
                           class="btn btn-warning"/>
                </p>
            </form>
            <hr width="150px" align="left">

                <%--Update User Form--%>
            <form class="form-horizontal" action="/employeeUpdateUser" method="post">

                <input type="hidden" name="id" value="${editedUser.id}">

                <div class="form-group">
                    <label for="userFirstName" class="col-sm-4 control-label"><spring:message
                            code="label.userFirstName"/></label>
                    <div class="col-sm-4">
                        <input type="text" minlength="2" maxlength="20"
                               title="<spring:message code="input.userFirstNameTitle"/>"
                               class="form-control" id="userFirstName" name="firstName"
                               placeholder="<spring:message code="label.userFirstName"/>"
                               value="${editedUser.firstName}" required>
                    </div>
                    <div class="col-sm-4" style="padding-top: 7px">
                        <form:errors path="editedUser.firstName" class="text-danger"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="userLastName" class="col-sm-4 control-label"><spring:message
                            code="label.userLastName"/></label>
                    <div class="col-sm-4">
                        <input type="text" minlength="2" maxlength="20"
                               title="<spring:message code="input.userLastNameTitle"/>"
                               class="form-control" id="userLastName" name="lastName"
                               placeholder="<spring:message code="label.userLastName"/>" value="${editedUser.lastName}"
                               required>
                    </div>
                    <div class="col-sm-4" style="padding-top: 7px">
                        <form:errors path="editedUser.lastName" class="text-danger"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="userBirthDate" class="col-sm-4 control-label"><spring:message
                            code="label.userBirthDate"/></label>
                    <div class="col-sm-4">
                        <input type="date" class="form-control" id="userBirthDate" name="birthDate"
                               placeholder="<spring:message code="label.userBirthDate"/>" value="${editedUserBirthDate}"
                               required>
                    </div>
                    <div class="col-sm-4" style="padding-top: 7px">
                        <form:errors path="editedUser.birthDate" class="text-danger"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="userPassportData" class="col-sm-4 control-label"><spring:message
                            code="label.userPassportData"/></label>
                    <div class="col-sm-4">
                        <input type="text" minlength="8" maxlength="40"
                               title="<spring:message code="input.userPassportDataTitle"/>"
                               class="form-control" id="userPassportData" name="passportData"
                               placeholder="<spring:message code="label.userPassportData"/>"
                               value="${editedUser.passportData}" required>
                    </div>
                    <div class="col-sm-4 text-danger" style="padding-top: 7px">
                        <form:errors path="editedUser.passportData"/>
                        <c:if test="${!empty notUniquePassportData}">
                            <spring:message code="label.${notUniquePassportData}"/>
                        </c:if>
                    </div>
                </div>

                <div class="form-group">
                    <label for="userEmail" class="col-sm-4 control-label"><spring:message
                            code="label.userEmail"/></label>
                    <div class="col-sm-4">
                        <input type="email" class="form-control" id="userEmail" name="email"
                               placeholder="<spring:message code="label.userEmail"/>" value="${editedUser.email}"
                               required>
                    </div>
                    <div class="col-sm-4 text-danger" style="padding-top: 7px">
                        <form:errors path="editedUser.email"/>
                        <c:if test="${!empty notUniqueEmail}">
                            <spring:message code="label.${notUniqueEmail}"/>
                        </c:if>
                    </div>
                </div>

                <div class="form-group">
                    <label for="updateUserPassword" class="col-sm-4 control-label"><spring:message
                            code="label.userPassword"/></label>
                    <div class="col-sm-4">
                        <input type="password" class="form-control" id="updateUserPassword" name="password"
                               placeholder="<spring:message code="label.leaveBlankIfDoNotWantToChange"/>"/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-4">
                        <div class="checkbox">
                            <label>
                                <c:if test="${!empty disabledEmployeeCheckbox}">
                                    <input type="hidden" name="employee" value="${editedUser.employee}"/>
                                </c:if>
                                <input type="checkbox" name="employee"
                                       value="true" ${employeeChecked} ${disabledEmployeeCheckbox}/><spring:message
                                    code="label.isEmployee"/>
                            </label>
                        </div>
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
                <tbody>
                <tr>
                    <td>
                        <spring:message code="label.userContracts"/>:
                    </td>
                    <td>
                        <c:forEach var="contract" items="${userContractList}"
                                   varStatus="loop">
                            <c:out value="${contract.phoneNumber.phoneNumberString}"/><c:if
                                test="${!loop.last}">, </c:if>
                        </c:forEach>
                    </td>
                </tr>
                </tbody>
            </table>

                <%--Delete User Button--%>
            <hr width="150px" align="right">
            <form action="/employeeDeleteUser" method="post">
                <p class="text-right">
                    <input type="hidden" name="id" value="${editedUser.id}">
                    <input type="submit" value="<spring:message code="label.delete"/>"
                           class="btn btn-danger" ${disabledDelete}/>
                </p>
            </form>
        </div>
    </div>
</c:if>

<div class="container">
    <div class="jumbotron">
        <h3><p class="text-right"><spring:message code="label.userManagement"/></p></h3>
        <hr width="200px" align="right">

        <%--If any User is edited in a moment, all edit buttons must be disabled--%>
        <c:if test="${editedUser != null}">
            <c:set var="disabled" value="disabled"/>
        </c:if>

        <br>
        <h5><b><spring:message code="label.editUserByPhoneNumber"/></b></h5>
        <form role="form" class="form-inline" action="/showEmployeeEditUserByPhoneNumberPage" method="post">
            <div class="form-group">
                <input type="number" name="editedUserPhoneNumber" class="form-control" id="editedUserPhoneNumber"
                       placeholder="<spring:message code="label.userPhoneNumber"/>" required/>
            </div>
            <button type="submit" class="btn btn-info" ${disabled}><spring:message code="label.search"/></button>
            <c:if test="${userNotFoundByPhoneNumber == true}">
                <h7 class="text-danger"><spring:message code="label.userNotFound"/></h7>
            </c:if>
        </form>
        <br>

        <table class="table table-condensed table-hover">
            <thead>
            <tr>
                <th></th>
                <th><spring:message code="label.userName"/></th>
                <th><spring:message code="label.birthDate"/></th>
                <th><spring:message code="label.email"/></th>
                <th><spring:message code="label.isEmployee"/></th>
            </tr>
            </thead>
            <tbody>

            <c:forEach var="user" items="${userList}">
                <c:set var="highlightSuccess" value=""/>
                <c:if test="${user.id == highlightSuccessId}">
                    <c:set var="highlightSuccess" value="background-color: #D7F0F3;"/>
                </c:if>
                <form action="/showEmployeeEditUserByIdPage" method="post">
                    <tr style="${highlightSuccess}">
                        <td>
                            <input type="hidden" name="editedUserId" value="${user.id}"/>
                            <input type="submit" value="<spring:message code="label.edit"/>"
                                   class="btn btn-info btn-xs" ${disabled}/>
                        </td>
                        <td>
                            <c:out value="${user.firstName} ${user.lastName}"/>
                        </td>
                        <td>
                            <fmt:formatDate type="date" value="${user.birthDate}"/>
                        </td>
                        <td>
                            <c:out value="${user.email}"/>
                        </td>
                        <td>
                            <c:out value="${user.employee}"/>
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
