package gamevaultbase.servlets;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.UserNotFoundException;
import gamevaultbase.management.UserManagement;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AddBalanceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=Session expired. Please login again.&messageType=error");
            return;
        }

        String amountStr = request.getParameter("amount");
        String errorMessage = null;
        String successMessage = null;

        UserManagement userManagement = (UserManagement) getServletContext().getAttribute("userManagement");

        if (userManagement == null) {
            System.err.println("FATAL: UserManagement not found in ServletContext!");
            errorMessage = "Balance update service is currently unavailable.";
            response.sendRedirect(request.getContextPath() + "/profile?message=" + 
                java.net.URLEncoder.encode(errorMessage, "UTF-8") + "&messageType=error");
            return;
        }

        try {
            // Validate amount
            if (amountStr == null || amountStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Amount cannot be empty.");
            }

            float amount = Float.parseFloat(amountStr.trim());
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero.");
            }

            // Update user's balance
            float newBalance = currentUser.getWalletBalance() + amount;
            User updatedUser = new User(
                currentUser.getUserId(),
                currentUser.getEmail(),
                currentUser.getPassword(),
                currentUser.getUsername(),
                newBalance,
                currentUser.getCreatedAt()
            );
            updatedUser.setIsAdmin(currentUser.isAdmin());

            // Update in database
            userManagement.updateUser(updatedUser);

            // Update session
            session.setAttribute("loggedInUser", updatedUser);
            successMessage = "Balance updated successfully! Added $" + String.format("%.2f", amount);

        } catch (NumberFormatException e) {
            errorMessage = "Please enter a valid amount.";
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        } catch (UserNotFoundException e) {
            errorMessage = "Could not find user profile to update.";
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login?message=Error finding profile. Please login again.&messageType=error");
            return;
        } catch (Exception e) {
            System.err.println("Error updating balance for user " + currentUser.getUserId() + ": " + e.getMessage());
            e.printStackTrace();
            errorMessage = "An unexpected error occurred while updating your balance.";
        }

        // Redirect back to profile with appropriate message
        String redirectUrl = request.getContextPath() + "/profile?message=" + 
            java.net.URLEncoder.encode(errorMessage != null ? errorMessage : successMessage, "UTF-8") + 
            "&messageType=" + (errorMessage != null ? "error" : "success");
        response.sendRedirect(redirectUrl);
    }
}