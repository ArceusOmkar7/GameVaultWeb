package gamevaultbase.servlets.base;

import gamevaultbase.entities.User;
import gamevaultbase.management.UserManagement;
import gamevaultbase.management.CartManagement;
import gamevaultbase.management.OrderManagement;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Base servlet for pages that require a logged-in user.
 * Automatically checks for user authentication before processing requests.
 */
public abstract class UserBaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        User currentUser = getLoggedInUser(request);
        if (currentUser == null) {
            redirectWithMessage(request, response, "/login", "Please login to access this page.", "error");
            return;
        }

        // Process the request
        processUserGetRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        User currentUser = getLoggedInUser(request);
        if (currentUser == null) {
            redirectWithMessage(request, response, "/login", "Please login to access this page.", "error");
            return;
        }

        // Process the request
        processUserPostRequest(request, response);
    }

    /**
     * Process GET request for user pages.
     * This method should be implemented by subclasses.
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    protected abstract void processUserGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /**
     * Process POST request for user pages.
     * This method should be implemented by subclasses.
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    protected abstract void processUserPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    protected User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("loggedInUser");
        }
        return null;
    }

    protected UserManagement getUserManagement() {
        return (UserManagement) getServletContext().getAttribute("userManagement");
    }

    protected CartManagement getCartManagement() {
        return (CartManagement) getServletContext().getAttribute("cartManagement");
    }

    protected OrderManagement getOrderManagement() {
        return (OrderManagement) getServletContext().getAttribute("orderManagement");
    }

    protected void forwardToJsp(HttpServletRequest request, HttpServletResponse response, String jspPath)
            throws ServletException, IOException {
        request.getRequestDispatcher(jspPath).forward(request, response);
    }

    protected void redirectTo(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    protected void redirectWithMessage(HttpServletRequest request, HttpServletResponse response, String path,
            String message, String messageType) throws IOException {
        String encodedMessage = java.net.URLEncoder.encode(message, "UTF-8");
        String redirectUrl;
        if (path.startsWith("http")) {
            // Absolute URL (referer)
            if (path.contains("?")) {
                redirectUrl = path + "&message=" + encodedMessage + "&messageType=" + messageType;
            } else {
                redirectUrl = path + "?message=" + encodedMessage + "&messageType=" + messageType;
            }
        } else {
            // Relative path
            redirectUrl = request.getContextPath() + path +
                    "?message=" + encodedMessage + "&messageType=" + messageType;
        }
        response.sendRedirect(redirectUrl);
    }
}