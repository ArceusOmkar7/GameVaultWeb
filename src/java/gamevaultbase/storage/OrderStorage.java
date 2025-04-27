package gamevaultbase.storage;

import gamevaultbase.entities.Order;
import gamevaultbase.interfaces.StorageInterface;
import gamevaultbase.helpers.DBUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp; // Use Timestamp for DATETIME/TIMESTAMP columns
import java.util.ArrayList;
import java.util.List;

public class OrderStorage implements StorageInterface<Order, Integer>{

    @Override
    public Order findById(Integer orderId) {
        String sql = "SELECT * FROM Orders WHERE orderId = ?";
        try {
            List<Order> orders = DBUtil.executeQuery(sql, rs -> mapResultSetToOrder(rs), orderId);
            return orders.isEmpty() ? null : orders.get(0);
        } catch (SQLException | IOException e) {
            System.err.println("Error finding order by ID: " + orderId + " - " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM Orders";
        try {
            return DBUtil.executeQuery(sql, rs -> mapResultSetToOrder(rs));
        } catch (SQLException | IOException e) {
            System.err.println("Error finding all orders: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
    }

    @Override
    public void save(Order order) {
        String sql = "INSERT INTO Orders (userId, totalAmount, orderDate) VALUES (?, ?, ?)";
        // Ensure orderDate is not null
        Timestamp sqlOrderDate = (order.getOrderDate() != null) ? new Timestamp(order.getOrderDate().getTime()) : new Timestamp(System.currentTimeMillis()); // Default to now if null

        try {
             int generatedId = DBUtil.executeInsertAndGetKey(sql,
                order.getUserId(),
                order.getTotalAmount(),
                sqlOrderDate // Use java.sql.Timestamp
             );

            if (generatedId != -1) {
                order.setOrderId(generatedId);
            } else {
                 System.err.println("WARN: Order insert did not return a generated ID for userId: " + order.getUserId());
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error saving order for userId: " + order.getUserId() + " - " + e.getMessage());
            // Consider rethrowing
        }
    }

    @Override
    public void update(Order order) {
        // Typically orders aren't updated, but if needed:
        String sql = "UPDATE Orders SET userId = ?, totalAmount = ?, orderDate = ? WHERE orderId = ?";
        Timestamp sqlOrderDate = (order.getOrderDate() != null) ? new Timestamp(order.getOrderDate().getTime()) : null;

        try {
             int rowsAffected = DBUtil.executeUpdate(sql,
                order.getUserId(),
                order.getTotalAmount(),
                sqlOrderDate,
                order.getOrderId()
             );
             if (rowsAffected == 0) {
                 System.err.println("WARN: Update affected 0 rows for orderId: " + order.getOrderId());
             }
        } catch (SQLException | IOException e) {
            System.err.println("Error updating order: " + order.getOrderId() + " - " + e.getMessage());
            // Consider rethrowing
        }
    }

    @Override
    public void delete(Integer orderId) {
        // Deleting an order should cascade delete OrderItems if set up correctly in DB
        String sql = "DELETE FROM Orders WHERE orderId = ?";
        try {
             int rowsAffected = DBUtil.executeUpdate(sql, orderId);
             if (rowsAffected == 0) {
                 System.err.println("WARN: Delete affected 0 rows for orderId: " + orderId);
             }
        } catch (SQLException | IOException e) {
            System.err.println("Error deleting order: " + orderId + " - " + e.getMessage());
            // Consider rethrowing
        }
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order =  new Order();
        order.setOrderId(rs.getInt("orderId"));
        order.setUserId(rs.getInt("userId"));
        order.setTotalAmount(rs.getDouble("totalAmount"));
        order.setOrderDate(rs.getTimestamp("orderDate")); // Retrieve as Timestamp, compatible with java.util.Date
        // Note: Does not load associated OrderItems here. That would require another query.
        return order;
    }
}