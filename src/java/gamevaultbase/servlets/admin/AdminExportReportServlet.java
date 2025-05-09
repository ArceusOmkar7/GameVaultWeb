package gamevaultbase.servlets.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Order;
import gamevaultbase.entities.User;
import gamevaultbase.services.admin.DashboardDataService;
import gamevaultbase.servlets.base.AdminBaseServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminExportReportServlet", urlPatterns = { "/admin/export-report" })
public class AdminExportReportServlet extends AdminBaseServlet {
    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Gather dashboard data
        List<Game> allGames = getGameManagement().getAllGames(null, null, null);
        List<User> allUsers = getUserManagement().getAllUsers();
        List<Order> allOrders = getOrderManagement().getAllOrders();
        DashboardDataService dashboardService = new DashboardDataService(allGames, allUsers, allOrders);

        Map<String, Object> stats = dashboardService.getBasicStats();
        Map<String, Object> growth = dashboardService.getGrowthTrendsData();
        Map<String, Object> sales = dashboardService.getSalesChartData();
        Map<String, Object> topGames = dashboardService.getTopGamesData();
        Map<String, Object> revenue = dashboardService.getRevenueBreakdownData();
        Map<String, Object> platforms = dashboardService.getPlatformDistributionData();

        // Set response headers
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=dashboard_report.csv");
        PrintWriter out = response.getWriter();

        // Write CSV content
        out.println("Dashboard Report Export");
        out.println(
                "Generated at: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        out.println();
        out.println("--- Basic Stats ---");
        out.println("Games,Users,Orders,Total Revenue");
        out.printf("%s,%s,%s,\"%s\"\n", stats.get("gamesCount"), stats.get("usersCount"), stats.get("ordersCount"),
                stats.get("totalRevenue"));
        out.println();
        out.println("--- Growth Trends ---");
        out.println("Games Growth (%),Users Growth (%),Orders Growth (%),Revenue Growth (%)");
        out.printf("%.2f,%.2f,%.2f,%.2f\n",
                parseDouble(growth.get("gamesGrowth")),
                parseDouble(growth.get("usersGrowth")),
                parseDouble(growth.get("ordersGrowth")),
                parseDouble(growth.get("revenueGrowth")));
        out.println();
        out.println("--- Top Selling Games ---");
        out.println("Title,Units Sold");
        @SuppressWarnings("unchecked")
        List<String> titles = (List<String>) topGames.get("titles");
        @SuppressWarnings("unchecked")
        List<Integer> salesList = (List<Integer>) topGames.get("sales");
        if (titles != null && salesList != null) {
            for (int i = 0; i < titles.size(); i++) {
                out.printf("\"%s\",%s\n", escapeCsv(titles.get(i)), salesList.get(i));
            }
        }
        out.println();
        out.println("--- Revenue Breakdown ---");
        out.println("Genre,Revenue");
        @SuppressWarnings("unchecked")
        List<String> genres = (List<String>) revenue.get("genres");
        @SuppressWarnings("unchecked")
        List<Double> revenues = (List<Double>) revenue.get("revenues");
        if (genres != null && revenues != null) {
            for (int i = 0; i < genres.size(); i++) {
                out.printf("\"%s\",%.2f\n", escapeCsv(genres.get(i)), revenues.get(i));
            }
        }
        out.println();
        out.println("--- Platform Distribution ---");
        out.println("Platform,Count");
        @SuppressWarnings("unchecked")
        List<String> platformNames = (List<String>) platforms.get("platforms");
        @SuppressWarnings("unchecked")
        List<Integer> counts = (List<Integer>) platforms.get("counts");
        if (platformNames != null && counts != null) {
            for (int i = 0; i < platformNames.size(); i++) {
                out.printf("\"%s\",%s\n", escapeCsv(platformNames.get(i)), counts.get(i));
            }
        }
        out.flush();
    }

    @Override
    protected void processAdminPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processAdminGetRequest(request, response);
    }

    // Helper to parse numbers safely
    private double parseDouble(Object obj) {
        if (obj == null)
            return 0.0;
        if (obj instanceof Number)
            return ((Number) obj).doubleValue();
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    // Helper to escape CSV text
    private String escapeCsv(String s) {
        if (s == null)
            return "";
        String result = s.replace("\"", "\"\"");
        return result;
    }
}