<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // --- Choose your default landing page ---
    // Option 1: Default to login page
    // String targetPage = "/login";

    // Option 2: Default to games list page
    String targetPage = "/games";
    // --- ---

    String contextPath = request.getContextPath();
    response.sendRedirect(contextPath + targetPage);
%>
<%-- Fallback content if redirect fails (shouldn't normally happen) --%>
<!DOCTYPE html>
<html>
<head><title>Redirecting...</title></head>
<body>
    <p>If you are not redirected automatically, please click one of the links below:</p>
    <ul>
        <li><a href="${pageContext.request.contextPath}/games">View Games</a></li>
        <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
    </ul>
</body>
</html>