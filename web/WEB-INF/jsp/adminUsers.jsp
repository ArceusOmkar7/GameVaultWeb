<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <c:set var="pageTitle" value="User Management" />
    <jsp:include page="components/adminHead.jsp" />
  </head>
  <body class="bg-gray-100">
    <jsp:include page="components/adminSidebarNew.jsp" />
    <jsp:include page="components/adminTopNav.jsp" />

    <div
      class="pt-24 md:pl-64 container mx-auto px-4 py-6 transition-all duration-200"
      id="mainContent"
    >
      <!-- Page Title -->
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold text-gray-800">User Management</h1>
        <button
          id="addUserBtn"
          class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          <i class="bi bi-person-plus-fill mr-2"></i>Add New User
        </button>
      </div>

      <!-- Search and Filter Section -->
      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex flex-wrap gap-4">
          <div class="flex-1 min-w-[200px]">
            <input
              type="text"
              id="searchUser"
              placeholder="Search users..."
              class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div class="w-[200px]">
            <select
              id="roleFilter"
              class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Roles</option>
              <option value="true">Admin</option>
              <option value="false">User</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Users Table -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  User
                </th>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Email
                </th>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Role
                </th>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Wallet Balance
                </th>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Created At
                </th>
                <th
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Actions
                </th>
              </tr>
            </thead>
            <tbody
              class="bg-white divide-y divide-gray-200"
              id="usersTableBody"
            >
              <c:forEach items="${users}" var="user">
                <tr>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center">
                      <div class="flex-shrink-0 h-10 w-10">
                        <div
                          class="h-10 w-10 rounded-full bg-gray-200 flex items-center justify-center"
                        >
                          <i class="bi bi-person text-gray-500 text-xl"></i>
                        </div>
                      </div>
                      <div class="ml-4">
                        <div class="text-sm font-medium text-gray-900">
                          ${user.username}
                        </div>
                      </div>
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="text-sm text-gray-900">${user.email}</div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span
                      class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${user.admin ? 'bg-purple-100 text-purple-800' : 'bg-green-100 text-green-800'}"
                    >
                      ${user.admin ? 'Admin' : 'User'}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="text-sm text-gray-900">
                      $${user.walletBalance}
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="text-sm text-gray-900">
                      <fmt:formatDate
                        value="${user.createdAt}"
                        pattern="MMM dd, yyyy"
                      />
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button
                      onclick="editUser(${user.userId})"
                      class="text-blue-600 hover:text-blue-900 mr-3"
                    >
                      <i class="bi bi-pencil"></i>
                    </button>
                    <button
                      onclick="deleteUser(${user.userId})"
                      class="text-red-600 hover:text-red-900"
                    >
                      <i class="bi bi-trash"></i>
                    </button>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Add/Edit User Modal -->
    <div
      id="userModal"
      class="fixed inset-0 bg-gray-600 bg-opacity-50 hidden overflow-y-auto h-full w-full"
    >
      <div
        class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white"
      >
        <div class="mt-3">
          <h3
            class="text-lg font-medium leading-6 text-gray-900"
            id="modalTitle"
          >
            Add New User
          </h3>
          <form id="userForm" class="mt-4">
            <input type="hidden" id="userId" />
            <div class="mb-4">
              <label
                class="block text-gray-700 text-sm font-bold mb-2"
                for="username"
                >Username</label
              >
              <input
                type="text"
                id="username"
                name="username"
                required
                class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              />
            </div>
            <div class="mb-4">
              <label
                class="block text-gray-700 text-sm font-bold mb-2"
                for="email"
                >Email</label
              >
              <input
                type="email"
                id="email"
                name="email"
                required
                class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              />
            </div>
            <div class="mb-4">
              <label
                class="block text-gray-700 text-sm font-bold mb-2"
                for="password"
                >Password</label
              >
              <input
                type="password"
                id="password"
                name="password"
                class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              />
            </div>
            <div class="mb-4">
              <label
                class="block text-gray-700 text-sm font-bold mb-2"
                for="walletBalance"
                >Wallet Balance</label
              >
              <input
                type="number"
                id="walletBalance"
                name="walletBalance"
                step="0.01"
                min="0"
                required
                class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              />
            </div>
            <div class="mb-4">
              <label
                class="block text-gray-700 text-sm font-bold mb-2"
                for="isAdmin"
                >Role</label
              >
              <select
                id="isAdmin"
                name="isAdmin"
                required
                class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              >
                <option value="false">User</option>
                <option value="true">Admin</option>
              </select>
            </div>
            <div class="flex justify-end gap-2">
              <button
                type="button"
                onclick="closeModal()"
                class="bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400"
              >
                Cancel
              </button>
              <button
                type="submit"
                class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
              >
                Save
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Scripts -->
    <jsp:include page="components/adminSidebarScripts.jsp" />
    <script>
      // User management functions
      function editUser(userId) {
        // Fetch user data and populate modal
        fetch(`${pageContext.request.contextPath}/admin/users/${userId}`)
          .then((response) => response.json())
          .then((user) => {
            document.getElementById("modalTitle").textContent = "Edit User";
            document.getElementById("userId").value = user.userId;
            document.getElementById("username").value = user.username;
            document.getElementById("email").value = user.email;
            document.getElementById("walletBalance").value = user.walletBalance;
            document.getElementById("isAdmin").value = user.admin;
            document.getElementById("password").required = false;
            document.getElementById("userModal").classList.remove("hidden");
          });
      }

      function deleteUser(userId) {
        if (confirm("Are you sure you want to delete this user?")) {
          fetch(`${pageContext.request.contextPath}/admin/users/${userId}`, {
            method: "DELETE",
          }).then((response) => {
            if (response.ok) {
              location.reload();
            }
          });
        }
      }

      function closeModal() {
        document.getElementById("userModal").classList.add("hidden");
        document.getElementById("userForm").reset();
        document.getElementById("userId").value = "";
        document.getElementById("modalTitle").textContent = "Add New User";
        document.getElementById("password").required = true;
      }

      // Event Listeners
      document.getElementById("addUserBtn").addEventListener("click", () => {
        document.getElementById("userModal").classList.remove("hidden");
      });

      document.getElementById("userForm").addEventListener("submit", (e) => {
        e.preventDefault();
        const userId = document.getElementById("userId").value;
        const method = userId ? "PUT" : "POST";
        const url = userId
          ? `${pageContext.request.contextPath}/admin/users/${userId}`
          : `${pageContext.request.contextPath}/admin/users`;

        fetch(url, {
          method: method,
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            userId: userId || null,
            username: document.getElementById("username").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value,
            walletBalance: parseFloat(
              document.getElementById("walletBalance").value
            ),
            admin: document.getElementById("isAdmin").value === "true",
          }),
        }).then((response) => {
          if (response.ok) {
            location.reload();
          }
        });
      });

      // Search and filter functionality
      document
        .getElementById("searchUser")
        .addEventListener("input", filterUsers);
      document
        .getElementById("roleFilter")
        .addEventListener("change", filterUsers);

      function filterUsers() {
        const searchTerm = document
          .getElementById("searchUser")
          .value.toLowerCase();
        const roleFilter = document.getElementById("roleFilter").value;
        const rows = document
          .getElementById("usersTableBody")
          .getElementsByTagName("tr");

        for (let row of rows) {
          const username = row.cells[0].textContent.toLowerCase();
          const email = row.cells[1].textContent.toLowerCase();
          const role = row.cells[2].textContent.trim();
          const matchesSearch =
            username.includes(searchTerm) || email.includes(searchTerm);
          const matchesRole =
            !roleFilter || role === (roleFilter === "true" ? "Admin" : "User");
          row.style.display = matchesSearch && matchesRole ? "" : "none";
        }
      }
    </script>
  </body>
</html>
