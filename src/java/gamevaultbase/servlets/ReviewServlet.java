package gamevaultbase.servlets;

import gamevaultbase.entities.Review;
import gamevaultbase.entities.User;
import gamevaultbase.management.ReviewManagement;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Mapped in web.xml as /reviews
public class ReviewServlet extends HttpServlet {

    /**
     * Handles HTTP GET requests - displays reviews for a specific game.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String gameIdParam = request.getParameter("gameId");
        String errorMessage = null;
        
        // Validate gameId parameter
        if (gameIdParam == null || gameIdParam.trim().isEmpty()) {
            errorMessage = "No game ID provided";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/jsp/notfound.jsp").forward(request, response);
            return;
        }
        
        // Retrieve ReviewManagement from context
        ReviewManagement reviewManagement = (ReviewManagement) getServletContext().getAttribute("reviewManagement");
        if (reviewManagement == null) {
            System.err.println("FATAL: ReviewManagement not found in ServletContext!");
            errorMessage = "Review service is currently unavailable.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
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
            request.getRequestDispatcher("/WEB-INF/jsp/notfound.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error retrieving reviews: " + e.getMessage());
            e.printStackTrace();
            
            errorMessage = "An error occurred while retrieving reviews";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }

    /**
     * Handles HTTP POST requests - submits a new review for a game.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String gameIdParam = request.getParameter("gameId");
        String comment = request.getParameter("comment");
        String message = null;
        String messageType = "error"; // Default to error styling
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=Please login to submit a review&messageType=error");
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
        String redirectUrl = request.getContextPath() + "/game?id=" + gameIdParam;
        redirectUrl += "&message=" + java.net.URLEncoder.encode(message, "UTF-8") 
                    + "&messageType=" + messageType;
        response.sendRedirect(redirectUrl);
    }
    
    @Override
    public String getServletInfo() {
        return "ReviewServlet handles displaying and submitting game reviews";
    }
}