<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> <%-- For functions like escapeXml --%>

<!DOCTYPE html>
<html>
<head>
    <title>GameVault - Login</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"> <%-- Link to your CSS --%>
</head>
<body>
    <jsp:include page="header.jsp" /> <%-- Include common header --%>

    <div class="container form-container">
        <h2>Login</h2>

        <%-- Display error messages passed via request attribute --%>
        <c:if test="${not empty errorMessage}">
            <p class="message error-message"><c:out value="${errorMessage}" /></p>
        </c:if>

        <%-- Display messages passed via redirect parameters --%>
        <c:if test="${not empty param.message}">
            <p class="message <c:out value="${param.messageType eq 'success' ? 'success-message' : 'error-message'}"/>">
                <c:out value="${param.message}" />
            </p>
        </c:if>

        <%-- Display success message passed via request attribute (e.g., after registration) --%>
         <c:if test="${not empty successMessage}">
             <p class="message success-message"><c:out value="${successMessage}" /></p>
         </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="<c:out value="${not empty email ? email : ''}"/>" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
                 <span class="note">(Plain text - INSECURE!)</span>
            </div>
            <div class="form-group">
                <button type="submit" class="button">Login</button>
            </div>
        </form>
         <p class="form-link">Don't have an account? <a href="${pageContext.request.contextPath}/register">Register here</a></p>
    </div>

    <jsp:include page="footer.jsp" /> <%-- Include common footer --%>
</body>
</html>