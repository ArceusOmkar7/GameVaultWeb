<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="gv-card">
  <div class="gv-card-header">
    <h1 class="text-xl font-bold">Dummy Data Generator</h1>
  </div>
  <div class="gv-card-body">
    <c:choose>
      <c:when test="${dataLoaded && !forceReload}">
        <p class="gv-alert gv-alert-warning">
          Dummy data has already been generated on ${lastLoadDate}
        </p>
        <p>If you want to generate more data, click the button below:</p>
        <a
          href="${pageContext.request.contextPath}/admin/generate-dummy-data?force=true"
          class="gv-btn gv-btn-primary"
        >
          Force Regenerate Data
        </a>

        <p class="mt-6">
          If you're experiencing errors, you can check your database structure:
        </p>
        <a
          href="${pageContext.request.contextPath}/admin/check-database"
          class="gv-btn gv-btn-outline"
        >
          Check Database Structure
        </a>
      </c:when>
      <c:otherwise>
        <c:if test="${not empty error}">
          <div class="gv-alert gv-alert-error mb-4">
            <p>${error}</p>
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

          <p class="mb-4">Please check your database structure:</p>
          <a
            href="${pageContext.request.contextPath}/admin/check-database"
            class="gv-btn gv-btn-outline"
          >
            Check Database Structure
          </a>
        </c:if>

        <c:if test="${generationSuccess}">
          <div class="gv-alert gv-alert-success mb-4">
            <p>Successfully generated dummy data!</p>
          </div>
        </c:if>

        <c:if test="${not empty logs}">
          <h3 class="gv-subtitle mt-6 mb-2">Process Log:</h3>
          <div
            class="bg-gray-50 p-4 rounded-md border border-gray-200 max-h-96 overflow-auto"
          >
            <ul class="space-y-1">
              <c:forEach var="log" items="${logs}">
                <c:choose>
                  <c:when test="${log.startsWith('ERROR:')}">
                    <li class="text-red-600 font-semibold">${log}</li>
                  </c:when>
                  <c:when test="${log.startsWith('Warning:')}">
                    <li class="text-yellow-600">${log}</li>
                  </c:when>
                  <c:otherwise>
                    <li class="text-gray-700">${log}</li>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
            </ul>
          </div>
        </c:if>
      </c:otherwise>
    </c:choose>

    <div class="mt-8">
      <a
        href="${pageContext.request.contextPath}/admin/dashboard"
        class="gv-btn gv-btn-outline"
      >
        Return to Admin Dashboard
      </a>
    </div>
  </div>
</div>
