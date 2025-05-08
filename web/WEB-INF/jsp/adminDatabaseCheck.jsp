<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:set var="pageTitle" value="Database Structure Check" />
    <jsp:include page="components/adminHead.jsp" />
</head>
<body class="bg-gray-100">
    <jsp:include page="components/adminSidebarNew.jsp" />
    <jsp:include page="components/adminTopNav.jsp" />

    <div class="pt-24 md:pl-64 container mx-auto px-4 py-6 transition-all duration-200" id="mainContent">
        <!-- Page Title -->
        <div class="flex justify-between items-center mb-6">
            <h1 class="text-3xl font-bold text-gray-800">Database Structure Check</h1>
        </div>

        <!-- Main Content Card -->
        <div class="bg-white rounded-lg shadow overflow-hidden">
            <div class="p-6">
                <h2 class="text-lg font-semibold text-gray-800 mb-4">Table Check Results</h2>

                <c:if test="${empty missingTables}">
                    <div class="mb-6 p-4 bg-green-50 border-l-4 border-green-400 text-green-700 rounded">
                        <div class="flex items-center">
                            <i class="bi bi-check-circle text-xl mr-2"></i>
                            <p class="font-medium">All required tables exist!</p>
                        </div>
                    </div>
                </c:if>
                
                <c:if test="${not empty missingTables}">
                    <div class="mb-6 p-4 bg-red-50 border-l-4 border-red-400 text-red-700 rounded">
                        <div class="flex items-center">
                            <i class="bi bi-exclamation-triangle text-xl mr-2"></i>
                            <p class="font-medium">Missing tables detected!</p>
                        </div>
                    </div>
                </c:if>

                <div class="overflow-x-auto mb-6">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Table Name</th>
                                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach var="table" items="${requiredTables}">
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${table}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm">
                                        <c:choose>
                                            <c:when test="${existingTables.contains(table)}">
                                                <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                                                    <i class="bi bi-check-circle mr-1"></i> Exists
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
                                                    <i class="bi bi-x-circle mr-1"></i> Missing
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <c:if test="${not empty missingTables}">
                    <h2 class="text-lg font-semibold text-gray-800 mt-6 mb-2">SQL to Create Missing Tables</h2>
                    <p class="text-gray-600 mb-2">Run these SQL statements to create the missing tables:</p>
                    <div class="bg-gray-50 p-4 rounded-md border border-gray-200 overflow-x-auto">
                        <pre class="text-gray-700 text-sm whitespace-pre-wrap">${sqlStatements}</pre>
                    </div>
                </c:if>

                <c:if test="${not empty columnCheckResults}">
                    <h2 class="text-lg font-semibold text-gray-800 mt-6 mb-2">Column Check for Existing Tables</h2>
                    <div class="space-y-4">
                        <c:forEach var="result" items="${columnCheckResults}">
                            <div class="p-4 rounded 
                                <c:choose>
                                    <c:when test="${result.status == 'success'}">bg-green-50 text-green-700</c:when>
                                    <c:when test="${result.status == 'error'}">bg-red-50 text-red-700</c:when>
                                    <c:when test="${result.status == 'warning'}">bg-amber-50 text-amber-700</c:when>
                                    <c:otherwise>bg-gray-50 text-gray-700</c:otherwise>
                                </c:choose>">
                                <p>${result.message}</p>
                                <c:if test="${not empty result.sql}">
                                    <p class="mt-2 mb-1 font-medium">Run this SQL to add it:</p>
                                    <div class="bg-gray-100 p-3 rounded border border-gray-200 overflow-x-auto">
                                        <pre class="text-gray-700 text-sm whitespace-pre-wrap">${result.sql}</pre>
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="mt-6 p-4 bg-red-50 border-l-4 border-red-400 text-red-700 rounded">
                        <p class="font-medium">Error checking database structure: ${error}</p>
                    </div>
                </c:if>

                <div class="mt-8 border-t border-gray-200 pt-4 flex flex-wrap gap-3">
                    <a href="${pageContext.request.contextPath}/admin/generate-dummy-data" 
                       class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                        <i class="bi bi-database-add mr-2"></i> Try Generating Dummy Data
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/dashboard" 
                       class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                        <i class="bi bi-arrow-left mr-2"></i> Return to Admin Dashboard
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Sidebar Scripts -->
    <jsp:include page="components/adminSidebarScripts.jsp" />
</body>
</html>
