// src/java/gamevaultbase/entities/Review.java
package gamevaultbase.entities;

import java.util.Date;

public class Review {
    private int reviewId;
    private int gameId;
    private int userId;
    // private int rating; // Optional
    private String comment;
    private Date reviewDate;

    // Add relationship fields if needed (populated manually or via JOINs)
    private String username; // To display who wrote the review
    private String gameTitle; // Context

    public Review() {}

    public Review(int gameId, int userId, String comment) {
        this.gameId = gameId;
        this.userId = userId;
        this.comment = comment;
    }

    // Getters and Setters for all fields (reviewId, gameId, userId, comment, reviewDate, username, gameTitle)

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }
    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Date getReviewDate() { return reviewDate; }
    public void setReviewDate(Date reviewDate) { this.reviewDate = reviewDate; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getGameTitle() { return gameTitle; }
    public void setGameTitle(String gameTitle) { this.gameTitle = gameTitle; }

    @Override
    public String toString() {
        return "Review{" +
               "reviewId=" + reviewId +
               ", gameId=" + gameId +
               ", userId=" + userId +
               ", comment='" + comment + '\'' +
               ", reviewDate=" + reviewDate +
               ", username='" + username + '\'' +
               '}';
    }
}