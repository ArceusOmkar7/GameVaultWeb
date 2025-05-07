package gamevaultbase.servlets.base;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Base servlet for pages that require a logged-in user.
 * Automatically checks for user authentication before processing requests.
 */
public abstract class UserBaseServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        if (!isLoggedIn(request)) {
            redirectWithMessage(request, response, "/login",
                    "Please log in to access this page", "warning");
            return;
        }

        // Setup common attributes for the page
        setupCommonAttributes(request);

        // Process the user GET request
        processUserGetRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        if (!isLoggedIn(request)) {
            redirectWithMessage(request, response, "/login",
                    "Please log in to access this page", "warning");
            return;
        }

        // Process the user POST request
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
    protected void processUserPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Default implementation forwards to GET after POST
        // Subclasses should override this method if they need to process POST requests
        redirectTo(request, response, request.getServletPath());
    }
}