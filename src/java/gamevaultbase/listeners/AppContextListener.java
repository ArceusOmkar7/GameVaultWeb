package gamevaultbase.listeners;

import gamevaultbase.helpers.DBUtil;
import gamevaultbase.management.*;
import gamevaultbase.storage.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class AppContextListener implements ServletContextListener {

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

            // Note: Games are now loaded from JSON using the admin interface
            // at /admin/load-json-data instead of using DbInitializer

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