<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header
  class="fixed top-0 left-0 right-0 z-50 bg-gray-900/90 text-white shadow-lg backdrop-blur-sm"
>
  <div class="container mx-auto px-4 py-3">
    <div class="flex flex-wrap items-center justify-between">
      <!-- Logo -->
      <div class="flex items-center">
        <a
          href="${pageContext.request.contextPath}/home"
          class="flex items-center group"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-8 w-8 mr-2 text-indigo-400 group-hover:text-indigo-300 transition-colors duration-200"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              d="M11 17a1 1 0 001.447.894l4-2A1 1 0 0017 15V9.236a1 1 0 00-1.447-.894l-4 2a1 1 0 00-.553.894V17zM15.211 6.276a1 1 0 000-1.788l-4.764-2.382a1 1 0 00-.894 0L4.789 4.488a1 1 0 000 1.788l4.764 2.382a1 1 0 00.894 0l4.764-2.382zM4.447 8.342A1 1 0 003 9.236V15a1 1 0 00.553.894l4 2A1 1 0 009 17v-5.764a1 1 0 00-.553-.894l-4-2z"
            />
          </svg>
          <span
            class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400 group-hover:from-indigo-300 group-hover:via-purple-300 group-hover:to-pink-300 transition-all duration-200"
            >GameVault</span
          >
        </a>
      </div>

      <!-- Navigation -->
      <nav class="hidden md:flex items-center space-x-6">
        <a
          href="${pageContext.request.contextPath}/home"
          class="text-gray-200 hover:text-white transition-colors duration-200"
          >Home</a
        >
        <a
          href="${pageContext.request.contextPath}/browse"
          class="text-gray-200 hover:text-white transition-colors duration-200"
          >Browse</a
        >
        <a
          href="${pageContext.request.contextPath}/viewCart"
          class="text-gray-200 hover:text-white transition-colors duration-200 relative"
        >
          Cart
          <c:if
            test="${not empty sessionScope.cart and sessionScope.cart.size() > 0}"
          >
            <span
              class="absolute -top-2 -right-2 bg-indigo-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center"
            >
              ${sessionScope.cart.size()}
            </span>
          </c:if>
        </a>
        <c:choose>
          <c:when test="${empty sessionScope.user}">
            <a
              href="${pageContext.request.contextPath}/login"
              class="text-gray-200 hover:text-white transition-colors duration-200"
              >Login</a
            >
            <a
              href="${pageContext.request.contextPath}/register"
              class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg transition-colors duration-200"
              >Register</a
            >
          </c:when>
          <c:otherwise>
            <div class="relative group">
              <button
                class="flex items-center space-x-2 text-gray-200 hover:text-white transition-colors duration-200"
              >
                <span>${sessionScope.user.username}</span>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-5 w-5"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fill-rule="evenodd"
                    d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                    clip-rule="evenodd"
                  />
                </svg>
              </button>
              <div
                class="absolute right-0 mt-2 w-48 bg-gray-800 rounded-lg shadow-lg py-2 hidden group-hover:block"
              >
                <a
                  href="${pageContext.request.contextPath}/profile"
                  class="block px-4 py-2 text-gray-200 hover:bg-gray-700"
                  >Profile</a
                >
                <a
                  href="${pageContext.request.contextPath}/orders"
                  class="block px-4 py-2 text-gray-200 hover:bg-gray-700"
                  >Orders</a
                >
                <a
                  href="${pageContext.request.contextPath}/logout"
                  class="block px-4 py-2 text-gray-200 hover:bg-gray-700"
                  >Logout</a
                >
              </div>
            </div>
          </c:otherwise>
        </c:choose>
      </nav>

      <!-- Mobile Menu Button -->
      <button
        class="md:hidden text-white focus:outline-none"
        onclick="toggleMobileMenu()"
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
            d="M4 6h16M4 12h16M4 18h16"
          />
        </svg>
      </button>
    </div>

    <!-- Mobile Menu -->
    <div id="mobileMenu" class="md:hidden hidden mt-4 pb-4">
      <nav class="flex flex-col space-y-4">
        <a
          href="${pageContext.request.contextPath}/home"
          class="text-gray-200 hover:text-white transition-colors duration-200"
          >Home</a
        >
        <a
          href="${pageContext.request.contextPath}/browse"
          class="text-gray-200 hover:text-white transition-colors duration-200"
          >Browse</a
        >
        <a
          href="${pageContext.request.contextPath}/viewCart"
          class="text-gray-200 hover:text-white transition-colors duration-200"
          >Cart</a
        >
        <c:choose>
          <c:when test="${empty sessionScope.user}">
            <a
              href="${pageContext.request.contextPath}/login"
              class="text-gray-200 hover:text-white transition-colors duration-200"
              >Login</a
            >
            <a
              href="${pageContext.request.contextPath}/register"
              class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg transition-colors duration-200"
              >Register</a
            >
          </c:when>
          <c:otherwise>
            <a
              href="${pageContext.request.contextPath}/profile"
              class="text-gray-200 hover:text-white transition-colors duration-200"
              >Profile</a
            >
            <a
              href="${pageContext.request.contextPath}/orders"
              class="text-gray-200 hover:text-white transition-colors duration-200"
              >Orders</a
            >
            <a
              href="${pageContext.request.contextPath}/logout"
              class="text-gray-200 hover:text-white transition-colors duration-200"
              >Logout</a
            >
          </c:otherwise>
        </c:choose>
      </nav>
    </div>
  </div>
</header>

<script>
  function toggleMobileMenu() {
    const mobileMenu = document.getElementById("mobileMenu");
    mobileMenu.classList.toggle("hidden");
  }
</script>
