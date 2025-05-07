<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${param.pageTitle} - GameVault</title>
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
  <body class="flex flex-col min-h-screen bg-gray-50">
    <!-- Header -->
    <jsp:include page="/WEB-INF/jsp/components/header.jsp" />

    <!-- Main Content -->
    <main class="flex-grow">
      <!-- System Message/Alert -->
      <c:if test="${not empty sessionScope.message}">
        <div class="gv-container mt-4">
          <div
            class="gv-alert p-4 border-l-4 ${sessionScope.messageType == 'error' ? 'border-red-500 bg-red-50 text-red-700' : sessionScope.messageType == 'warning' ? 'border-yellow-500 bg-yellow-50 text-yellow-700' : 'border-green-500 bg-green-50 text-green-700'}"
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

    <!-- Footer -->
    <jsp:include page="/WEB-INF/jsp/components/footer.jsp" />
  </body>
</html>
