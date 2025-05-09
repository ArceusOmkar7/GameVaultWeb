<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
  // Global variables to store chart instances
  let salesChart;
  let topGamesChart;
  let userGrowthChart;
  let revenuePieChart;
  let platformDoughnutChart;

  // Initialize all charts on page load
  document.addEventListener("DOMContentLoaded", function () {
    initializeSalesChart();
    initializeTopGamesChart();
    initializeUserGrowthChart();
    initializeRevenueBreakdownChart();
    initializePlatformDistributionChart();
  });
  // Function to fetch chart data via AJAX
  async function fetchChartData(chartType) {
    try {
      const response = await fetch(
        `${pageContext.request.contextPath}/admin/api/dashboard-data?chart=${chartType}`
      );
      if (!response.ok) {
        const errorData = await response.json();
        console.error(`Error fetching ${chartType} data:`, errorData.error);
        return null;
      }
      return await response.json();
    } catch (error) {
      console.error(`Error fetching ${chartType} data:`, error);
      return null;
    }
  }
  // Initialize Sales Overview Chart
  function initializeSalesChart() {
    const salesCtx = document.getElementById("salesChart").getContext("2d");
    salesChart = new Chart(salesCtx, {
      type: "line",
      data: {
        labels: [
          <c:forEach var="date" items="${salesData.dates}" varStatus="loop">
            "${date}"<c:if test="${!loop.last}">, </c:if>
          </c:forEach>,
        ],
        datasets: [
          {
            label: "Sales",
            data: [
              <c:forEach var="sale" items="${salesData.sales}" varStatus="loop">
                ${sale}
                <c:if test="${!loop.last}">, </c:if>
              </c:forEach>,
            ],
            borderColor: "#3b82f6",
            backgroundColor: "rgba(59, 130, 246, 0.1)",
            fill: true,
            tension: 0.4,
            pointRadius: 4,
            pointBackgroundColor: "#3b82f6",
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: function (context) {
                return `$${context.raw.toFixed(2)}`;
              },
            },
          },
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: function (value) {
                return "$" + value;
              },
            },
          },
          x: { grid: { display: false } },
        },
        layout: {
          padding: {
            left: 10,
            right: 25,
            top: 25,
            bottom: 10,
          },
        },
      },
    });
  }

  // Initialize Top Selling Games Chart
  function initializeTopGamesChart() {
    const topGamesCtx = document
      .getElementById("topGamesChart")
      .getContext("2d");
    topGamesChart = new Chart(topGamesCtx, {
      type: "bar",
      data: {
        labels: [
          <c:forEach
            var="title"
            items="${topGamesData.titles}"
            varStatus="loop"
          >
            "${title}"<c:if test="${!loop.last}">, </c:if>
          </c:forEach>,
        ],
        datasets: [
          {
            label: "Units Sold",
            data: [
              <c:forEach
                var="sale"
                items="${topGamesData.sales}"
                varStatus="loop"
              >
                ${sale}
                <c:if test="${!loop.last}">, </c:if>
              </c:forEach>,
            ],
            backgroundColor: [
              "#6366f1", // indigo
              "#22d3ee", // cyan
              "#f59e42", // orange
              "#84cc16", // lime
              "#f43f5e", // red
            ],
            borderRadius: 8,
            maxBarThickness: 32,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false },
          title: { display: false },
        },
        scales: {
          x: { grid: { display: false } },
          y: { beginAtZero: true },
        },
      },
    });
  }

  // Initialize User Growth Chart
  function initializeUserGrowthChart() {
    const userGrowthCtx = document
      .getElementById("userGrowthChart")
      .getContext("2d");
    userGrowthChart = new Chart(userGrowthCtx, {
      type: "line",
      data: {
        labels: [
          <c:forEach
            var="month"
            items="${userGrowthData.months}"
            varStatus="loop"
          >
            "${month}"<c:if test="${!loop.last}">, </c:if>
          </c:forEach>,
        ],
        datasets: [
          {
            label: "New Users",
            data: [
              <c:forEach
                var="count"
                items="${userGrowthData.users}"
                varStatus="loop"
              >
                ${count}
                <c:if test="${!loop.last}">, </c:if>
              </c:forEach>,
            ],
            borderColor: "#10b981",
            backgroundColor: "rgba(16, 185, 129, 0.15)",
            fill: true,
            tension: 0.4,
            pointRadius: 4,
            pointBackgroundColor: "#10b981",
          },
        ],
      },
      options: {
        responsive: true,
        plugins: { legend: { display: false } },
        scales: {
          y: { beginAtZero: true },
          x: { grid: { display: false } },
        },
      },
    });
  }

  // Initialize Revenue Breakdown Chart
  function initializeRevenueBreakdownChart() {
    const revenuePieCtx = document
      .getElementById("revenuePieChart")
      .getContext("2d");
    revenuePieChart = new Chart(revenuePieCtx, {
      type: "pie",
      data: {
        labels: [
          <c:forEach
            var="genre"
            items="${revenueBreakdownData.genres}"
            varStatus="loop"
          >
            "${genre}"<c:if test="${!loop.last}">, </c:if>
          </c:forEach>,
        ],
        datasets: [
          {
            data: [
              <c:forEach
                var="revenue"
                items="${revenueBreakdownData.revenues}"
                varStatus="loop"
              >
                ${revenue}
                <c:if test="${!loop.last}">, </c:if>
              </c:forEach>,
            ],
            backgroundColor: [
              "#3b82f6", // blue
              "#f59e42", // orange
              "#10b981", // green
              "#6366f1", // indigo
              "#f43f5e", // red
              "#22d3ee", // cyan
              "#84cc16", // lime
              "#a855f7", // purple
              "#ec4899", // pink
            ],
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: "bottom" },
          tooltip: {
            callbacks: {
              label: function (context) {
                return `$${context.raw.toFixed(2)}`;
              },
            },
          },
        },
      },
    });
  }

  // Initialize Platform Distribution Chart
  function initializePlatformDistributionChart() {
    const platformDoughnutCtx = document
      .getElementById("platformDoughnutChart")
      .getContext("2d");
    platformDoughnutChart = new Chart(platformDoughnutCtx, {
      type: "doughnut",
      data: {
        labels: [
          <c:forEach
            var="platform"
            items="${platformDistributionData.platforms}"
            varStatus="loop"
          >
            "${platform}"<c:if test="${!loop.last}">, </c:if>
          </c:forEach>,
        ],
        datasets: [
          {
            data: [
              <c:forEach
                var="count"
                items="${platformDistributionData.counts}"
                varStatus="loop"
              >
                ${count}
                <c:if test="${!loop.last}">, </c:if>
              </c:forEach>,
            ],
            backgroundColor: [
              "#6366f1", // indigo
              "#f43f5e", // red
              "#22d3ee", // cyan
              "#f59e42", // orange
              "#10b981", // green
              "#a855f7", // purple
              "#84cc16", // lime
              "#ec4899", // pink
            ],
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: "bottom" },
        },
      },
    });
  }
</script>
