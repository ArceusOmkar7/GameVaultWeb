<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header
  class="bg-gradient-to-r from-blue-700 to-blue-900 text-white shadow-md w-full"
>
  <div class="container mx-auto px-4 py-3">
    <div class="flex flex-wrap items-center justify-between">
      <!-- Logo -->
      <div class="flex items-center">
        <a
          href="${pageContext.request.contextPath}/home"
          class="flex items-center"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-8 w-8 mr-2"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              d="M11 17a1 1 0 001.447.894l4-2A1 1 0 0017 15V9.236a1 1 0 00-1.447-.894l-4 2a1 1 0 00-.553.894V17zM15.211 6.276a1 1 0 000-1.788l-4.764-2.382a1 1 0 00-.894 0L4.789 4.488a1 1 0 000 1.788l4.764 2.382a1 1 0 00.894 0l4.764-2.382zM4.447 8.342A1 1 0 003 9.236V15a1 1 0 00.553.894l4 2A1 1 0 009 17v-5.764a1 1 0 00-.553-.894l-4-2z"
            />
          </svg>
          <span class="text-xl font-bold">GameVault</span>
        </a>
      </div>

      <!-- Navigation Menu -->
      <nav class="hidden md:flex space-x-6 text-white font-medium">
        <a
          href="${pageContext.request.contextPath}/home"
          class="hover:text-blue-200 transition"
          >Home</a
        >
        <a
          href="${pageContext.request.contextPath}/browse"
          class="hover:text-blue-200 transition"
          >Browse Games</a
        >
        <c:if test="${not empty sessionScope.loggedInUser}">
          <a
            href="${pageContext.request.contextPath}/orders"
            class="hover:text-blue-200 transition"
            >My Orders</a
          >
        </c:if>
        <c:if test="${sessionScope.loggedInUser.isAdmin}">
          <a
            href="${pageContext.request.contextPath}/admin/dashboard"
            class="hover:text-blue-200 transition"
            >Admin Panel</a
          >
        </c:if>
      </nav>

      <!-- User Actions -->
      <div class="flex items-center space-x-4">
        <!-- Cart Button -->
        <a
          href="${pageContext.request.contextPath}/viewCart"
          class="relative hover:text-blue-200 transition"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"
            />
          </svg>
          <c:if
            test="${not empty sessionScope.cart && sessionScope.cart.gameCount > 0}"
          >
            <span
              class="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center"
            >
              ${sessionScope.cart.gameCount}
            </span>
          </c:if>
        </a>

        <!-- User Menu -->
        <c:choose>
          <c:when test="${empty sessionScope.loggedInUser}">
            <div class="flex space-x-2">
              <a
                href="${pageContext.request.contextPath}/login"
                class="text-white hover:text-blue-200 transition"
                >Login</a
              >
              <span class="text-white">|</span>
              <a
                href="${pageContext.request.contextPath}/register"
                class="text-white hover:text-blue-200 transition"
                >Register</a
              >
            </div>
          </c:when>
          <c:otherwise>
            <div class="relative group">
              <button
                class="flex items-center space-x-1 hover:text-blue-200 transition"
              >
                <span>${sessionScope.loggedInUser.username}</span>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-4 w-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M19 9l-7 7-7-7"
                  />
                </svg>
              </button>
              <div
                class="absolute right-0 w-48 bg-white rounded-md shadow-lg py-1 mt-2 z-10 hidden group-hover:block"
              >
                <a
                  href="${pageContext.request.contextPath}/profile"
                  class="block px-4 py-2 text-gray-800 hover:bg-blue-100 transition"
                  >My Profile</a
                >
                <a
                  href="${pageContext.request.contextPath}/orders"
                  class="block px-4 py-2 text-gray-800 hover:bg-blue-100 transition"
                  >Order History</a
                >
                <a
                  href="${pageContext.request.contextPath}/logout"
                  class="block px-4 py-2 text-gray-800 hover:bg-blue-100 transition"
                  >Logout</a
                >
              </div>
            </div>
          </c:otherwise>
        </c:choose>
      </div>

      <!-- Mobile Menu Button -->
      <div class="md:hidden">
        <button id="mobile-menu-button" class="text-white focus:outline-none">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M4 6h16M4 12h16M4 18h16"
            />
          </svg>
        </button>
      </div>
    </div>

    <!-- Mobile Navigation Menu (Hidden by default) -->
    <div
      id="mobile-menu"
      class="md:hidden hidden mt-4 pt-4 border-t border-blue-500"
    >
      <nav class="flex flex-col space-y-3 text-white">
        <a
          href="${pageContext.request.contextPath}/home"
          class="hover:text-blue-200 transition"
          >Home</a
        >
        <a
          href="${pageContext.request.contextPath}/browse"
          class="hover:text-blue-200 transition"
          >Browse Games</a
        >
        <c:if test="${not empty sessionScope.loggedInUser}">
          <a
            href="${pageContext.request.contextPath}/orders"
            class="hover:text-blue-200 transition"
            >My Orders</a
          >
          <a
            href="${pageContext.request.contextPath}/profile"
            class="hover:text-blue-200 transition"
            >My Profile</a
          >
          <c:if test="${sessionScope.loggedInUser.isAdmin}">
            <a
              href="${pageContext.request.contextPath}/admin/dashboard"
              class="hover:text-blue-200 transition"
              >Admin Panel</a
            >
          </c:if>
          <a
            href="${pageContext.request.contextPath}/logout"
            class="hover:text-blue-200 transition"
            >Logout</a
          >
        </c:if>
        <c:if test="${empty sessionScope.loggedInUser}">
          <a
            href="${pageContext.request.contextPath}/login"
            class="hover:text-blue-200 transition"
            >Login</a
          >
          <a
            href="${pageContext.request.contextPath}/register"
            class="hover:text-blue-200 transition"
            >Register</a
          >
        </c:if>
      </nav>
    </div>
  </div>
</header>

<!-- Mobile Menu Toggle Script -->
<script>
  document.addEventListener("DOMContentLoaded", function () {
    const mobileMenuButton = document.getElementById("mobile-menu-button");
    const mobileMenu = document.getElementById("mobile-menu");

    mobileMenuButton.addEventListener("click", function () {
      mobileMenu.classList.toggle("hidden");
    });
  });
</script>
