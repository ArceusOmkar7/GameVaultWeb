<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="gamevaultbase.helpers.GameJspHelper" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Games</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* Notification styles */
        .notification {
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 20px;
            border-radius: 4px;
            display: flex;
            align-items: center;
            box-shadow: 0 3px 10px rgba(0,0,0,0.2);
            transform: translateX(120%);
            transition: transform 0.3s ease-in-out;
            z-index: 1000;
            max-width: 350px;
        }
        .notification.show {
            transform: translateX(0);
        }
        .notification-success {
            background-color: #10B981;
            color: white;
        }
        .notification-error {
            background-color: #EF4444;
            color: white;
        }
        .notification-warning {
            background-color: #F59E0B;
            color: white;
        }
        .notification-info {
            background-color: #3B82F6;
            color: white;
        }
    </style>
</head>
<body class="bg-gray-100">
    <jsp:include page="header.jsp" />
    
    <!-- Notification container -->
    <div id="notification-container"></div>
    <div class="container mx-auto px-4 py-8">
        <h2 class="text-3xl font-bold mb-6 text-gray-800">Browse Games</h2>
        <form action="${pageContext.request.contextPath}/browse" method="get" class="mb-6 flex flex-wrap gap-4 items-end">
            <input type="text" name="search" placeholder="Search games..." value="<c:out value='${searchQuery}'/>" class="border p-2 rounded w-64" />
            
            <div>
                <label for="filter-platform" class="block text-sm font-medium text-gray-700 mb-1">Platform</label>
                <select name="filter-platform" id="filter-platform" class="border p-2 rounded">
                    <option value="" ${empty selectedPlatform ? 'selected' : ''}>All Platforms</option>
                    <c:forEach var="platform" items="${platforms}">
                        <option value="${platform.name}" ${selectedPlatform == platform.name ? 'selected' : ''}>
                            <c:out value="${platform.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div>
                <label for="filter-genre" class="block text-sm font-medium text-gray-700 mb-1">Genre</label>
                <select name="filter-genre" id="filter-genre" class="border p-2 rounded">
                    <option value="" ${empty selectedGenre ? 'selected' : ''}>All Genres</option>
                    <c:forEach var="genre" items="${genres}">
                        <option value="${genre.name}" ${selectedGenre == genre.name ? 'selected' : ''}>
                            <c:out value="${genre.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div>
                <label for="sort" class="block text-sm font-medium text-gray-700 mb-1">Sort By</label>
                <select name="sort" id="sort" class="border p-2 rounded">
                    <option value="" ${empty selectedSort ? 'selected' : ''}>Relevance</option>
                    <option value="price_asc" ${selectedSort == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                    <option value="price_desc" ${selectedSort == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                    <option value="release_date" ${selectedSort == 'release_date' ? 'selected' : ''}>Release Date</option>
                </select>
            </div>
            
            <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded flex items-center gap-2">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M3 3a1 1 0 011-1h12a1 1 0 011 1v3a1 1 0 01-.293.707L12 11.414V15a1 1 0 01-.293.707l-2 2A1 1 0 018 17v-5.586L3.293 6.707A1 1 0 013 6V3z" clip-rule="evenodd" />
                </svg>
                Apply Filters
            </button>
        </form>
        <c:if test="${not empty errorMessage}">
            <div class="mb-4 p-4 rounded bg-red-100 border border-red-400 text-red-700">
                <p><strong>Error:</strong> <c:out value="${errorMessage}" /></p>
            </div>
        </c:if>
        <c:choose>
            <c:when test="${not empty gamesList}">
                <!-- Clean section above the game grid -->
                <div class="mb-6 p-4 bg-blue-50 border-l-4 border-blue-400 rounded">
                    <h3 class="text-xl font-semibold text-blue-800 mb-1">Discover Your Next Favorite Game</h3>
                    <p class="text-gray-700 text-sm">Browse our curated collection. Use the filters above to find games by platform, price, or release date.</p>
                </div>
                <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                    <c:forEach var="game" items="${gamesList}">
                        <div class="bg-white p-4 rounded shadow flex flex-col">
                            <c:choose>
                                <c:when test="${not empty game.imagePath}">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(game.imagePath, 'http')}">
                                            <img src="${game.imagePath}" alt="${game.title}" class="w-full h-48 object-cover rounded mb-2">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/${game.imagePath}" alt="${game.title}" class="w-full h-48 object-cover rounded mb-2">
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <div class="bg-gray-200 h-48 rounded mb-2 flex items-center justify-center text-gray-400 text-sm">No Image</div>
                                </c:otherwise>
                            </c:choose>
                            <h3 class="text-lg font-semibold mb-1">
                                <a href="${pageContext.request.contextPath}/game?id=${game.gameId}" class="text-blue-600 hover:underline">
                                    <c:out value="${game.title}"/>
                                </a>
                            </h3>
                            <p class="text-gray-600 mb-1">By <c:out value="${game.developer}"/></p>
                            <p class="text-gray-500 text-sm mb-2">Platform: <c:out value="${game.platform}"/></p>                            <p class="text-green-700 font-bold mb-2">
                                <fmt:formatNumber value="${game.price}" type="currency" currencySymbol="$" />
                            </p>
                            <c:if test="${not empty sessionScope.loggedInUser}">
                                <c:choose>
                                    <c:when test="${ownedGameIds.contains(game.gameId)}">
                                        <button type="button"
                                                class="w-full bg-gray-400 text-white font-bold py-2 px-4 rounded text-base cursor-not-allowed opacity-80 mt-2"
                                                disabled>
                                            Owned
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button"
                                                onclick="addToCart('${game.gameId}', '${fn:escapeXml(game.title)}', event)"
                                                class="w-full bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-base mt-2 add-to-cart-btn">
                                            Add to Cart
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>

                <!-- Pagination Controls -->
                <c:if test="${totalPages > 1}">
                    <div class="mt-8 flex justify-center items-center space-x-4">
                        <!-- Previous Page Button -->
                        <c:if test="${currentPage > 1}">
                            <a href="${pageContext.request.contextPath}/browse?page=${currentPage - 1}&search=${searchQuery}&filter-platform=${selectedPlatform}&filter-genre=${selectedGenre}&sort=${selectedSort}" 
                               class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
                                Previous
                            </a>
                        </c:if>

                        <!-- Page Numbers -->
                        <div class="flex space-x-2">
                            <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                <c:choose>
                                    <c:when test="${pageNum == currentPage}">
                                        <span class="px-4 py-2 border border-blue-500 rounded-md text-sm font-medium text-white bg-blue-500">
                                            ${pageNum}
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/browse?page=${pageNum}&search=${searchQuery}&filter-platform=${selectedPlatform}&filter-genre=${selectedGenre}&sort=${selectedSort}"
                                           class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
                                            ${pageNum}
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>

                        <!-- Next Page Button -->
                        <c:if test="${currentPage < totalPages}">
                            <a href="${pageContext.request.contextPath}/browse?page=${currentPage + 1}&search=${searchQuery}&filter-platform=${selectedPlatform}&filter-genre=${selectedGenre}&sort=${selectedSort}"
                               class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
                                Next
                            </a>
                        </c:if>
                    </div>

                    <!-- Page Info -->
                    <div class="mt-4 text-center text-sm text-gray-500">
                        Showing ${(currentPage - 1) * pageSize + 1} to ${Math.min(currentPage * pageSize, totalGames)} of ${totalGames} games
                    </div>
                </c:if>
            </c:when>
            <c:otherwise>
                <p class="text-gray-500">No games found.</p>
            </c:otherwise>
        </c:choose>    </div>
    <jsp:include page="footer.jsp" />
    
    <!-- JavaScript for AJAX and notifications -->
    <script>
        // Function to create and show notifications
        function showNotification(message, type) {
            const container = document.getElementById('notification-container');
            const notification = document.createElement('div');
            notification.className = `notification notification-${type} fixed top-4 right-4 p-4 rounded-lg shadow-lg z-50 transform transition-all duration-300 ease-in-out opacity-0 translate-y-[-1rem]`;
            
            // Set background color based on type
            switch(type) {
                case 'success':
                    notification.classList.add('bg-green-100', 'border-green-400', 'text-green-700');
                    break;
                case 'error':
                    notification.classList.add('bg-red-100', 'border-red-400', 'text-red-700');
                    break;
                case 'warning':
                    notification.classList.add('bg-yellow-100', 'border-yellow-400', 'text-yellow-700');
                    break;
                case 'info':
                    notification.classList.add('bg-blue-100', 'border-blue-400', 'text-blue-700');
                    break;
                default:
                    notification.classList.add('bg-blue-100', 'border-blue-400', 'text-blue-700');
            }
            
            notification.innerHTML = message;
            
            container.appendChild(notification);
            
            // Show the notification after a small delay to allow CSS transition
            setTimeout(() => {
                notification.classList.remove('opacity-0', 'translate-y-[-1rem]');
                notification.classList.add('opacity-100', 'translate-y-0');
            }, 10);
            
            // Automatically close the notification after 4 seconds
            setTimeout(() => {
                notification.classList.remove('opacity-100', 'translate-y-0');
                notification.classList.add('opacity-0', 'translate-y-[-1rem]');
                // Remove from DOM after animation completes
                setTimeout(() => {
                    container.removeChild(notification);
                }, 300);
            }, 4000);
        }
        
        // Function to handle the "Add to Cart" action
        function addToCart(gameId, gameTitle, event) {
            event.target.disabled = true;
            const formData = new URLSearchParams();
            formData.append('gameId', gameId);
            fetch('${pageContext.request.contextPath}/addToCart', {
                method: 'POST',
                body: formData,
                headers: {
                    'X-Requested-With': 'XMLHttpRequest',
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    showNotification(`<strong>Success!</strong> ${data.message}`, 'success');
                } else if (data.message && data.message.toLowerCase().includes('already in your cart')) {
                    showNotification(`<div style='display:flex;align-items:center;font-weight:bold;font-size:1.1em;'><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" width="22" height="22" style="margin-right:8px;"><path d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM216 336l24 0 0-64-24 0c-13.3 0-24-10.7-24-24s10.7-24 24-24l48 0c13.3 0 24 10.7 24 24l0 88 8 0c13.3 0 24 10.7 24 24s-10.7 24-24 24l-80 0c-13.3 0-24-10.7-24-24s10.7-24 24-24zm40-208a32 32 0 1 1 0 64 32 32 0 1 1 0-64z"/></svg>Game Already in Cart</div>`, 'info');
                } else if (data.message && data.message.toLowerCase().includes('already own')) {
                    showNotification(`<div style='display:flex;align-items:center;font-weight:bold;font-size:1.1em;'><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" width="22" height="22" style="margin-right:8px;"><path d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM216 336l24 0 0-64-24 0c-13.3 0-24-10.7-24-24s10.7-24 24-24l48 0c13.3 0 24 10.7 24 24l0 88 8 0c13.3 0 24 10.7 24 24s-10.7 24-24 24l-80 0c-13.3 0-24-10.7-24-24s10.7-24 24-24zm40-208a32 32 0 1 1 0 64 32 32 0 1 1 0-64z"/></svg>Game Already Owned</div>`, 'info');
                } else {
                    showNotification(`<strong>Notice:</strong> ${data.message}`, 'info');
                }
                event.target.disabled = false;
            })
            .catch(error => {
                console.error('Error:', error);
                showNotification('<strong>Notice:</strong> Unable to add game to cart. Please try again.', 'info');
                event.target.disabled = false;
            });
        }
        
        // Check if there's a message in the URL parameters (for backward compatibility)
        document.addEventListener('DOMContentLoaded', function() {
            const urlParams = new URLSearchParams(window.location.search);
            const message = urlParams.get('message');
            const messageType = urlParams.get('messageType') || 'info';
            
            if (message) {
                showNotification(`<strong>${messageType.charAt(0).toUpperCase() + messageType.slice(1)}:</strong> ${message}`, messageType);
                
                // Clean up the URL to remove the message parameters
                const newUrl = window.location.pathname + 
                    (window.location.search.replace(/[?&]message=[^&]*/, '').replace(/[?&]messageType=[^&]*/, '').replace(/^\?$/, ''));
                window.history.replaceState({}, document.title, newUrl);
            }
        });
    </script>
</body>
</html>
