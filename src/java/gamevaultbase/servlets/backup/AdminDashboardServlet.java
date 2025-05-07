package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.servlets.base.AdminBaseServlet;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = { "/admin/dashboard" })
public class AdminDashboardServlet extends AdminBaseServlet {

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get all games to display in the dashboard
        List<Game> games = getGameManagement().getAllGames(null, null, null);
        request.setAttribute("games", games);

        // Forward to admin dashboard JSP
        forwardToJsp(request, response, "/WEB-INF/jsp/adminDashboard.jsp");
    }
}