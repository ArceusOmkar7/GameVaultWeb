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
     * 
     * @param gameId The ID of the game.
     * @return A list of Review objects, or an empty list if none are found or an
     *         error occurs.
     */
    public List<Review> getReviewsForGame(int gameId) {
        // Delegates to the storage method that performs the JOIN with user details
        return reviewStorage.findByGameId(gameId);
    }

    /**
     * Adds a new review to the database.
     * 
     * @param review The Review object to save. Must contain gameId, userId, and
     *               comment.
     * @throws IllegalArgumentException If the review data is invalid (e.g., empty
     *                                  comment).
     * @throws SQLException             If a database error occurs during saving.
     * @throws RuntimeException         If an unexpected IO error occurs in the
     *                                  storage layer.
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

        // The save method in storage might throw SQLException or RuntimeException (for
        // IO)
        reviewStorage.save(review);
    }

    /**
     * Deletes a review if the current user is authorized (owner of the review).
     * 
     * @param reviewId      The ID of the review to delete
     * @param currentUserId The ID of the user attempting to delete the review
     * @throws UnauthorizedException If the current user is not authorized to delete
     *                               this review
     * @throws SQLException          If a database error occurs
     */
    public void deleteReview(int reviewId, int currentUserId) throws IllegalArgumentException, SQLException {
        // 1. Fetch the review by reviewId
        Review review = reviewStorage.findById(reviewId);

        // 2. Check if review exists and if user is authorized
        if (review == null) {
            throw new IllegalArgumentException("Review with ID " + reviewId + " not found");
        }

        if (review.getUserId() != currentUserId) {
            throw new IllegalArgumentException("You are not authorized to delete this review");
        }

        // 3. If authorized, delete the review
        reviewStorage.delete(reviewId);
    }

    /**
     * Updates an existing review if the current user is authorized (owner of the
     * review).
     * 
     * @param review        The updated review object
     * @param currentUserId The ID of the user attempting to update the review
     * @throws IllegalArgumentException If review data is invalid or user is
     *                                  unauthorized
     * @throws SQLException             If a database error occurs
     */
    public void updateReview(Review review, int currentUserId) throws IllegalArgumentException, SQLException {
        if (review == null || review.getReviewId() <= 0) {
            throw new IllegalArgumentException("Invalid review data for update");
        }

        // 1. Check that the current user owns this review
        Review existingReview = reviewStorage.findById(review.getReviewId());
        if (existingReview == null) {
            throw new IllegalArgumentException("Review with ID " + review.getReviewId() + " not found");
        }

        if (existingReview.getUserId() != currentUserId) {
            throw new IllegalArgumentException("You are not authorized to update this review");
        }

        // 2. Validate the updated data
        if (review.getComment() == null || review.getComment().trim().isEmpty()) {
            throw new IllegalArgumentException("Review comment cannot be empty");
        }

        // 3. Update the review
        reviewStorage.update(review);
    }
}