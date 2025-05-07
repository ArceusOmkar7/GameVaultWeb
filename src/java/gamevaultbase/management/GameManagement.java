package gamevaultbase.management;

import gamevaultbase.entities.Game;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.storage.GameStorage;

import java.util.List;
import java.util.stream.Collectors;

public class GameManagement {

    private final GameStorage gameStorage;

    public GameManagement(GameStorage gameStorage) {
        this.gameStorage = gameStorage;
    }

    public Game getGame(int gameId) throws GameNotFoundException {
        Game game = gameStorage.findById(gameId);
        if (game == null) {
            throw new GameNotFoundException("Game not found with ID: " + gameId);
        }
        return game;
    }

    public List<Game> getAllGames(String searchQuery, String filterPlatform, String sortBy) {
        return gameStorage.findAllWithFilters(searchQuery, filterPlatform, sortBy);
    }

    // Existing method to get featured games
    public List<Game> getFeaturedGames() {
        return gameStorage.findFeaturedGames(); // Delegate to storage
    }

    // New overloaded method that takes a limit parameter
    public List<Game> getFeaturedGames(int limit) {
        List<Game> allFeatured = getFeaturedGames();
        if (allFeatured != null && allFeatured.size() > limit) {
            return allFeatured.stream().limit(limit).collect(Collectors.toList());
        }
        return allFeatured;
    }

    // Get games that a user owns (from completed orders)
    public List<Game> getOwnedGames(int userId) {
        return gameStorage.findOwnedGamesByUser(userId);
    }

    // Check if a user already owns a specific game
    public boolean isGameOwnedByUser(int userId, int gameId) {
        List<Game> ownedGames = getOwnedGames(userId);
        return ownedGames.stream().anyMatch(game -> game.getGameId() == gameId);
    }

    public void addGame(Game game) {
        // TODO: Add validation before saving if needed
        gameStorage.save(game);
    }

    public void updateGame(Game game) {
        // TODO: Add validation before updating if needed
        gameStorage.update(game);
    }

    public void deleteGame(int gameId) {
        // TODO: Add checks (e.g., is game in active carts/orders?) if necessary
        gameStorage.delete(gameId);
    }
}