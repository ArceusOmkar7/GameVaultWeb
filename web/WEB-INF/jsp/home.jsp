<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Home</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <style>
        .main-featured-game {
            transition: transform 0.3s ease;
            aspect-ratio: 16/9;
        }
        
        .main-featured-game:hover {
            transform: scale(1.01);
        }
        
        .featured-side-game {
            transition: transform 0.2s ease;
        }
        
        .featured-side-game:hover {
            transform: translateY(-3px);
        }
        
        .bottom-game-card {
            transition: all 0.2s ease;
        }
        
        .bottom-game-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
        }
        
        .dark-gradient {
            background: linear-gradient(to top, rgba(0,0,0,0.8) 0%, rgba(0,0,0,0.4) 40%, rgba(0,0,0,0) 100%);
        }
    </style>
</head>
<body class="bg-gray-900 text-white pt-20">

    <jsp:include page="header.jsp" />

    <div class="container mx-auto px-4 py-8">
        <%-- Messages (keep as is) --%>
        <c:if test="${not empty message}">
            <div class="mb-4 p-4 rounded ${messageType eq 'success' ? 'bg-green-100 border border-green-400 text-green-700' : 'bg-red-100 border border-red-400 text-red-700'}" role="alert">
                <p><c:out value="${message}" /></p>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="mb-4 p-4 rounded bg-red-100 border border-red-400 text-red-700" role="alert">
                <p><strong>Error:</strong> <c:out value="${errorMessage}" /></p>
            </div>
        </c:if>

        <!-- Hero Banner with Current Date -->
        <div class="mb-6 text-center">
            <h1 class="text-4xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-purple-600 inline-block">
                GameVault
            </h1>
            <p class="text-gray-400 mt-2">
                <span class="bg-blue-900 px-3 py-1 rounded-full text-xs">
                    <fmt:formatDate value="<%= new java.util.Date() %>" pattern="MMMM d, yyyy" />
                </span>
            </p>
        </div>

        <!-- Main Content with Epic Games Layout -->
        <div class="flex flex-col lg:flex-row gap-6">
            <!-- Main Featured Games Section (Left/Center) -->
            <div class="w-full lg:w-8/12">
                <c:choose>
                    <c:when test="${not empty mainFeaturedGame}">
                        <!-- Main Featured Game -->
                        <div class="relative mb-6 rounded-lg overflow-hidden main-featured-game group">
                            <c:choose>
                                <c:when test="${not empty mainFeaturedGame.imagePath}">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(mainFeaturedGame.imagePath, 'http')}">
                                            <img src="${mainFeaturedGame.imagePath}" 
                                                alt="${mainFeaturedGame.title}" 
                                                class="w-full object-cover rounded-lg" style="height: 500px;">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/${mainFeaturedGame.imagePath}" 
                                                alt="${mainFeaturedGame.title}" 
                                                class="w-full object-cover rounded-lg" style="height: 500px;">
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <div class="bg-gray-800 rounded-lg flex items-center justify-center text-gray-500 text-2xl" style="height: 500px;">
                                        No Image Available for ${mainFeaturedGame.title}
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            
                            <!-- Gradient Overlay -->
                            <div class="absolute inset-0 dark-gradient"></div>
                            
                            <!-- Game Info Overlay -->
                            <div class="absolute bottom-0 left-0 p-6 w-full">
                                <div class="flex flex-col gap-2">
                                    <div class="flex items-center gap-2">
                                        <span class="bg-blue-600 text-xs px-2 py-1 rounded font-medium">Featured</span>
                                        <span class="bg-gray-800 text-xs px-2 py-1 rounded font-medium">
                                            <c:forEach var="genre" items="${mainFeaturedGame.genre}" varStatus="genreStatus">
                                                ${genre}<c:if test="${!genreStatus.last}">, </c:if>
                                            </c:forEach>
                                        </span>
                                    </div>
                                    <h2 class="text-3xl font-bold text-white">${mainFeaturedGame.title}</h2>
                                    <p class="text-gray-300 line-clamp-2 text-sm mb-2">${mainFeaturedGame.description}</p>
                                    <div class="flex items-center gap-3 mt-2">
                                        <a href="${pageContext.request.contextPath}/game?id=${mainFeaturedGame.gameId}" 
                                           class="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md transition-all">
                                            View Details
                                        </a>
                                        <c:if test="${not empty sessionScope.loggedInUser}">
                                            <form action="${pageContext.request.contextPath}/addToCart" method="post" class="inline">
                                                <input type="hidden" name="gameId" value="${mainFeaturedGame.gameId}" />
                                                <button type="submit" class="bg-gray-800 hover:bg-gray-700 text-white font-medium py-2 px-4 rounded-md transition-all flex items-center gap-2">
                                                    <i class="bi bi-cart-plus"></i> $<fmt:formatNumber value="${mainFeaturedGame.price}" pattern="#,##0.00" />
                                                </button>
                                            </form>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="bg-gray-800 p-6 rounded-lg text-center mb-6">
                            <p class="text-gray-400">No featured game available.</p>
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- Secondary Featured Games (Carousel Replacement) -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                    <c:forEach var="featuredGame" items="${featuredGamesList}" begin="0" end="3" varStatus="status">
                        <div class="relative rounded-lg overflow-hidden featured-side-game group">
                            <c:choose>
                                <c:when test="${not empty featuredGame.imagePath}">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(featuredGame.imagePath, 'http')}">
                                            <img src="${featuredGame.imagePath}" 
                                                alt="${featuredGame.title}" 
                                                class="w-full h-48 object-cover">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/${featuredGame.imagePath}" 
                                                alt="${featuredGame.title}" 
                                                class="w-full h-48 object-cover">
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <div class="bg-gray-800 h-48 w-full flex items-center justify-center">
                                        <span class="text-gray-500">No Image</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            
                            <!-- Gradient Overlay -->
                            <div class="absolute inset-0 dark-gradient opacity-70"></div>
                            
                            <!-- Game Info -->
                            <div class="absolute bottom-0 left-0 p-3 w-full">
                                <h3 class="text-lg font-semibold text-white line-clamp-1">${featuredGame.title}</h3>
                                <div class="flex justify-between items-center mt-1">
                                    <span class="text-gray-300 text-sm">$<fmt:formatNumber value="${featuredGame.price}" pattern="#,##0.00" /></span>
                                    <a href="${pageContext.request.contextPath}/game?id=${featuredGame.gameId}" class="text-xs bg-blue-600 hover:bg-blue-700 px-2 py-1 rounded text-white">
                                        View
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Side Panel with Featured Games (Right) -->
            <div class="w-full lg:w-4/12">
                <div class="bg-gray-800 rounded-lg p-4 mb-6">
                    <h2 class="text-xl font-bold mb-4 pb-2 border-b border-gray-700">Featured Games</h2>
                    
                    <div class="space-y-4">
                        <c:forEach var="featuredGame" items="${featuredGamesList}" begin="4" end="9" varStatus="status">
                            <div class="flex gap-3 featured-side-game hover:bg-gray-700 p-2 rounded-lg">
                                <c:choose>
                                    <c:when test="${not empty featuredGame.imagePath}">
                                        <c:choose>
                                            <c:when test="${fn:startsWith(featuredGame.imagePath, 'http')}">
                                                <img src="${featuredGame.imagePath}" 
                                                    alt="${featuredGame.title}" 
                                                    class="w-24 h-16 object-cover rounded">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/${featuredGame.imagePath}" 
                                                    alt="${featuredGame.title}" 
                                                    class="w-24 h-16 object-cover rounded">
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="bg-gray-700 w-24 h-16 rounded flex items-center justify-center">
                                            <span class="text-gray-500 text-xs">No Image</span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                
                                <div class="flex-1"></div>
                                    <a href="${pageContext.request.contextPath}/game?id=${featuredGame.gameId}" class="text-white hover:text-blue-400">
                                        <h3 class="font-medium line-clamp-1">${featuredGame.title}</h3>
                                    </a>
                                    <p class="text-blue-400 text-sm">$<fmt:formatNumber value="${featuredGame.price}" pattern="#,##0.00" /></p>
                                    <c:if test="${not empty sessionScope.loggedInUser}">
                                        <form action="${pageContext.request.contextPath}/addToCart" method="post" class="mt-1">
                                            <input type="hidden" name="gameId" value="${featuredGame.gameId}" />
                                            <button type="submit" class="text-xs text-white bg-blue-600 hover:bg-blue-700 rounded px-2 py-1">
                                                Add to Cart
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                
                <!-- Deals Widget -->
                <div class="bg-gradient-to-r from-purple-800 to-blue-700 rounded-lg p-4 text-center">
                    <h3 class="text-lg font-bold mb-2">May Deals</h3>
                    <p class="text-sm mb-3">Special discounts for a limited time!</p>
                    <a href="${pageContext.request.contextPath}/browse?filter=deals" class="inline-block bg-white text-blue-900 font-bold py-2 px-4 rounded-lg">
                        Browse Deals
                    </a>
                </div>
            </div>
        </div>

        <!-- Bottom Game Catalog Section -->
        <div class="mt-10">
            <div class="flex justify-between items-center mb-6">
                <h2 class="text-2xl font-bold">Latest Games</h2>
                <a href="${pageContext.request.contextPath}/browse" class="text-blue-400 hover:text-blue-300 flex items-center gap-1">
                    View All <i class="bi bi-arrow-right"></i>
                </a>
            </div>
            
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-5">
                <c:forEach var="game" items="${allGamesList}" begin="0" end="9">
                    <div class="bg-gray-800 rounded-lg overflow-hidden bottom-game-card">
                        <a href="${pageContext.request.contextPath}/game?id=${game.gameId}">
                            <c:choose>
                                <c:when test="${not empty game.imagePath}">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(game.imagePath, 'http')}">
                                            <img src="${game.imagePath}" 
                                                alt="${game.title}" 
                                                class="w-full h-44 object-cover">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/${game.imagePath}" 
                                                alt="${game.title}" 
                                                class="w-full h-44 object-cover">
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <div class="bg-gray-700 h-44 flex items-center justify-center">
                                        <span class="text-gray-500">No Image</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </a>
                        
                        <div class="p-3">
                            <a href="${pageContext.request.contextPath}/game?id=${game.gameId}" class="hover:text-blue-400">
                                <h3 class="font-semibold text-sm mb-1 line-clamp-1">${game.title}</h3>
                            </a>
                            
                            <div class="flex items-center justify-between">
                                <span class="text-blue-400 font-medium">$<fmt:formatNumber value="${game.price}" pattern="#,##0.00" /></span>
                                
                                <c:if test="${not empty sessionScope.loggedInUser}">
                                    <form action="${pageContext.request.contextPath}/addToCart" method="post">
                                        <input type="hidden" name="gameId" value="${game.gameId}" />
                                        <button type="submit" class="text-xs bg-gray-700 hover:bg-gray-600 rounded-full p-1" title="Add to Cart">
                                            <i class="bi bi-cart-plus"></i>
                                        </button>
                                    </form>
                                </c:if>
                            </div>
                            
                            <div class="flex flex-wrap gap-1 mt-2">
                                <c:forEach var="platform" items="${game.platform}" varStatus="platformStatus" end="1">
                                    <span class="text-gray-400 text-xs bg-gray-700 px-1.5 py-0.5 rounded">
                                        ${platform}
                                    </span>
                                </c:forEach>
                                <c:if test="${fn:length(game.platform) > 2}">
                                    <span class="text-gray-400 text-xs bg-gray-700 px-1.5 py-0.5 rounded">
                                        +${fn:length(game.platform) - 2}
                                    </span>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            
            <div class="mt-8 text-center">
                <a href="${pageContext.request.contextPath}/browse" class="inline-block bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-6 rounded-lg transition-all">
                    Browse All Games
                </a>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
    
    <script>
        // You could add any interactive JS here if needed
        document.addEventListener('DOMContentLoaded', function() {
            console.log('Home page loaded');
        });
    </script>
</body>
</html>
``` 