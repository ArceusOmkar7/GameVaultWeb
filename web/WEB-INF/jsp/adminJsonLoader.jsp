<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <c:set var="pageTitle" value="Admin JSON Game Loader" />
    <jsp:include page="components/adminHead.jsp" />
  </head>
  <body class="bg-gray-100">
    <jsp:include page="components/adminSidebarNew.jsp" />
    <jsp:include page="components/adminTopNav.jsp" />

    <div
      class="pt-24 md:pl-64 container mx-auto px-4 py-6 transition-all duration-200"
      id="mainContent"
    >
      <!-- Page Title -->
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold text-gray-800">JSON Game Data Loader</h1>
      </div>

      <!-- Main Content Card -->
      <div class="bg-white rounded-lg shadow overflow-hidden">
        <div class="p-6">
          <c:choose>
            <c:when test="${dataLoaded && !forceReload}">
              <div
                class="mb-6 p-4 bg-amber-50 border-l-4 border-amber-400 text-amber-700 rounded"
              >
                <p class="font-medium">
                  JSON game data has already been loaded on ${lastLoadDate}
                </p>
              </div>

              <p class="mb-4 text-gray-600">
                If you want to reload the data, click the button below:
              </p>
              <a
                href="${pageContext.request.contextPath}/admin/load-json-data?force=true"
                class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
              >
                <i class="bi bi-arrow-repeat mr-2"></i> Force Reload Data
              </a>

              <p class="mt-6 mb-4 text-gray-600">
                If you're experiencing errors, you can check your database
                structure:
              </p>
              <a
                href="${pageContext.request.contextPath}/admin/check-database"
                class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
              >
                <i class="bi bi-database-check mr-2"></i> Check Database
                Structure
              </a>
            </c:when>
            <c:otherwise>
              <c:if test="${not empty error}">
                <div
                  class="mb-6 p-4 bg-red-50 border-l-4 border-red-400 text-red-700 rounded"
                >
                  <p class="font-medium">${error}</p>
                </div>

                <c:if test="${not empty stackTrace}">
                  <div
                    class="bg-red-50 p-4 rounded-md border border-red-100 overflow-auto max-h-96 mb-6"
                  >
                    <pre class="text-red-800 text-sm whitespace-pre-wrap">
${stackTrace}</pre
                    >
                  </div>
                </c:if>

                <p class="mb-4 text-gray-600">
                  Please check your database structure:
                </p>
                <a
                  href="${pageContext.request.contextPath}/admin/check-database"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                >
                  <i class="bi bi-database-check mr-2"></i> Check Database
                  Structure
                </a>
              </c:if>

              <c:if test="${generationSuccess}">
                <div
                  class="mb-6 p-4 bg-green-50 border-l-4 border-green-400 text-green-700 rounded"
                >
                  <div class="flex items-center">
                    <i class="bi bi-check-circle text-xl mr-2"></i>
                    <p class="font-medium">
                      Successfully loaded ${successCount} of ${totalCount} games
                      from JSON file!
                    </p>
                  </div>
                </div>
              </c:if>

              <c:if test="${not empty logs}">
                <h3 class="text-lg font-semibold text-gray-800 mt-6 mb-2">
                  Process Log:
                </h3>
                <div
                  class="bg-gray-50 p-4 rounded-md border border-gray-200 max-h-96 overflow-auto"
                >
                  <ul class="space-y-1">
                    <c:forEach var="log" items="${logs}">
                      <c:choose>
                        <c:when test="${log.startsWith('ERROR:')}">
                          <li class="text-red-600 font-semibold">
                            <i class="bi bi-x-circle mr-1"></i> ${log}
                          </li>
                        </c:when>
                        <c:when test="${log.startsWith('Warning:')}">
                          <li class="text-yellow-600">
                            <i class="bi bi-exclamation-triangle mr-1"></i>
                            ${log}
                          </li>
                        </c:when>
                        <c:otherwise>
                          <li class="text-gray-700">
                            <i class="bi bi-info-circle mr-1"></i> ${log}
                          </li>
                        </c:otherwise>
                      </c:choose>
                    </c:forEach>
                  </ul>
                </div>
              </c:if>
            </c:otherwise>
          </c:choose>

          <div class="mt-8 border-t border-gray-200 pt-4">
            <a
              href="${pageContext.request.contextPath}/admin/dashboard"
              class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              <i class="bi bi-arrow-left mr-2"></i> Return to Admin Dashboard
            </a>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
