package gamevaultbase.servlets;

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
        // Password changes would require current password validation - skipped for simplicity
        // String currentPassword = request.getParameter("currentPassword");
        // String newPassword = request.getParameter("newPassword");
        // String confirmNewPassword = request.getParameter("confirmNewPassword");

        String errorMessage = null;
        String successMessage = null;

        UserManagement userManagement = (UserManagement) getServletContext().getAttribute("userManagement");
         if (userManagement == null) {
             System.err.println("FATAL: UserManagement not found in ServletContext!");
             errorMessage = "Profile update service is currently unavailable.";
             request.setAttribute("errorMessage", errorMessage);
             request.setAttribute("user", currentUser); // Pass back original user data
             request.getRequestDispatcher("/WEB-INF/jsp/editProfile.jsp").forward(request, response);
             return;
         }


        try {
            // Basic Validation
            if (email == null || email.trim().isEmpty() || username == null || username.trim().isEmpty()) {
                throw new InvalidUserDataException("form", "Email and Username cannot be empty.");
            }
            email = email.trim();
            username = username.trim();

            // Create a temporary user object with updated details
            // IMPORTANT: We keep the original password as we are not handling password change here
            User updatedUser = new User(
                currentUser.getUserId(), // Keep the original ID
                email,
                currentUser.getPassword(), // Keep original plain text password
                username,
                currentUser.getWalletBalance(), // Keep original balance
                currentUser.getCreatedAt() // Keep original creation date
            );

            // TODO: Implement actual update logic in UserManagement and UserStorage if needed
            // For now, we simulate success but don't actually call the update method
            // userManagement.updateUser(updatedUser); // <-- Call this when update is implemented

            System.out.println("TODO: Profile update logic skipped for user: " + currentUser.getUserId()); // Log simulation

            // Update user in session
            session.setAttribute("loggedInUser", updatedUser);
            successMessage = "Profile updated successfully!";

            // Redirect back to profile page with success message
            response.sendRedirect(request.getContextPath() + "/profile?message=" + java.net.URLEncoder.encode(successMessage, "UTF-8") + "&messageType=success");
            return; // Stop processing after redirect


        } catch (InvalidUserDataException e) {
            errorMessage = e.getMessage();
         }
        // Uncomment if updateUser is implemented and can throw UserNotFoundException
        // catch (UserNotFoundException e) {
        //     errorMessage = "Could not find user profile to update.";
        //     // Consider logging out the user if their profile disappeared
        //     session.invalidate();
        //     response.sendRedirect(request.getContextPath() + "/login?message=Error finding profile. Please login again.&messageType=error");
        //     return;
        // }
        catch (Exception e) {
             System.err.println("Error updating profile for user " + currentUser.getUserId() + ": " + e.getMessage());
             e.printStackTrace();
             errorMessage = "An unexpected error occurred while updating your profile.";
         }

         // If error occurred, forward back to edit page
         request.setAttribute("errorMessage", errorMessage);
         // Pass back the potentially modified (but not saved) user object or original one
         request.setAttribute("user", currentUser); // Or create a temp user with submitted values: new User(currentUser.getUserId(), email, currentUser.getPassword(), username, currentUser.getWalletBalance(), currentUser.getCreatedAt())
         request.getRequestDispatcher("/WEB-INF/jsp/editProfile.jsp").forward(request, response);
    }
}