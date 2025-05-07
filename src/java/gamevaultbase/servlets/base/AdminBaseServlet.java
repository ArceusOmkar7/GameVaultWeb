package gamevaultbase.servlets.base;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Base servlet for all admin-only pages.
 * Automatically checks for admin authorization before processing requests.
 */
public abstract class AdminBaseServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is authorized as admin
        if (!isAdmin(request)) {
            redirectWithMessage(request, response, "/login",
                    "Admin access required", "error");
            return;
        }

        // Setup common attributes for the page
        setupCommonAttributes(request);

        // Process the admin GET request
        processAdminGetRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is authorized as admin
        if (!isAdmin(request)) {
            redirectWithMessage(request, response, "/login",
                    "Admin access required", "error");
            return;
        }

        // Process the admin POST request
        processAdminPostRequest(request, response);
    }

    /**
     * Process GET request for admin pages.
     * This method should be implemented by subclasses.
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    protected abstract void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /**
     * Process POST request for admin pages.
     * This method should be implemented by subclasses.
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    protected void processAdminPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Default implementation forwards to GET after POST
        // Subclasses should override this method if they need to process POST requests
        redirectTo(request, response, request.getServletPath());
    }
}