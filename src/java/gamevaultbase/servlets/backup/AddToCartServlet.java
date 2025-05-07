package gamevaultbase.servlets;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.GameAlreadyOwnedException;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.servlets.base.UserBaseServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AddToCartServlet", urlPatterns = { "/addToCart" })
public class AddToCartServlet extends UserBaseServlet {

    @Override
    protected void processUserGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Adding to cart should ideally be POST, but handle GET if called via simple
        // link
        processUserPostRequest(request, response);
    }

    @Override
    protected void processUserPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = getLoggedInUser(request);

        String gameIdParam = request.getParameter("gameId");
        String message = null;
        String messageType = "error"; // Default to error message styling
        String redirectUrl = "/games"; // Default redirect back to games list

        if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            message = "Invalid request: Missing game ID.";
        } else {
            try {
                int gameId = Integer.parseInt(gameIdParam.trim());

                // Optional: Get game details if needed for message
                // Game game = getGameManagement().getGame(gameId);

                getCartManagement().addGameToCart(currentUser.getUserId(), gameId);
                // Adjust message if you fetched game details
                // message = game.getTitle() + " added to cart successfully!";
                message = "Game added to cart successfully!";
                messageType = "success";
                // Optionally redirect to cart page after adding
                // redirectUrl = "/viewCart";

            } catch (NumberFormatException e) {
                message = "Invalid game ID format provided.";
            } catch (GameNotFoundException e) {
                message = "The selected game could not be found.";
            } catch (GameAlreadyOwnedException e) {
                // This exception might be thrown if user tries to add owned game
                message = "You already own this game.";
            } catch (Exception e) {
                // Catch potential SQL exceptions from storage layer (e.g., duplicate entry in
                // CartItems)
                if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
                    message = "This game is already in your cart.";
                } else {
                    System.err.println("Error adding to cart (User: " + currentUser.getUserId() + ", Game: "
                            + gameIdParam + "): " + e.getMessage());
                    e.printStackTrace(); // Log stack trace
                    message = "An error occurred while adding the game to your cart.";
                }
            }
        }

        // Redirect back with a message
        redirectWithMessage(request, response, redirectUrl, message, messageType);
    }
}