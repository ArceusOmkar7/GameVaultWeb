<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Database Structure Check</title>
    <jsp:include page="theme.jsp" />
    <style>
      body {
        font-family: Arial, sans-serif;
        background-color: #f9fafb;
      }

      .success {
        color: var(--gv-success);
      }
      .error {
        color: var(--gv-error);
      }
      .warning {
        color: var(--gv-warning);
      }
    </style>
  </head>
  <body>
    <jsp:include page="header.jsp" />

    <div class="gv-container gv-page-content">
      <div class="gv-card">
        <div class="gv-card-header">
          <h1 class="text-xl font-bold">Database Structure Check</h1>
        </div>
        <div class="gv-card-body">
          <h2 class="gv-subtitle mb-4">Table Check Results</h2>

          <c:if test="${empty missingTables}">
            <p class="success font-semibold mb-4">
              All required tables exist! ✓
            </p>
          </c:if>
          <c:if test="${not empty missingTables}">
            <p class="error font-semibold mb-4">Missing tables detected! ⚠️</p>
          </c:if>

          <table class="gv-table">
            <thead>
              <tr>
                <th>Table Name</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="table" items="${requiredTables}">
                <tr>
                  <td>${table}</td>
                  <td>
                    <c:choose>
                      <c:when test="${existingTables.contains(table)}">
                        <span class="success">Exists ✓</span>
                      </c:when>
                      <c:otherwise>
                        <span class="error">Missing ✗</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>

          <c:if test="${not empty missingTables}">
            <h2 class="gv-subtitle mt-6 mb-2">SQL to Create Missing Tables</h2>
            <p>Run these SQL statements to create the missing tables:</p>
            <pre
              style="
                background-color: #f5f5f5;
                padding: 10px;
                overflow-x: auto;
                border-radius: 4px;
                margin-top: 10px;
              "
            >
${sqlStatements}</pre
            >
          </c:if>

          <c:if test="${not empty columnCheckResults}">
            <h2 class="gv-subtitle mt-6 mb-2">
              Column Check for Existing Tables
            </h2>
            <c:forEach var="result" items="${columnCheckResults}">
              <p class="${result.status}">${result.message}</p>
              <c:if test="${not empty result.sql}">
                <p>Run this SQL to add it:</p>
                <pre
                  style="
                    background-color: #f5f5f5;
                    padding: 10px;
                    overflow-x: auto;
                    border-radius: 4px;
                    margin-top: 5px;
                  "
                >
${result.sql}</pre
                >
              </c:if>
            </c:forEach>
          </c:if>

          <c:if test="${not empty error}">
            <p class="error mt-4">
              Error checking database structure: ${error}
            </p>
          </c:if>

          <div class="mt-8 space-x-4">
            <a
              href="${pageContext.request.contextPath}/admin/generate-dummy-data"
              class="gv-btn gv-btn-primary"
            >
              Try Generating Dummy Data
            </a>
            <a
              href="${pageContext.request.contextPath}/admin/dashboard"
              class="gv-btn gv-btn-outline"
            >
              Return to Admin Dashboard
            </a>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="footer.jsp" />
  </body>
</html>
