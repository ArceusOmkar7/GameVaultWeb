<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Add Game Modal -->
<div
  id="addGameModal"
  class="fixed inset-0 bg-gray-800 bg-opacity-75 flex items-center justify-center z-50 hidden"
>
  <div
    class="bg-white rounded-lg shadow-lg w-full max-w-3xl max-h-screen overflow-y-auto"
  >
    <div
      class="px-6 py-4 border-b border-gray-200 flex justify-between items-center"
    >
      <h3 class="text-lg font-semibold text-gray-900">Add New Game</h3>
      <button id="closeAddGameModal" class="text-gray-400 hover:text-gray-500">
        <i class="bi bi-x-lg"></i>
      </button>
    </div>
    <div class="p-6">
      <form
        id="addGameForm"
        action="${pageContext.request.contextPath}/admin/add-game"
        method="post"
      >
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="col-span-2">
            <label for="title" class="block text-sm font-medium text-gray-700"
              >Title</label
            >
            <input
              type="text"
              name="title"
              id="title"
              class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
              required
            />
          </div>

          <div class="col-span-2">
            <label
              for="description"
              class="block text-sm font-medium text-gray-700"
              >Description</label
            >
            <textarea
              name="description"
              id="description"
              rows="3"
              class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
              required
            ></textarea>
          </div>

          <div>
            <label
              for="developer"
              class="block text-sm font-medium text-gray-700"
              >Developer</label
            >
            <input
              type="text"
              name="developer"
              id="developer"
              class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
              required
            />
          </div>

          <div>
            <label for="price" class="block text-sm font-medium text-gray-700"
              >Price</label
            >
            <div class="mt-1 relative rounded-md shadow-sm">
              <div
                class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"
              >
                <span class="text-gray-500 sm:text-sm">$</span>
              </div>
              <input
                type="number"
                step="0.01"
                min="0"
                name="price"
                id="price"
                class="focus:ring-blue-500 focus:border-blue-500 block w-full pl-7 pr-12 sm:text-sm border-gray-300 rounded-md"
                placeholder="0.00"
                required
              />
            </div>
          </div>
          <div>
            <label
              for="platform"
              class="block text-sm font-medium text-gray-700"
              >Platform</label
            >
            <select
              name="platform"
              id="platform"
              class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
              multiple
              required
              size="4"
            >
              <c:forEach var="platform" items="${platforms}">
                <option value="${platform.name}">${platform.name}</option>
              </c:forEach>
            </select>
            <p class="mt-1 text-xs text-gray-500">
              Hold Ctrl/Cmd to select multiple platforms
            </p>
          </div>

          <div>
            <label for="genre" class="block text-sm font-medium text-gray-700"
              >Genre</label
            >
            <select
              name="genre"
              id="genre"
              class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
              multiple
              required
              size="4"
            >
              <c:forEach var="genre" items="${genres}">
                <option value="${genre.name}">${genre.name}</option>
              </c:forEach>
            </select>
            <p class="mt-1 text-xs text-gray-500">
              Hold Ctrl/Cmd to select multiple genres
            </p>
          </div>

          <div>
            <label
              for="releaseDate"
              class="block text-sm font-medium text-gray-700"
              >Release Date</label
            >
            <input
              type="date"
              name="releaseDate"
              id="releaseDate"
              class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
              required
            />
          </div>
          <div>
            <label for="rating" class="block text-sm font-medium text-gray-700"
              >Rating</label
            >
            <div class="flex items-center gap-3">
              <input
                type="range"
                step="0.5"
                min="0"
                max="5"
                name="rating"
                id="rating"
                class="mt-1 w-full"
                value="0"
                oninput="document.getElementById('ratingValue').textContent = this.value"
              />
              <span id="ratingValue" class="text-sm font-medium">0</span>
            </div>
            <p class="mt-1 text-xs text-gray-500">
              Rate from 0 to 5 (in 0.5 increments)
            </p>
          </div>

          <div class="col-span-2">
            <label
              for="imagePath"
              class="block text-sm font-medium text-gray-700"
              >Image Path</label
            >
            <input
              type="text"
              name="imagePath"
              id="imagePath"
              class="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
              placeholder="URL or path to image"
            />
            <p class="mt-1 text-xs text-gray-500">
              Enter a URL or upload an image separately via the image upload
              tool
            </p>
          </div>
        </div>

        <div class="mt-6 flex justify-end">
          <button
            type="button"
            id="cancelAddGame"
            class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 mr-3"
          >
            Cancel
          </button>
          <button
            type="submit"
            class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Save Game
          </button>
        </div>
      </form>
    </div>
  </div>
</div>
