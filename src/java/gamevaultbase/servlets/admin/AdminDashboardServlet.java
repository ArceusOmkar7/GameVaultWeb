package gamevaultbase.servlets.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Order;
import gamevaultbase.entities.User;
import gamevaultbase.services.admin.DashboardDataService;
import gamevaultbase.servlets.base.AdminBaseServlet;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
        // Fetch all necessary data from the database
        List<Game> allGames = getGameManagement().getAllGames(null, null, null);
        List<User> allUsers = getUserManagement().getAllUsers();
        List<Order> allOrders = getOrderManagement().getAllOrders();

        // Initialize the dashboard data service
        DashboardDataService dashboardService = new DashboardDataService(allGames, allUsers, allOrders);

        // Get basic statistics
        Map<String, Object> basicStats = dashboardService.getBasicStats();

        // Set attributes for the JSP
        request.setAttribute("gamesCount", basicStats.get("gamesCount"));
        request.setAttribute("usersCount", basicStats.get("usersCount"));
        request.setAttribute("ordersCount", basicStats.get("ordersCount"));
        request.setAttribute("totalRevenue", basicStats.get("totalRevenue"));

        // Get and set chart data
        request.setAttribute("salesData", dashboardService.getSalesChartData());
        request.setAttribute("topGamesData", dashboardService.getTopGamesData());
        request.setAttribute("userGrowthData", dashboardService.getUserGrowthData());
        request.setAttribute("revenueBreakdownData", dashboardService.getRevenueBreakdownData());
        request.setAttribute("platformDistributionData", dashboardService.getPlatformDistributionData());

        // Get and set growth trends data
        request.setAttribute("growthTrends", dashboardService.getGrowthTrendsData());
        request.setAttribute("gameStatsData", dashboardService.getGameStatsData());

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