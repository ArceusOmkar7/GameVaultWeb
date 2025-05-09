package gamevaultbase.services.admin;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Genre;
import java.util.*;
import java.util.stream.Collectors;

public class GameStatsService implements DashboardService {
    private final List<Game> games;

    public GameStatsService(List<Game> games) {
        this.games = games;
    }

    @Override
    public Map<String, Object> generateData() {
        Map<String, Object> result = new HashMap<>();
        result.put("ratingDistribution", calculateRatingDistribution());
        result.put("priceRanges", calculatePriceRanges());
        result.put("genrePopularity", calculateGenrePopularity());
        return result;
    }

    private Map<String, Object> calculateRatingDistribution() {
        Map<String, Object> distribution = new HashMap<>();
        Map<Integer, Long> ratingCounts = games.stream()
                .collect(Collectors.groupingBy(
                        game -> (int) Math.round(game.getRating()),
                        Collectors.counting()));

        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        // Initialize all possible ratings (1-5)
        for (int i = 1; i <= 5; i++) {
            labels.add(i + " Stars");
            data.add(ratingCounts.getOrDefault(i, 0L));
        }

        distribution.put("labels", labels);
        distribution.put("data", data);
        return distribution;
    }

    private Map<String, Object> calculatePriceRanges() {
        Map<String, Object> ranges = new HashMap<>();
        List<String> labels = Arrays.asList("$0-10", "$11-20", "$21-30", "$31-40", "$40+");
        List<Long> data = new ArrayList<>();

        // Count games in each price range
        for (String range : labels) {
            long count = games.stream()
                    .filter(game -> {
                        double price = game.getPrice();
                        if (range.equals("$0-10"))
                            return price <= 10;
                        if (range.equals("$11-20"))
                            return price > 10 && price <= 20;
                        if (range.equals("$21-30"))
                            return price > 20 && price <= 30;
                        if (range.equals("$31-40"))
                            return price > 30 && price <= 40;
                        return price > 40;
                    })
                    .count();
            data.add(count);
        }

        ranges.put("labels", labels);
        ranges.put("data", data);
        return ranges;
    }

    private Map<String, Object> calculateGenrePopularity() {
        Map<String, Object> popularity = new HashMap<>();
        Map<String, Long> genreCounts = new HashMap<>();

        // Count games per genre
        for (Game game : games) {
            List<Genre> genres = game.getGenres();
            if (genres != null) {
                for (Genre genre : genres) {
                    genreCounts.merge(genre.getName(), 1L, Long::sum);
                }
            }
        }

        // Sort genres by count
        List<Map.Entry<String, Long>> sortedGenres = new ArrayList<>(genreCounts.entrySet());
        sortedGenres.sort(Map.Entry.<String, Long>comparingByValue().reversed());

        // Take top 10 genres
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        for (int i = 0; i < Math.min(10, sortedGenres.size()); i++) {
            labels.add(sortedGenres.get(i).getKey());
            data.add(sortedGenres.get(i).getValue());
        }

        popularity.put("labels", labels);
        popularity.put("data", data);
        return popularity;
    }
}