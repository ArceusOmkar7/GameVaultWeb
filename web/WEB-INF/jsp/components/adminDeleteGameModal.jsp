<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Delete Confirmation Modal -->
<div
  id="deleteModal"
  class="fixed inset-0 bg-gray-800 bg-opacity-75 flex items-center justify-center z-50 hidden"
>
  <div class="bg-white rounded-lg shadow-lg w-full max-w-md">
    <div class="px-6 py-4 border-b border-gray-200">
      <h3 class="text-lg font-semibold text-gray-900">Confirm Deletion</h3>
    </div>
    <div class="p-6">
      <p class="text-gray-700 mb-4">
        Are you sure you want to delete the game "<span
          id="deleteGameTitle"
        ></span
        >"? This action cannot be undone.
      </p>
      <form
        id="deleteGameForm"
        action="${pageContext.request.contextPath}/admin/delete-game"
        method="post"
      >
        <input type="hidden" id="deleteGameId" name="gameId" />
        <div class="flex justify-end">
          <button
            type="button"
            id="cancelDelete"
            class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 mr-3"
          >
            Cancel
          </button>
          <button
            type="submit"
            class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
          >
            Delete
          </button>
        </div>
      </form>
    </div>
  </div>
</div>
