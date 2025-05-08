package gamevaultbase.helpers;

import gamevaultbase.entities.Game;
import javax.servlet.ServletContext;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for database CRUD operations
 */
public class DBUtil {

    // Flag to ensure DB/Tables checked only once
    private static boolean setupComplete = false;

    /**
     * Initializes tables and loads sample data if needed
     * Should be called once during application startup
     * 
     * @param context ServletContext to read resources
     * @throws SQLException if database operations fail
     */
    public static synchronized void initialize(ServletContext context) throws SQLException {
        if (setupComplete) {
            return; // Already initialized
        }

        System.out.println("Initializing database tables and sample data...");

        try (Connection conn = DBConnectionUtil.getConnection()) {
            // Create tables if they don't exist
            createTablesIfNotExist(conn);

            // Check if Games table is empty and load sample data if needed
            if (isTableEmpty(conn, "Games")) {
                System.out.println("Games table is empty. Loading sample data from JSON...");
                try {
                    loadSampleGamesFromJson(context, conn);
                    System.out.println("Sample game data loaded successfully.");
                } catch (Exception e) {
                    System.err.println("WARNING: Failed to load sample game data: " + e.getMessage());
                    // Continue initialization even if sample data loading fails
                }
            }

            // Create default admin user if no admin exists
            createDefaultAdminIfNeeded(conn);

        } catch (SQLException e) {
            System.err.println("FATAL: Failed to initialize database: " + e.getMessage());
            throw e;
        }

        System.out.println("Database initialization complete.");
        setupComplete = true;
    }

    /**
     * Creates tables if they don't exist
     */
    private static void createTablesIfNotExist(Connection conn) throws SQLException {
        System.out.println("Checking/Creating tables in database...");
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(SQL_CREATE_USERS_TABLE);
            statement.executeUpdate(SQL_CREATE_GAMES_TABLE);
            statement.executeUpdate(SQL_CREATE_CARTS_TABLE);
            statement.executeUpdate(SQL_CREATE_CART_ITEMS_TABLE);
            statement.executeUpdate(SQL_CREATE_ORDERS_TABLE);
            statement.executeUpdate(SQL_CREATE_ORDER_ITEMS_TABLE);
            statement.executeUpdate(SQL_CREATE_TRANSACTIONS_TABLE);
            statement.executeUpdate(SQL_CREATE_REVIEWS_TABLE);
            System.out.println("Tables checked/created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Checks if a table is empty (has no rows)
     */
    private static boolean isTableEmpty(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
            return true; // Default to true if no result
        }
    }

    /**
     * Loads sample game data from the games.json file
     */
    private static void loadSampleGamesFromJson(ServletContext context, Connection conn)
            throws Exception {
        // Get the games.json file from the WEB-INF directory
        try (InputStream inputStream = context.getResourceAsStream("/WEB-INF/games.json")) {
            if (inputStream == null) {
                throw new Exception("Sample games.json file not found in WEB-INF directory");
            }

            // Parse JSON data using JSONUtil
            List<Game> games = JSONUtil.parseGamesFromJson(inputStream);

            if (games.isEmpty()) {
                System.out.println("No games found in JSON file.");
                return;
            }

            // Insert games into the database
            String sql = "INSERT INTO Games (title, description, developer, platform, price, releaseDate, imagePath, genre, rating) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Game game : games) {
                    pstmt.setString(1, game.getTitle());
                    pstmt.setString(2, game.getDescription());
                    pstmt.setString(3, game.getDeveloper());
                    pstmt.setString(4, game.getPlatform());
                    pstmt.setFloat(5, game.getPrice());

                    if (game.getReleaseDate() != null) {
                        pstmt.setDate(6, new java.sql.Date(game.getReleaseDate().getTime()));
                    } else {
                        pstmt.setNull(6, Types.DATE);
                    }

                    pstmt.setString(7, game.getImagePath());
                    pstmt.setString(8, game.getGenre());
                    pstmt.setFloat(9, game.getRating());

                    pstmt.addBatch();
                }

                int[] results = pstmt.executeBatch();
                System.out.println("Inserted " + results.length + " games from JSON file.");
            }
        }
    }

    /**
     * Creates a default admin user if no admin exists
     */
    private static void createDefaultAdminIfNeeded(Connection conn) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Users WHERE isAdmin = TRUE";

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // No admin exists, create one
                System.out.println("No admin user found. Creating default admin account...");

                String insertSql = "INSERT INTO Users (email, password, username, walletBalance, isAdmin) " +
                        "VALUES ('admin@gamevault.com', 'admin123', 'admin', 1000.0, TRUE)";

                try (Statement insertStmt = conn.createStatement()) {
                    insertStmt.executeUpdate(insertSql);
                    System.out.println("Default admin account created successfully.");
                }
            }
        }
    }

    // --- Generic CRUD Operations ---

    /**
     * Execute a query and process results with a handler
     * 
     * @param sql     SQL query with ? placeholders for parameters
     * @param handler Handler to process each row of results
     * @param params  Parameters to substitute in the SQL
     * @return List of objects produced by the handler
     */
    public static <T> List<T> executeQuery(String sql, ResultSetHandler<T> handler, Object... params)
            throws SQLException {
        List<T> results = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            // Set parameters if any
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(handler.handle(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            throw e;
        } finally {
            // Close resources in reverse order
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignore */ }
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    /* ignore */ }
            if (conn != null)
                DBConnectionUtil.closeConnection(conn);
        }

        return results;
    }

    /**
     * Execute an update (INSERT, UPDATE, DELETE) statement
     * 
     * @param sql    SQL statement with ? placeholders for parameters
     * @param params Parameters to substitute in the SQL
     * @return Number of rows affected
     */
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            // Set parameters if any
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            throw e;
        } finally {
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    /* ignore */ }
            if (conn != null)
                DBConnectionUtil.closeConnection(conn);
        }
    }

    /**
     * Execute an insert and return generated keys
     * 
     * @param sql    SQL INSERT statement with ? placeholders for parameters
     * @param params Parameters to substitute in the SQL
     * @return The generated key (usually primary key ID)
     */
    public static int executeInsertAndGetKey(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int generatedKey = -1;

        try {
            conn = DBConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set parameters if any
            for (int i = 0; i < params.length; i++) {
                if (params[i] == null) {
                    pstmt.setNull(i + 1, Types.NULL);
                } else {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error executing insert: " + e.getMessage());
            throw e;
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignore */ }
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    /* ignore */ }
            if (conn != null)
                DBConnectionUtil.closeConnection(conn);
        }

        return generatedKey;
    }

    /**
     * ResultSet handler interface for processing query results
     */
    @FunctionalInterface
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }

    // --- SQL Constants for Table Creation ---
    private static final String SQL_CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS Users (" +
            "userId INT AUTO_INCREMENT PRIMARY KEY," +
            "email VARCHAR(255) NOT NULL UNIQUE," +
            "password VARCHAR(255) NOT NULL," +
            "username VARCHAR(255) NOT NULL UNIQUE," +
            "walletBalance FLOAT NOT NULL DEFAULT 0.0," +
            "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "isAdmin BOOLEAN DEFAULT FALSE" +
            ") ENGINE=InnoDB;";

    private static final String SQL_CREATE_GAMES_TABLE = "CREATE TABLE IF NOT EXISTS Games (" +
            "gameId INT AUTO_INCREMENT PRIMARY KEY," +
            "title VARCHAR(255) NOT NULL," +
            "description TEXT," +
            "developer VARCHAR(255)," +
            "platform VARCHAR(255)," +
            "price FLOAT NOT NULL," +
            "releaseDate DATE," +
            "imagePath VARCHAR(255)," +
            "genre VARCHAR(100)," +
            "rating FLOAT DEFAULT 0.0" +
            ") ENGINE=InnoDB;";

    private static final String SQL_CREATE_CARTS_TABLE = "CREATE TABLE IF NOT EXISTS Carts (" +
            "userId INT PRIMARY KEY," +
            "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
            "FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE" +
            ") ENGINE=InnoDB;";

    private static final String SQL_CREATE_CART_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS CartItems (" +
            "cartItemId INT AUTO_INCREMENT PRIMARY KEY," +
            "userId INT NOT NULL," +
            "gameId INT NOT NULL," +
            "addedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE," +
            "FOREIGN KEY (gameId) REFERENCES Games(gameId) ON DELETE CASCADE," +
            "UNIQUE KEY `unique_user_game` (`userId`,`gameId`)" +
            ") ENGINE=InnoDB;";

    private static final String SQL_CREATE_ORDERS_TABLE = "CREATE TABLE IF NOT EXISTS Orders (" +
            "orderId INT AUTO_INCREMENT PRIMARY KEY," +
            "userId INT NOT NULL," +
            "totalAmount DOUBLE NOT NULL," +
            "orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE RESTRICT" +
            ") ENGINE=InnoDB;";

    private static final String SQL_CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS OrderItems (" +
            "orderItemId INT AUTO_INCREMENT PRIMARY KEY," +
            "orderId INT NOT NULL," +
            "gameId INT NOT NULL," +
            "priceAtPurchase FLOAT NOT NULL," +
            "FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE," +
            "FOREIGN KEY (gameId) REFERENCES Games(gameId) ON DELETE RESTRICT" +
            ") ENGINE=InnoDB;";

    private static final String SQL_CREATE_TRANSACTIONS_TABLE = "CREATE TABLE IF NOT EXISTS Transactions (" +
            "transactionId INT AUTO_INCREMENT PRIMARY KEY," +
            "orderId INT NULL," +
            "userId INT NOT NULL," +
            "type VARCHAR(50) NOT NULL," +
            "amount FLOAT NOT NULL," +
            "transactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE RESTRICT," +
            "FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE SET NULL" +
            ") ENGINE=InnoDB;";

    private static final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE IF NOT EXISTS Reviews (" +
            "reviewId INT AUTO_INCREMENT PRIMARY KEY," +
            "gameId INT NOT NULL," +
            "userId INT NOT NULL," +
            "rating INT NOT NULL," +
            "comment TEXT NOT NULL," +
            "reviewDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (gameId) REFERENCES Games(gameId) ON DELETE CASCADE," +
            "FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE" +
            ") ENGINE=InnoDB;";
}