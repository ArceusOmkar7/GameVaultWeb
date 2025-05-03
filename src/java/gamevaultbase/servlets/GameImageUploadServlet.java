package gamevaultbase.servlets;

import gamevaultbase.entities.Game;
import gamevaultbase.entities.User;
import gamevaultbase.exceptions.GameNotFoundException;
import gamevaultbase.management.GameManagement;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    private String getGameImagesPath() {
        return getServletContext().getRealPath("/") + "game_images";
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
            request.setAttribute("errorMessage", "Game ID is required.");
            request.getRequestDispatcher("/WEB-INF/jsp/adminDashboard.jsp").forward(request, response);
            return;
        }

        int gameId;
        try {
            gameId = Integer.parseInt(gameIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid game ID format.");
            request.getRequestDispatcher("/WEB-INF/jsp/adminDashboard.jsp").forward(request, response);
            return;
        }

        // Get the game from database
        Game game;
        try {
            game = gameManagement.getGame(gameId);
        } catch (GameNotFoundException e) {
            request.setAttribute("errorMessage", "Game not found with ID: " + gameId);
            request.getRequestDispatcher("/WEB-INF/jsp/adminDashboard.jsp").forward(request, response);
            return;
        }

        // Get the uploaded file from request
        Part filePart = request.getPart("gameImage");
        if (filePart == null || filePart.getSize() <= 0) {
            request.setAttribute("errorMessage", "No image file uploaded.");
            request.getRequestDispatcher("/WEB-INF/jsp/adminDashboard.jsp").forward(request, response);
            return;
        }

        // Validate the file type (only accept images)
        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            request.setAttribute("errorMessage", "Only image files are allowed.");
            request.getRequestDispatcher("/WEB-INF/jsp/adminDashboard.jsp").forward(request, response);
            return;
        }

        // Create the upload directory if it doesn't exist
        String uploadPath = getGameImagesPath();
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Generate a unique filename based on game ID and timestamp
        String fileName = "game_" + gameId + "_" + System.currentTimeMillis() + getFileExtension(filePart);
        String filePath = uploadPath + File.separator + fileName;

        // Save the file to disk
        try (InputStream fileContent = filePart.getInputStream()) {
            Path targetPath = Paths.get(filePath);
            Files.copy(fileContent, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Failed to save the image: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/adminDashboard.jsp").forward(request, response);
            return;
        }

        // Update the game entity with the image path
        String relativePath = "game_images/" + fileName;
        game.setImagePath(relativePath);
        gameManagement.updateGame(game);

        // Redirect back to admin dashboard with success message
        request.setAttribute("message", "Image uploaded successfully for game: " + game.getTitle());
        request.setAttribute("messageType", "success");

        // Get updated list of games
        request.setAttribute("games", gameManagement.getAllGames());
        request.getRequestDispatcher("/WEB-INF/jsp/adminDashboard.jsp").forward(request, response);
    }

    private String getFileExtension(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                String fileName = token.substring(token.indexOf("=") + 2, token.length() - 1);
                int lastDot = fileName.lastIndexOf('.');
                if (lastDot > 0) {
                    return fileName.substring(lastDot);
                }
            }
        }
        return ".jpg"; // Default extension if none is found
    }
}