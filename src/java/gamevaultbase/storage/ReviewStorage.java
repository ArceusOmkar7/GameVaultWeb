package gamevaultbase.storage;

import gamevaultbase.entities.Review;
import gamevaultbase.helpers.DBUtil;
// Potentially implement StorageInterface if you want full CRUD for reviews later
// import gamevaultbase.interfaces.StorageInterface;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReviewStorage { // Could implement StorageInterface<Review, Integer>

    // Find reviews for a specific game, joining with Users table to get username
    public List<Review> findByGameIdWithUserDetails(int gameId) {
        // Join with Users table to get the username for display
        String sql = "SELECT r.reviewId, r.gameId, r.userId, r.comment, r.reviewDate, u.username " +
                     "FROM Reviews r JOIN Users u ON r.userId = u.userId " +
                     "WHERE r.gameId = ? ORDER BY r.reviewDate DESC";
        try {
            // Use the handler that maps both Review and User data
            return DBUtil.executeQuery(sql, rs -> mapResultSetToReviewWithUser(rs), gameId);
        } catch (SQLException | IOException e) {
            System.err.println("Error finding reviews by gameId " + gameId + ": " + e.getMessage());
            e.printStackTrace(); // Log stack trace for debugging
            return new ArrayList<>(); // Return empty list on error
        }
    }

     // Basic save method for a new review
     public void save(Review review) throws SQLException {
        String sql = "INSERT INTO Reviews (gameId, userId, comment, reviewDate) VALUES (?, ?, ?, ?)";
        // Use current time if review date isn't explicitly set
        Timestamp reviewDate = (review.getReviewDate() != null) ? new Timestamp(review.getReviewDate().getTime()) : new Timestamp(System.currentTimeMillis());

        try {
            int generatedId = DBUtil.executeInsertAndGetKey(sql,
                review.getGameId(),
                review.getUserId(),
                review.getComment(),
                reviewDate
            );
             if (generatedId != -1) {
                 review.setReviewId(generatedId); // Set the generated ID back to the object
             } else {
                  System.err.println("WARN: Review insert did not return a generated ID for gameId: " + review.getGameId() + ", userId: " + review.getUserId());
             }
        } catch (IOException e) {
             // Wrap IOException in a RuntimeException or a custom DataAccessException
             System.err.println("IO Error saving review: " + e.getMessage());
             e.printStackTrace();
             throw new RuntimeException("IO Error saving review", e);
        } catch (SQLException e) {
             System.err.println("SQL Error saving review: " + e.getMessage());
             e.printStackTrace();
             throw e; // Re-throw SQLException so the management layer can potentially handle it
        }
    }

    // Maps a ResultSet row to a Review object, including the username from the JOIN
    private Review mapResultSetToReviewWithUser(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("reviewId"));
        review.setGameId(rs.getInt("gameId"));
        review.setUserId(rs.getInt("userId"));
        review.setComment(rs.getString("comment"));
        review.setReviewDate(rs.getTimestamp("reviewDate"));
        // Add the username obtained from the JOIN
        review.setUsername(rs.getString("username"));
        // gameTitle is not directly available here unless joined further
        return review;
    }

    // --- Optional: Add other standard CRUD methods if needed ---
    /*
    public Review findById(Integer reviewId) {
        // Implementation needed
        return null;
    }

    public List<Review> findAll() {
        // Implementation needed
        return new ArrayList<>();
    }

    public void update(Review review) {
        // Implementation needed
    }

    public void delete(Integer reviewId) {
         String sql = "DELETE FROM Reviews WHERE reviewId = ?";
         try {
             DBUtil.executeUpdate(sql, reviewId);
         } catch (SQLException | IOException e) {
             System.err.println("Error deleting review: " + reviewId + " - " + e.getMessage());
             e.printStackTrace();
             // Consider rethrowing
         }
    }
    */
}