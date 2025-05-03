<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GameVault - Login</title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-100 flex flex-col min-h-screen">
    <jsp:include page="header.jsp" />

    <main
      class="flex-grow flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8"
    >
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
                <p class="text-sm font-medium text-red-800">
                  <c:out value="${errorMessage}" />
                </p>
              </div>
            </div>
          </div>
        </c:if>
        <c:if test="${not empty param.message}">
          <div
            class="rounded-md ${param.messageType eq 'success' ? 'bg-green-50' : 'bg-red-50'} p-4"
          >
            <div class="flex">
              <div class="ml-3">
                <p
                  class="text-sm font-medium ${param.messageType eq 'success' ? 'text-green-800' : 'text-red-800'}"
                >
                  <c:out value="${param.message}" />
                </p>
              </div>
            </div>
          </div>
        </c:if>

        <div class="mt-8 space-y-6 flex flex-col items-center">
          <p class="text-lg text-gray-600 mb-4">Select your login type:</p>

          <div class="w-full flex flex-col space-y-4">
            <form
              action="${pageContext.request.contextPath}/login"
              method="post"
            >
              <input type="hidden" name="userType" value="user" />
              <button
                type="submit"
                class="group relative w-full flex justify-center py-4 px-4 border border-transparent text-lg font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors duration-200"
              >
                Login as User
              </button>
            </form>

            <form
              action="${pageContext.request.contextPath}/login"
              method="post"
            >
              <input type="hidden" name="userType" value="admin" />
              <button
                type="submit"
                class="group relative w-full flex justify-center py-4 px-4 border border-transparent text-lg font-medium rounded-md text-white bg-purple-600 hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500 transition-colors duration-200"
              >
                Login as Admin
              </button>
            </form>
          </div>
        </div>
      </div>
    </main>

    <jsp:include page="footer.jsp" />
  </body>
</html>
