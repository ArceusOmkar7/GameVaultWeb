package gamevaultbase.servlets;

import gamevaultbase.entities.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Mapped in web.xml as /profile
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=Please login to view your profile.&messageType=error");
            return;
        }

        // The user object is already in the session, no need to fetch again unless updated
        // If profile edit was implemented, you'd fetch the latest user data here
        // UserManagement userManagement = (UserManagement) getServletContext().getAttribute("userManagement");
        // try {
        //     currentUser = userManagement.getUser(currentUser.getUserId());
        //     session.setAttribute("loggedInUser", currentUser); // Update session if needed
        // } catch (UserNotFoundException e) {
        //     // Handle error, maybe invalidate session
        // }

        // Pass messages if any (e.g., from profile update)
        String message = request.getParameter("message");
        String messageType = request.getParameter("messageType");
        if (message != null) {
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType);
        }

        // Forward to the JSP
        request.setAttribute("user", currentUser); // Pass user object for clarity in JSP
        request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
    }

    // POST might be used for profile updates in the future
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO: Implement profile update logic if adding Edit Profile form
        doGet(req, resp); // For now, just show the profile
    }
}