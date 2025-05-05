package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Order;
import gamevaultbase.entities.User;
import gamevaultbase.storage.GameStorage;
import gamevaultbase.storage.OrderStorage;
import gamevaultbase.storage.UserStorage;
import gamevaultbase.helpers.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * This servlet generates dummy data for the GameVault application.
 * It creates games, users and orders with realistic information.
 * It uses a configuration file to ensure data is only loaded once.
 * Access at /admin/generate-dummy-data
 */
@WebServlet(name = "DummyDataServlet", urlPatterns = { "/admin/generate-dummy-data" })
public class DummyDataServlet extends HttpServlet {

    private final Random random = new Random();
    private static final String CONFIG_FILE_PATH = "/WEB-INF/dummy_data_config.txt";

    // Lists of dummy data for game generation
    private final String[] gameTitles = {
            "Eternal Shadows", "Cyber Nexus", "Dragon's Legacy", "Stellar Odyssey", "Phantom Warriors",
            "Ancient Realms", "Mystic Quest", "Frozen Kingdoms", "Galactic Frontiers", "Neon Wasteland",
            "Arcane Legends", "Chaos Protocol", "Titan's Fall", "Midnight Eclipse", "Crimson Dynasty",
            "Savage Horizon", "Lunar Chronicles", "Astral Dominion", "Crystal Conquest", "Frostbite",
            "Warped Reality", "Enchanted Realms", "Shadow's Edge", "Celestial Warfare", "Quantum Breach",
            "Nova Strike", "Emerald Empire", "Fallen Angels", "Chronos Gate", "Void Wanderers",
            "Inferno Rising", "Obsidian Legacy", "Spirit Walker", "Elysian Fields", "Nebula Command",
            "Tempest's Fury", "Arcadia Lost", "Solar Storms", "Vengeance Protocol", "Mythic Kingdoms",
            "Bloodlines", "Warp Drive", "Thunderstrike", "Digital Dreams", "Ghost Protocol",
            "Iron Legion", "Radiant Dawn", "Cursed Lands", "Relic Hunters", "Dystopian Future"
    };

    private final String[] gameDescriptions = {
            "A sprawling open-world RPG where players navigate through a post-apocalyptic landscape filled with mutated creatures and remnants of a once-great civilization.",
            "An intense first-person shooter set in a cyberpunk future where corporations wage covert wars using augmented mercenaries.",
            "A strategic turn-based game where players command armies of mythical creatures in an epic battle for control of ancient magical artifacts.",
            "A space exploration adventure where players discover new planets, encounter alien species, and unravel the mysteries of a dying universe.",
            "A survival horror experience set in an abandoned space station overrun by parasitic aliens that transform their hosts.",
            "A puzzle-platformer with beautiful hand-drawn art that follows a child's journey through a fantastical dreamscape.",
            "A roguelike dungeon crawler featuring procedurally generated levels and permanent death mechanics.",
            "A narrative-driven adventure game exploring themes of loss, memory, and redemption in a forgotten civilization.",
            "A high-octane racing game featuring gravity-defying tracks and customizable vehicles from the future.",
            "A stealth action game where players control a master assassin with supernatural abilities in a world of intrigue.",
            "A sandbox building game where players can create their own worlds and share them with a global community.",
            "A competitive multiplayer arena fighter featuring hero characters with unique abilities and playstyles.",
            "A story-rich visual novel with branching narratives that change based on player choices and relationships.",
            "A rhythm action game that synchronizes combat mechanics with an adaptive musical score.",
            "A historical strategy game where players guide a dynasty through centuries of political intrigue and warfare.",
            "A farming and life simulation game set in a small fantasy village populated by magical creatures.",
            "A tactical squad-based shooter requiring precise teamwork and communication to complete objectives.",
            "A survival crafting game set in a procedurally generated wilderness filled with dangerous wildlife.",
            "A metroidvania-style action-adventure featuring tight platforming and a complex upgrade system.",
            "An MMO where thousands of players occupy a persistent fantasy world with a player-driven economy."
    };

    private final String[] developers = {
            "Quantum Studios", "Eclipse Games", "Nebula Interactive", "Vertex Development", "Fusion Entertainment",
            "Prism Games", "Horizon Software", "Luminous Productions", "Stellar Technologies", "Chronos Interactive",
            "Apex Digital", "Radiant Studios", "Obsidian Entertainment", "Raven Software", "Crystal Dynamics",
            "Neon Gaming", "Astral Works", "Celestial Studios", "Enigma Games", "Phantom Studios",
            "Ember Interactive", "Catalyst Games", "Specter Software", "Nova Entertainment", "Arcane Studios"
    };

    private final String[] platforms = {
            "PC", "PlayStation 5", "Xbox Series X", "Nintendo Switch", "PC/PlayStation 5",
            "PC/Xbox Series X", "PlayStation 5/Xbox Series X", "All Platforms", "PC/Nintendo Switch",
            "Mobile/PC", "PlayStation 5/Nintendo Switch", "Xbox Series X/Nintendo Switch"
    };

    private final String[] genres = {
            "Action", "Adventure", "RPG", "Strategy", "Simulation",
            "Sports", "Racing", "Puzzle", "Horror", "Platformer",
            "FPS", "MMORPG", "Survival", "Open World", "Fighting",
            "Stealth", "Battle Royale", "MOBA", "Roguelike", "Visual Novel"
    };

    private final String[] usernames = {
            "GamerPro", "DigitalNinja", "PixelWarrior", "LevelMaster", "GameWizard",
            "QuestHunter", "VirtualHero", "CyberGamer", "GameSage", "ControlFreak",
            "ArcadeLegend", "PowerPlayer", "EliteGamer", "ConsoleKing", "GameMaster",
            "BattleChamp", "TacticalGamer", "EpicPlayer", "RetroGamer", "GameAddict"
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check if user is logged in and is an admin
        if (session.getAttribute("loggedInUser") == null ||
                !(boolean) session.getAttribute("isAdmin")) {
            response.sendRedirect(request.getContextPath() + "/login?message=Admin access required&messageType=error");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // HTML header
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Generate Dummy Data</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px; }");
        out.println("h1 { color: #333; }");
        out.println(".success { color: green; }");
        out.println(".warning { color: orange; }");
        out.println(".error { color: red; }");
        out.println("ul { margin-top: 20px; }");
        out.println("li { margin-bottom: 10px; }");
        out.println("a { display: inline-block; margin-top: 20px; color: blue; text-decoration: none; }");
        out.println("a:hover { text-decoration: underline; }");
        out.println(
                ".button { background-color: #4CAF50; border: none; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer; border-radius: 5px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Dummy Data Generator</h1>");

        try {
            // Load configuration
            Properties config = loadConfiguration();
            List<String> logs = new ArrayList<>();
            logs.add("Starting dummy data generation process");

            // Check if data has already been loaded
            boolean dataLoaded = Boolean.parseBoolean(config.getProperty("dummy_data.loaded", "false"));
            String lastLoadDate = config.getProperty("last_load_date", "");

            // Handle force reload parameter
            boolean forceReload = "true".equals(request.getParameter("force"));

            if (dataLoaded && !forceReload) {
                // Data has already been loaded, show info message
                out.println("<p class='warning'>Dummy data has already been generated on " + lastLoadDate + "</p>");
                out.println("<p>If you want to generate more data, click the button below:</p>");
                out.println("<a href='" + request.getContextPath() +
                        "/admin/generate-dummy-data?force=true' class='button'>Force Regenerate Data</a>");

                // Add a link to check database structure
                out.println(
                        "<p class='info' style='margin-top: 20px;'>If you're experiencing errors, you can check your database structure:</p>");
                out.println("<a href='" + request.getContextPath() +
                        "/admin/check-database' class='button' style='background-color: #2196F3;'>Check Database Structure</a>");
            } else {
                // Generate new data
                int gamesCount = Integer.parseInt(config.getProperty("games.count", "50"));
                int ordersCount = Integer.parseInt(config.getProperty("orders.count", "50"));
                int usersCount = Integer.parseInt(config.getProperty("users.count", "10"));

                logs.add("Configured to generate: " + gamesCount + " games, " + ordersCount + " orders, " + usersCount
                        + " users");

                try {
                    // Create storage objects
                    logs.add("Creating GameStorage");
                    GameStorage gameStorage = new GameStorage();

                    // Create users if needed
                    logs.add("Generating users");
                    List<Integer> userIds = generateUsers(usersCount, logs);
                    logs.add("Generated " + userIds.size() + " user IDs");

                    // Generate games
                    logs.add("Generating games");
                    List<Game> generatedGames = generateGames(gameStorage, gamesCount, logs);
                    logs.add("Generated " + generatedGames.size() + " games");

                    // Generate orders using the created games
                    logs.add("Generating orders");
                    generateOrders(generatedGames, userIds, ordersCount, logs);

                    // Update configuration
                    logs.add("Updating configuration");
                    updateConfiguration(config);

                    // Output success message and logs
                    out.println("<p class='success'>Successfully generated dummy data!</p>");
                } catch (Exception e) {
                    // Capture detailed error information
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stackTrace = sw.toString();

                    logs.add("ERROR: " + e.getClass().getName() + ": " + e.getMessage());
                    logs.add("Stack trace: " + stackTrace.replace("\n", "<br>"));

                    out.println("<p class='error'>Error occurred during data generation: " + e.getMessage() + "</p>");
                    out.println(
                            "<pre class='error' style='background-color: #fff0f0; padding: 10px; border: 1px solid #ffcccc; overflow: auto; max-height: 300px;'>");
                    out.println(stackTrace.replace("<", "&lt;").replace(">", "&gt;"));
                    out.println("</pre>");

                    // Add a link to check database structure
                    out.println("<p>Please check your database structure:</p>");
                    out.println("<a href='" + request.getContextPath() +
                            "/admin/check-database' class='button' style='background-color: #2196F3;'>Check Database Structure</a>");
                }

                // Display logs regardless of success/failure
                out.println("<h3>Process Log:</h3>");
                out.println(
                        "<ul style='background-color: #f9f9f9; padding: 15px; border: 1px solid #ddd; max-height: 400px; overflow: auto;'>");
                for (String log : logs) {
                    if (log.startsWith("ERROR:")) {
                        out.println("<li style='color: red; font-weight: bold;'>" + log + "</li>");
                    } else if (log.startsWith("Warning:")) {
                        out.println("<li style='color: orange;'>" + log + "</li>");
                    } else {
                        out.println("<li>" + log + "</li>");
                    }
                }
                out.println("</ul>");
            }

        } catch (Exception e) {
            // Output error message for any exception during the entire process
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();

            out.println("<p class='error'>Critical error generating dummy data: " + e.getMessage() + "</p>");
            out.println(
                    "<pre class='error' style='background-color: #fff0f0; padding: 10px; border: 1px solid #ffcccc; overflow: auto; max-height: 300px;'>");
            out.println(stackTrace.replace("<", "&lt;").replace(">", "&gt;"));
            out.println("</pre>");

            // Add a link to check database structure
            out.println("<p>Please check your database structure:</p>");
            out.println("<a href='" + request.getContextPath() +
                    "/admin/check-database' class='button' style='background-color: #2196F3;'>Check Database Structure</a>");
        } finally {
            // HTML footer
            out.println("<a href='" + request.getContextPath() + "/admin/dashboard'>Return to Admin Dashboard</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Loads the configuration from the config file.
     */
    private Properties loadConfiguration() throws IOException {
        Properties config = new Properties();
        String fullPath = getServletContext().getRealPath(CONFIG_FILE_PATH);
        File configFile = new File(fullPath);

        // Create the config file if it doesn't exist
        if (!configFile.exists()) {
            // Create parent directories if needed
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }

            // Initialize with default values
            config.setProperty("dummy_data.loaded", "false");
            config.setProperty("games.count", "50");
            config.setProperty("users.count", "10");
            config.setProperty("orders.count", "50");
            config.setProperty("last_load_date", "");

            // Save the initial config
            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                config.store(fos, "Initial Dummy Data Configuration");
            }
        } else {
            // Load existing config
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
            }
        }

        return config;
    }

    /**
     * Creates orders using the generated games.
     */
    private void generateOrders(List<Game> games, List<Integer> userIds, int count, List<String> logs)
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
            orderStorage = new OrderStorage();
        } catch (Exception e) {
            logs.add("ERROR: Failed to create OrderStorage: " + e.getMessage());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logs.add("Stack trace: " + sw.toString());
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

                logs.add("Saving order to database");
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                logs.add("ERROR creating order: " + e.getMessage());
                logs.add("Stack trace: " + sw.toString());
            }
        }
    }

    /**
     * Generates a specified number of games with random data.
     */
    private List<Game> generateGames(GameStorage gameStorage, int count, List<String> logs) {
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

            // Generate new games
            for (int i = 0; i < count; i++) {
                try {
                    Game game = createRandomGame(existingGameCount + i);
                    gameStorage.save(game);
                    generatedGames.add(game);
                    logs.add(
                            "Created game: " + game.getTitle() + " (" + game.getPlatform() + ") - $" + game.getPrice());
                } catch (Exception e) {
                    logs.add("Error creating game #" + (i + 1) + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            logs.add("Error working with games: " + e.getMessage());
        }

        return generatedGames;
    }

    /**
     * Updates the configuration file after data generation.
     */
    private void updateConfiguration(Properties config) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        config.setProperty("dummy_data.loaded", "true");
        config.setProperty("last_load_date", currentDate);

        String fullPath = getServletContext().getRealPath(CONFIG_FILE_PATH);
        try (FileOutputStream fos = new FileOutputStream(fullPath)) {
            config.store(fos, "Dummy Data Configuration - Last Updated: " + currentDate);
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

    /**
     * Creates a random game using the arrays of titles, descriptions, etc.
     */
    private Game createRandomGame(int index) {
        Game game = new Game();

        // Select appropriate data based on the index to avoid duplicates
        String title = gameTitles[index % gameTitles.length];
        if (index >= gameTitles.length) {
            // If we're past the array length, append a number to make unique titles
            title += " " + (index / gameTitles.length + 1);
        }

        // Select random data for the game
        String description = gameDescriptions[random.nextInt(gameDescriptions.length)];
        String developer = developers[random.nextInt(developers.length)];
        String platform = platforms[random.nextInt(platforms.length)];
        String genre = genres[random.nextInt(genres.length)];

        // Generate random price between $10 and $60, rounded to .99
        float price = Math.round((10 + random.nextFloat() * 50) * 100) / 100.0f;
        price = (float) (Math.floor(price) + 0.99);

        // Generate random release date in the past 3 years
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -random.nextInt(3)); // 0-2 years ago
        calendar.add(Calendar.DAY_OF_YEAR, -random.nextInt(365)); // Random day within that year

        // Set all properties
        game.setTitle(title);
        game.setDescription(description);
        game.setDeveloper(developer);
        game.setPlatform(platform);
        game.setPrice(price);
        game.setReleaseDate(calendar.getTime());
        game.setGenre(genre);

        // Default image path (will be updated if images are uploaded)
        game.setImagePath("/game_images/default_game.jpg");

        return game;
    }

    /**
     * Generates users if needed and returns a list of user IDs.
     */
    private List<Integer> generateUsers(int count, List<String> logs) throws SQLException, IOException {
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
                    String username = getUniqueUsername(i);
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
     * Gets a unique username based on index.
     */
    private String getUniqueUsername(int index) {
        String baseUsername = usernames[index % usernames.length];
        if (index >= usernames.length) {
            return baseUsername + (index / usernames.length + 1);
        }
        return baseUsername;
    }
}