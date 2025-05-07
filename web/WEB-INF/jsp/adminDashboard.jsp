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
  <body class="bg-gray-100" style="margin-top: 64px;">
    <jsp:include page="header.jsp" />
    <!-- Left Navbar -->
    <div class="flex">
      <!-- Hamburger for mobile -->
      <button id="hamburgerBtn" class="md:hidden fixed top-4 left-4 z-50 bg-white p-2 rounded shadow-lg focus:outline-none">
        <i class="bi bi-list text-2xl"></i>
      </button>
      <!-- Sidebar -->
      <nav id="sidebar" class="flex flex-col justify-between min-h-screen bg-gray-900 text-gray-100 w-16 md:w-64 transition-all duration-300 overflow-hidden fixed top-0 left-0 z-40">
        <!-- Top: Logo and Title -->
        <div>
          <div class="flex items-center gap-2 px-4 py-6 border-b border-gray-800">
            <i class="bi bi-bootstrap-fill text-3xl text-white"></i>
            <span class="text-xl font-bold hidden md:inline">Sidebar</span>
          </div>
          <ul class="mt-4 flex-1 flex flex-col gap-1">
            <li>
              <a href="#" class="flex items-center gap-3 px-4 py-3 rounded-lg transition font-medium bg-blue-700 text-white">
                <i class="bi bi-house-door-fill text-lg"></i>
                <span class="hidden md:inline">Home</span>
              </a>
            </li>
            <li>
              <a href="#" class="flex items-center gap-3 px-4 py-3 rounded-lg transition hover:bg-gray-800 hover:text-white">
                <i class="bi bi-speedometer2 text-lg"></i>
                <span class="hidden md:inline">Dashboard</span>
              </a>
            </li>
            <li>
              <a href="#" class="flex items-center gap-3 px-4 py-3 rounded-lg transition hover:bg-gray-800 hover:text-white">
                <i class="bi bi-table text-lg"></i>
                <span class="hidden md:inline">Orders</span>
              </a>
            </li>
            <li>
              <a href="#" class="flex items-center gap-3 px-4 py-3 rounded-lg transition hover:bg-gray-800 hover:text-white">
                <i class="bi bi-grid text-lg"></i>
                <span class="hidden md:inline">Products</span>
              </a>
            </li>
            <li>
              <a href="#" class="flex items-center gap-3 px-4 py-3 rounded-lg transition hover:bg-gray-800 hover:text-white">
                <i class="bi bi-person text-lg"></i>
                <span class="hidden md:inline">Customers</span>
              </a>
            </li>
          </ul>
        </div>
        <!-- Bottom: User Profile -->
        <div class="px-4 py-4 border-t border-gray-800 flex items-center gap-3">
          <img src="https://randomuser.me/api/portraits/men/1.jpg" alt="Profile" class="w-10 h-10 rounded-full border-2 border-blue-700" />
          <div class="hidden md:block">
            <div class="font-semibold">mdo</div>
            <div class="text-xs text-gray-400">Admin</div>
          </div>
          <i class="bi bi-caret-down-fill ml-auto hidden md:block"></i>
        </div>
      </nav>
      <!-- Main Content Wrapper (add left margin for sidebar) -->
      <div class="md:ml-64 ml-16 flex-1">
        <div class="container mx-auto px-4 py-6">
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
          <!-- Charts Section -->
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <!-- Sales Overview -->
            <div class="bg-white rounded-lg shadow p-6 md:col-span-2">
              <div class="flex justify-between items-center mb-4">
                <h2 class="text-xl font-semibold">Sales Overview</h2>
                <div class="flex space-x-2">
                  <button class="bg-gray-200 px-3 py-1 rounded text-sm font-medium" id="dayBtn">Day</button>
                  <button class="bg-gray-200 px-3 py-1 rounded text-sm font-medium" id="weekBtn">Week</button>
                  <button class="bg-gray-200 px-3 py-1 rounded text-sm font-medium" id="monthBtn">Month</button>
                </div>
              </div>
              <canvas id="salesChart" height="120"></canvas>
            </div>
            <!-- Top Genres -->
            <div class="bg-white rounded-lg shadow p-6">
              <h2 class="text-xl font-semibold mb-4">Top Genres</h2>
              <canvas id="genresChart" height="200"></canvas>
            </div>
          </div>
        </div>
      </div>
    </div>
    <jsp:include page="footer.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
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
      // Top Genres Chart
      const genresCtx = document.getElementById('genresChart').getContext('2d');
      const genresChart = new Chart(genresCtx, {
        type: 'doughnut',
        data: {
          labels: ['Action', 'Adventure', 'RPG', 'Strategy', 'Sports'],
          datasets: [{
            data: [35, 20, 25, 10, 10],
            backgroundColor: [
              '#3b82f6',
              '#f59e42',
              '#22c55e',
              '#f43f5e',
              '#fbbf24'
            ],
            borderWidth: 2
          }]
        },
        options: {
          cutout: '70%',
          plugins: {
            legend: {
              display: true,
              position: 'bottom',
              labels: { boxWidth: 16, font: { size: 14 } }
            }
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
      // Hamburger toggle for mobile sidebar
      const hamburgerBtn = document.getElementById('hamburgerBtn');
      const sidebar = document.getElementById('sidebar');
      let sidebarOpen = false;
      hamburgerBtn.addEventListener('click', function() {
        sidebarOpen = !sidebarOpen;
        if (sidebarOpen) {
          sidebar.classList.remove('w-16');
          sidebar.classList.add('w-64');
          sidebar.querySelector('.p-6').classList.remove('hidden');
          sidebar.querySelector('.p-2').classList.add('hidden');
        } else {
          sidebar.classList.add('w-16');
          sidebar.classList.remove('w-64');
          sidebar.querySelector('.p-6').classList.add('hidden');
          sidebar.querySelector('.p-2').classList.remove('hidden');
        }
      });
      // On load, ensure sidebar is icons only on mobile
      if (window.innerWidth < 768) {
        sidebar.classList.add('w-16');
        sidebar.classList.remove('w-64');
        sidebar.querySelector('.p-6').classList.add('hidden');
        sidebar.querySelector('.p-2').classList.remove('hidden');
      }
    </script>
  </body>
</html>
