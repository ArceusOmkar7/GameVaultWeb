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
    <style>
      /* Notification styles */
      .notification {
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 4px;
        display: flex;
        align-items: center;
        box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
        transform: translateX(120%);
        transition: transform 0.3s ease-in-out;
        z-index: 1000;
        max-width: 350px;
      }
      .notification.show {
        transform: translateX(0);
      }
      .notification-success {
        background-color: #10b981;
        color: white;
      }
      .notification-error {
        background-color: #ef4444;
        color: white;
      }
      .notification-warning {
        background-color: #f59e0b;
        color: white;
      }
      .notification-info {
        background-color: #3b82f6;
        color: white;
      }
    </style>
  </head>
  <body class="bg-gray-100">
    <jsp:include page="header.jsp" />

    <!-- Notification container -->
    <div id="notification-container"></div>

    <div class="container mx-auto px-4 py-8 pt-8">
      <%-- Display messages --%>
      <c:if test="${not empty message}">
        <div
          class="mb-4 p-4 rounded ${messageType eq 'success' || (message != null && message.toLowerCase().contains('already own')) ? 'bg-green-100 border border-green-400 text-green-700' : 'bg-red-100 border border-red-400 text-red-700'}"
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
                    <button
                      type="button"
                      onclick="addToCart('${game.gameId}', '${fn:escapeXml(game.title)}', event)"
                      class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-200"
                    >
                      Add to Cart
                    </button>

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
                      <input type="hidden" name="buyNow" value="true" />
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

    <!-- JavaScript for AJAX and notifications -->
    <script>
      // Function to create and show notifications
      function showNotification(message, type) {
        const container = document.getElementById("notification-container");
        const notification = document.createElement("div");
        notification.className = `notification notification-${type} fixed top-4 right-4 p-4 rounded-lg shadow-lg z-50 transform transition-all duration-300 ease-in-out opacity-0 translate-y-[-1rem]`;

        // Set background color based on type
        switch (type) {
          case "success":
            notification.classList.add(
              "bg-green-100",
              "border-green-400",
              "text-green-700"
            );
            break;
          case "error":
            notification.classList.add(
              "bg-red-100",
              "border-red-400",
              "text-red-700"
            );
            break;
          case "warning":
            notification.classList.add(
              "bg-yellow-100",
              "border-yellow-400",
              "text-yellow-700"
            );
            break;
          case "info":
            notification.classList.add(
              "bg-blue-100",
              "border-blue-400",
              "text-blue-700"
            );
            break;
          default:
            notification.classList.add(
              "bg-blue-100",
              "border-blue-400",
              "text-blue-700"
            );
        }

        notification.innerHTML = message;

        container.appendChild(notification);

        // Show the notification after a small delay to allow CSS transition
        setTimeout(() => {
          notification.classList.remove("opacity-0", "translate-y-[-1rem]");
          notification.classList.add("opacity-100", "translate-y-0");
        }, 10);

        // Automatically close the notification after 4 seconds
        setTimeout(() => {
          notification.classList.remove("opacity-100", "translate-y-0");
          notification.classList.add("opacity-0", "translate-y-[-1rem]");
          // Remove from DOM after animation completes
          setTimeout(() => {
            container.removeChild(notification);
          }, 300);
        }, 4000);
      }

      // Function to handle the "Add to Cart" action
      function addToCart(gameId, gameTitle, event) {
        event.target.disabled = true;
        const formData = new URLSearchParams();
        formData.append("gameId", gameId);
        fetch("${pageContext.request.contextPath}/addToCart", {
          method: "POST",
          body: formData,
          headers: {
            "X-Requested-With": "XMLHttpRequest",
            "Content-Type": "application/x-www-form-urlencoded",
          },
        })
          .then((response) => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.json();
          })
          .then((data) => {
            if (data.success) {
              showNotification(
                `<strong>Success!</strong> ${data.message}`,
                "success"
              );
            } else if (
              data.message &&
              data.message.toLowerCase().includes("already own")
            ) {
              showNotification(
                `<div style='display:flex;align-items:center;font-weight:bold;font-size:1.1em;'><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 512 512\" width=\"22\" height=\"22\" style=\"margin-right:8px;\"><path d=\"M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM216 336l24 0 0-64-24 0c-13.3 0-24-10.7-24-24s10.7-24 24-24l48 0c13.3 0 24 10.7 24 24l0 88 8 0c13.3 0 24 10.7 24 24s-10.7 24-24 24l-80 0c-13.3 0-24-10.7-24-24s10.7-24 24-24zm40-208a32 32 0 1 1 0 64 32 32 0 1 1 0-64z\"/></svg>Game Already Owned</div>`,
                "info"
              );
            } else {
              showNotification(
                `<strong>Notice:</strong> ${data.message}`,
                "info"
              );
            }
            event.target.disabled = false;
          })
          .catch((error) => {
            console.error("Error:", error);
            showNotification(
              "<strong>Notice:</strong> Unable to add game to cart. Please try again.",
              "info"
            );
            event.target.disabled = false;
          });
      }

      // Check if there's a message in the URL parameters (for backward compatibility)
      document.addEventListener("DOMContentLoaded", function () {
        const urlParams = new URLSearchParams(window.location.search);
        const message = urlParams.get("message");
        const messageType = urlParams.get("messageType") || "info";

        if (message) {
          showNotification(
            `<strong>${
              messageType.charAt(0).toUpperCase() + messageType.slice(1)
            }:</strong> ${message}`,
            messageType
          );

          // Clean up the URL to remove the message parameters
          const newUrl =
            window.location.pathname +
            window.location.search
              .replace(/[?&]message=[^&]*/, "")
              .replace(/[?&]messageType=[^&]*/, "")
              .replace(/^\?$/, "");
          window.history.replaceState({}, document.title, newUrl);
        }
      });
    </script>
  </body>
</html>
