<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
User login page.
--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Login Page</title>
    <spring:url value="/resources/css/my.css" var="myCss"/>
    <spring:url value="/resources/css/bootstrap.min.css" var="bootstrapMinCss"/>
    <spring:url value="/resources/img/favicon.ico" var="favicon"/>
    <spring:url value="/resources/img/flag_de.png" var="flagDe"/>
    <spring:url value="/resources/img/flag_en.png" var="flagEn"/>
    <spring:url value="/resources/img/flag_fr.png" var="flagFr"/>
    <spring:url value="/resources/js/bootstrap.min.js" var="bootstrapMinJs"/>
    <link href="${favicon}" rel="shortcut icon" type="image/x-icon">
    <link href="${myCss}" rel="stylesheet"/>
    <link href="${bootstrapMinCss}" rel="stylesheet"/>
</head>
<body>

<c:set var="userActive" value="active"/>
<c:set var="userTabPane" value="active in"/>
<c:set var="employeeTabPane" value="fade"/>

<c:if test="${employeeView == true}">
    <c:set var="employeeActive" value="active"/>
    <c:set var="employeeTabPane" value="active in"/>
    <c:set var="userActive" value=""/>
    <c:set var="userTabPane" value="fade"/>
</c:if>

<div class="container div-login">
    <div class="" id="loginModal">
        <div class="modal-body">

            <div class="well">
                <div class="form-group">
                    <h3 class="form-signin-heading text-danger" align="center">
                        T-Systems eCare16
                    </h3>
                    <hr width="200px">
                </div>
                <ul class="nav nav-tabs">
                    <li class="${userActive}"><a href="#userLogin" data-toggle="tab"><spring:message
                            code="label.userLogin"/></a></li>
                    <li class="${employeeActive}"><a href="#employeeLogin" data-toggle="tab"><spring:message
                            code="label.employeeLogin"/></a></li>
                </ul>
                <div id="myTabContent" class="tab-content">
                    <div class="tab-pane ${userTabPane}" id="userLogin">
                        <form class="form-signin" role="form" action="/userAuthenticate" method="post">
                            <div class="form-group">
                                <input type="email" name="email" class="form-control input-lg"
                                       placeholder="<spring:message code="label.yourEmail"/>" required autofocus>
                            </div>
                            <div class="form-group">
                                <input type="password" name="password" class="form-control input-lg"
                                       placeholder="<spring:message code="label.yourPassword"/>" required>
                                <hr width="50px">
                            </div>
                            <div class="form-group">
                                <input type="submit" class="btn btn-sm btn-success btn-block"
                                       value="<spring:message code="label.signIn"/>"/>
                            </div>
                            <c:if test="${userInvalidAuth == true}">
                                <div class="form-group text-center">
                                    <h5 class="text-danger"><spring:message code="label.incorrectLoginOrPassword"/></h5>
                                </div>
                            </c:if>
                            <c:if test="${unknownError == true}">
                                <div class="form-group text-center">
                                    <h5 class="text-danger"><spring:message
                                            code="label.unknownErrorPleaseReLogin"/></h5>
                                </div>
                            </c:if>
                        </form>
                        <div class="form-group text-center">
                            <a href="/showLoginPage?siteLanguage=de"><img src="${flagDe}"/></a>
                            <a href="/showLoginPage?siteLanguage=en"><img src="${flagEn}"/></a>
                            <a href="/showLoginPage?siteLanguage=fr"><img src="${flagFr}"/></a>
                        </div>
                    </div>

                    <div class="tab-pane ${employeeTabPane}" id="employeeLogin">
                        <form class="form-signin" role="form" action="/employeeAuthenticate" method="post">

                            <div class="form-group">
                                <input type="email" name="email" class="form-control input-lg"
                                       placeholder="<spring:message code="label.yourEmail"/>"
                                       required autofocus>
                            </div>
                            <div class="form-group">
                                <input type="password" name="password" class="form-control input-lg"
                                       placeholder="<spring:message code="label.yourPassword"/>">
                                <hr width="50px">
                            </div>
                            <div class="form-group">
                                <input type="submit" class="btn btn-sm btn-primary btn-block"
                                       value="<spring:message code="label.signIn"/>"/>
                            </div>
                            <c:if test="${employeeInvalidAuth == true}">
                                <div class="form-group text-center">
                                    <h5 class="text-danger"><spring:message code="label.incorrectLoginOrPassword"/></h5>
                                </div>
                            </c:if>
                            <c:if test="${userIsNotEmployee == true}">
                                <div class="form-group text-center">
                                    <h5 class="text-danger"><spring:message code="label.userIsNotEmployee"/></h5>
                                </div>
                            </c:if>
                        </form>
                        <div class="form-group text-center">
                            <a href="/showLoginPage?siteLanguage=de&employeeView=true"><img src="${flagDe}"/></a>
                            <a href="/showLoginPage?siteLanguage=en&employeeView=true"><img src="${flagEn}"/></a>
                            <a href="/showLoginPage?siteLanguage=fr&employeeView=true"><img src="${flagFr}"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<%--<script src="${bootstrapMinJs}"></script>--%>
<script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.1/js/bootstrap.min.js"></script>
</body>
</html>