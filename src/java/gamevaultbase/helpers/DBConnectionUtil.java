package gamevaultbase.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to manage database connections using simple JDBC
 */
public class DBConnectionUtil {

    // JDBC database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_NAME = "gamevault";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "1234";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            // Load the JDBC driver
            Class.forName(JDBC_DRIVER);
            System.out.println("JDBC Driver loaded successfully");
            // Create the database if it doesn't exist
            createDatabaseIfNotExists();
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading JDBC driver: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error creating database: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error in DBConnectionUtil initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create the database if it doesn't exist
     */
    private static void createDatabaseIfNotExists() throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        try {
            System.out.println("Attempting to connect to MySQL server...");
            // Connect to MySQL server without specifying a database
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            System.out.println("Connected to MySQL server successfully");

            stmt = conn.createStatement();

            // Create the database if it doesn't exist
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);

            System.out.println("Database '" + DB_NAME + "' created or already exists.");
        } catch (SQLException e) {
            System.err.println("Failed to create database: " + e.getMessage());
            throw e;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Get a connection to the database
     * 
     * @return the database connection
     * @throws SQLException if a database error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            // First ensure database exists
            createDatabaseIfNotExists();

            // Connect directly to the gamevault database
            String url = "jdbc:mysql://localhost:3306/" + DB_NAME
                    + "?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";

            System.out.println("Connecting to database with URL: " + url);
            System.out.println("Using credentials - User: " + JDBC_USER + ", Password: [HIDDEN]");

            Connection conn = DriverManager.getConnection(url, JDBC_USER, JDBC_PASSWORD);
            System.out.println("Connected to database '" + DB_NAME + "' successfully");

            // Test the connection
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT 1");
                if (!rs.next()) {
                    throw new SQLException("Failed to validate database connection");
                }
                System.out.println("Database connection test query succeeded");
            }

            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database '" + DB_NAME + "': " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Close a database connection
     * 
     * @param connection the connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}