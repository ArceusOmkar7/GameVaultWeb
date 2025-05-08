package gamevaultbase.storage;

import gamevaultbase.entities.Review;
import gamevaultbase.interfaces.StorageInterface;
import gamevaultbase.helpers.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReviewStorage implements StorageInterface<Review, Integer> {

    @Override
    public Review findById(Integer reviewId) {
        String sql = "SELECT * FROM Reviews WHERE reviewId = ?";
        try {
            List<Review> reviews = DBUtil.executeQuery(sql, rs -> mapResultSetToReview(rs), reviewId);
            return reviews.isEmpty() ? null : reviews.get(0);
        } catch (SQLException e) {
            System.err.println("Error finding review by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Review> findAll() {
        String sql = "SELECT * FROM Reviews ORDER BY reviewDate DESC";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToReview(rs));
        } catch (SQLException e) {
            System.err.println("Error finding all reviews: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void save(Review review) {
        String sql = "INSERT INTO Reviews (gameId, userId, comment, reviewDate) VALUES (?, ?, ?, ?)";
        try {
            int reviewId = DBUtil.executeInsertAndGetKey(sql,
                    review.getGameId(),
                    review.getUserId(),
                    review.getComment(),
                    review.getReviewDate() != null ? new Timestamp(review.getReviewDate().getTime())
                            : new Timestamp(System.currentTimeMillis()));
            if (reviewId != -1) {
                review.setReviewId(reviewId);
            }
        } catch (SQLException e) {
            System.err.println("Error saving review: " + e.getMessage());
        }
    }

    @Override
    public void update(Review review) {
        String sql = "UPDATE Reviews SET comment = ? WHERE reviewId = ?";
        try {
            DBUtil.executeUpdate(sql,
                    review.getComment(),
                    review.getReviewId());
        } catch (SQLException e) {
            System.err.println("Error updating review: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer reviewId) {
        String sql = "DELETE FROM Reviews WHERE reviewId = ?";
        try {
            DBUtil.executeUpdate(sql, reviewId);
        } catch (SQLException e) {
            System.err.println("Error deleting review: " + e.getMessage());
        }
    }

    public List<Review> findByGameId(Integer gameId) {
        String sql = "SELECT r.*, u.username FROM Reviews r JOIN Users u ON r.userId = u.userId WHERE r.gameId = ? ORDER BY r.reviewDate DESC";
        try {
            return DBUtil.executeQuery(sql, rs -> {
                Review review = mapResultSetToReview(rs);
                review.setUsername(rs.getString("username")); // Add username from join query
                return review;
            }, gameId);
        } catch (SQLException e) {
            System.err.println("Error finding reviews by game ID: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Review> findByUserId(Integer userId) {
        String sql = "SELECT * FROM Reviews WHERE userId = ? ORDER BY reviewDate DESC";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToReview(rs), userId);
        } catch (SQLException e) {
            System.err.println("Error finding reviews by user ID: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean hasUserReviewedGame(Integer userId, Integer gameId) {
        String sql = "SELECT COUNT(*) as count FROM Reviews WHERE userId = ? AND gameId = ?";
        try {
            List<Integer> results = DBUtil.executeQuery(sql, rs -> rs.getInt("count"), userId, gameId);
            return !results.isEmpty() && results.get(0) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking if user reviewed game: " + e.getMessage());
            return false;
        }
    }

    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("reviewId"));
        review.setGameId(rs.getInt("gameId"));
        review.setUserId(rs.getInt("userId"));

        // The rating field is commented out in the Review entity
        // review.setRating(rs.getInt("rating"));

        review.setComment(rs.getString("comment"));
        review.setReviewDate(rs.getTimestamp("reviewDate"));
        return review;
    }
}