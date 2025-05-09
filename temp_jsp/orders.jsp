<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order History - GameVault</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
    <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <style>
        .order-card {
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(99, 102, 241, 0.1);
            transition: all 0.3s ease;
        }
        
        .order-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 8px 20px rgba(99, 102, 241, 0.15);
            border-color: rgba(99, 102, 241, 0.3);
        }

        .status-badge {
            background: linear-gradient(135deg, #10b981, #059669);
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0% { opacity: 0.8; }
            50% { opacity: 1; }
            100% { opacity: 0.8; }
        }

        .timeline-dot {
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background: #6366f1;
            position: relative;
        }

        .timeline-dot::after {
            content: '';
            position: absolute;
            top: 50%;
            left: 12px;
            width: 100%;
            height: 2px;
            background: #e5e7eb;
        }

        .timeline-dot.completed {
            background: #10b981;
        }

        .download-button {
            transition: all 0.3s ease;
        }

        .download-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(99, 102, 241, 0.2);
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
        <div class="mb-12 text-center" data-aos="fade-down">
            <h1 class="text-4xl font-bold mb-6 text-gray-900">Your Gaming Journey</h1>
            
            <!-- Stats Cards -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-4xl mx-auto">
                <div class="stats-card p-6 rounded-xl shadow-lg" data-aos="fade-up" data-aos-delay="100">
                    <div class="text-4xl font-bold text-indigo-600 mb-2">
                        ${fn:length(orderList)}
                    </div>
                    <div class="text-gray-600">Total Orders</div>
                </div>
                <div class="stats-card p-6 rounded-xl shadow-lg" data-aos="fade-up" data-aos-delay="200">
                    <div class="text-4xl font-bold text-purple-600 mb-2">
                        <fmt:formatNumber value="${totalSpent}" type="currency" currencySymbol="$" />
                    </div>
                    <div class="text-gray-600">Total Spent</div>
                </div>
                <div class="stats-card p-6 rounded-xl shadow-lg" data-aos="fade-up" data-aos-delay="300">
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
                    <c:forEach var="order" items="${orderList}" varStatus="status">
                        <div class="order-card rounded-xl overflow-hidden" data-aos="fade-up" data-aos-delay="${status.index * 100}">
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
                                    <span class="status-badge px-3 py-1 rounded-full text-white text-sm font-medium">
                                        Completed
                                    </span>
                                </div>
                            </div>

                            <!-- Order Details -->
                            <div class="p-6">
                                <!-- Order Timeline -->
                                <div class="flex items-center justify-between mb-6">
                                    <div class="flex items-center w-full">
                                        <div class="timeline-dot completed"></div>
                                        <div class="flex-1 ml-4">
                                            <p class="text-sm font-medium text-gray-900">Order Placed</p>
                                            <p class="text-xs text-gray-500">
                                                <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy HH:mm" />
                                            </p>
                                        </div>
                                    </div>
                                    <div class="flex items-center w-full">
                                        <div class="timeline-dot completed"></div>
                                        <div class="flex-1 ml-4">
                                            <p class="text-sm font-medium text-gray-900">Payment Confirmed</p>
                                            <p class="text-xs text-gray-500">
                                                <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy HH:mm" />
                                            </p>
                                        </div>
                                    </div>
                                    <div class="flex items-center w-full">
                                        <div class="timeline-dot completed"></div>
                                        <div class="flex-1 ml-4">
                                            <p class="text-sm font-medium text-gray-900">Games Added to Library</p>
                                            <p class="text-xs text-gray-500">
                                                <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy HH:mm" />
                                            </p>
                                        </div>
                                    </div>
                                </div>

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
                                            <a href="${pageContext.request.contextPath}/game?id=${orderItem.key.gameId}" 
                                               class="download-button px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 inline-flex items-center gap-2">
                                                <i class="bi bi-download"></i>
                                                Download
                                            </a>
                                        </div>
                                    </c:forEach>
                                </div>

                                <!-- Order Actions -->
                                <div class="mt-6 flex justify-end gap-4">
                                    <button onclick="generateInvoice('${order.orderId}')"
                                            class="px-4 py-2 text-gray-600 hover:text-gray-900 transition-colors">
                                        <i class="bi bi-receipt mr-2"></i>
                                        View Invoice
                                    </button>
                                    <button onclick="showOrderDetails('${order.orderId}')"
                                            class="px-4 py-2 text-indigo-600 hover:text-indigo-800 transition-colors">
                                        <i class="bi bi-eye mr-2"></i>
                                        View Details
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="max-w-2xl mx-auto bg-white/80 backdrop-blur-xl rounded-2xl shadow-xl p-12 text-center" data-aos="fade-up">
                    <div class="w-24 h-24 mx-auto mb-6 rounded-full bg-gradient-to-br from-indigo-400 to-purple-500 flex items-center justify-center text-white shadow-xl">
                        <i class="bi bi-bag text-4xl"></i>
                    </div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-2">Your order history is empty</h3>
                    <p class="text-gray-600 mb-8">Start your gaming journey by exploring our collection!</p>
                    <a href="${pageContext.request.contextPath}/browse" 
                       class="inline-flex items-center px-6 py-3 rounded-xl bg-indigo-600 text-white hover:bg-indigo-700 transition-all transform hover:scale-105">
                        <i class="bi bi-controller mr-2"></i>
                        Browse Games
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <jsp:include page="footer.jsp" />

    <script>
        // Initialize AOS
        AOS.init({
            duration: 800,
            once: true
        });

        // Function to show order details
        function showOrderDetails(orderId) {
            // Implement order details view
            alert('Viewing details for order #' + orderId);
        }

        // Function to generate invoice
        function generateInvoice(orderId) {
            // Implement invoice generation
            alert('Generating invoice for order #' + orderId);
        }
    </script>
</body>
</html>