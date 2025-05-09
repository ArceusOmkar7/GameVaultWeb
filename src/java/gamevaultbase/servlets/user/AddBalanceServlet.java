package gamevaultbase.servlets.user;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.InvalidUserDataException;
import gamevaultbase.exceptions.UserNotFoundException;
import gamevaultbase.servlets.base.UserBaseServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddBalanceServlet extends UserBaseServlet {

    @Override
    protected void processUserGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // No GET request handling needed for balance updates
        redirectTo(request, response, "/profile");
    }

    @Override
    protected void processUserPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = getLoggedInUser(request);
        String amountStr = request.getParameter("amount");
        String message = null;
        String messageType = "error";

        try {
            // Validate amount
            if (amountStr == null || amountStr.trim().isEmpty()) {
                throw new InvalidUserDataException("amount", "Amount cannot be empty.");
            }

            float amount = Float.parseFloat(amountStr.trim());
            if (amount <= 0) {
                throw new InvalidUserDataException("amount", "Amount must be greater than 0.");
            }

            // Update balance
            getUserManagement().updateWalletBalance(currentUser.getUserId(), amount);

            // Refresh user data in session
            User updatedUser = getUserManagement().getUser(currentUser.getUserId());
            request.getSession().setAttribute("loggedInUser", updatedUser);

            message = "Balance updated successfully!";
            messageType = "success";

        } catch (NumberFormatException e) {
            message = "Invalid amount format.";
        } catch (InvalidUserDataException e) {
            message = e.getMessage();
        } catch (UserNotFoundException e) {
            message = "User not found. Please log in again.";
            request.getSession().invalidate();
            redirectWithMessage(request, response, "/login", message, messageType);
            return;
        } catch (Exception e) {
            System.err.println("Error updating balance for user " + currentUser.getUserId() + ": " + e.getMessage());
            e.printStackTrace();
            message = "An unexpected error occurred while updating your balance.";
        }

        // Redirect back to profile page with message
        redirectWithMessage(request, response, "/profile", message, messageType);
    }
} 