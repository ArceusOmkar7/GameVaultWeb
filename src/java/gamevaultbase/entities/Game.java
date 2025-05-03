package gamevaultbase.entities;

import java.util.Date;

public class Game {
    private int gameId;
    private String title;
    private String description;
    private String developer;
    private String platform;
    private float price;
    private Date releaseDate;
    private String imagePath; // New field for storing game image path

    public Game(String title, String description, String developer, String platform, float price, Date releaseDate) {
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.platform = platform;
        this.price = price;
        this.releaseDate = releaseDate;
        this.imagePath = null; // Default to null (no image)
    }

    public Game(int gameId, String title, String description, String developer, String platform, float price,
            Date releaseDate) {
        this.gameId = gameId;
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.platform = platform;
        this.price = price;
        this.releaseDate = releaseDate;
        this.imagePath = null; // Default to null (no image)
    }

    public Game(int gameId, String title, String description, String developer, String platform, float price,
            Date releaseDate, String imagePath) {
        this.gameId = gameId;
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.platform = platform;
        this.price = price;
        this.releaseDate = releaseDate;
        this.imagePath = imagePath;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Helper method to get a default image path if none is set
    public String getImagePathOrDefault() {
        return (imagePath != null && !imagePath.isEmpty()) ? imagePath : "game_images/default_game.png";
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", developer='" + developer + '\'' +
                ", platform='" + platform + '\'' +
                ", price=" + price +
                ", releaseDate=" + releaseDate +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
