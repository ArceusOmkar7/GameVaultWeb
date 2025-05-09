<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Order History</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <style>
        .order-card {
            background: rgba(255, 255, 255, 0.9);
            border: 1px solid rgba(99, 102, 241, 0.1);
            transition: all 0.2s ease;
        }
        
        .order-card:hover {
            box-shadow: 0 4px 12px rgba(99, 102, 241, 0.1);
        }

        .platform-badge {
            background: linear-gradient(135deg, #3b82f6, #6366f1);
        }

        .price-text {
            background: linear-gradient(135deg, #6366f1, #8b5cf6);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .stats-card {
            background: rgba(255, 255, 255, 0.8);
            backdrop-filter: blur(8px);
        }
    </style>
</head>
<body class="bg-gradient-to-br from-slate-50 to-indigo-50 min-h-screen">
    <jsp:include page="header.jsp" />

    <main class="container mx-auto px-4 py-8 mt-20">
        <!-- Page Title and Stats -->
        <div class="mb-12 text-center">
            <h1 class="text-4xl font-bold mb-6 text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-purple-600">Your Gaming Journey</h1>
            
            <!-- Stats Cards -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-4xl mx-auto">
                <div class="stats-card p-6 rounded-xl shadow-lg">
                    <div class="text-4xl font-bold text-indigo-600 mb-2">
                        ${fn:length(orderList)}
                    </div>
                    <div class="text-gray-600">Total Orders</div>
                </div>
                <div class="stats-card p-6 rounded-xl shadow-lg">
                    <div class="text-4xl font-bold text-purple-600 mb-2">
                        <fmt:formatNumber value="${totalSpent}" type="currency" currencySymbol="$" />
                    </div>
                    <div class="text-gray-600">Total Spent</div>
                </div>
                <div class="stats-card p-6 rounded-xl shadow-lg">
                    <div class="text-4xl font-bold text-blue-600 mb-2">
                        ${totalGames}
                    </div>
                    <div class="text-gray-600">Games Owned</div>
                </div>
            </div>
        </div>

        <!-- Messages -->
        <c:if test="${not empty message}">
            <div class="mb-6 rounded-lg bg-gradient-to-r ${messageType eq 'success' ? 'from-green-50 to-emerald-50 border-green-200' : 'from-red-50 to-rose-50 border-red-200'} border p-4" role="alert">
                <p class="${messageType eq 'success' ? 'text-green-800' : 'text-red-800'}">
                    <c:out value="${message}" />
                </p>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="mb-6 rounded-lg bg-gradient-to-r from-red-50 to-rose-50 border border-red-200 p-4" role="alert">
                <p class="text-red-800"><strong>Error:</strong> <c:out value="${errorMessage}" /></p>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${not empty orderList}">
                <!-- Order Timeline -->
                <div class="max-w-4xl mx-auto space-y-6">
                    <c:forEach var="order" items="${orderList}">
                        <div class="order-card rounded-xl overflow-hidden">
                            <!-- Order Header -->
                            <div class="bg-gray-50 p-6 flex flex-wrap items-center justify-between gap-4">
                                <div>
                                    <span class="text-sm text-gray-500">Order ID</span>
                                    <h3 class="text-lg font-semibold text-gray-900">#${order.orderId}</h3>
                                </div>
                                <div>
                                    <span class="text-sm text-gray-500">Date Placed</span>
                                    <p class="font-medium text-gray-900">
                                        <fmt:formatDate value="${order.orderDate}" type="both" dateStyle="medium" timeStyle="short" />
                                    </p>
                                </div>
                                <div>
                                    <span class="text-sm text-gray-500">Total Amount</span>
                                    <p class="font-bold text-indigo-600">
                                        <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$" />
                                    </p>
                                </div>
                                <div>
                                    <span class="px-3 py-1 rounded-full text-white text-sm font-medium bg-green-500">
                                        Completed
                                    </span>
                                </div>
                            </div>

                            <!-- Order Details -->
                            <div class="p-6">
                                <!-- Games List -->
                                <div class="space-y-4">
                                    <c:forEach var="orderItem" items="${order.orderItems}">
                                        <div class="flex items-center gap-4 p-4 bg-gray-50 rounded-lg">
                                            <div class="w-16 h-16 bg-gray-200 rounded-lg overflow-hidden">
                                                <c:choose>
                                                    <c:when test="${not empty orderItem.key.imagePath}">
                                                        <img src="${fn:startsWith(orderItem.key.imagePath, 'http') ? orderItem.key.imagePath : pageContext.request.contextPath.concat('/').concat(orderItem.key.imagePath)}"
                                                             alt="${orderItem.key.title}"
                                                             class="w-full h-full object-cover">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="w-full h-full flex items-center justify-center">
                                                            <i class="bi bi-controller text-2xl text-gray-400"></i>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="flex-1">
                                                <h4 class="text-lg font-medium text-gray-900">${orderItem.key.title}</h4>
                                                <p class="text-sm text-gray-500">${orderItem.key.platform}</p>
                                            </div>
                                            <div class="text-right">
                                                <p class="text-sm text-gray-500">Price paid:</p>
                                                <p class="font-bold text-gray-900">
                                                    <fmt:formatNumber value="${orderItem.value}" type="currency" currencySymbol="$" />
                                                </p>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <!-- Order Actions -->
                                <div class="mt-6 flex justify-end gap-4">
                                    <button onclick="showOrderDetails('${order.orderId}')"
                                            class="px-4 py-2 text-indigo-600 hover:text-indigo-800 transition-colors">
                                        <i class="bi bi-eye mr-2"></i>
                                        View Invoice
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="max-w-2xl mx-auto bg-white rounded-2xl shadow-lg p-12 text-center">
                    <div class="w-24 h-24 mx-auto mb-6 rounded-full bg-gradient-to-br from-indigo-400 to-purple-500 flex items-center justify-center text-white">
                        <i class="bi bi-bag text-4xl"></i>
                    </div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-2">No orders found</h3>
                    <p class="text-gray-600 mb-8">You haven't placed any orders yet.</p>
                    <a href="${pageContext.request.contextPath}/home" 
                       class="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700">
                        Browse Games
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <!-- Order Details Modal -->
    <div id="orderDetailsModal" class="fixed inset-0 bg-gray-800 bg-opacity-75 flex items-center justify-center z-50 hidden">
        <div class="bg-white rounded-lg shadow-xl max-w-3xl w-full max-h-[90vh] overflow-y-auto">
            <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
                <h3 class="text-lg font-semibold text-gray-900">Order Details</h3>
                <button type="button" id="closeOrderDetails" class="text-gray-400 hover:text-gray-500 focus:outline-none">
                    <i class="bi bi-x-lg"></i>
                </button>
            </div>
            <div id="orderDetailsContent" class="p-6">
                <div class="flex justify-center">
                    <div class="animate-spin rounded-full h-12 w-12 border-4 border-blue-500 border-t-transparent"></div>
                </div>
                <p class="text-center mt-4 text-gray-600">Loading order details...</p>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />

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
                if (orderId === "${order.orderId}") {
                    // Format date
                    var orderDateStr = "${order.orderDate}";
                    try {
                        var dateObj = new Date(orderDateStr);
                        if (!isNaN(dateObj.getTime())) {
                            document.getElementById("orderDate").textContent = dateObj.toLocaleString();
                        } else {
                            document.getElementById("orderDate").textContent = orderDateStr;
                        }
                    } catch (e) {
                        document.getElementById("orderDate").textContent = orderDateStr;
                    }

                    // Add order items
                    var orderItemsContainer = document.getElementById("orderItems");
                    var itemsHtml = "";
                    var hasItems = false;

                    <c:if test="${not empty order.orderItems}">
                        // Create a JavaScript array of order items for this order
                        var orderItems = [];
                        <c:forEach var="item" items="${order.orderItems}">
                            orderItems.push({
                                title: "${fn:replace(item.key.title, '"', '\\"')}",
                                platform: "${fn:replace(item.key.platform, '"', '\\"')}",
                                price: "${item.value}",
                                imagePath: "${fn:replace(item.key.imagePath, '"', '\\"')}"
                            });
                        </c:forEach>

                        // Generate HTML for each item
                        orderItems.forEach(function(item) {
                            itemsHtml = itemsHtml + 
                                "<div class=\"flex items-center justify-between p-3 bg-gray-50 rounded-lg\">" +
                                "<div class=\"flex items-center gap-3\">" +
                                "<div class=\"w-12 h-12 bg-gray-200 rounded-lg overflow-hidden\">" +
                                (item.imagePath ? 
                                    "<img src=\"" + (item.imagePath.startsWith('http') ? item.imagePath : "${pageContext.request.contextPath}/" + item.imagePath) + "\" " +
                                    "alt=\"" + item.title + "\" " +
                                    "class=\"w-full h-full object-cover\">" :
                                    "<div class=\"w-full h-full flex items-center justify-center\">" +
                                    "<i class=\"bi bi-controller text-xl text-gray-400\"></i>" +
                                    "</div>"
                                ) +
                                "</div>" +
                                "<div>" +
                                "<h5 class=\"font-medium text-gray-900\">" + item.title + "</h5>" +
                                "<p class=\"text-sm text-gray-500\">" + item.platform + "</p>" +
                                "</div>" +
                                "</div>" +
                                "<div class=\"text-right\">" +
                                "<p class=\"text-sm text-gray-500\">Price paid:</p>" +
                                "<p class=\"font-bold text-gray-900\">$" + parseFloat(item.price).toFixed(2) + "</p>" +
                                "</div>" +
                                "</div>";
                        });
                        hasItems = true;
                    </c:if>

                    if (hasItems) {
                        orderItemsContainer.innerHTML = itemsHtml;
                    } else {
                        orderItemsContainer.innerHTML = "<p class=\"text-gray-500 text-center\">No items found in this order.</p>";
                    }

                    // Set total amount with dollar symbol and 2 decimal places
                    document.getElementById("orderTotal").textContent = "$" + parseFloat("${order.totalAmount}").toFixed(2);
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

        // Function to generate invoice (placeholder)
        function generateInvoice(orderId) {
            alert('Invoice generation for order #' + orderId + ' would be implemented here.');
        }
    </script>
</body>
</html>
