package gamevaultbase.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
// Import annotation if using
// import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Mapped in web.xml as /logout
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Don't create session if it doesn't exist
        if (session != null) {
             Object user = session.getAttribute("loggedInUser");
             System.out.println("Logging out user: " + (user != null ? user : "Unknown"));
             session.invalidate(); // Invalidate the session, removing all attributes
        }
         // Redirect to login page with a logged out message
         String message = "You have been logged out successfully.";
         response.sendRedirect(request.getContextPath() + "/login?message=" + java.net.URLEncoder.encode(message, "UTF-8") + "&messageType=success");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Typically logout is done via GET, but handle POST just in case
        doGet(request, response);
    }
}