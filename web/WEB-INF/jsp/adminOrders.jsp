<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <c:set var="pageTitle" value="Admin Order Management" />
    <jsp:include page="components/adminHead.jsp" />
</head>
<body class="bg-gray-100">
    <jsp:include page="components/adminSidebarNew.jsp" />
    <jsp:include page="components/adminTopNav.jsp" />

    <div class="pt-24 md:pl-64 container mx-auto px-4 py-6 transition-all duration-200" id="mainContent">
        <!-- Page Title -->
        <div class="flex justify-between items-center mb-6">
            <h1 class="text-3xl font-bold text-gray-800">Order Management</h1>
        </div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
            <div class="bg-white rounded-lg shadow flex items-center justify-between p-6">
                <div>
                    <h2 class="text-lg font-semibold text-gray-600">Total Orders</h2>
                    <p class="text-3xl font-bold">${totalOrders}</p>
                </div>
                <div class="text-4xl text-blue-600">
                    <i class="bi bi-bag-check"></i>
                </div>
            </div>
            
            <div class="bg-white rounded-lg shadow flex items-center justify-between p-6">
                <div>
                    <h2 class="text-lg font-semibold text-gray-600">Total Revenue</h2>
                    <p class="text-3xl font-bold">$<fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00" /></p>
                </div>
                <div class="text-4xl text-green-600">
                    <i class="bi bi-currency-dollar"></i>
                </div>
            </div>
        </div>

        <!-- Filters -->
        <form action="${pageContext.request.contextPath}/admin/orders" method="get" class="bg-white rounded-lg shadow p-6 mb-8">
            <h2 class="text-xl font-bold text-gray-800 mb-4">Filter Orders</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4 mb-4">
                <div>
                    <label for="userIdFilter" class="block text-sm font-medium text-gray-700">User ID</label>
                    <input type="number" id="userIdFilter" name="userIdFilter" value="${userIdFilter}" 
                        class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm" 
                        placeholder="Filter by user ID">
                </div>
                
                <div>
                    <label for="minAmountFilter" class="block text-sm font-medium text-gray-700">Min Amount ($)</label>
                    <input type="number" step="0.01" id="minAmountFilter" name="minAmountFilter" value="${minAmountFilter}" 
                        class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm" 
                        placeholder="Min amount">
                </div>
                
                <div>
                    <label for="maxAmountFilter" class="block text-sm font-medium text-gray-700">Max Amount ($)</label>
                    <input type="number" step="0.01" id="maxAmountFilter" name="maxAmountFilter" value="${maxAmountFilter}" 
                        class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm" 
                        placeholder="Max amount">
                </div>
                
                <div>
                    <label for="startDateFilter" class="block text-sm font-medium text-gray-700">Start Date</label>
                    <input type="date" id="startDateFilter" name="startDateFilter" value="${startDateFilter}" 
                        class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm">
                </div>
                
                <div>
                    <label for="endDateFilter" class="block text-sm font-medium text-gray-700">End Date</label>
                    <input type="date" id="endDateFilter" name="endDateFilter" value="${endDateFilter}" 
                        class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm">
                </div>
            </div>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4 items-end">
                <div>
                    <label for="sortBy" class="block text-sm font-medium text-gray-700">Sort By</label>
                    <select id="sortBy" name="sortBy" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm">
                        <option value="date_desc" ${sortBy == 'date_desc' ? 'selected' : ''}>Date (Newest First)</option>
                        <option value="date_asc" ${sortBy == 'date_asc' ? 'selected' : ''}>Date (Oldest First)</option>
                        <option value="amount_desc" ${sortBy == 'amount_desc' ? 'selected' : ''}>Amount (High to Low)</option>
                        <option value="amount_asc" ${sortBy == 'amount_asc' ? 'selected' : ''}>Amount (Low to High)</option>
                        <option value="user_id" ${sortBy == 'user_id' ? 'selected' : ''}>User ID</option>
                        <option value="order_id" ${sortBy == 'order_id' ? 'selected' : ''}>Order ID</option>
                    </select>
                </div>
                
                <div class="flex justify-end">
                    <button type="submit" class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                        <i class="bi bi-funnel mr-2"></i> Apply Filters
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="ml-3 inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                        <i class="bi bi-x-circle mr-2"></i> Clear Filters
                    </a>
                </div>
            </div>
        </form>

        <!-- Orders List -->
        <div class="bg-white rounded-lg shadow overflow-hidden">
            <div class="overflow-x-auto">
                <c:choose>
                    <c:when test="${not empty orders}">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Order ID</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">User</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Amount</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Items</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                    <th scope="col" class="relative px-6 py-3">
                                        <span class="sr-only">Actions</span>
                                    </th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:forEach var="order" items="${orders}">
                                    <tr>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">#${order.orderId}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            <c:set var="foundUser" value="false" />
                                            <c:forEach var="user" items="${users}">
                                                <c:if test="${user.userId == order.userId}">
                                                    <c:set var="foundUser" value="true" />
                                                    <span class="font-medium">${user.username}</span> (ID: ${order.userId})
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${!foundUser}">
                                                User ID: ${order.userId}
                                            </c:if>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            <fmt:formatDate value="${order.orderDate}" type="both" dateStyle="medium" timeStyle="short" />
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-medium">
                                            $<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00" />
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                                                <c:choose>
                                                    <c:when test="${order.orderItems != null && !empty order.orderItems}">
                                                        ${order.orderItems.size()} items
                                                    </c:when>
                                                    <c:otherwise>
                                                        0 items
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                                                Completed
                                            </span>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                            <button type="button" onclick="showOrderDetails(${order.orderId})" class="text-blue-600 hover:text-blue-900">
                                                <i class="bi bi-eye"></i> View
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-12">
                            <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                            </svg>
                            <h3 class="mt-2 text-sm font-medium text-gray-900">No orders found</h3>
                            <p class="mt-1 text-sm text-gray-500">No orders match your current filters.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

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

    <script>
        // Function to show order details modal
        function showOrderDetails(orderId) {
            const modal = document.getElementById('orderDetailsModal');
            const content = document.getElementById('orderDetailsContent');
            
            // Show modal
            modal.classList.remove('hidden');
            
            // Initialize HTML content structure first with concat instead of template literals
            content.innerHTML = 
                '<div class="border-b border-gray-200 pb-4 mb-4">' +
                    '<h4 class="text-lg font-semibold mb-2">Order #' + orderId + '</h4>' +
                    '<p class="text-gray-600">' +
                        '<span class="font-medium">Date:</span> ' +
                        '<span id="orderDate"></span>' +
                    '</p>' +
                    '<p class="text-gray-600">' +
                        '<span class="font-medium">User:</span> ' +
                        '<span id="orderUser"></span>' +
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
            <c:forEach var="order" items="${orders}">
                if (${order.orderId} == orderId) {
                    // Format date safely
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
                    
                    // Find user
                    var foundUser = false;
                    <c:forEach var="user" items="${users}">
                        if (${user.userId} == ${order.userId}) {
                            foundUser = true;
                            document.getElementById('orderUser').textContent = "${user.username} (ID: ${order.userId})";
                        }
                    </c:forEach>
                    
                    if (!foundUser) {
                        document.getElementById('orderUser').textContent = "User ID: ${order.userId}";
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
                                title: "<c:out value='${item.key.title}' escapeXml='true'/>",
                                developer: "<c:out value='${item.key.developer}' escapeXml='true'/>",
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
                    document.getElementById('orderTotal').textContent = '$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00" />';
                }
            </c:forEach>
        }

        // Close modal when clicking the close button
        document.getElementById('closeOrderDetails').addEventListener('click', function() {
            document.getElementById('orderDetailsModal').classList.add('hidden');
        });

        // Close modal when clicking outside the modal content
        document.getElementById('orderDetailsModal').addEventListener('click', function(e) {
            if (e.target === this) {
                this.classList.add('hidden');
            }
        });
    </script>

    <!-- Sidebar Scripts -->
    <jsp:include page="components/adminSidebarScripts.jsp" />
</body>
</html>