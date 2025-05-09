<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%--
Simplified footer with just copyright information --%>
<footer
  class="text-white mt-auto border-t border-purple-500/20"
  style="
    background: linear-gradient(
      90deg,
      rgba(26, 28, 46, 0.95) 0%,
      rgba(45, 27, 105, 0.95) 100%
    );
  "
>
  <div class="py-4">
    <div class="gv-container">
      <p class="text-center text-gray-400 text-sm">
        © <%= new java.text.SimpleDateFormat("yyyy").format(new
        java.util.Date()) %> GameVault. All rights reserved.
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
