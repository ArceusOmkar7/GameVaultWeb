<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Edit Profile</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">

    <jsp:include page="header.jsp" />

    <div class="container mx-auto px-4 py-8 max-w-2xl"> <%-- Limit width --%>
        <h2 class="text-3xl font-bold mb-6 text-gray-800">Edit Profile</h2>

        <%-- Display messages --%>
        <c:if test="${not empty errorMessage}">
            <div class="mb-4 p-4 rounded bg-red-100 border border-red-400 text-red-700" role="alert">
                <p><strong>Error:</strong> <c:out value="${errorMessage}" /></p>
            </div>
        </c:if>
        <%-- Success message handled by redirect --%>

        <div class="bg-white p-8 rounded-lg shadow-md">
            <form action="${pageContext.request.contextPath}/editProfile" method="post" class="space-y-6">

                <div>
                    <label for="username" class="block text-sm font-medium text-gray-700">Username</label>
                    <div class="mt-1">
                         <%-- Pre-fill with user's current username --%>
                        <input type="text" name="username" id="username" required
                               value="<c:out value="${user.username}"/>"
                               class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                    </div>
                </div>

                <div>
                    <label for="email" class="block text-sm font-medium text-gray-700">Email address</label>
                    <div class="mt-1">
                         <%-- Pre-fill with user's current email --%>
                        <input id="email" name="email" type="email" autocomplete="email" required
                               value="<c:out value="${user.email}"/>"
                               class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                    </div>
                </div>

                <%-- Password change section (Optional - requires more logic) --%>
                <%--
                <div class="border-t pt-6 mt-6">
                    <h3 class="text-lg font-medium text-gray-900 mb-4">Change Password (Optional)</h3>
                     <div>
                         <label for="currentPassword" class="block text-sm font-medium text-gray-700">Current Password</label>
                         <input type="password" name="currentPassword" id="currentPassword" class="mt-1 appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                     </div>
                     <div class="mt-4">
                         <label for="newPassword" class="block text-sm font-medium text-gray-700">New Password</label>
                         <input type="password" name="newPassword" id="newPassword" class="mt-1 appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                     </div>
                     <div class="mt-4">
                         <label for="confirmNewPassword" class="block text-sm font-medium text-gray-700">Confirm New Password</label>
                         <input type="password" name="confirmNewPassword" id="confirmNewPassword" class="mt-1 appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                     </div>
                </div>
                --%>


                <div class="flex justify-end gap-4 pt-6 border-t mt-6">
                     <a href="${pageContext.request.contextPath}/profile" class="inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        Cancel
                     </a>
                    <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        Save Changes
                    </button>
                </div>
                 <p class="text-center text-xs text-gray-500 mt-4">TODO: Profile update logic is currently simulated and does not persist changes.</p>
            </form>
        </div>

    </div>

    <jsp:include page="footer.jsp" />

</body>
</html>