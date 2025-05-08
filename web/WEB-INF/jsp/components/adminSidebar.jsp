<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%-- Admin sidebar
navigation component to be included in all admin pages --%>
<div
  class="bg-gray-800 text-white w-64 min-h-screen fixed left-0 top-0 pt-16 z-40"
>
  <div class="p-4">
    <h2 class="text-xl font-bold mb-6 text-white">Admin Dashboard</h2>

    <nav class="space-y-1">
      <a
        href="${pageContext.request.contextPath}/admin/dashboard"
        class="group flex items-center px-2 py-2 text-sm font-medium rounded-md ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/dashboard' ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white'}"
      >
        <svg
          class="mr-3 h-6 w-6 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/dashboard' ? 'text-gray-300' : 'text-gray-400 group-hover:text-gray-300'}"
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
          />
        </svg>
        Dashboard
      </a>

      <a
        href="${pageContext.request.contextPath}/admin/game-management"
        class="group flex items-center px-2 py-2 text-sm font-medium rounded-md ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/game-management' ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white'}"
      >
        <svg
          class="mr-3 h-6 w-6 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/game-management' ? 'text-gray-300' : 'text-gray-400 group-hover:text-gray-300'}"
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z"
          />
        </svg>
        Game Management
      </a>

      <a
        href="${pageContext.request.contextPath}/admin/load-json-data"
        class="group flex items-center px-2 py-2 text-sm font-medium rounded-md ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/load-json-data' ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white'}"
      >
        <svg
          class="mr-3 h-6 w-6 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/load-json-data' ? 'text-gray-300' : 'text-gray-400 group-hover:text-gray-300'}"
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"
          />
        </svg>
        Load JSON Data
      </a>

      <a
        href="${pageContext.request.contextPath}/admin/check-database"
        class="group flex items-center px-2 py-2 text-sm font-medium rounded-md ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/check-database' ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white'}"
      >
        <svg
          class="mr-3 h-6 w-6 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/check-database' ? 'text-gray-300' : 'text-gray-400 group-hover:text-gray-300'}"
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z"
          />
        </svg>
        Check Database
      </a>

      <a
        href="${pageContext.request.contextPath}/admin/generate-dummy-data"
        class="group flex items-center px-2 py-2 text-sm font-medium rounded-md ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/generate-dummy-data' ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white'}"
      >
        <svg
          class="mr-3 h-6 w-6 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/generate-dummy-data' ? 'text-gray-300' : 'text-gray-400 group-hover:text-gray-300'}"
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"
          />
        </svg>
        Generate Dummy Data
      </a>
    </nav>
  </div>

  <div class="p-4 mt-8 border-t border-gray-700">
    <div class="flex items-center">
      <div class="ml-3">
        <p class="text-sm font-medium text-white">Admin User</p>
        <a
          href="${pageContext.request.contextPath}/home"
          class="text-xs font-medium text-gray-300 hover:text-white"
        >
          Return to main site
        </a>
      </div>
    </div>
  </div>
</div>
