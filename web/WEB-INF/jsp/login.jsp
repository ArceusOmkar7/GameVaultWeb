<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Login</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 flex flex-col min-h-screen">

    <jsp:include page="header.jsp" />

    <main class="flex-grow flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
        <div class="max-w-md w-full space-y-8 bg-white p-10 rounded-xl shadow-lg">
            <div>
                <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
                    Sign in to GameVault
                </h2>
            </div>

            <%-- Display messages --%>
            <c:if test="${not empty errorMessage}">
                 <div class="rounded-md bg-red-50 p-4">
                     <div class="flex">
                         <div class="ml-3">
                             <p class="text-sm font-medium text-red-800"><c:out value="${errorMessage}" /></p>
                         </div>
                     </div>
                 </div>
            </c:if>
            <c:if test="${not empty param.message}">
                <div class="rounded-md ${param.messageType eq 'success' ? 'bg-green-50' : 'bg-red-50'} p-4">
                    <div class="flex">
                         <div class="ml-3">
                              <p class="text-sm font-medium ${param.messageType eq 'success' ? 'text-green-800' : 'text-red-800'}">
                                 <c:out value="${param.message}" />
                              </p>
                         </div>
                    </div>
                </div>
            </c:if>

            <form class="mt-8 space-y-6" action="${pageContext.request.contextPath}/login" method="post">
                <input type="hidden" name="remember" value="true">
                <div class="rounded-md shadow-sm -space-y-px">
                     <%-- Email/Phone Input - Adjusted to match wireframe --%>
                    <div>
                        <label for="email" class="sr-only">Email or Phone Number</label>
                        <input id="email" name="email" type="text" autocomplete="email" required
                               class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                               placeholder="Email or Phone Number" value="<c:out value="${not empty email ? email : ''}"/>">
                    </div>
                     <%-- Password Input --%>
                    <div>
                        <label for="password" class="sr-only">Password</label>
                        <input id="password" name="password" type="password" autocomplete="current-password" required
                               class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                               placeholder="Password">
                               <%-- <span class="text-xs text-red-500 ml-1">(Plain text - INSECURE!)</span> --%>
                               <%-- TODO: Remove insecure note in production --%>
                    </div>
                </div>

                <div>
                    <button type="submit" class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        Sign in
                    </button>
                </div>
            </form>
             <div class="text-sm text-center">
                 <a href="${pageContext.request.contextPath}/register" class="font-medium text-indigo-600 hover:text-indigo-500">
                     Don't have an account? Register
                 </a>
             </div>
        </div>
    </main>

    <jsp:include page="footer.jsp" />

</body>
</html>