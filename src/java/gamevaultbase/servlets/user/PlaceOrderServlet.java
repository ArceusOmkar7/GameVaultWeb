package gamevaultbase.servlets.user;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.CartEmptyException;
import gamevaultbase.exceptions.UserNotFoundException;
import gamevaultbase.servlets.base.UserBaseServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "PlaceOrderServlet", urlPatterns = { "/placeOrder" })
public class PlaceOrderServlet extends UserBaseServlet {

    @Override
    protected void processUserGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Placing orders should always be POST to avoid accidental duplicates via GET
        redirectWithMessage(request, response, "/viewCart",
                "Please use the button in the cart to place order", "error");
    }

    @Override
    protected void processUserPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = getLoggedInUser(request);
        String message = null;
        String messageType = "error"; // Default to error message styling
        String redirectUrl = "/viewCart"; // Default redirect back to cart

        try {
            // Attempt to place the order
            getOrderManagement().placeOrder(currentUser.getUserId());
            message = "Order placed successfully!";
            messageType = "success";
            redirectUrl = "/orders"; // Redirect to orders page on success

            // Refresh user balance in session after successful order
            try {
                User updatedUser = getUserManagement().getUser(currentUser.getUserId()); // Fetch updated user data
                if (updatedUser != null) {
                    // Update session attribute
                    request.getSession().setAttribute("loggedInUser", updatedUser);
                    System.out.println("User balance updated in session for userId: " + currentUser.getUserId());
                } else {
                    // Handle case where user might have been deleted concurrently? Unlikely but
                    // possible.
                    System.err.println("WARN: Could not find user " + currentUser.getUserId()
                            + " after placing order to update session.");
                    request.getSession().invalidate(); // Log out user if data is inconsistent
                    redirectWithMessage(request, response, "/login",
                            "User data inconsistency. Please log in again.", "error");
                    return; // Stop further processing
                }
            } catch (UserNotFoundException unfEx) {
                System.err.println("WARN: Could not find user " + currentUser.getUserId()
                        + " after placing order to update session (UserNotFoundException).");
                request.getSession().invalidate(); // Log out user
                redirectWithMessage(request, response, "/login",
                        "User data inconsistency. Please log in again.", "error");
                return; // Stop further processing
            }

        } catch (CartEmptyException e) {
            message = "Cannot place order: Your cart is empty.";
        } catch (IllegalStateException e) { // Catch insufficient balance or other expected issues from OrderManagement
            message = "Cannot place order: " + e.getMessage(); // e.g., "Insufficient balance"
        } catch (Exception e) {
            System.err.println("Error placing order for user " + currentUser.getUserId() + ": " + e.getMessage());
            e.printStackTrace(); // Log stack trace
            message = "An unexpected error occurred while placing your order.";
        }

        // Redirect back with a status message
        redirectWithMessage(request, response, redirectUrl, message, messageType);
    }
}
