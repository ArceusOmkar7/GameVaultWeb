<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Game Filter Controls -->
<form id="filterForm" action="${pageContext.request.contextPath}/admin/game-management" method="get">
    <div class="bg-white rounded-lg shadow p-6 mb-6">
        <div class="flex flex-wrap justify-between items-end gap-4">
            <div class="w-full md:w-1/3">
                <label for="searchQuery" class="block text-sm font-medium text-gray-700 mb-1">Search Games</label>
                <input type="text" id="searchQuery" name="searchQuery" placeholder="Search by title or developer..." 
                       value="${searchQuery}"
                       class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
            </div>
            <div class="w-full md:w-1/5">
                <label for="platformFilter" class="block text-sm font-medium text-gray-700 mb-1">Platform</label>
                <select id="platformFilter" name="platformFilter" 
                        class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <option value="">All Platforms</option>
                    <c:forEach var="platform" items="${platforms}">
                        <option value="${platform.name}" ${platformFilter == platform.name ? 'selected' : ''}>
                            <c:out value="${platform.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="w-full md:w-1/5">
                <label for="genreFilter" class="block text-sm font-medium text-gray-700 mb-1">Genre</label>
                <select id="genreFilter" name="genreFilter" 
                        class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <option value="">All Genres</option>
                    <c:forEach var="genre" items="${genres}">
                        <option value="${genre.name}" ${genreFilter == genre.name ? 'selected' : ''}>
                            <c:out value="${genre.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="w-full md:w-1/5">
                <label for="sortBy" class="block text-sm font-medium text-gray-700 mb-1">Sort By</label>
                <select id="sortBy" name="sortBy" 
                        class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <option value="title_asc" ${sortBy == 'title_asc' ? 'selected' : ''}>Title (A-Z)</option>
                    <option value="title_desc" ${sortBy == 'title_desc' ? 'selected' : ''}>Title (Z-A)</option>
                    <option value="price_asc" ${sortBy == 'price_asc' ? 'selected' : ''}>Price (Low to High)</option>
                    <option value="price_desc" ${sortBy == 'price_desc' ? 'selected' : ''}>Price (High to Low)</option>
                    <option value="rating_desc" ${sortBy == 'rating_desc' ? 'selected' : ''}>Rating (High to Low)</option>
                    <option value="release_date" ${sortBy == 'release_date' ? 'selected' : ''}>Release Date (Newest)</option>
                </select>
            </div>
            <div>
                <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded font-medium">
                    Apply Filters
                </button>
            </div>
        </div>
    </div>
</form>