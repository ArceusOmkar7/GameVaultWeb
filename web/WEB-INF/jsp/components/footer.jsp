<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%-- Footer
Component with enhanced styling matching our GameVault theme --%>
<footer class="bg-gray-900 text-white mt-auto">
  <div class="container mx-auto px-4 py-12">
    <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
      <!-- About Section -->
      <div class="col-span-1 md:col-span-2">
        <div class="flex items-center mb-4">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-8 w-8 mr-2 text-indigo-400"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              d="M11 17a1 1 0 001.447.894l4-2A1 1 0 0017 15V9.236a1 1 0 00-1.447-.894l-4 2a1 1 0 00-.553.894V17zM15.211 6.276a1 1 0 000-1.788l-4.764-2.382a1 1 0 00-.894 0L4.789 4.488a1 1 0 000 1.788l4.764 2.382a1 1 0 00.894 0l4.764-2.382zM4.447 8.342A1 1 0 003 9.236V15a1 1 0 00.553.894l4 2A1 1 0 009 17v-5.764a1 1 0 00-.553-.894l-4-2z"
            />
          </svg>
          <span
            class="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400"
            >GameVault</span
          >
        </div>
        <p class="text-gray-300 text-base mb-4">
          Your ultimate destination for digital gaming. Discover, collect, and
          play your favorite games all in one place.
        </p>
        <p class="text-gray-400 text-sm">
          © 2024 GameVault. All rights reserved.
        </p>
      </div>

      <!-- Quick Links -->
      <div>
        <h3 class="text-lg font-semibold mb-4 text-indigo-300">Quick Links</h3>
        <ul class="space-y-2">
          <li>
            <a
              href="${pageContext.request.contextPath}/home"
              class="text-gray-300 hover:text-white transition-colors duration-200"
              >Home</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/browse"
              class="text-gray-300 hover:text-white transition-colors duration-200"
              >Browse Games</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/viewCart"
              class="text-gray-300 hover:text-white transition-colors duration-200"
              >Cart</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/profile"
              class="text-gray-300 hover:text-white transition-colors duration-200"
              >My Profile</a
            >
          </li>
        </ul>
      </div>

      <!-- Support -->
      <div>
        <h3 class="text-lg font-semibold mb-4 text-indigo-300">Support</h3>
        <ul class="space-y-2">
          <li>
            <a
              href="#"
              class="text-gray-300 hover:text-white transition-colors duration-200"
              >Help Center</a
            >
          </li>
          <li>
            <a
              href="#"
              class="text-gray-300 hover:text-white transition-colors duration-200"
              >Contact Us</a
            >
          </li>
          <li>
            <a
              href="#"
              class="text-gray-300 hover:text-white transition-colors duration-200"
              >Terms of Service</a
            >
          </li>
          <li>
            <a
              href="#"
              class="text-gray-300 hover:text-white transition-colors duration-200"
              >Privacy Policy</a
            >
          </li>
        </ul>
      </div>
    </div>

    <!-- Social Links -->
    <div class="mt-8 pt-8 border-t border-gray-800">
      <div class="flex justify-center space-x-6">
        <a
          href="#"
          class="text-gray-400 hover:text-white transition-colors duration-200"
        >
          <span class="sr-only">Facebook</span>
          <svg class="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
            <path
              fill-rule="evenodd"
              d="M22 12c0-5.523-4.477-10-10-10S2 6.477 2 12c0 4.991 3.657 9.128 8.438 9.878v-6.987h-2.54V12h2.54V9.797c0-2.506 1.492-3.89 3.777-3.89 1.094 0 2.238.195 2.238.195v2.46h-1.26c-1.243 0-1.63.771-1.63 1.562V12h2.773l-.443 2.89h-2.33v6.988C18.343 21.128 22 16.991 22 12z"
              clip-rule="evenodd"
            />
          </svg>
        </a>
        <a
          href="#"
          class="text-gray-400 hover:text-white transition-colors duration-200"
        >
          <span class="sr-only">Twitter</span>
          <svg class="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
            <path
              d="M8.29 20.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0022 5.92a8.19 8.19 0 01-2.357.646 4.118 4.118 0 001.804-2.27 8.224 8.224 0 01-2.605.996 4.107 4.107 0 00-6.993 3.743 11.65 11.65 0 01-8.457-4.287 4.106 4.106 0 001.27 5.477A4.072 4.072 0 012.8 9.713v.052a4.105 4.105 0 003.292 4.022 4.095 4.095 0 01-1.853.07 4.108 4.108 0 003.834 2.85A8.233 8.233 0 012 18.407a11.616 11.616 0 006.29 1.84"
            />
          </svg>
        </a>
        <a
          href="#"
          class="text-gray-400 hover:text-white transition-colors duration-200"
        >
          <span class="sr-only">Discord</span>
          <svg class="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
            <path
              d="M20.317 4.37a19.791 19.791 0 0 0-4.885-1.515.074.074 0 0 0-.079.037c-.21.375-.444.864-.608 1.25a18.27 18.27 0 0 0-5.487 0 12.64 12.64 0 0 0-.617-1.25.077.077 0 0 0-.079-.037A19.736 19.736 0 0 0 3.677 4.37a.07.07 0 0 0-.032.027C.533 9.046-.32 13.58.099 18.057a.082.082 0 0 0 .031.057 19.9 19.9 0 0 0 5.993 3.03.078.078 0 0 0 .084-.028c.462-.63.874-1.295 1.226-1.994.021-.041.001-.09-.041-.106a13.107 13.107 0 0 1-1.872-.892.077.077 0 0 1-.008-.128 10.2 10.2 0 0 0 .372-.292.074.074 0 0 1 .077-.01c3.928 1.793 8.18 1.793 12.062 0a.074.074 0 0 1 .078.01c.12.098.246.198.373.292a.077.077 0 0 1-.006.127 12.299 12.299 0 0 1-1.873.892.077.077 0 0 0-.041.107c.36.698.772 1.362 1.225 1.993a.076.076 0 0 0 .084.028 19.839 19.839 0 0 0 6.002-3.03.077.077 0 0 0 .032-.054c.5-5.177-.838-9.674-3.549-13.66a.061.061 0 0 0-.031-.03zM8.02 15.33c-1.183 0-2.157-1.085-2.157-2.419 0-1.333.956-2.419 2.157-2.419 1.21 0 2.176 1.096 2.157 2.42 0 1.333-.956 2.418-2.157 2.418zm7.975 0c-1.183 0-2.157-1.085-2.157-2.419 0-1.333.955-2.419 2.157-2.419 1.21 0 2.176 1.096 2.157 2.42 0 1.333-.946 2.418-2.157 2.418z"
            />
          </svg>
        </a>
      </div>
    </div>
  </div>
</footer>

<script>
  // Add alert auto-dismiss functionality
  document.addEventListener("DOMContentLoaded", function () {
    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll(".gv-alert");
    alerts.forEach((alert) => {
      setTimeout(() => {
        alert.style.opacity = "0";
        alert.style.transition = "opacity 0.5s";
        setTimeout(() => {
          alert.style.display = "none";
        }, 500);
      }, 5000);
    });
  });
</script>
