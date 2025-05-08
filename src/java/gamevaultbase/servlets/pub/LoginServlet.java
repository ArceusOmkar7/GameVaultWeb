package gamevaultbase.servlets.pub;

import gamevaultbase.entities.User;
import gamevaultbase.exceptions.UserNotFoundException;
import gamevaultbase.management.UserManagement;
import gamevaultbase.servlets.base.PublicBaseServlet;
import gamevaultbase.helpers.DBUtil;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

// Mapped in web.xml as /login
public class LoginServlet extends PublicBaseServlet {

    @Override
    protected void processPublicGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is already logged in, redirect to home if so
        if (isLoggedIn(request)) {
            redirectTo(request, response, "/home");
            return;
        }
        forwardToJsp(request, response, "/WEB-INF/jsp/login.jsp");
    }

    @Override
    protected void processPublicPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userType = request.getParameter("userType");

        // Validate user type
        if (userType == null || userType.trim().isEmpty()) {
            setErrorMessage(request, "Please select a valid login type.");
            forwardToJsp(request, response, "/WEB-INF/jsp/login.jsp");
            return;
        }

        try {
            // Ensure both demo users exist in the database
            ensureDemoUsersExist();

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
            // Also set the isAdmin attribute directly for servlets that check it this way
            session.setAttribute("isAdmin", user.isAdmin());
            session.setMaxInactiveInterval(60 * 60); // Set session timeout to 1 hour

            // Redirect to home or admin dashboard
            if (user.isAdmin()) {
                redirectTo(request, response, "/admin/dashboard");
            } else {
                redirectTo(request, response, "/home");
            }

        } catch (Exception e) {
            // Log unexpected errors
            System.err.println("Login Error: " + e.getMessage());
            e.printStackTrace();
            setErrorMessage(request, "An internal error occurred. Please try again later.");
            forwardToJsp(request, response, "/WEB-INF/jsp/login.jsp");
        }
    }

    // Helper method to ensure both demo users exist in the database
    private void ensureDemoUsersExist() throws SQLException {
        // Create admin user (ID 1)
        createOrUpdateDemoUser(1, "admin", "admin@gamevault.com", 1000.0f, true);

        // Create regular user (ID 2)
        createOrUpdateDemoUser(2, "user", "user@gamevault.com", 500.0f, false);
    }

    private void createOrUpdateDemoUser(int userId, String username, String email,
            float balance, boolean isAdmin) throws SQLException {
        // Check if user already exists by ID
        String checkSql = "SELECT COUNT(*) AS userCount FROM Users WHERE userId = ?";
        List<Integer> results = DBUtil.executeQuery(checkSql, rs -> rs.getInt("userCount"), userId);
        if (!results.isEmpty() && results.get(0) > 0) {
            // User already exists, update it to ensure correct data
            String updateSql = "UPDATE Users SET username = ?, email = ?, " +
                    "walletBalance = ?, isAdmin = ? WHERE userId = ?";
            DBUtil.executeUpdate(updateSql, username, email, balance, isAdmin ? 1 : 0, userId);
            System.out.println("Updated demo user: " + username + " with ID " + userId);
            return;
        }

        // Check if user already exists by username (to avoid unique constraint
        // violations)
        checkSql = "SELECT COUNT(*) AS userCount FROM Users WHERE username = ?";
        results = DBUtil.executeQuery(checkSql, rs -> rs.getInt("userCount"), username);
        if (!results.isEmpty() && results.get(0) > 0) {
            // User with this username already exists, but has a different ID
            // Update the existing user's ID to match the expected ID
            String updateSql = "UPDATE Users SET userId = ?, email = ?, " +
                    "walletBalance = ?, isAdmin = ? WHERE username = ?";
            DBUtil.executeUpdate(updateSql, userId, email, balance, isAdmin ? 1 : 0, username);
            System.out.println("Updated existing user to use demo ID: " + username + " with ID " + userId);
            return;
        }

        // Insert the demo user - note we must include password since it's NOT NULL
        String sql = "INSERT INTO Users (userId, username, email, password, walletBalance, isAdmin) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        DBUtil.executeUpdate(sql, userId, username, email, username + "123", balance, isAdmin ? 1 : 0);
        System.out.println("Created demo user: " + username + " with ID " + userId);
    }
}
