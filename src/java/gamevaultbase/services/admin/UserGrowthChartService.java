package gamevaultbase.services.admin;

import gamevaultbase.entities.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for generating user growth data for the admin dashboard.
 */
public class UserGrowthChartService implements DashboardService {

    private final List<User> users;

    public UserGrowthChartService(List<User> users) {
        this.users = users;
    }

    @Override
    public Map<String, Object> generateData() {
        return calculateUserGrowth();
    }

    /**
     * Calculate user growth over time
     */
    private Map<String, Object> calculateUserGrowth() {
        Map<String, Object> result = new HashMap<>();

        // Sort users by creation date
        List<User> sortedUsers = new ArrayList<>(users);
        Collections.sort(sortedUsers, Comparator.comparing(User::getCreatedAt));

        // Group users by month
        Map<String, Integer> usersByMonth = new HashMap<>();
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

        for (User user : sortedUsers) {
            Date creationDate = user.getCreatedAt();
            if (creationDate != null) {
                String month = monthFormat.format(creationDate);
                usersByMonth.put(month, usersByMonth.getOrDefault(month, 0) + 1);
            }
        }

        // Prepare data for chart (last 6 months)
        List<String> months = new ArrayList<>();
        List<Integer> userCounts = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -5); // Start from 5 months ago

        for (int i = 0; i < 6; i++) {
            String month = monthFormat.format(cal.getTime());
            months.add(month);
            userCounts.add(usersByMonth.getOrDefault(month, 0));
            cal.add(Calendar.MONTH, 1);
        }

        result.put("months", months);
        result.put("users", userCounts);

        return result;
    }
}