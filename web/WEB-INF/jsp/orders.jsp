<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>GameVault - Order History</title>
     <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <h2>Your Order History</h2>

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
            <c:when test="${not empty orderList}">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Date Placed</th>
                            <th>Total Amount</th>
                            <%-- Add<th>Details</th> header if order details view is implemented --%>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${orderList}">
                            <tr>
                                <td>#<c:out value="${order.orderId}" /></td>
                                <td><fmt:formatDate value="${order.orderDate}" type="both" dateStyle="medium" timeStyle="short" /></td>
                                <td><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$" /></td>
                                <%-- Add link/button cell if needed --%>
                                <%-- <td><a href="${pageContext.request.contextPath}/orderDetails?orderId=${order.orderId}">View Details</a></td> --%>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p>You have not placed any orders yet.</p>
                 <p><a href="${pageContext.request.contextPath}/games" class="button">Browse Games</a></p>
            </c:otherwise>
        </c:choose>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>