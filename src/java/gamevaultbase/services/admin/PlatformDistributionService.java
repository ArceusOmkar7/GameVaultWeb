package gamevaultbase.services.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Platform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for generating platform distribution data for the admin dashboard.
 */
public class PlatformDistributionService implements DashboardService {

    private final List<Game> games;

    public PlatformDistributionService(List<Game> games) {
        this.games = games;
    }

    @Override
    public Map<String, Object> generateData() {
        return calculatePlatformDistribution();
    }

    /**
     * Calculate platform distribution of games
     */
    private Map<String, Object> calculatePlatformDistribution() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> platformCounts = new HashMap<>();

        // Count games per platform
        for (Game game : games) {
            List<Platform> platforms = game.getPlatforms();
            if (platforms != null) {
                for (Platform platform : platforms) {
                    String platformName = platform.getName();
                    platformCounts.put(platformName, platformCounts.getOrDefault(platformName, 0) + 1);
                }
            }
        }

        // Prepare data for chart
        List<String> platformNames = new ArrayList<>(platformCounts.keySet());
        List<Integer> counts = new ArrayList<>(platformCounts.values());

        result.put("platforms", platformNames);
        result.put("counts", counts);

        return result;
    }
}