package gamevaultbase.helpers;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.User;
import gamevaultbase.management.GameManagement;
import gamevaultbase.management.UserManagement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Provides common utility methods for handling application logic,
 * particularly operations that are frequently used across multiple servlets.
 * This helps reduce code duplication and maintain consistency.
 */
public class AppUtil {

    /**
     * Sets common page attributes needed on most pages, such as search query,
     * game recommendations, etc.
     * 
     * @param request The HTTP request
     */
    public static void setCommonPageAttributes(HttpServletRequest request) {
        // Preserve search query if it exists
        String searchQuery = request.getParameter("search");
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            request.setAttribute("searchQuery", searchQuery);
        }

        // Add featured games for sidebar or recommendations
        try {
            ServletContext context = request.getServletContext();
            GameManagement gameManagement = (GameManagement) context.getAttribute("gameManagement");

            if (gameManagement != null) {
                List<Game> featuredGames = gameManagement.getFeaturedGames(5);
                request.setAttribute("featuredGames", featuredGames);
            } else {
                System.err.println("GameManagement not found in ServletContext");
            }
        } catch (Exception e) {
            // Log but don't break the page if featured games can't be loaded
            System.err.println("Error loading featured games: " + e.getMessage());
        }
    }

    /**
     * Retrieves the logged-in user from the session and sets it as a request
     * attribute
     * for easy access in JSP pages.
     * 
     * @param request The HTTP request with associated session
     * @return The logged-in User object, or null if no user is logged in
     */
    public static User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser != null) {
                request.setAttribute("user", loggedInUser);
                return loggedInUser;
            }
        }
        return null;
    }

    /**
     * Updates the user object in the session when user information changes
     * (e.g., after a wallet balance update, profile edit, etc.)
     * 
     * @param request The HTTP request
     * @param userId  The ID of the user to refresh
     * @return The updated User object, or null if user couldn't be found
     */
    public static User refreshUserSession(HttpServletRequest request, int userId) {
        try {
            ServletContext context = request.getServletContext();
            UserManagement userManagement = (UserManagement) context.getAttribute("userManagement");

            if (userManagement == null) {
                System.err.println("UserManagement not found in ServletContext");
                return null;
            }

            // Fix: Changed getUserById to getUser to match the actual method name
            User updatedUser = userManagement.getUser(userId);

            if (updatedUser != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("loggedInUser", updatedUser);
                session.setAttribute("isAdmin", updatedUser.isAdmin());
                return updatedUser;
            }
        } catch (Exception e) {
            System.err.println("Error refreshing user session: " + e.getMessage());
        }
        return null;
    }

    /**
     * Sets appropriate page size for pagination based on view type and device
     * detection
     * 
     * @param request     The HTTP request
     * @param defaultSize Default page size to use if not specified
     * @return The appropriate page size
     */
    public static int getPageSize(HttpServletRequest request, int defaultSize) {
        String pageSizeParam = request.getParameter("pageSize");
        if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
            try {
                return Integer.parseInt(pageSizeParam);
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
        }

        // Simple device detection for responsive pagination
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && (userAgent.contains("Mobile") || userAgent.contains("Android"))) {
            // Use smaller page size for mobile devices
            return Math.min(defaultSize, 8);
        }

        return defaultSize;
    }

    /**
     * Constructs a base URL for pagination that preserves all current query
     * parameters
     * except the page parameter
     * 
     * @param request The HTTP request
     * @return A URL string with all parameters except 'page'
     */
    public static String buildPaginationBaseUrl(HttpServletRequest request) {
        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(request.getServletPath()).append("?");

        boolean hasParams = false;

        // Add all parameters except 'page'
        for (String paramName : request.getParameterMap().keySet()) {
            if (!"page".equals(paramName)) {
                String[] values = request.getParameterValues(paramName);
                for (String value : values) {
                    if (hasParams) {
                        baseUrl.append("&");
                    }
                    baseUrl.append(paramName).append("=").append(value);
                    hasParams = true;
                }
            }
        }

        if (hasParams) {
            baseUrl.append("&");
        }

        return baseUrl.toString();
    }
}