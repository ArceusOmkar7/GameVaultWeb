package gamevaultbase.entities;

/**
 * Entity class representing a game platform
 */
public class Platform {
    private int platformId;
    private String name;

    public Platform() {
        // Default constructor
    }

    public Platform(String name) {
        this.name = name;
    }

    public Platform(int platformId, String name) {
        this.platformId = platformId;
        this.name = name;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Platform platform = (Platform) obj;
        return name.equalsIgnoreCase(platform.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
}