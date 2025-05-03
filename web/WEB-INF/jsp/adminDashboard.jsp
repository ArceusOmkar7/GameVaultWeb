<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GameVault - Admin Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-100 flex flex-col min-h-screen">
    <jsp:include page="header.jsp" />

    <main class="flex-grow container mx-auto p-4">
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="flex justify-between items-center mb-6">
          <h1 class="text-3xl font-bold text-purple-800">Admin Dashboard</h1>
          <div>
            <c:if
              test="${not empty sessionScope.loggedInUser && sessionScope.loggedInUser.admin}"
            >
              <span
                class="bg-purple-100 text-purple-800 text-xs font-medium mr-2 px-2.5 py-0.5 rounded"
              >
                Admin: ${sessionScope.loggedInUser.username}
              </span>
            </c:if>
          </div>
        </div>

        <%-- Display messages --%>
        <c:if test="${not empty param.message}">
          <div
            class="mb-4 p-4 rounded ${param.messageType eq 'success' ? 'bg-green-100 border border-green-400 text-green-700' : 'bg-red-100 border border-red-400 text-red-700'}"
            role="alert"
          >
            <p><c:out value="${param.message}" /></p>
          </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
          <div
            class="mb-4 p-4 rounded bg-red-100 border border-red-400 text-red-700"
            role="alert"
          >
            <p><strong>Error:</strong> <c:out value="${errorMessage}" /></p>
          </div>
        </c:if>

        <%-- Admin Dashboard Tabs --%>
        <div class="mb-6">
          <ul
            class="flex flex-wrap text-sm font-medium text-center text-gray-500 border-b border-gray-200"
          >
            <li class="mr-2">
              <a
                href="#"
                class="inline-block p-4 text-purple-600 bg-gray-100 rounded-t-lg active"
                aria-current="page"
              >
                Game Management
              </a>
            </li>
            <li class="mr-2">
              <a
                href="#"
                class="inline-block p-4 rounded-t-lg hover:text-gray-600 hover:bg-gray-50"
              >
                User Management
              </a>
            </li>
            <li class="mr-2">
              <a
                href="#"
                class="inline-block p-4 rounded-t-lg hover:text-gray-600 hover:bg-gray-50"
              >
                Order History
              </a>
            </li>
          </ul>
        </div>

        <%-- Game Management Section --%>
        <div class="overflow-x-auto">
          <h2 class="text-2xl font-semibold mb-4">Game Catalog</h2>

          <table class="min-w-full bg-white border border-gray-200">
            <thead class="bg-gray-100">
              <tr>
                <th class="py-2 px-4 border-b text-left">ID</th>
                <th class="py-2 px-4 border-b text-left">Image</th>
                <th class="py-2 px-4 border-b text-left">Title</th>
                <th class="py-2 px-4 border-b text-left">Developer</th>
                <th class="py-2 px-4 border-b text-left">Platform</th>
                <th class="py-2 px-4 border-b text-left">Price</th>
                <th class="py-2 px-4 border-b text-left">Release Date</th>
                <th class="py-2 px-4 border-b text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="game" items="${games}">
                <tr class="hover:bg-gray-50">
                  <td class="py-2 px-4 border-b">${game.gameId}</td>
                  <td class="py-2 px-4 border-b">
                    <c:choose>
                      <c:when test="${not empty game.imagePath}">
                        <img
                          src="${pageContext.request.contextPath}/${game.imagePath}"
                          alt="${game.title}"
                          class="w-20 h-20 object-cover rounded"
                        />
                      </c:when>
                      <c:otherwise>
                        <div
                          class="w-20 h-20 bg-gray-200 rounded flex items-center justify-center text-gray-400 text-xs"
                        >
                          No Image
                        </div>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td class="py-2 px-4 border-b">${game.title}</td>
                  <td class="py-2 px-4 border-b">${game.developer}</td>
                  <td class="py-2 px-4 border-b">${game.platform}</td>
                  <td class="py-2 px-4 border-b">
                    <fmt:formatNumber
                      value="${game.price}"
                      type="currency"
                      currencySymbol="$"
                    />
                  </td>
                  <td class="py-2 px-4 border-b">
                    <form
                      action="${pageContext.request.contextPath}/admin/upload-game-image"
                      method="post"
                      enctype="multipart/form-data"
                      class="flex items-center space-x-2"
                    >
                      <input
                        type="hidden"
                        name="gameId"
                        value="${game.gameId}"
                      />
                      <div class="flex-1">
                        <input
                          type="file"
                          name="gameImage"
                          accept="image/*"
                          class="w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
                        />
                      </div>
                      <button
                        type="submit"
                        class="bg-blue-500 hover:bg-blue-700 text-white py-1 px-3 rounded text-sm"
                      >
                        Upload
                      </button>
                    </form>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </main>

    <jsp:include page="footer.jsp" />
  </body>
</html>
