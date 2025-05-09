<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Cart - GameVault</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
    <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <style>
        .cart-item {
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(99, 102, 241, 0.1);
            transition: all 0.3s ease;
        }
        
        .cart-item:hover {
            transform: translateX(8px);
            box-shadow: 0 8px 20px rgba(99, 102, 241, 0.15);
            border-color: rgba(99, 102, 241, 0.3);
        }

        .game-image {
            transition: all 0.5s ease;
        }

        .cart-item:hover .game-image {
            transform: scale(1.05);
        }

        .remove-btn {
            transition: all 0.2s ease;
        }

        .remove-btn:hover {
            transform: scale(1.05);
            box-shadow: 0 4px 12px rgba(239, 68, 68, 0.2);
        }

        .summary-card {
            background: rgba(255, 255, 255, 0.8);
            backdrop-filter: blur(8px);
            border: 1px solid rgba(99, 102, 241, 0.1);
        }

        .checkout-btn {
            background: linear-gradient(135deg, #4f46e5, #7c3aed);
            transition: all 0.3s ease;
        }

        .checkout-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(99, 102, 241, 0.3);
            background: linear-gradient(135deg, #4338ca, #6d28d9);
        }

        .platform-badge {
            background: linear-gradient(135deg, #3b82f6, #6366f1);
        }

        @keyframes float {
            0% { transform: translateY(0px); }
            50% { transform: translateY(-10px); }
            100% { transform: translateY(0px); }
        }

        .float-animation {
            animation: float 3s ease-in-out infinite;
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
        <div class="text-center mb-12" data-aos="fade-down">
            <h1 class="text-4xl md:text-5xl font-bold mb-4 text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-purple-600">
                Your Shopping Cart
            </h1>
            <p class="text-gray-600 max-w-2xl mx-auto">
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

        <c:choose>
            <c:when test="${not empty cartGames}">
                <div class="max-w-6xl mx-auto">
                    <div class="flex flex-col lg:flex-row gap-8">
                        <!-- Cart Items -->
                        <div class="flex-grow space-y-4">
                            <c:forEach var="game" items="${cartGames}" varStatus="status">
                                <div class="cart-item rounded-xl overflow-hidden" data-aos="fade-right" data-aos-delay="${status.index * 100}">
                                    <div class="p-6 flex items-center gap-6">
                                        <!-- Game Image -->
                                        <div class="w-32 h-32 rounded-lg overflow-hidden flex-shrink-0">
                                            <c:choose>
                                                <c:when test="${not empty game.imagePath}">
                                                    <img src="${fn:startsWith(game.imagePath, 'http') ? game.imagePath : pageContext.request.contextPath.concat('/').concat(game.imagePath)}"
                                                         alt="${game.title}"
                                                         class="game-image w-full h-full object-cover">
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
                                            <div class="flex items-center gap-3 mb-3">
                                                <span class="platform-badge px-3 py-1 rounded-full text-white text-sm">
                                                    ${game.platform}
                                                </span>
                                                <c:forEach var="genre" items="${game.genre}" end="2">
                                                    <span class="text-xs px-2 py-1 rounded-full bg-gray-100 text-gray-600">
                                                        ${genre}
                                                    </span>
                                                </c:forEach>
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
                                                        class="remove-btn px-4 py-2 rounded-lg bg-red-500 text-white hover:bg-red-600 inline-flex items-center gap-2">
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
                            <div class="summary-card rounded-xl p-6 sticky top-24" data-aos="fade-left">
                                <h3 class="text-xl font-bold mb-6 pb-3 border-b border-gray-200">Order Summary</h3>
                                
                                <!-- Summary Details -->
                                <div class="space-y-4 mb-6">
                                    <div class="flex justify-between text-gray-600">
                                        <span>Subtotal</span>
                                        <span><fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$" /></span>
                                    </div>
                                    <div class="flex justify-between text-gray-600">
                                        <span>Tax</span>
                                        <span>$0.00</span>
                                    </div>
                                    <div class="flex justify-between text-gray-600">
                                        <span>Platform Fee</span>
                                        <span>$0.00</span>
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
                                            class="checkout-btn w-full py-4 text-white font-medium rounded-xl flex items-center justify-center gap-2">
                                        <i class="bi bi-credit-card"></i>
                                        Complete Purchase
                                    </button>
                                </form>

                                <!-- Secure Transaction Notice -->
                                <div class="mt-6 text-center">
                                    <p class="text-sm text-gray-500 flex items-center justify-center gap-2">
                                        <i class="bi bi-shield-check"></i>
                                        Secure transaction
                                    </p>
                                </div>

                                <!-- Continue Shopping -->
                                <div class="mt-6 text-center">
                                    <a href="${pageContext.request.contextPath}/browse" 
                                       class="text-indigo-600 hover:text-indigo-800 transition-colors inline-flex items-center gap-1">
                                        <i class="bi bi-arrow-left"></i>
                                        Continue Shopping
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Related Games -->
                    <div class="mt-12" data-aos="fade-up">
                        <h2 class="text-2xl font-bold mb-6">You Might Also Like</h2>
                        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                            <c:forEach var="relatedGame" items="${relatedGames}" end="3">
                                <div class="cart-item rounded-xl overflow-hidden">
                                    <div class="relative">
                                        <img src="${relatedGame.imagePath}" alt="${relatedGame.title}" class="w-full h-48 object-cover">
                                        <div class="absolute bottom-2 right-2">
                                            <span class="platform-badge px-3 py-1 rounded-full text-white text-sm">
                                                ${relatedGame.platform}
                                            </span>
                                        </div>
                                    </div>
                                    <div class="p-4">
                                        <h3 class="font-semibold text-gray-900">${relatedGame.title}</h3>
                                        <div class="flex justify-between items-center mt-2">
                                            <span class="price-text font-bold">
                                                $<fmt:formatNumber value="${relatedGame.price}" pattern="#,##0.00" />
                                            </span>
                                            <a href="${pageContext.request.contextPath}/game?id=${relatedGame.gameId}" 
                                               class="text-indigo-600 hover:text-indigo-800">
                                                View Details
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="max-w-2xl mx-auto bg-white/80 backdrop-blur-xl rounded-2xl shadow-xl p-12 text-center" data-aos="fade-up">
                    <div class="w-24 h-24 mx-auto mb-6 rounded-full bg-gradient-to-br from-indigo-400 to-purple-500 flex items-center justify-center text-white shadow-xl float-animation">
                        <i class="bi bi-cart text-4xl"></i>
                    </div>
                    <h3 class="text-2xl font-bold text-gray-900 mb-2">Your cart is empty</h3>
                    <p class="text-gray-600 mb-8">Time to discover your next favorite game!</p>
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
    </script>
</body>
</html>