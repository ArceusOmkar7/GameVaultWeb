package gamevaultbase.servlets.admin;

import gamevaultbase.servlets.base.AdminBaseServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling the admin dashboard page.
 * This is the main entry point for the admin panel.
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = { "/admin/dashboard" })
public class AdminDashboardServlet extends AdminBaseServlet {

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Add any data needed for the dashboard
        int gameCount = getGameManagement().getAllGames(null, null, null).size();
        int userCount = getUserManagement().getAllUsers().size();

        // Set attributes for the JSP
        request.setAttribute("gameCount", gameCount);
        request.setAttribute("userCount", userCount);

        // Forward to the admin dashboard JSP
        forwardToJsp(request, response, "/WEB-INF/jsp/adminDashboard.jsp");
    }

    @Override
    protected void processAdminPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Admin dashboard typically doesn't handle POST requests
        // Redirect to GET
        redirectTo(request, response, "/admin/dashboard");
    }
}