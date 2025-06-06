<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Shopping Cart</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <style>
        .cart-item {
            background: rgba(255, 255, 255, 0.9);
            border: 1px solid rgba(99, 102, 241, 0.1);
            transition: all 0.2s ease;
        }
        
        .cart-item:hover {
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
    </style>
</head>
<body class="bg-gradient-to-br from-slate-50 to-indigo-50 min-h-screen">
    <jsp:include page="header.jsp" />

    <main class="container mx-auto px-4 py-8 mt-20">
        <div class="text-center mb-8">
            <h1 class="text-3xl font-bold mb-2 text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-purple-600">
                Your Shopping Cart
            </h1>
            <p class="text-gray-600">
                Review your selected games and proceed to checkout
            </p>
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
            <c:when test="${not empty cartGames}">
                <div class="max-w-6xl mx-auto">
                    <div class="flex flex-col lg:flex-row gap-8">
                        <!-- Cart Items -->
                        <div class="flex-grow space-y-4">
                            <c:forEach var="game" items="${cartGames}">
                                <div class="cart-item rounded-xl overflow-hidden">
                                    <div class="p-6 flex items-center gap-6">
                                        <!-- Game Image -->
                                        <div class="w-32 h-32 rounded-lg overflow-hidden flex-shrink-0">
                                            <c:choose>
                                                <c:when test="${not empty game.imagePath}">
                                                    <img src="${fn:startsWith(game.imagePath, 'http') ? game.imagePath : pageContext.request.contextPath.concat('/').concat(game.imagePath)}"
                                                         alt="${game.title}"
                                                         class="w-full h-full object-cover">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="w-full h-full bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center">
                                                        <i class="bi bi-controller text-3xl text-white"></i>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <!-- Game Details -->
                                        <div class="flex-grow">
                                            <h3 class="text-xl font-semibold mb-2">
                                                <a href="${pageContext.request.contextPath}/game?id=${game.gameId}" 
                                                   class="text-gray-900 hover:text-indigo-600 transition-colors">
                                                    <c:out value="${game.title}" />
                                                </a>
                                            </h3>
                                            <div class="flex items-center gap-3">
                                                <span class="platform-badge px-3 py-1 rounded-full text-white text-sm">
                                                    ${game.platform}
                                                </span>
                                            </div>
                                        </div>

                                        <!-- Price and Actions -->
                                        <div class="text-right flex flex-col items-end gap-3">
                                            <p class="text-2xl font-bold price-text">
                                                <fmt:formatNumber value="${game.price}" type="currency" currencySymbol="$" />
                                            </p>
                                            <form action="${pageContext.request.contextPath}/removeFromCart" method="post">
                                                <input type="hidden" name="gameId" value="${game.gameId}" />
                                                <button type="submit" 
                                                        class="px-4 py-2 rounded-lg bg-red-500 text-white hover:bg-red-600 inline-flex items-center gap-2">
                                                    <i class="bi bi-trash"></i>
                                                    Remove
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Order Summary -->
                        <div class="lg:w-96">
                            <div class="bg-white rounded-xl p-6 sticky top-24">
                                <h3 class="text-xl font-bold mb-6 pb-3 border-b border-gray-200">Order Summary</h3>
                                
                                <!-- Summary Details -->
                                <div class="space-y-4 mb-6">
                                    <div class="flex justify-between text-gray-600">
                                        <span>Subtotal</span>
                                        <span><fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$" /></span>
                                    </div>
                                    <div class="h-px bg-gray-200"></div>
                                    <div class="flex justify-between text-xl font-bold">
                                        <span>Total</span>
                                        <span class="price-text"><fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$" /></span>
                                    </div>
                                </div>

                                <!-- Checkout Button -->
                                <form action="${pageContext.request.contextPath}/placeOrder" method="post">
                                    <button type="submit" 
                                            class="w-full py-4 text-white font-medium rounded-xl bg-indigo-600 hover:bg-indigo-700 flex items-center justify-center gap-2">
                                        <i class="bi bi-credit-card"></i>
                                        Complete Purchase
                                    </button>
                                </form>

                                <!-- Continue Shopping -->
                                <div class="mt-6 text-center">
                                    <a href="${pageContext.request.contextPath}/home" 
                                       class="text-indigo-600 hover:text-indigo-800 transition-colors inline-flex items-center gap-1">
                                        <i class="bi bi-arrow-left"></i>
                                        Continue Shopping
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <div class="max-w-2xl mx-auto bg-white rounded-2xl shadow-lg p-12 text-center">
                    <div class="w-24 h-24 mx-auto mb-6 rounded-full bg-gradient-to-br from-indigo-400 to-purple-500 flex items-center justify-center text-white">
                        <i class="bi bi-cart text-4xl"></i>
                    </div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-2">Your shopping cart is empty</h3>
                    <p class="text-gray-600 mb-8">Looks like you haven't added any games yet.</p>
                    <a href="${pageContext.request.contextPath}/home" 
                       class="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700">
                        Browse Games
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <jsp:include page="footer.jsp" />
</body>
</html>
