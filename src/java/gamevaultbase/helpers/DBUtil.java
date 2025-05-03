package gamevaultbase.helpers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import java.io.IOException;

public class DBUtil {

    // Runtime connection details - loaded from web.xml via AppContextListener
    private static String dbUrl;       // Full URL including database name
    private static String dbUser;
    private static String dbPassword;
    private static String dbDriver;

    private static boolean driverLoaded = false;
    private static boolean setupComplete = false; // Flag to ensure DB/Tables checked only once

    /**
     * Initializes DBUtil with runtime credentials and performs initial setup.
     * Should be called once from AppContextListener.
     * @param context ServletContext to read init parameters.
     * @throws SQLException if driver loading, DB connection, or schema setup fails.
     * @throws ClassNotFoundException if the JDBC driver class is not found.
     */
    public static synchronized void initialize(ServletContext context) throws SQLException, ClassNotFoundException {
        if (setupComplete) {
            return; // Already initialized
        }

        System.out.println("Initializing DBUtil with direct JDBC connection...");

        // Load runtime credentials from web.xml context parameters
        dbUrl = context.getInitParameter("db.url");
        dbUser = context.getInitParameter("db.user");
        dbPassword = context.getInitParameter("db.password");
        dbDriver = context.getInitParameter("db.driver");

        // Basic validation of parameters
        if (dbDriver == null || dbDriver.trim().isEmpty()) {
            throw new SQLException("Database driver class name ('db.driver') not configured in web.xml");
        }
        if (dbUrl == null || dbUrl.trim().isEmpty()) {
            throw new SQLException("Database URL ('db.url') not configured in web.xml");
        }
        if (dbUser == null) { // User can be empty string? Maybe not. Check requirement.
             System.err.println("WARN: Database user ('db.user') not configured in web.xml or is null.");
             throw new SQLException("Database user ('db.user') not configured in web.xml");
        }
        if (dbPassword == null) {
             System.out.println("INFO: Database password ('db.password') is null. Assuming empty password.");
             dbPassword = ""; // Default to empty if null
        }

        System.out.println("DB Config: Driver=" + dbDriver + ", URL=" + dbUrl + ", User=" + dbUser);

        // Load the JDBC driver class
        if (!driverLoaded) {
            try {
                Class.forName(dbDriver);
                driverLoaded = true;
                System.out.println("JDBC Driver loaded successfully: " + dbDriver);
            } catch (ClassNotFoundException e) {
                System.err.println("FATAL: Could not load JDBC Driver: " + dbDriver);
                throw e; // Re-throw
            }
        }

        // --- Perform Database and Table Setup ---
        // Extract base URL and DB name for creation check
        String dbName = extractDbNameFromUrl(dbUrl);
        String baseUrl = extractBaseUrl(dbUrl);

        if (dbName == null || baseUrl == null) {
             throw new SQLException("Could not parse database name and base URL from db.url: " + dbUrl);
        }

        // Create database if it doesn't exist
        createDatabaseIfNotExist(baseUrl, dbName, dbUser, dbPassword);

        // Create tables if they don't exist (connect to the specific database)
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            createTablesIfNotExist(conn);
        } catch (SQLException e) {
            System.err.println("FATAL: Failed to connect to database '" + dbUrl + "' for table creation check.");
            throw e; // Re-throw
        }

        System.out.println("DBUtil initialization and schema setup complete.");
        setupComplete = true;
    }

    // Helper to extract DB name (simple implementation)
    private static String extractDbNameFromUrl(String url) {
        try {
            int lastSlash = url.lastIndexOf('/');
            int questionMark = url.indexOf('?', lastSlash);
            if (lastSlash != -1 && lastSlash < url.length() - 1) {
                 return (questionMark == -1) ? url.substring(lastSlash + 1) : url.substring(lastSlash + 1, questionMark);
            }
        } catch (Exception e) { /* ignore parsing error */ }
        return null;
    }

    // Helper to extract Base URL (simple implementation)
     private static String extractBaseUrl(String url) {
          try {
             int lastSlash = url.lastIndexOf('/');
             if (lastSlash > 0) {
                  // Check if the character before the last slash is also a slash (like in jdbc:mysql://host/)
                  if (url.charAt(lastSlash - 1) == '/') {
                      return url.substring(0, lastSlash + 1); // Keep trailing slash e.g., jdbc:mysql://localhost:3306/
                  } else {
                      return url.substring(0, lastSlash); // e.g. jdbc:mysql://localhost:3306
                  }
             }
         } catch (Exception e) { /* ignore parsing error */ }
         return null;
     }


    // Method to get a direct connection
    public static Connection getConnection() throws SQLException {
         if (!driverLoaded || !setupComplete) {
             // This shouldn't happen if initialize was called correctly by the listener
             throw new SQLException("DBUtil not initialized. Ensure AppContextListener ran successfully.");
         }
         // Each call gets a new connection. Closing is handled by the calling try-with-resources block.
         return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    // --- Database and Table Creation Logic (using DriverManager) ---

// --- Database and Table Creation Logic (using DriverManager) ---

    private static void createDatabaseIfNotExist(String baseUrl, String dbName, String user, String password) throws SQLException {
         System.out.println("Checking/Creating database: " + dbName);
         // Need to connect to the server itself (base URL) without specifying the database

         // Append sslMode=DISABLED to the base URL for this initial connection check
         String paramToAdd = "sslMode=DISABLED";
         String baseUrlForCheck;
         if (baseUrl.contains("?")) {
             // Base URL already has parameters, append with &
             baseUrlForCheck = baseUrl + "&" + paramToAdd;
         } else {
             // No parameters yet in Base URL, append with ?
             baseUrlForCheck = baseUrl + "?" + paramToAdd;
         }

         System.out.println("Connecting to base URL for DB check: " + baseUrlForCheck);

         // Now use the modified baseUrlForCheck for the initial connection
         try (Connection conn = DriverManager.getConnection(baseUrlForCheck, user, password)) {
              // Check if DB exists using metadata
              boolean dbExists = false;
              try (ResultSet rs = conn.getMetaData().getCatalogs()) {
                  while (rs.next()) {
                      if (rs.getString(1).equalsIgnoreCase(dbName)) {
                          dbExists = true;
                          break;
                      }
                  }
              } // ResultSet rs is closed here

              // If database doesn't exist, create it
              if (!dbExists) {
                  System.out.println("Database " + dbName + " not found. Creating...");
                  try (Statement stmt = conn.createStatement()) {
                      // Use backticks for safety and specify character set/collation
                      stmt.executeUpdate("CREATE DATABASE `" + dbName + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                      System.out.println("Database " + dbName + " created successfully.");
                  } // Statement stmt is closed here
              } else {
                   System.out.println("Database " + dbName + " already exists.");
              }
         } catch (SQLException e) {
             // Provide more context in the error message
             System.err.println("Error during database check/creation for '" + dbName + "' using base URL '" + baseUrlForCheck + "': " + e.getMessage());
             // Log the specific SQLState and ErrorCode which can be very helpful
             System.err.println("SQLState: " + e.getSQLState() + " ErrorCode: " + e.getErrorCode());
             e.printStackTrace(); // Print the full stack trace to the server log
             throw e; // Re-throw the exception so the initialization process fails clearly
         } // Connection conn is closed here
    }
    
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
            System.out.println("Tables checked/created successfully.");
        } catch (SQLException e) {
             System.err.println("Error creating tables: " + e.getMessage());
             throw e;
        }
    }

    // --- Public execute Methods (Unchanged Internally - Use the new getConnection()) ---

    public static <T> List<T> executeQuery(String sql, ResultSetHandler<T> handler, Object... params)
            throws SQLException, IOException {
        List<T> results = new ArrayList<>();
        try (Connection conn = getConnection(); // Gets a new direct connection
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(handler.handle(rs));
                }
            }
        } // conn and pstmt are closed here
        return results;
    }

    public static int executeUpdate(String sql, Object... params) throws SQLException, IOException {
        try (Connection conn = getConnection(); // Gets a new direct connection
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            return pstmt.executeUpdate();
        } // conn and pstmt are closed here
    }

     public static int executeInsertAndGetKey(String sql, Object... params) throws SQLException, IOException {
         int generatedKey = -1;
         try (Connection conn = getConnection(); // Gets a new direct connection
              PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             for (int i = 0; i < params.length; i++) {
                 // Handle potential null values passed for foreign keys etc.
                 if (params[i] == null) {
                      // Infer SQL type or set explicitly if needed, often Types.NULL works
                      pstmt.setNull(i + 1, Types.NULL);
                 } else {
                      pstmt.setObject(i + 1, params[i]);
                 }
             }
             pstmt.executeUpdate();
             try (ResultSet rs = pstmt.getGeneratedKeys()) {
                 if (rs.next()) {
                     generatedKey = rs.getInt(1);
                 } else {
                     System.err.println("WARN: No generated key obtained after insert for SQL (might be expected): " + sql);
                 }
             }
         } // conn and pstmt are closed here
         return generatedKey;
     }

    // --- ResultSetHandler Interface (Unchanged) ---
    @FunctionalInterface
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException, IOException;
    }

    // --- SQL Constants for Table Creation (Unchanged from previous version) ---
    private static final String SQL_CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS Users (" +
            "userId INT AUTO_INCREMENT PRIMARY KEY," +
            "email VARCHAR(255) NOT NULL UNIQUE," +
            // CONFIRM THIS LINE: Should be VARCHAR(255) for plain text
            "password VARCHAR(255) NOT NULL," +
            "username VARCHAR(255) NOT NULL UNIQUE," +
            "walletBalance FLOAT NOT NULL DEFAULT 0.0," +
            "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ") ENGINE=InnoDB;";

     private static final String SQL_CREATE_GAMES_TABLE = "CREATE TABLE IF NOT EXISTS Games (" +
             "gameId INT AUTO_INCREMENT PRIMARY KEY," +
             "title VARCHAR(255) NOT NULL," +
             "description TEXT," +
             "developer VARCHAR(255)," +
             "platform VARCHAR(255)," +
             "price FLOAT NOT NULL," +
             "releaseDate DATE" +
             ") ENGINE=InnoDB;";

     private static final String SQL_CREATE_CARTS_TABLE = "CREATE TABLE IF NOT EXISTS Carts (" +
             "userId INT PRIMARY KEY," +
             "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
             "FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE" +
             ") ENGINE=InnoDB;";

     private static final String SQL_CREATE_CART_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS CartItems (" +
             "cartItemId INT AUTO_INCREMENT PRIMARY KEY," +
             "userId INT NOT NULL," +
             "gameId INT NOT NULL," +
             "addedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
             "FOREIGN KEY (userId) REFERENCES Carts(userId) ON DELETE CASCADE," +
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
             // Changed column name for clarity
             "priceAtPurchase FLOAT NOT NULL," + 
             "FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE," +
             "FOREIGN KEY (gameId) REFERENCES Games(gameId) ON DELETE RESTRICT" +
             ") ENGINE=InnoDB;";

     private static final String SQL_CREATE_TRANSACTIONS_TABLE = "CREATE TABLE IF NOT EXISTS Transactions (" +
             "transactionId INT AUTO_INCREMENT PRIMARY KEY," +
             "orderId INT NULL," + // Allow NULL
             "userId INT NOT NULL," +
             "transactionType VARCHAR(50) NOT NULL," +
             "amount FLOAT NOT NULL," +
             "transactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
             "FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE RESTRICT," +
             "FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE SET NULL" + // Set orderId to NULL if order deleted
             ") ENGINE=InnoDB;";
}