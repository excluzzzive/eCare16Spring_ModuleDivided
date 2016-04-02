<div class="navbar navbar-default navbar-static-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/showUserWelcomePage"><b>T-Systems eCare16</b></a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">

                <li class="${contractsActive}"><a href="/showUserContractListPage">Contracts</a></li>

            </ul>

            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">Welcome, <c:out
                        value="${sessionScope.user.firstName} ${sessionScope.user.lastName}"/>!</a></li>
                <li><a href="/logout">Logout</a></li>
            </ul>
        </div>
    </div>
</div>