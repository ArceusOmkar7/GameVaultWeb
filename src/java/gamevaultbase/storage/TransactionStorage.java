package gamevaultbase.storage;

import gamevaultbase.entities.Transaction;
import gamevaultbase.interfaces.StorageInterface;
import gamevaultbase.helpers.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransactionStorage implements StorageInterface<Transaction, Integer> {

    @Override
    public Transaction findById(Integer transactionId) {
        String sql = "SELECT * FROM Transactions WHERE transactionId = ?";
        try {
            List<Transaction> transactions = DBUtil.executeQuery(sql, rs -> mapResultSetToTransaction(rs),
                    transactionId);
            return transactions.isEmpty() ? null : transactions.get(0);
        } catch (SQLException e) {
            System.err.println("Error finding transaction by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM Transactions ORDER BY transactionDate DESC";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToTransaction(rs));
        } catch (SQLException e) {
            System.err.println("Error finding all transactions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO Transactions (orderId, userId, type, amount, transactionDate) VALUES (?, ?, ?, ?, ?)";
        try {
            int transactionId = DBUtil.executeInsertAndGetKey(sql,
                    transaction.getOrderId(),
                    transaction.getUserId(),
                    transaction.getTransactionType(), // Changed from getType to getTransactionType
                    transaction.getAmount(),
                    transaction.getTransactionDate() != null ? Timestamp.valueOf(transaction.getTransactionDate()) : // Convert
                                                                                                                     // LocalDateTime
                                                                                                                     // to
                                                                                                                     // Timestamp
                            new Timestamp(System.currentTimeMillis()));
            if (transactionId != -1) {
                transaction.setTransactionId(transactionId);
            }
        } catch (SQLException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }

    @Override
    public void update(Transaction transaction) {
        String sql = "UPDATE Transactions SET orderId = ?, userId = ?, type = ?, amount = ?, transactionDate = ? WHERE transactionId = ?";
        try {
            DBUtil.executeUpdate(sql,
                    transaction.getOrderId(),
                    transaction.getUserId(),
                    transaction.getTransactionType(), // Changed from getType to getTransactionType
                    transaction.getAmount(),
                    transaction.getTransactionDate() != null ? Timestamp.valueOf(transaction.getTransactionDate()) : // Convert
                                                                                                                     // LocalDateTime
                                                                                                                     // to
                                                                                                                     // Timestamp
                            null,
                    transaction.getTransactionId());
        } catch (SQLException e) {
            System.err.println("Error updating transaction: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer transactionId) {
        String sql = "DELETE FROM Transactions WHERE transactionId = ?";
        try {
            DBUtil.executeUpdate(sql, transactionId);
        } catch (SQLException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
        }
    }

    public List<Transaction> findByUserId(Integer userId) {
        String sql = "SELECT * FROM Transactions WHERE userId = ? ORDER BY transactionDate DESC";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToTransaction(rs), userId);
        } catch (SQLException e) {
            System.err.println("Error finding transactions by user ID: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Transaction> findByOrderId(Integer orderId) {
        String sql = "SELECT * FROM Transactions WHERE orderId = ? ORDER BY transactionDate DESC";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToTransaction(rs), orderId);
        } catch (SQLException e) {
            System.err.println("Error finding transactions by order ID: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transactionId"));
        transaction.setOrderId(rs.getObject("orderId") != null ? rs.getInt("orderId") : null);
        transaction.setUserId(rs.getInt("userId"));
        transaction.setTransactionType(rs.getString("type")); // Changed from setType to setTransactionType
        transaction.setAmount(rs.getFloat("amount"));

        // Convert java.sql.Timestamp to LocalDateTime
        java.sql.Timestamp timestamp = rs.getTimestamp("transactionDate");
        if (timestamp != null) {
            transaction.setTransactionDate(timestamp.toLocalDateTime());
        }

        return transaction;
    }
}