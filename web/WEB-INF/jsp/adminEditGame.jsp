<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Edit Game</title>
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
            <h1 class="text-3xl font-bold text-gray-800">Edit Game</h1>
            <a href="${pageContext.request.contextPath}/admin/game-management" class="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded font-semibold flex items-center gap-2">
                <i class="bi bi-arrow-left"></i> Back to Games
            </a>
        </div>

        <!-- Edit Game Form -->
        <div class="bg-white rounded-lg shadow-lg p-6">
            <form id="editGameForm" action="${pageContext.request.contextPath}/admin/edit-game" method="post">
                <input type="hidden" name="gameId" value="${game.gameId}">
                
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div class="col-span-2">
                        <label for="title" class="block text-sm font-medium text-gray-700">Title</label>
                        <input type="text" name="title" id="title" value="${game.title}" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" required>
                    </div>
                    
                    <div class="col-span-2">
                        <label for="description" class="block text-sm font-medium text-gray-700">Description</label>
                        <textarea name="description" id="description" rows="3" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md">${game.description}</textarea>
                    </div>
                    
                    <div>
                        <label for="developer" class="block text-sm font-medium text-gray-700">Developer</label>
                        <input type="text" name="developer" id="developer" value="${game.developer}" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md">
                    </div>
                    
                    <div>
                        <label for="price" class="block text-sm font-medium text-gray-700">Price</label>
                        <div class="mt-1 relative rounded-md shadow-sm">
                            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                <span class="text-gray-500 sm:text-sm">$</span>
                            </div>
                            <input type="number" step="0.01" min="0" name="price" id="price" value="${game.price}" class="focus:ring-blue-500 focus:border-blue-500 block w-full pl-7 pr-12 sm:text-sm border-gray-300 rounded-md" placeholder="0.00" required>
                        </div>
                    </div>
                    
                    <div>
                        <label for="platform" class="block text-sm font-medium text-gray-700">Platform</label>
                        <input type="text" name="platform" id="platform" value="${game.platform}" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" placeholder="PC, PlayStation 5, Xbox Series X/S">
                    </div>
                    
                    <div>
                        <label for="genre" class="block text-sm font-medium text-gray-700">Genre</label>
                        <input type="text" name="genre" id="genre" value="${game.genre}" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" placeholder="Action, Adventure, RPG">
                    </div>
                    
                    <div>
                        <label for="releaseDate" class="block text-sm font-medium text-gray-700">Release Date</label>
                        <fmt:formatDate value="${game.releaseDate}" pattern="yyyy-MM-dd" var="formattedReleaseDate" />
                        <input type="date" name="releaseDate" id="releaseDate" value="${formattedReleaseDate}" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md">
                    </div>
                    
                    <div>
                        <label for="rating" class="block text-sm font-medium text-gray-700">Rating</label>
                        <input type="number" step="0.1" min="0" max="5" name="rating" id="rating" value="${game.rating}" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" placeholder="0.0-5.0">
                    </div>
                    
                    <div class="col-span-2">
                        <label for="imagePath" class="block text-sm font-medium text-gray-700">Image Path</label>
                        <input type="text" name="imagePath" id="imagePath" value="${game.imagePath}" class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" placeholder="URL or path to image">
                        <p class="mt-1 text-xs text-gray-500">Enter a URL or upload an image separately via the image upload tool</p>
                    </div>

                    <div class="col-span-2">
                        <div class="flex items-center space-x-2 mt-4">
                            <c:if test="${not empty game.imagePath}">
                                <div class="bg-gray-100 p-2 rounded">
                                    <p class="text-sm font-medium text-gray-700 mb-1">Current Image:</p>
                                    <img src="${game.getImagePathOrDefault()}" alt="${game.title}" class="h-20 w-auto object-cover rounded">
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
                
                <div class="mt-6 flex justify-end">
                    <a href="${pageContext.request.contextPath}/admin/game-management" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 mr-3">
                        Cancel
                    </a>
                    <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                        Save Changes
                    </button>
                </div>
            </form>
        </div>
    </div>

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
    </script>
</body>
</html>