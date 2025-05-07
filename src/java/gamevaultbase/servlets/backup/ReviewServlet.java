package gamevaultbase.servlets;

import gamevaultbase.entities.Review;
import gamevaultbase.entities.User;
import gamevaultbase.management.ReviewManagement;
import gamevaultbase.servlets.base.PublicBaseServlet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ReviewServlet", urlPatterns = { "/reviews" })
public class ReviewServlet extends PublicBaseServlet {

    /**
     * Processes HTTP GET requests - displays reviews for a specific game.
     */
    @Override
    protected void processPublicGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String gameIdParam = request.getParameter("gameId");
        String errorMessage = null;

        // Validate gameId parameter
        if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            errorMessage = "No game ID provided";
            request.setAttribute("errorMessage", errorMessage);
            forwardToJsp(request, response, "/WEB-INF/jsp/notfound.jsp");
            return;
        }

        // Retrieve ReviewManagement from context
        ReviewManagement reviewManagement = (ReviewManagement) getServletContext().getAttribute("reviewManagement");
        if (reviewManagement == null) {
            System.err.println("FATAL: ReviewManagement not found in ServletContext!");
            errorMessage = "Review service is currently unavailable.";
            request.setAttribute("errorMessage", errorMessage);
            forwardToJsp(request, response, "/WEB-INF/jsp/error.jsp");
            return;
        }

        try {
            // Parse gameId and get reviews
            int gameId = Integer.parseInt(gameIdParam.trim());
            List<Review> reviews = reviewManagement.getReviewsForGame(gameId);

            // Set reviews in request attribute
            request.setAttribute("reviews", reviews);

            // Forward to the game detail page with the reviews
            request.getRequestDispatcher("/game?id=" + gameId).forward(request, response);

        } catch (NumberFormatException e) {
            errorMessage = "Invalid game ID format";
            request.setAttribute("errorMessage", errorMessage);
            forwardToJsp(request, response, "/WEB-INF/jsp/notfound.jsp");
        } catch (Exception e) {
            System.err.println("Error retrieving reviews: " + e.getMessage());
            e.printStackTrace();

            errorMessage = "An error occurred while retrieving reviews";
            request.setAttribute("errorMessage", errorMessage);
            forwardToJsp(request, response, "/WEB-INF/jsp/error.jsp");
        }
    }

    /**
     * Processes HTTP POST requests - submits a new review for a game.
     */
    @Override
    protected void processPublicPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String gameIdParam = request.getParameter("gameId");
        String comment = request.getParameter("comment");
        String message = null;
        String messageType = "error"; // Default to error styling

        // Check if user is logged in
        User currentUser = getLoggedInUser(request);

        if (currentUser == null) {
            redirectWithMessage(request, response, "/login",
                    "Please login to submit a review", "error");
            return;
        }

        // Validate required parameters
        if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            message = "No game ID provided";
        } else if (comment == null || comment.trim().isEmpty()) {
            message = "Review comment cannot be empty";
        } else {
            // Get ReviewManagement
            ReviewManagement reviewManagement = (ReviewManagement) getServletContext().getAttribute("reviewManagement");

            if (reviewManagement == null) {
                System.err.println("FATAL: ReviewManagement not found in ServletContext!");
                message = "Review service is currently unavailable";
            } else {
                try {
                    // Parse gameId and create review
                    int gameId = Integer.parseInt(gameIdParam.trim());
                    Review review = new Review(gameId, currentUser.getUserId(), comment.trim());

                    // Save the review
                    reviewManagement.addReview(review);

                    message = "Review submitted successfully";
                    messageType = "success";

                } catch (NumberFormatException e) {
                    message = "Invalid game ID format";
                } catch (IllegalArgumentException e) {
                    // Validation error from ReviewManagement
                    message = e.getMessage();
                } catch (SQLException e) {
                    System.err.println("SQL Error submitting review: " + e.getMessage());
                    e.printStackTrace();

                    // Check for specific error types
                    if (e.getMessage().contains("duplicate") || e.getMessage().contains("UNIQUE")) {
                        message = "You have already submitted a review for this game";
                    } else {
                        message = "Database error occurred while submitting your review";
                    }
                } catch (Exception e) {
                    System.err.println("Error submitting review: " + e.getMessage());
                    e.printStackTrace();
                    message = "An unexpected error occurred while submitting your review";
                }
            }
        }

        // Redirect back to game detail with message
        redirectWithMessage(request, response, "/game?id=" + gameIdParam, message, messageType);
    }
}