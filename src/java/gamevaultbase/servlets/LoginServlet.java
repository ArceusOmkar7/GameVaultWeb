package gamevaultbase.servlets;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.UserNotFoundException;
import gamevaultbase.management.UserManagement;
import java.io.IOException;
import javax.servlet.ServletException;
// Import annotation if using, otherwise web.xml handles mapping
// import javax.servlet.annotation.WebServlet; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Mapped in web.xml as /login
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show the login page
        // Check if user is already logged in, maybe redirect to games?
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            response.sendRedirect(request.getContextPath() + "/games");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password"); // Plain password
        String errorMessage = null;

        // Basic validation
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            errorMessage = "Email and password are required.";
        } else {
            // Get UserManagement from ServletContext
            UserManagement userManagement = (UserManagement) getServletContext().getAttribute("userManagement");
            if (userManagement == null) {
                 System.err.println("FATAL: UserManagement not found in ServletContext!");
                 errorMessage = "Application error: User service not available.";
                 request.setAttribute("errorMessage", errorMessage);
                 request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response); // Show generic error
                 return;
            }

            try {
                // Attempt login using plain text password (INSECURE)
                User user = userManagement.login(email.trim(), password); // login checks plain password

                // Login successful: Store user in session
                HttpSession session = request.getSession(); // Create session if not exists
                session.setAttribute("loggedInUser", user);
                session.setMaxInactiveInterval(30 * 60); // Set session timeout (e.g., 30 minutes)

                // Redirect to games page
                 System.out.println("Login successful for: " + user.getEmail());
                 response.sendRedirect(request.getContextPath() + "/games");
                return; // Important to return after redirect

            } catch (UserNotFoundException e) {
                errorMessage = e.getMessage(); // "Invalid email or password"
            } catch (Exception e) {
                // Log unexpected errors
                 System.err.println("Login Error: " + e.getMessage());
                 e.printStackTrace(); // Log stack trace to server logs
                 errorMessage = "An internal error occurred. Please try again later.";
            }
        }

        // If login fails or error occurs
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("email", email); // Keep email in form for user convenience
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
}