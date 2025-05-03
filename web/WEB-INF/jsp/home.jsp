<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Home</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">

    <jsp:include page="header.jsp" /> <%-- Assumes header.jsp search form submits GET --%>

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


        <div class="flex flex-col md:flex-row gap-6">

            <!-- Left Column: Game List & Filter/Sort -->
            <div class="w-full md:w-1/4">
                <%-- Game List (keep as is) --%>
                <div class="bg-white p-4 rounded shadow mb-4">
                    <h3 class="text-lg font-semibold mb-3 border-b pb-2">Browse Games</h3>
                    <div class="max-h-96 overflow-y-auto">
                         <c:choose>
                             <c:when test="${not empty gamesList}">
                                 <ul>
                                     <c:forEach var="game" items="${gamesList}">
                                         <li class="mb-2 border-b last:border-b-0 pb-2">
                                             <a href="${pageContext.request.contextPath}/game?id=${game.gameId}" class="text-blue-600 hover:underline">
                                                 <c:out value="${game.title}" />
                                             </a>
                                             <span class="text-sm text-gray-500 block">
                                                <fmt:formatNumber value="${game.price}" type="currency" currencySymbol="$" />
                                            </span>
                                         </li>
                                     </c:forEach>
                                 </ul>
                             </c:when>
                             <c:otherwise>
                                 <p class="text-gray-500">No games found.</p>
                             </c:otherwise>
                         </c:choose>
                    </div>
                </div>

                <%-- Filter/Sort Form --%>
                <form action="${pageContext.request.contextPath}/home" method="get" class="bg-white p-4 rounded shadow">
                    <input type="hidden" name="search" value="${searchQuery}"> <%-- Keep search query when filtering/sorting --%>
                    <h3 class="text-lg font-semibold mb-3 border-b pb-2">Refine</h3>
                    <div class="mb-3">
                        <label for="filter" class="block text-sm font-medium text-gray-700">Filter By Platform:</label>
                        <select id="filter" name="filter" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
                            <option value="" ${empty selectedPlatform ? 'selected' : ''}>All Platforms</option>
                            <option value="PC" ${selectedPlatform == 'PC' ? 'selected' : ''}>PC</option>
                            <option value="PS4" ${selectedPlatform == 'PS4' ? 'selected' : ''}>PS4</option>
                            <option value="Xbox" ${selectedPlatform == 'Xbox' ? 'selected' : ''}>Xbox</option>
                            <%-- TODO: Populate dynamically if needed --%>
                        </select>
                    </div>
                    <div>
                        <label for="sort" class="block text-sm font-medium text-gray-700">Sort By:</label>
                        <select id="sort" name="sort" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
                            <option value="" ${empty selectedSort ? 'selected' : ''}>Relevance</option>
                            <option value="price_asc" ${selectedSort == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                            <option value="price_desc" ${selectedSort == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                            <option value="release_date" ${selectedSort == 'release_date' ? 'selected' : ''}>Release Date</option>
                        </select>
                    </div>
                     <button type="submit" class="mt-4 w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                         Apply Filters/Sort
                     </button>
                </form>
            </div>

            <%-- Right Column (Keep as is) --%>
            <div class="w-full md:w-3/4">
                <!-- Main Featured Game Poster -->
                <div class="bg-white p-6 rounded shadow mb-6">
                    <h2 class="text-2xl font-bold mb-4">Featured Game</h2>
                    <c:choose>
                        <c:when test="${not empty mainFeaturedGame}">
                             <div class="mb-4">
                                 <c:choose>
                                     <c:when test="${not empty mainFeaturedGame.imagePath}">
                                         <img src="${pageContext.request.contextPath}/${mainFeaturedGame.imagePath}" 
                                             alt="${mainFeaturedGame.title}" 
                                             class="w-full h-64 md:h-96 object-cover rounded shadow">
                                     </c:when>
                                     <c:otherwise>
                                         <div class="bg-gray-200 h-64 md:h-96 rounded flex items-center justify-center text-gray-500 text-2xl">
                                             No Image Available for ${mainFeaturedGame.title}
                                         </div>
                                     </c:otherwise>
                                 </c:choose>
                             </div>
                             <h3 class="text-xl font-semibold"><c:out value="${mainFeaturedGame.title}"/></h3>
                             <p class="text-gray-600 mt-1 mb-3"><c:out value="${mainFeaturedGame.description}"/></p>
                             <a href="${pageContext.request.contextPath}/game?id=${mainFeaturedGame.gameId}" class="inline-block bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
                                 View Details
                             </a>
                        </c:when>
                        <c:otherwise>
                            <p class="text-center text-gray-500 py-10">No featured game available.</p>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Featured Games List (Bottom Row) -->
                 <div class="bg-white p-4 rounded shadow">
                    <h3 class="text-xl font-semibold mb-4 border-b pb-2">More Featured Games</h3>
                     <c:choose>
                         <c:when test="${not empty featuredGamesList}">
                             <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                                 <c:forEach var="featuredGame" items="${featuredGamesList}">
                                     <div class="border rounded p-3 hover:shadow-lg transition duration-200">
                                         <c:choose>
                                             <c:when test="${not empty featuredGame.imagePath}">
                                                 <img src="${pageContext.request.contextPath}/${featuredGame.imagePath}" 
                                                     alt="${featuredGame.title}" 
                                                     class="w-full h-32 object-cover rounded mb-2">
                                             </c:when>
                                             <c:otherwise>
                                                 <div class="bg-gray-200 h-32 rounded mb-2 flex items-center justify-center text-gray-400 text-sm">
                                                     No Image Available
                                                 </div>
                                             </c:otherwise>
                                         </c:choose>
                                         <h4 class="font-semibold text-md mb-1">
                                             <a href="${pageContext.request.contextPath}/game?id=${featuredGame.gameId}" class="text-blue-600 hover:underline">
                                                <c:out value="${featuredGame.title}"/>
                                             </a>
                                         </h4>
                                         <p class="text-sm text-gray-600">
                                             <fmt:formatNumber value="${featuredGame.price}" type="currency" currencySymbol="$" />
                                         </p>
                                     </div>
                                 </c:forEach>
                             </div>
                         </c:when>
                         <c:otherwise>
                             <p class="text-gray-500">No other featured games.</p>
                         </c:otherwise>
                     </c:choose>
                 </div>
            </div>

        </div> <%-- End flex row --%>
    </div> <%-- End container --%>

    <jsp:include page="footer.jsp" />

</body>
</html>