package gamevaultbase.servlets.user;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.InvalidUserDataException;
import gamevaultbase.exceptions.UserNotFoundException;
import gamevaultbase.management.UserManagement;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Mapped in web.xml as /editProfile
public class EditProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=Please login to edit your profile.&messageType=error");
            return;
        }

        // Forward to the JSP with current user data
        request.setAttribute("user", currentUser);
        request.getRequestDispatcher("/WEB-INF/jsp/editProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=Session expired. Please login again.&messageType=error");
            return;
        }

        // Get parameters from form
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String errorMessage = null;
        String successMessage = null;

        UserManagement userManagement = (UserManagement) getServletContext().getAttribute("userManagement");

        if (userManagement == null) {
            System.err.println("FATAL: UserManagement not found in ServletContext!");
            errorMessage = "Profile update service is currently unavailable.";
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/WEB-INF/jsp/editProfile.jsp").forward(request, response);
            return;
        }

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
            userManagement.updateUser(updatedUser);

            // Update session
            session.setAttribute("loggedInUser", updatedUser);
            successMessage = "Profile updated successfully!";

            // Redirect back to profile page with success message
            response.sendRedirect(request.getContextPath() + "/profile?message=" + 
                java.net.URLEncoder.encode(successMessage, "UTF-8") + "&messageType=success");
            return;

        } catch (InvalidUserDataException e) {
            errorMessage = e.getMessage();
        } catch (UserNotFoundException e) {
            errorMessage = "Could not find user profile to update.";
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login?message=Error finding profile. Please login again.&messageType=error");
            return;
        } catch (Exception e) {
            System.err.println("Error updating profile for user " + currentUser.getUserId() + ": " + e.getMessage());
            e.printStackTrace();
            errorMessage = "An unexpected error occurred while updating your profile.";
        }

        // If error occurred, forward back to edit page
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("user", currentUser);
        request.getRequestDispatcher("/WEB-INF/jsp/editProfile.jsp").forward(request, response);
    }
}
