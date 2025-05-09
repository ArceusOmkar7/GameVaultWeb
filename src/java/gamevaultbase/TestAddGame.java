package gamevaultbase;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Genre;
import gamevaultbase.entities.Platform;
import gamevaultbase.storage.GameStorage;

import java.util.Date;

/**
 * Test utility to directly add a game to the database
 * This bypasses the servlet and form submission to test if the database
 * functionality works
 */
public class TestAddGame {

    public static void main(String[] args) {
        try {
            System.out.println("Starting test to add a game directly to database...");

            // Create a test game
            Game game = new Game();
            game.setTitle("Test Game " + System.currentTimeMillis()); // Unique title
            game.setDescription("This is a test game for database testing");
            game.setDeveloper("Test Developer");
            game.setPrice(19.99f);
            game.setRating(4.5f);
            game.setReleaseDate(new Date());
            game.setImagePath("game_images/default_game.png");

            // Add platforms
            Platform platform1 = new Platform("PC");
            Platform platform2 = new Platform("PlayStation 5");
            game.addPlatform(platform1);
            game.addPlatform(platform2);
            game.setPlatform("PC, PlayStation 5"); // Also set the string representation

            // Add genres
            Genre genre1 = new Genre("Action");
            Genre genre2 = new Genre("Adventure");
            game.addGenre(genre1);
            game.addGenre(genre2);
            game.setGenre("Action, Adventure"); // Also set the string representation

            // Print game details
            System.out.println("Game to be added:");
            System.out.println("Title: " + game.getTitle());
            System.out.println("Description: " + game.getDescription());
            System.out.println("Developer: " + game.getDeveloper());
            System.out.println("Platform: " + game.getPlatform());
            System.out.println("Genre: " + game.getGenre());
            System.out.println("Price: " + game.getPrice());
            System.out.println("Rating: " + game.getRating());
            System.out.println("ReleaseDate: " + game.getReleaseDate());

            // Save to database
            GameStorage gameStorage = new GameStorage();
            gameStorage.save(game);

            if (game.getGameId() > 0) {
                System.out.println("SUCCESS: Game added successfully with ID: " + game.getGameId());
            } else {
                System.out.println("FAILURE: Game could not be added to the database");
            }

        } catch (Exception e) {
            System.err.println("ERROR: Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
