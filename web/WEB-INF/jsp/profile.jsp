<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - User Profile</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">

    <jsp:include page="header.jsp" />

    <div class="container mx-auto px-4 py-8">
        <h2 class="text-3xl font-bold mb-6 text-gray-800">User Profile</h2>

         <%-- Display messages --%>
        <c:if test="${not empty message}">
            <div class="mb-4 p-4 rounded ${messageType eq 'success' ? 'bg-green-100 border border-green-400 text-green-700' : 'bg-red-100 border border-red-400 text-red-700'}" role="alert">
                <p><c:out value="${message}" /></p>
            </div>
        </c:if>

        <div class="bg-white p-6 rounded-lg shadow-md">
            <div class="border-b pb-4 mb-4">
                 <h3 class="text-xl font-semibold text-gray-700">Account Information</h3>
            </div>

            <div class="space-y-3 text-gray-700">
                 <p><strong>Name:</strong> <c:out value="${user.username}"/></p>
                 <p><strong>Email:</strong> <c:out value="${user.email}"/></p>
                 <p><strong>Member Since:</strong> <fmt:formatDate value="${user.createdAt}" type="date" dateStyle="long" /></p>
                 <p><strong>Wallet Balance:</strong> <fmt:formatNumber value="${user.walletBalance}" type="currency" currencySymbol="$" /></p>
             </div>

             <div class="mt-6 pt-6 border-t flex gap-4">
                 <a href="${pageContext.request.contextPath}/editProfile" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-200">
                     Edit Profile <%-- TODO: Implement Edit Profile Page/Servlet --%>
                 </a>
                 <a href="${pageContext.request.contextPath}/orders" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition duration-200">
                     Order History
                 </a>
             </div>
        </div>

    </div>

    <jsp:include page="footer.jsp" />

</body>
</html>