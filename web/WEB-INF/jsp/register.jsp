<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>GameVault - Register</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container form-container">
        <h2>Register New Account</h2>

        <%-- Display error messages --%>
        <c:if test="${not empty errorMessage}">
            <p class="message error-message"><c:out value="${errorMessage}" /></p>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-group">
                <label for="email">Email:</label>
                <%-- Use c:out to prevent XSS on pre-filled values --%>
                <input type="email" id="email" name="email" value="<c:out value="${email}"/>" required>
            </div>
             <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" value="<c:out value="${username}"/>" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
                <span class="note">(Plain text - INSECURE!)</span>
            </div>
             <div class="form-group">
                <label for="confirmPassword">Confirm Password:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
             <div class="form-group">
                <label for="walletBalance">Initial Balance (Optional):</label>
                <input type="number" id="walletBalance" name="walletBalance" step="0.01" min="0" value="<c:out value="${walletBalance}"/>">
            </div>
            <div class="form-group">
                <button type="submit" class="button">Register</button>
            </div>
        </form>
         <p class="form-link">Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a></p>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>