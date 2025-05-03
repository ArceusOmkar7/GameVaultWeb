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

            // --- Initialize Storages (No changes needed here) ---
            UserStorage userStorage = new UserStorage();
            GameStorage gameStorage = new GameStorage();
            CartStorage cartStorage = new CartStorage(gameStorage);
            OrderStorage orderStorage = new OrderStorage();
            TransactionStorage transactionStorage = new TransactionStorage();
            ReviewStorage reviewStorage = new ReviewStorage(); // Add ReviewStorage

            // --- Initialize Managements (No changes needed here) ---
            UserManagement userManagement = new UserManagement(userStorage);
            GameManagement gameManagement = new GameManagement(gameStorage);
            CartManagement cartManagement = new CartManagement(cartStorage, gameStorage);
            TransactionManagement transactionManagement = new TransactionManagement(transactionStorage);
            OrderManagement orderManagement = new OrderManagement(orderStorage, cartStorage, userStorage, transactionManagement);
            ReviewManagement reviewManagement = new ReviewManagement(reviewStorage); // Add ReviewManagement

            // Create the main facade (Optional)
            GameVaultManagement vaultManager = new GameVaultManagement(userManagement, gameManagement, orderManagement, transactionManagement);

            // --- Store management instances in ServletContext (No changes needed here) ---
            context.setAttribute("userManagement", userManagement);
            context.setAttribute("gameManagement", gameManagement);
            context.setAttribute("cartManagement", cartManagement);
            context.setAttribute("orderManagement", orderManagement);
            context.setAttribute("transactionManagement", transactionManagement);
            context.setAttribute("reviewManagement", reviewManagement); // Add ReviewManagement to context
            context.setAttribute("vaultManager", vaultManager);

            // --- Initialize Sample Data (Optional - runs after DB setup) ---
            // WARNING: This will fail if data already exists (due to UNIQUE constraints)
            initializeSampleData(userManagement, gameManagement);

            System.out.println("GameVaultWebApp initialization complete (Direct JDBC).");

        } catch (ClassNotFoundException e) {
             System.err.println("FATAL: JDBC Driver class not found during initialization!");
             e.printStackTrace();
             throw new RuntimeException("Application failed to initialize - JDBC Driver Missing", e);
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

     // --- Sample Data Initialization (Same as before, but runs after DBUtil.initialize) ---
     private void initializeSampleData(UserManagement userManagement, GameManagement gameManagement) {
         System.out.println("Attempting to initialize sample data...");
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
                 userManagement.addUser(new gamevaultbase.entities.User("sasuke@example.com", "plainpassword1", "Sasuke", 50.0f));
                 userManagement.addUser(new gamevaultbase.entities.User("naruto@example.com", "plainpassword2", "Naruto", 100.0f));
                 userManagement.addUser(new gamevaultbase.entities.User("user@example.com", "password", "DefaultUser", 200.0f));
                 System.out.println("Sample users added.");
             } catch (Exception e) {
                 System.err.println("ERROR adding sample users: " + e.getMessage());
             }
         } else {
              System.out.println("Sample users seem to exist already. Skipping user creation.");
         }

         // Check if games exist (basic check)
         boolean gamesExist = false;
         try {
              gamesExist = gameManagement.getAllGames() != null && !gameManagement.getAllGames().isEmpty();
         } catch (Exception e) {
              System.err.println("WARN: Could not check existing games before adding samples: " + e.getMessage());
              // Proceed cautiously
         }

         if (!gamesExist) {
             System.out.println("Adding sample games...");
             try {
                 gameManagement.addGame(new gamevaultbase.entities.Game("Spider-Man Remastered", "Swing through NYC.", "Insomniac", "PC", 52.3f, new java.util.Date()));
                 gameManagement.addGame(new gamevaultbase.entities.Game("God of War", "Dad simulator.", "Santa Monica", "PS4", 49.99f, new java.util.Date()));
                 gameManagement.addGame(new gamevaultbase.entities.Game("The Last of Us Part II", "Emotional rollercoaster.", "Naughty Dog", "PS4", 59.99f, new java.util.Date()));
                 gameManagement.addGame(new gamevaultbase.entities.Game("Cyberpunk 2077", "Futuristic adventure.", "CDPR", "PC", 39.99f, new java.util.Date()));
                 System.out.println("Sample games added.");
              } catch (Exception e) {
                  System.err.println("ERROR adding sample games: " + e.getMessage());
              }
         } else {
              System.out.println("Sample games seem to exist already. Skipping game creation.");
         }
          System.out.println("Sample data initialization attempt finished.");
     }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("GameVaultWebApp destroying (Direct JDBC)...");
        // No specific JDBC cleanup needed here as connections are opened/closed per request cycle.
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
        // Deregister driver? Usually not necessary in modern containers, but can be done:
        // try {
        //     java.sql.Driver d = DriverManager.getDriver(dbUrl); // dbUrl needs to be accessible
        //     DriverManager.deregisterDriver(d);
        // } catch (SQLException e) {
        //     System.err.println("Error deregistering JDBC driver: " + e.getMessage());
        // }
    }
}