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

// Mapped in web.xml as /login
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show the login page
        // Check if user is already logged in, redirect to home if so
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userType = request.getParameter("userType");
        String errorMessage = null;

        // Validate user type
        if (userType == null || userType.trim().isEmpty()) {
            errorMessage = "Please select a valid login type.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        try {
            // Create a user based on the selected type
            User user = null;
            if ("admin".equals(userType)) {
                // Create admin user
                user = new User("admin@gamevault.com", "admin", "Admin", 1000.0f);
                user.setUserId(1); // Set a fixed ID for admin
                user.setIsAdmin(true);
            } else {
                // Create regular user
                user = new User("user@gamevault.com", "user", "User", 500.0f);
                user.setUserId(2); // Set a fixed ID for regular user
                user.setIsAdmin(false);
            }

            // Login successful: Store user in session
            HttpSession session = request.getSession(); // Create session if not exists
            session.setAttribute("loggedInUser", user);
            session.setMaxInactiveInterval(60 * 60); // Set session timeout to 1 hour

            // Redirect to home or admin dashboard
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
            return;

        } catch (Exception e) {
            // Log unexpected errors
            System.err.println("Login Error: " + e.getMessage());
            e.printStackTrace();
            errorMessage = "An internal error occurred. Please try again later.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
}