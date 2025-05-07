package gamevaultbase.helpers;

import gamevaultbase.entities.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing JSON data. Uses native Java functionality without
 * external libraries.
 */
public class JSONUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Parses a list of games from a JSON input stream.
     * 
     * @param inputStream The input stream containing the JSON data
     * @return A list of Game objects
     * @throws IOException If there's an error reading the input stream
     */
    public static List<Game> parseGamesFromJson(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonContent = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonContent.append(line.trim());
        }

        // Extract game objects from JSON array
        return parseGameArray(jsonContent.toString());
    }

    /**
     * Parses a JSON array of games.
     * 
     * @param jsonArray The JSON array string
     * @return A list of Game objects
     */
    private static List<Game> parseGameArray(String jsonArray) {
        List<Game> games = new ArrayList<>();

        // Clean the JSON string (remove comments if any)
        jsonArray = removeJsonComments(jsonArray);

        // Ensure it's an array
        if (!jsonArray.trim().startsWith("[") || !jsonArray.trim().endsWith("]")) {
            throw new IllegalArgumentException("Invalid JSON array format");
        }

        // Extract content between the brackets
        String content = jsonArray.substring(jsonArray.indexOf('[') + 1, jsonArray.lastIndexOf(']'));

        // Split by game objects
        List<String> gameJsonObjects = splitJsonObjects(content);

        // Parse each game object
        for (String gameJson : gameJsonObjects) {
            try {
                Game game = parseGameObject(gameJson);
                if (game != null) {
                    games.add(game);
                }
            } catch (Exception e) {
                System.err.println("Error parsing game: " + e.getMessage() + "\nJSON: " + gameJson);
            }
        }

        return games;
    }

    /**
     * Removes comments from JSON string (comments are not standard in JSON but
     * often included).
     */
    private static String removeJsonComments(String json) {
        // Remove single line comments (// ...)
        Pattern singleCommentPattern = Pattern.compile("//.*?\\n");
        Matcher singleCommentMatcher = singleCommentPattern.matcher(json);
        json = singleCommentMatcher.replaceAll("\n");

        // Remove multi-line comments (/* ... */)
        Pattern multiCommentPattern = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
        Matcher multiCommentMatcher = multiCommentPattern.matcher(json);
        return multiCommentMatcher.replaceAll("");
    }

    /**
     * Splits a JSON string into individual game objects.
     */
    private static List<String> splitJsonObjects(String jsonContent) {
        List<String> objects = new ArrayList<>();
        int bracketCount = 0;
        int startIndex = 0;
        boolean inQuotes = false;
        char[] chars = jsonContent.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            // Handle quotes (respect escaped quotes)
            if (c == '"' && (i == 0 || chars[i - 1] != '\\')) {
                inQuotes = !inQuotes;
            }

            // Only count brackets when not in quotes
            if (!inQuotes) {
                if (c == '{') {
                    if (bracketCount == 0) {
                        startIndex = i;
                    }
                    bracketCount++;
                } else if (c == '}') {
                    bracketCount--;
                    // When we close a top-level object, add it to the list
                    if (bracketCount == 0) {
                        objects.add(jsonContent.substring(startIndex, i + 1));
                    }
                }
            }
        }

        return objects;
    }

    /**
     * Parses a single game object from JSON.
     */
    private static Game parseGameObject(String gameJson) {
        // Extract field values using regex
        String title = extractStringValue(gameJson, "title");
        String description = extractStringValue(gameJson, "description");
        String developer = extractStringValue(gameJson, "developer");
        String platformJson = extractValue(gameJson, "platform");
        String platform = parsePlatformString(platformJson);
        float price = extractFloatValue(gameJson, "price");
        String releaseDateStr = extractStringValue(gameJson, "releaseDate");
        Date releaseDate = parseReleaseDate(releaseDateStr);
        String imagePath = extractStringValue(gameJson, "imagePath");
        String genreJson = extractValue(gameJson, "genre");
        String genre = parseGenreString(genreJson);
        float rating = extractFloatValue(gameJson, "rating");

        // Create and return the Game object
        Game game = new Game();
        game.setTitle(title);
        game.setDescription(description);
        game.setDeveloper(developer);
        game.setPlatform(platform);
        game.setPrice(price);
        game.setReleaseDate(releaseDate);
        game.setImagePath(imagePath);
        game.setGenre(genre);
        game.setRating(rating);

        return game;
    }

    /**
     * Extracts a string value from a JSON field.
     */
    private static String extractStringValue(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*\"(.*?)\"(?=,|\\})", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            // Unescape JSON string
            return unescapeJsonString(matcher.group(1));
        }

        // Check for null value
        Pattern nullPattern = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*null(?=,|\\})", Pattern.DOTALL);
        Matcher nullMatcher = nullPattern.matcher(json);

        if (nullMatcher.find()) {
            return null;
        }

        return "";
    }

    /**
     * Extracts a raw value (might be array, string, or primitive) from a JSON
     * field.
     */
    private static String extractValue(String json, String fieldName) {
        String regex = "\"" + fieldName + "\"\\s*:\\s*(.*?)(?=,\\s*\"|\\}\\s*$)";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String value = matcher.group(1).trim();
            // Remove trailing commas if they exist
            if (value.endsWith(",")) {
                value = value.substring(0, value.length() - 1);
            }
            return value;
        }

        return null;
    }

    /**
     * Extracts a float value from a JSON field.
     */
    private static float extractFloatValue(String json, String fieldName) {
        String valueStr = extractValue(json, fieldName);

        if (valueStr != null) {
            try {
                return Float.parseFloat(valueStr);
            } catch (NumberFormatException e) {
                // If not a valid number, try again by checking for quoted value
                String quotedValue = extractStringValue(json, fieldName);
                if (quotedValue != null && !quotedValue.isEmpty()) {
                    try {
                        return Float.parseFloat(quotedValue);
                    } catch (NumberFormatException ex) {
                        // Unable to parse float
                    }
                }
            }
        }

        // Default value if not found or parsing failed
        return 0.0f;
    }

    /**
     * Parses a release date from a string.
     */
    private static Date parseReleaseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty() || dateStr.equals("null")) {
            return null;
        }

        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Converts a platform array from JSON to a comma-separated string.
     */
    private static String parsePlatformString(String platformJson) {
        if (platformJson == null) {
            return "";
        }

        // Check if it's a JSON array
        if (platformJson.startsWith("[") && platformJson.endsWith("]")) {
            // Remove brackets and quotes, then split by comma
            String content = platformJson.substring(1, platformJson.length() - 1);
            return parseStringArray(content);
        } else {
            // It might be a single value
            return platformJson.replace("\"", "");
        }
    }

    /**
     * Converts a genre array from JSON to a comma-separated string.
     */
    private static String parseGenreString(String genreJson) {
        if (genreJson == null) {
            return "";
        }

        // Check if it's a JSON array
        if (genreJson.startsWith("[") && genreJson.endsWith("]")) {
            // Remove brackets and quotes, then split by comma
            String content = genreJson.substring(1, genreJson.length() - 1);
            return parseStringArray(content);
        } else {
            // It might be a single value
            return genreJson.replace("\"", "");
        }
    }

    /**
     * Parses a string array from JSON.
     */
    private static String parseStringArray(String arrayContent) {
        StringBuilder result = new StringBuilder();
        boolean inQuotes = false;
        boolean escaped = false;
        StringBuilder currentItem = new StringBuilder();

        for (char c : arrayContent.toCharArray()) {
            if (escaped) {
                currentItem.append(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                // We found a separator between items
                if (currentItem.length() > 0) {
                    if (result.length() > 0) {
                        result.append(", ");
                    }
                    result.append(currentItem.toString().trim());
                    currentItem = new StringBuilder();
                }
            } else if (inQuotes || !Character.isWhitespace(c)) {
                currentItem.append(c);
            }
        }

        // Add the last item
        if (currentItem.length() > 0) {
            if (result.length() > 0) {
                result.append(", ");
            }
            result.append(currentItem.toString().trim());
        }

        return result.toString();
    }

    /**
     * Unescapes a JSON string.
     */
    private static String unescapeJsonString(String input) {
        if (input == null) {
            return null;
        }

        return input.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\/", "/")
                .replace("\\b", "\b")
                .replace("\\f", "\f")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }
}