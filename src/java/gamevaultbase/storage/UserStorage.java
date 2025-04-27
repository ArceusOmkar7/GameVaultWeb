package gamevaultbase.storage;

import gamevaultbase.entities.User;
import gamevaultbase.interfaces.StorageInterface;
import gamevaultbase.helpers.DBUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class UserStorage implements StorageInterface<User, Integer> {

    @Override
    public User findById(Integer userId) {
        String sql = "SELECT * FROM Users WHERE userId = ?";
        try {
            List<User> users = DBUtil.executeQuery(sql, rs -> mapResultSetToUser(rs), userId);
            return users.isEmpty() ? null : users.get(0);
        } catch (SQLException | IOException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            return null;
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try {
            List<User> users = DBUtil.executeQuery(sql, rs -> mapResultSetToUser(rs), email);
            return users.isEmpty() ? null : users.get(0);
        } catch (SQLException | IOException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM Users";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToUser(rs));
        } catch (SQLException | IOException e) {
            System.err.println("Error finding all users: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void save(User user) {
        // WARNING: Storing plain text password - INSECURE
        String sql = "INSERT INTO Users (email, password, username, walletBalance, createdAt) VALUES (?, ?, ?, ?, ?)";
        try {
            int generatedId = DBUtil.executeInsertAndGetKey(sql,
                user.getEmail(),
                user.getPassword(), // Plain text password
                user.getUsername(),
                user.getWalletBalance(),
                new Timestamp(user.getCreatedAt().getTime())
            );
            if (generatedId != -1) {
                user.setUserId(generatedId);
            } else {
                 // This might indicate a problem or just that the DB didn't return a key
                 System.err.println("WARN: User insert did not return a generated ID for email: " + user.getEmail());
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error saving user: " + user.getEmail() + " - " + e.getMessage());
            // Consider rethrowing a custom exception
            // throw new DataAccessException("Failed to save user", e);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE Users SET email = ?, password = ?, username = ?, walletBalance = ? WHERE userId = ?";
        try {
            DBUtil.executeUpdate(sql, user.getEmail(), user.getPassword(), user.getUsername(), user.getWalletBalance(), user.getUserId());
        } catch (SQLException | IOException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer userId) {
        String sql = "DELETE FROM Users WHERE userId = ?";
        try {
            DBUtil.executeUpdate(sql, userId);
        } catch (SQLException | IOException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("userId"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("username"),
                rs.getFloat("walletBalance"),
                rs.getTimestamp("createdAt")
        );
    }
}