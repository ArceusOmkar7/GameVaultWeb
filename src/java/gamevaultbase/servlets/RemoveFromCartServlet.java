package gamevaultbase.servlets;

import gamevaultbase.entities.User;
import gamevaultbase.management.CartManagement;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Mapped in web.xml as /removeFromCart
public class RemoveFromCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        // User must be logged in
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=Please login to modify your cart&messageType=error");
            return;
        }

        String gameIdParam = request.getParameter("gameId");
        String message = null;
        String messageType = "error"; // Default to error
        String redirectUrl = request.getContextPath() + "/viewCart"; // Redirect back to cart

        CartManagement cartManagement = (CartManagement) getServletContext().getAttribute("cartManagement");

        if (cartManagement == null) {
            System.err.println("FATAL: CartManagement not found in ServletContext!");
            message = "Application error: Cart service unavailable.";
        } else if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            message = "Invalid request: Missing game ID.";
        } else {
            try {
                int gameId = Integer.parseInt(gameIdParam.trim());

                // Perform the removal
                cartManagement.removeGameFromCart(currentUser.getUserId(), gameId);
                message = "Game removed from cart successfully.";
                messageType = "success";

            } catch (NumberFormatException e) {
                message = "Invalid game ID format provided.";
            } catch (Exception e) {
                System.err.println("Error removing from cart (User: " + currentUser.getUserId() + ", Game: " + gameIdParam + "): " + e.getMessage());
                e.printStackTrace();
                message = "An error occurred while removing the game from your cart.";
            }
        }

        // Redirect back to the cart page with a message
        redirectUrl += "?message=" + java.net.URLEncoder.encode(message, "UTF-8")
                    + "&messageType=" + messageType;
        response.sendRedirect(redirectUrl);
    }

    // Removing items should be a POST action
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         resp.sendRedirect(req.getContextPath() + "/viewCart?message=Please use the remove button in the cart&messageType=error");
    }
}