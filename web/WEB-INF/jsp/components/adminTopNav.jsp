<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Top Navbar -->
<nav
  class="w-full flex items-center justify-between bg-gray-900 text-white px-8 py-4 fixed top-0 left-0 md:left-20 z-40 shadow transition-all duration-200"
  style="left: 0"
  id="topNavbar"
>
  <div class="flex items-center gap-2">
    <i class="bi bi-bootstrap-fill text-2xl"></i>
    <span class="text-2xl font-bold">GameVault</span>
  </div>
  <form
    action="${pageContext.request.contextPath}/logout"
    method="post"
    class="hidden md:block"
  >
    <button
      type="submit"
      class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded font-semibold flex items-center gap-2"
    >
      <i class="bi bi-box-arrow-right"></i> Logout
    </button>
  </form>
</nav>
