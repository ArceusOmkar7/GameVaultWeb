<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Games</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
    <jsp:include page="header.jsp" />
    <div class="container mx-auto px-4 py-8">
        <h2 class="text-3xl font-bold mb-6 text-gray-800">Browse Games</h2>
        <form action="${pageContext.request.contextPath}/browse" method="get" class="mb-6 flex flex-wrap gap-4 items-end">
            <input type="text" name="search" placeholder="Search games..." value="<c:out value='${searchQuery}'/>" class="border p-2 rounded w-64" />
            <select name="filter" class="border p-2 rounded">
                <option value="" ${empty selectedPlatform ? 'selected' : ''}>All Platforms</option>
                <option value="PC" ${selectedPlatform == 'PC' ? 'selected' : ''}>PC</option>
                <option value="PS4" ${selectedPlatform == 'PS4' ? 'selected' : ''}>PS4</option>
                <option value="Xbox" ${selectedPlatform == 'Xbox' ? 'selected' : ''}>Xbox</option>
            </select>
            <select name="sort" class="border p-2 rounded">
                <option value="" ${empty selectedSort ? 'selected' : ''}>Relevance</option>
                <option value="price_asc" ${selectedSort == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                <option value="price_desc" ${selectedSort == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                <option value="release_date" ${selectedSort == 'release_date' ? 'selected' : ''}>Release Date</option>
            </select>
            <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Apply</button>
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
                                    <img src="${pageContext.request.contextPath}/${game.imagePath}" alt="${game.title}" class="w-full h-48 object-cover rounded mb-2">
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
                            <p class="text-gray-500 text-sm mb-2">Platform: <c:out value="${game.platform}"/></p>
                            <p class="text-green-700 font-bold mb-2">
                                <fmt:formatNumber value="${game.price}" type="currency" currencySymbol="$" />
                            </p>
                            <c:if test="${not empty sessionScope.loggedInUser}">
                                <form action="${pageContext.request.contextPath}/addToCart" method="post">
                                    <input type="hidden" name="gameId" value="${game.gameId}" />
                                    <button type="submit" class="bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-3 rounded text-sm">Add to Cart</button>
                                </form>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <p class="text-gray-500">No games found.</p>
            </c:otherwise>
        </c:choose>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>
