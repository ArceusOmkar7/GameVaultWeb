package gamevaultbase.storage;

import gamevaultbase.entities.Game;
import gamevaultbase.interfaces.StorageInterface;
import gamevaultbase.helpers.DBUtil;

import java.io.IOException;
import java.sql.Date; // Use java.sql.Date for PreparedStatement
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameStorage implements StorageInterface<Game, Integer> {

    // ... (findById, findAll, findOwnedGamesByUser remain the same) ...
    @Override
    public Game findById(Integer gameId) {
        String sql = "SELECT * FROM Games WHERE gameId = ?";
        try {
            List<Game> games = DBUtil.executeQuery(sql, rs -> mapResultSetToGame(rs), gameId);
            return games.isEmpty() ? null : games.get(0);
        } catch (SQLException | IOException e) {
            System.err.println("Error finding game by ID: " + gameId + " - " + e.getMessage());
            // Consider logging the stack trace e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Game> findAll() {
        String sql = "SELECT * FROM Games";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToGame(rs));
        } catch (SQLException | IOException e) {
            System.err.println("Error finding all games: " + e.getMessage());
            // Consider logging the stack trace e.printStackTrace();
            return new ArrayList<>(); // Return empty list on error
        }
    }

    // Find all games owned by a specific user from completed orders
    public List<Game> findOwnedGamesByUser(Integer userId) {
        String sql = "SELECT DISTINCT g.* FROM Games g " +
                "JOIN OrderItems oi ON g.gameId = oi.gameId " +
                "JOIN Orders o ON oi.orderId = o.orderId " +
                "WHERE o.userId = ?";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToGame(rs), userId);
        } catch (SQLException | IOException e) {
            System.err.println("Error finding owned games for user: " + userId + " - " + e.getMessage());
            // Consider logging the stack trace e.printStackTrace();
            return new ArrayList<>(); // Return empty list on error
        }
    }

    // Method to get a few featured games (e.g., first 3 added)
    // TODO: Implement a proper "featured" flag or logic if needed
    public List<Game> findFeaturedGames() {
        String sql = "SELECT * FROM Games ORDER BY gameId ASC LIMIT 3"; // Simple example: Get first 3 games
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToGame(rs));
        } catch (SQLException | IOException e) {
            System.err.println("Error finding featured games: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
    }

    @Override
    public void save(Game game) {
        String sql = "INSERT INTO Games (title, description, developer, platform, price, releaseDate, imagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
        // Ensure releaseDate is not null before attempting to get time
        Date sqlReleaseDate = (game.getReleaseDate() != null) ? new Date(game.getReleaseDate().getTime()) : null;
        try {
            int generatedId = DBUtil.executeInsertAndGetKey(sql,
                    game.getTitle(),
                    game.getDescription(),
                    game.getDeveloper(),
                    game.getPlatform(),
                    game.getPrice(),
                    sqlReleaseDate, // Use java.sql.Date
                    game.getImagePath());

            if (generatedId != -1) {
                game.setGameId(generatedId);
            } else {
                System.err.println("WARN: Game insert did not return a generated ID for title: " + game.getTitle());
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error saving game: " + game.getTitle() + " - " + e.getMessage());
            // Consider rethrowing a custom exception
            // throw new DataAccessException("Failed to save game", e);
        }
    }

    @Override
    public void update(Game game) {
        String sql = "UPDATE Games SET title = ?, description = ?, developer = ?, platform = ?, price = ?, releaseDate = ?, imagePath = ? WHERE gameId = ?";
        Date sqlReleaseDate = (game.getReleaseDate() != null) ? new Date(game.getReleaseDate().getTime()) : null;
        try {
            int rowsAffected = DBUtil.executeUpdate(sql,
                    game.getTitle(),
                    game.getDescription(),
                    game.getDeveloper(),
                    game.getPlatform(),
                    game.getPrice(),
                    sqlReleaseDate, // Use java.sql.Date
                    game.getImagePath(),
                    game.getGameId());
            if (rowsAffected == 0) {
                System.err.println("WARN: Update affected 0 rows for gameId: " + game.getGameId());
                // This could mean the gameId didn't exist
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error updating game: " + game.getGameId() + " - " + e.getMessage());
            // Consider rethrowing a custom exception
        }
    }

    @Override
    public void delete(Integer gameId) {
        // Consider dependencies: Check if game is in OrderItems before deleting?
        // The DB foreign key constraint (ON DELETE RESTRICT) should prevent this if set
        // up correctly.
        String sql = "DELETE FROM Games WHERE gameId = ?";
        try {
            int rowsAffected = DBUtil.executeUpdate(sql, gameId);
            if (rowsAffected == 0) {
                System.err.println("WARN: Delete affected 0 rows for gameId: " + gameId);
            }
        } catch (SQLException | IOException e) {
            // Catch potential foreign key constraint violation errors specifically if
            // needed
            System.err.println("Error deleting game: " + gameId + " - " + e.getMessage());
            // Consider rethrowing a custom exception
        }
    }

    // Public map method if needed by other classes (like CartStorage)
    public Game mapResultSetToGame(ResultSet rs) throws SQLException {
        return new Game(
                rs.getInt("gameId"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("developer"),
                rs.getString("platform"),
                rs.getFloat("price"),
                rs.getDate("releaseDate"), // Retrieve as java.sql.Date, compatible with java.util.Date
                rs.getString("imagePath") // Added image path
        );
    }
}