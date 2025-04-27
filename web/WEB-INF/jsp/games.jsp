<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- For formatting numbers (price) --%>

<!DOCTYPE html>
<html>
<head>
    <title>GameVault - Games</title>
     <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <h2>Available Games</h2>

        <%-- Display messages passed from redirects or request attribute --%>
         <c:if test="${not empty message}">
             <p class="message <c:out value="${messageType eq 'success' ? 'success-message' : 'error-message'}"/>">
                 <c:out value="${message}" />
             </p>
         </c:if>
         <c:if test="${not empty errorMessage}">
             <p class="message error-message"><c:out value="${errorMessage}" /></p>
         </c:if>

        <c:choose>
            <c:when test="${not empty gamesList}">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Developer</th>
                            <th>Platform</th>
                            <th>Price</th>
                            <th>Description</th>
                            <c:if test="${not empty sessionScope.loggedInUser}">
                                <th>Action</th> <%-- Only show action column if logged in --%>
                            </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="game" items="${gamesList}">
                            <tr>
                                <td><c:out value="${game.title}" /></td>
                                <td><c:out value="${game.developer}" /></td>
                                <td><c:out value="${game.platform}" /></td>
                                <td><fmt:formatNumber value="${game.price}" type="currency" currencySymbol="$" /></td>
                                <td><c:out value="${game.description}" /></td>
                                 <c:if test="${not empty sessionScope.loggedInUser}">
                                    <td>
                                         <%-- Add to Cart Form --%>
                                         <form action="${pageContext.request.contextPath}/addToCart" method="post" style="display:inline;">
                                             <input type="hidden" name="gameId" value="${game.gameId}" />
                                             <button type="submit" class="button small-button">Add to Cart</button>
                                         </form>
                                    </td>
                                 </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p>No games available at the moment.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>