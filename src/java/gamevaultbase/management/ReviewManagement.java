package gamevaultbase.management;

import gamevaultbase.entities.Review;
import gamevaultbase.storage.ReviewStorage;
import java.sql.SQLException;
import java.util.List;

public class ReviewManagement {

    private final ReviewStorage reviewStorage;

    public ReviewManagement(ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    /**
     * Gets all reviews for a specific game, including the username of the reviewer.
     * @param gameId The ID of the game.
     * @return A list of Review objects, or an empty list if none are found or an error occurs.
     */
    public List<Review> getReviewsForGame(int gameId) {
        // Delegates directly to the storage method that performs the JOIN
        return reviewStorage.findByGameIdWithUserDetails(gameId);
    }

    /**
     * Adds a new review to the database.
     * @param review The Review object to save. Must contain gameId, userId, and comment.
     * @throws IllegalArgumentException If the review data is invalid (e.g., empty comment).
     * @throws SQLException If a database error occurs during saving.
     * @throws RuntimeException If an unexpected IO error occurs in the storage layer.
     */
    public void addReview(Review review) throws IllegalArgumentException, SQLException {
        if (review == null) {
             throw new IllegalArgumentException("Review object cannot be null.");
        }
        if (review.getGameId() <= 0) {
             throw new IllegalArgumentException("Invalid gameId for review.");
        }
         if (review.getUserId() <= 0) {
             throw new IllegalArgumentException("Invalid userId for review.");
        }
        if (review.getComment() == null || review.getComment().trim().isEmpty()) {
            throw new IllegalArgumentException("Review comment cannot be empty.");
        }
        // Add more validation if needed (e.g., rating range, comment length)

        // The save method in storage might throw SQLException or RuntimeException (for IO)
        reviewStorage.save(review);
    }

    // Add other methods like deleteReview, updateReview if needed,
    // making sure to handle permissions (e.g., only allow user to delete their own review).
    /*
    public void deleteReview(int reviewId, int currentUserId) throws Exception {
        // 1. Fetch the review by reviewId (need findById in ReviewStorage)
        // Review review = reviewStorage.findById(reviewId);
        // 2. Check if review exists and if review.getUserId() matches currentUserId
        // 3. If conditions met, call reviewStorage.delete(reviewId);
        // 4. Handle exceptions (ReviewNotFound, NotAuthorized, SQLException)
    }
    */
}