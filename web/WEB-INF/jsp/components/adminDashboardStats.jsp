<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Summary Stats Cards -->
<div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
  <div
    class="bg-white rounded-lg shadow flex flex-col items-center justify-center p-6"
  >
    <i class="bi bi-controller text-4xl text-blue-600 mb-2"></i>
    <div class="text-3xl font-bold">
      ${gamesCount != null ? gamesCount : '120'}
    </div>
    <c:choose>
      <c:when test="${growthTrends != null && growthTrends.gamesGrowth >= 0}">
        <div class="text-green-600 text-sm flex items-center">
          <span class="mr-1"
            >↑
            <fmt:formatNumber
              value="${growthTrends.gamesGrowth}"
              pattern="#,##0.0"
            />%</span
          >
        </div>
      </c:when>
      <c:when test="${growthTrends != null && growthTrends.gamesGrowth < 0}">
        <div class="text-red-600 text-sm flex items-center">
          <span class="mr-1"
            >↓
            <fmt:formatNumber
              value="${growthTrends.gamesGrowth * -1}"
              pattern="#,##0.0"
            />%</span
          >
        </div>
      </c:when>
      <c:otherwise>
        <div class="text-green-600 text-sm flex items-center">
          <span class="mr-1">↑ 4.2%</span>
        </div>
      </c:otherwise>
    </c:choose>
    <div class="text-gray-500 mt-1">Total Games</div>
  </div>
  <div
    class="bg-white rounded-lg shadow flex flex-col items-center justify-center p-6"
  >
    <i class="bi bi-person-lines-fill text-4xl text-green-600 mb-2"></i>
    <div class="text-3xl font-bold">
      ${usersCount != null ? usersCount : '2,340'}
    </div>
    <c:choose>
      <c:when test="${growthTrends != null && growthTrends.usersGrowth >= 0}">
        <div class="text-green-600 text-sm flex items-center">
          <span class="mr-1"
            >↑
            <fmt:formatNumber
              value="${growthTrends.usersGrowth}"
              pattern="#,##0.0"
            />%</span
          >
        </div>
      </c:when>
      <c:when test="${growthTrends != null && growthTrends.usersGrowth < 0}">
        <div class="text-red-600 text-sm flex items-center">
          <span class="mr-1"
            >↓
            <fmt:formatNumber
              value="${growthTrends.usersGrowth * -1}"
              pattern="#,##0.0"
            />%</span
          >
        </div>
      </c:when>
      <c:otherwise>
        <div class="text-green-600 text-sm flex items-center">
          <span class="mr-1">↑ 2.1%</span>
        </div>
      </c:otherwise>
    </c:choose>
    <div class="text-gray-500 mt-1">Total Users</div>
  </div>
  <div
    class="bg-white rounded-lg shadow flex flex-col items-center justify-center p-6"
  >
    <i class="bi bi-bag-check text-4xl text-blue-700 mb-2"></i>
    <div class="text-3xl font-bold">
      ${ordersCount != null ? ordersCount : '410'}
    </div>
    <c:choose>
      <c:when test="${growthTrends != null && growthTrends.ordersGrowth >= 0}">
        <div class="text-green-600 text-sm flex items-center">
          <span class="mr-1"
            >↑
            <fmt:formatNumber
              value="${growthTrends.ordersGrowth}"
              pattern="#,##0.0"
            />%</span
          >
        </div>
      </c:when>
      <c:when test="${growthTrends != null && growthTrends.ordersGrowth < 0}">
        <div class="text-red-600 text-sm flex items-center">
          <span class="mr-1"
            >↓
            <fmt:formatNumber
              value="${growthTrends.ordersGrowth * -1}"
              pattern="#,##0.0"
            />%</span
          >
        </div>
      </c:when>
      <c:otherwise>
        <div class="text-green-600 text-sm flex items-center">
          <span class="mr-1">↑ 6.7%</span>
        </div>
      </c:otherwise>
    </c:choose>
    <div class="text-gray-500 mt-1">Total Orders</div>
  </div>
  <div
    class="bg-white rounded-lg shadow flex flex-col items-center justify-center p-6"
  >
    <i class="bi bi-currency-dollar text-4xl text-yellow-500 mb-2"></i>
    <div class="text-3xl font-bold">
      ${totalRevenue != null ? totalRevenue : '$18,900'}
    </div>
    <c:choose>
      <c:when test="${growthTrends != null && growthTrends.revenueGrowth >= 0}">
        <div class="text-green-600 text-sm flex items-center">
          <span class="mr-1"
            >↑
            <fmt:formatNumber
              value="${growthTrends.revenueGrowth}"
              pattern="#,##0.0"
            />%</span
          >
        </div>
      </c:when>
      <c:when test="${growthTrends != null && growthTrends.revenueGrowth < 0}">
        <div class="text-red-600 text-sm flex items-center">
          <span class="mr-1"
            >↓
            <fmt:formatNumber
              value="${growthTrends.revenueGrowth * -1}"
              pattern="#,##0.0"
            />%</span
          >
        </div>
      </c:when>
      <c:otherwise>
        <div class="text-red-600 text-sm flex items-center">
          <span class="mr-1">↓ 1.3%</span>
        </div>
      </c:otherwise>
    </c:choose>
    <div class="text-gray-500 mt-1">Total Revenue</div>
  </div>
</div>
