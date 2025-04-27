package gamevaultbase.storage;

import gamevaultbase.entities.Transaction;
import gamevaultbase.interfaces.StorageInterface;
import gamevaultbase.helpers.DBUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp; // Use Timestamp for database
import java.sql.Types; // Needed for setting NULL
import java.util.ArrayList;
import java.util.List;

public class TransactionStorage implements StorageInterface<Transaction, Integer> {

    @Override
    public Transaction findById(Integer transactionId) {
        String sql = "SELECT * FROM Transactions WHERE transactionId = ?";
        try {
            List<Transaction> transactions = DBUtil.executeQuery(sql, rs -> mapResultSetToTransaction(rs), transactionId);
            return transactions.isEmpty() ? null : transactions.get(0);
        } catch (SQLException | IOException e) {
            System.err.println("Error finding transaction by ID: " + transactionId + " - " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM Transactions";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToTransaction(rs));
        } catch (SQLException | IOException e) {
            System.err.println("Error finding all transactions: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
    }

    @Override
    public void save(Transaction transaction) {
        String sql = "INSERT INTO Transactions (orderId, userId, transactionType, amount, transactionDate) VALUES (?, ?, ?, ?, ?)";
        // Ensure transactionDate is not null
        Timestamp sqlTransactionDate = (transaction.getTransactionDate() != null) ? Timestamp.valueOf(transaction.getTransactionDate()) : new Timestamp(System.currentTimeMillis());

        // Handle potentially null orderId
        Integer orderId = transaction.getOrderId();

        try {
            int generatedId = DBUtil.executeInsertAndGetKey(sql,
                // Use setObject with Types.INTEGER for potentially null FKs
                (orderId != null ? orderId : null), // Pass null directly if orderId is null
                transaction.getUserId(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                sqlTransactionDate
            );

            if (generatedId != -1) {
                transaction.setTransactionId(generatedId);
            } else {
                 System.err.println("WARN: Transaction insert did not return a generated ID for userId: " + transaction.getUserId());
            }
        } catch (SQLException | IOException e) {
             System.err.println("Error saving transaction for userId: " + transaction.getUserId() + " - " + e.getMessage());
             // Consider rethrowing
        }
    }


    @Override
    public void update(Transaction transaction) {
        String sql = "UPDATE Transactions SET orderId = ?, userId = ?, transactionType = ?, amount = ?, transactionDate = ? WHERE transactionId = ?";
        Timestamp sqlTransactionDate = (transaction.getTransactionDate() != null) ? Timestamp.valueOf(transaction.getTransactionDate()) : null;
        Integer orderId = transaction.getOrderId(); // Handle null orderId

        try {
            int rowsAffected = DBUtil.executeUpdate(sql,
                (orderId != null ? orderId : null), // Use setObject for null FK
                transaction.getUserId(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                sqlTransactionDate,
                transaction.getTransactionId()
            );
             if (rowsAffected == 0) {
                 System.err.println("WARN: Update affected 0 rows for transactionId: " + transaction.getTransactionId());
             }
        } catch (SQLException | IOException e) {
            System.err.println("Error updating transaction: " + transaction.getTransactionId() + " - " + e.getMessage());
            // Consider rethrowing
        }
    }

    @Override
    public void delete(Integer transactionId) {
        String sql = "DELETE FROM Transactions WHERE transactionId = ?";
        try {
             int rowsAffected = DBUtil.executeUpdate(sql, transactionId);
             if (rowsAffected == 0) {
                 System.err.println("WARN: Delete affected 0 rows for transactionId: " + transactionId);
             }
        } catch (SQLException | IOException e) {
            System.err.println("Error deleting transaction: " + transactionId + " - " + e.getMessage());
            // Consider rethrowing
        }
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transactionId"));
        // Handle potential NULL orderId from DB
        int orderId = rs.getInt("orderId");
        transaction.setOrderId(rs.wasNull() ? null : orderId); // Set to null if DB value was NULL

        transaction.setUserId(rs.getInt("userId"));
        transaction.setTransactionType(rs.getString("transactionType"));
        transaction.setAmount(rs.getFloat("amount"));
        // Retrieve Timestamp and convert to LocalDateTime
        Timestamp ts = rs.getTimestamp("transactionDate");
        transaction.setTransactionDate(ts != null ? ts.toLocalDateTime() : null);
        return transaction;
    }
}