<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%-- Footer
Component with enhanced styling matching our GameVault theme --%>
<footer class="bg-gray-800 text-white mt-auto">
  <div class="gv-container py-10">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
      <div>
        <h3 class="text-2xl font-bold mb-4">GameVault</h3>
        <p class="text-gray-300 text-base mb-2">
          Your one-stop destination for gaming. Browse, buy, and build your
          digital game collection.<br />
          Discover the latest releases, exclusive deals, and a vibrant gaming
          community.
        </p>
        <p class="text-gray-400 text-sm mt-4">Empowering gamers since 2023.</p>
      </div>
      <div>
        <h3 class="text-lg font-bold mb-4">Quick Links</h3>
        <ul class="space-y-2">
          <li>
            <a
              href="${pageContext.request.contextPath}/home"
              class="text-gray-300 hover:text-white"
              >Home</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/browse"
              class="text-gray-300 hover:text-white"
              >Browse Games</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/viewCart"
              class="text-gray-300 hover:text-white"
              >Cart</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/profile"
              class="text-gray-300 hover:text-white"
              >My Profile</a
            >
          </li>
        </ul>
      </div>
    </div>
  </div>
  <div class="bg-gray-900 py-4">
    <div class="gv-container">
      <p class="text-center text-gray-400">
        © <%= new java.text.SimpleDateFormat("yyyy").format(new
        java.util.Date()) %> GameVault Application. All rights reserved.
      </p>
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
