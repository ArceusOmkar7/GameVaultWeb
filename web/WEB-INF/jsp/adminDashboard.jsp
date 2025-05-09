<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <c:set var="pageTitle" value="Admin Dashboard" />
    <jsp:include page="components/adminHead.jsp" />
    <style>
      .dashboard-section {
        background: white;
        border-radius: 0.75rem;
        box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.08);
        margin-bottom: 2rem;
        padding: 0;
        display: flex;
        flex-direction: column;
        align-items: center;
      }
      .dashboard-section-header {
        padding: 1.25rem 2rem 0.5rem 2rem;
        border-bottom: 1px solid #e5e7eb;
        width: 100%;
        text-align: center;
      }
      .dashboard-section-content {
        padding: 2rem 2rem 2.5rem 2rem;
        width: 100%;
        display: flex;
        justify-content: center;
      }
      .dashboard-main-wrapper {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
      }
      .dashboard-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
        gap: 2rem;
        width: 100%;
        max-width: 1200px;
        margin: 0 auto;
      }
      .dashboard-row {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        gap: 2rem;
        width: 100%;
        max-width: 1200px;
        margin: 0 auto 2rem auto;
      }
      @media (max-width: 900px) {
        .dashboard-grid,
        .dashboard-row {
          grid-template-columns: 1fr;
          flex-direction: column;
          gap: 1.25rem;
        }
        .dashboard-section-content {
          padding: 1.25rem;
        }
      }
    </style>
  </head>
  <body class="bg-gray-100">
    <jsp:include page="components/adminSidebarNew.jsp" />
    <jsp:include page="components/adminTopNav.jsp" />

    <div
      class="pt-24 md:pl-64 min-h-screen flex flex-col items-center justify-start bg-gray-100"
    >
      <div class="dashboard-main-wrapper">
        <!-- Page Header -->
        <div class="flex justify-between items-center mb-8 w-full max-w-5xl">
          <h1 class="text-3xl font-bold text-gray-800">Admin Dashboard</h1>
          <div class="flex space-x-4">
            <a
              href="${pageContext.request.contextPath}/admin/export-report"
              class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center"
              style="text-decoration: none"
            >
              <i class="bi bi-download mr-2"></i>Export Report
            </a>
          </div>
        </div>

        <!-- Summary Stats Cards -->
        <div class="w-full max-w-5xl mb-8">
          <jsp:include page="components/adminDashboardStats.jsp" />
        </div>

        <!-- Sales Overview -->
        <div class="dashboard-section w-full max-w-5xl mb-10">
          <div class="dashboard-section-header">
            <h2 class="text-xl font-semibold">Sales Overview</h2>
          </div>
          <div class="dashboard-section-content">
            <canvas
              id="salesChart"
              height="300"
              style="max-width: 1000px; width: 100%"
            ></canvas>
          </div>
        </div>

        <!-- Game Statistics Section -->
        <div class="dashboard-grid mb-10">
          <!-- Rating Distribution -->
          <div class="dashboard-section">
            <div class="dashboard-section-header">
              <h3 class="text-lg font-medium">Rating Distribution</h3>
            </div>
            <div class="dashboard-section-content">
              <canvas
                id="ratingDistributionChart"
                height="250"
                style="max-width: 350px; width: 100%"
              ></canvas>
            </div>
          </div>

          <!-- Price Ranges -->
          <div class="dashboard-section">
            <div class="dashboard-section-header">
              <h3 class="text-lg font-medium">Price Range Distribution</h3>
            </div>
            <div class="dashboard-section-content">
              <canvas
                id="priceRangesChart"
                height="250"
                style="max-width: 350px; width: 100%"
              ></canvas>
            </div>
          </div>

          <!-- Genre Popularity -->
          <div class="dashboard-section">
            <div class="dashboard-section-header">
              <h3 class="text-lg font-medium">Top Genres</h3>
            </div>
            <div class="dashboard-section-content">
              <canvas
                id="genrePopularityChart"
                height="250"
                style="max-width: 350px; width: 100%"
              ></canvas>
            </div>
          </div>
        </div>

        <!-- Other Charts Grid -->
        <div class="dashboard-grid mb-10">
          <!-- Top Games -->
          <div class="dashboard-section">
            <div class="dashboard-section-header">
              <h2 class="text-xl font-semibold">Top Selling Games</h2>
            </div>
            <div class="dashboard-section-content">
              <canvas
                id="topGamesChart"
                height="300"
                style="max-width: 400px; width: 100%"
              ></canvas>
            </div>
          </div>

          <!-- User Growth -->
          <div class="dashboard-section">
            <div class="dashboard-section-header">
              <h2 class="text-xl font-semibold">User Growth</h2>
            </div>
            <div class="dashboard-section-content">
              <canvas
                id="userGrowthChart"
                height="300"
                style="max-width: 400px; width: 100%"
              ></canvas>
            </div>
          </div>

          <!-- Revenue Breakdown -->
          <div class="dashboard-section">
            <div class="dashboard-section-header">
              <h2 class="text-xl font-semibold">Revenue Breakdown</h2>
            </div>
            <div class="dashboard-section-content">
              <canvas
                id="revenuePieChart"
                height="300"
                style="max-width: 400px; width: 100%"
              ></canvas>
            </div>
          </div>

          <!-- Platform Distribution -->
          <div class="dashboard-section">
            <div class="dashboard-section-header">
              <h2 class="text-xl font-semibold">Platform Distribution</h2>
            </div>
            <div class="dashboard-section-content">
              <canvas
                id="platformDoughnutChart"
                height="300"
                style="max-width: 400px; width: 100%"
              ></canvas>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Scripts Section -->
    <jsp:include page="components/adminSidebarScripts.jsp" />
    <jsp:include page="components/adminChartsScripts.jsp" />
  </body>
</html>
