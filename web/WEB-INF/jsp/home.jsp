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
    <!-- Bootstrap CSS for Carousel -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>

    <style>
      /* Force carousel and images to desired height */
      #carouselExampleIndicators .carousel-inner,
      #carouselExampleIndicators .carousel-item {
        min-height: 480px !important;
        height: 480px !important;
      }
      #carouselExampleIndicators .carousel-item img {
        min-height: 480px !important;
        height: 480px !important;
        object-fit: cover;
      }
    </style>
</head>
<body class="bg-gray-100 pt-20">

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

        <!-- Clean welcome section above carousel -->
        <div class="mb-8 p-6 bg-blue-50 border-l-4 border-blue-400 rounded text-center">
            <h1 class="text-3xl font-bold text-blue-800 mb-2">Welcome to GameVault</h1>
            <p class="text-gray-700 text-base">Discover, browse, and buy your favorite games. Enjoy curated collections and exclusive deals!</p>
        </div>

        <div class="flex flex-col md:flex-row gap-6">
            <!-- Remove Left Column: Game List & Filter/Sort -->
            <!-- Only keep the right/featured games section -->
            <div class="w-full">
                <!-- Carousel Slider (Bootstrap) -->
                <div id="carouselExampleIndicators" class="carousel slide mb-8" data-ride="carousel" data-interval="1000">
                  <c:if test="${not empty featuredGamesList}">
                    <ol class="carousel-indicators">
                      <c:forEach var="featuredGame" items="${featuredGamesList}" varStatus="status">
                        <li data-target="#carouselExampleIndicators" data-slide-to="${status.index}" class="${status.first ? 'active' : ''}"></li>
                      </c:forEach>
                    </ol>
                    <div class="carousel-inner rounded shadow">
                      <c:forEach var="featuredGame" items="${featuredGamesList}" varStatus="status">
                        <div class="carousel-item ${status.first ? 'active' : ''}">
                          <a href="${pageContext.request.contextPath}/game?id=${featuredGame.gameId}">
                            <c:choose>
                              <c:when test="${not empty featuredGame.imagePath}">
                                <c:choose>
                                  <c:when test="${fn:startsWith(featuredGame.imagePath, 'http')}">
                                    <img class="d-block w-100" src="${featuredGame.imagePath}" alt="${featuredGame.title}" style="height:480px;object-fit:cover;">
                                  </c:when>
                                  <c:otherwise>
                                    <img class="d-block w-100" src="${pageContext.request.contextPath}/${featuredGame.imagePath}" alt="${featuredGame.title}" style="height:480px;object-fit:cover;">
                                  </c:otherwise>
                                </c:choose>
                              </c:when>
                              <c:otherwise>
                                <%-- Random image for missing game image --%>
                                <%
                                  String[] randomImages = {
                                    "https://images.unsplash.com/photo-1511512578047-dfb367046420?auto=format&fit=crop&w=800&q=80",
                                    "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80",
                                    "https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=800&q=80",
                                    "https://images.unsplash.com/photo-1470770841072-f978cf4d019e?auto=format&fit=crop&w=800&q=80"
                                  };
                                  int idx = (int) (Math.random() * randomImages.length);
                                  String randomImg = randomImages[idx];
                                %>
                                <img class="d-block w-100" src="<%= randomImg %>" alt="Random Game" style="height:480px;object-fit:cover;">
                              </c:otherwise>
                            </c:choose>
                          </a>
                        </div>
                      </c:forEach>
                    </div>
                  </c:if>
                  <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="sr-only">Previous</span>
                  </a>
                  <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="sr-only">Next</span>
                  </a>
                </div>
                <!-- End Carousel Slider -->

                <!-- Main Featured Game Poster -->
                <div class="bg-white p-6 rounded shadow mb-6">
                    <h2 class="text-2xl font-bold mb-4">Featured Game</h2>
                    <c:choose>
                        <c:when test="${not empty mainFeaturedGame}">
                             <div class="mb-4">
                                 <c:choose>
                                     <c:when test="${not empty mainFeaturedGame.imagePath}">
                                         <c:choose>
                                             <c:when test="${fn:startsWith(mainFeaturedGame.imagePath, 'http')}">
                                                 <img src="${mainFeaturedGame.imagePath}" 
                                                     alt="${mainFeaturedGame.title}" 
                                                     class="w-full h-64 md:h-96 object-cover rounded shadow">
                                             </c:when>
                                             <c:otherwise>
                                                 <img src="${pageContext.request.contextPath}/${mainFeaturedGame.imagePath}" 
                                                     alt="${mainFeaturedGame.title}" 
                                                     class="w-full h-64 md:h-96 object-cover rounded shadow">
                                             </c:otherwise>
                                         </c:choose>
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
                             <div class="flex space-x-2">
                                 <a href="${pageContext.request.contextPath}/game?id=${mainFeaturedGame.gameId}" class="inline-block bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
                                     View Details
                                 </a>
                                 <c:if test="${not empty sessionScope.loggedInUser}">
                                     <form action="${pageContext.request.contextPath}/addToCart" method="post" class="inline">
                                         <input type="hidden" name="gameId" value="${mainFeaturedGame.gameId}" />
                                         <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                                             Add to Cart
                                         </button>
                                     </form>
                                 </c:if>
                             </div>
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
                                                 <c:choose>
                                                     <c:when test="${fn:startsWith(featuredGame.imagePath, 'http')}">
                                                         <img src="${featuredGame.imagePath}" 
                                                             alt="${featuredGame.title}" 
                                                             class="w-full h-32 object-cover rounded mb-2">
                                                     </c:when>
                                                     <c:otherwise>
                                                         <img src="${pageContext.request.contextPath}/${featuredGame.imagePath}" 
                                                             alt="${featuredGame.title}" 
                                                             class="w-full h-32 object-cover rounded mb-2">
                                                     </c:otherwise>
                                                 </c:choose>
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
                                         <div class="flex justify-between items-center">
                                             <p class="text-sm text-gray-600">
                                                 <fmt:formatNumber value="${featuredGame.price}" type="currency" currencySymbol="$" />
                                             </p>
                                             <c:if test="${not empty sessionScope.loggedInUser}">
                                                 <form action="${pageContext.request.contextPath}/addToCart" method="post" class="inline">
                                                     <input type="hidden" name="gameId" value="${featuredGame.gameId}" />
                                                     <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-3 rounded text-sm">
                                                         Add to Cart
                                                     </button>
                                                 </form>
                                             </c:if>
                                         </div>
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