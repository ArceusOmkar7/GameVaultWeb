package gamevaultbase.servlets.user;

import gamevaultbase.entities.User;
import gamevaultbase.servlets.base.UserBaseServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RemoveFromCartServlet", urlPatterns = { "/removeFromCart" })
public class RemoveFromCartServlet extends UserBaseServlet {

    @Override
    protected void processUserGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Removing items should be a POST action
        redirectWithMessage(request, response, "/viewCart",
                "Please use the remove button in the cart", "error");
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

                // Perform the removal
                getCartManagement().removeGameFromCart(currentUser.getUserId(), gameId);
                message = "Game removed from cart successfully.";
                messageType = "success";

            } catch (NumberFormatException e) {
                message = "Invalid game ID format provided.";
            } catch (Exception e) {
                System.err.println("Error removing from cart (User: " + currentUser.getUserId() + ", Game: "
                        + gameIdParam + "): " + e.getMessage());
                e.printStackTrace();
                message = "An error occurred while removing the game from your cart.";
            }
        }

        // Redirect back to the cart page with a message
        redirectWithMessage(request, response, "/viewCart", message, messageType);
    }
}
