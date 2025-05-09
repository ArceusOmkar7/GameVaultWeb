package gamevaultbase.storage;

import gamevaultbase.entities.Cart;
import gamevaultbase.entities.Game;
import gamevaultbase.interfaces.StorageInterface;
import gamevaultbase.helpers.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartStorage implements StorageInterface<Cart, Integer> {

    private GameStorage gameStorage;

    // Default constructor for AppContextListener
    public CartStorage() {
        this.gameStorage = null; // Will be set via setGameStorage method
    }

    public CartStorage(GameStorage gameStorage) {
        this.gameStorage = gameStorage;
    }

    // Setter for GameStorage to use after construction if needed
    public void setGameStorage(GameStorage gameStorage) {
        this.gameStorage = gameStorage;
    }

    @Override
    public Cart findById(Integer userId) {
        String sql = "SELECT userId FROM Carts WHERE userId = ?";
        try {
            List<Cart> carts = DBUtil.executeQuery(sql, rs -> mapResultSetToCart(rs), userId);
            return carts.isEmpty() ? null : carts.get(0);
        } catch (SQLException e) {
            System.err.println("Error finding cart by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Cart> findAll() {
        String sql = "SELECT userId FROM Carts";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToCart(rs));
        } catch (SQLException e) {
            System.err.println("Error finding all carts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void save(Cart cart) {
        String sql = "INSERT INTO Carts (userId) VALUES (?)";
        try {
            DBUtil.executeUpdate(sql, cart.getUserId());
        } catch (SQLException e) {
            System.err.println("Error saving cart: " + e.getMessage());
        }
    }

    @Override
    public void update(Cart cart) {
        // Cart table only contains userId, so there's nothing to update directly.
        // The games in the cart are managed in the CartItems table.
        System.out.println("Updating cart is not supported, since game table is not present.");
    }

    @Override
    public void delete(Integer userId) {
        String sql = "DELETE FROM Carts WHERE userId = ?";
        try {
            DBUtil.executeUpdate(sql, userId);
        } catch (SQLException e) {
            System.err.println("Error deleting cart: " + e.getMessage());
        }
    }

    // Adds game to cart
    public String addGameToCart(int userId, int gameId) {
        // Verify user exists first
        if (!doesUserExist(userId)) {
            System.err.println("Error: Unable to add to cart - User ID " + userId + " doesn't exist in the database");
            return "User not found";
        }

        // Verify game exists
        if (!doesGameExist(gameId)) {
            System.err.println("Error: Unable to add to cart - Game ID " + gameId + " doesn't exist in the database");
            return "Game not found";
        }

        // Now ensure the cart exists
        ensureCartExists(userId);

        // Check if game is already in cart
        if (isGameInCart(userId, gameId)) {
            System.out.println("Game is already in user's cart");
            return "already_in_cart";
        }

        // Then add the game to the cart
        String sql = "INSERT INTO CartItems (userId, gameId) VALUES (?, ?)";
        try {
            DBUtil.executeUpdate(sql, userId, gameId);
            return "success";
        } catch (SQLException e) {
            System.err.println("Error adding game to cart: " + e.getMessage());
            return "error";
        }
    }

    // Check if game exists in the database
    private boolean doesGameExist(int gameId) {
        String sql = "SELECT COUNT(*) AS gameCount FROM Games WHERE gameId = ?";
        try {
            List<Integer> results = DBUtil.executeQuery(sql, rs -> rs.getInt("gameCount"), gameId);
            return !results.isEmpty() && results.get(0) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking if game exists: " + e.getMessage());
            return false;
        }
    }

    // Check if a game is already in the user's cart
    private boolean isGameInCart(int userId, int gameId) {
        String sql = "SELECT COUNT(*) AS itemCount FROM CartItems WHERE userId = ? AND gameId = ?";
        try {
            List<Integer> results = DBUtil.executeQuery(sql, rs -> rs.getInt("itemCount"), userId, gameId);
            return !results.isEmpty() && results.get(0) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking if game is in cart: " + e.getMessage());
            return false;
        }
    }

    // Ensure cart record exists for user
    private void ensureCartExists(int userId) {
        // First check if the user exists in the database
        if (!doesUserExist(userId)) {
            // The user doesn't exist in database, but we have a session user
            // Let's create a real user record in the database to match the session user
            createDemoUser(userId);

            // If we still don't have a user in the database after attempting to create one,
            // return
            if (!doesUserExist(userId)) {
                System.err.println(
                        "Error: User with ID " + userId + " doesn't exist in the database and couldn't be created");
                return;
            }
        }

        Cart existingCart = findById(userId);
        if (existingCart == null) {
            // Create cart if it doesn't exist
            String sql = "INSERT INTO Carts (userId) VALUES (?)";
            try {
                DBUtil.executeUpdate(sql, userId);
            } catch (SQLException e) {
                System.err.println("Error creating cart for user: " + e.getMessage());
            }
        }
    }

    // Create a demo user in the database to match session users
    private void createDemoUser(int userId) {
        try {
            // Only handle the two known demo users (1=admin, 2=regular user)
            if (userId != 1 && userId != 2) {
                return;
            }

            String username = userId == 1 ? "admin" : "user";
            String email = userId == 1 ? "admin@gamevault.com" : "user@gamevault.com";
            String name = userId == 1 ? "Admin" : "User";
            float balance = userId == 1 ? 1000.0f : 500.0f;
            boolean isAdmin = userId == 1;

            // Check if user already exists by username (to avoid unique constraint
            // violations)
            String checkSql = "SELECT COUNT(*) AS userCount FROM Users WHERE username = ?";
            List<Integer> results = DBUtil.executeQuery(checkSql, rs -> rs.getInt("userCount"), username);
            if (!results.isEmpty() && results.get(0) > 0) {
                // User with this username already exists, but has a different ID
                // Update the existing user's ID to match the expected ID
                String updateSql = "UPDATE Users SET userId = ? WHERE username = ?";
                DBUtil.executeUpdate(updateSql, userId, username);
                System.out.println("Updated demo user ID for " + username);
                return;
            }

            // Insert the demo user
            String sql = "INSERT INTO Users (userId, username, email, name, walletBalance, isAdmin) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            DBUtil.executeUpdate(sql, userId, username, email, name, balance, isAdmin ? 1 : 0);
            System.out.println("Created demo user: " + username + " with ID " + userId);

        } catch (SQLException e) {
            System.err.println("Error creating demo user: " + e.getMessage());
        }
    }

    // Check if user exists in the database
    private boolean doesUserExist(int userId) {
        String sql = "SELECT COUNT(*) AS userCount FROM Users WHERE userId = ?";
        try {
            List<Integer> results = DBUtil.executeQuery(sql, rs -> rs.getInt("userCount"), userId);
            return !results.isEmpty() && results.get(0) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
            return false;
        }
    }

    // Removes game from cart
    public void removeGameFromCart(int userId, int gameId) {
        // Ensure the cart exists before attempting to remove items
        ensureCartExists(userId);

        String sql = "DELETE FROM CartItems WHERE userId = ? AND gameId = ?";
        try {
            DBUtil.executeUpdate(sql, userId, gameId);
        } catch (SQLException e) {
            System.err.println("Error removing game from cart: " + e.getMessage());
        }
    }

    // Gets games in cart
    public List<Game> getGamesInCart(int userId) {
        if (gameStorage == null) {
            // Initialize GameStorage if not set
            gameStorage = new GameStorage();
        }

        String sql = "SELECT g.* FROM CartItems ci JOIN Games g ON ci.gameId = g.gameId WHERE ci.userId = ?";
        try {
            return DBUtil.executeQuery(sql, rs -> gameStorage.mapResultSetToGame(rs), userId);
        } catch (SQLException e) {
            System.err.println("Error getting games in cart: " + e.getMessage());
            return new ArrayList<>(); // Return empty list in case of error
        }
    }

    public void clearCart(int userId) {
        String sql = "DELETE FROM CartItems WHERE userId = ?";
        try {
            DBUtil.executeUpdate(sql, userId);
        } catch (SQLException e) {
            System.err.println("Error clearing cart: " + e.getMessage());
        }
    }

    private Cart mapResultSetToCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setUserId(rs.getInt("userId"));
        return cart;
    }
}