<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Admin Panel</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" />
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body class="bg-gray-100">
  <div class="flex min-h-screen">
    <!-- Sidebar -->
    <aside class="w-64 bg-gray-900 text-gray-100 flex flex-col">
      <div class="h-16 flex items-center justify-center text-2xl font-bold border-b border-gray-800">Admin Panel</div>
      <nav class="flex-1 py-4">
        <a href="#" class="flex items-center px-6 py-3 hover:bg-gray-800"><i class="bi bi-speedometer2 mr-3"></i>Dashboard</a>
        <a href="#" class="flex items-center px-6 py-3 hover:bg-gray-800"><i class="bi bi-people mr-3"></i>Customers</a>
        <a href="#" class="flex items-center px-6 py-3 hover:bg-gray-800"><i class="bi bi-truck mr-3"></i>Suppliers</a>
        <a href="#" class="flex items-center px-6 py-3 hover:bg-gray-800"><i class="bi bi-tags mr-3"></i>Categories</a>
        <a href="#" class="flex items-center px-6 py-3 hover:bg-gray-800"><i class="bi bi-box-seam mr-3"></i>Products</a>
        <a href="#" class="flex items-center px-6 py-3 hover:bg-gray-800"><i class="bi bi-bag-check mr-3"></i>Orders</a>
        <a href="#" class="flex items-center px-6 py-3 hover:bg-gray-800"><i class="bi bi-bar-chart-line mr-3"></i>Reports</a>
        <a href="#" class="flex items-center px-6 py-3 hover:bg-gray-800"><i class="bi bi-exclamation-triangle mr-3"></i>Complaints</a>
        <a href="#" class="flex items-center px-6 py-3 hover:bg-gray-800"><i class="bi bi-question-circle mr-3"></i>FAQs</a>
      </nav>
      <div class="p-4 border-t border-gray-800 flex items-center">
        <i class="bi bi-person-circle text-2xl mr-2"></i>
        <span>Admin</span>
      </div>
    </aside>
    <!-- Main Content -->
    <div class="flex-1 flex flex-col">
      <!-- Top Navbar -->
      <header class="h-16 bg-blue-900 flex items-center justify-between px-8 text-white shadow">
        <h1 class="text-xl font-bold">Admin Panel</h1>
        <button class="bg-white text-blue-900 px-4 py-2 rounded font-semibold hover:bg-gray-200">Logout</button>
      </header>
      <!-- Dashboard Content -->
      <main class="flex-1 p-8 bg-gray-100">
        <!-- Cards -->
        <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <div class="bg-white rounded shadow p-6 flex flex-col items-center">
            <i class="bi bi-currency-dollar text-3xl text-blue-600 mb-2"></i>
            <div class="text-2xl font-bold">$24,589</div>
            <div class="text-green-600 text-sm">↑ 12.5%</div>
            <div class="text-gray-500 text-sm">Total Sales</div>
          </div>
          <div class="bg-white rounded shadow p-6 flex flex-col items-center">
            <i class="bi bi-lock text-3xl text-green-600 mb-2"></i>
            <div class="text-2xl font-bold">10</div>
            <div class="text-green-600 text-sm">↑ 8.2%</div>
            <div class="text-gray-500 text-sm">Total Orders</div>
          </div>
          <div class="bg-white rounded shadow p-6 flex flex-col items-center">
            <i class="bi bi-people text-3xl text-blue-500 mb-2"></i>
            <div class="text-2xl font-bold">89</div>
            <div class="text-green-600 text-sm">↑ 5.7%</div>
            <div class="text-gray-500 text-sm">Total Customers</div>
          </div>
          <div class="bg-white rounded shadow p-6 flex flex-col items-center">
            <i class="bi bi-box-seam text-3xl text-yellow-500 mb-2"></i>
            <div class="text-2xl font-bold">38</div>
            <div class="text-red-600 text-sm">↓ 2.3%</div>
            <div class="text-gray-500 text-sm">Total Products</div>
          </div>
        </div>
        <!-- Graphs and Charts -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div class="bg-white rounded shadow p-6 md:col-span-2">
            <div class="flex justify-between items-center mb-4">
              <div class="font-semibold text-lg">Sales Overview</div>
              <div>
                <button class="text-xs px-2 py-1 bg-gray-200 rounded">Day</button>
                <button class="text-xs px-2 py-1 bg-gray-200 rounded">Week</button>
                <button class="text-xs px-2 py-1 bg-gray-200 rounded">Month</button>
              </div>
            </div>
            <canvas id="salesChart" height="100"></canvas>
          </div>
          <div class="bg-white rounded shadow p-6">
            <div class="font-semibold text-lg mb-4">Top Categories</div>
            <canvas id="categoryChart" height="180"></canvas>
          </div>
        </div>
        <!-- Recent Orders and Activity -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div class="md:col-span-2 bg-white rounded shadow p-6">
            <div class="flex justify-between items-center mb-4">
              <div class="font-semibold text-lg">Recent Orders</div>
              <button class="text-xs px-2 py-1 bg-gray-200 rounded">View All</button>
            </div>
            <div class="overflow-x-auto">
              <table class="min-w-full text-sm">
                <thead>
                  <tr class="bg-gray-100">
                    <th class="py-2 px-4 text-left">Order ID</th>
                    <th class="py-2 px-4 text-left">Customer</th>
                    <th class="py-2 px-4 text-left">Date</th>
                    <th class="py-2 px-4 text-left">Amount</th>
                    <th class="py-2 px-4 text-left">Status</th>
                  </tr>
                </thead>
                <tbody>
                  <!-- 20 Dummy Orders -->
                  <tr>
                    <td class="py-2 px-4">#ORD-110</td>
                    <td class="py-2 px-4">Chirayu Vaghela</td>
                    <td class="py-2 px-4">2025-05-04 16:36:11</td>
                    <td class="py-2 px-4">$599.99</td>
                    <td class="py-2 px-4"><span class="bg-yellow-200 text-yellow-800 px-2 py-1 rounded">Pending</span></td>
                  </tr>
                  <tr>
                    <td class="py-2 px-4">#ORD-109</td>
                    <td class="py-2 px-4">Chirayu Vaghela</td>
                    <td class="py-2 px-4">2025-05-03 18:59:48</td>
                    <td class="py-2 px-4">$2349.96</td>
                    <td class="py-2 px-4"><span class="bg-yellow-200 text-yellow-800 px-2 py-1 rounded">Pending</span></td>
                  </tr>
                  <tr>
                    <td class="py-2 px-4">#ORD-108</td>
                    <td class="py-2 px-4">Vikram Rao</td>
                    <td class="py-2 px-4">2025-03-27 00:00:00</td>
                    <td class="py-2 px-4">$159.98</td>
                    <td class="py-2 px-4"><span class="bg-green-200 text-green-800 px-2 py-1 rounded">Delivered</span></td>
                  </tr>
                  <tr>
                    <td class="py-2 px-4">#ORD-107</td>
                    <td class="py-2 px-4">Neha Gupta</td>
                    <td class="py-2 px-4">2025-03-26 00:00:00</td>
                    <td class="py-2 px-4">$129.99</td>
                    <td class="py-2 px-4"><span class="bg-green-200 text-green-800 px-2 py-1 rounded">Delivered</span></td>
                  </tr>
                  <!-- Add 16 more dummy orders -->
                  <c:forEach var="i" begin="1" end="16">
                    <tr>
                      <td class="py-2 px-4">#ORD-10${i}</td>
                      <td class="py-2 px-4">Dummy User ${i}</td>
                      <td class="py-2 px-4">2025-03-${i+10} 12:00:00</td>
                      <td class="py-2 px-4">$${i*100}.00</td>
                      <td class="py-2 px-4">
                        <span class="${i%2==0 ? 'bg-green-200 text-green-800' : 'bg-yellow-200 text-yellow-800'} px-2 py-1 rounded">
                          ${i%2==0 ? 'Delivered' : 'Pending'}
                        </span>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
          <div class="bg-white rounded shadow p-6">
            <div class="font-semibold text-lg mb-4">Recent Activity</div>
            <ul class="space-y-3 text-sm">
              <li><i class="bi bi-bag-check text-blue-600 mr-2"></i>Order #ORD-110 - $599.99</li>
              <li><i class="bi bi-box-arrow-up text-green-600 mr-2"></i>Product updated: NZXT H510 Elite (Stock: 30)</li>
              <li><i class="bi bi-person-plus text-blue-600 mr-2"></i>New customer registered: Karan Mehta (karan@gmail.com)</li>
              <li><i class="bi bi-bag-check text-blue-600 mr-2"></i>Order #ORD-109 - $2349.96</li>
              <li><i class="bi bi-box-arrow-up text-green-600 mr-2"></i>Product updated: RTX 4090 (Stock: 12)</li>
              <li><i class="bi bi-person-plus text-blue-600 mr-2"></i>New customer registered: Priya Shah (priya@gmail.com)</li>
              <li><i class="bi bi-bag-check text-blue-600 mr-2"></i>Order #ORD-108 - $159.98</li>
              <li><i class="bi bi-bag-check text-blue-600 mr-2"></i>Order #ORD-107 - $129.99</li>
              <li><i class="bi bi-box-arrow-up text-green-600 mr-2"></i>Product updated: Ryzen 9 5900X (Stock: 8)</li>
              <li><i class="bi bi-person-plus text-blue-600 mr-2"></i>New customer registered: Dummy User 1 (dummy1@gmail.com)</li>
              <!-- Add more dummy activities as needed -->
            </ul>
          </div>
        </div>
      </main>
    </div>
  </div>
  <script>
    // Sales Overview Chart
    const salesChart = new Chart(document.getElementById('salesChart').getContext('2d'), {
      type: 'line',
      data: {
        labels: [
          '2025-04-28', '2025-04-29', '2025-04-30', '2025-05-01', '2025-05-02', '2025-05-03', '2025-05-04'
        ],
        datasets: [{
          label: 'Sales',
          data: [0, 200, 400, 800, 1800, 2200, 1200],
          borderColor: '#2563eb',
          backgroundColor: 'rgba(37,99,235,0.1)',
          fill: true,
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false }
        },
        scales: {
          y: { beginAtZero: true }
        }
      }
    });
    // Top Categories Pie Chart
    const categoryChart = new Chart(document.getElementById('categoryChart').getContext('2d'), {
      type: 'doughnut',
      data: {
        labels: ['CPU', 'GPU', 'RAM', 'SSD', 'Motherboard'],
        datasets: [{
          data: [8, 6, 3, 2, 1],
          backgroundColor: ['#2563eb', '#22c55e', '#f59e42', '#f43f5e', '#eab308']
        }]
      },
      options: {
        plugins: {
          legend: { position: 'bottom' }
        }
      }
    });
  </script>
</body>
</html>
