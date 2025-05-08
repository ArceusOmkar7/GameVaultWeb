package gamevaultbase.listeners;

import gamevaultbase.entities.Game;
import gamevaultbase.helpers.DBUtil;
import gamevaultbase.helpers.JSONUtil;
import gamevaultbase.management.*;
import gamevaultbase.storage.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final String JSON_FILE_PATH = "/WEB-INF/games.json";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("GameVaultWebApp initializing (Direct JDBC)...");
        ServletContext context = sce.getServletContext();

        try {
            // Initialize DBUtil: Loads driver, credentials, checks/creates schema
            DBUtil.initialize(context);

            // --- Initialize Storages with proper dependencies ---
            // Create storage instances
            UserStorage userStorage = new UserStorage();
            GameStorage gameStorage = new GameStorage();
            CartStorage cartStorage = new CartStorage();
            OrderStorage orderStorage = new OrderStorage();
            TransactionStorage transactionStorage = new TransactionStorage();
            ReviewStorage reviewStorage = new ReviewStorage();

            // Set dependencies - ensure all necessary dependencies are properly set
            cartStorage.setGameStorage(gameStorage);
            orderStorage.setGameStorage(gameStorage);

            // --- Initialize Managements with the storage instances ---
            UserManagement userManagement = new UserManagement(userStorage);
            GameManagement gameManagement = new GameManagement(gameStorage);
            CartManagement cartManagement = new CartManagement(cartStorage, gameStorage);
            TransactionManagement transactionManagement = new TransactionManagement(transactionStorage);
            OrderManagement orderManagement = new OrderManagement(orderStorage, cartStorage, userStorage,
                    transactionManagement);
            ReviewManagement reviewManagement = new ReviewManagement(reviewStorage);

            // Create the main facade (Optional)
            GameVaultManagement vaultManager = new GameVaultManagement(userManagement, gameManagement, orderManagement,
                    transactionManagement);

            // --- Store management instances in ServletContext ---
            context.setAttribute("userManagement", userManagement);
            context.setAttribute("gameManagement", gameManagement);
            context.setAttribute("cartManagement", cartManagement);
            context.setAttribute("orderManagement", orderManagement);
            context.setAttribute("transactionManagement", transactionManagement);
            context.setAttribute("reviewManagement", reviewManagement);
            context.setAttribute("vaultManager", vaultManager);

            // --- Initialize Data ---
            // Initialize sample users (from the original code)
            initializeSampleUsers(userManagement);

            // Initialize games from JSON if the database is empty
            initializeGamesFromJson(gameStorage, context);

            System.out.println("GameVaultWebApp initialization complete (Direct JDBC).");

        } catch (SQLException e) {
            System.err.println("FATAL: SQL error during DBUtil initialization or schema setup!");
            e.printStackTrace();
            throw new RuntimeException("Application failed to initialize - Database Error", e);
        } catch (Exception e) {
            // Catch any other unexpected errors during manager setup etc.
            System.err.println("FATAL: Unexpected error during application initialization!");
            e.printStackTrace();
            throw new RuntimeException("Application failed to initialize", e);
        }
    }

    // --- Sample Users Initialization ---
    private void initializeSampleUsers(UserManagement userManagement) {
        System.out.println("Attempting to initialize sample users...");
        // Check if users exist (basic check)
        boolean usersExist = false;
        try {
            usersExist = userManagement.getAllUsers() != null && !userManagement.getAllUsers().isEmpty();
        } catch (Exception e) {
            System.err.println("WARN: Could not check existing users before adding samples: " + e.getMessage());
            // Proceed cautiously
        }

        if (!usersExist) {
            System.out.println("Adding sample users...");
            try {
                userManagement.addUser(
                        new gamevaultbase.entities.User("sasuke@example.com", "plainpassword1", "Sasuke", 50.0f));
                userManagement.addUser(
                        new gamevaultbase.entities.User("naruto@example.com", "plainpassword2", "Naruto", 100.0f));
                userManagement.addUser(
                        new gamevaultbase.entities.User("user@example.com", "password", "DefaultUser", 200.0f));
                userManagement.addUser(
                        new gamevaultbase.entities.User("admin@gamevault.com", "admin", "Admin", 1000.0f, true)); // Add
                                                                                                                  // admin
                                                                                                                  // user
                System.out.println("Sample users added.");
            } catch (Exception e) {
                System.err.println("ERROR adding sample users: " + e.getMessage());
            }
        } else {
            System.out.println("Sample users seem to exist already. Skipping user creation.");
        }
    }

    // --- Games Initialization from JSON ---
    private void initializeGamesFromJson(GameStorage gameStorage, ServletContext context) {
        System.out.println("Checking if games data needs to be loaded from JSON...");

        try {
            // Check if games exist in the database
            String sqlCountGames = "SELECT COUNT(*) AS gameCount FROM Games";
            List<Integer> results = DBUtil.executeQuery(sqlCountGames, rs -> rs.getInt("gameCount"));
            int existingGameCount = results.isEmpty() ? 0 : results.get(0);

            if (existingGameCount == 0) {
                System.out.println("No games found in the database. Loading from JSON file...");

                // Load the JSON file
                InputStream inputStream = context.getResourceAsStream(JSON_FILE_PATH);

                if (inputStream == null) {
                    System.err.println("WARNING: Could not find JSON file: " + JSON_FILE_PATH);
                    return;
                }

                try {
                    // Parse the JSON data using the updated JSONUtil
                    List<Game> games = JSONUtil.parseGamesFromJson(inputStream);
                    System.out.println("Parsed " + games.size() + " games from JSON");

                    // Save the games to the database
                    int successCount = 0;
                    for (Game game : games) {
                        try {
                            // Verify platform data is properly formatted
                            if (game.getPlatform() != null) {
                                System.out.println(
                                        "Adding game: " + game.getTitle() + " with platforms: " + game.getPlatform());
                            }

                            gameStorage.save(game);
                            successCount++;

                            if (successCount % 10 == 0) {
                                System.out.println("Progress: Added " + successCount + " games so far...");
                            }
                        } catch (Exception e) {
                            System.err.println("Error saving game '" + game.getTitle() + "': " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    System.out.println(
                            "Successfully loaded " + successCount + " of " + games.size() + " games from JSON file");
                } catch (Exception e) {
                    System.err.println("ERROR parsing games from JSON: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Found " + existingGameCount + " games in database. Skipping JSON import.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR checking database or loading games from JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected ERROR during game initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("GameVaultWebApp destroying (Direct JDBC)...");
        // No specific JDBC cleanup needed here as connections are opened/closed per
        // request cycle.
        // Just clear context attributes
        ServletContext context = sce.getServletContext();
        context.removeAttribute("userManagement");
        context.removeAttribute("gameManagement");
        context.removeAttribute("cartManagement");
        context.removeAttribute("orderManagement");
        context.removeAttribute("transactionManagement");
        context.removeAttribute("reviewManagement"); // Add this line to remove reviewManagement attribute
        context.removeAttribute("vaultManager");
        System.out.println("GameVaultWebApp destroyed.");
        // Deregister driver? Usually not necessary in modern containers, but can be
        // done:
        // try {
        // java.sql.Driver d = DriverManager.getDriver(dbUrl); // dbUrl needs to be
        // accessible
        // DriverManager.deregisterDriver(d);
        // } catch (SQLException e) {
        // System.err.println("Error deregistering JDBC driver: " + e.getMessage());
        // }
    }
}