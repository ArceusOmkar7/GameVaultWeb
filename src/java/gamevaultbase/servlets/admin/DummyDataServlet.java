package gamevaultbase.servlets.admin;

import gamevaultbase.config.DummyDataConfig;
import gamevaultbase.entities.Game;
import gamevaultbase.helpers.ServletUtil;
import gamevaultbase.servlets.base.AdminBaseServlet;
import gamevaultbase.services.DummyDataService;
import gamevaultbase.storage.GameStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This servlet generates dummy data for the GameVault application.
 * It now uses separated components for better organization.
 * Access at /admin/generate-dummy-data
 */
@WebServlet(name = "DummyDataServlet", urlPatterns = { "/admin/generate-dummy-data" })
public class DummyDataServlet extends AdminBaseServlet {

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Create a list to store logs
        List<String> logs = new ArrayList<>();
        logs.add("Starting dummy data generation process");

        try {
            // Initialize configuration and service components
            DummyDataConfig config = new DummyDataConfig(getServletContext());
            config.loadConfiguration();
            DummyDataService service = new DummyDataService(getServletContext());

            // Check if data has already been loaded
            boolean dataLoaded = config.isDataLoaded();
            String lastLoadDate = config.getLastLoadDate();

            // Handle force reload parameter
            boolean forceReload = "true".equals(request.getParameter("force"));

            // Set attributes for the JSP
            request.setAttribute("dataLoaded", dataLoaded);
            request.setAttribute("lastLoadDate", lastLoadDate);
            request.setAttribute("forceReload", forceReload);

            if (dataLoaded && !forceReload) {
                // No action needed, just display the JSP with info
                logs.add("Data already loaded on " + lastLoadDate + ". Use force=true parameter to reload.");
            } else {
                // Generate new data
                int gamesCount = config.getGamesCount();
                int ordersCount = config.getOrdersCount();
                int usersCount = config.getUsersCount();

                logs.add("Configured to generate: " + gamesCount + " games, " + ordersCount + " orders, " + usersCount
                        + " users");

                try {
                    // Create GameStorage object
                    logs.add("Creating GameStorage");
                    GameStorage gameStorage = new GameStorage();

                    // Create users if needed
                    logs.add("Generating users");
                    List<Integer> userIds = service.generateUsers(usersCount, logs);
                    logs.add("Generated " + userIds.size() + " user IDs");

                    // Generate games from JSON
                    logs.add("Generating games from JSON data");
                    List<Game> generatedGames = service.generateGames(gameStorage, gamesCount, logs);
                    logs.add("Generated " + generatedGames.size() + " games");

                    // Generate orders using the created games
                    logs.add("Generating orders");
                    service.generateOrders(generatedGames, userIds, ordersCount, logs);

                    // Update configuration
                    logs.add("Updating configuration");
                    config.updateConfiguration();

                    request.setAttribute("generationSuccess", true);
                } catch (Exception e) {
                    // Capture detailed error information
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String stackTrace = sw.toString();

                    logs.add("ERROR: " + e.getClass().getName() + ": " + e.getMessage());

                    request.setAttribute("error", "Error occurred during data generation: " + e.getMessage());
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

            request.setAttribute("error", "Critical error generating dummy data: " + e.getMessage());
            request.setAttribute("stackTrace", stackTrace);
        }

        // Forward to the JSP view
        ServletUtil.forwardToJsp(request, response, "/WEB-INF/jsp/adminDummyData.jsp");
    }
}
