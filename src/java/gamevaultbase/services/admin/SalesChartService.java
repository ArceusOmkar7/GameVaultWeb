package gamevaultbase.services.admin;

import gamevaultbase.entities.Order;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for generating sales data for the admin dashboard sales chart.
 */
public class SalesChartService implements DashboardService {

    private final List<Order> orders;

    public SalesChartService(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public Map<String, Object> generateData() {
        return calculateSalesData();
    }

    /**
     * Calculate sales data for the last 7 days
     */
    private Map<String, Object> calculateSalesData() {
        Map<String, Object> result = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Get last 7 days
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        List<String> dates = new ArrayList<>();
        List<Double> salesValues = new ArrayList<>();
        List<Integer> orderCounts = new ArrayList<>();

        // Start from 6 days ago (to get 7 days total including today)
        cal.add(Calendar.DAY_OF_MONTH, -6);

        for (int i = 0; i < 7; i++) {
            Date currentDate = cal.getTime();
            String dateStr = dateFormat.format(currentDate);
            dates.add(dateStr);

            // Calculate sales for this day
            double daySales = 0.0;
            int dayOrders = 0;

            for (Order order : orders) {
                Date orderDate = order.getOrderDate();
                if (orderDate != null && isSameDay(orderDate, currentDate)) {
                    daySales += order.getTotalAmount();
                    dayOrders++;
                }
            }

            salesValues.add(daySales);
            orderCounts.add(dayOrders);

            // Move to next day
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        result.put("dates", dates);
        result.put("sales", salesValues);
        result.put("orders", orderCounts);

        return result;
    }

    /**
     * Helper method to check if two dates are the same day
     */
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}