package gamevaultbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Direct database testing utility to check database connection and perform
 * raw SQL queries without using Storage classes
 */
public class TestDirectDatabase {

    public static void main(String[] args) {
        Connection conn = null;

        try {
            System.out.println("Testing direct database connection...");

            // Get connection
            conn = gamevaultbase.helpers.DBConnectionUtil.getConnection();
            System.out.println("Database connection successful!");

            // Test database tables
            checkTables(conn);

            // Insert a test game directly with SQL
            insertTestGame(conn);

            // Query and display all games
            listAllGames(conn);

        } catch (SQLException e) {
            System.err.println("Database test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Check if required tables exist
     */
    private static void checkTables(Connection conn) throws SQLException {
        System.out.println("\n=== Checking Database Tables ===");
        String[] tables = { "Games", "Genres", "Platforms", "GameGenres", "GamePlatforms" };

        for (String table : tables) {
            try (Statement stmt = conn.createStatement()) {
                // Try to select from the table to see if it exists
                ResultSet rs = stmt.executeQuery("SELECT 1 FROM " + table + " LIMIT 1");
                System.out.println("Table " + table + " exists.");
            } catch (SQLException e) {
                System.err
                        .println("ERROR: Table " + table + " does not exist or cannot be accessed: " + e.getMessage());
            }
        }
    }

    /**
     * Insert a test game directly with SQL
     */
    private static void insertTestGame(Connection conn) throws SQLException {
        System.out.println("\n=== Inserting Test Game with Direct SQL ===");

        // First, commit any pending transactions
        if (!conn.getAutoCommit()) {
            conn.setAutoCommit(true);
        }

        String sql = "INSERT INTO Games (title, description, developer, platform, price, releaseDate, imagePath, genre, rating) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int gameId = -1;

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set values for the new game
            String uniqueId = String.valueOf(System.currentTimeMillis());
            pstmt.setString(1, "Direct SQL Test Game " + uniqueId);
            pstmt.setString(2, "This game was inserted using direct SQL queries");
            pstmt.setString(3, "SQL Test Developer");
            pstmt.setString(4, "PC, Xbox");
            pstmt.setFloat(5, 29.99f);
            pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            pstmt.setString(7, "game_images/test_game.png");
            pstmt.setString(8, "Action, Simulation");
            pstmt.setFloat(9, 4.8f);

            // Execute the insert
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected by insert: " + rowsAffected);

            // Get the generated ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    gameId = rs.getInt(1);
                    System.out.println("Generated Game ID: " + gameId);
                }
            }
        }

        // If game was inserted successfully, add platform and genre relationships
        if (gameId > 0) {
            // Add "PC" platform
            addPlatform(conn, gameId, "PC");
            // Add "Xbox" platform
            addPlatform(conn, gameId, "Xbox");

            // Add "Action" genre
            addGenre(conn, gameId, "Action");
            // Add "Simulation" genre
            addGenre(conn, gameId, "Simulation");
        }
    }

    /**
     * Add a platform to a game
     */
    private static void addPlatform(Connection conn, int gameId, String platformName) throws SQLException {
        // First check if the platform exists
        int platformId = -1;

        String checkSql = "SELECT platformId FROM Platforms WHERE name = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, platformName);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    platformId = rs.getInt("platformId");
                    System.out.println("Found existing platform: " + platformName + " with ID " + platformId);
                }
            }
        }

        // If platform doesn't exist, add it
        if (platformId == -1) {
            String insertPlatformSql = "INSERT INTO Platforms (name) VALUES (?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertPlatformSql,
                    Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, platformName);
                insertStmt.executeUpdate();

                try (ResultSet rs = insertStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        platformId = rs.getInt(1);
                        System.out.println("Created new platform: " + platformName + " with ID " + platformId);
                    }
                }
            }
        }

        // Link the game to the platform
        if (platformId > 0) {
            String linkSql = "INSERT INTO GamePlatforms (gameId, platformId) VALUES (?, ?)";
            try (PreparedStatement linkStmt = conn.prepareStatement(linkSql)) {
                linkStmt.setInt(1, gameId);
                linkStmt.setInt(2, platformId);
                int result = linkStmt.executeUpdate();
                System.out.println(
                        "Linked game " + gameId + " to platform " + platformId + ": " + result + " row(s) affected");
            }
        }
    }

    /**
     * Add a genre to a game
     */
    private static void addGenre(Connection conn, int gameId, String genreName) throws SQLException {
        // First check if the genre exists
        int genreId = -1;

        String checkSql = "SELECT genreId FROM Genres WHERE name = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, genreName);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    genreId = rs.getInt("genreId");
                    System.out.println("Found existing genre: " + genreName + " with ID " + genreId);
                }
            }
        }

        // If genre doesn't exist, add it
        if (genreId == -1) {
            String insertGenreSql = "INSERT INTO Genres (name) VALUES (?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertGenreSql,
                    Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, genreName);
                insertStmt.executeUpdate();

                try (ResultSet rs = insertStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        genreId = rs.getInt(1);
                        System.out.println("Created new genre: " + genreName + " with ID " + genreId);
                    }
                }
            }
        }

        // Link the game to the genre
        if (genreId > 0) {
            String linkSql = "INSERT INTO GameGenres (gameId, genreId) VALUES (?, ?)";
            try (PreparedStatement linkStmt = conn.prepareStatement(linkSql)) {
                linkStmt.setInt(1, gameId);
                linkStmt.setInt(2, genreId);
                int result = linkStmt.executeUpdate();
                System.out
                        .println("Linked game " + gameId + " to genre " + genreId + ": " + result + " row(s) affected");
            }
        }
    }

    /**
     * List all games in the database
     */
    private static void listAllGames(Connection conn) throws SQLException {
        System.out.println("\n=== Listing All Games ===");

        String sql = "SELECT * FROM Games";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            int count = 0;
            while (rs.next()) {
                count++;
                int gameId = rs.getInt("gameId");
                String title = rs.getString("title");
                String developer = rs.getString("developer");
                float price = rs.getFloat("price");

                System.out.println(count + ". Game ID: " + gameId +
                        ", Title: " + title +
                        ", Developer: " + developer +
                        ", Price: $" + price);

                // Get platforms for this game
                String platformSql = "SELECT p.name FROM Platforms p " +
                        "JOIN GamePlatforms gp ON p.platformId = gp.platformId " +
                        "WHERE gp.gameId = ?";
                try (PreparedStatement pStmt = conn.prepareStatement(platformSql)) {
                    pStmt.setInt(1, gameId);
                    try (ResultSet pRs = pStmt.executeQuery()) {
                        StringBuilder platforms = new StringBuilder("Platforms: ");
                        boolean hasPlatforms = false;

                        while (pRs.next()) {
                            if (hasPlatforms) {
                                platforms.append(", ");
                            }
                            platforms.append(pRs.getString("name"));
                            hasPlatforms = true;
                        }

                        if (hasPlatforms) {
                            System.out.println("   " + platforms.toString());
                        } else {
                            System.out.println("   No platforms found for this game");
                        }
                    }
                }

                // Get genres for this game
                String genreSql = "SELECT g.name FROM Genres g " +
                        "JOIN GameGenres gg ON g.genreId = gg.genreId " +
                        "WHERE gg.gameId = ?";
                try (PreparedStatement gStmt = conn.prepareStatement(genreSql)) {
                    gStmt.setInt(1, gameId);
                    try (ResultSet gRs = gStmt.executeQuery()) {
                        StringBuilder genres = new StringBuilder("Genres: ");
                        boolean hasGenres = false;

                        while (gRs.next()) {
                            if (hasGenres) {
                                genres.append(", ");
                            }
                            genres.append(gRs.getString("name"));
                            hasGenres = true;
                        }

                        if (hasGenres) {
                            System.out.println("   " + genres.toString());
                        } else {
                            System.out.println("   No genres found for this game");
                        }
                    }
                }

                System.out.println();
            }

            if (count == 0) {
                System.out.println("No games found in the database.");
            } else {
                System.out.println("Total games: " + count);
            }
        }
    }
}
