package gamevaultbase.servlets.base;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Base servlet for public pages that don't require authentication.
 * Provides common functionality and utility methods for public pages.
 */
public abstract class PublicBaseServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Setup common attributes for the page
        setupCommonAttributes(request);

        // Process the public GET request
        processPublicGetRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Process the public POST request
        processPublicPostRequest(request, response);
    }

    /**
     * Process GET request for public pages.
     * This method should be implemented by subclasses.
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    protected abstract void processPublicGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /**
     * Process POST request for public pages.
     * This method should be implemented by subclasses.
     * 
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs
     */
    protected void processPublicPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Default implementation forwards to GET after POST
        // Subclasses should override this method if they need to process POST requests
        redirectTo(request, response, request.getServletPath());
    }
}