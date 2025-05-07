package gamevaultbase.servlets.user;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.User;
import gamevaultbase.exceptions.CartEmptyException;
import gamevaultbase.servlets.base.UserBaseServlet;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling the user's shopping cart view.
 * Only accessible to logged-in users.
 */
@WebServlet(name = "CartServlet", urlPatterns = { "/viewCart" })
public class CartServlet extends UserBaseServlet {

    @Override
    protected void processUserGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = getLoggedInUser(request);
        List<Game> cartGames = Collections.emptyList();
        double cartTotal = 0.0;
        String errorMessage = null;

        try {
            // Get the items in the user's cart - fixed method name from getCartItems to
            // getGamesInCart
            cartGames = getCartManagement().getGamesInCart(currentUser.getUserId());

            // Calculate the total price
            if (cartGames != null && !cartGames.isEmpty()) {
                for (Game game : cartGames) {
                    cartTotal += game.getPrice();
                }
            }
        } catch (CartEmptyException e) {
            // This is a normal case, not an error - just show empty cart
            cartGames = Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error retrieving cart for user " + currentUser.getUserId() + ": " + e.getMessage());
            e.printStackTrace();
            errorMessage = "An error occurred while retrieving your cart items.";
            cartGames = Collections.emptyList();
        }

        // Pass data to the JSP
        request.setAttribute("cartGames", cartGames);
        request.setAttribute("cartTotal", cartTotal);
        request.setAttribute("errorMessage", errorMessage);

        // Pass messages from redirects (e.g., from AddToCart or RemoveFromCart)
        String message = request.getParameter("message");
        String messageType = request.getParameter("messageType");
        if (message != null) {
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType);
        }

        // Forward to the cart JSP
        forwardToJsp(request, response, "/WEB-INF/jsp/cart.jsp");
    }
}
