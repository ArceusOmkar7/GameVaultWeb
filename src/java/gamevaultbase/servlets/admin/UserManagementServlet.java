package gamevaultbase.servlets.admin;

import gamevaultbase.entities.User;
import gamevaultbase.storage.UserStorage;
import gamevaultbase.servlets.base.AdminBaseServlet;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet("/admin/users/*")
public class UserManagementServlet extends AdminBaseServlet {
    private UserStorage userStorage;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        userStorage = new UserStorage();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // List all users
            List<User> users = userStorage.findAll();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/jsp/adminUsers.jsp").forward(request, response);
        } else {
            // Get specific user
            try {
                int userId = Integer.parseInt(pathInfo.substring(1));
                User user = userStorage.findById(userId);
                if (user != null) {
                    response.setContentType("application/json");
                    response.getWriter().write(objectMapper.writeValueAsString(user));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void processAdminPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = objectMapper.readValue(request.getReader(), User.class);

            // Validate required fields
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is required");
                return;
            }
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email is required");
                return;
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password is required");
                return;
            }

            // Trim fields
            user.setUsername(user.getUsername().trim());
            user.setEmail(user.getEmail().trim());
            user.setPassword(user.getPassword().trim());

            // Set creation date
            user.setCreatedAt(new Date());

            // Save user
            userStorage.save(user);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int userId = Integer.parseInt(pathInfo.substring(1));
            User user = objectMapper.readValue(request.getReader(), User.class);

            // Check if trying to edit default users
            User existingUser = userStorage.findById(userId);
            if (existingUser != null &&
                    (existingUser.getEmail().equals("admin@gamevault.com") ||
                            existingUser.getEmail().equals("user@gamevault.com"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Cannot edit default users");
                return;
            }

            user.setUserId(userId);
            userStorage.update(user);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int userId = Integer.parseInt(pathInfo.substring(1));

            // Check if trying to delete default users
            User user = userStorage.findById(userId);
            if (user != null &&
                    (user.getEmail().equals("admin@gamevault.com") ||
                            user.getEmail().equals("user@gamevault.com"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Cannot delete default users");
                return;
            }

            userStorage.delete(userId);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}