package gamevaultbase.servlets.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Order;
import gamevaultbase.entities.User;
import gamevaultbase.services.admin.DashboardDataService;
import gamevaultbase.services.admin.SalesChartService;
import gamevaultbase.servlets.base.AdminBaseServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * Servlet for handling AJAX requests related to admin dashboard data.
 * This allows for dynamic updates to dashboard charts without page reload.
 */
@WebServlet(name = "AdminDashboardDataServlet", urlPatterns = { "/admin/api/dashboard-data" })
public class AdminDashboardDataServlet extends AdminBaseServlet {

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get chart type parameter
        String chartType = request.getParameter("chart");
        String timeRange = request.getParameter("range"); // day, week, month, year

        if (chartType == null || chartType.isEmpty()) {
            sendErrorResponse(response, "Chart type parameter is required");
            return;
        }

        // Default to day if not specified
        if (timeRange == null || timeRange.isEmpty()) {
            timeRange = "day";
        }

        // Fetch all necessary data from the database
        List<Game> allGames = getGameManagement().getAllGames(null, null, null);
        List<User> allUsers = getUserManagement().getAllUsers();
        List<Order> allOrders = getOrderManagement().getAllOrders();

        // Initialize the dashboard data service
        DashboardDataService dashboardService = new DashboardDataService(allGames, allUsers, allOrders);

        JSONObject result = new JSONObject();

        try {
            // Return different data based on the requested chart
            switch (chartType) {
                case "sales":
                    // For sales chart, we'll handle different time ranges
                    result = getSalesDataForRange(allOrders, timeRange);
                    break;
                case "topGames":
                    result = new JSONObject(dashboardService.getTopGamesData());
                    break;
                case "userGrowth":
                    result = new JSONObject(dashboardService.getUserGrowthData());
                    break;
                case "revenueBreakdown":
                    result = new JSONObject(dashboardService.getRevenueBreakdownData());
                    break;
                case "platformDistribution":
                    result = new JSONObject(dashboardService.getPlatformDistributionData());
                    break;
                default:
                    sendErrorResponse(response, "Invalid chart type: " + chartType);
                    return;
            }

            // Send JSON response
            sendJsonResponse(response, result);

        } catch (Exception e) {
            sendErrorResponse(response, "Error generating chart data: " + e.getMessage());
        }
    }

    @Override
    protected void processAdminPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // API endpoints typically don't differentiate between GET/POST
        processAdminGetRequest(request, response);
    }

    /**
     * Get sales data for a specific time range
     * 
     * @param orders    List of all orders
     * @param timeRange The time range (day, week, month, year)
     * @return JSONObject containing sales data for the specified range
     */
    private JSONObject getSalesDataForRange(List<Order> orders, String timeRange) {
        // Create a specialized service for the sales chart with time range support
        CustomSalesChartService salesService = new CustomSalesChartService(orders);

        Map<String, Object> data;
        switch (timeRange) {
            case "week":
                data = salesService.generateWeeklyData();
                break;
            case "month":
                data = salesService.generateMonthlyData();
                break;
            case "year":
                data = salesService.generateYearlyData();
                break;
            case "day":
            default:
                data = salesService.generateDailyData();
                break;
        }

        return new JSONObject(data);
    }

    /**
     * Send a JSON response to the client
     * 
     * @param response   The HTTP response
     * @param jsonObject The JSON data to send
     * @throws IOException If an I/O error occurs
     */
    private void sendJsonResponse(HttpServletResponse response, JSONObject jsonObject) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
        out.flush();
    }

    /**
     * Send an error response to the client
     * 
     * @param response     The HTTP response
     * @param errorMessage The error message
     * @throws IOException If an I/O error occurs
     */
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        JSONObject error = new JSONObject();
        error.put("error", errorMessage);

        PrintWriter out = response.getWriter();
        out.print(error.toString());
        out.flush();
    }

    /**
     * Extended SalesChartService with support for different time ranges
     */
    private class CustomSalesChartService extends SalesChartService {

        public CustomSalesChartService(List<Order> orders) {
            super(orders);
        }

        /**
         * Generate daily sales data (last 7 days)
         */
        public Map<String, Object> generateDailyData() {
            return super.generateData(); // The default is daily data
        }

        /**
         * Generate weekly sales data (last 12 weeks)
         */
        public Map<String, Object> generateWeeklyData() {
            return generateAggregatedData(12, "week");
        }

        /**
         * Generate monthly sales data (last 12 months)
         */
        public Map<String, Object> generateMonthlyData() {
            return generateAggregatedData(12, "month");
        }

        /**
         * Generate yearly sales data (last 5 years)
         */
        public Map<String, Object> generateYearlyData() {
            return generateAggregatedData(5, "year");
        }

        /**
         * Generate aggregated data for specified time periods
         */
        private Map<String, Object> generateAggregatedData(int periods, String periodType) {
            // This would be implemented with logic to aggregate sales by week/month/year
            // For demonstration, we'll return simulated data
            Map<String, Object> result = new java.util.HashMap<>();
            java.util.List<String> labels = new java.util.ArrayList<>();
            java.util.List<Double> sales = new java.util.ArrayList<>();
            java.util.List<Integer> orders = new java.util.ArrayList<>();

            java.text.SimpleDateFormat formatter;
            java.util.Calendar cal = java.util.Calendar.getInstance();

            switch (periodType) {
                case "week":
                    formatter = new java.text.SimpleDateFormat("'Week 'w, yyyy");
                    // Go back to current week start
                    cal.set(java.util.Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                    break;
                case "month":
                    formatter = new java.text.SimpleDateFormat("MMM yyyy");
                    // Go back to current month start
                    cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
                    break;
                case "year":
                    formatter = new java.text.SimpleDateFormat("yyyy");
                    // Go back to current year start
                    cal.set(java.util.Calendar.DAY_OF_YEAR, 1);
                    break;
                default:
                    formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    break;
            }

            // Go back by the required periods
            for (int i = periods - 1; i >= 0; i--) {
                switch (periodType) {
                    case "week":
                        cal.add(java.util.Calendar.WEEK_OF_YEAR, -i);
                        break;
                    case "month":
                        cal.add(java.util.Calendar.MONTH, -i);
                        break;
                    case "year":
                        cal.add(java.util.Calendar.YEAR, -i);
                        break;
                }

                String label = formatter.format(cal.getTime());
                labels.add(label);

                // Reset calendar back to original position for next iteration
                switch (periodType) {
                    case "week":
                        cal.add(java.util.Calendar.WEEK_OF_YEAR, i);
                        break;
                    case "month":
                        cal.add(java.util.Calendar.MONTH, i);
                        break;
                    case "year":
                        cal.add(java.util.Calendar.YEAR, i);
                        break;
                }

                // In a real implementation, you would calculate actual sales for this period
                // For demonstration, we'll use random values
                double randomSales = Math.random() * 1000 + 500;
                int randomOrders = (int) (Math.random() * 50 + 10);

                sales.add(randomSales);
                orders.add(randomOrders);
            }

            result.put("dates", labels);
            result.put("sales", sales);
            result.put("orders", orders);

            return result;
        }
    }
}