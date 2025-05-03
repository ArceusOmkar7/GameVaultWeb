<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Redirect to the new home page
    String targetPage = "/home";

    String contextPath = request.getContextPath();
    response.sendRedirect(contextPath + targetPage);
%>
<%-- Fallback content if redirect fails --%>
<!DOCTYPE html>
<html>
<head><title>Redirecting...</title></head>
<body>
    <p>If you are not redirected automatically, please click the link below:</p>
    <ul>
        <li><a href="${pageContext.request.contextPath}/home">Go to Homepage</a></li>
    </ul>
</body>
</html>