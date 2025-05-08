<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GameVault - Admin Game Management</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
    />
  </head>
  <body class="bg-gray-100">
    <!-- Admin Sidebar Component -->
    <jsp:include page="components/adminSidebarNew.jsp" />

    <!-- Top Navbar -->
    <jsp:include page="components/adminTopNav.jsp" />

    <!-- Main Content -->
    <div
      class="pt-24 md:pl-64 container mx-auto px-4 py-6 transition-all duration-200"
      id="mainContent"
    >
      <!-- Page Title -->
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold text-gray-800">Game Management</h1>
        <button
          id="addGameBtn"
          class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded font-semibold flex items-center gap-2"
        >
          <i class="bi bi-plus-circle"></i> Add New Game
        </button>
      </div>

      <!-- Game Filter Controls Component -->
      <jsp:include page="components/adminGameFilters.jsp" />

      <!-- Games List Table Component -->
      <jsp:include page="components/adminGameTable.jsp" />

      <!-- Pagination -->
      <div
        class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6"
      >
        <div
          class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between"
        >
          <div>
            <p class="text-sm text-gray-700">
              Showing <span class="font-medium">${firstGame}</span> to
              <span class="font-medium">${lastGame}</span> of
              <span class="font-medium">${totalGames}</span> results
            </p>
          </div>
          <div>
            <c:if test="${totalPages > 1}">
              <nav
                class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px"
                aria-label="Pagination"
              >
                <!-- Previous page link -->
                <c:choose>
                  <c:when test="${currentPage > 1}">
                    <a
                      href="${pageContext.request.contextPath}${baseUrl}page=${currentPage - 1}"
                      class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
                    >
                      <span class="sr-only">Previous</span>
                      <i class="bi bi-chevron-left"></i>
                    </a>
                  </c:when>
                  <c:otherwise>
                    <span
                      class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-300 cursor-not-allowed"
                    >
                      <span class="sr-only">Previous</span>
                      <i class="bi bi-chevron-left"></i>
                    </span>
                  </c:otherwise>
                </c:choose>

                <!-- Page numbers -->
                <c:set
                  var="startPage"
                  value="${(currentPage > 2) ? (currentPage - 2) : 1}"
                />
                <c:set
                  var="endPage"
                  value="${(startPage + 4 < totalPages) ? (startPage + 4) : totalPages}"
                />

                <!-- Show first page if not in range -->
                <c:if test="${startPage > 1}">
                  <a
                    href="${pageContext.request.contextPath}${baseUrl}page=1"
                    class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
                    >1</a
                  >
                  <c:if test="${startPage > 2}">
                    <span
                      class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700"
                      >...</span
                    >
                  </c:if>
                </c:if>

                <!-- Page range -->
                <c:forEach begin="${startPage}" end="${endPage}" var="i">
                  <c:choose>
                    <c:when test="${i == currentPage}">
                      <span
                        class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-blue-50 text-sm font-medium text-blue-600"
                        >${i}</span
                      >
                    </c:when>
                    <c:otherwise>
                      <a
                        href="${pageContext.request.contextPath}${baseUrl}page=${i}"
                        class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
                        >${i}</a
                      >
                    </c:otherwise>
                  </c:choose>
                </c:forEach>

                <!-- Show last page if not in range -->
                <c:if test="${endPage < totalPages}">
                  <c:if test="${endPage < totalPages - 1}">
                    <span
                      class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700"
                      >...</span
                    >
                  </c:if>
                  <a
                    href="${pageContext.request.contextPath}${baseUrl}page=${totalPages}"
                    class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
                    >${totalPages}</a
                  >
                </c:if>

                <!-- Next page link -->
                <c:choose>
                  <c:when test="${currentPage < totalPages}">
                    <a
                      href="${pageContext.request.contextPath}${baseUrl}page=${currentPage + 1}"
                      class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
                    >
                      <span class="sr-only">Next</span>
                      <i class="bi bi-chevron-right"></i>
                    </a>
                  </c:when>
                  <c:otherwise>
                    <span
                      class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-300 cursor-not-allowed"
                    >
                      <span class="sr-only">Next</span>
                      <i class="bi bi-chevron-right"></i>
                    </span>
                  </c:otherwise>
                </c:choose>
              </nav>
            </c:if>
          </div>
        </div>
      </div>
    </div>

    <!-- Game Modal Components -->
    <jsp:include page="components/adminAddGameModal.jsp" />
    <jsp:include page="components/adminEditGameModal.jsp" />
    <jsp:include page="components/adminDeleteGameModal.jsp" />

    <!-- Sidebar Scripts -->
    <jsp:include page="components/adminSidebarScripts.jsp" />

    <!-- Game Management Scripts -->
    <jsp:include page="components/adminGameScripts.jsp" />
  </body>
</html>
