package gamevaultbase.servlets;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.CartEmptyException;
import gamevaultbase.exceptions.UserNotFoundException; // Import this
import gamevaultbase.management.OrderManagement;
import gamevaultbase.management.UserManagement; // Needed to refresh user balance in session
import java.io.IOException;
import javax.servlet.ServletException;
// Import annotation if using
// import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Mapped in web.xml as /placeOrder
public class PlaceOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        // User must be logged in
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=Please login to place an order&messageType=error");
            return;
        }

        OrderManagement orderManagement = (OrderManagement) getServletContext().getAttribute("orderManagement");
        UserManagement userManagement = (UserManagement) getServletContext().getAttribute("userManagement");
        String message = null;
        String messageType = "error"; // Default to error message styling
        String redirectUrl = request.getContextPath() + "/viewCart"; // Default redirect back to cart

         if (orderManagement == null || userManagement == null) {
             System.err.println("FATAL: OrderManagement or UserManagement not found in ServletContext!");
             message = "Application error: Order service unavailable.";
         } else {
            try {
                // Attempt to place the order
                orderManagement.placeOrder(currentUser.getUserId());
                message = "Order placed successfully!";
                messageType = "success";
                redirectUrl = request.getContextPath() + "/orders"; // Redirect to orders page on success

                // Refresh user balance in session after successful order
                 try {
                    User updatedUser = userManagement.getUser(currentUser.getUserId()); // Fetch updated user data
                    if (updatedUser != null) {
                        session.setAttribute("loggedInUser", updatedUser); // Update session attribute
                        System.out.println("User balance updated in session for userId: " + currentUser.getUserId());
                    } else {
                         // Handle case where user might have been deleted concurrently? Unlikely but possible.
                         System.err.println("WARN: Could not find user " + currentUser.getUserId() + " after placing order to update session.");
                         session.invalidate(); // Log out user if data is inconsistent
                         response.sendRedirect(request.getContextPath() + "/login?message=User data inconsistency. Please log in again.&messageType=error");
                         return; // Stop further processing
                    }
                 } catch (UserNotFoundException unfEx) {
                      System.err.println("WARN: Could not find user " + currentUser.getUserId() + " after placing order to update session (UserNotFoundException).");
                       session.invalidate(); // Log out user
                       response.sendRedirect(request.getContextPath() + "/login?message=User data inconsistency. Please log in again.&messageType=error");
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
        }

        // Redirect back with a status message
        redirectUrl += "?message=" + java.net.URLEncoder.encode(message, "UTF-8")
                    + "&messageType=" + messageType;
        response.sendRedirect(redirectUrl);
    }

    // Placing orders should always be POST to avoid accidental duplicates via GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/viewCart?message=Please use the button in the cart to place order&messageType=error");
    }
}