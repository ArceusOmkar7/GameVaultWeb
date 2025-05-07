package gamevaultbase.servlets;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.InvalidUserDataException;
import gamevaultbase.servlets.base.PublicBaseServlet;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = { "/register" })
public class RegisterServlet extends PublicBaseServlet {

    /**
     * Processes the HTTP GET method. Displays the registration form.
     */
    @Override
    protected void processPublicGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Just forward to the JSP page to show the form
        forwardToJsp(request, response, "/WEB-INF/jsp/register.jsp");
    }

    /**
     * Processes the HTTP POST method. Handles the registration form submission.
     */
    @Override
    protected void processPublicPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve form parameters
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String balanceStr = request.getParameter("walletBalance");

        String errorMessage = null;
        String successMessage = null; // For redirecting to login

        try {
            // --- Input Validation ---
            if (email == null || email.trim().isEmpty()
                    || username == null || username.trim().isEmpty()
                    || password == null || password.isEmpty()
                    || confirmPassword == null || confirmPassword.isEmpty()) {
                // Use a more specific exception or just set the error message
                errorMessage = "Email, Username, and Password fields are required.";
                throw new Exception(errorMessage); // Use generic exception to jump to catch block
            }

            email = email.trim();
            username = username.trim();

            if (!password.equals(confirmPassword)) {
                errorMessage = "Passwords do not match.";
                throw new Exception(errorMessage);
            }

            // Validate optional balance
            float balance = 0.0f; // Default balance
            if (balanceStr != null && !balanceStr.trim().isEmpty()) {
                try {
                    balance = Float.parseFloat(balanceStr.trim());
                    if (balance < 0) {
                        errorMessage = "Initial balance cannot be negative.";
                        throw new Exception(errorMessage);
                    }
                } catch (NumberFormatException e) {
                    errorMessage = "Invalid format for initial balance.";
                    throw new Exception(errorMessage);
                }
            }

            User newUser = new User(email, password, username, balance);
            newUser.setCreatedAt(new Date());

            // --- Attempt to Add User via Management Layer ---
            // This call might throw InvalidUserDataException for duplicate email/username
            getUserManagement().addUser(newUser);

            // --- Success ---
            successMessage = "Registration successful! Please log in.";
            // Redirect to login page with success message
            redirectWithMessage(request, response, "/login", successMessage, "success");
            return; // IMPORTANT: Return after redirect to stop further processing

        } catch (InvalidUserDataException e) {
            // Specific errors from business logic (e.g., duplicate email/user)
            errorMessage = e.getMessage();
        } catch (Exception e) {
            // Catch validation errors thrown above or other unexpected errors
            if (errorMessage == null) { // Avoid overwriting specific validation messages
                System.err.println("Registration Error: " + e.getMessage());
                e.printStackTrace(); // Log unexpected errors
                errorMessage = "An unexpected error occurred during registration.";
            }
        }

        // --- Error Handling ---
        // If we reached here, an error occurred. Forward back to the registration page.
        request.setAttribute("errorMessage", errorMessage);
        // Preserve entered values (except passwords) to make it user-friendly
        request.setAttribute("email", email);
        request.setAttribute("username", username);
        request.setAttribute("walletBalance", balanceStr); // Pass back the original string
        forwardToJsp(request, response, "/WEB-INF/jsp/register.jsp");
    }
}
