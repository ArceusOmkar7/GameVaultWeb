<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GameVault - Admin Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
    />
    <style>
      .sidebar {
        transition: width 0.3s, left 0.3s;
        width: 64px;
        min-width: 64px;
        max-width: 220px;
        background: #1e293b;
        color: #fff;
        height: 100vh;
        position: fixed;
        top: 0;
        left: 0;
        z-index: 40;
        overflow-x: hidden;
        box-shadow: 2px 0 8px rgba(0, 0, 0, 0.04);
      }
      .sidebar.expanded {
        width: 220px;
        min-width: 220px;
      }
      @media (max-width: 768px) {
        .sidebar {
          left: -220px;
          width: 220px;
          min-width: 220px;
        }
        .sidebar.expanded {
          left: 0;
        }
        .main-content {
          margin-left: 0 !important;
        }
        .sidebar-overlay {
          display: none;
          position: fixed;
          top: 0;
          left: 0;
          width: 100vw;
          height: 100vh;
          background: rgba(0,0,0,0.4);
          z-index: 30;
        }
        .sidebar.expanded ~ .sidebar-overlay {
          display: block;
        }
      }
      .main-content {
        margin-left: 64px;
        transition: margin-left 0.3s;
      }
      .sidebar.expanded ~ .main-content {
        margin-left: 220px;
      }
      .sidebar .nav-link {
        display: flex;
        align-items: center;
        gap: 1rem;
        padding: 0.75rem 1rem;
        color: #fff;
        text-decoration: none;
        font-size: 1rem;
        border-radius: 0.375rem;
        transition: background 0.2s;
      }
      .sidebar .nav-link:hover {
        background: #334155;
      }
      .sidebar .nav-link .nav-text {
        display: none;
      }
      .sidebar.expanded .nav-link .nav-text {
        display: inline;
      }
      .sidebar .sidebar-toggle {
        background: none;
        border: none;
        color: #fff;
        font-size: 1.5rem;
        width: 100%;
        text-align: left;
        padding: 1rem 1rem 0.5rem 1rem;
        cursor: pointer;
        outline: none;
        margin-top: 1.5rem; /* Added margin to move the menu button lower */
      }
    </style>
  </head>
  <body class="bg-gray-100 flex flex-col min-h-screen">
    <jsp:include page="header.jsp" />

    <div class="flex flex-row flex-grow">
      <%-- Sidebar --%>
      <div class="sidebar">
        <button class="sidebar-toggle" id="sidebarToggle">
          <i class="bi bi-list"></i>
        </button>
        <nav class="mt-4">
          <a
            href="#"
            class="nav-link active"
            aria-current="page"
            ><i class="bi bi-house-door"></i
            ><span class="nav-text">Dashboard</span>
          </a>
          <a href="#" class="nav-link">
            <i class="bi bi-gamepad"></i>
            <span class="nav-text">Game Management</span>
          </a>
          <a href="#" class="nav-link">
            <i class="bi bi-person"></i>
            <span class="nav-text">User Management</span>
          </a>
          <a href="#" class="nav-link">
            <i class="bi bi-cart"></i>
            <span class="nav-text">Order History</span>
          </a>
          <a href="#" class="nav-link">
            <i class="bi bi-gear"></i>
            <span class="nav-text">Settings</span>
          </a>
        </nav>
      </div>

      <main class="main-content flex-grow container mx-auto p-4">
        <div class="bg-white rounded-lg shadow-md p-6 mb-6">
          <div class="flex justify-between items-center mb-6">
            <h1 class="text-3xl font-bold text-purple-800">Admin Dashboard</h1>
            <div class="flex space-x-3">
              <a
                href="${pageContext.request.contextPath}/admin/generate-dummy-data"
                class="bg-purple-500 hover:bg-purple-600 text-white font-medium py-2 px-4 rounded"
              >
                Generate Dummy Data
              </a>
              <c:if
                test="${not empty sessionScope.loggedInUser && sessionScope.loggedInUser.admin}"
              >
                <span
                  class="bg-purple-100 text-purple-800 text-xs font-medium mr-2 px-2.5 py-0.5 rounded"
                >
                  Admin: ${sessionScope.loggedInUser.username}
                </span>
              </c:if>
            </div>
          </div>

          <%-- Display messages --%>
          <c:if test="${not empty message || not empty param.message}">
            <div
              class="mb-4 p-4 rounded ${(messageType eq 'success' || param.messageType eq 'success') ? 'bg-green-100 border border-green-400 text-green-700' : (messageType eq 'error' || param.messageType eq 'error') ? 'bg-red-100 border border-red-400 text-red-700' : (messageType eq 'warning' || param.messageType eq 'warning') ? 'bg-yellow-100 border border-yellow-400 text-yellow-700' : 'bg-blue-100 border border-blue-400 text-blue-700'}"
              role="alert"
            >
              <p>
                <c:out value="${not empty message ? message : param.message}" />
              </p>
            </div>
          </c:if>
          <c:if test="${not empty errorMessage}">
            <div
              class="mb-4 p-4 rounded bg-red-100 border border-red-400 text-red-700"
              role="alert"
            >
              <p><strong>Error:</strong> <c:out value="${errorMessage}" /></p>
            </div>
          </c:if>

          <%-- Admin Dashboard Tabs --%>
          <div class="mb-6">
            <ul
              class="flex flex-wrap text-sm font-medium text-center text-gray-500 border-b border-gray-200"
            >
              <li class="mr-2">
                <a
                  href="#"
                  class="inline-block p-4 text-purple-600 bg-gray-100 rounded-t-lg active"
                  aria-current="page"
                >
                  Game Management
                </a>
              </li>
              <li class="mr-2">
                <a
                  href="#"
                  class="inline-block p-4 rounded-t-lg hover:text-gray-600 hover:bg-gray-50"
                >
                  User Management
                </a>
              </li>
              <li class="mr-2">
                <a
                  href="#"
                  class="inline-block p-4 rounded-t-lg hover:text-gray-600 hover:bg-gray-50"
                >
                  Order History
                </a>
              </li>
            </ul>
          </div>

          <%-- Game Management Section --%>
          <div class="overflow-x-auto">
            <div class="flex justify-between items-center mb-4">
              <h2 class="text-2xl font-semibold">Game Catalog</h2>
              <button
                id="addGameBtn"
                class="bg-green-500 hover:bg-green-600 text-white font-medium py-2 px-4 rounded"
              >
                Add New Game
              </button>
            </div>

            <%-- Add Game Form (Hidden by default) --%>
            <div
              id="addGameForm"
              class="hidden mb-6 p-4 bg-gray-50 rounded border border-gray-300"
            >
              <h3 class="text-xl font-semibold mb-4">Add New Game</h3>
              <form
                action="${pageContext.request.contextPath}/admin/add-game"
                method="post"
                class="space-y-4"
              >
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label
                      for="title"
                      class="block text-sm font-medium text-gray-700 mb-1"
                      >Title *</label
                    >
                    <input
                      type="text"
                      id="title"
                      name="title"
                      required
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                    />
                  </div>
                  <div>
                    <label
                      for="developer"
                      class="block text-sm font-medium text-gray-700 mb-1"
                      >Developer *</label
                    >
                    <input
                      type="text"
                      id="developer"
                      name="developer"
                      required
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                    />
                  </div>
                  <div>
                    <label
                      for="platform"
                      class="block text-sm font-medium text-gray-700 mb-1"
                      >Platform *</label
                    >
                    <select
                      id="platform"
                      name="platform"
                      required
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                    >
                      <option value="">Select Platform</option>
                      <option value="PC">PC</option>
                      <option value="PlayStation 5">PlayStation 5</option>
                      <option value="PlayStation 4">PlayStation 4</option>
                      <option value="Xbox Series X">Xbox Series X</option>
                      <option value="Xbox One">Xbox One</option>
                      <option value="Nintendo Switch">Nintendo Switch</option>
                      <option value="Mobile">Mobile</option>
                    </select>
                  </div>
                  <div>
                    <label
                      for="price"
                      class="block text-sm font-medium text-gray-700 mb-1"
                      >Price * ($)</label
                    >
                    <input
                      type="number"
                      id="price"
                      name="price"
                      min="0"
                      step="0.01"
                      required
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                    />
                  </div>
                  <div>
                    <label
                      for="releaseDate"
                      class="block text-sm font-medium text-gray-700 mb-1"
                      >Release Date *</label
                    >
                    <input
                      type="date"
                      id="releaseDate"
                      name="releaseDate"
                      required
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                    />
                  </div>
                  <div>
                    <label
                      for="genre"
                      class="block text-sm font-medium text-gray-700 mb-1"
                      >Genre *</label
                    >
                    <select
                      id="genre"
                      name="genre"
                      required
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                    >
                      <option value="">Select Genre</option>
                      <option value="Action">Action</option>
                      <option value="Adventure">Adventure</option>
                      <option value="RPG">RPG</option>
                      <option value="Strategy">Strategy</option>
                      <option value="Simulation">Simulation</option>
                      <option value="Sports">Sports</option>
                      <option value="Racing">Racing</option>
                      <option value="Puzzle">Puzzle</option>
                      <option value="Fighting">Fighting</option>
                      <option value="Horror">Horror</option>
                      <option value="Shooter">Shooter</option>
                    </select>
                  </div>
                </div>
                <div>
                  <label
                    for="description"
                    class="block text-sm font-medium text-gray-700 mb-1"
                    >Description *</label
                  >
                  <textarea
                    id="description"
                    name="description"
                    rows="4"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                  ></textarea>
                </div>
                <div class="flex justify-end space-x-3">
                  <button
                    type="button"
                    id="cancelAddGame"
                    class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-medium py-2 px-4 rounded"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    class="bg-green-500 hover:bg-green-600 text-white font-medium py-2 px-4 rounded"
                  >
                    Add Game
                  </button>
                </div>
              </form>
            </div>

            <table class="min-w-full bg-white border border-gray-200">
              <thead class="bg-gray-100">
                <tr>
                  <th class="py-2 px-4 border-b text-left">ID</th>
                  <th class="py-2 px-4 border-b text-left">Image</th>
                  <th class="py-2 px-4 border-b text-left">Title</th>
                  <th class="py-2 px-4 border-b text-left">Developer</th>
                  <th class="py-2 px-4 border-b text-left">Platform</th>
                  <th class="py-2 px-4 border-b text-left">Price</th>
                  <th class="py-2 px-4 border-b text-left">Release Date</th>
                  <th class="py-2 px-4 border-b text-left">Actions</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="game" items="${games}">
                  <tr class="hover:bg-gray-50">
                    <td class="py-2 px-4 border-b">${game.gameId}</td>
                    <td class="py-2 px-4 border-b">
                      <c:choose>
                        <c:when test="${not empty game.imagePath}">
                          <img
                            src="${pageContext.request.contextPath}/${game.imagePath}"
                            alt="${game.title}"
                            class="w-20 h-20 object-cover rounded"
                          />
                        </c:when>
                        <c:otherwise>
                          <div
                            class="w-20 h-20 bg-gray-200 rounded flex items-center justify-center text-gray-400 text-xs"
                          >
                            No Image
                          </div>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td class="py-2 px-4 border-b">${game.title}</td>
                    <td class="py-2 px-4 border-b">${game.developer}</td>
                    <td class="py-2 px-4 border-b">${game.platform}</td>
                    <td class="py-2 px-4 border-b">
                      <fmt:formatNumber
                        value="${game.price}"
                        type="currency"
                        currencySymbol="$"
                      />
                    </td>
                    <td class="py-2 px-4 border-b">
                      <fmt:formatDate
                        value="${game.releaseDate}"
                        pattern="MM/dd/yyyy"
                      />
                    </td>
                    <td class="py-2 px-4 border-b">
                      <form
                        action="${pageContext.request.contextPath}/admin/upload-game-image"
                        method="post"
                        enctype="multipart/form-data"
                        class="flex items-center space-x-2"
                      >
                        <input
                          type="hidden"
                          name="gameId"
                          value="${game.gameId}"
                        />
                        <div class="flex-1">
                          <input
                            type="file"
                            name="gameImage"
                            accept="image/*"
                            class="w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
                          />
                        </div>
                        <button
                          type="submit"
                          class="bg-blue-500 hover:bg-blue-700 text-white py-1 px-3 rounded text-sm"
                        >
                          Upload
                        </button>
                      </form>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>

    <jsp:include page="footer.jsp" />

    <script>
      // Toggle Add Game Form
      document
        .getElementById("addGameBtn")
        .addEventListener("click", function () {
          document.getElementById("addGameForm").classList.toggle("hidden");
        });

      document
        .getElementById("cancelAddGame")
        .addEventListener("click", function () {
          document.getElementById("addGameForm").classList.add("hidden");
        });

      // Toggle Sidebar
      document
        .getElementById("sidebarToggle")
        .addEventListener("click", function () {
          document.querySelector(".sidebar").classList.toggle("expanded");
        });
    </script>
  </body>
</html>
