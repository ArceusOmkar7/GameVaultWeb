<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Shopping Cart</title>
    <%-- Include Tailwind CSS via CDN --%>
    <script src="https://cdn.tailwindcss.com"></script>
    <%-- Optional: Link to your custom CSS for overrides --%>
    <%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"> --%>
</head>
<body class="bg-gray-100">

    <%-- Include Header --%>
    <jsp:include page="header.jsp" />

    <%-- Main Cart Content Area --%>
    <div class="container mx-auto px-4 py-8">
        <h2 class="text-3xl font-bold mb-6 text-gray-800">Your Shopping Cart</h2>

        <%-- Display Messages (e.g., from adding/removing items, placing order) --%>
        <c:if test="${not empty message}">
            <div class="mb-4 p-4 rounded ${messageType eq 'success' ? 'bg-green-100 border border-green-400 text-green-700' : 'bg-red-100 border border-red-400 text-red-700'}" role="alert">
                <p><c:out value="${message}" /></p>
            </div>
        </c:if>
        <%-- Display specific error messages related to cart loading --%>
        <c:if test="${not empty errorMessage}">
             <div class="mb-4 p-4 rounded bg-red-100 border border-red-400 text-red-700" role="alert">
                <p><strong>Error:</strong> <c:out value="${errorMessage}" /></p>
             </div>
        </c:if>

        <%-- Check if the cart has items --%>
        <c:choose>
            <%-- Case 1: Cart is NOT Empty --%>
            <c:when test="${not empty cartGames}">
                <div class="bg-white shadow-md rounded-lg overflow-hidden">
                    <%-- List of Games in Cart --%>
                    <ul role="list" class="divide-y divide-gray-200">
                        <c:forEach var="game" items="${cartGames}">
                             <li class="p-4 sm:p-6 flex items-center">
                                 <%-- Game Image Placeholder --%>
                                 <div class="flex-shrink-0 w-24 h-24 bg-gray-200 rounded-md flex items-center justify-center text-gray-400 mr-4">
                                     <svg class="h-12 w-12 text-gray-300" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor">
                                       <path stroke-linecap="round" stroke-linejoin="round" d="m2.25 15.75 5.159-5.159a2.25 2.25 0 0 1 3.182 0l5.159 5.159m-1.5-1.5 1.409-1.409a2.25 2.25 0 0 1 3.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 0 0 1.5-1.5V6a1.5 1.5 0 0 0-1.5-1.5H3.75A1.5 1.5 0 0 0 2.25 6v12a1.5 1.5 0 0 0 1.5 1.5Zm10.5-11.25h.008v.008h-.008V8.25Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z" />
                                     </svg>
                                 </div>
                                 <%-- Game Details --%>
                                 <div class="flex-1 flex justify-between items-start">
                                     <div>
                                         <%-- Game Title (Link to Detail Page) --%>
                                         <h3 class="text-lg font-medium text-gray-900">
                                             <a href="${pageContext.request.contextPath}/game?id=${game.gameId}" class="hover:text-indigo-600">
                                                 <c:out value="${game.title}" />
                                             </a>
                                         </h3>
                                         <%-- Platform --%>
                                         <p class="text-sm text-gray-500 mt-1"><c:out value="${game.platform}" /></p>
                                     </div>
                                     <div class="text-right ml-4 flex flex-col items-end">
                                         <%-- Price --%>
                                         <p class="text-lg font-medium text-gray-900">
                                             <fmt:formatNumber value="${game.price}" type="currency" currencySymbol="$" />
                                         </p>
                                          <%-- Remove Button Form --%>
                                          <form action="${pageContext.request.contextPath}/removeFromCart" method="post" class="mt-2">
                                              <input type="hidden" name="gameId" value="${game.gameId}" />
                                              <button type="submit" class="text-sm font-medium text-red-600 hover:text-red-500 transition duration-150 ease-in-out">
                                                  Remove
                                              </button>
                                          </form>
                                     </div>
                                 </div>
                             </li>
                        </c:forEach>
                    </ul>

                    <%-- Subtotal and Checkout Actions Section --%>
                    <div class="border-t border-gray-200 py-6 px-4 sm:px-6">
                        <div class="flex justify-between text-lg font-medium text-gray-900 mb-4">
                            <p>Subtotal</p>
                            <%-- Display Calculated Subtotal --%>
                            <p><fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$" /></p>
                        </div>
                         <p class="mb-4 text-sm text-gray-500">Shipping and taxes calculated at checkout (if applicable).</p>
                         <%-- Purchase Button Form --%>
                         <form action="${pageContext.request.contextPath}/placeOrder" method="post" class="mb-4">
                             <button type="submit" class="w-full flex justify-center items-center px-6 py-3 border border-transparent rounded-md shadow-sm text-base font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition duration-150 ease-in-out">
                                 Purchase for myself (<fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$" />)
                             </button>
                         </form>
                        <%-- Continue Shopping Link --%>
                        <div class="mt-6 text-center text-sm">
                            <a href="${pageContext.request.contextPath}/home" class="font-medium text-indigo-600 hover:text-indigo-500 transition duration-150 ease-in-out">
                                Or continue shopping<span aria-hidden="true"> →</span>
                            </a>
                        </div>
                    </div>
                </div>
            </c:when>

            <%-- Case 2: Cart is Empty --%>
            <c:otherwise>
                <div class="text-center bg-white p-10 rounded-lg shadow-md">
                     <%-- Empty Cart Icon --%>
                     <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                       <path vector-effect="non-scaling-stroke" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                     </svg>
                     <h3 class="mt-2 text-lg font-medium text-gray-900">Your shopping cart is empty</h3>
                     <p class="mt-1 text-sm text-gray-500">Looks like you haven't added any games yet.</p>
                     <div class="mt-6">
                       <%-- Browse Games Button --%>
                       <a href="${pageContext.request.contextPath}/home" class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition duration-150 ease-in-out">
                         Browse Games
                       </a>
                     </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div> <%-- End Container --%>

    <%-- Include Footer --%>
    <jsp:include page="footer.jsp" />

</body>
</html>