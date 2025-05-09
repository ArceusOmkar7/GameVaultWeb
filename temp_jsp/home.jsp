<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameVault - Your Digital Gaming Paradise</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
    <script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
    <style>
        @keyframes float {
            0% { transform: translateY(0px); }
            50% { transform: translateY(-10px); }
            100% { transform: translateY(0px); }
        }

        @keyframes glow {
            0% { box-shadow: 0 0 5px rgba(99, 102, 241, 0.4); }
            50% { box-shadow: 0 0 20px rgba(99, 102, 241, 0.6); }
            100% { box-shadow: 0 0 5px rgba(99, 102, 241, 0.4); }
        }

        .floating {
            animation: float 3s ease-in-out infinite;
        }

        .glow-effect {
            animation: glow 2s ease-in-out infinite;
        }

        .cyber-border {
            border: 2px solid transparent;
            background: linear-gradient(45deg, #4f46e5, #7c3aed, #2563eb);
            background-clip: padding-box;
            position: relative;
        }

        .cyber-border::after {
            content: '';
            position: absolute;
            top: -2px; right: -2px; bottom: -2px; left: -2px;
            background: linear-gradient(45deg, #4f46e5, #7c3aed, #2563eb);
            z-index: -1;
            border-radius: inherit;
        }

        .game-card {
            background: rgba(17, 24, 39, 0.8);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(99, 102, 241, 0.2);
            transition: all 0.4s ease;
        }

        .game-card:hover {
            transform: translateY(-8px) scale(1.02);
            border-color: rgba(99, 102, 241, 0.8);
            box-shadow: 0 8px 25px rgba(99, 102, 241, 0.2);
        }

        .neon-text {
            text-shadow: 0 0 10px rgba(99, 102, 241, 0.8);
        }

        .parallax-bg {
            background-attachment: fixed;
            background-position: center;
            background-repeat: no-repeat;
            background-size: cover;
        }

        .category-card {
            background: rgba(17, 24, 39, 0.9);
            backdrop-filter: blur(8px);
            border: 1px solid rgba(99, 102, 241, 0.3);
            transition: all 0.3s ease;
        }

        .category-card:hover {
            transform: translateY(-5px);
            border-color: rgba(99, 102, 241, 0.8);
            box-shadow: 0 5px 15px rgba(99, 102, 241, 0.3);
        }

        .price-badge {
            background: linear-gradient(45deg, #4f46e5, #7c3aed);
            padding: 0.5rem 1rem;
            border-radius: 9999px;
            font-weight: 600;
            color: white;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3);
        }

        .custom-scroll::-webkit-scrollbar {
            width: 6px;
            height: 6px;
        }

        .custom-scroll::-webkit-scrollbar-thumb {
            background: #4f46e5;
            border-radius: 3px;
        }

        .custom-scroll::-webkit-scrollbar-track {
            background: rgba(17, 24, 39, 0.8);
        }
    </style>
</head>
<body class="bg-gray-900 text-white min-h-screen flex flex-col custom-scroll">
    <div class="fixed inset-0 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-gray-900 via-gray-900 to-indigo-900 -z-10"></div>
    
    <jsp:include page="header.jsp" />

    <main class="flex-grow pt-20">
        <!-- Hero Section with Animated Background -->
        <div class="relative overflow-hidden mb-16">
            <div class="absolute inset-0 bg-gradient-to-b from-indigo-900/20 to-gray-900"></div>
            <div class="container mx-auto px-4 py-16 relative">
                <div class="text-center mb-12" data-aos="fade-down">
                    <h1 class="text-7xl font-bold mb-6 bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 via-purple-400 to-pink-400 inline-block neon-text">
                        GameVault
                    </h1>
                    <p class="text-xl text-gray-300 mb-8 max-w-2xl mx-auto">
                        Your portal to endless gaming adventures. Discover, collect, and play the best digital games.
                    </p>
                    <div class="flex justify-center gap-4">
                        <a href="#featured" class="cyber-border px-8 py-3 rounded-lg bg-indigo-600 hover:bg-indigo-700 transition-all">
                            Explore Games
                        </a>
                        <c:if test="${empty sessionScope.loggedInUser}">
                            <a href="${pageContext.request.contextPath}/register" class="px-8 py-3 rounded-lg bg-gray-800 hover:bg-gray-700 transition-all border border-indigo-500/30">
                                Join Now
                            </a>
                        </c:if>
                    </div>
                </div>

                <!-- Categories Section -->
                <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-16" data-aos="fade-up">
                    <a href="#action-games" class="category-card p-6 rounded-xl text-center transition-all hover:bg-gray-800/50">
                        <i class="bi bi-controller text-4xl mb-3 text-indigo-400"></i>
                        <h3 class="font-semibold">Action</h3>
                        <p class="text-sm text-gray-400">Thrilling adventures</p>
                    </a>
                    <a href="#strategy-games" class="category-card p-6 rounded-xl text-center transition-all hover:bg-gray-800/50">
                        <i class="bi bi-puzzle text-4xl mb-3 text-purple-400"></i>
                        <h3 class="font-semibold">Strategy</h3>
                        <p class="text-sm text-gray-400">Test your mind</p>
                    </a>
                    <div class="category-card p-6 rounded-xl text-center">
                        <i class="bi bi-trophy text-4xl mb-3 text-blue-400"></i>
                        <h3 class="font-semibold">Sports</h3>
                        <p class="text-sm text-gray-400">Compete & win</p>
                    </div>
                    <div class="category-card p-6 rounded-xl text-center">
                        <i class="bi bi-heart text-4xl mb-3 text-pink-400"></i>
                        <h3 class="font-semibold">RPG</h3>
                        <p class="text-sm text-gray-400">Epic stories</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Featured Games Section -->
        <section id="featured" class="container mx-auto px-4 mb-16">
            <div class="flex justify-between items-center mb-8">
                <h2 class="text-3xl font-bold" data-aos="fade-right">Featured Games</h2>
                <div class="flex gap-4" data-aos="fade-left">
                    <button class="scroll-left px-4 py-2 rounded-lg bg-gray-800 hover:bg-gray-700">
                        <i class="bi bi-chevron-left"></i>
                    </button>
                    <button class="scroll-right px-4 py-2 rounded-lg bg-gray-800 hover:bg-gray-700">
                        <i class="bi bi-chevron-right"></i>
                    </button>
                </div>
            </div>

            <div class="featured-games-scroll overflow-x-auto custom-scroll">
                <div class="flex gap-6 pb-4" style="width: max-content;">
                    <c:forEach var="game" items="${featuredGamesList}" varStatus="status">
                        <div class="game-card w-72 rounded-xl overflow-hidden" data-aos="fade-up" data-aos-delay="${status.index * 100}">
                            <div class="relative">
                                <c:choose>
                                    <c:when test="${not empty game.imagePath}">
                                        <img src="${fn:startsWith(game.imagePath, 'http') ? game.imagePath : pageContext.request.contextPath.concat('/').concat(game.imagePath)}"
                                             alt="${game.title}"
                                             class="w-full h-40 object-cover">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="w-full h-40 bg-gray-800 flex items-center justify-center">
                                            <i class="bi bi-controller text-4xl text-gray-600"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                <div class="absolute top-2 right-2">
                                    <span class="price-badge">
                                        $<fmt:formatNumber value="${game.price}" pattern="#,##0.00" />
                                    </span>
                                </div>
                            </div>
                            <div class="p-4">
                                <h3 class="font-semibold mb-2 line-clamp-1">${game.title}</h3>
                                <div class="flex flex-wrap gap-2 mb-3">
                                    <c:forEach var="genre" items="${game.genre}" end="2">
                                        <span class="text-xs px-2 py-1 rounded-full bg-gray-800 text-gray-300">
                                            ${genre}
                                        </span>
                                    </c:forEach>
                                </div>
                                <div class="flex justify-between items-center">
                                    <a href="${pageContext.request.contextPath}/game?id=${game.gameId}"
                                       class="text-sm bg-indigo-600 hover:bg-indigo-700 px-4 py-2 rounded-lg transition-all">
                                        View Details
                                    </a>
                                    <c:if test="${not empty sessionScope.loggedInUser}">
                                        <form action="${pageContext.request.contextPath}/addToCart" method="post" class="inline">
                                            <input type="hidden" name="gameId" value="${game.gameId}" />
                                            <button type="submit" class="text-indigo-400 hover:text-indigo-300 transition-colors">
                                                <i class="bi bi-cart-plus text-xl"></i>
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- Action Games Section -->
        <section id="action-games" class="container mx-auto px-4 mb-16">
            <div class="flex justify-between items-center mb-8">
                <h2 class="text-3xl font-bold" data-aos="fade-right">Action Games</h2>
                <a href="${pageContext.request.contextPath}/browse?filter-genre=Action" 
                   class="text-indigo-400 hover:text-indigo-300 flex items-center gap-2 transition-colors"
                   data-aos="fade-left">
                    View All <i class="bi bi-arrow-right"></i>
                </a>
            </div>
            
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                <c:forEach var="game" items="${actionGames}" end="7">
                    <div class="game-card rounded-xl overflow-hidden" data-aos="fade-up">
                        <div class="relative">
                            <c:choose>
                                <c:when test="${not empty game.imagePath}">
                                    <img src="${fn:startsWith(game.imagePath, 'http') ? game.imagePath : pageContext.request.contextPath.concat('/').concat(game.imagePath)}"
                                         alt="${game.title}"
                                         class="w-full h-48 object-cover">
                                </c:when>
                                <c:otherwise>
                                    <div class="w-full h-48 bg-gray-800 flex items-center justify-center">
                                        <i class="bi bi-controller text-4xl text-gray-600"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="p-4">
                            <h3 class="font-semibold mb-2 line-clamp-1">${game.title}</h3>
                            <div class="flex justify-between items-center">
                                <span class="price-badge text-sm">
                                    $<fmt:formatNumber value="${game.price}" pattern="#,##0.00" />
                                </span>
                                <c:if test="${not empty sessionScope.loggedInUser}">
                                    <form action="${pageContext.request.contextPath}/addToCart" method="post">
                                        <input type="hidden" name="gameId" value="${game.gameId}" />
                                        <button type="submit" class="text-indigo-400 hover:text-indigo-300 transition-colors">
                                            <i class="bi bi-cart-plus text-xl"></i>
                                        </button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>

        <!-- Strategy Games Section -->
        <section id="strategy-games" class="container mx-auto px-4 mb-16">
            <div class="flex justify-between items-center mb-8">
                <h2 class="text-3xl font-bold" data-aos="fade-right">Strategy Games</h2>
                <a href="${pageContext.request.contextPath}/browse?filter-genre=Strategy" 
                   class="text-indigo-400 hover:text-indigo-300 flex items-center gap-2 transition-colors"
                   data-aos="fade-left">
                    View All <i class="bi bi-arrow-right"></i>
                </a>
            </div>
            
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                <c:forEach var="game" items="${strategyGames}" end="7">
                    <div class="game-card rounded-xl overflow-hidden" data-aos="fade-up">
                        <div class="relative">
                            <c:choose>
                                <c:when test="${not empty game.imagePath}">
                                    <img src="${fn:startsWith(game.imagePath, 'http') ? game.imagePath : pageContext.request.contextPath.concat('/').concat(game.imagePath)}"
                                         alt="${game.title}"
                                         class="w-full h-48 object-cover">
                                </c:when>
                                <c:otherwise>
                                    <div class="w-full h-48 bg-gray-800 flex items-center justify-center">
                                        <i class="bi bi-controller text-4xl text-gray-600"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="p-4">
                            <h3 class="font-semibold mb-2 line-clamp-1">${game.title}</h3>
                            <div class="flex justify-between items-center">
                                <span class="price-badge text-sm">
                                    $<fmt:formatNumber value="${game.price}" pattern="#,##0.00" />
                                </span>
                                <c:if test="${not empty sessionScope.loggedInUser}">
                                    <form action="${pageContext.request.contextPath}/addToCart" method="post">
                                        <input type="hidden" name="gameId" value="${game.gameId}" />
                                        <button type="submit" class="text-indigo-400 hover:text-indigo-300 transition-colors">
                                            <i class="bi bi-cart-plus text-xl"></i>
                                        </button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>

        <!-- Daily Deals Section -->
        <section class="relative py-16 mb-16 parallax-bg" style="background-image: url('https://images.unsplash.com/photo-1542751371-adc38448a05e?ixlib=rb-1.2.1&auto=format&fit=crop&w=2850&q=80');">
            <div class="absolute inset-0 bg-gray-900/80 backdrop-blur-sm"></div>
            <div class="container mx-auto px-4 relative">
                <div class="text-center mb-12" data-aos="fade-up">
                    <h2 class="text-3xl font-bold mb-4">Daily Deals</h2>
                    <p class="text-gray-300">Limited time offers on amazing games!</p>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                    <c:forEach var="game" items="${allGamesList}" begin="0" end="2">
                        <div class="game-card rounded-xl overflow-hidden" data-aos="fade-up">
                            <div class="relative">
                                <c:choose>
                                    <c:when test="${not empty game.imagePath}">
                                        <img src="${fn:startsWith(game.imagePath, 'http') ? game.imagePath : pageContext.request.contextPath.concat('/').concat(game.imagePath)}"
                                             alt="${game.title}"
                                             class="w-full h-48 object-cover">
                                        <div class="absolute top-2 right-2">
                                            <span class="bg-red-500 text-white px-3 py-1 rounded-full text-sm font-semibold">
                                                -30%
                                            </span>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="w-full h-48 bg-gray-800 flex items-center justify-center">
                                            <i class="bi bi-controller text-4xl text-gray-600"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="p-6">
                                <h3 class="text-xl font-semibold mb-2">${game.title}</h3>
                                <div class="flex items-center gap-2 mb-4">
                                    <span class="text-gray-400 line-through">$<fmt:formatNumber value="${game.price * 1.3}" pattern="#,##0.00" /></span>
                                    <span class="text-2xl font-bold text-white">$<fmt:formatNumber value="${game.price}" pattern="#,##0.00" /></span>
                                </div>
                                <div class="flex justify-between items-center">
                                    <c:if test="${not empty sessionScope.loggedInUser}">
                                        <form action="${pageContext.request.contextPath}/addToCart" method="post" class="w-full">
                                            <input type="hidden" name="gameId" value="${game.gameId}" />
                                            <button type="submit" 
                                                    class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 px-4 rounded-lg transition-all flex items-center justify-center gap-2">
                                                <i class="bi bi-cart-plus"></i> Add to Cart
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- Latest Games Grid -->
        <section class="container mx-auto px-4 mb-16">
            <div class="flex justify-between items-center mb-8">
                <h2 class="text-3xl font-bold" data-aos="fade-right">Latest Games</h2>
                <a href="${pageContext.request.contextPath}/browse" 
                   class="text-indigo-400 hover:text-indigo-300 flex items-center gap-2 transition-colors"
                   data-aos="fade-left">
                    View All <i class="bi bi-arrow-right"></i>
                </a>
            </div>
            
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
                <c:forEach var="game" items="${allGamesList}" begin="0" end="9" varStatus="status">
                    <div class="game-card" data-aos="fade-up" data-aos-delay="${status.index * 50}">
                        <a href="${pageContext.request.contextPath}/game?id=${game.gameId}" class="block relative group">
                            <c:choose>
                                <c:when test="${not empty game.imagePath}">
                                    <img src="${fn:startsWith(game.imagePath, 'http') ? game.imagePath : pageContext.request.contextPath.concat('/').concat(game.imagePath)}"
                                         alt="${game.title}"
                                         class="w-full h-48 object-cover">
                                    <div class="absolute inset-0 bg-indigo-600/0 group-hover:bg-indigo-600/20 transition-all duration-300"></div>
                                </c:when>
                                <c:otherwise>
                                    <div class="w-full h-48 bg-gray-800 flex items-center justify-center">
                                        <i class="bi bi-controller text-4xl text-gray-600"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </a>
                        <div class="p-4">
                            <h3 class="font-semibold text-lg mb-2 line-clamp-1">
                                <a href="${pageContext.request.contextPath}/game?id=${game.gameId}" 
                                   class="hover:text-indigo-400 transition-colors">
                                    ${game.title}
                                </a>
                            </h3>
                            <div class="flex items-center justify-between mb-3">
                                <span class="price-badge text-sm">
                                    $<fmt:formatNumber value="${game.price}" pattern="#,##0.00" />
                                </span>
                                <c:if test="${not empty sessionScope.loggedInUser}">
                                    <form action="${pageContext.request.contextPath}/addToCart" method="post">
                                        <input type="hidden" name="gameId" value="${game.gameId}" />
                                        <button type="submit" class="text-indigo-400 hover:text-indigo-300 transition-colors">
                                            <i class="bi bi-cart-plus text-xl"></i>
                                        </button>
                                    </form>
                                </c:if>
                            </div>
                            <div class="flex flex-wrap gap-2">
                                <c:forEach var="platform" items="${game.platform}" end="1">
                                    <span class="text-xs px-2 py-1 rounded-full bg-gray-800 text-gray-300">
                                        ${platform}
                                    </span>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>

        <!-- Newsletter Section -->
        <section class="bg-gradient-to-r from-indigo-900 to-purple-900 py-16">
            <div class="container mx-auto px-4 text-center">
                <div class="max-w-2xl mx-auto">
                    <h2 class="text-3xl font-bold mb-4" data-aos="fade-up">Stay in the Game</h2>
                    <p class="text-gray-300 mb-8" data-aos="fade-up" data-aos-delay="100">
                        Subscribe to our newsletter for exclusive deals, new releases, and gaming news!
                    </p>
                    <form class="flex gap-4 max-w-md mx-auto" data-aos="fade-up" data-aos-delay="200">
                        <input type="email" 
                               placeholder="Enter your email" 
                               class="flex-1 px-4 py-3 rounded-lg bg-gray-800 border border-gray-700 text-white placeholder-gray-400 focus:outline-none focus:border-indigo-500">
                        <button type="submit" 
                                class="px-6 py-3 rounded-lg bg-indigo-600 hover:bg-indigo-700 transition-all">
                            Subscribe
                        </button>
                    </form>
                </div>
            </div>
        </section>
    </main>

    <jsp:include page="footer.jsp" />
    
    <script>
        // Initialize AOS
        AOS.init({
            duration: 800,
            once: true
        });

        // Horizontal scroll buttons for featured games
        document.querySelector('.scroll-left').addEventListener('click', () => {
            document.querySelector('.featured-games-scroll').scrollBy({
                left: -300,
                behavior: 'smooth'
            });
        });

        document.querySelector('.scroll-right').addEventListener('click', () => {
            document.querySelector('.featured-games-scroll').scrollBy({
                left: 300,
                behavior: 'smooth'
            });
        });

        // Smooth scroll
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                document.querySelector(this.getAttribute('href')).scrollIntoView({
                    behavior: 'smooth'
                });
            });
        });

        // Parallax effect for background images
        window.addEventListener('scroll', () => {
            const parallaxElements = document.querySelectorAll('.parallax-bg');
            parallaxElements.forEach(element => {
                const scrolled = window.pageYOffset;
                const rate = scrolled * 0.5;
                element.style.backgroundPosition = `center ${rate}px`;
            });
        });
    </script>
</body>
</html>