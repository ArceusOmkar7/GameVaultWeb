package gamevaultbase.helpers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Utility class for common servlet operations.
 * Helps reduce code duplication and standardize behavior across servlets.
 */
public class ServletUtil {

    /**
     * Checks if a user is logged in and is an admin.
     * If not, redirects to the login page with an error message.
     * 
     * @param request  The HTTP request
     * @param response The HTTP response
     * @return true if the user is an admin, false otherwise
     * @throws IOException If an input or output error occurs
     */
    public static boolean checkAdminAccess(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");

        if (session.getAttribute("loggedInUser") == null || isAdmin == null || !isAdmin) {
            response.sendRedirect(request.getContextPath() +
                    "/login?message=Admin access required&messageType=error");
            return false;
        }

        return true;
    }

    /**
     * Checks if a user is logged in.
     * If not, redirects to the login page with an error message.
     * 
     * @param request  The HTTP request
     * @param response The HTTP response
     * @return true if the user is logged in, false otherwise
     * @throws IOException If an input or output error occurs
     */
    public static boolean checkUserLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() +
                    "/login?message=Login required&messageType=error");
            return false;
        }

        return true;
    }

    /**
     * Forwards the request to a JSP page.
     * 
     * @param request  The HTTP request
     * @param response The HTTP response
     * @param jspPath  The path to the JSP page
     * @throws ServletException If the request could not be handled
     * @throws IOException      If an input or output error occurs
     */
    public static void forwardToJsp(HttpServletRequest request, HttpServletResponse response, String jspPath)
            throws ServletException, IOException {
        request.getRequestDispatcher(jspPath).forward(request, response);
    }

    /**
     * Adds an error message to the request.
     * 
     * @param request The HTTP request
     * @param message The error message
     */
    public static void setErrorMessage(HttpServletRequest request, String message) {
        request.setAttribute("message", message);
        request.setAttribute("messageType", "error");
    }

    /**
     * Adds a success message to the request.
     * 
     * @param request The HTTP request
     * @param message The success message
     */
    public static void setSuccessMessage(HttpServletRequest request, String message) {
        request.setAttribute("message", message);
        request.setAttribute("messageType", "success");
    }

    /**
     * Sets pagination attributes on the request.
     * 
     * @param request      The HTTP request
     * @param currentPage  The current page number
     * @param totalPages   The total number of pages
     * @param itemsPerPage The number of items per page
     */
    public static void setPaginationAttributes(HttpServletRequest request, int currentPage, int totalPages,
            int itemsPerPage) {
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("itemsPerPage", itemsPerPage);

        // Generate pagination URL using AppUtil
        String baseUrl = AppUtil.buildPaginationBaseUrl(request);
        request.setAttribute("baseUrl", baseUrl);
    }

    /**
     * Adds a warning message to the request.
     * 
     * @param request The HTTP request
     * @param message The warning message
     */
    public static void setWarningMessage(HttpServletRequest request, String message) {
        request.setAttribute("message", message);
        request.setAttribute("messageType", "warning");
    }

    /**
     * Adds an info message to the request.
     * 
     * @param request The HTTP request
     * @param message The info message
     */
    public static void setInfoMessage(HttpServletRequest request, String message) {
        request.setAttribute("message", message);
        request.setAttribute("messageType", "info");
    }

    /**
     * Sets up common attributes for all pages, including user authentication
     * status,
     * common page attributes, and any messages from redirect parameters.
     * 
     * @param request The HTTP request
     */
    public static void setupCommonAttributes(HttpServletRequest request) {
        // Set the logged-in user attribute for easy access in JSP
        AppUtil.getLoggedInUser(request);

        // Set common page attributes (search queries, featured games, etc.)
        AppUtil.setCommonPageAttributes(request);

        // Check for message parameters that might be set in redirects
        String message = request.getParameter("message");
        String messageType = request.getParameter("messageType");

        if (message != null && !message.isEmpty()) {
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType != null ? messageType : "info");
        }
    }

    /**
     * Handle a request for a page that requires pagination, setting up all
     * necessary attributes.
     * 
     * @param request         The HTTP request
     * @param totalItems      Total number of items in the collection
     * @param defaultPageSize Default number of items per page
     * @return The current page number (1-based)
     */
    public static int handlePaginationRequest(HttpServletRequest request, int totalItems, int defaultPageSize) {
        // Get the page size (potentially adjusted for mobile devices)
        int pageSize = AppUtil.getPageSize(request, defaultPageSize);

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Get requested page number
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) {
                    currentPage = 1;
                } else if (currentPage > totalPages && totalPages > 0) {
                    currentPage = totalPages;
                }
            } catch (NumberFormatException e) {
                // Invalid page number, use default
            }
        }

        // Set all pagination attributes
        setPaginationAttributes(request, currentPage, totalPages, pageSize);

        return currentPage;
    }
}