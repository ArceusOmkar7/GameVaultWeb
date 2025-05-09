<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Sidebar -->
<div
  id="sidebar"
  class="fixed top-0 left-0 h-full w-20 md:w-64 bg-gray-900 text-gray-200 z-50 transition-all duration-200 ease-in-out flex flex-col sidebar-collapsed shadow-lg mt-16"
>
  <div class="flex items-center justify-end px-6 py-4 border-b border-gray-800">
    <!-- Sidebar Collapse/Expand Button (Desktop only) -->
    <button
      id="toggleSidebar"
      class="hidden md:inline text-2xl focus:outline-none text-gray-200"
      title="Collapse/Expand Sidebar"
    >
      <i class="bi bi-chevron-left" id="toggleSidebarIcon"></i>
    </button>
    <button
      id="closeSidebar"
      class="md:hidden text-2xl focus:outline-none text-gray-200"
    >
      <i class="bi bi-x"></i>
    </button>
  </div>
  <nav class="flex-1 px-4 py-6">
    <ul class="space-y-2">
      <li>
        <a
          href="${pageContext.request.contextPath}/admin/dashboard"
          class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/dashboard' ? 'bg-gray-800' : ''}"
          ><i class="bi bi-speedometer2 text-gray-200"></i>
          <span class="sidebar-label text-gray-200">Dashboard</span></a
        >
      </li>
      <li>
        <a
          href="${pageContext.request.contextPath}/admin/users"
          class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/users' ? 'bg-gray-800' : ''}"
          ><i class="bi bi-people text-gray-200"></i>
          <span class="sidebar-label text-gray-200">Users</span></a
        >
      </li>
      <li>
        <a
          href="${pageContext.request.contextPath}/admin/game-management"
          class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/game-management' ? 'bg-gray-800' : ''}"
          ><i class="bi bi-controller text-gray-200"></i>
          <span class="sidebar-label text-gray-200">Games</span></a
        >
      </li>
      <li>
        <a
          href="${pageContext.request.contextPath}/admin/orders"
          class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/orders' ? 'bg-gray-800' : ''}"
          ><i class="bi bi-bag-check text-gray-200"></i>
          <span class="sidebar-label text-gray-200">Orders</span></a
        >
      </li>
      <li>
        <a
          href="${pageContext.request.contextPath}/admin/check-database"
          class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/check-database' ? 'bg-gray-800' : ''}"
          ><i class="bi bi-database-fill-gear text-gray-200"></i>
          <span class="sidebar-label text-gray-200">Database Status</span></a
        >
      </li>
      <li>
        <a
          href="${pageContext.request.contextPath}/admin/generate-dummy-data"
          class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/generate-dummy-data' ? 'bg-gray-800' : ''}"
          ><i class="bi bi-database-fill-add text-gray-200"></i>
          <span class="sidebar-label text-gray-200">Dummy Data</span></a
        >
      </li>
      <li>
        <a
          href="${pageContext.request.contextPath}/admin/load-json-data"
          class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200 ${requestScope['javax.servlet.forward.servlet_path'] eq '/admin/load-json-data' ? 'bg-gray-800' : ''}"
          ><i class="bi bi-filetype-json text-gray-200"></i>
          <span class="sidebar-label text-gray-200">Load JSON</span></a
        >
      </li>
      <li>
        <form action="${pageContext.request.contextPath}/logout" method="post">
          <button
            type="submit"
            class="w-full flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200"
          >
            <i class="bi bi-box-arrow-right text-gray-200"></i>
            <span class="sidebar-label text-gray-200">Logout</span>
          </button>
        </form>
      </li>
    </ul>
  </nav>
</div>

<!-- Hamburger Button -->
<button
  id="openSidebar"
  class="fixed top-4 left-4 z-60 bg-blue-700 text-white p-2 rounded focus:outline-none"
>
  <i class="bi bi-list text-2xl"></i>
</button>
