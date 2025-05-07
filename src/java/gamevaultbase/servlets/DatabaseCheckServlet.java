package gamevaultbase.servlets;

import gamevaultbase.helpers.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This servlet checks the database structure to ensure all required tables
 * exist.
 * It helps diagnose issues with missing tables or columns.
 * Access at /admin/check-database
 */
@WebServlet(name = "DatabaseCheckServlet", urlPatterns = { "/admin/check-database" })
public class DatabaseCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Check if user is logged in and is an admin
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (session.getAttribute("loggedInUser") == null || isAdmin == null || !isAdmin) {
            response.sendRedirect(request.getContextPath() +
                    "/login?message=Admin access required&messageType=error");
            return;
        }

        // HTML header
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Database Structure Check</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px; }");
        out.println("h1 { color: #333; }");
        out.println(".success { color: green; }");
        out.println(".error { color: red; }");
        out.println(".warning { color: orange; }");
        out.println("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
        out.println("th, td { text-align: left; padding: 8px; border: 1px solid #ddd; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("tr:nth-child(even) { background-color: #f9f9f9; }");
        out.println("a { display: inline-block; margin-top: 20px; color: blue; text-decoration: none; }");
        out.println("a:hover { text-decoration: underline; }");
        out.println(".button { background-color: #4CAF50; border: none; color: white; padding: 10px 20px; " +
                "text-align: center; text-decoration: none; display: inline-block; " +
                "font-size: 16px; margin: 4px 2px; cursor: pointer; border-radius: 5px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Database Structure Check</h1>");

        List<String> requiredTables = Arrays.asList(
                "Users", "Games", "Orders", "OrderItems", "Transactions", "Reviews", "Carts", "CartItems");

        List<String> missingTables = new ArrayList<>();
        List<String> existingTables = new ArrayList<>();

        try {
            // Check each required table
            for (String tableName : requiredTables) {
                try {
                    String sql = "SELECT 1 FROM " + tableName + " LIMIT 1";
                    DBUtil.executeQuery(sql, rs -> 1);
                    existingTables.add(tableName);
                } catch (Exception e) {
                    missingTables.add(tableName);
                }
            }

            // Display results
            out.println("<h2>Table Check Results</h2>");
            if (missingTables.isEmpty()) {
                out.println("<p class='success'>All required tables exist!</p>");
            } else {
                out.println("<p class='error'>Missing tables detected!</p>");
            }

            out.println("<table>");
            out.println("<tr><th>Table Name</th><th>Status</th></tr>");

            for (String table : requiredTables) {
                String status = existingTables.contains(table)
                        ? "<span class='success'>Exists</span>"
                        : "<span class='error'>Missing</span>";
                out.println("<tr><td>" + table + "</td><td>" + status + "</td></tr>");
            }

            out.println("</table>");

            // If there are missing tables, show SQL to create them
            if (!missingTables.isEmpty()) {
                out.println("<h2>SQL to Create Missing Tables</h2>");
                out.println("<p>Run these SQL statements to create the missing tables:</p>");
                out.println("<pre style='background-color: #f5f5f5; padding: 10px; overflow-x: auto;'>");

                if (missingTables.contains("Users")) {
                    out.println("CREATE TABLE Users (\n" +
                            "  userId INT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  email VARCHAR(255) NOT NULL UNIQUE,\n" +
                            "  password VARCHAR(255) NOT NULL,\n" +
                            "  username VARCHAR(50) NOT NULL,\n" +
                            "  walletBalance FLOAT DEFAULT 0,\n" +
                            "  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                            "  isAdmin BOOLEAN DEFAULT FALSE\n" +
                            ");\n");
                }

                if (missingTables.contains("Games")) {
                    out.println("\nCREATE TABLE Games (\n" +
                            "  gameId INT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  title VARCHAR(255) NOT NULL,\n" +
                            "  description TEXT,\n" +
                            "  developer VARCHAR(255),\n" +
                            "  platform VARCHAR(100),\n" +
                            "  price FLOAT,\n" +
                            "  releaseDate DATE,\n" +
                            "  imagePath VARCHAR(255),\n" +
                            "  genre VARCHAR(100)\n" +
                            ");\n");
                }

                if (missingTables.contains("Orders")) {
                    out.println("\nCREATE TABLE Orders (\n" +
                            "  orderId INT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  userId INT NOT NULL,\n" +
                            "  totalAmount FLOAT NOT NULL,\n" +
                            "  orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                            "  FOREIGN KEY (userId) REFERENCES Users(userId)\n" +
                            ");\n");
                }

                if (missingTables.contains("OrderItems")) {
                    out.println("\nCREATE TABLE OrderItems (\n" +
                            "  orderItemId INT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  orderId INT NOT NULL,\n" +
                            "  gameId INT NOT NULL,\n" +
                            "  priceAtPurchase FLOAT NOT NULL,\n" +
                            "  FOREIGN KEY (orderId) REFERENCES Orders(orderId),\n" +
                            "  FOREIGN KEY (gameId) REFERENCES Games(gameId)\n" +
                            ");\n");
                }

                if (missingTables.contains("Transactions")) {
                    out.println("\nCREATE TABLE Transactions (\n" +
                            "  transactionId INT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  orderId INT,\n" +
                            "  userId INT NOT NULL,\n" +
                            "  type VARCHAR(50) NOT NULL,\n" +
                            "  amount FLOAT NOT NULL,\n" +
                            "  transactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                            "  FOREIGN KEY (orderId) REFERENCES Orders(orderId),\n" +
                            "  FOREIGN KEY (userId) REFERENCES Users(userId)\n" +
                            ");\n");
                }

                if (missingTables.contains("Reviews")) {
                    out.println("\nCREATE TABLE Reviews (\n" +
                            "  reviewId INT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  gameId INT NOT NULL,\n" +
                            "  userId INT NOT NULL,\n" +
                            "  rating INT NOT NULL,\n" +
                            "  comment TEXT,\n" +
                            "  reviewDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                            "  FOREIGN KEY (gameId) REFERENCES Games(gameId),\n" +
                            "  FOREIGN KEY (userId) REFERENCES Users(userId)\n" +
                            ");\n");
                }

                if (missingTables.contains("Carts")) {
                    out.println("\nCREATE TABLE Carts (\n" +
                            "  userId INT PRIMARY KEY,\n" +
                            "  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                            "  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                            "  FOREIGN KEY (userId) REFERENCES Users(userId)\n" +
                            ");\n");
                }

                if (missingTables.contains("CartItems")) {
                    out.println("\nCREATE TABLE CartItems (\n" +
                            "  cartItemId INT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  userId INT NOT NULL,\n" +
                            "  gameId INT NOT NULL,\n" +
                            "  addedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                            "  FOREIGN KEY (userId) REFERENCES Users(userId),\n" +
                            "  FOREIGN KEY (gameId) REFERENCES Games(gameId),\n" +
                            "  UNIQUE KEY (userId, gameId)\n" +
                            ");\n");
                }

                out.println("</pre>");
            }

            // Check column structure for existing tables
            if (!existingTables.isEmpty()) {
                out.println("<h2>Column Check for Existing Tables</h2>");

                // Check Games table columns if it exists
                if (existingTables.contains("Games")) {
                    try {
                        // Try to check if the 'genre' column exists
                        String sql = "SELECT genre FROM Games LIMIT 1";
                        DBUtil.executeQuery(sql, rs -> 1);
                        out.println("<p class='success'>Games table has 'genre' column ✓</p>");
                    } catch (Exception e) {
                        // Genre column might be missing
                        out.println("<p class='warning'>Games table might be missing 'genre' column.</p>");
                        out.println("<p>Run this SQL to add it:</p>");
                        out.println("<pre>ALTER TABLE Games ADD COLUMN genre VARCHAR(100);</pre>");
                    }
                }

                // Check OrderItems table columns if it exists
                if (existingTables.contains("OrderItems")) {
                    try {
                        // Try to check if the 'priceAtPurchase' column exists
                        String sql = "SELECT priceAtPurchase FROM OrderItems LIMIT 1";
                        DBUtil.executeQuery(sql, rs -> 1);
                        out.println("<p class='success'>OrderItems table has 'priceAtPurchase' column ✓</p>");
                    } catch (Exception e) {
                        // priceAtPurchase column might be missing
                        out.println(
                                "<p class='warning'>OrderItems table might be missing 'priceAtPurchase' column.</p>");
                        out.println("<p>Run this SQL to add it:</p>");
                        out.println(
                                "<pre>ALTER TABLE OrderItems ADD COLUMN priceAtPurchase FLOAT NOT NULL DEFAULT 0;</pre>");
                    }
                }

                // Check Transactions table columns if it exists
                if (existingTables.contains("Transactions")) {
                    try {
                        // Try to check if the 'type' column exists
                        String sql = "SELECT type FROM Transactions LIMIT 1";
                        DBUtil.executeQuery(sql, rs -> 1);
                        out.println("<p class='success'>Transactions table has 'type' column ✓</p>");
                    } catch (Exception e) {
                        // type column might be missing
                        out.println("<p class='warning'>Transactions table might be missing 'type' column.</p>");
                        out.println("<p>Run this SQL to add it:</p>");
                        out.println(
                                "<pre>ALTER TABLE Transactions ADD COLUMN type VARCHAR(50) NOT NULL DEFAULT 'Purchase';</pre>");
                    }
                }
            }

        } catch (Exception e) {
            out.println("<p class='error'>Error checking database structure: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        } finally {
            // Add link to generate dummy data
            out.println("<div style='margin-top: 20px;'>");
            out.println("<a href='" + request.getContextPath() +
                    "/admin/generate-dummy-data' class='button'>Try Generating Dummy Data</a>");
            out.println("<a href='" + request.getContextPath() +
                    "/admin/dashboard' style='margin-left: 10px;'>Return to Admin Dashboard</a>");
            out.println("</div>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}