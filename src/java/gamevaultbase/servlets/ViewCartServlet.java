package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.User;
import gamevaultbase.exceptions.CartEmptyException;
import gamevaultbase.management.CartManagement;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Mapped in web.xml as /viewCart
public class ViewCartServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     * Fetches and displays the user's shopping cart contents.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Don't create a new session if one doesn't exist
        User currentUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        // --- Authentication Check ---
        if (currentUser == null) {
            // User is not logged in, redirect to login page with a message
            String message = "Please login to view your cart.";
            response.sendRedirect(request.getContextPath() + "/login?message=" + java.net.URLEncoder.encode(message, "UTF-8") + "&messageType=error");
            return; // Stop processing
        }

        // --- Get Cart Management Service ---
        CartManagement cartManagement = (CartManagement) getServletContext().getAttribute("cartManagement");
        if (cartManagement == null) {
             System.err.println("FATAL: CartManagement not found in ServletContext!");
             // Forward to a generic error page or show an error on the cart page
             request.setAttribute("errorMessage", "Cart service is currently unavailable.");
             request.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(request, response); // Still show cart page structure
             return;
        }

        List<Game> cartGames = Collections.emptyList(); // Default to empty list
        String requestErrorMessage = null; // Error specific to this request processing
        double total = 0.0;

        // --- Fetch Cart Contents ---
        try {
            cartGames = cartManagement.getGamesInCart(currentUser.getUserId());
            // Calculate total only if cartGames is not null (it shouldn't be null from storage, but check anyway)
            if (cartGames != null) {
                total = cartGames.stream()
                                 .mapToDouble(Game::getPrice) // Assumes getPrice() returns double or float
                                 .sum();
            }
        } catch (CartEmptyException e) {
             // This is a normal condition, not an error to display prominently.
             // The JSP will handle displaying "Your cart is empty."
             System.out.println("Cart view: Cart is empty for user " + currentUser.getUserId());
             cartGames = Collections.emptyList(); // Ensure it's an empty list for the JSP
        } catch (Exception e) {
            // Catch other potential errors (like database connection issues in storage)
            System.err.println("Error viewing cart for user " + currentUser.getUserId() + ": " + e.getMessage());
            e.printStackTrace(); // Log the full error
            requestErrorMessage = "An error occurred while retrieving your cart contents.";
            cartGames = Collections.emptyList(); // Ensure it's an empty list for the JSP
        }

        // --- Prepare data for JSP ---
        // Pass any message forwarded from another servlet (like PlaceOrderServlet)
        String redirectMessage = request.getParameter("message");
        String redirectMessageType = request.getParameter("messageType");

        if (redirectMessage != null) {
            request.setAttribute("message", redirectMessage);
            request.setAttribute("messageType", redirectMessageType);
        }
        // Also pass any error message generated during cart retrieval
        if (requestErrorMessage != null) {
             request.setAttribute("errorMessage", requestErrorMessage);
        }

        request.setAttribute("cartGames", cartGames); // The list of games in the cart
        request.setAttribute("cartTotal", total);     // The calculated total price

        // --- Forward to JSP ---
        request.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * In this case, viewing the cart is typically a GET request.
     * Redirect POST requests to GET to avoid issues with browser refresh.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to the GET handler for this servlet
        response.sendRedirect(request.getContextPath() + "/viewCart");
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Displays the contents of the user's shopping cart.";
    }
}