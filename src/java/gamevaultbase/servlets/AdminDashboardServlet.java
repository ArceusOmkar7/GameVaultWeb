package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.User;
import gamevaultbase.management.GameManagement;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in and is an admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("loggedInUser");
        if (!user.isAdmin()) {
            // Redirect non-admin users to home
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Check for any message parameters from redirects
        String message = request.getParameter("message");
        String messageType = request.getParameter("messageType");

        if (message != null && !message.isEmpty()) {
            request.setAttribute("message", message);

            // Set the message type (default to "info" if not specified)
            if (messageType != null && (messageType.equals("success") || messageType.equals("error")
                    || messageType.equals("warning"))) {
                request.setAttribute("messageType", messageType);
            } else {
                request.setAttribute("messageType", "info");
            }
        }

        // Get game management from application context
        GameManagement gameManagement = (GameManagement) getServletContext().getAttribute("gameManagement");
        if (gameManagement == null) {
            request.setAttribute("errorMessage", "System error: Game management service not available.");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        // Get all games to display in the dashboard
        List<Game> games = gameManagement.getAllGames(null, null, null);
        request.setAttribute("games", games);

        // Forward to admin dashboard JSP
        request.getRequestDispatcher("/WEB-INF/jsp/adminDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Admin dashboard doesn't handle any POST requests directly
        // POST requests for image uploads are handled by GameImageUploadServlet
        doGet(request, response);
    }
}