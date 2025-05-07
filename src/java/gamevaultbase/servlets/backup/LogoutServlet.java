package gamevaultbase.servlets;

import gamevaultbase.servlets.base.PublicBaseServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LogoutServlet", urlPatterns = { "/logout" })
public class LogoutServlet extends PublicBaseServlet {

    @Override
    protected void processPublicGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Don't create session if it doesn't exist
        if (session != null) {
            Object user = session.getAttribute("loggedInUser");
            System.out.println("Logging out user: " + (user != null ? user : "Unknown"));
            session.invalidate(); // Invalidate the session, removing all attributes
        }

        // Redirect to login page with a logged out message
        redirectWithMessage(request, response, "/login",
                "You have been logged out successfully.", "success");
    }

    @Override
    protected void processPublicPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Typically logout is done via GET, but handle POST just in case
        processPublicGetRequest(request, response);
    }
}