<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- 
  Reusable Game Card component
  
  Required attributes:
  - game: The Game object with title, price, imagePath, developer, etc.
  - linkToDetail: Whether to link to game detail page (default: true)
--%>

<c:if test="${empty linkToDetail}">
    <c:set var="linkToDetail" value="true" />
</c:if>

<div class="gv-game-card transform transition hover:-translate-y-1">
    <c:if test="${linkToDetail}">
        <a href="${pageContext.request.contextPath}/gameDetail?id=${game.gameId}">
    </c:if>
    
    <div class="relative">
        <img src="${empty game.imagePath ? 'https://placehold.co/600x400?text=No+Image' : game.imagePath}" 
             alt="${game.title}" 
             class="gv-game-image" />
             
        <c:if test="${not empty game.genre}">
            <span class="absolute bottom-2 right-2 bg-gray-800 text-white text-xs px-2 py-1 rounded">
                ${game.genre}
            </span>
        </c:if>
    </div>
    
    <div class="p-4">
        <h3 class="gv-game-title">${game.title}</h3>
        
        <div class="text-sm text-gray-500 mt-1">${game.developer}</div>
        
        <div class="flex justify-between items-center mt-3">
            <div class="gv-game-price">
                <fmt:formatNumber value="${game.price}" type="currency" currencySymbol="$" minFractionDigits="2" />
            </div>
            
            <c:if test="${not empty game.platform}">
                <div class="text-sm text-gray-600">
                    ${game.platform}
                </div>
            </c:if>
        </div>
    </div>
    
    <c:if test="${linkToDetail}">
        </a>
    </c:if>
</div>