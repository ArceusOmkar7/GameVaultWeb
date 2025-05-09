package gamevaultbase.storage;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Genre;
import gamevaultbase.entities.Platform;
import gamevaultbase.interfaces.StorageInterface;
import gamevaultbase.helpers.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        PreparedStatement pstmt = null;

        try {
            System.out.println("GameStorage: Attempting to get database connection...");
            conn = gamevaultbase.helpers.DBConnectionUtil.getConnection();
            if (conn == null) {
                System.err.println("ERROR: Failed to get database connection");
                throw new SQLException("Failed to get database connection");
            }

            conn.setAutoCommit(false); // Start transaction
            System.out.println("GameStorage: Database connection established successfully");

            try {
                // 1. Insert the game
                String sql = "INSERT INTO Games (title, description, developer, platform, price, releaseDate, imagePath, genre, rating, addedAt) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, game.getTitle());
                pstmt.setString(2, game.getDescription());
                pstmt.setString(3, game.getDeveloper());
                pstmt.setString(4, game.getPlatform());
                pstmt.setFloat(5, game.getPrice());
                pstmt.setDate(6, new java.sql.Date(game.getReleaseDate().getTime()));
                pstmt.setString(7, game.getImagePath());
                pstmt.setString(8, game.getGenre());
                pstmt.setFloat(9, game.getRating());
                pstmt.setTimestamp(10, new java.sql.Timestamp(System.currentTimeMillis()));

                System.out.println("GameStorage: Executing game insert with title: " + game.getTitle());
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating game failed, no rows affected.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        game.setGameId(generatedKeys.getInt(1));
                        System.out.println("GameStorage: Game inserted with ID: " + game.getGameId());
                    } else {
                        throw new SQLException("Creating game failed, no ID obtained.");
                    }
                }

                // 2. Save platforms
                saveGamePlatforms(conn, game);

                // 3. Save genres
                saveGameGenres(conn, game);

                // If we get here, commit the transaction
                conn.commit();
                System.out.println("GameStorage: Transaction committed successfully");

            } catch (SQLException e) {
                // If there's an error, rollback the transaction
                if (conn != null) {
                    try {
                        conn.rollback();
                        System.err.println("GameStorage: Transaction rolled back due to error: " + e.getMessage());
                    } catch (SQLException ex) {
                        System.err.println("GameStorage: Error rolling back transaction: " + ex.getMessage());
                    }
                }
                throw e;
            } finally {
                // Close the prepared statement
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (SQLException e) {
                        System.err.println("GameStorage: Error closing prepared statement: " + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("GameStorage: Error saving game: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save game: " + e.getMessage(), e);
        } finally {
            // Close the connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("GameStorage: Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Save genres for a game and create relationships
     */
    private void saveGameGenres(Connection conn, Game game) throws SQLException {
        System.out.println("Starting saveGameGenres for game ID " + game.getGameId());
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        PreparedStatement linkStmt = null;

        try {
            for (Genre genre : game.getGenres()) {
                if (genre == null || genre.getName() == null || genre.getName().trim().isEmpty()) {
                    System.out.println("Skipping empty genre");
                    continue;
                }

                String genreName = genre.getName().trim();
                System.out.println("Processing genre: " + genreName);

                // Insert the genre if it doesn't exist and get its ID
                String checkGenreSql = "SELECT genreId FROM Genres WHERE name = ?";
                checkStmt = conn.prepareStatement(checkGenreSql);
                checkStmt.setString(1, genreName);
                ResultSet rs = checkStmt.executeQuery();

                int genreId;
                if (rs.next()) {
                    genreId = rs.getInt("genreId");
                    genre.setGenreId(genreId);
                    System.out.println("Found existing genre: " + genreName + " with ID: " + genreId);
                } else {
                    // Insert new genre
                    String insertGenreSql = "INSERT INTO Genres (name) VALUES (?)";
                    insertStmt = conn.prepareStatement(insertGenreSql,
                            java.sql.Statement.RETURN_GENERATED_KEYS);
                    insertStmt.setString(1, genreName);
                    int rowsAffected = insertStmt.executeUpdate();
                    System.out.println("Inserted new genre: " + genreName + " - rows affected: " + rowsAffected);

                    ResultSet genreRs = insertStmt.getGeneratedKeys();
                    if (genreRs.next()) {
                        genreId = genreRs.getInt(1);
                        genre.setGenreId(genreId);
                        System.out.println("New genre ID: " + genreId);
                    } else {
                        throw new SQLException("Failed to insert genre: " + genreName);
                    }
                }

                // Link the game to the genre - using INSERT IGNORE to avoid duplicates
                // Or use a more compatible approach
                String linkSql = "INSERT INTO GameGenres (gameId, genreId) VALUES (?, ?)";
                // First check if relationship already exists
                String checkLinkSql = "SELECT 1 FROM GameGenres WHERE gameId = ? AND genreId = ?";
                PreparedStatement checkLinkStmt = conn.prepareStatement(checkLinkSql);
                checkLinkStmt.setInt(1, game.getGameId());
                checkLinkStmt.setInt(2, genreId);
                ResultSet linkRs = checkLinkStmt.executeQuery();

                if (!linkRs.next()) {
                    // Only insert if not exists
                    linkStmt = conn.prepareStatement(linkSql);
                    linkStmt.setInt(1, game.getGameId());
                    linkStmt.setInt(2, genreId);
                    int linkResult = linkStmt.executeUpdate();
                    System.out.println(
                            "Linked game " + game.getGameId() + " to genre " + genreId + " - result: " + linkResult);
                } else {
                    System.out.println("Game " + game.getGameId() + " already linked to genre " + genreId);
                }

                if (checkLinkStmt != null) {
                    checkLinkStmt.close();
                }
            }
        } finally {
            // Close statements
            if (checkStmt != null) {
                checkStmt.close();
            }
            if (insertStmt != null) {
                insertStmt.close();
            }
            if (linkStmt != null) {
                linkStmt.close();
            }
        }
        System.out.println("Completed saveGameGenres for game ID " + game.getGameId());
    }

    /**
     * Save platforms for a game and create relationships
     */
    private void saveGamePlatforms(Connection conn, Game game) throws SQLException {
        System.out.println("GameStorage: Starting saveGamePlatforms for game ID " + game.getGameId());
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        PreparedStatement linkStmt = null;

        try {
            for (Platform platform : game.getPlatforms()) {
                if (platform == null || platform.getName() == null || platform.getName().trim().isEmpty()) {
                    System.out.println("GameStorage: Skipping empty platform");
                    continue;
                }

                String platformName = platform.getName().trim();
                System.out.println("GameStorage: Processing platform: " + platformName);

                // Insert the platform if it doesn't exist and get its ID
                String checkPlatformSql = "SELECT platformId FROM Platforms WHERE name = ?";
                checkStmt = conn.prepareStatement(checkPlatformSql);
                checkStmt.setString(1, platformName);
                ResultSet rs = checkStmt.executeQuery();

                int platformId;
                if (rs.next()) {
                    platformId = rs.getInt("platformId");
                    platform.setPlatformId(platformId);
                    System.out.println(
                            "GameStorage: Found existing platform: " + platformName + " with ID: " + platformId);
                } else {
                    // Insert new platform
                    String insertPlatformSql = "INSERT INTO Platforms (name) VALUES (?)";
                    insertStmt = conn.prepareStatement(insertPlatformSql,
                            java.sql.Statement.RETURN_GENERATED_KEYS);
                    insertStmt.setString(1, platformName);
                    int rowsAffected = insertStmt.executeUpdate();
                    System.out.println(
                            "GameStorage: Inserted new platform: " + platformName + " - rows affected: "
                                    + rowsAffected);

                    ResultSet platformRs = insertStmt.getGeneratedKeys();
                    if (platformRs.next()) {
                        platformId = platformRs.getInt(1);
                        platform.setPlatformId(platformId);
                        System.out.println("GameStorage: New platform ID: " + platformId);
                    } else {
                        throw new SQLException("Failed to insert platform: " + platformName);
                    }
                }

                // Link the game to the platform - first check if the relationship exists
                String checkLinkSql = "SELECT 1 FROM GamePlatforms WHERE gameId = ? AND platformId = ?";
                PreparedStatement checkLinkStmt = conn.prepareStatement(checkLinkSql);
                checkLinkStmt.setInt(1, game.getGameId());
                checkLinkStmt.setInt(2, platformId);
                ResultSet linkRs = checkLinkStmt.executeQuery();

                if (!linkRs.next()) {
                    // Only insert if not exists
                    String linkSql = "INSERT INTO GamePlatforms (gameId, platformId) VALUES (?, ?)";
                    linkStmt = conn.prepareStatement(linkSql);
                    linkStmt.setInt(1, game.getGameId());
                    linkStmt.setInt(2, platformId);
                    int linkResult = linkStmt.executeUpdate();
                    System.out.println("GameStorage: Linked game " + game.getGameId() + " to platform " + platformId
                            + " - result: "
                            + linkResult);
                } else {
                    System.out.println(
                            "GameStorage: Game " + game.getGameId() + " already linked to platform " + platformId);
                }

                if (checkLinkStmt != null) {
                    checkLinkStmt.close();
                }
            }
        } finally {
            // Close statements
            if (checkStmt != null) {
                checkStmt.close();
            }
            if (insertStmt != null) {
                insertStmt.close();
            }
            if (linkStmt != null) {
                linkStmt.close();
            }
        }
        System.out.println("GameStorage: Completed saveGamePlatforms for game ID " + game.getGameId());
    }

    @Override
    public void update(Game game) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            System.out.println("Attempting to get database connection for update...");
            conn = gamevaultbase.helpers.DBConnectionUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction
            System.out.println("Database connection established successfully for update");// 1. Update the basic game
                                                                                          // information
            String sql = "UPDATE Games SET title = ?, description = ?, developer = ?, platform = ?, price = ?, releaseDate = ?, imagePath = ?, genre = ?, rating = ? WHERE gameId = ?";
            System.out.println("Preparing UPDATE SQL: " + sql);

            java.sql.Date sqlReleaseDate = null;
            if (game.getReleaseDate() != null) {
                sqlReleaseDate = new java.sql.Date(game.getReleaseDate().getTime());
            }

            // Debug values being updated
            System.out.println("Values being updated:");
            System.out.println("1. Title: " + game.getTitle());
            System.out.println("2. Description: " + (game.getDescription() != null
                    ? game.getDescription().substring(0, Math.min(20, game.getDescription().length())) + "..."
                    : "null"));
            System.out.println("3. Developer: " + game.getDeveloper());
            System.out.println("4. Platform: " + game.getPlatform());
            System.out.println("5. Price: " + game.getPrice());
            System.out.println("6. ReleaseDate: " + sqlReleaseDate);
            System.out.println("7. ImagePath: " + game.getImagePath());
            System.out.println("8. Genre: " + game.getGenre());
            System.out.println("9. Rating: " + game.getRating());
            System.out.println("10. GameId: " + game.getGameId());

            pstmt = conn.prepareStatement(sql);
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

            System.out.println("Executing UPDATE statement...");
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("UPDATE completed. Rows affected: " + rowsAffected);

            if (rowsAffected == 0) {
                throw new SQLException("Game update failed, no rows affected, gameId: " + game.getGameId());
            } // 2. Update genres (delete existing links and recreate)
            System.out.println("Updating genre relationships...");
            String deleteGenresSql = "DELETE FROM GameGenres WHERE gameId = ?";
            System.out.println("Deleting existing genre links: " + deleteGenresSql);
            PreparedStatement deleteGenresStmt = conn.prepareStatement(deleteGenresSql);
            deleteGenresStmt.setInt(1, game.getGameId());
            int genresDeleted = deleteGenresStmt.executeUpdate();
            System.out.println("Deleted " + genresDeleted + " genre relationships");

            // Save genres
            if (game.getGenres() != null && !game.getGenres().isEmpty()) {
                System.out.println("Saving " + game.getGenres().size() + " genres");
                saveGameGenres(conn, game);
            } else if (game.getGenre() != null && !game.getGenre().isEmpty()) {
                // If there are no Genre objects but there's a genre string, parse it
                System.out.println("Parsing genres from string: " + game.getGenre());
                String[] genreNames = game.getGenre().split(",");
                for (String genreName : genreNames) {
                    genreName = genreName.trim();
                    if (!genreName.isEmpty()) {
                        Genre genre = new Genre(genreName);
                        game.addGenre(genre);
                        System.out.println("Added genre: " + genreName);
                    }
                }
                if (!game.getGenres().isEmpty()) {
                    System.out.println("Saving " + game.getGenres().size() + " parsed genres");
                    saveGameGenres(conn, game);
                }
            }

            // 3. Update platforms (delete existing links and recreate)
            System.out.println("Updating platform relationships...");
            String deletePlatformsSql = "DELETE FROM GamePlatforms WHERE gameId = ?";
            System.out.println("Deleting existing platform links: " + deletePlatformsSql);
            PreparedStatement deletePlatformsStmt = conn.prepareStatement(deletePlatformsSql);
            deletePlatformsStmt.setInt(1, game.getGameId());
            int platformsDeleted = deletePlatformsStmt.executeUpdate();
            System.out.println("Deleted " + platformsDeleted + " platform relationships");

            // Save platforms
            if (game.getPlatforms() != null && !game.getPlatforms().isEmpty()) {
                System.out.println("Saving " + game.getPlatforms().size() + " platforms");
                saveGamePlatforms(conn, game);
            } else if (game.getPlatform() != null && !game.getPlatform().isEmpty()) {
                // If there are no Platform objects but there's a platform string, parse it
                System.out.println("Parsing platforms from string: " + game.getPlatform());
                String[] platformNames = game.getPlatform().split(",");
                for (String platformName : platformNames) {
                    platformName = platformName.trim();
                    if (!platformName.isEmpty()) {
                        Platform platform = new Platform(platformName);
                        game.addPlatform(platform);
                        System.out.println("Added platform: " + platformName);
                    }
                }
                if (!game.getPlatforms().isEmpty()) {
                    System.out.println("Saving " + game.getPlatforms().size() + " parsed platforms");
                    saveGamePlatforms(conn, game);
                }
            }
            System.out.println("Committing update transaction...");
            conn.commit(); // Commit transaction
            System.out.println("Update transaction committed successfully");
        } catch (SQLException e) {
            System.err.println("SQL Error updating game: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.println("Rolling back update transaction...");
                    conn.rollback(); // Rollback on error
                    System.err.println("Update transaction rolled back");
                } catch (SQLException ex) {
                    System.err.println("Error rolling back update transaction: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            // Log error but don't rethrow
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    gamevaultbase.helpers.DBConnectionUtil.closeConnection(conn);
                    System.out.println("Database connection closed after update");
                } catch (SQLException e) {
                    System.err.println("Error closing connection after update: " + e.getMessage());
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