<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Register</title>
     <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 flex flex-col min-h-screen">

     <jsp:include page="header.jsp" />

     <main class="flex-grow flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
         <div class="max-w-md w-full space-y-8 bg-white p-10 rounded-xl shadow-lg">
             <div>
                 <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
                     Create your GameVault account
                 </h2>
             </div>

             <%-- Display error messages --%>
             <c:if test="${not empty errorMessage}">
                 <div class="rounded-md bg-red-50 p-4">
                     <div class="flex">
                         <div class="ml-3">
                             <p class="text-sm font-medium text-red-800"><c:out value="${errorMessage}" /></p>
                         </div>
                     </div>
                 </div>
             </c:if>

             <form class="mt-8 space-y-6" action="${pageContext.request.contextPath}/register" method="post">
                 <div class="rounded-md shadow-sm -space-y-px">
                     <%-- Name Input --%>
                     <div>
                         <label for="username" class="sr-only">Name</label>
                         <input id="username" name="username" type="text" autocomplete="name" required
                                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                                placeholder="Name" value="<c:out value="${username}"/>">
                     </div>
                     <%-- Email Input --%>
                     <div>
                         <label for="email" class="sr-only">Email address</label>
                         <input id="email" name="email" type="email" autocomplete="email" required
                                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                                placeholder="Email address" value="<c:out value="${email}"/>">
                     </div>
                      <%-- Password Input --%>
                     <div>
                         <label for="password" class="sr-only">Password</label>
                         <input id="password" name="password" type="password" autocomplete="new-password" required
                                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                                placeholder="Password">
                                <%-- TODO: Add password strength indicator --%>
                                <%-- <span class="text-xs text-red-500 ml-1">(Plain text - INSECURE!)</span> --%>
                     </div>
                     <%-- Confirm Password Input --%>
                      <div>
                         <label for="confirmPassword" class="sr-only">Confirm Password</label>
                         <input id="confirmPassword" name="confirmPassword" type="password" autocomplete="new-password" required
                                class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                                placeholder="Confirm Password">
                     </div>
                     <%-- Wallet Balance (Optional) - hidden for now based on wireframe --%>
                     <input type="hidden" id="walletBalance" name="walletBalance" value="0"> <%-- Default to 0 --%>
                 </div>

                 <div>
                     <button type="submit" class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                         Register
                     </button>
                 </div>
             </form>
              <div class="text-sm text-center">
                  <a href="${pageContext.request.contextPath}/login" class="font-medium text-indigo-600 hover:text-indigo-500">
                      Already have an account? Sign in
                  </a>
              </div>
         </div>
     </main>

     <jsp:include page="footer.jsp" />

</body>
</html>