package gamevaultbase.servlets.base;

import gamevaultbase.entities.User;
import gamevaultbase.helpers.ServletUtil;
import gamevaultbase.management.CartManagement;
import gamevaultbase.management.GameManagement;
import gamevaultbase.management.OrderManagement;
import gamevaultbase.management.UserManagement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Base abstract servlet that provides common functionality for all servlets.
 * Extends HttpServlet and provides utility methods to reduce code duplication.
 */
public abstract class BaseServlet extends HttpServlet {

    /**
     * Get the GameManagement instance from ServletContext
     * 
     * @return GameManagement instance
     */
    protected GameManagement getGameManagement() {
        return (GameManagement) getServletContext().getAttribute("gameManagement");
    }

    /**
     * Get the UserManagement instance from ServletContext
     * 
     * @return UserManagement instance
     */
    protected UserManagement getUserManagement() {
        return (UserManagement) getServletContext().getAttribute("userManagement");
    }

    /**
     * Get the CartManagement instance from ServletContext
     * 
     * @return CartManagement instance
     */
    protected CartManagement getCartManagement() {
        return (CartManagement) getServletContext().getAttribute("cartManagement");
    }

    /**
     * Get the OrderManagement instance from ServletContext
     * 
     * @return OrderManagement instance
     */
    protected OrderManagement getOrderManagement() {
        return (OrderManagement) getServletContext().getAttribute("orderManagement");
    }

    /**
     * Get the currently logged in user from the session
     * 
     * @param request HTTP request
     * @return User object or null if not logged in
     */
    protected User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("loggedInUser");
        }
        return null;
    }

    /**
     * Check if the current user is logged in
     * 
     * @param request HTTP request
     * @return true if logged in, false otherwise
     */
    protected boolean isLoggedIn(HttpServletRequest request) {
        return getLoggedInUser(request) != null;
    }

    /**
     * Check if the current user is an admin
     * 
     * @param request HTTP request
     * @return true if admin, false otherwise
     */
    protected boolean isAdmin(HttpServletRequest request) {
        User user = getLoggedInUser(request);
        return user != null && user.isAdmin();
    }

    /**
     * Forward the request to a JSP page
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @param jspPath  Path to the JSP page
     * @throws ServletException If forwarding fails
     * @throws IOException      If I/O error occurs
     */
    protected void forwardToJsp(HttpServletRequest request, HttpServletResponse response, String jspPath)
            throws ServletException, IOException {
        ServletUtil.forwardToJsp(request, response, jspPath);
    }

    /**
     * Set error message in request attributes
     * 
     * @param request HTTP request
     * @param message Error message
     */
    protected void setErrorMessage(HttpServletRequest request, String message) {
        ServletUtil.setErrorMessage(request, message);
    }

    /**
     * Set success message in request attributes
     * 
     * @param request HTTP request
     * @param message Success message
     */
    protected void setSuccessMessage(HttpServletRequest request, String message) {
        ServletUtil.setSuccessMessage(request, message);
    }

    /**
     * Set warning message in request attributes
     * 
     * @param request HTTP request
     * @param message Warning message
     */
    protected void setWarningMessage(HttpServletRequest request, String message) {
        ServletUtil.setWarningMessage(request, message);
    }

    /**
     * Set info message in request attributes
     * 
     * @param request HTTP request
     * @param message Info message
     */
    protected void setInfoMessage(HttpServletRequest request, String message) {
        ServletUtil.setInfoMessage(request, message);
    }

    /**
     * Setup common attributes for all pages
     * 
     * @param request HTTP request
     */
    protected void setupCommonAttributes(HttpServletRequest request) {
        ServletUtil.setupCommonAttributes(request);
    }

    /**
     * Redirect to another page
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @param path     Path to redirect to
     * @throws IOException If redirection fails
     */
    protected void redirectTo(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    /**
     * Redirect with a message
     * 
     * @param request     HTTP request
     * @param response    HTTP response
     * @param path        Path to redirect to
     * @param message     Message to display
     * @param messageType Type of message (error, success, warning, info)
     * @throws IOException If redirection fails
     */
    protected void redirectWithMessage(HttpServletRequest request, HttpServletResponse response,
            String path, String message, String messageType) throws IOException {
        response.sendRedirect(request.getContextPath() + path + "?message=" +
                java.net.URLEncoder.encode(message, "UTF-8") + "&messageType=" + messageType);
    }

    /**
     * Get parameter as integer with default value
     * 
     * @param request      HTTP request
     * @param paramName    Parameter name
     * @param defaultValue Default value if parameter is not present or invalid
     * @return Parameter value as integer
     */
    protected int getParameterAsInt(HttpServletRequest request, String paramName, int defaultValue) {
        String paramValue = request.getParameter(paramName);
        if (paramValue != null && !paramValue.isEmpty()) {
            try {
                return Integer.parseInt(paramValue);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Handle pagination request
     * 
     * @param request         HTTP request
     * @param totalItems      Total number of items
     * @param defaultPageSize Default page size
     * @return Current page number
     */
    protected int handlePaginationRequest(HttpServletRequest request, int totalItems, int defaultPageSize) {
        return ServletUtil.handlePaginationRequest(request, totalItems, defaultPageSize);
    }
}