package gamevaultbase.helpers;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.Genre;
import gamevaultbase.entities.Platform;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for parsing JSON data using the javax.json API.
 */
public class JSONUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Parses a list of games from a JSON input stream.
     * 
     * @param inputStream The input stream containing the JSON data
     * @return A list of Game objects
     */
    public static List<Game> parseGamesFromJson(InputStream inputStream) {
        List<Game> games = new ArrayList<>();

        try (JsonReader reader = Json.createReader(inputStream)) {
            JsonArray gamesArray = reader.readArray();

            for (int i = 0; i < gamesArray.size(); i++) {
                try {
                    JsonObject gameObj = gamesArray.getJsonObject(i);
                    Game game = parseGameObject(gameObj);
                    games.add(game);
                } catch (Exception e) {
                    System.err.println("Error parsing game at index " + i + ": " + e.getMessage());
                }
            }
        }

        return games;
    }

    /**
     * Parses a single game object from JSON.
     */
    private static Game parseGameObject(JsonObject gameObj) {
        Game game = new Game();

        // Parse basic string fields
        game.setTitle(getStringValue(gameObj, "title"));
        game.setDescription(getStringValue(gameObj, "description"));
        game.setDeveloper(getStringValue(gameObj, "developer"));
        game.setImagePath(getStringValue(gameObj, "imagePath"));

        // Parse price
        if (gameObj.containsKey("price")) {
            try {
                game.setPrice((float) gameObj.getJsonNumber("price").doubleValue());
            } catch (Exception e) {
                // Handle case where price might be a string
                String priceStr = getStringValue(gameObj, "price");
                if (priceStr != null && !priceStr.isEmpty()) {
                    try {
                        game.setPrice(Float.parseFloat(priceStr));
                    } catch (NumberFormatException ex) {
                        game.setPrice(0.0f);
                    }
                }
            }
        }

        // Parse rating
        if (gameObj.containsKey("rating")) {
            try {
                game.setRating((float) gameObj.getJsonNumber("rating").doubleValue());
            } catch (Exception e) {
                // Handle case where rating might be a string
                String ratingStr = getStringValue(gameObj, "rating");
                if (ratingStr != null && !ratingStr.isEmpty()) {
                    try {
                        game.setRating(Float.parseFloat(ratingStr));
                    } catch (NumberFormatException ex) {
                        game.setRating(0.0f);
                    }
                }
            }
        }

        // Parse release date
        String releaseDateStr = getStringValue(gameObj, "releaseDate");
        game.setReleaseDate(parseReleaseDate(releaseDateStr));

        // Parse platforms as array and create Platform objects
        if (gameObj.containsKey("platform")) {
            JsonValue platformValue = gameObj.get("platform");
            if (platformValue.getValueType() == JsonValue.ValueType.ARRAY) {
                JsonArray platformArray = gameObj.getJsonArray("platform");
                String platforms = jsonArrayToString(platformArray);
                game.setPlatform(platforms);

                // Create Platform objects
                List<Platform> platformObjects = new ArrayList<>();
                for (JsonValue value : platformArray) {
                    if (value.getValueType() == JsonValue.ValueType.STRING) {
                        String platformName = ((JsonString) value).getString().trim();
                        if (!platformName.isEmpty()) {
                            platformObjects.add(new Platform(platformName));
                        }
                    }
                }
                game.setPlatforms(platformObjects);
            } else if (platformValue.getValueType() == JsonValue.ValueType.STRING) {
                String platformStr = ((JsonString) platformValue).getString();
                game.setPlatform(platformStr);

                // Create Platform objects
                String[] platformArray = platformStr.split(",");
                List<Platform> platformObjects = new ArrayList<>();
                for (String platformName : platformArray) {
                    platformName = platformName.trim();
                    if (!platformName.isEmpty()) {
                        platformObjects.add(new Platform(platformName));
                    }
                }
                game.setPlatforms(platformObjects);
            }
        }

        // Parse genre as array and create Genre objects
        if (gameObj.containsKey("genre")) {
            JsonValue genreValue = gameObj.get("genre");
            if (genreValue.getValueType() == JsonValue.ValueType.ARRAY) {
                JsonArray genreArray = gameObj.getJsonArray("genre");
                String genres = jsonArrayToString(genreArray);
                game.setGenre(genres);

                // Create Genre objects
                List<Genre> genreObjects = new ArrayList<>();
                for (JsonValue value : genreArray) {
                    if (value.getValueType() == JsonValue.ValueType.STRING) {
                        String genreName = ((JsonString) value).getString().trim();
                        if (!genreName.isEmpty()) {
                            genreObjects.add(new Genre(genreName));
                        }
                    }
                }
                game.setGenres(genreObjects);
            } else if (genreValue.getValueType() == JsonValue.ValueType.STRING) {
                String genreStr = ((JsonString) genreValue).getString();
                game.setGenre(genreStr);

                // Create Genre objects
                String[] genreArray = genreStr.split(",");
                List<Genre> genreObjects = new ArrayList<>();
                for (String genreName : genreArray) {
                    genreName = genreName.trim();
                    if (!genreName.isEmpty()) {
                        genreObjects.add(new Genre(genreName));
                    }
                }
                game.setGenres(genreObjects);
            }
        }

        return game;
    }

    /**
     * Converts a JsonArray to a comma-separated string.
     */
    private static String jsonArrayToString(JsonArray array) {
        return array.getValuesAs(JsonString.class).stream()
                .map(JsonString::getString)
                .collect(Collectors.joining(", "));
    }

    /**
     * Gets a string value from a JSON object.
     */
    private static String getStringValue(JsonObject obj, String key) {
        if (obj.containsKey(key) && obj.get(key).getValueType() == JsonValue.ValueType.STRING) {
            return obj.getString(key);
        }
        return "";
    }

    /**
     * Parses a release date from a string.
     */
    private static Date parseReleaseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }

        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}