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

    // Find a review by its ID
    public Review findById(Integer reviewId) {
        String sql = "SELECT r.reviewId, r.gameId, r.userId, r.comment, r.reviewDate, u.username " +
                     "FROM Reviews r JOIN Users u ON r.userId = u.userId " +
                     "WHERE r.reviewId = ?";
        try {
            List<Review> reviews = DBUtil.executeQuery(sql, rs -> mapResultSetToReviewWithUser(rs), reviewId);
            return reviews.isEmpty() ? null : reviews.get(0);
        } catch (SQLException | IOException e) {
            System.err.println("Error finding review by ID " + reviewId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Review> findAll() {
        String sql = "SELECT r.reviewId, r.gameId, r.userId, r.comment, r.reviewDate, u.username " +
                     "FROM Reviews r JOIN Users u ON r.userId = u.userId " +
                     "ORDER BY r.reviewDate DESC";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToReviewWithUser(rs));
        } catch (SQLException | IOException e) {
            System.err.println("Error finding all reviews: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void update(Review review) throws SQLException {
        String sql = "UPDATE Reviews SET comment = ? WHERE reviewId = ?";
        try {
            int rowsAffected = DBUtil.executeUpdate(sql, review.getComment(), review.getReviewId());
            if (rowsAffected == 0) {
                System.err.println("WARN: Update affected 0 rows for reviewId: " + review.getReviewId());
                // This could mean the reviewId didn't exist
                throw new SQLException("Review with ID " + review.getReviewId() + " not found or not updated");
            }
        } catch (IOException e) {
            System.err.println("IO Error updating review: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("IO Error updating review", e);
        }
    }

    public void delete(Integer reviewId) throws SQLException {
        String sql = "DELETE FROM Reviews WHERE reviewId = ?";
        try {
            int rowsAffected = DBUtil.executeUpdate(sql, reviewId);
            if (rowsAffected == 0) {
                System.err.println("WARN: Delete affected 0 rows for reviewId: " + reviewId);
                // This could mean the reviewId didn't exist
                throw new SQLException("Review with ID " + reviewId + " not found or not deleted");
            }
        } catch (IOException e) {
            System.err.println("Error deleting review: " + reviewId + " - " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("IO Error deleting review", e);
        }
    }
}