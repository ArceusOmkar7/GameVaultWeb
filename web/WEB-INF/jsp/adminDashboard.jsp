<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GameVault - Admin Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
    />
  </head>
  <body class="bg-gray-100">
    <!-- Sidebar -->
    <div id="sidebar" class="fixed top-0 left-0 h-full w-20 md:w-64 bg-gray-900 text-gray-200 z-50 transition-all duration-200 ease-in-out flex flex-col sidebar-collapsed shadow-lg mt-16">
      <div class="flex items-center justify-between px-6 py-4 border-b border-gray-800">
        <div class="flex items-center gap-2">
          <i class="bi bi-bootstrap-fill text-2xl text-gray-200"></i>
          <span class="sidebar-label text-2xl font-bold text-gray-200">GameVault</span>
        </div>
        <!-- Sidebar Collapse/Expand Button (Desktop only) -->
        <button id="toggleSidebar" class="hidden md:inline text-2xl focus:outline-none text-gray-200 ml-2" title="Collapse/Expand Sidebar">
          <i class="bi bi-chevron-left" id="toggleSidebarIcon"></i>
        </button>
        <button id="closeSidebar" class="md:hidden text-2xl focus:outline-none text-gray-200"><i class="bi bi-x"></i></button>
      </div>
      <nav class="flex-1 px-4 py-6">
        <ul class="space-y-2">
          <li><a href="#" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200"><i class="bi bi-speedometer2 text-gray-200"></i> <span class="sidebar-label text-gray-200">Dashboard</span></a></li>
          <li><a href="#" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200"><i class="bi bi-people text-gray-200"></i> <span class="sidebar-label text-gray-200">Users</span></a></li>
          <li><a href="#" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200"><i class="bi bi-controller text-gray-200"></i> <span class="sidebar-label text-gray-200">Games</span></a></li>
          <li><a href="#" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200"><i class="bi bi-bag-check text-gray-200"></i> <span class="sidebar-label text-gray-200">Orders</span></a></li>
          <li><form action="logout" method="post"><button type="submit" class="w-full flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200"><i class="bi bi-box-arrow-right text-gray-200"></i> <span class="sidebar-label text-gray-200">Logout</span></button></form></li>
        </ul>
      </nav>
    </div>
    <!-- Hamburger Button -->
    <button id="openSidebar" class="fixed top-4 left-4 z-60 bg-blue-700 text-white p-2 rounded focus:outline-none"><i class="bi bi-list text-2xl"></i></button>
    <!-- Top Navbar (moved right for sidebar) -->
    <nav class="w-full flex items-center justify-between bg-gray-900 text-white px-8 py-4 fixed top-0 left-0 md:left-20 z-40 shadow transition-all duration-200" style="left:0;" id="topNavbar">
      <div class="flex items-center gap-2">
        <i class="bi bi-bootstrap-fill text-2xl"></i>
        <span class="text-2xl font-bold">GameVault</span>
      </div>
      <form action="logout" method="post" class="hidden md:block">
        <button type="submit" class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded font-semibold flex items-center gap-2">
          <i class="bi bi-box-arrow-right"></i> Logout
        </button>
      </form>
    </nav>
    <div class="pt-24 md:pl-64 container mx-auto px-4 py-6 transition-all duration-200" id="mainContent">
      <!-- Page Title for clarity -->
      <h1 class="text-3xl font-bold mb-6 text-gray-800">Admin Dashboard</h1>
      <!-- Summary Cards -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <div class="bg-white rounded-lg shadow flex flex-col items-center justify-center p-6">
          <i class="bi bi-controller text-4xl text-blue-600 mb-2"></i>
          <div class="text-3xl font-bold">120</div>
          <div class="text-green-600 text-sm flex items-center"><span class="mr-1">↑ 4.2%</span></div>
          <div class="text-gray-500 mt-1">Total Games</div>
        </div>
        <div class="bg-white rounded-lg shadow flex flex-col items-center justify-center p-6">
          <i class="bi bi-person-lines-fill text-4xl text-green-600 mb-2"></i>
          <div class="text-3xl font-bold">2,340</div>
          <div class="text-green-600 text-sm flex items-center"><span class="mr-1">↑ 2.1%</span></div>
          <div class="text-gray-500 mt-1">Total Users</div>
        </div>
        <div class="bg-white rounded-lg shadow flex flex-col items-center justify-center p-6">
          <i class="bi bi-bag-check text-4xl text-blue-700 mb-2"></i>
          <div class="text-3xl font-bold">410</div>
          <div class="text-green-600 text-sm flex items-center"><span class="mr-1">↑ 6.7%</span></div>
          <div class="text-gray-500 mt-1">Total Orders</div>
        </div>
        <div class="bg-white rounded-lg shadow flex flex-col items-center justify-center p-6">
          <i class="bi bi-currency-dollar text-4xl text-yellow-500 mb-2"></i>
          <div class="text-3xl font-bold">$18,900</div>
          <div class="text-red-600 text-sm flex items-center"><span class="mr-1">↓ 1.3%</span></div>
          <div class="text-gray-500 mt-1">Total Revenue</div>
        </div>
      </div>
      <!-- Charts Row: All three charts side by side on desktop, stacked on mobile -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <!-- Sales Overview Chart -->
        <div class="bg-white rounded-lg shadow p-6 w-full flex flex-col">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-semibold">Sales Overview</h2>
            <div class="flex space-x-2">
              <button class="bg-gray-200 px-3 py-1 rounded text-sm font-medium" id="dayBtn">Day</button>
              <button class="bg-gray-200 px-3 py-1 rounded text-sm font-medium" id="weekBtn">Week</button>
              <button class="bg-gray-200 px-3 py-1 rounded text-sm font-medium" id="monthBtn">Month</button>
            </div>
          </div>
          <canvas id="salesChart" height="300" style="max-width:600px;margin:auto;"></canvas>
        </div>
        <!-- Top Selling Games Bar Chart -->
        <div class="bg-white rounded-lg shadow p-6 w-full flex flex-col">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-semibold">Top Selling Games</h2>
          </div>
          <canvas id="topGamesChart" height="300" style="max-width:600px;margin:auto;"></canvas>
        </div>
        <!-- User Growth Area Chart -->
        <div class="bg-white rounded-lg shadow p-6 w-full flex flex-col">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-semibold">User Growth</h2>
          </div>
          <canvas id="userGrowthChart" height="300" style="max-width:600px;margin:auto;"></canvas>
        </div>
      </div>
      <!-- Additional Charts Row -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <!-- Revenue Breakdown Pie Chart -->
        <div class="bg-white rounded-lg shadow p-6 w-full flex flex-col">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-semibold">Revenue Breakdown</h2>
          </div>
          <canvas id="revenuePieChart" height="300" style="max-width:600px;margin:auto;"></canvas>
        </div>
        <!-- Platform Distribution Doughnut Chart -->
        <div class="bg-white rounded-lg shadow p-6 w-full flex flex-col">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-semibold">Platform Distribution</h2>
          </div>
          <canvas id="platformDoughnutChart" height="300" style="max-width:600px;margin:auto;"></canvas>
        </div>
      </div>
    </div>
    <jsp:include page="footer.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
      // Sidebar toggle logic (improved)
      const sidebar = document.getElementById('sidebar');
      const openSidebar = document.getElementById('openSidebar');
      const closeSidebar = document.getElementById('closeSidebar');
      const toggleSidebar = document.getElementById('toggleSidebar');
      const toggleSidebarIcon = document.getElementById('toggleSidebarIcon');
      const sidebarLabels = document.querySelectorAll('.sidebar-label');
      let isSidebarOpen = false;
      function setSidebarState(expanded) {
        if (expanded) {
          sidebar.classList.remove('sidebar-collapsed');
          sidebar.classList.add('sidebar-expanded');
          sidebar.style.width = '16rem'; // 64
          sidebarLabels.forEach(l => l.classList.remove('hidden'));
          sidebar.style.boxShadow = '2px 0 10px rgba(0,0,0,0.2)';
          if (toggleSidebarIcon) toggleSidebarIcon.classList.replace('bi-chevron-right', 'bi-chevron-left');
          isSidebarOpen = true;
        } else {
          sidebar.classList.remove('sidebar-expanded');
          sidebar.classList.add('sidebar-collapsed');
          sidebar.style.width = '5rem'; // 20
          sidebarLabels.forEach(l => l.classList.add('hidden'));
          sidebar.style.boxShadow = 'none';
          if (toggleSidebarIcon) toggleSidebarIcon.classList.replace('bi-chevron-left', 'bi-chevron-right');
          isSidebarOpen = false;
        }
      }
      openSidebar.addEventListener('click', () => {
        setSidebarState(true);
        openSidebar.style.display = 'none';
      });
      closeSidebar.addEventListener('click', () => {
        setSidebarState(false);
        openSidebar.style.display = 'block';
      });
      // Toggle sidebar on desktop
      if (toggleSidebar) {
        toggleSidebar.addEventListener('click', () => {
          setSidebarState(!isSidebarOpen);
        });
      }
      function handleResize() {
        if(window.innerWidth >= 768) {
          setSidebarState(true);
          openSidebar.style.display = 'none';
        } else {
          setSidebarState(false);
          openSidebar.style.display = 'block';
        }
      }
      window.addEventListener('resize', handleResize);
      document.addEventListener('DOMContentLoaded', handleResize);
      // Sales Overview Chart
      const salesCtx = document.getElementById('salesChart').getContext('2d');
      const salesChart = new Chart(salesCtx, {
        type: 'line',
        data: {
          labels: ['2025-04-28', '2025-04-29', '2025-04-30', '2025-05-01', '2025-05-02', '2025-05-03', '2025-05-04'],
          datasets: [{
            label: 'Sales',
            data: [10, 25, 40, 60, 85, 115, 70],
            borderColor: '#3b82f6',
            backgroundColor: 'rgba(59, 130, 246, 0.1)',
            fill: true,
            tension: 0.4,
            pointRadius: 4,
            pointBackgroundColor: '#3b82f6',
          }]
        },
        options: {
          responsive: true,
          plugins: { legend: { display: false } },
          scales: {
            y: { beginAtZero: true, max: 120 },
            x: { grid: { display: false } }
          }
        }
      });
      // Top Selling Games Bar Chart
      const topGamesCtx = document.getElementById('topGamesChart').getContext('2d');
      const topGamesChart = new Chart(topGamesCtx, {
        type: 'bar',
        data: {
          labels: ['Game A', 'Game B', 'Game C', 'Game D', 'Game E'],
          datasets: [{
            label: 'Units Sold',
            data: [120, 95, 80, 60, 45],
            backgroundColor: [
              '#6366f1', // indigo
              '#22d3ee', // cyan
              '#f59e42', // orange
              '#84cc16', // lime
              '#f43f5e'  // red
            ],
            borderRadius: 8,
            maxBarThickness: 32
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: { display: false },
            title: { display: false }
          },
          scales: {
            x: { grid: { display: false } },
            y: { beginAtZero: true }
          }
        }
      });
      // User Growth Area Chart
      const userGrowthCtx = document.getElementById('userGrowthChart').getContext('2d');
      const userGrowthChart = new Chart(userGrowthCtx, {
        type: 'line',
        data: {
          labels: ['2025-01', '2025-02', '2025-03', '2025-04', '2025-05'],
          datasets: [{
            label: 'Users',
            data: [1200, 1450, 1700, 2100, 2340],
            borderColor: '#10b981',
            backgroundColor: 'rgba(16, 185, 129, 0.15)',
            fill: true,
            tension: 0.4,
            pointRadius: 4,
            pointBackgroundColor: '#10b981',
          }]
        },
        options: {
          responsive: true,
          plugins: { legend: { display: false } },
          scales: {
            y: { beginAtZero: true },
            x: { grid: { display: false } }
          }
        }
      });
      // Revenue Breakdown Pie Chart
      const revenuePieCtx = document.getElementById('revenuePieChart').getContext('2d');
      const revenuePieChart = new Chart(revenuePieCtx, {
        type: 'pie',
        data: {
          labels: ['Games', 'DLCs', 'Subscriptions', 'Other'],
          datasets: [{
            data: [12000, 3500, 2500, 900],
            backgroundColor: [
              '#3b82f6', // blue
              '#f59e42', // orange
              '#10b981', // green
              '#6366f1'  // indigo
            ],
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: { position: 'bottom' }
          }
        }
      });
      // Platform Distribution Doughnut Chart
      const platformDoughnutCtx = document.getElementById('platformDoughnutChart').getContext('2d');
      const platformDoughnutChart = new Chart(platformDoughnutCtx, {
        type: 'doughnut',
        data: {
          labels: ['PC', 'PlayStation', 'Xbox', 'Switch'],
          datasets: [{
            data: [55, 25, 15, 5],
            backgroundColor: [
              '#6366f1', // indigo
              '#f43f5e', // red
              '#22d3ee', // cyan
              '#f59e42'  // orange
            ],
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: { position: 'bottom' }
          }
        }
      });
      // Toggle active button for Day/Week/Month (UI only)
      function setActive(btnId) {
        ['dayBtn','weekBtn','monthBtn'].forEach(id => {
          document.getElementById(id).classList.remove('bg-gray-300','font-bold');
        });
        document.getElementById(btnId).classList.add('bg-gray-300','font-bold');
      }
      setActive('dayBtn');
      document.getElementById('dayBtn').onclick = () => setActive('dayBtn');
      document.getElementById('weekBtn').onclick = () => setActive('weekBtn');
      document.getElementById('monthBtn').onclick = () => setActive('monthBtn');
    </script>
  </body>
</html>
