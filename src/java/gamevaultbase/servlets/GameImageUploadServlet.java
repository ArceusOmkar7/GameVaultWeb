package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.User;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.management.GameManagement;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class GameImageUploadServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(GameImageUploadServlet.class.getName());
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp");

    private String getGameImagesPath() {
        // Get the absolute path to the deployed webapp
        String webappPath = getServletContext().getRealPath("/");

        // Create a path to the game_images directory
        File gameImagesDir = new File(webappPath, "game_images");

        // Ensure the directory exists
        if (!gameImagesDir.exists()) {
            boolean created = gameImagesDir.mkdirs();
            if (!created) {
                LOGGER.log(Level.WARNING, "Could not create game_images directory at: {0}",
                        gameImagesDir.getAbsolutePath());
            } else {
                LOGGER.log(Level.INFO, "Created game_images directory at: {0}", gameImagesDir.getAbsolutePath());
            }
        }

        // Set proper permissions (attempt to make directory writable)
        gameImagesDir.setWritable(true, false);

        // Log the path for debugging
        LOGGER.log(Level.INFO, "Game images directory path: {0}", gameImagesDir.getAbsolutePath());

        return gameImagesDir.getAbsolutePath();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in and is an admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("loggedInUser");
        if (!user.isAdmin()) {
            // Redirect non-admin users to home
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Get game management from application context
        GameManagement gameManagement = (GameManagement) getServletContext().getAttribute("gameManagement");
        if (gameManagement == null) {
            request.setAttribute("errorMessage", "System error: Game management service not available.");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        // Get game ID from request
        String gameIdStr = request.getParameter("gameId");
        if (gameIdStr == null || gameIdStr.trim().isEmpty()) {
            redirectWithError(request, response, "Game ID is required.");
            return;
        }

        int gameId;
        try {
            gameId = Integer.parseInt(gameIdStr);
        } catch (NumberFormatException e) {
            redirectWithError(request, response, "Invalid game ID format.");
            return;
        }

        // Get the game from database
        Game game;
        try {
            game = gameManagement.getGame(gameId);
        } catch (GameNotFoundException e) {
            redirectWithError(request, response, "Game not found with ID: " + gameId);
            return;
        }

        // Get the uploaded file from request
        Part filePart;
        try {
            filePart = request.getPart("gameImage");
            if (filePart == null || filePart.getSize() <= 0) {
                redirectWithError(request, response, "No image file uploaded.");
                return;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing upload request", e);
            redirectWithError(request, response, "Error processing upload request: " + e.getMessage());
            return;
        }

        // Validate the file type (only accept images)
        String contentType = filePart.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            redirectWithError(request, response,
                    "Only JPG, PNG, GIF and WebP images are allowed. Received: " + contentType);
            return;
        }

        // Validate file size (redundant check but good for clarity)
        if (filePart.getSize() > 10 * 1024 * 1024) { // 10MB
            redirectWithError(request, response, "Image file is too large. Maximum size is 10MB.");
            return;
        }

        // Create the upload directory if it doesn't exist
        String uploadPath = getGameImagesPath();
        File uploadDir = new File(uploadPath);

        LOGGER.log(Level.INFO, "Checking if directory exists: {0}", uploadDir.getAbsolutePath());
        LOGGER.log(Level.INFO, "Directory exists: {0}", uploadDir.exists());

        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs(); // Use mkdirs() instead of mkdir() to create parent directories if
                                                  // needed
            LOGGER.log(Level.INFO, "Creating directory: {0}", uploadDir.getAbsolutePath());
            LOGGER.log(Level.INFO, "Directory created successfully: {0}", created);

            if (!created) {
                LOGGER.log(Level.SEVERE, "Failed to create directory: {0}", uploadPath);
                redirectWithError(request, response, "Failed to create upload directory. Check server permissions.");
                return;
            }
        }

        // Generate a unique filename based on game ID and timestamp
        String fileName = "game_" + gameId + "_" + System.currentTimeMillis() + getFileExtension(filePart);
        File targetFile = new File(uploadDir, fileName);

        LOGGER.log(Level.INFO, "Saving file to: {0}", targetFile.getAbsolutePath());

        // Save the file to disk
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.log(Level.INFO, "File saved successfully at: {0}", targetFile.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving file", e);
            redirectWithError(request, response, "Failed to save the image: " + e.getMessage());
            return;
        }

        try {
            // Update the game entity with the image path
            String relativePath = "game_images/" + fileName;
            game.addImagePath(relativePath); // Use addImagePath instead of setImagePath to support multiple images
            gameManagement.updateGame(game);
            LOGGER.log(Level.INFO, "Game updated with new image path: {0}", relativePath);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating game with new image path", e);
            // Try to delete the uploaded file if the database update fails
            try {
                Files.deleteIfExists(targetFile.toPath());
            } catch (IOException ioe) {
                LOGGER.log(Level.WARNING, "Could not delete uploaded file after failed database update", ioe);
            }
            redirectWithError(request, response, "Failed to update game with image: " + e.getMessage());
            return;
        }

        // Redirect back to admin dashboard with success message
        response.sendRedirect(
                request.getContextPath() + "/admin/dashboard?message=Image+uploaded+successfully+for+game:+"
                        + game.getTitle() + "&messageType=success");
    }

    private void redirectWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws IOException {
        LOGGER.log(Level.WARNING, "Error in GameImageUploadServlet: {0}", errorMessage);
        response.sendRedirect(
                request.getContextPath() + "/admin/dashboard?message=" + errorMessage + "&messageType=error");
    }

    private String getFileExtension(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                String fileName = token.substring(token.indexOf("=") + 2, token.length() - 1);
                int lastDot = fileName.lastIndexOf('.');
                if (lastDot > 0) {
                    return fileName.substring(lastDot).toLowerCase();
                }
            }
        }

        // If no extension found, derive it from content type
        String contentType = part.getContentType();
        if (contentType != null) {
            if (contentType.contains("jpeg") || contentType.contains("jpg")) {
                return ".jpg";
            } else if (contentType.contains("png")) {
                return ".png";
            } else if (contentType.contains("gif")) {
                return ".gif";
            } else if (contentType.contains("webp")) {
                return ".webp";
            }
        }

        return ".jpg"; // Default extension if none is found
    }
}