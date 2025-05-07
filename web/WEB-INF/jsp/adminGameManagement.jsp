<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Admin Game Management</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body class="bg-gray-100">
    <!-- Sidebar -->
    <div id="sidebar" class="fixed top-0 left-0 h-full w-20 md:w-64 bg-gray-900 text-gray-200 z-50 transition-all duration-200 ease-in-out flex flex-col sidebar-collapsed shadow-lg mt-16">
        <div class="flex items-center justify-between px-6 py-4 border-b border-gray-800">
            <div class="flex items-center gap-2">
                <i class="bi bi-bootstrap-fill text-2xl text-gray-200"></i>
                <span class="sidebar-label text-2xl font-bold text-gray-200">GameVault</span>
            </div>
            <button id="toggleSidebar" class="hidden md:inline text-2xl focus:outline-none text-gray-200 ml-2" title="Collapse/Expand Sidebar">
                <i class="bi bi-chevron-left" id="toggleSidebarIcon"></i>
            </button>
            <button id="closeSidebar" class="md:hidden text-2xl focus:outline-none text-gray-200"><i class="bi bi-x"></i></button>
        </div>
        <nav class="flex-1 px-4 py-6">
            <ul class="space-y-2">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200"><i class="bi bi-speedometer2 text-gray-200"></i> <span class="sidebar-label text-gray-200">Dashboard</span></a></li>
                <li><a href="#" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200"><i class="bi bi-people text-gray-200"></i> <span class="sidebar-label text-gray-200">Users</span></a></li>
                <li><a href="${pageContext.request.contextPath}/admin/game-management" class="flex items-center gap-2 px-3 py-2 rounded bg-gray-800 text-white"><i class="bi bi-controller text-white"></i> <span class="sidebar-label text-white">Games</span></a></li>
                <li><a href="#" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200"><i class="bi bi-bag-check text-gray-200"></i> <span class="sidebar-label text-gray-200">Orders</span></a></li>
                <li><a href="${pageContext.request.contextPath}/admin/load-json-data" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200"><i class="bi bi-database-fill-gear text-gray-200"></i> <span class="sidebar-label text-gray-200">Load JSON Data</span></a></li>
                <li><form action="${pageContext.request.contextPath}/logout" method="post"><button type="submit" class="w-full flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-800 text-gray-200"><i class="bi bi-box-arrow-right text-gray-200"></i> <span class="sidebar-label text-gray-200">Logout</span></button></form></li>
            </ul>
        </nav>
    </div>
    <!-- Hamburger Button -->
    <button id="openSidebar" class="fixed top-4 left-4 z-60 bg-blue-700 text-white p-2 rounded focus:outline-none"><i class="bi bi-list text-2xl"></i></button>
    <!-- Top Navbar -->
    <nav class="w-full flex items-center justify-between bg-gray-900 text-white px-8 py-4 fixed top-0 left-0 md:left-20 z-40 shadow transition-all duration-200" style="left:0;" id="topNavbar">
        <div class="flex items-center gap-2">
            <i class="bi bi-bootstrap-fill text-2xl"></i>
            <span class="text-2xl font-bold">GameVault</span>
        </div>
        <form action="${pageContext.request.contextPath}/logout" method="post" class="hidden md:block">
            <button type="submit" class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded font-semibold flex items-center gap-2">
                <i class="bi bi-box-arrow-right"></i> Logout
            </button>
        </form>
    </nav>

    <!-- Main Content -->
    <div class="pt-24 md:pl-64 container mx-auto px-4 py-6 transition-all duration-200" id="mainContent">
        <!-- Page Title -->
        <div class="flex justify-between items-center mb-6">
            <h1 class="text-3xl font-bold text-gray-800">Game Management</h1>
            <button id="addGameBtn" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded font-semibold flex items-center gap-2">
                <i class="bi bi-plus-circle"></i> Add New Game
            </button>
        </div>

        <!-- Game Filter Controls -->
        <div class="bg-white rounded-lg shadow p-6 mb-6">
            <div class="flex flex-wrap justify-between items-end gap-4">
                <div class="w-full md:w-1/3">
                    <label for="searchQuery" class="block text-sm font-medium text-gray-700 mb-1">Search Games</label>
                    <input type="text" id="searchQuery" name="searchQuery" placeholder="Search by title or developer..." 
                           class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                </div>
                <div class="w-full md:w-1/4">
                    <label for="platformFilter" class="block text-sm font-medium text-gray-700 mb-1">Platform</label>
                    <select id="platformFilter" name="platformFilter" 
                            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">All Platforms</option>
                        <option value="PC">PC</option>
                        <option value="PlayStation 5">PlayStation 5</option>
                        <option value="PlayStation 4">PlayStation 4</option>
                        <option value="Xbox Series S/X">Xbox Series S/X</option>
                        <option value="Xbox One">Xbox One</option>
                        <option value="Nintendo Switch">Nintendo Switch</option>
                    </select>
                </div>
                <div class="w-full md:w-1/4">
                    <label for="sortBy" class="block text-sm font-medium text-gray-700 mb-1">Sort By</label>
                    <select id="sortBy" name="sortBy" 
                            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="title_asc">Title (A-Z)</option>
                        <option value="title_desc">Title (Z-A)</option>
                        <option value="price_asc">Price (Low to High)</option>
                        <option value="price_desc">Price (High to Low)</option>
                        <option value="rating_desc">Rating (High to Low)</option>
                        <option value="release_date">Release Date (Newest)</option>
                    </select>
                </div>
                <div>
                    <button id="applyFilters" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded font-medium">
                        Apply Filters
                    </button>
                </div>
            </div>
        </div>

        <!-- Games List -->
        <div class="bg-white rounded-lg shadow overflow-hidden">
            <div class="overflow-x-auto">
                <table class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                        <tr>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Game</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Developer</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Platform</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Price</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Rating</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Release Date</th>
                            <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                        </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                        <c:forEach var="game" items="${games}">
                            <tr>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="flex items-center">
                                        <div class="flex-shrink-0 h-10 w-10">
                                            <img class="h-10 w-10 rounded-sm object-cover" src="${game.getImagePathOrDefault()}" alt="${game.title}">
                                        </div>
                                        <div class="ml-4">
                                            <div class="text-sm font-medium text-gray-900">${game.title}</div>
                                            <div class="text-sm text-gray-500">${game.genre}</div>
                                        </div>
                                    </div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="text-sm text-gray-900">${game.developer}</div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="text-sm text-gray-900">${game.platform}</div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="text-sm text-gray-900">$<fmt:formatNumber value="${game.price}" pattern="#,##0.00"/></div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="text-sm text-gray-900">
                                        <span class="flex items-center">
                                            <fmt:formatNumber value="${game.rating}" pattern="#.##"/>
                                            <i class="bi bi-star-fill text-yellow-400 ml-1"></i>
                                        </span>
                                    </div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="text-sm text-gray-900">
                                        <fmt:formatDate value="${game.releaseDate}" pattern="MMM d, yyyy"/>
                                    </div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                    <a href="${pageContext.request.contextPath}/admin/edit-game?id=${game.gameId}" class="text-blue-600 hover:text-blue-900 mr-3">Edit</a>
                                    <a href="#" class="text-red-600 hover:text-red-900 delete-game" data-id="${game.gameId}" data-title="${game.title}">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                        
                        <!-- If no games are found -->
                        <c:if test="${empty games}">
                            <tr>
                                <td colspan="7" class="px-6 py-4 text-center text-gray-500">
                                    No games found. <a href="${pageContext.request.contextPath}/admin/load-json-data" class="text-blue-600 hover:underline">Import games from JSON</a> or add games manually.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            
            <!-- Pagination controls -->
            <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
                <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
                    <div>
                        <p class="text-sm text-gray-700">
                            Showing <span class="font-medium">1</span> to <span class="font-medium">10</span> of <span class="font-medium">${totalGames}</span> results
                        </p>
                    </div>
                    <div>
                        <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                            <a href="#" class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
                                <span class="sr-only">Previous</span>
                                <i class="bi bi-chevron-left"></i>
                            </a>
                            <a href="#" class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-blue-50 text-sm font-medium text-blue-600 hover:bg-blue-100">1</a>
                            <a href="#" class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">2</a>
                            <a href="#" class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">3</a>
                            <span class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700">...</span>
                            <a href="#" class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">8</a>
                            <a href="#" class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">9</a>
                            <a href="#" class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">10</a>
                            <a href="#" class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
                                <span class="sr-only">Next</span>
                                <i class="bi bi-chevron-right"></i>
                            </a>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Add Game Modal -->
    <div id="addGameModal" class="fixed inset-0 bg-gray-800 bg-opacity-75 flex items-center justify-center z-50 hidden">
        <div class="bg-white rounded-lg shadow-lg w-full max-w-3xl max-h-screen overflow-y-auto">
            <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
                <h3 class="text-lg font-semibold text-gray-900">Add New Game</h3>
                <button id="closeAddGameModal" class="text-gray-400 hover:text-gray-500">
                    <i class="bi bi-x-lg"></i>
                </button>
            </div>
            <div class="p-6">
                <form id="addGameForm" action="${pageContext.request.contextPath}/admin/add-game" method="post">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div class="col-span-2">
                            <label for="title" class="block text-sm font-medium text-gray-700">Title</label>
                            <input type="text" name="title" id="title" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" required>
                        </div>
                        
                        <div class="col-span-2">
                            <label for="description" class="block text-sm font-medium text-gray-700">Description</label>
                            <textarea name="description" id="description" rows="3" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"></textarea>
                        </div>
                        
                        <div>
                            <label for="developer" class="block text-sm font-medium text-gray-700">Developer</label>
                            <input type="text" name="developer" id="developer" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md">
                        </div>
                        
                        <div>
                            <label for="price" class="block text-sm font-medium text-gray-700">Price</label>
                            <div class="mt-1 relative rounded-md shadow-sm">
                                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <span class="text-gray-500 sm:text-sm">$</span>
                                </div>
                                <input type="number" step="0.01" min="0" name="price" id="price" class="focus:ring-blue-500 focus:border-blue-500 block w-full pl-7 pr-12 sm:text-sm border-gray-300 rounded-md" placeholder="0.00" required>
                            </div>
                        </div>
                        
                        <div>
                            <label for="platform" class="block text-sm font-medium text-gray-700">Platform</label>
                            <input type="text" name="platform" id="platform" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" placeholder="PC, PlayStation 5, Xbox Series X/S">
                        </div>
                        
                        <div>
                            <label for="genre" class="block text-sm font-medium text-gray-700">Genre</label>
                            <input type="text" name="genre" id="genre" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" placeholder="Action, Adventure, RPG">
                        </div>
                        
                        <div>
                            <label for="releaseDate" class="block text-sm font-medium text-gray-700">Release Date</label>
                            <input type="date" name="releaseDate" id="releaseDate" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md">
                        </div>
                        
                        <div>
                            <label for="rating" class="block text-sm font-medium text-gray-700">Rating</label>
                            <input type="number" step="0.1" min="0" max="5" name="rating" id="rating" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" placeholder="0.0-5.0">
                        </div>
                        
                        <div class="col-span-2">
                            <label for="imagePath" class="block text-sm font-medium text-gray-700">Image Path</label>
                            <input type="text" name="imagePath" id="imagePath" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" placeholder="URL or path to image">
                            <p class="mt-1 text-xs text-gray-500">Enter a URL or upload an image separately via the image upload tool</p>
                        </div>
                    </div>
                    
                    <div class="mt-6 flex justify-end">
                        <button type="button" id="cancelAddGame" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 mr-3">
                            Cancel
                        </button>
                        <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                            Save Game
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div id="deleteModal" class="fixed inset-0 bg-gray-800 bg-opacity-75 flex items-center justify-center z-50 hidden">
        <div class="bg-white rounded-lg shadow-lg w-full max-w-md">
            <div class="px-6 py-4 border-b border-gray-200">
                <h3 class="text-lg font-semibold text-gray-900">Confirm Deletion</h3>
            </div>
            <div class="p-6">
                <p class="text-gray-700 mb-4">Are you sure you want to delete the game "<span id="deleteGameTitle"></span>"? This action cannot be undone.</p>
                <form id="deleteGameForm" action="${pageContext.request.contextPath}/admin/delete-game" method="post">
                    <input type="hidden" id="deleteGameId" name="gameId">
                    <div class="flex justify-end">
                        <button type="button" id="cancelDelete" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 mr-3">
                            Cancel
                        </button>
                        <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500">
                            Delete
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />

    <script>
        // Sidebar toggle logic
        const sidebar = document.getElementById('sidebar');
        const openSidebar = document.getElementById('openSidebar');
        const closeSidebar = document.getElementById('closeSidebar');
        const toggleSidebar = document.getElementById('toggleSidebar');
        const toggleSidebarIcon = document.getElementById('toggleSidebarIcon');
        const sidebarLabels = document.querySelectorAll('.sidebar-label');
        let isSidebarOpen = false;
        
        function setSidebarState(expanded) {
            if (expanded) {
                sidebar.classList.remove('sidebar-collapsed');
                sidebar.classList.add('sidebar-expanded');
                sidebar.style.width = '16rem'; // 64
                sidebarLabels.forEach(l => l.classList.remove('hidden'));
                sidebar.style.boxShadow = '2px 0 10px rgba(0,0,0,0.2)';
                if (toggleSidebarIcon) toggleSidebarIcon.classList.replace('bi-chevron-right', 'bi-chevron-left');
                isSidebarOpen = true;
            } else {
                sidebar.classList.remove('sidebar-expanded');
                sidebar.classList.add('sidebar-collapsed');
                sidebar.style.width = '5rem'; // 20
                sidebarLabels.forEach(l => l.classList.add('hidden'));
                sidebar.style.boxShadow = 'none';
                if (toggleSidebarIcon) toggleSidebarIcon.classList.replace('bi-chevron-left', 'bi-chevron-right');
                isSidebarOpen = false;
            }
        }
        
        openSidebar.addEventListener('click', () => {
            setSidebarState(true);
            openSidebar.style.display = 'none';
        });
        
        closeSidebar.addEventListener('click', () => {
            setSidebarState(false);
            openSidebar.style.display = 'block';
        });
        
        // Toggle sidebar on desktop
        if (toggleSidebar) {
            toggleSidebar.addEventListener('click', () => {
                setSidebarState(!isSidebarOpen);
            });
        }
        
        function handleResize() {
            if(window.innerWidth >= 768) {
                setSidebarState(true);
                openSidebar.style.display = 'none';
            } else {
                setSidebarState(false);
                openSidebar.style.display = 'block';
            }
        }
        
        window.addEventListener('resize', handleResize);
        document.addEventListener('DOMContentLoaded', handleResize);
        
        // Add Game Modal
        const addGameBtn = document.getElementById('addGameBtn');
        const addGameModal = document.getElementById('addGameModal');
        const closeAddGameModal = document.getElementById('closeAddGameModal');
        const cancelAddGame = document.getElementById('cancelAddGame');
        
        addGameBtn.addEventListener('click', () => {
            addGameModal.classList.remove('hidden');
        });
        
        closeAddGameModal.addEventListener('click', () => {
            addGameModal.classList.add('hidden');
        });
        
        cancelAddGame.addEventListener('click', () => {
            addGameModal.classList.add('hidden');
        });
        
        // Delete Game Modal
        const deleteModal = document.getElementById('deleteModal');
        const cancelDelete = document.getElementById('cancelDelete');
        const deleteButtons = document.querySelectorAll('.delete-game');
        const deleteGameTitle = document.getElementById('deleteGameTitle');
        const deleteGameId = document.getElementById('deleteGameId');
        
        deleteButtons.forEach(button => {
            button.addEventListener('click', (e) => {
                e.preventDefault();
                const gameId = button.getAttribute('data-id');
                const gameTitle = button.getAttribute('data-title');
                
                deleteGameId.value = gameId;
                deleteGameTitle.textContent = gameTitle;
                deleteModal.classList.remove('hidden');
            });
        });
        
        cancelDelete.addEventListener('click', () => {
            deleteModal.classList.add('hidden');
        });
        
        // Close modals when clicking outside
        window.addEventListener('click', (e) => {
            if (e.target === addGameModal) {
                addGameModal.classList.add('hidden');
            }
            if (e.target === deleteModal) {
                deleteModal.classList.add('hidden');
            }
        });
    </script>
</body>
</html>