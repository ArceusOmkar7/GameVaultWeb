<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GameVault - Order History</title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-100">
    <jsp:include page="header.jsp" />

    <div class="container mx-auto px-4 py-8 pt-8">
      <h2 class="text-3xl font-bold mb-6 text-gray-800">Your Order History</h2>

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
        <c:when test="${not empty orderList}">
          <div class="bg-white shadow-md rounded-lg overflow-x-auto">
            <%-- overflow-x-auto for responsiveness --%>
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Order ID
                  </th>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Date Placed
                  </th>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Total Amount
                  </th>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Status
                  </th>
                  <%-- Added Status --%>
                  <th scope="col" class="relative px-6 py-3">
                    <span class="sr-only">Details</span>
                  </th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <c:forEach var="order" items="${orderList}">
                  <tr>
                    <td
                      class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900"
                    >
                      #<c:out value="${order.orderId}" />
                    </td>
                    <td
                      class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"
                    >
                      <fmt:formatDate
                        value="${order.orderDate}"
                        type="both"
                        dateStyle="medium"
                        timeStyle="short"
                      />
                    </td>
                    <td
                      class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"
                    >
                      <fmt:formatNumber
                        value="${order.totalAmount}"
                        type="currency"
                        currencySymbol="$"
                      />
                    </td>
                    <td
                      class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"
                    >
                      <span
                        class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800"
                      >
                        Completed <%-- Assuming all listed orders are completed
                        --%>
                      </span>
                    </td>
                    <td
                      class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium"
                    >
                      <button
                        onclick="showOrderDetails(${order.orderId})"
                        class="text-indigo-600 hover:text-indigo-900"
                      >
                        View Details
                      </button>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </c:when>
        <c:otherwise>
          <div class="text-center bg-white p-10 rounded-lg shadow-md">
            <svg
              class="mx-auto h-12 w-12 text-gray-400"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M8.25 6.75h7.5M8.25 12h7.5m-7.5 5.25h7.5M3.75 6.75h.007v.008H3.75V6.75Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0ZM3.75 12h.007v.008H3.75V12Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm-.375 5.25h.007v.008H3.75v-.008Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z"
              />
            </svg>
            <h3 class="mt-2 text-lg font-medium text-gray-900">
              No orders found
            </h3>
            <p class="mt-1 text-sm text-gray-500">
              You haven't placed any orders yet.
            </p>
            <div class="mt-6">
              <a
                href="${pageContext.request.contextPath}/home"
                class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Browse Games
              </a>
            </div>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- Order Details Modal -->
    <div
      id="orderDetailsModal"
      class="fixed inset-0 bg-gray-800 bg-opacity-75 flex items-center justify-center z-50 hidden"
    >
      <div
        class="bg-white rounded-lg shadow-xl max-w-3xl w-full max-h-[90vh] overflow-y-auto"
      >
        <div
          class="px-6 py-4 border-b border-gray-200 flex justify-between items-center"
        >
          <h3 class="text-lg font-semibold text-gray-900">Order Details</h3>
          <button
            type="button"
            id="closeOrderDetails"
            class="text-gray-400 hover:text-gray-500 focus:outline-none"
          >
            <i class="bi bi-x-lg"></i>
          </button>
        </div>
        <div id="orderDetailsContent" class="p-6">
          <div class="flex justify-center">
            <div
              class="animate-spin rounded-full h-12 w-12 border-4 border-blue-500 border-t-transparent"
            ></div>
          </div>
          <p class="text-center mt-4 text-gray-600">Loading order details...</p>
        </div>
      </div>
    </div>

    <script>
      // Function to show order details modal
      function showOrderDetails(orderId) {
          const modal = document.getElementById('orderDetailsModal');
          const content = document.getElementById('orderDetailsContent');

          // Show modal
          modal.classList.remove('hidden');

          // Initialize HTML content structure
          content.innerHTML =
              '<div class="border-b border-gray-200 pb-4 mb-4">' +
                  '<h4 class="text-lg font-semibold mb-2">Order #' + orderId + '</h4>' +
                  '<p class="text-gray-600">' +
                      '<span class="font-medium">Date:</span> ' +
                      '<span id="orderDate"></span>' +
                  '</p>' +
                  '<p class="text-gray-600">' +
                      '<span class="font-medium">Status:</span> ' +
                      '<span class="text-green-600">Completed</span>' +
                  '</p>' +
              '</div>' +
              '<div class="mb-4">' +
                  '<h4 class="text-lg font-semibold mb-2">Order Items</h4>' +
                  '<div id="orderItems" class="space-y-2"></div>' +
              '</div>' +
              '<div class="border-t border-gray-200 pt-4 flex justify-between items-center">' +
                  '<span class="text-gray-600 font-medium">Total Amount:</span>' +
                  '<span class="text-xl font-bold" id="orderTotal"></span>' +
              '</div>';

          // Populate order details
          <c:forEach var="order" items="${orderList}">
              if (${order.orderId} == orderId) {
                  // Format date
                  var orderDateStr = "${order.orderDate}";
                  try {
                      var dateObj = new Date(orderDateStr);
                      if (!isNaN(dateObj.getTime())) {
                          document.getElementById('orderDate').textContent = dateObj.toLocaleString();
                      } else {
                          document.getElementById('orderDate').textContent = orderDateStr;
                      }
                  } catch (e) {
                      document.getElementById('orderDate').textContent = orderDateStr;
                  }

                  // Add order items
                  var orderItemsContainer = document.getElementById('orderItems');
                  var itemsHtml = '';
                  var hasItems = false;

                  <c:if test="${not empty order.orderItems}">
                      // Create a JavaScript array of order items for this order
                      var orderItems = [];
                      <c:forEach var="item" items="${order.orderItems}">
                          orderItems.push({
                              title: "${item.key.title}",
                              developer: "${item.key.developer}",
                              price: "${item.value}"
                          });
                      </c:forEach>

                      // Now use the JavaScript array to build the HTML
                      if (orderItems.length > 0) {
                          hasItems = true;
                          orderItems.forEach(function(item) {
                              itemsHtml +=
                                  '<div class="flex justify-between items-center p-2 bg-gray-50 rounded">' +
                                      '<div>' +
                                          '<span class="font-medium">' + item.title + '</span>' +
                                          '<span class="text-gray-500 text-sm ml-2">' + item.developer + '</span>' +
                                      '</div>' +
                                      '<span class="font-medium">$' + item.price + '</span>' +
                                  '</div>';
                          });
                      }
                  </c:if>

                  if (hasItems) {
                      orderItemsContainer.innerHTML = itemsHtml;
                  } else {
                      orderItemsContainer.innerHTML = '<p class="text-gray-500">No items available</p>';
                  }

                  // Set total amount
                  document.getElementById('orderTotal').textContent = '$' + ${order.totalAmount}.toFixed(2);
              }
          </c:forEach>
      }

      // Close modal when clicking the close button
      document.getElementById('closeOrderDetails').addEventListener('click', function() {
          document.getElementById('orderDetailsModal').classList.add('hidden');
      });

      // Close modal when clicking outside
      document.getElementById('orderDetailsModal').addEventListener('click', function(e) {
          if (e.target === this) {
              this.classList.add('hidden');
          }
      });
    </script>

    <jsp:include page="footer.jsp" />
  </body>
</html>
