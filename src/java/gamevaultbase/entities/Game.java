package gamevaultbase.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game {
    private int gameId;
    private String title;
    private String description;
    private String developer;
    // We'll keep these strings for backward compatibility during migration
    private String platform;
    private String genre;
    // New fields for the entity relationships
    private List<Platform> platforms;
    private List<Genre> genres;
    private float price;
    private Date releaseDate;
    private String imagePath;
    private float rating;

    public Game() {
        // Default empty constructor
        this.platforms = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public Game(String title, String description, String developer, String platform, float price, Date releaseDate) {
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.platform = platform;
        this.price = price;
        this.releaseDate = releaseDate;
        this.imagePath = null; // Default to null (no image)
        this.platforms = new ArrayList<>();
        this.genres = new ArrayList<>();
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
        this.platforms = new ArrayList<>();
        this.genres = new ArrayList<>();
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
        this.platforms = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public Game(int gameId, String title, String description, String developer, String platform, float price,
            Date releaseDate, String imagePath, String genre) {
        this.gameId = gameId;
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.platform = platform;
        this.price = price;
        this.releaseDate = releaseDate;
        this.imagePath = imagePath;
        this.genre = genre;
        this.rating = 0.0f; // Default rating
        this.platforms = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public Game(int gameId, String title, String description, String developer, String platform, float price,
            Date releaseDate, String imagePath, String genre, float rating) {
        this.gameId = gameId;
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.platform = platform;
        this.price = price;
        this.releaseDate = releaseDate;
        this.imagePath = imagePath;
        this.genre = genre;
        this.rating = rating;
        this.platforms = new ArrayList<>();
        this.genres = new ArrayList<>();
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

    // Methods for the legacy string-based platform field
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    // Methods for the new Platform entity relationships
    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public void addPlatform(Platform platform) {
        if (this.platforms == null) {
            this.platforms = new ArrayList<>();
        }
        if (!this.platforms.contains(platform)) {
            this.platforms.add(platform);
        }
    }

    public void removePlatform(Platform platform) {
        if (this.platforms != null) {
            this.platforms.remove(platform);
        }
    }

    // Methods for the legacy string-based genre field
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    // Methods for the new Genre entity relationships
    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void addGenre(Genre genre) {
        if (this.genres == null) {
            this.genres = new ArrayList<>();
        }
        if (!this.genres.contains(genre)) {
            this.genres.add(genre);
        }
    }

    public void removeGenre(Genre genre) {
        if (this.genres != null) {
            this.genres.remove(genre);
        }
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    // Helper method to get a default image path if none is set
    public String getImagePathOrDefault() {
        return (imagePath != null && !imagePath.isEmpty()) ? imagePath : "game_images/default_game.png";
    }

    /**
     * Adds an image path to this game.
     * Currently replaces any existing image path, but could be modified to support
     * multiple images.
     * 
     * @param imagePath the path to the image file
     */
    public void addImagePath(String imagePath) {
        // For now, we just set the image path
        // In a future version, this could be modified to store multiple images
        this.imagePath = imagePath;
    }

    /**
     * Helper method to extract the first platform from the platform list
     * for display purposes in certain contexts.
     * 
     * @return The first platform or "Multiple" if there are multiple platforms
     */
    public String getPrimaryPlatform() {
        // If we have platform objects, use those first
        if (platforms != null && !platforms.isEmpty()) {
            if (platforms.size() > 1) {
                return platforms.get(0).getName() + " +";
            }
            return platforms.get(0).getName();
        }

        // Fall back to string-based platform if needed
        if (platform == null || platform.isEmpty()) {
            return "Unknown";
        }

        // If comma exists, it's multiple platforms
        if (platform.contains(",")) {
            String firstPlatform = platform.split(",")[0].trim();
            return firstPlatform + " +";
        }

        return platform.trim();
    }

    /**
     * Helper method to get platforms as a list from the string representation
     * (for backward compatibility)
     * 
     * @return List of platform names
     */
    public List<String> getPlatformList() {
        List<String> platformNames = new ArrayList<>();

        // If we have platform objects, use those first
        if (platforms != null && !platforms.isEmpty()) {
            for (Platform p : platforms) {
                platformNames.add(p.getName());
            }
            return platformNames;
        }

        // Fall back to string-based platform if needed
        if (platform == null || platform.isEmpty()) {
            return platformNames;
        }

        String[] platformArray = platform.split(",");
        for (String p : platformArray) {
            platformNames.add(p.trim());
        }

        return platformNames;
    }

    /**
     * Helper method to get genres as a list from the string representation
     * (for backward compatibility)
     * 
     * @return List of genre names
     */
    public List<String> getGenreList() {
        List<String> genreNames = new ArrayList<>();

        // If we have genre objects, use those first
        if (genres != null && !genres.isEmpty()) {
            for (Genre g : genres) {
                genreNames.add(g.getName());
            }
            return genreNames;
        }

        // Fall back to string-based genre if needed
        if (genre == null || genre.isEmpty()) {
            return genreNames;
        }

        String[] genreArray = genre.split(",");
        for (String g : genreArray) {
            genreNames.add(g.trim());
        }

        return genreNames;
    }

    /**
     * Converts platforms and genres to comma-separated strings for
     * backward compatibility with existing code
     */
    public void updateLegacyFields() {
        // Update platform string from platform objects
        if (platforms != null && !platforms.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < platforms.size(); i++) {
                sb.append(platforms.get(i).getName());
                if (i < platforms.size() - 1) {
                    sb.append(", ");
                }
            }
            this.platform = sb.toString();
        }

        // Update genre string from genre objects
        if (genres != null && !genres.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < genres.size(); i++) {
                sb.append(genres.get(i).getName());
                if (i < genres.size() - 1) {
                    sb.append(", ");
                }
            }
            this.genre = sb.toString();
        }
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", developer='" + developer + '\'' +
                ", price=" + price +
                ", releaseDate=" + releaseDate +
                ", imagePath='" + imagePath + '\'' +
                ", rating=" + rating +
                '}';
    }
}
