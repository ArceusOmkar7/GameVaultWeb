<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Charts Section -->
<!-- Sales Overview Chart - Full Width -->
<div class="mb-8">
  <div class="bg-white rounded-lg shadow p-6 w-full flex flex-col">
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-semibold">Sales Overview</h2>
      <div class="flex space-x-2">
        <button
          class="bg-gray-200 px-3 py-1 rounded text-sm font-medium"
          id="dayBtn"
        >
          Day
        </button>
        <button
          class="bg-gray-200 px-3 py-1 rounded text-sm font-medium"
          id="weekBtn"
        >
          Week
        </button>
        <button
          class="bg-gray-200 px-3 py-1 rounded text-sm font-medium"
          id="monthBtn"
        >
          Month
        </button>
      </div>
    </div>
    <div class="w-full">
      <canvas
        id="salesChart"
        height="300"
        width="1000"
        style="width: 100%; height: auto"
      ></canvas>
    </div>
  </div>
</div>

<!-- Top Selling Games and User Growth Charts - Two per row -->
<div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
  <!-- Top Selling Games Bar Chart -->
  <div class="bg-white rounded-lg shadow p-6 w-full flex flex-col">
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-semibold">Top Selling Games</h2>
    </div>
    <canvas
      id="topGamesChart"
      height="300"
      style="max-width: 100%; margin: auto"
    ></canvas>
  </div>
  <!-- User Growth Area Chart -->
  <div class="bg-white rounded-lg shadow p-6 w-full flex flex-col">
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-semibold">User Growth</h2>
    </div>
    <canvas
      id="userGrowthChart"
      height="300"
      style="max-width: 100%; margin: auto"
    ></canvas>
  </div>
</div>

<!-- Revenue Breakdown and Platform Distribution Charts - Two per row -->
<div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
  <!-- Revenue Breakdown Pie Chart -->
  <div class="bg-white rounded-lg shadow p-6 w-full flex flex-col">
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-semibold">Revenue Breakdown</h2>
    </div>
    <canvas
      id="revenuePieChart"
      height="300"
      style="max-width: 100%; margin: auto"
    ></canvas>
  </div>
  <!-- Platform Distribution Doughnut Chart -->
  <div class="bg-white rounded-lg shadow p-6 w-full flex flex-col">
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-semibold">Platform Distribution</h2>
    </div>
    <canvas
      id="platformDoughnutChart"
      height="300"
      style="max-width: 100%; margin: auto"
    ></canvas>
  </div>
</div>
