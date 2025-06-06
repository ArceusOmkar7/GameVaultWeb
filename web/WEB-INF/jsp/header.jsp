<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- Modern Header with
Navigation matching content styling --%>
<nav
  class="fixed top-0 w-full z-50 bg-opacity-90 backdrop-filter backdrop-blur-lg"
  style="
    background: linear-gradient(
      90deg,
      rgba(45, 27, 105, 0.95) 0%,
      rgba(26, 28, 46, 0.95) 100%
    );
  "
>
  <div class="gv-container px-4 sm:px-6 lg:px-8">
    <div class="flex items-center justify-between h-16">
      <!-- Logo -->
      <div class="flex-shrink-0 flex items-center">
        <a
          href="${pageContext.request.contextPath}/home"
          class="text-transparent bg-clip-text bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400 text-2xl font-bold neon-text"
        >
          GameVault
        </a>
      </div>
      <!-- Navigation Links -->
      <div class="hidden md:flex space-x-4 ml-8">
        <a
          href="${pageContext.request.contextPath}/home"
          class="text-gray-300 hover:text-white hover:border-indigo-400 px-3 py-2 text-sm font-medium border-b-2 border-transparent transition-all"
          >Home</a
        >
        <a
          href="${pageContext.request.contextPath}/browse"
          class="text-gray-300 hover:text-white hover:border-indigo-400 px-3 py-2 text-sm font-medium border-b-2 border-transparent transition-all"
          >Browse Games</a
        >
        <c:if test="${not empty sessionScope.loggedInUser}">
          <a
            href="${pageContext.request.contextPath}/viewCart"
            class="text-gray-300 hover:text-white hover:border-indigo-400 px-3 py-2 text-sm font-medium border-b-2 border-transparent transition-all"
            >Cart</a
          >
          <a
            href="${pageContext.request.contextPath}/orders"
            class="text-gray-300 hover:text-white hover:border-indigo-400 px-3 py-2 text-sm font-medium border-b-2 border-transparent transition-all"
            >My Orders</a
          >
        </c:if>
      </div>
      <!-- Profile & Wallet -->
      <div class="flex items-center space-x-4">
        <c:choose>
          <c:when test="${not empty sessionScope.loggedInUser}">
            <div class="relative" id="profileDropdownWrapper">
              <button
                id="profileDropdownBtn"
                class="flex items-center space-x-2 bg-opacity-50 bg-indigo-900 hover:bg-indigo-800 border border-purple-500/30 text-white px-4 py-2 rounded-full focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-400 transition"
              >
                <svg
                  class="h-7 w-7 text-gray-200"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="1.5"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M17.982 18.725A7.488 7.488 0 0 0 12 15.75a7.488 7.488 0 0 0-5.982 2.975m11.963 0a9 9 0 1 0-11.963 0m11.963 0A8.966 8.966 0 0 1 12 21a8.966 8.966 0 0 1-5.982-2.275M15 9.75a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
                  />
                </svg>
                <span class="text-gray-200 font-semibold text-base">
                  <fmt:formatNumber
                    value="${sessionScope.loggedInUser.walletBalance}"
                    type="currency"
                    currencySymbol="$"
                  />
                </span>
              </button>
              <!-- Dropdown -->
              <div
                id="profileDropdownMenu"
                class="absolute right-0 mt-2 w-44 bg-gray-900/95 border border-purple-500/30 backdrop-filter backdrop-blur-lg rounded-md shadow-lg py-2 z-50 hidden"
              >
                <a
                  href="${pageContext.request.contextPath}/profile"
                  class="block px-4 py-2 text-gray-300 hover:bg-indigo-900/50 hover:text-white transition-all"
                  >Profile</a
                >
                <a
                  href="${pageContext.request.contextPath}/logout"
                  class="block px-4 py-2 text-gray-300 hover:bg-indigo-900/50 hover:text-white transition-all"
                  >Logout</a
                >
              </div>
            </div>
          </c:when>
          <c:otherwise>
            <a
              href="${pageContext.request.contextPath}/login"
              class="text-gray-300 hover:text-white px-4 py-2 border-transparent hover:border-indigo-500 border rounded-md text-sm font-medium transition-all"
              >Login</a
            >
            <a
              href="${pageContext.request.contextPath}/register"
              class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-all"
              >Register</a
            >
          </c:otherwise>
        </c:choose>
      </div>
      <!-- Mobile menu button -->
      <div class="-mr-2 flex md:hidden">
        <button
          type="button"
          class="bg-indigo-900/50 inline-flex items-center justify-center p-2 rounded-md text-gray-300 hover:text-white hover:bg-indigo-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-400"
          aria-controls="mobile-menu"
          aria-expanded="false"
        >
          <span class="sr-only">Open main menu</span>
          <svg
            class="block h-6 w-6"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="1.5"
            stroke="currentColor"
            aria-hidden="true"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"
            />
          </svg>
          <svg
            class="hidden h-6 w-6"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="1.5"
            stroke="currentColor"
            aria-hidden="true"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </button>
      </div>
    </div>
  </div>
  <!-- Mobile menu, show/hide based on menu state. -->
  <div
    class="md:hidden backdrop-filter backdrop-blur-lg bg-gray-900/90"
    id="mobile-menu"
  >
    <div class="px-2 pt-2 pb-3 space-y-1 sm:px-3 border-t border-purple-500/20">
      <a
        href="${pageContext.request.contextPath}/home"
        class="text-gray-300 hover:bg-indigo-900/50 hover:text-white block px-3 py-2 rounded-md text-base font-medium"
        >Home</a
      >
      <a
        href="${pageContext.request.contextPath}/browse"
        class="text-gray-300 hover:bg-indigo-900/50 hover:text-white block px-3 py-2 rounded-md text-base font-medium"
        >Browse Games</a
      >
      <c:if test="${not empty sessionScope.loggedInUser}">
        <a
          href="${pageContext.request.contextPath}/viewCart"
          class="text-gray-300 hover:bg-indigo-900/50 hover:text-white block px-3 py-2 rounded-md text-base font-medium"
          >Cart</a
        >
        <a
          href="${pageContext.request.contextPath}/orders"
          class="text-gray-300 hover:bg-indigo-900/50 hover:text-white block px-3 py-2 rounded-md text-base font-medium"
          >My Orders</a
        >
      </c:if>
      <c:if test="${not empty sessionScope.loggedInUser}">
        <a
          href="${pageContext.request.contextPath}/profile"
          class="text-gray-300 hover:bg-indigo-900/50 hover:text-white block px-3 py-2 rounded-md text-base font-medium"
          >Profile</a
        >
        <a
          href="${pageContext.request.contextPath}/logout"
          class="text-gray-300 hover:bg-indigo-900/50 hover:text-white block px-3 py-2 rounded-md text-base font-medium"
          >Logout</a
        >
      </c:if>
      <c:if test="${empty sessionScope.loggedInUser}">
        <a
          href="${pageContext.request.contextPath}/login"
          class="text-gray-300 hover:bg-indigo-900/50 hover:text-white block px-3 py-2 rounded-md text-base font-medium"
          >Login</a
        >
        <a
          href="${pageContext.request.contextPath}/register"
          class="bg-indigo-600 hover:bg-indigo-700 text-white block px-3 py-2 my-2 rounded-md text-base font-medium"
          >Register</a
        >
      </c:if>
    </div>
  </div>
</nav>

<script>
  // Dropdown show/hide logic
  const wrapper = document.getElementById("profileDropdownWrapper");
  const btn = document.getElementById("profileDropdownBtn");
  const menu = document.getElementById("profileDropdownMenu");
  if (wrapper && btn && menu) {
    let dropdownTimeout;
    wrapper.addEventListener("mouseenter", () => {
      clearTimeout(dropdownTimeout);
      menu.classList.remove("hidden");
    });
    wrapper.addEventListener("mouseleave", () => {
      dropdownTimeout = setTimeout(() => menu.classList.add("hidden"), 100);
    });
    btn.addEventListener("focus", () => menu.classList.remove("hidden"));
    btn.addEventListener("blur", () => menu.classList.add("hidden"));
  }
</script>
