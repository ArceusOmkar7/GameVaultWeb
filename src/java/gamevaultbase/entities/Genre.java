package gamevaultbase.entities;

/**
 * Entity class representing a game genre
 */
public class Genre {
    private int genreId;
    private String name;

    public Genre() {
        // Default constructor
    }

    public Genre(String name) {
        this.name = name;
    }

    public Genre(int genreId, String name) {
        this.genreId = genreId;
        this.name = name;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
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

        Genre genre = (Genre) obj;
        return name.equalsIgnoreCase(genre.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
}