package gamevaultbase.servlets.admin;

import gamevaultbase.helpers.DBUtil;
import gamevaultbase.helpers.ServletUtil;
import gamevaultbase.servlets.base.AdminBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This servlet checks the database structure to ensure all required tables
 * exist.
 * It helps diagnose issues with missing tables or columns.
 * Access at /admin/check-database
 */
@WebServlet(name = "DatabaseCheckServlet", urlPatterns = { "/admin/check-database" })
public class DatabaseCheckServlet extends AdminBaseServlet {

    @Override
    protected void processAdminGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> requiredTables = Arrays.asList(
                "Users", "Games", "Orders", "OrderItems", "Transactions", "Reviews", "Carts", "CartItems");

        List<String> missingTables = new ArrayList<>();
        List<String> existingTables = new ArrayList<>();
        List<Map<String, String>> columnCheckResults = new ArrayList<>();
        StringBuilder sqlStatements = new StringBuilder();

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

            // Build SQL statements for missing tables
            if (!missingTables.isEmpty()) {
                if (missingTables.contains("Users")) {
                    sqlStatements.append("CREATE TABLE Users (\n" +
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
                    sqlStatements.append("\nCREATE TABLE Games (\n" +
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
                    sqlStatements.append("\nCREATE TABLE Orders (\n" +
                            "  orderId INT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  userId INT NOT NULL,\n" +
                            "  totalAmount FLOAT NOT NULL,\n" +
                            "  orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                            "  FOREIGN KEY (userId) REFERENCES Users(userId)\n" +
                            ");\n");
                }

                if (missingTables.contains("OrderItems")) {
                    sqlStatements.append("\nCREATE TABLE OrderItems (\n" +
                            "  orderItemId INT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  orderId INT NOT NULL,\n" +
                            "  gameId INT NOT NULL,\n" +
                            "  priceAtPurchase FLOAT NOT NULL,\n" +
                            "  FOREIGN KEY (orderId) REFERENCES Orders(orderId),\n" +
                            "  FOREIGN KEY (gameId) REFERENCES Games(gameId)\n" +
                            ");\n");
                }

                if (missingTables.contains("Transactions")) {
                    sqlStatements.append("\nCREATE TABLE Transactions (\n" +
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
                    sqlStatements.append("\nCREATE TABLE Reviews (\n" +
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
                    sqlStatements.append("\nCREATE TABLE Carts (\n" +
                            "  userId INT PRIMARY KEY,\n" +
                            "  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                            "  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                            "  FOREIGN KEY (userId) REFERENCES Users(userId)\n" +
                            ");\n");
                }

                if (missingTables.contains("CartItems")) {
                    sqlStatements.append("\nCREATE TABLE CartItems (\n" +
                            "  cartItemId INT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  userId INT NOT NULL,\n" +
                            "  gameId INT NOT NULL,\n" +
                            "  addedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                            "  FOREIGN KEY (userId) REFERENCES Users(userId),\n" +
                            "  FOREIGN KEY (gameId) REFERENCES Games(gameId),\n" +
                            "  UNIQUE KEY (userId, gameId)\n" +
                            ");\n");
                }
            }

            // Check column structure for existing tables
            if (!existingTables.isEmpty()) {
                // Check Games table columns if it exists
                if (existingTables.contains("Games")) {
                    try {
                        // Try to check if the 'genre' column exists
                        String sql = "SELECT genre FROM Games LIMIT 1";
                        DBUtil.executeQuery(sql, rs -> 1);

                        Map<String, String> result = new LinkedHashMap<>();
                        result.put("status", "success");
                        result.put("message", "Games table has 'genre' column ✓");
                        columnCheckResults.add(result);
                    } catch (Exception e) {
                        // Genre column might be missing
                        Map<String, String> result = new LinkedHashMap<>();
                        result.put("status", "warning");
                        result.put("message", "Games table might be missing 'genre' column.");
                        result.put("sql", "ALTER TABLE Games ADD COLUMN genre VARCHAR(100);");
                        columnCheckResults.add(result);
                    }
                }

                // Check OrderItems table columns if it exists
                if (existingTables.contains("OrderItems")) {
                    try {
                        // Try to check if the 'priceAtPurchase' column exists
                        String sql = "SELECT priceAtPurchase FROM OrderItems LIMIT 1";
                        DBUtil.executeQuery(sql, rs -> 1);

                        Map<String, String> result = new LinkedHashMap<>();
                        result.put("status", "success");
                        result.put("message", "OrderItems table has 'priceAtPurchase' column ✓");
                        columnCheckResults.add(result);
                    } catch (Exception e) {
                        // priceAtPurchase column might be missing
                        Map<String, String> result = new LinkedHashMap<>();
                        result.put("status", "warning");
                        result.put("message", "OrderItems table might be missing 'priceAtPurchase' column.");
                        result.put("sql",
                                "ALTER TABLE OrderItems ADD COLUMN priceAtPurchase FLOAT NOT NULL DEFAULT 0;");
                        columnCheckResults.add(result);
                    }
                }

                // Check Transactions table columns if it exists
                if (existingTables.contains("Transactions")) {
                    try {
                        // Try to check if the 'type' column exists
                        String sql = "SELECT type FROM Transactions LIMIT 1";
                        DBUtil.executeQuery(sql, rs -> 1);

                        Map<String, String> result = new LinkedHashMap<>();
                        result.put("status", "success");
                        result.put("message", "Transactions table has 'type' column ✓");
                        columnCheckResults.add(result);
                    } catch (Exception e) {
                        // type column might be missing
                        Map<String, String> result = new LinkedHashMap<>();
                        result.put("status", "warning");
                        result.put("message", "Transactions table might be missing 'type' column.");
                        result.put("sql",
                                "ALTER TABLE Transactions ADD COLUMN type VARCHAR(50) NOT NULL DEFAULT 'Purchase';");
                        columnCheckResults.add(result);
                    }
                }
            }

            // Set attributes for the JSP
            request.setAttribute("requiredTables", requiredTables);
            request.setAttribute("existingTables", existingTables);
            request.setAttribute("missingTables", missingTables);
            request.setAttribute("sqlStatements", sqlStatements.toString());
            request.setAttribute("columnCheckResults", columnCheckResults);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            e.printStackTrace();
        }

        // Forward to the JSP view
        ServletUtil.forwardToJsp(request, response, "/WEB-INF/jsp/adminDatabaseCheck.jsp");
    }
}
