package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.helpers.JSONUtil;
import gamevaultbase.storage.GameStorage;
import gamevaultbase.helpers.DBUtil;
import gamevaultbase.helpers.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This servlet loads game data from the games.json file into the database.
 * It's an alternative to the DummyDataServlet that uses real game data instead
 * of random data.
 * Access at /admin/load-json-data
 */
@WebServlet(name = "GameJsonLoaderServlet", urlPatterns = { "/admin/load-json-data" })
public class GameJsonLoaderServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(GameJsonLoaderServlet.class.getName());
    private static final String CONFIG_FILE_PATH = "/WEB-INF/dummy_data_config.txt";
    private static final String JSON_FILE_PATH = "/WEB-INF/games.json";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in and is an admin
        if (!ServletUtil.checkAdminAccess(request, response)) {
            return;
        }

        // Create a list to store logs
        List<String> logs = new ArrayList<>();
        logs.add("Starting JSON game data loading process");

        try {
            // Load configuration
            Properties config = loadConfiguration();

            // Check if data has already been loaded
            boolean dataLoaded = Boolean.parseBoolean(config.getProperty("json_data.loaded", "false"));
            String lastLoadDate = config.getProperty("json_load_date", "");

            // Handle force reload parameter
            boolean forceReload = "true".equals(request.getParameter("force"));

            // Set attributes for the JSP
            request.setAttribute("dataLoaded", dataLoaded);
            request.setAttribute("lastLoadDate", lastLoadDate);
            request.setAttribute("forceReload", forceReload);

            if (dataLoaded && !forceReload) {
                // No action needed, just display the JSP with info
            } else {
                try {
                    // Load the JSON file
                    logs.add("Opening JSON file: " + JSON_FILE_PATH);
                    InputStream inputStream = getServletContext().getResourceAsStream(JSON_FILE_PATH);

                    if (inputStream == null) {
                        throw new IOException("Could not find JSON file: " + JSON_FILE_PATH);
                    }

                    // Parse the JSON data
                    logs.add("Parsing JSON data");
                    List<Game> games = JSONUtil.parseGamesFromJson(inputStream);
                    logs.add("Parsed " + games.size() + " games from JSON");

                    // Check for existing games
                    String sqlCountGames = "SELECT COUNT(*) AS gameCount FROM Games";
                    List<Integer> results = DBUtil.executeQuery(sqlCountGames, rs -> rs.getInt("gameCount"));
                    int existingGameCount = results.isEmpty() ? 0 : results.get(0);
                    logs.add("Found " + existingGameCount + " existing games in database");

                    // Clear existing games if force reload
                    if (forceReload && existingGameCount > 0) {
                        logs.add("Force reload requested. Clearing existing game data...");
                        try {
                            String sqlClearGames = "DELETE FROM Games";
                            int deleted = DBUtil.executeUpdate(sqlClearGames);
                            logs.add("Deleted " + deleted + " existing games");
                        } catch (SQLException e) {
                            logs.add("ERROR: Could not clear existing games: " + e.getMessage());
                            logs.add(
                                    "This might be due to foreign key constraints. You may need to clear related tables first.");
                        }
                    }

                    // Save the games to the database
                    logs.add("Creating GameStorage instance");
                    GameStorage gameStorage = new GameStorage();

                    int successCount = 0;
                    logs.add("Starting to save " + games.size() + " games to database");

                    for (Game game : games) {
                        try {
                            gameStorage.save(game);
                            successCount++;

                            if (successCount % 10 == 0 || successCount == games.size()) {
                                logs.add("Progress: Saved " + successCount + " of " + games.size() + " games");
                            }
                        } catch (Exception e) {
                            logs.add("ERROR saving game '" + game.getTitle() + "': " + e.getMessage());
                        }
                    }

                    // Update configuration
                    logs.add("Updating configuration");
                    config.setProperty("json_data.loaded", "true");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDate = dateFormat.format(new Date());
                    config.setProperty("json_load_date", currentDate);

                    String fullPath = getServletContext().getRealPath(CONFIG_FILE_PATH);
                    try (FileOutputStream fos = new FileOutputStream(fullPath)) {
                        config.store(fos, "JSON Data Configuration - Last Updated: " + currentDate);
                    }

                    // Set success attributes for the JSP
                    request.setAttribute("generationSuccess", true);
                    request.setAttribute("successCount", successCount);
                    request.setAttribute("totalCount", games.size());

                } catch (Exception e) {
                    // Capture detailed error information
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stackTrace = sw.toString();

                    logs.add("ERROR: " + e.getClass().getName() + ": " + e.getMessage());

                    request.setAttribute("error", "Error occurred during data loading: " + e.getMessage());
                    request.setAttribute("stackTrace", stackTrace);
                }
            }

            // Set the logs attribute for the JSP
            request.setAttribute("logs", logs);

        } catch (Exception e) {
            // Handle general exceptions
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();

            request.setAttribute("error", "Critical error loading JSON data: " + e.getMessage());
            request.setAttribute("stackTrace", stackTrace);
        }

        // Forward to the JSP view
        ServletUtil.forwardToJsp(request, response, "/WEB-INF/jsp/adminJsonLoader.jsp");
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
            config.setProperty("json_data.loaded", "false");
            config.setProperty("last_load_date", "");
            config.setProperty("json_load_date", "");

            // Save the initial config
            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                config.store(fos, "Initial Game Data Configuration");
            }
        } else {
            // Load existing config
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
            }
        }

        return config;
    }
}