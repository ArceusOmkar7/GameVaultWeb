<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Your Digital Gaming Paradise</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <style>
        .game-card {
            background: rgba(17, 24, 39, 0.8);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(99, 102, 241, 0.2);
            transition: all 0.4s ease;
        }

        .game-card:hover {
            transform: translateY(-8px) scale(1.02);
            border-color: rgba(99, 102, 241, 0.8);
            box-shadow: 0 8px 25px rgba(99, 102, 241, 0.2);
        }

        .neon-text {
            text-shadow: 0 0 10px rgba(99, 102, 241, 0.8);
        }

        .category-card {
            background: rgba(17, 24, 39, 0.9);
            backdrop-filter: blur(8px);
            border: 1px solid rgba(99, 102, 241, 0.3);
            transition: all 0.3s ease;
        }

        .category-card:hover {
            transform: translateY(-5px);
            border-color: rgba(99, 102, 241, 0.8);
            box-shadow: 0 5px 15px rgba(99, 102, 241, 0.3);
        }

        .price-badge {
            background: linear-gradient(45deg, #4f46e5, #7c3aed);
            padding: 0.5rem 1rem;
            border-radius: 9999px;
            font-weight: 600;
            color: white;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3);
        }

        .custom-scroll::-webkit-scrollbar {
            width: 6px;
            height: 6px;
        }

        .custom-scroll::-webkit-scrollbar-thumb {
            background: #4f46e5;
            border-radius: 3px;
        }

        .custom-scroll::-webkit-scrollbar-track {
            background: rgba(17, 24, 39, 0.8);
        }

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
<body class="bg-gray-900 text-white min-h-screen flex flex-col custom-scroll">
    <div class="fixed inset-0 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-gray-900 via-gray-900 to-indigo-900 -z-10"></div>
    
    <jsp:include page="header.jsp" />
    
    <!-- Notification container -->
    <div id="notification-container"></div>

    <main class="flex-grow pt-20">
        <!-- Hero Section -->
        <div class="relative overflow-hidden mb-16">
            <div class="absolute inset-0 bg-gradient-to-b from-indigo-900/20 to-gray-900"></div>
            <div class="container mx-auto px-4 py-16 relative">
                <div class="text-center mb-12">
                    <h1 class="text-7xl font-bold mb-6 bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400 inline-block neon-text">
                        GameVault
                    </h1>
                    <p class="text-xl text-gray-300 mb-8 max-w-2xl mx-auto">
                        Your portal to endless gaming adventures. Discover, collect, and play the best digital games.
                    </p>
                    <div class="flex justify-center gap-4">
                        <a href="#featured" class="px-8 py-3 rounded-lg bg-indigo-600 hover:bg-indigo-700 transition-all">
                            Explore Games
                        </a>
                        <c:if test="${empty sessionScope.loggedInUser}">
                            <a href="${pageContext.request.contextPath}/register" class="px-8 py-3 rounded-lg bg-gray-800 hover:bg-gray-700 transition-all border border-indigo-500/30">
                                Join Now
                            </a>
                        </c:if>
                    </div>
                </div>

                <!-- Categories Section -->
                <div class="grid grid-cols-2 md:grid-cols-3 gap-4 mb-16">
                    <a href="#action-games" class="category-card p-6 rounded-xl text-center transition-all hover:bg-gray-800/50">
                        <i class="bi bi-controller text-4xl mb-3 text-indigo-400"></i>
                        <h3 class="font-semibold">Action</h3>
                        <p class="text-sm text-gray-400">Thrilling adventures</p>
                    </a>
                    <a href="#strategy-games" class="category-card p-6 rounded-xl text-center transition-all hover:bg-gray-800/50">
                        <i class="bi bi-puzzle text-4xl mb-3 text-purple-400"></i>
                        <h3 class="font-semibold">Strategy</h3>
                        <p class="text-sm text-gray-400">Test your mind</p>
                    </a>
                    <a href="#rpg-games" class="category-card p-6 rounded-xl text-center transition-all hover:bg-gray-800/50">
                        <i class="bi bi-heart text-4xl mb-3 text-pink-400"></i>
                        <h3 class="font-semibold">RPG</h3>
                        <p class="text-sm text-gray-400">Epic stories</p>
                    </a>
                </div>
            </div>
        </div>

        <!-- Featured Games Section -->
        <section id="featured" class="container mx-auto px-4 mb-16">
            <div class="flex justify-between items-center mb-8">
                <h2 class="text-3xl font-bold">Featured Games</h2>
                <div class="flex gap-4">
                    <button class="scroll-left-featured px-4 py-2 rounded-lg bg-gray-800 hover:bg-gray-700">
                        <i class="bi bi-chevron-left"></i>
                    </button>
                    <button class="scroll-right-featured px-4 py-2 rounded-lg bg-gray-800 hover:bg-gray-700">
                        <i class="bi bi-chevron-right"></i>
                    </button>
                </div>
            </div>

            <div class="featured-games-scroll overflow-x-auto custom-scroll">
                <div class="flex gap-6 pb-4" style="width: max-content;">
                    <c:forEach var="game" items="${featuredGamesList}" varStatus="status">
                        <div class="game-card w-72 rounded-xl overflow-hidden">
                            <div class="relative">
                                <c:choose>
                                    <c:when test="${not empty game.imagePath}">
                                        <img src="${fn:startsWith(game.imagePath, 'http') ? game.imagePath : pageContext.request.contextPath.concat('/').concat(game.imagePath)}"
                                             alt="${game.title}"
                                             class="w-full h-40 object-cover">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="w-full h-40 bg-gray-800 flex items-center justify-center">
                                            <i class="bi bi-controller text-4xl text-gray-600"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                <div class="absolute top-2 right-2">
                                    <span class="price-badge">
                                        $<fmt:formatNumber value="${game.price}" pattern="#,##0.00" />
                                    </span>
                                </div>
                            </div>
                            <div class="p-4">
                                <h3 class="font-semibold mb-2 line-clamp-1">${game.title}</h3>
                                <div class="flex flex-wrap gap-2 mb-3">
                                    <c:forEach var="genre" items="${game.genre}" end="2">
                                        <span class="text-xs px-2 py-1 rounded-full bg-gray-800 text-gray-300">
                                            ${genre}
                                        </span>
                                    </c:forEach>
                                </div>
                                <div class="flex justify-between items-center">
                                    <a href="${pageContext.request.contextPath}/game?id=${game.gameId}"
                                       class="text-sm bg-indigo-600 hover:bg-indigo-700 px-4 py-2 rounded-lg transition-all">
                                        View Details
                                    </a>
                                    <c:if test="${not empty sessionScope.loggedInUser}">
                                        <form action="${pageContext.request.contextPath}/addToCart" method="post" class="inline">
                                            <input type="hidden" name="gameId" value="${game.gameId}" />
                                            <button type="submit" class="text-indigo-400 hover:text-indigo-300 transition-colors">
                                                <i class="bi bi-cart-plus text-xl"></i>
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- Action Games Section -->
        <section id="action-games" class="container mx-auto px-4 mb-16">
            <div class="flex justify-between items-center mb-8">
                <h2 class="text-3xl font-bold">Action Games</h2>
                <div class="flex gap-4">
                    <button class="scroll-left-action px-4 py-2 rounded-lg bg-gray-800 hover:bg-gray-700">
                        <i class="bi bi-chevron-left"></i>
                    </button>
                    <button class="scroll-right-action px-4 py-2 rounded-lg bg-gray-800 hover:bg-gray-700">
                        <i class="bi bi-chevron-right"></i>
                    </button>
                </div>
            </div>
            
            <div class="action-games-scroll overflow-x-auto custom-scroll">
                <div class="flex gap-6 pb-4" style="width: max-content;">
                    <c:forEach var="game" items="${actionGames}" varStatus="status">
                        <div class="game-card w-72 rounded-xl overflow-hidden">
                            <div class="relative">
                                <c:choose>
                                    <c:when test="${not empty game.imagePath}">
                                        <img src="${fn:startsWith(game.imagePath, 'http') ? game.imagePath : pageContext.request.contextPath.concat('/').concat(game.imagePath)}"
                                             alt="${game.title}"
                                             class="w-full h-40 object-cover">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="w-full h-40 bg-gray-800 flex items-center justify-center">
                                            <i class="bi bi-controller text-4xl text-gray-600"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                <div class="absolute top-2 right-2">
                                    <span class="price-badge">
                                        $<fmt:formatNumber value="${game.price}" pattern="#,##0.00" />
                                    </span>
                                </div>
                            </div>
                            <div class="p-4">
                                <h3 class="font-semibold mb-2 line-clamp-1">${game.title}</h3>
                                <div class="flex flex-wrap gap-2 mb-3">
                                    <c:forEach var="genre" items="${game.genre}" end="2">
                                        <span class="text-xs px-2 py-1 rounded-full bg-gray-800 text-gray-300">
                                            ${genre}
                                        </span>
                                    </c:forEach>
                                </div>
                                <div class="flex justify-between items-center">
                                    <a href="${pageContext.request.contextPath}/game?id=${game.gameId}"
                                       class="text-sm bg-indigo-600 hover:bg-indigo-700 px-4 py-2 rounded-lg transition-all">
                                        View Details
                                    </a>
                                    <c:if test="${not empty sessionScope.loggedInUser}">
                                        <form action="${pageContext.request.contextPath}/addToCart" method="post" class="inline">
                                            <input type="hidden" name="gameId" value="${game.gameId}" />
                                            <button type="submit" class="text-indigo-400 hover:text-indigo-300 transition-colors">
                                                <i class="bi bi-cart-plus text-xl"></i>
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- Strategy Games Section -->
        <section id="strategy-games" class="container mx-auto px-4 mb-16">
            <div class="flex justify-between items-center mb-8">
                <h2 class="text-3xl font-bold">Strategy Games</h2>
                <div class="flex gap-4">
                    <button class="scroll-left-strategy px-4 py-2 rounded-lg bg-gray-800 hover:bg-gray-700">
                        <i class="bi bi-chevron-left"></i>
                    </button>
                    <button class="scroll-right-strategy px-4 py-2 rounded-lg bg-gray-800 hover:bg-gray-700">
                        <i class="bi bi-chevron-right"></i>
                    </button>
                </div>
            </div>
            
            <div class="strategy-games-scroll overflow-x-auto custom-scroll">
                <div class="flex gap-6 pb-4" style="width: max-content;">
                    <c:forEach var="game" items="${strategyGames}" varStatus="status">
                        <div class="game-card w-72 rounded-xl overflow-hidden">
                            <div class="relative">
                                <c:choose>
                                    <c:when test="${not empty game.imagePath}">
                                        <img src="${fn:startsWith(game.imagePath, 'http') ? game.imagePath : pageContext.request.contextPath.concat('/').concat(game.imagePath)}"
                                             alt="${game.title}"
                                             class="w-full h-40 object-cover">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="w-full h-40 bg-gray-800 flex items-center justify-center">
                                            <i class="bi bi-controller text-4xl text-gray-600"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                <div class="absolute top-2 right-2">
                                    <span class="price-badge">
                                        $<fmt:formatNumber value="${game.price}" pattern="#,##0.00" />
                                    </span>
                                </div>
                            </div>
                            <div class="p-4">
                                <h3 class="font-semibold mb-2 line-clamp-1">${game.title}</h3>
                                <div class="flex flex-wrap gap-2 mb-3">
                                    <c:forEach var="genre" items="${game.genre}" end="2">
                                        <span class="text-xs px-2 py-1 rounded-full bg-gray-800 text-gray-300">
                                            ${genre}
                                        </span>
                                    </c:forEach>
                                </div>
                                <div class="flex justify-between items-center">
                                    <a href="${pageContext.request.contextPath}/game?id=${game.gameId}"
                                       class="text-sm bg-indigo-600 hover:bg-indigo-700 px-4 py-2 rounded-lg transition-all">
                                        View Details
                                    </a>
                                    <c:if test="${not empty sessionScope.loggedInUser}">
                                        <form action="${pageContext.request.contextPath}/addToCart" method="post" class="inline">
                                            <input type="hidden" name="gameId" value="${game.gameId}" />
                                            <button type="submit" class="text-indigo-400 hover:text-indigo-300 transition-colors">
                                                <i class="bi bi-cart-plus text-xl"></i>
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- RPG Games Section -->
        <section id="rpg-games" class="container mx-auto px-4 mb-16">
            <div class="flex justify-between items-center mb-8">
                <h2 class="text-3xl font-bold">RPG Games</h2>
                <div class="flex gap-4">
                    <button class="scroll-left-rpg px-4 py-2 rounded-lg bg-gray-800 hover:bg-gray-700">
                        <i class="bi bi-chevron-left"></i>
                    </button>
                    <button class="scroll-right-rpg px-4 py-2 rounded-lg bg-gray-800 hover:bg-gray-700">
                        <i class="bi bi-chevron-right"></i>
                    </button>
                </div>
            </div>
            
            <div class="rpg-games-scroll overflow-x-auto custom-scroll">
                <div class="flex gap-6 pb-4" style="width: max-content;">
                    <c:forEach var="game" items="${rpgGames}" varStatus="status">
                        <div class="game-card w-72 rounded-xl overflow-hidden">
                            <div class="relative">
                                <c:choose>
                                    <c:when test="${not empty game.imagePath}">
                                        <img src="${fn:startsWith(game.imagePath, 'http') ? game.imagePath : pageContext.request.contextPath.concat('/').concat(game.imagePath)}"
                                             alt="${game.title}"
                                             class="w-full h-40 object-cover">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="w-full h-40 bg-gray-800 flex items-center justify-center">
                                            <i class="bi bi-controller text-4xl text-gray-600"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                <div class="absolute top-2 right-2">
                                    <span class="price-badge">
                                        $<fmt:formatNumber value="${game.price}" pattern="#,##0.00" />
                                    </span>
                                </div>
                            </div>
                            <div class="p-4">
                                <h3 class="font-semibold mb-2 line-clamp-1">${game.title}</h3>
                                <div class="flex flex-wrap gap-2 mb-3">
                                    <c:forEach var="genre" items="${game.genre}" end="2">
                                        <span class="text-xs px-2 py-1 rounded-full bg-gray-800 text-gray-300">
                                            ${genre}
                                        </span>
                                    </c:forEach>
                                </div>
                                <div class="flex justify-between items-center">
                                    <a href="${pageContext.request.contextPath}/game?id=${game.gameId}"
                                       class="text-sm bg-indigo-600 hover:bg-indigo-700 px-4 py-2 rounded-lg transition-all">
                                        View Details
                                    </a>
                                    <c:if test="${not empty sessionScope.loggedInUser}">
                                        <form action="${pageContext.request.contextPath}/addToCart" method="post" class="inline">
                                            <input type="hidden" name="gameId" value="${game.gameId}" />
                                            <button type="submit" class="text-indigo-400 hover:text-indigo-300 transition-colors">
                                                <i class="bi bi-cart-plus text-xl"></i>
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>
    </main>

    <jsp:include page="footer.jsp" />

    <script>
        // Function to handle horizontal scrolling
        function setupScrollButtons(containerClass, leftButtonClass, rightButtonClass) {
            const container = document.querySelector(`.${containerClass}`);
            const leftButton = document.querySelector(`.${leftButtonClass}`);
            const rightButton = document.querySelector(`.${rightButtonClass}`);
            
            if (!container || !leftButton || !rightButton) return;
            
            const scrollAmount = 300;
            
            leftButton.addEventListener('click', () => {
                container.scrollBy({
                    left: -scrollAmount,
                    behavior: 'smooth'
                });
            });
            
            rightButton.addEventListener('click', () => {
                container.scrollBy({
                    left: scrollAmount,
                    behavior: 'smooth'
                });
            });
        }

        // Setup scroll buttons for each section
        document.addEventListener('DOMContentLoaded', () => {
            setupScrollButtons('featured-games-scroll', 'scroll-left-featured', 'scroll-right-featured');
            setupScrollButtons('action-games-scroll', 'scroll-left-action', 'scroll-right-action');
            setupScrollButtons('strategy-games-scroll', 'scroll-left-strategy', 'scroll-right-strategy');
            setupScrollButtons('rpg-games-scroll', 'scroll-left-rpg', 'scroll-right-rpg');
        });
    </script>
</body>
</html>