package gamevaultbase.servlets;

import gamevaultbase.entities.User;
import gamevaultbase.servlets.base.UserBaseServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Mapped in web.xml as /profile
public class ProfileServlet extends UserBaseServlet {

    @Override
    protected void processUserGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the current user from the session
        User currentUser = getLoggedInUser(request);

        // The user object is already in the session, no need to fetch again unless
        // updated
        // If profile edit was implemented, you'd fetch the latest user data here
        // try {
        // currentUser = getUserManagement().getUser(currentUser.getUserId());
        // request.getSession().setAttribute("loggedInUser", currentUser); // Update
        // session if needed
        // } catch (UserNotFoundException e) {
        // // Handle error
        // }

        // Pass user object for clarity in JSP
        request.setAttribute("user", currentUser);

        // Forward to the JSP
        forwardToJsp(request, response, "/WEB-INF/jsp/profile.jsp");
    }

    @Override
    protected void processUserPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Implement profile update logic if adding Edit Profile form
        // For now, just redirect back to the profile page
        redirectTo(request, response, "/profile");
    }
}