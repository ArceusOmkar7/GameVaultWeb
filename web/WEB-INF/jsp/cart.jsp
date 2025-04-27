<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>GameVault - Shopping Cart</title>
     <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <h2>Your Shopping Cart</h2>

         <%-- Display messages passed via request attribute or redirect param --%>
          <c:if test="${not empty message}">
              <p class="message <c:out value="${messageType eq 'success' ? 'success-message' : 'error-message'}"/>">
                  <c:out value="${message}" />
              </p>
          </c:if>
          <c:if test="${not empty errorMessage}">
              <p class="message error-message"><c:out value="${errorMessage}" /></p>
          </c:if>


        <c:choose>
            <c:when test="${not empty cartGames}">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Platform</th>
                            <th>Price</th>
                            <%-- Add<th>Action</th> header if remove functionality is added --%>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="game" items="${cartGames}">
                            <tr>
                                <td><c:out value="${game.title}" /></td>
                                <td><c:out value="${game.platform}" /></td>
                                <td><fmt:formatNumber value="${game.price}" type="currency" currencySymbol="$" /></td>
                                <%-- Add remove button cell if needed --%>
                                <%-- <td>
                                     <form action="${pageContext.request.contextPath}/removeFromCart" method="post" style="display:inline;">
                                         <input type="hidden" name="gameId" value="${game.gameId}" />
                                         <button type="submit" class="button small-button warning-button">Remove</button>
                                     </form>
                                </td> --%>
                            </tr>
                        </c:forEach>
                    </tbody>
                     <tfoot>
                         <tr>
                            <td colspan="2" style="text-align:right;"><strong>Total:</strong></td>
                            <td><strong><fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$" /></strong></td>
                            <%-- Add empty cell if action column exists --%>
                         </tr>
                     </tfoot>
                </table>

                 <div class="cart-actions">
                     <form action="${pageContext.request.contextPath}/placeOrder" method="post">
                        <button type="submit" class="button primary-button">Place Order</button>
                     </form>
                     <a href="${pageContext.request.contextPath}/games" class="button secondary-button">Continue Shopping</a>
                     <%-- Add Clear Cart button if needed --%>
                     <%-- <form action="${pageContext.request.contextPath}/clearCart" method="post" style="display:inline;"> --%>
                     <%--    <button type="submit" class="button warning-button">Clear Cart</button> --%>
                     <%-- </form> --%>
                 </div>

            </c:when>
            <c:otherwise>
                <p>Your shopping cart is empty.</p>
                <p><a href="${pageContext.request.contextPath}/games" class="button">Browse Games</a></p>
            </c:otherwise>
        </c:choose>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>