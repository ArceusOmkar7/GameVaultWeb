<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
                            <button type="button" class="text-blue-600 hover:text-blue-900 mr-3 edit-game-btn" 
                                    data-id="${game.gameId}" data-title="${game.title}">Edit</button>
                            <button type="button" class="text-red-600 hover:text-red-900 delete-game" 
                                    data-id="${game.gameId}" data-title="${game.title}">Delete</button>
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
</div>