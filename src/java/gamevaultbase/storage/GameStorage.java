package gamevaultbase.storage;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Genre;
import gamevaultbase.entities.Platform;
import gamevaultbase.interfaces.StorageInterface;
import gamevaultbase.helpers.DBUtil;

import java.sql.Date; // Use java.sql.Date for PreparedStatement
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class GameStorage implements StorageInterface<Game, Integer> {

    @Override
    public Game findById(Integer gameId) {
        try {
            // Use the enhanced method that loads the relationships
            return DBUtil.getGameWithRelations(gameId);
        } catch (SQLException e) {
            System.err.println("Error finding game by ID: " + gameId + " - " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Game> findAll() {
        return findAllWithFilters(null, null, null, null);
    }

    public List<Game> findAllWithFilters(String searchQuery, String filterPlatform, String filterGenre, String sortBy) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        // Base query with joins for platforms and genres
        sql.append("SELECT DISTINCT g.* FROM Games g ");

        // Add joins only if we need them for filtering
        boolean needPlatformJoin = filterPlatform != null && !filterPlatform.trim().isEmpty();
        boolean needGenreJoin = filterGenre != null && !filterGenre.trim().isEmpty();

        if (needPlatformJoin) {
            sql.append("LEFT JOIN GamePlatforms gp ON g.gameId = gp.gameId ");
            sql.append("LEFT JOIN Platforms p ON gp.platformId = p.platformId ");
        }

        if (needGenreJoin) {
            sql.append("LEFT JOIN GameGenres gg ON g.gameId = gg.gameId ");
            sql.append("LEFT JOIN Genres gnr ON gg.genreId = gnr.genreId ");
        }

        sql.append("WHERE 1=1 ");

        // Search by title or description
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql.append("AND (g.title LIKE ? OR g.description LIKE ?) ");
            params.add("%" + searchQuery.trim() + "%");
            params.add("%" + searchQuery.trim() + "%");
        }

        // Filter by platform - check both the normalized table and legacy field
        if (needPlatformJoin) {
            sql.append("AND (p.name = ? OR g.platform LIKE ?) ");
            params.add(filterPlatform.trim());
            params.add("%" + filterPlatform.trim() + "%");
        }

        // Filter by genre - check both the normalized table and legacy field
        if (needGenreJoin) {
            sql.append("AND (gnr.name = ? OR g.genre LIKE ?) ");
            params.add(filterGenre.trim());
            params.add("%" + filterGenre.trim() + "%");
        }

        // Sort results
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            switch (sortBy) {
                case "title_asc":
                    sql.append("ORDER BY g.title ASC");
                    break;
                case "title_desc":
                    sql.append("ORDER BY g.title DESC");
                    break;
                case "price_asc":
                    sql.append("ORDER BY g.price ASC");
                    break;
                case "price_desc":
                    sql.append("ORDER BY g.price DESC");
                    break;
                case "rating_desc":
                    sql.append("ORDER BY g.rating DESC");
                    break;
                case "release_date":
                    sql.append("ORDER BY g.releaseDate DESC");
                    break;
                default:
                    sql.append("ORDER BY g.gameId ASC");
            }
        } else {
            sql.append("ORDER BY g.gameId ASC");
        }

        try {
            List<Game> games = DBUtil.executeQuery(sql.toString(), rs -> mapResultSetToGame(rs), params.toArray());

            // For each game, load its platforms and genres relationships
            for (Game game : games) {
                loadGameRelationships(game);
            }

            return games;
        } catch (SQLException e) {
            System.err.println("Error finding games with filters: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
    }

    /**
     * Load platforms and genres for a game
     */
    private void loadGameRelationships(Game game) {
        try {
            // Load platforms
            String platformSql = "SELECT p.* FROM Platforms p " +
                    "JOIN GamePlatforms gp ON p.platformId = gp.platformId " +
                    "WHERE gp.gameId = ?";
            List<Platform> platforms = DBUtil.executeQuery(platformSql, rs -> {
                Platform platform = new Platform();
                platform.setPlatformId(rs.getInt("platformId"));
                platform.setName(rs.getString("name"));
                return platform;
            }, game.getGameId());

            game.setPlatforms(platforms);

            // Load genres
            String genreSql = "SELECT g.* FROM Genres g " +
                    "JOIN GameGenres gg ON g.genreId = gg.genreId " +
                    "WHERE gg.gameId = ?";
            List<Genre> genres = DBUtil.executeQuery(genreSql, rs -> {
                Genre genre = new Genre();
                genre.setGenreId(rs.getInt("genreId"));
                genre.setName(rs.getString("name"));
                return genre;
            }, game.getGameId());

            game.setGenres(genres);

            // Update legacy fields for backward compatibility
            game.updateLegacyFields();

        } catch (SQLException e) {
            System.err.println("Error loading relationships for game " + game.getGameId() + ": " + e.getMessage());
        }
    }

    // Find all games owned by a specific user from completed orders
    public List<Game> findOwnedGamesByUser(Integer userId) {
        String sql = "SELECT DISTINCT g.* FROM Games g " +
                "JOIN OrderItems oi ON g.gameId = oi.gameId " +
                "JOIN Orders o ON oi.orderId = o.orderId " +
                "WHERE o.userId = ?";
        try {
            List<Game> games = DBUtil.executeQuery(sql, rs -> mapResultSetToGame(rs), userId);

            // Load relationships for each game
            for (Game game : games) {
                loadGameRelationships(game);
            }

            return games;
        } catch (SQLException e) {
            System.err.println("Error finding owned games for user: " + userId + " - " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
    }

    // Method to get a few featured games (e.g., first 3 added)
    public List<Game> findFeaturedGames() {
        String sql = "SELECT * FROM Games ORDER BY gameId ASC LIMIT 3"; // Simple example: Get first 3 games
        try {
            List<Game> games = DBUtil.executeQuery(sql, rs -> mapResultSetToGame(rs));

            // Load relationships for each game
            for (Game game : games) {
                loadGameRelationships(game);
            }

            return games;
        } catch (SQLException e) {
            System.err.println("Error finding featured games: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
    }

    // Method to get all available platforms
    public List<Platform> findAllPlatforms() {
        String sql = "SELECT DISTINCT * FROM Platforms ORDER BY name";
        try {
            return DBUtil.executeQuery(sql, rs -> {
                Platform platform = new Platform();
                platform.setPlatformId(rs.getInt("platformId"));
                platform.setName(rs.getString("name"));
                return platform;
            });
        } catch (SQLException e) {
            System.err.println("Error finding all platforms: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Method to get all available genres
    public List<Genre> findAllGenres() {
        String sql = "SELECT DISTINCT * FROM Genres ORDER BY name";
        try {
            return DBUtil.executeQuery(sql, rs -> {
                Genre genre = new Genre();
                genre.setGenreId(rs.getInt("genreId"));
                genre.setName(rs.getString("name"));
                return genre;
            });
        } catch (SQLException e) {
            System.err.println("Error finding all genres: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void save(Game game) {
        Connection conn = null;
        try {
            conn = gamevaultbase.helpers.DBConnectionUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert the game
            String sql = "INSERT INTO Games (title, description, developer, platform, price, releaseDate, imagePath, genre, rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // Ensure releaseDate is not null before attempting to get time
            Date sqlReleaseDate = (game.getReleaseDate() != null) ? new Date(game.getReleaseDate().getTime()) : null;

            PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getDescription());
            pstmt.setString(3, game.getDeveloper());
            pstmt.setString(4, game.getPlatform());
            pstmt.setFloat(5, game.getPrice());
            pstmt.setDate(6, sqlReleaseDate); // Use java.sql.Date
            pstmt.setString(7, game.getImagePath());
            pstmt.setString(8, game.getGenre());
            pstmt.setFloat(9, game.getRating());

            pstmt.executeUpdate();

            // Get the generated game ID
            ResultSet rs = pstmt.getGeneratedKeys();
            int gameId = -1;
            if (rs.next()) {
                gameId = rs.getInt(1);
                game.setGameId(gameId);
            } else {
                throw new SQLException("Failed to retrieve generated game ID");
            }

            // 2. Save genres and link to the game
            if (game.getGenres() != null && !game.getGenres().isEmpty()) {
                saveGameGenres(conn, game);
            } else if (game.getGenre() != null && !game.getGenre().isEmpty()) {
                // If there are no Genre objects but there's a genre string, parse it
                String[] genreNames = game.getGenre().split(",");
                for (String genreName : genreNames) {
                    genreName = genreName.trim();
                    if (!genreName.isEmpty()) {
                        Genre genre = new Genre(genreName);
                        game.addGenre(genre);
                    }
                }
                if (!game.getGenres().isEmpty()) {
                    saveGameGenres(conn, game);
                }
            }

            // 3. Save platforms and link to the game
            if (game.getPlatforms() != null && !game.getPlatforms().isEmpty()) {
                saveGamePlatforms(conn, game);
            } else if (game.getPlatform() != null && !game.getPlatform().isEmpty()) {
                // If there are no Platform objects but there's a platform string, parse it
                String[] platformNames = game.getPlatform().split(",");
                for (String platformName : platformNames) {
                    platformName = platformName.trim();
                    if (!platformName.isEmpty()) {
                        Platform platform = new Platform(platformName);
                        game.addPlatform(platform);
                    }
                }
                if (!game.getPlatforms().isEmpty()) {
                    saveGamePlatforms(conn, game);
                }
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            System.err.println("Error saving game: " + game.getTitle() + " - " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            // Consider rethrowing a custom exception
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    gamevaultbase.helpers.DBConnectionUtil.closeConnection(conn);
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Save genres for a game and create relationships
     */
    private void saveGameGenres(Connection conn, Game game) throws SQLException {
        for (Genre genre : game.getGenres()) {
            // Insert the genre if it doesn't exist and get its ID
            String checkGenreSql = "SELECT genreId FROM Genres WHERE name = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkGenreSql);
            checkStmt.setString(1, genre.getName());
            ResultSet rs = checkStmt.executeQuery();

            int genreId;
            if (rs.next()) {
                genreId = rs.getInt("genreId");
                genre.setGenreId(genreId);
            } else {
                // Insert new genre
                String insertGenreSql = "INSERT INTO Genres (name) VALUES (?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertGenreSql,
                        java.sql.Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, genre.getName());
                insertStmt.executeUpdate();

                ResultSet genreRs = insertStmt.getGeneratedKeys();
                if (genreRs.next()) {
                    genreId = genreRs.getInt(1);
                    genre.setGenreId(genreId);
                } else {
                    throw new SQLException("Failed to insert genre: " + genre.getName());
                }
            }

            // Link the game to the genre
            String linkSql = "INSERT IGNORE INTO GameGenres (gameId, genreId) VALUES (?, ?)";
            PreparedStatement linkStmt = conn.prepareStatement(linkSql);
            linkStmt.setInt(1, game.getGameId());
            linkStmt.setInt(2, genreId);
            linkStmt.executeUpdate();
        }
    }

    /**
     * Save platforms for a game and create relationships
     */
    private void saveGamePlatforms(Connection conn, Game game) throws SQLException {
        for (Platform platform : game.getPlatforms()) {
            // Insert the platform if it doesn't exist and get its ID
            String checkPlatformSql = "SELECT platformId FROM Platforms WHERE name = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkPlatformSql);
            checkStmt.setString(1, platform.getName());
            ResultSet rs = checkStmt.executeQuery();

            int platformId;
            if (rs.next()) {
                platformId = rs.getInt("platformId");
                platform.setPlatformId(platformId);
            } else {
                // Insert new platform
                String insertPlatformSql = "INSERT INTO Platforms (name) VALUES (?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertPlatformSql,
                        java.sql.Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, platform.getName());
                insertStmt.executeUpdate();

                ResultSet platformRs = insertStmt.getGeneratedKeys();
                if (platformRs.next()) {
                    platformId = platformRs.getInt(1);
                    platform.setPlatformId(platformId);
                } else {
                    throw new SQLException("Failed to insert platform: " + platform.getName());
                }
            }

            // Link the game to the platform
            String linkSql = "INSERT IGNORE INTO GamePlatforms (gameId, platformId) VALUES (?, ?)";
            PreparedStatement linkStmt = conn.prepareStatement(linkSql);
            linkStmt.setInt(1, game.getGameId());
            linkStmt.setInt(2, platformId);
            linkStmt.executeUpdate();
        }
    }

    @Override
    public void update(Game game) {
        Connection conn = null;
        try {
            conn = gamevaultbase.helpers.DBConnectionUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Update the basic game information
            String sql = "UPDATE Games SET title = ?, description = ?, developer = ?, platform = ?, price = ?, releaseDate = ?, imagePath = ?, genre = ?, rating = ? WHERE gameId = ?";
            Date sqlReleaseDate = (game.getReleaseDate() != null) ? new Date(game.getReleaseDate().getTime()) : null;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getDescription());
            pstmt.setString(3, game.getDeveloper());
            pstmt.setString(4, game.getPlatform());
            pstmt.setFloat(5, game.getPrice());
            pstmt.setDate(6, sqlReleaseDate);
            pstmt.setString(7, game.getImagePath());
            pstmt.setString(8, game.getGenre());
            pstmt.setFloat(9, game.getRating());
            pstmt.setInt(10, game.getGameId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Game update failed, no rows affected, gameId: " + game.getGameId());
            }

            // 2. Update genres (delete existing links and recreate)
            String deleteGenresSql = "DELETE FROM GameGenres WHERE gameId = ?";
            PreparedStatement deleteGenresStmt = conn.prepareStatement(deleteGenresSql);
            deleteGenresStmt.setInt(1, game.getGameId());
            deleteGenresStmt.executeUpdate();

            // Save genres
            if (game.getGenres() != null && !game.getGenres().isEmpty()) {
                saveGameGenres(conn, game);
            } else if (game.getGenre() != null && !game.getGenre().isEmpty()) {
                // If there are no Genre objects but there's a genre string, parse it
                String[] genreNames = game.getGenre().split(",");
                for (String genreName : genreNames) {
                    genreName = genreName.trim();
                    if (!genreName.isEmpty()) {
                        Genre genre = new Genre(genreName);
                        game.addGenre(genre);
                    }
                }
                if (!game.getGenres().isEmpty()) {
                    saveGameGenres(conn, game);
                }
            }

            // 3. Update platforms (delete existing links and recreate)
            String deletePlatformsSql = "DELETE FROM GamePlatforms WHERE gameId = ?";
            PreparedStatement deletePlatformsStmt = conn.prepareStatement(deletePlatformsSql);
            deletePlatformsStmt.setInt(1, game.getGameId());
            deletePlatformsStmt.executeUpdate();

            // Save platforms
            if (game.getPlatforms() != null && !game.getPlatforms().isEmpty()) {
                saveGamePlatforms(conn, game);
            } else if (game.getPlatform() != null && !game.getPlatform().isEmpty()) {
                // If there are no Platform objects but there's a platform string, parse it
                String[] platformNames = game.getPlatform().split(",");
                for (String platformName : platformNames) {
                    platformName = platformName.trim();
                    if (!platformName.isEmpty()) {
                        Platform platform = new Platform(platformName);
                        game.addPlatform(platform);
                    }
                }
                if (!game.getPlatforms().isEmpty()) {
                    saveGamePlatforms(conn, game);
                }
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            System.err.println("Error updating game: " + game.getGameId() + " - " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    gamevaultbase.helpers.DBConnectionUtil.closeConnection(conn);
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void delete(Integer gameId) {
        Connection conn = null;
        try {
            conn = gamevaultbase.helpers.DBConnectionUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Delete game relationship records first (should cascade, but being explicit)
            String deleteGenresSql = "DELETE FROM GameGenres WHERE gameId = ?";
            PreparedStatement deleteGenresStmt = conn.prepareStatement(deleteGenresSql);
            deleteGenresStmt.setInt(1, gameId);
            deleteGenresStmt.executeUpdate();

            String deletePlatformsSql = "DELETE FROM GamePlatforms WHERE gameId = ?";
            PreparedStatement deletePlatformsStmt = conn.prepareStatement(deletePlatformsSql);
            deletePlatformsStmt.setInt(1, gameId);
            deletePlatformsStmt.executeUpdate();

            // Delete the game
            String deleteGameSql = "DELETE FROM Games WHERE gameId = ?";
            PreparedStatement deleteGameStmt = conn.prepareStatement(deleteGameSql);
            deleteGameStmt.setInt(1, gameId);
            int rowsAffected = deleteGameStmt.executeUpdate();

            if (rowsAffected == 0) {
                System.err.println("WARN: Delete affected 0 rows for gameId: " + gameId);
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            // Catch potential foreign key constraint violation errors specifically if
            // needed
            System.err.println("Error deleting game: " + gameId + " - " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    gamevaultbase.helpers.DBConnectionUtil.closeConnection(conn);
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
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
                rs.getString("imagePath"),
                rs.getString("genre"),
                rs.getFloat("rating"));
    }
}