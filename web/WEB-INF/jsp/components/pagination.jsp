<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%-- Reusable pagination
component Required attributes: - currentPage: The current page number (1-based)
- totalPages: Total number of pages - baseUrl: The base URL for pagination links
(e.g., "/browse?search=games&") --%>

<c:if test="${totalPages > 1}">
  <div class="gv-pagination">
    <%-- Previous page --%>
    <c:choose>
      <c:when test="${currentPage > 1}">
        <a href="${baseUrl}page=${currentPage - 1}" class="gv-pagination-item">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fill-rule="evenodd"
              d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
              clip-rule="evenodd"
            />
          </svg>
        </a>
      </c:when>
      <c:otherwise>
        <span class="gv-pagination-item opacity-50">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fill-rule="evenodd"
              d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
              clip-rule="evenodd"
            />
          </svg>
        </span>
      </c:otherwise>
    </c:choose>

    <%-- Page numbers --%>
    <c:set var="startPage" value="${Math.max(1, currentPage - 2)}" />
    <c:set var="endPage" value="${Math.min(totalPages, startPage + 4)}" />
    <c:if test="${startPage > 1}">
      <a href="${baseUrl}page=1" class="gv-pagination-item">1</a>
      <c:if test="${startPage > 2}">
        <span class="gv-pagination-item">...</span>
      </c:if>
    </c:if>

    <c:forEach begin="${startPage}" end="${endPage}" var="i">
      <c:choose>
        <c:when test="${i == currentPage}">
          <span class="gv-pagination-item gv-pagination-active">${i}</span>
        </c:when>
        <c:otherwise>
          <a href="${baseUrl}page=${i}" class="gv-pagination-item">${i}</a>
        </c:otherwise>
      </c:choose>
    </c:forEach>

    <c:if test="${endPage < totalPages}">
      <c:if test="${endPage < totalPages - 1}">
        <span class="gv-pagination-item">...</span>
      </c:if>
      <a href="${baseUrl}page=${totalPages}" class="gv-pagination-item"
        >${totalPages}</a
      >
    </c:if>

    <%-- Next page --%>
    <c:choose>
      <c:when test="${currentPage < totalPages}">
        <a href="${baseUrl}page=${currentPage + 1}" class="gv-pagination-item">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fill-rule="evenodd"
              d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
              clip-rule="evenodd"
            />
          </svg>
        </a>
      </c:when>
      <c:otherwise>
        <span class="gv-pagination-item opacity-50">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fill-rule="evenodd"
              d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
              clip-rule="evenodd"
            />
          </svg>
        </span>
      </c:otherwise>
    </c:choose>
  </div>
</c:if>
