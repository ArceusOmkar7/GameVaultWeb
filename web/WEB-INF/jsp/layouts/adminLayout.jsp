<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${param.pageTitle} - GameVault Admin</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/gamevault.css"
    />
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
      tailwind.config = {
        theme: {
          extend: {
            colors: {
              primary: {
                50: "#f0f9ff",
                100: "#e0f2fe",
                200: "#bae6fd",
                300: "#7dd3fc",
                400: "#38bdf8",
                500: "#0ea5e9",
                600: "#0284c7",
                700: "#0369a1",
                800: "#075985",
                900: "#0c4a6e",
              },
            },
          },
        },
      };
    </script>
  </head>
  <body class="flex flex-col min-h-screen bg-gray-100">
    <!-- Header -->
    <jsp:include page="/WEB-INF/jsp/components/header.jsp" />

    <div class="flex flex-grow">
      <!-- Admin Sidebar -->
      <aside class="w-64 bg-gray-800 text-white p-4">
        <h2 class="text-xl font-bold mb-4">Admin Panel</h2>
        <nav>
          <ul class="space-y-2">
            <li>
              <a
                href="${pageContext.request.contextPath}/admin/dashboard"
                class="block py-2 px-4 hover:bg-gray-700 rounded ${param.activeMenu == 'dashboard' ? 'bg-gray-700' : ''}"
              >
                Dashboard
              </a>
            </li>
            <li>
              <a
                href="${pageContext.request.contextPath}/admin/game-management"
                class="block py-2 px-4 hover:bg-gray-700 rounded ${param.activeMenu == 'games' ? 'bg-gray-700' : ''}"
              >
                Game Management
              </a>
            </li>
            <li>
              <a
                href="${pageContext.request.contextPath}/admin/check-database"
                class="block py-2 px-4 hover:bg-gray-700 rounded ${param.activeMenu == 'database' ? 'bg-gray-700' : ''}"
              >
                Database Check
              </a>
            </li>
            <li>
              <a
                href="${pageContext.request.contextPath}/admin/generate-dummy-data"
                class="block py-2 px-4 hover:bg-gray-700 rounded ${param.activeMenu == 'dummy-data' ? 'bg-gray-700' : ''}"
              >
                Dummy Data
              </a>
            </li>
            <li>
              <a
                href="${pageContext.request.contextPath}/admin/load-json-data"
                class="block py-2 px-4 hover:bg-gray-700 rounded ${param.activeMenu == 'json-loader' ? 'bg-gray-700' : ''}"
              >
                JSON Loader
              </a>
            </li>
          </ul>
        </nav>
      </aside>

      <!-- Main Content -->
      <main class="flex-grow p-6">
        <!-- System Message/Alert -->
        <c:if test="${not empty sessionScope.message}">
          <div class="mb-4">
            <div
              class="p-4 border-l-4 ${sessionScope.messageType == 'error' ? 'border-red-500 bg-red-50 text-red-700' : sessionScope.messageType == 'warning' ? 'border-yellow-500 bg-yellow-50 text-yellow-700' : 'border-green-500 bg-green-50 text-green-700'}"
            >
              <p>${sessionScope.message}</p>
            </div>
          </div>
          <%-- Clear the message after displaying it --%>
          <c:remove var="message" scope="session" />
          <c:remove var="messageType" scope="session" />
        </c:if>

        <!-- Page Content from specific JSP -->
        <jsp:doBody />
      </main>
    </div>

    <!-- Footer -->
    <jsp:include page="/WEB-INF/jsp/components/footer.jsp" />
  </body>
</html>
