<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>GameVault - Error</title>
     <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
     <jsp:include page="header.jsp" />
     <div class="container error-page">
        <h2>An Error Occurred</h2>
        <p class="message error-message">
            Sorry, the application encountered an unexpected problem. Please try again later or contact support if the issue persists.
        </p>

        <%-- Basic error details (useful for development, REMOVE or SECURE in production) --%>
        <c:if test="${pageContext.exception != null}">
             <div class="error-details">
                 <p><strong>Error Type:</strong> <c:out value="${pageContext.exception.class.name}" /></p>
                 <p><strong>Message:</strong> <c:out value="${pageContext.exception.message}" /></p>
                 <%-- Uncomment for stack trace during development ONLY
                 <p><strong>Stack Trace (Development Only):</strong></p>
                 <pre><% pageContext.getException().printStackTrace(new java.io.PrintWriter(out)); %></pre>
                 --%>
             </div>
        </c:if>

         <p><a href="${pageContext.request.contextPath}/" class="button">Return to Home</a></p>
    </div>
     <jsp:include page="footer.jsp" />
</body>
</html>