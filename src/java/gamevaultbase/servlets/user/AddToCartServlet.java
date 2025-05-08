package gamevaultbase.servlets.user;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.GameAlreadyOwnedException;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.servlets.base.UserBaseServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling the add to cart functionality.
 * Only accessible to logged-in users.
 */
@WebServlet(name = "AddToCartServlet", urlPatterns = { "/addToCart" })
public class AddToCartServlet extends UserBaseServlet {

    @Override
    protected void processUserGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Adding items should be a POST action
        redirectWithMessage(request, response, "/viewCart",
                "Please use the 'Add to Cart' button on game pages", "error");
    }

    @Override
    protected void processUserPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = getLoggedInUser(request);

        String gameIdParam = request.getParameter("gameId");
        String message = null;
        String messageType = "error"; // Default to error

        if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            message = "Invalid request: Missing game ID.";
        } else {
            try {
                int gameId = Integer.parseInt(gameIdParam.trim());

                // Perform the add to cart operation
                getCartManagement().addGameToCart(currentUser.getUserId(), gameId);
                message = "Game added to cart successfully.";
                messageType = "success";

            } catch (NumberFormatException e) {
                message = "Invalid game ID format provided.";
            } catch (GameNotFoundException e) {
                message = "The requested game was not found.";
            } catch (GameAlreadyOwnedException e) {
                message = "You already own this game.";
            } catch (Exception e) {
                System.err.println("Error adding to cart (User: " + currentUser.getUserId() + ", Game: "
                        + gameIdParam + "): " + e.getMessage());
                e.printStackTrace();
                message = "An error occurred while adding the game to your cart.";
            }
        }

        // Redirect back to the referring page or cart
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            redirectWithMessage(request, response, referer, message, messageType);
        } else {
            redirectWithMessage(request, response, "/viewCart", message, messageType);
        }
    }
}