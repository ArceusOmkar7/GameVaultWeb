package gamevaultbase.servlets.user;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.InvalidUserDataException;
import gamevaultbase.exceptions.UserNotFoundException;
import gamevaultbase.servlets.base.UserBaseServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Mapped in web.xml as /editProfile
public class EditProfileServlet extends UserBaseServlet {

    @Override
    protected void processUserGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = getLoggedInUser(request);
        request.setAttribute("user", currentUser);
        forwardToJsp(request, response, "/WEB-INF/jsp/editProfile.jsp");
    }

    @Override
    protected void processUserPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = getLoggedInUser(request);
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String message = null;
        String messageType = "error";

        try {
            // Basic Validation
            if (email == null || email.trim().isEmpty()) {
                throw new InvalidUserDataException("email", "Email cannot be empty.");
            }
            if (username == null || username.trim().isEmpty()) {
                throw new InvalidUserDataException("username", "Username cannot be empty.");
            }
            
            email = email.trim();
            username = username.trim();

            // Create updated user object
            User updatedUser = new User(
                currentUser.getUserId(),
                email,
                currentUser.getPassword(),
                username,
                currentUser.getWalletBalance(),
                currentUser.getCreatedAt()
            );
            updatedUser.setIsAdmin(currentUser.isAdmin());

            // Update user in database
            getUserManagement().updateUser(updatedUser);

            // Update session
            request.getSession().setAttribute("loggedInUser", updatedUser);
            message = "Profile updated successfully!";
            messageType = "success";

            // Redirect back to profile page with success message
            redirectWithMessage(request, response, "/profile", message, messageType);
            return;

        } catch (InvalidUserDataException e) {
            message = e.getMessage();
        } catch (UserNotFoundException e) {
            message = "Could not find user profile to update.";
            request.getSession().invalidate();
            redirectWithMessage(request, response, "/login", "Error finding profile. Please login again.", messageType);
            return;
        } catch (Exception e) {
            System.err.println("Error updating profile for user " + currentUser.getUserId() + ": " + e.getMessage());
            e.printStackTrace();
            message = "An unexpected error occurred while updating your profile.";
        }

        // If error occurred, forward back to edit page
        request.setAttribute("errorMessage", message);
        request.setAttribute("user", currentUser);
        forwardToJsp(request, response, "/WEB-INF/jsp/editProfile.jsp");
    }
}
