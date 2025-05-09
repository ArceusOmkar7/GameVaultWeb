package gamevaultbase.services.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Order;
import gamevaultbase.entities.User;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for calculating growth trend data for the admin dashboard.
 */
public class GrowthTrendService implements DashboardService {

    private final List<Game> games;
    private final List<User> users;
    private final List<Order> orders;

    public GrowthTrendService(List<Game> games, List<User> users, List<Order> orders) {
        this.games = games;
        this.users = users;
        this.orders = orders;
    }

    @Override
    public Map<String, Object> generateData() {
        return calculateGrowthTrends();
    }

    /**
     * Calculate growth trend percentages for display in stats cards
     */
    private Map<String, Object> calculateGrowthTrends() {
        Map<String, Object> trends = new HashMap<>();

        // Calculate game growth (compare last month to previous month)
        trends.put("gamesGrowth", calculateGrowthPercentage(games, Game::getAddedAt));

        // Calculate user growth
        trends.put("usersGrowth", calculateGrowthPercentage(users, User::getCreatedAt));

        // Calculate order growth
        trends.put("ordersGrowth", calculateGrowthPercentage(orders, Order::getOrderDate));

        // Calculate revenue growth (comparing this month to previous month)
        trends.put("revenueGrowth", calculateRevenueGrowth());

        return trends;
    }

    /**
     * Calculate growth percentage between current and previous period
     */
    private <T> double calculateGrowthPercentage(List<T> items, Function<T, Date> dateExtractor) {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();

        // Current month start
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date currentMonthStart = cal.getTime();

        // Previous month start/end
        cal.add(Calendar.MONTH, -1);
        Date prevMonthStart = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date prevMonthEnd = cal.getTime();

        // Count items in each period
        int currentPeriodCount = 0;
        int prevPeriodCount = 0;

        for (T item : items) {
            Date itemDate = dateExtractor.apply(item);
            if (itemDate != null) {
                if (itemDate.after(currentMonthStart) && itemDate.before(now)) {
                    currentPeriodCount++;
                } else if (itemDate.after(prevMonthStart) && itemDate.before(prevMonthEnd)) {
                    prevPeriodCount++;
                }
            }
        }

        // Calculate growth percentage
        if (prevPeriodCount > 0) {
            return ((double) currentPeriodCount - prevPeriodCount) / prevPeriodCount * 100;
        } else if (currentPeriodCount > 0) {
            return 100.0; // Infinite growth (from 0 to something)
        } else {
            return 0.0; // No growth (0 to 0)
        }
    }

    /**
     * Calculate revenue growth percentage
     */
    private double calculateRevenueGrowth() {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();

        // Current month start
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date currentMonthStart = cal.getTime();

        // Previous month start/end
        cal.add(Calendar.MONTH, -1);
        Date prevMonthStart = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date prevMonthEnd = cal.getTime();

        // Calculate revenue for each period
        double currentPeriodRevenue = 0.0;
        double prevPeriodRevenue = 0.0;

        for (Order order : orders) {
            Date orderDate = order.getOrderDate();
            if (orderDate != null) {
                if (orderDate.after(currentMonthStart) && orderDate.before(now)) {
                    currentPeriodRevenue += order.getTotalAmount();
                } else if (orderDate.after(prevMonthStart) && orderDate.before(prevMonthEnd)) {
                    prevPeriodRevenue += order.getTotalAmount();
                }
            }
        }

        // Calculate growth percentage
        if (prevPeriodRevenue > 0) {
            return ((currentPeriodRevenue - prevPeriodRevenue) / prevPeriodRevenue) * 100;
        } else if (currentPeriodRevenue > 0) {
            return 100.0; // Infinite growth (from 0 to something)
        } else {
            return 0.0; // No growth (0 to 0)
        }
    }
}