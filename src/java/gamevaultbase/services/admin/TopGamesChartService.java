package gamevaultbase.services.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Order;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Service for generating top selling games data for the admin dashboard.
 */
public class TopGamesChartService implements DashboardService {

    private final List<Order> orders;
    private final List<Game> games;

    public TopGamesChartService(List<Order> orders, List<Game> games) {
        this.orders = orders;
        this.games = games;
    }

    @Override
    public Map<String, Object> generateData() {
        return calculateTopSellingGames();
    }

    /**
     * Calculate top selling games
     */
    private Map<String, Object> calculateTopSellingGames() {
        Map<String, Object> result = new HashMap<>();
        Map<Integer, Integer> gameSales = new HashMap<>();

        // Count sales per game
        for (Order order : orders) {
            Map<Game, Float> orderItems = order.getOrderItems();
            for (Game game : orderItems.keySet()) {
                int gameId = game.getGameId();
                gameSales.put(gameId, gameSales.getOrDefault(gameId, 0) + 1);
            }
        }

        // Create sorted list of game titles and sales
        List<Map.Entry<Integer, Integer>> sortedSales = new ArrayList<>(gameSales.entrySet());
        Collections.sort(sortedSales, Collections.reverseOrder(Map.Entry.comparingByValue()));

        List<String> gameTitles = new ArrayList<>();
        List<Integer> salesCounts = new ArrayList<>();

        // Take top 5 games
        int count = 0;
        for (Entry<Integer, Integer> entry : sortedSales) {
            if (count >= 5)
                break; // Limit to top 5

            int gameId = entry.getKey();
            // Find the game with this ID
            for (Game game : games) {
                if (game.getGameId() == gameId) {
                    gameTitles.add(game.getTitle());
                    salesCounts.add(entry.getValue());
                    count++;
                    break;
                }
            }
        }

        result.put("titles", gameTitles);
        result.put("sales", salesCounts);

        return result;
    }
}