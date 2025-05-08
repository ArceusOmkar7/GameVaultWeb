<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <c:set var="pageTitle" value="Admin Dashboard" />
    <jsp:include page="components/adminHead.jsp" />
  </head>
  <body class="bg-gray-100">
    <jsp:include page="components/adminSidebarNew.jsp" />
    <jsp:include page="components/adminTopNav.jsp" />

    <div
      class="pt-24 md:pl-64 container mx-auto px-4 py-6 transition-all duration-200"
      id="mainContent"
    >
      <!-- Page Title for clarity -->
      <h1 class="text-3xl font-bold mb-6 text-gray-800">Admin Dashboard</h1>

      <!-- Summary Stats Cards -->
      <jsp:include page="components/adminDashboardStats.jsp" />

      <!-- Charts Section -->
      <jsp:include page="components/adminDashboardCharts.jsp" />
    </div>

    <!-- Scripts Section -->
    <jsp:include page="components/adminSidebarScripts.jsp" />
    <jsp:include page="components/adminChartsScripts.jsp" />
  </body>
</html>
