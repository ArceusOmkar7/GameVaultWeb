package gamevaultbase.services.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for generating revenue breakdown data by game genre for the admin
 * dashboard.
 */
public class RevenueBreakdownService implements DashboardService {

    private final List<Order> orders;
    private final List<Game> games;

    public RevenueBreakdownService(List<Order> orders, List<Game> games) {
        this.orders = orders;
        this.games = games;
    }

    @Override
    public Map<String, Object> generateData() {
        return calculateRevenueBreakdown();
    }

    /**
     * Calculate revenue breakdown by game genre/category
     */
    private Map<String, Object> calculateRevenueBreakdown() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Double> revenueByGenre = new HashMap<>();

        // Map game IDs to their genres
        Map<Integer, String> gameGenres = new HashMap<>();
        for (Game game : games) {
            if (game.getGenres() != null && !game.getGenres().isEmpty()) {
                gameGenres.put(game.getGameId(), game.getGenres().get(0).getName());
            } else {
                gameGenres.put(game.getGameId(), "Other");
            }
        }

        // Calculate revenue per genre
        for (Order order : orders) {
            Map<Game, Float> orderItems = order.getOrderItems();
            for (Map.Entry<Game, Float> entry : orderItems.entrySet()) {
                Game game = entry.getKey();
                Float price = entry.getValue();
                String genre = gameGenres.getOrDefault(game.getGameId(), "Other");

                revenueByGenre.put(genre, revenueByGenre.getOrDefault(genre, 0.0) + price);
            }
        }

        // Prepare data for chart
        List<String> genres = new ArrayList<>(revenueByGenre.keySet());
        List<Double> revenues = new ArrayList<>(revenueByGenre.values());

        result.put("genres", genres);
        result.put("revenues", revenues);

        return result;
    }
}