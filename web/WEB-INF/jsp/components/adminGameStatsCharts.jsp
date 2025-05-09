<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Game Statistics Section -->
<div class="mb-8">
  <h2 class="text-xl font-semibold mb-4">Game Statistics</h2>
  <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
    <!-- Rating Distribution -->
    <div class="bg-white rounded-lg shadow p-6">
      <h3 class="text-lg font-medium mb-4">Rating Distribution</h3>
      <canvas id="ratingDistributionChart" height="250"></canvas>
    </div>

    <!-- Price Ranges -->
    <div class="bg-white rounded-lg shadow p-6">
      <h3 class="text-lg font-medium mb-4">Price Range Distribution</h3>
      <canvas id="priceRangesChart" height="250"></canvas>
    </div>

    <!-- Genre Popularity -->
    <div class="bg-white rounded-lg shadow p-6">
      <h3 class="text-lg font-medium mb-4">Top Genres</h3>
      <canvas id="genrePopularityChart" height="250"></canvas>
    </div>
  </div>
</div>
