<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
  // Sales Overview Chart
  const salesCtx = document.getElementById("salesChart").getContext("2d");
  const salesChart = new Chart(salesCtx, {
    type: "line",
    data: {
      labels: [
        "2025-04-28",
        "2025-04-29",
        "2025-04-30",
        "2025-05-01",
        "2025-05-02",
        "2025-05-03",
        "2025-05-04",
      ],
      datasets: [
        {
          label: "Sales",
          data: [10, 25, 40, 60, 85, 115, 70],
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
      },
      scales: {
        y: { beginAtZero: true, max: 120 },
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

  // Top Selling Games Bar Chart
  const topGamesCtx = document.getElementById("topGamesChart").getContext("2d");
  const topGamesChart = new Chart(topGamesCtx, {
    type: "bar",
    data: {
      labels: ["Game A", "Game B", "Game C", "Game D", "Game E"],
      datasets: [
        {
          label: "Units Sold",
          data: [120, 95, 80, 60, 45],
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

  // User Growth Area Chart
  const userGrowthCtx = document
    .getElementById("userGrowthChart")
    .getContext("2d");
  const userGrowthChart = new Chart(userGrowthCtx, {
    type: "line",
    data: {
      labels: ["2025-01", "2025-02", "2025-03", "2025-04", "2025-05"],
      datasets: [
        {
          label: "Users",
          data: [1200, 1450, 1700, 2100, 2340],
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

  // Revenue Breakdown Pie Chart
  const revenuePieCtx = document
    .getElementById("revenuePieChart")
    .getContext("2d");
  const revenuePieChart = new Chart(revenuePieCtx, {
    type: "pie",
    data: {
      labels: ["Games", "DLCs", "Subscriptions", "Other"],
      datasets: [
        {
          data: [12000, 3500, 2500, 900],
          backgroundColor: [
            "#3b82f6", // blue
            "#f59e42", // orange
            "#10b981", // green
            "#6366f1", // indigo
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

  // Platform Distribution Doughnut Chart
  const platformDoughnutCtx = document
    .getElementById("platformDoughnutChart")
    .getContext("2d");
  const platformDoughnutChart = new Chart(platformDoughnutCtx, {
    type: "doughnut",
    data: {
      labels: ["PC", "PlayStation", "Xbox", "Switch"],
      datasets: [
        {
          data: [55, 25, 15, 5],
          backgroundColor: [
            "#6366f1", // indigo
            "#f43f5e", // red
            "#22d3ee", // cyan
            "#f59e42", // orange
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

  // Toggle active button for Day/Week/Month (UI only)
  function setActive(btnId) {
    ["dayBtn", "weekBtn", "monthBtn"].forEach((id) => {
      document.getElementById(id).classList.remove("bg-gray-300", "font-bold");
    });
    document.getElementById(btnId).classList.add("bg-gray-300", "font-bold");
  }

  setActive("dayBtn");
  document.getElementById("dayBtn").onclick = () => setActive("dayBtn");
  document.getElementById("weekBtn").onclick = () => setActive("weekBtn");
  document.getElementById("monthBtn").onclick = () => setActive("monthBtn");
</script>
