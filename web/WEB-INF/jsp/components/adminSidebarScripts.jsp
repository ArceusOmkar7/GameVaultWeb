<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script>
  // Sidebar toggle logic (improved)
  const sidebar = document.getElementById("sidebar");
  const openSidebar = document.getElementById("openSidebar");
  const closeSidebar = document.getElementById("closeSidebar");
  const toggleSidebar = document.getElementById("toggleSidebar");
  const toggleSidebarIcon = document.getElementById("toggleSidebarIcon");
  const sidebarLabels = document.querySelectorAll(".sidebar-label");
  let isSidebarOpen = false;

  function setSidebarState(expanded) {
    if (expanded) {
      sidebar.classList.remove("sidebar-collapsed");
      sidebar.classList.add("sidebar-expanded");
      sidebar.style.width = "16rem"; // 64
      sidebarLabels.forEach((l) => l.classList.remove("hidden"));
      sidebar.style.boxShadow = "2px 0 10px rgba(0,0,0,0.2)";
      if (toggleSidebarIcon)
        toggleSidebarIcon.classList.replace(
          "bi-chevron-right",
          "bi-chevron-left"
        );
      isSidebarOpen = true;
    } else {
      sidebar.classList.remove("sidebar-expanded");
      sidebar.classList.add("sidebar-collapsed");
      sidebar.style.width = "5rem"; // 20
      sidebarLabels.forEach((l) => l.classList.add("hidden"));
      sidebar.style.boxShadow = "none";
      if (toggleSidebarIcon)
        toggleSidebarIcon.classList.replace(
          "bi-chevron-left",
          "bi-chevron-right"
        );
      isSidebarOpen = false;
    }
  }

  openSidebar.addEventListener("click", () => {
    setSidebarState(true);
    openSidebar.style.display = "none";
  });

  closeSidebar.addEventListener("click", () => {
    setSidebarState(false);
    openSidebar.style.display = "block";
  });

  // Toggle sidebar on desktop
  if (toggleSidebar) {
    toggleSidebar.addEventListener("click", () => {
      setSidebarState(!isSidebarOpen);
    });
  }

  function handleResize() {
    if (window.innerWidth >= 768) {
      setSidebarState(true);
      openSidebar.style.display = "none";
    } else {
      setSidebarState(false);
      openSidebar.style.display = "block";
    }
  }

  window.addEventListener("resize", handleResize);
  document.addEventListener("DOMContentLoaded", handleResize);
</script>
