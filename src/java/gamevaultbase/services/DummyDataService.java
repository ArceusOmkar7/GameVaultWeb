package gamevaultbase.services;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Order;
import gamevaultbase.entities.User;
import gamevaultbase.helpers.DBUtil;
import gamevaultbase.helpers.JSONUtil;
import gamevaultbase.storage.GameStorage;
import gamevaultbase.storage.OrderStorage;
import gamevaultbase.storage.UserStorage;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class that handles generation of dummy data for the GameVault
 * application.
 * It loads games from JSON and creates users and orders with realistic
 * information.
 */
public class DummyDataService {
    private static final Logger LOGGER = Logger.getLogger(DummyDataService.class.getName());
    private final Random random = new Random();
    private final ServletContext servletContext;

    public DummyDataService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Loads games from a JSON file instead of using hardcoded values.
     * Uses the existing JSONUtil to parse the games.
     */
    public List<Game> generateGames(GameStorage gameStorage, int count, List<String> logs) {
        List<Game> generatedGames = new ArrayList<>();

        // Check existing game count to avoid duplicates
        try {
            String sqlCountGames = "SELECT COUNT(*) AS gameCount FROM Games";
            List<Integer> results = DBUtil.executeQuery(sqlCountGames, rs -> rs.getInt("gameCount"));
            int existingGameCount = results.isEmpty() ? 0 : results.get(0);

            if (existingGameCount > 0) {
                // Get existing games
                String sqlGetGames = "SELECT * FROM Games";
                generatedGames = DBUtil.executeQuery(sqlGetGames, rs -> {
                    Game game = new Game();
                    game.setGameId(rs.getInt("gameId"));
                    game.setTitle(rs.getString("title"));
                    game.setDescription(rs.getString("description"));
                    game.setDeveloper(rs.getString("developer"));
                    game.setPlatform(rs.getString("platform"));
                    game.setPrice(rs.getFloat("price"));
                    game.setReleaseDate(rs.getDate("releaseDate"));
                    game.setImagePath(rs.getString("imagePath"));
                    game.setGenre(rs.getString("genre"));
                    return game;
                });

                if (generatedGames.size() >= count) {
                    logs.add("Using " + generatedGames.size() + " existing games");
                    return generatedGames;
                }

                logs.add("Adding " + (count - generatedGames.size()) + " more games to reach target of " + count);
                count = count - generatedGames.size();
            }

            // Load games from JSON file
            logs.add("Loading games from JSON file");
            try (InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/games.json")) {
                if (inputStream != null) {
                    List<Game> jsonGames = JSONUtil.parseGamesFromJson(inputStream);

                    // Only add as many games as needed to reach the count
                    int gamesToAdd = Math.min(jsonGames.size(), count);
                    logs.add("Found " + jsonGames.size() + " games in JSON, will add " + gamesToAdd);

                    for (int i = 0; i < gamesToAdd; i++) {
                        Game game = jsonGames.get(i);
                        gameStorage.save(game);
                        generatedGames.add(game);
                        logs.add("Added game: " + game.getTitle() + " (" + game.getPlatform() + ") - $"
                                + game.getPrice());
                    }
                } else {
                    logs.add("ERROR: Could not find games.json file");
                }
            } catch (Exception e) {
                logs.add("Error loading games from JSON: " + e.getMessage());
            }

        } catch (Exception e) {
            logs.add("Error working with games: " + e.getMessage());
        }

        return generatedGames;
    }

    /**
     * Generates users if needed and returns a list of user IDs.
     */
    public List<Integer> generateUsers(int count, List<String> logs) throws SQLException, IOException {
        List<Integer> userIds = new ArrayList<>();

        // Check existing users
        String sqlCountUsers = "SELECT COUNT(*) AS userCount FROM Users";
        List<Integer> results = DBUtil.executeQuery(sqlCountUsers, rs -> rs.getInt("userCount"));
        int existingUserCount = results.isEmpty() ? 0 : results.get(0);

        if (existingUserCount > 0) {
            logs.add("Found " + existingUserCount + " existing users");

            // Get existing regular user IDs
            String sqlGetUsers = "SELECT userId FROM Users WHERE isAdmin = false";
            userIds = DBUtil.executeQuery(sqlGetUsers, rs -> rs.getInt("userId"));

            if (userIds.size() < 2) {
                // Create at least 2 users if fewer exist
                createBasicUsers(logs);

                // Reload user IDs
                userIds = DBUtil.executeQuery(sqlGetUsers, rs -> rs.getInt("userId"));
            }
        } else {
            // Create admin and basic users
            createBasicUsers(logs);

            // Create additional dummy users
            for (int i = 0; i < count; i++) {
                try {
                    String username = "User" + (i + 1);
                    String email = username.toLowerCase() + "@example.com";
                    float walletBalance = 1000.0f + random.nextInt(9000);

                    String sqlInsertUser = "INSERT INTO Users (email, password, username, walletBalance, isAdmin) VALUES (?, ?, ?, ?, ?)";
                    int userId = DBUtil.executeInsertAndGetKey(sqlInsertUser,
                            email,
                            "password123",
                            username,
                            walletBalance,
                            false);

                    userIds.add(userId);
                    logs.add("Created user: " + username + " with $" + walletBalance + " wallet balance");
                } catch (Exception e) {
                    logs.add("Error creating user #" + (i + 1) + ": " + e.getMessage());
                }
            }
        }

        return userIds;
    }

    /**
     * Creates the basic admin and user accounts.
     */
    private void createBasicUsers(List<String> logs) throws SQLException, IOException {
        try {
            // Create admin user
            String sqlInsertAdmin = "INSERT INTO Users (email, password, username, walletBalance, isAdmin) VALUES (?, ?, ?, ?, ?)";
            int adminId = DBUtil.executeInsertAndGetKey(sqlInsertAdmin,
                    "admin@gamevault.com",
                    "admin123",
                    "Administrator",
                    1000.0f,
                    true);
            logs.add("Created admin user: admin@gamevault.com / admin123 (ID: " + adminId + ")");

            // Create regular user with high wallet balance for testing
            String sqlInsertUser = "INSERT INTO Users (email, password, username, walletBalance, isAdmin) VALUES (?, ?, ?, ?, ?)";
            int userId = DBUtil.executeInsertAndGetKey(sqlInsertUser,
                    "user@gamevault.com",
                    "user123",
                    "TestUser",
                    5000.0f,
                    false);
            logs.add("Created regular user: user@gamevault.com / user123 (ID: " + userId + ")");
        } catch (Exception e) {
            // If the users already exist, this will fail with a duplicate key error
            logs.add("Note: Basic users may already exist. Error: " + e.getMessage());

            // Try getting the existing user IDs instead
            String sqlGetAdmin = "SELECT userId FROM Users WHERE email = 'admin@gamevault.com'";
            List<Integer> adminIds = DBUtil.executeQuery(sqlGetAdmin, rs -> rs.getInt("userId"));

            String sqlGetUser = "SELECT userId FROM Users WHERE email = 'user@gamevault.com'";
            List<Integer> userIds = DBUtil.executeQuery(sqlGetUser, rs -> rs.getInt("userId"));

            if (!adminIds.isEmpty()) {
                logs.add("Found existing admin user (ID: " + adminIds.get(0) + ")");
            }

            if (!userIds.isEmpty()) {
                logs.add("Found existing regular user (ID: " + userIds.get(0) + ")");
            }
        }
    }

    /**
     * Creates orders using the generated games.
     */
    public void generateOrders(List<Game> games, List<Integer> userIds, int count, List<String> logs)
            throws SQLException, IOException {

        if (games == null || games.isEmpty()) {
            logs.add("ERROR: No games available to create orders - games list is null or empty");
            return;
        }

        if (userIds == null || userIds.isEmpty()) {
            logs.add("ERROR: No regular users found to create orders - userIds list is null or empty");
            return;
        }

        // Check existing orders
        try {
            String sqlCountOrders = "SELECT COUNT(*) AS orderCount FROM Orders";
            List<Integer> results = DBUtil.executeQuery(sqlCountOrders, rs -> rs.getInt("orderCount"));
            int existingOrderCount = results.isEmpty() ? 0 : results.get(0);

            if (existingOrderCount > 0) {
                logs.add("Found " + existingOrderCount + " existing orders - will generate " +
                        (count - existingOrderCount) + " more");

                if (existingOrderCount >= count) {
                    logs.add("Already have enough orders - skipping order generation");
                    return;
                }

                // Adjust count to create only what's needed
                count = count - existingOrderCount;
            }
        } catch (Exception e) {
            logs.add("Warning: Unable to check existing orders. Will proceed with generating " + count
                    + " orders. Error: " + e.getMessage());
        }

        // Create an OrderStorage instance
        logs.add("Creating OrderStorage instance");
        OrderStorage orderStorage = null;
        try {
            // Create GameStorage first to properly initialize OrderStorage
            GameStorage gameStorageInstance = new GameStorage();
            orderStorage = new OrderStorage(gameStorageInstance);
        } catch (Exception e) {
            logs.add("ERROR: Failed to create OrderStorage: " + e.getMessage());
            return;
        }

        if (orderStorage == null) {
            logs.add("ERROR: OrderStorage is null");
            return;
        }

        // Create a distribution of orders through time for better analytics
        logs.add("Distributing orders across different time periods for better analytics");

        // Will create 20% of orders in the last week, 30% in the last month,
        // and the rest distributed over the past year
        int recentOrders = Math.max(1, (int) (count * 0.2)); // Last week
        int monthOrders = Math.max(1, (int) (count * 0.3)); // Last month
        int olderOrders = Math.max(1, count - recentOrders - monthOrders); // Rest of the year

        logs.add("Creating " + recentOrders + " orders from last week");
        createOrdersForTimePeriod(games, userIds, recentOrders, 7, orderStorage, logs);

        logs.add("Creating " + monthOrders + " orders from last month");
        createOrdersForTimePeriod(games, userIds, monthOrders, 30, orderStorage, logs);

        logs.add("Creating " + olderOrders + " orders from past year");
        createOrdersForTimePeriod(games, userIds, olderOrders, 365, orderStorage, logs);
    }

    /**
     * Creates orders for a specific time period.
     */
    private void createOrdersForTimePeriod(List<Game> games, List<Integer> userIds, int count,
            int daysPast, OrderStorage orderStorage, List<String> logs)
            throws SQLException, IOException {

        logs.add("Starting order creation for " + count + " orders in " + daysPast + " day period");

        for (int i = 0; i < count; i++) {
            try {
                logs.add("Creating order #" + (i + 1));

                // Validate inputs before proceeding
                if (userIds == null || userIds.isEmpty()) {
                    logs.add("ERROR: userIds list is null or empty");
                    return;
                }

                if (games == null || games.isEmpty()) {
                    logs.add("ERROR: games list is null or empty");
                    return;
                }

                // Select a random user
                int userId = userIds.get(random.nextInt(userIds.size()));
                logs.add("Selected user ID: " + userId);

                // Select random number of games (1-3)
                int gameCount = 1 + random.nextInt(3);
                logs.add("Selecting " + gameCount + " games for order");

                List<Game> selectedGames = selectRandomGames(games, gameCount);
                logs.add("Selected " + selectedGames.size() + " games for order");

                float totalAmount = 0;
                for (Game game : selectedGames) {
                    if (game != null) {
                        totalAmount += game.getPrice();
                    } else {
                        logs.add("Warning: Found null game in selected games");
                    }
                }

                // Create order with randomized date
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -random.nextInt(daysPast));

                Order order = new Order();
                order.setUserId(userId);
                order.setTotalAmount(totalAmount);
                order.setOrderDate(calendar.getTime());
                // Set status to COMPLETED for all dummy orders
                try {
                    order.setStatus("COMPLETED");
                } catch (Exception e) {
                }

                logs.add("Saving order to database");
                orderStorage.save(order);

                // Add order items (games) to the order
                int orderId = order.getOrderId();
                for (Game game : selectedGames) {
                    if (game != null) {
                        String sqlInsertOrderItem = "INSERT INTO OrderItems (orderId, gameId, priceAtPurchase) VALUES (?, ?, ?)";
                        DBUtil.executeUpdate(sqlInsertOrderItem, orderId, game.getGameId(), game.getPrice());
                    }
                }

                logs.add("Successfully created order #" + orderId + " with " + selectedGames.size() + " games");
            } catch (Exception e) {
                logs.add("ERROR creating order: " + e.getMessage());
            }
        }
    }

    /**
     * Selects a random subset of games from the available list.
     */
    private List<Game> selectRandomGames(List<Game> availableGames, int count) {
        List<Game> result = new ArrayList<>();
        List<Game> gamesCopy = new ArrayList<>(availableGames);

        // Limit count to available games
        count = Math.min(count, gamesCopy.size());

        for (int i = 0; i < count; i++) {
            if (gamesCopy.isEmpty()) {
                break;
            }
            int randomIndex = random.nextInt(gamesCopy.size());
            result.add(gamesCopy.remove(randomIndex));
        }

        return result;
    }
}