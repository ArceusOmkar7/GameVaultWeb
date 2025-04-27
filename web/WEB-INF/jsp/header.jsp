<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- Added for balance formatting --%>

<%-- Simple Header with Navigation --%>
<div class="header">
    <div class="logo">
        <h1><a href="${pageContext.request.contextPath}/">GameVault</a></h1>
    </div>
    <nav class="navigation">
        <ul>
            <li><a href="${pageContext.request.contextPath}/games">Games</a></li>
            <c:choose>
                <c:when test="${not empty sessionScope.loggedInUser}">
                    <%-- User is logged in --%>
                    <li><a href="${pageContext.request.contextPath}/viewCart">Cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders">My Orders</a></li>
                    <li class="welcome-user">
                        Welcome, <c:out value="${sessionScope.loggedInUser.username}"/>!
                        (<fmt:formatNumber value="${sessionScope.loggedInUser.walletBalance}" type="currency" currencySymbol="$" />)
                    </li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </c:when>
                <c:otherwise>
                    <%-- User is not logged in --%>
                    <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/register">Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</div>
<hr/>