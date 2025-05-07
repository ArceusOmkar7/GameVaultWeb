<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>
      GameVault - <c:out value="${not empty game ? game.title : 'Game
      Details'}"/>
    </title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-100">
    <jsp:include page="header.jsp" />

    <div class="container mx-auto px-4 py-8">
      <%-- Display messages --%>
      <c:if test="${not empty message}">
        <div
          class="mb-4 p-4 rounded ${messageType eq 'success' ? 'bg-green-100 border border-green-400 text-green-700' : 'bg-red-100 border border-red-400 text-red-700'}"
          role="alert"
        >
          <p><c:out value="${message}" /></p>
        </div>
      </c:if>
      <c:if test="${not empty errorMessage}">
        <div
          class="mb-4 p-4 rounded bg-red-100 border border-red-400 text-red-700"
          role="alert"
        >
          <p><strong>Error:</strong> <c:out value="${errorMessage}" /></p>
        </div>
      </c:if>

      <c:choose>
        <c:when test="${not empty game}">
          <div class="bg-white p-6 rounded shadow">
            <div class="flex flex-col md:flex-row gap-6">
              <!-- Left: Game Image -->
              <div class="w-full md:w-1/3">
                <c:choose>
                  <c:when test="${not empty game.imagePath}">
                    <c:choose>
                      <c:when test="${fn:startsWith(game.imagePath, 'http')}">
                        <img
                          src="${game.imagePath}"
                          alt="${game.title}"
                          class="w-full h-80 object-cover rounded shadow"
                        />
                      </c:when>
                      <c:otherwise>
                        <img
                          src="${pageContext.request.contextPath}/${game.imagePath}"
                          alt="${game.title}"
                          class="w-full h-80 object-cover rounded shadow"
                        />
                      </c:otherwise>
                    </c:choose>
                  </c:when>
                  <c:otherwise>
                    <div
                      class="bg-gray-200 h-80 rounded flex items-center justify-center text-gray-500 text-xl"
                    >
                      <span>No Image Available</span>
                    </div>
                  </c:otherwise>
                </c:choose>
              </div>

              <!-- Right: Details & Actions -->
              <div class="w-full md:w-2/3">
                <h2 class="text-3xl font-bold mb-2">
                  <c:out value="${game.title}" />
                </h2>
                <p class="text-lg text-gray-700 mb-4">
                  By <c:out value="${game.developer}" />
                </p>
                <span
                  class="inline-block bg-blue-100 text-blue-800 text-xs font-medium mr-2 px-2.5 py-0.5 rounded dark:bg-blue-900 dark:text-blue-300 mb-4"
                >
                  Platform: <c:out value="${game.platform}" />
                </span>
                <span
                  class="inline-block bg-gray-100 text-gray-800 text-xs font-medium mr-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-gray-300 mb-4"
                >
                  Release Date:
                  <fmt:formatDate
                    value="${game.releaseDate}"
                    pattern="yyyy-MM-dd"
                  />
                </span>

                <p class="text-gray-600 mb-5">
                  <c:out value="${game.description}" />
                </p>

                <div class="mb-5">
                  <span class="text-3xl font-bold text-green-600">
                    <fmt:formatNumber
                      value="${game.price}"
                      type="currency"
                      currencySymbol="$"
                    />
                  </span>
                </div>

                <%-- Action Buttons --%>
                <c:if test="${not empty sessionScope.loggedInUser}">
                  <div class="flex items-center gap-4">
                    <%-- Add to Cart Form --%>
                    <form
                      action="${pageContext.request.contextPath}/addToCart"
                      method="post"
                      style="display: inline"
                    >
                      <input
                        type="hidden"
                        name="gameId"
                        value="${game.gameId}"
                      />
                      <button
                        type="submit"
                        class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-200"
                      >
                        Add to Cart
                      </button>
                    </form>
                    <%-- Buy Now (Could redirect to cart or implement direct
                    purchase) --%>
                    <form
                      action="${pageContext.request.contextPath}/addToCart"
                      method="post"
                      style="display: inline"
                    >
                      <%-- Simplification: Also adds to cart first --%>
                      <input
                        type="hidden"
                        name="gameId"
                        value="${game.gameId}"
                      />
                      <input type="hidden" name="buyNow" value="true" /> <%--
                      Optional param to redirect to cart after adding --%>
                      <button
                        type="submit"
                        class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition duration-200"
                      >
                        Buy Now
                      </button>
                    </form>
                  </div>
                </c:if>
                <c:if test="${empty sessionScope.loggedInUser}">
                  <p class="text-red-600">
                    Please
                    <a
                      href="${pageContext.request.contextPath}/login?redirect=${pageContext.request.requestURI}?${pageContext.request.queryString}"
                      class="underline font-semibold"
                      >login</a
                    >
                    to purchase or add to cart.
                  </p>
                </c:if>
              </div>
            </div>

            <!-- Reviews Section -->
            <div class="mt-10 pt-6 border-t">
              <h3 class="text-2xl font-semibold mb-4">Reviews</h3>

              <!-- Review Form -->
              <c:if test="${not empty sessionScope.loggedInUser}">
                <div class="mb-6 p-4 border rounded bg-gray-50">
                  <h4 class="font-semibold mb-2">Write a Review</h4>
                  <form
                    action="${pageContext.request.contextPath}/reviews"
                    method="post"
                  >
                    <input type="hidden" name="gameId" value="${game.gameId}" />
                    <div class="mb-3">
                      <textarea
                        name="comment"
                        rows="3"
                        class="w-full p-2 border rounded focus:border-blue-500 focus:outline-none"
                        placeholder="Share your thoughts about this game..."
                      ></textarea>
                    </div>
                    <button
                      type="submit"
                      class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-200"
                    >
                      Submit Review
                    </button>
                  </form>
                </div>
              </c:if>
              <c:if test="${empty sessionScope.loggedInUser}">
                <div class="mb-6 p-4 border rounded bg-gray-50">
                  <p>
                    Please
                    <a
                      href="${pageContext.request.contextPath}/login?redirect=${pageContext.request.requestURI}?${pageContext.request.queryString}"
                      class="text-blue-600 underline font-semibold"
                      >login</a
                    >
                    to write a review.
                  </p>
                </div>
              </c:if>

              <!-- List of Reviews -->
              <div class="space-y-4">
                <c:choose>
                  <c:when test="${not empty reviews}">
                    <c:forEach var="review" items="${reviews}">
                      <div class="border rounded p-4 bg-gray-50">
                        <h4 class="font-semibold">
                          <c:out value="${review.username}" />
                        </h4>
                        <p class="text-sm text-gray-500 mb-1">
                          Posted on
                          <fmt:formatDate
                            value="${review.reviewDate}"
                            pattern="yyyy-MM-dd"
                          />
                        </p>
                        <p><c:out value="${review.comment}" /></p>
                      </div>
                    </c:forEach>
                  </c:when>
                  <c:otherwise>
                    <p class="text-gray-500">
                      No reviews yet. Be the first to share your thoughts!
                    </p>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <div class="bg-white p-6 rounded shadow text-center">
            <h2 class="text-2xl font-bold mb-4 text-red-600">Game Not Found</h2>
            <p class="text-gray-600 mb-4">
              <c:choose>
                <c:when test="${not empty errorMessage}">
                  <c:out value="${errorMessage}" />
                </c:when>
                <c:otherwise>
                  The game you are looking for could not be found.
                </c:otherwise>
              </c:choose>
            </p>
            <a
              href="${pageContext.request.contextPath}/home"
              class="inline-block bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
            >
              Back to Home
            </a>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <jsp:include page="footer.jsp" />
  </body>
</html>
