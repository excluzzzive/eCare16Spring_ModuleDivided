<div class="navbar navbar-default navbar-static-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/showEmployeeWelcomePage"><b>T-Systems eCare16</b></a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">

                <li class="dropdown ${contractsActive}">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="label.contracts"/>
                        <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li class="${createContractActive}">
                            <a href="/showEmployeeContractCreatePage?currentStep=stepZero">
                                <spring:message code="label.createContract"/></a></li>
                        <li class="${contactListActive}">
                            <a href="/showEmployeeContractListPage"><spring:message code="label.showContractList"/></a></li>
                    </ul>
                </li>

                <li class="dropdown ${usersActive}">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="label.users"/>
                        <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li class="${createUserActive}">
                            <a href="/showEmployeeUserCreatePage"><spring:message code="label.createUser"/></a></li>
                        <li class="${userListActive}">
                            <a href="/showEmployeeUserListPage"><spring:message code="label.showUserList"/></a></li>
                    </ul>
                </li>

                <li class="dropdown ${tariffsActive}">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="label.tariffs"/>
                        <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li class="${createTariffActive}">
                            <a href="/showEmployeeTariffCreatePage"><spring:message code="label.createTariff"/></a></li>
                        <li class="${tariffListActive}">
                            <a href="/showEmployeeTariffListPage"><spring:message code="label.showTariffList"/></a></li>
                    </ul>
                </li>

                <li class="dropdown ${optionsActive}">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="label.options"/>
                        <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li class="${createOptionActive}">
                            <a href="/showEmployeeOptionCreatePage"><spring:message code="label.createOption"/></a></li>
                        <li class="${optionListActive}">
                            <a href="/showEmployeeOptionListPage"><spring:message code="label.showOptionList"/></a></li>
                    </ul>
                </li>

                <li class="${phoneNumbersActive}"><a href="/showEmployeePhoneNumberListPage">
                    <spring:message code="label.phoneNumbers"/></a></li>

            </ul>

            <ul class="nav navbar-nav navbar-right">
                <li><a href="#"><spring:message code="label.welcome"/>, <c:out
                        value="${sessionScope.user.firstName} ${sessionScope.user.lastName}"/>!</a></li>
                <li><a href="/logout"><spring:message code="label.logout"/></a></li>
            </ul>
        </div>
    </div>
</div>