<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>GameVault - Page Not Found</title>
     <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
     <jsp:include page="header.jsp" />
    <div class="container error-page">
        <h2>Page Not Found (404)</h2>
        <p class="message error-message">Sorry, the page you were looking for (`<c:out value="${requestScope['javax.servlet.error.request_uri']}" default="unknown"/>`) could not be found.</p>
        <p>Please check the URL or navigate using the links below.</p>
         <p><a href="${pageContext.request.contextPath}/" class="button">Return to Home</a></p>
         <p><a href="${pageContext.request.contextPath}/games" class="button">Browse Games</a></p>
    </div>
     <jsp:include page="footer.jsp" />
</body>
</html>