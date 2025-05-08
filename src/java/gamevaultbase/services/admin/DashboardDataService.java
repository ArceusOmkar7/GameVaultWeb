package gamevaultbase.services.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Order;
import gamevaultbase.entities.User;
import java.util.List;
import java.util.Map;

/**
 * Facade service that coordinates all dashboard data services.
 * This class simplifies the interaction with the various dashboard data
 * services.
 */
public class DashboardDataService {

    private final List<Game> games;
    private final List<User> users;
    private final List<Order> orders;

    // Service instances
    private final SalesChartService salesChartService;
    private final TopGamesChartService topGamesChartService;
    private final UserGrowthChartService userGrowthChartService;
    private final RevenueBreakdownService revenueBreakdownService;
    private final PlatformDistributionService platformDistributionService;
    private final GrowthTrendService growthTrendService;

    /**
     * Constructor initializing all required services with necessary data.
     * 
     * @param games  List of all games
     * @param users  List of all users
     * @param orders List of all orders
     */
    public DashboardDataService(List<Game> games, List<User> users, List<Order> orders) {
        this.games = games;
        this.users = users;
        this.orders = orders;

        // Initialize services
        this.salesChartService = new SalesChartService(orders);
        this.topGamesChartService = new TopGamesChartService(orders, games);
        this.userGrowthChartService = new UserGrowthChartService(users);
        this.revenueBreakdownService = new RevenueBreakdownService(orders, games);
        this.platformDistributionService = new PlatformDistributionService(games);
        this.growthTrendService = new GrowthTrendService(games, users, orders);
    }

    /**
     * Get basic statistics for the dashboard.
     * 
     * @return Map containing basic statistics (counts, revenue)
     */
    public Map<String, Object> getBasicStats() {
        Map<String, Object> stats = new java.util.HashMap<>();

        // Basic stats
        stats.put("gamesCount", games.size());
        stats.put("usersCount", users.size());
        stats.put("ordersCount", orders.size());

        // Calculate total revenue
        double totalRevenue = calculateTotalRevenue();
        stats.put("totalRevenue", String.format("$%,.2f", totalRevenue));

        return stats;
    }

    /**
     * Get sales chart data.
     * 
     * @return Map containing sales chart data
     */
    public Map<String, Object> getSalesChartData() {
        return salesChartService.generateData();
    }

    /**
     * Get top selling games data.
     * 
     * @return Map containing top games data
     */
    public Map<String, Object> getTopGamesData() {
        return topGamesChartService.generateData();
    }

    /**
     * Get user growth data.
     * 
     * @return Map containing user growth data
     */
    public Map<String, Object> getUserGrowthData() {
        return userGrowthChartService.generateData();
    }

    /**
     * Get revenue breakdown data.
     * 
     * @return Map containing revenue breakdown data
     */
    public Map<String, Object> getRevenueBreakdownData() {
        return revenueBreakdownService.generateData();
    }

    /**
     * Get platform distribution data.
     * 
     * @return Map containing platform distribution data
     */
    public Map<String, Object> getPlatformDistributionData() {
        return platformDistributionService.generateData();
    }

    /**
     * Get growth trends data.
     * 
     * @return Map containing growth trends data
     */
    public Map<String, Object> getGrowthTrendsData() {
        return growthTrendService.generateData();
    }

    /**
     * Calculate total revenue from all orders
     */
    private double calculateTotalRevenue() {
        double total = 0.0;
        for (Order order : orders) {
            total += order.getTotalAmount();
        }
        return total;
    }
}