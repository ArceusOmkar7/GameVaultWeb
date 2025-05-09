package gamevaultbase.storage;

import gamevaultbase.entities.Order;
import gamevaultbase.entities.Game;
import gamevaultbase.interfaces.StorageInterface;
import gamevaultbase.helpers.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderStorage implements StorageInterface<Order, Integer> {

    private GameStorage gameStorage;

    // Default constructor for AppContextListener
    public OrderStorage() {
        this.gameStorage = null; // Will be set via setGameStorage method
    }

    public OrderStorage(GameStorage gameStorage) {
        this.gameStorage = gameStorage;
    }

    // Setter for GameStorage to use after construction if needed
    public void setGameStorage(GameStorage gameStorage) {
        this.gameStorage = gameStorage;
    }

    @Override
    public Order findById(Integer orderId) {
        String sql = "SELECT * FROM Orders WHERE orderId = ?";
        try {
            List<Order> orders = DBUtil.executeQuery(sql, rs -> {
                Order order = mapResultSetToOrder(rs);
                order.setOrderItems(findOrderItems(order.getOrderId()));
                return order;
            }, orderId);
            return orders.isEmpty() ? null : orders.get(0);
        } catch (SQLException e) {
            System.err.println("Error finding order by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM Orders ORDER BY orderDate DESC";
        try {
            List<Order> orders = DBUtil.executeQuery(sql, rs -> mapResultSetToOrder(rs));
            // For each order, load its order items
            for (Order order : orders) {
                order.setOrderItems(findOrderItems(order.getOrderId()));
            }
            return orders;
        } catch (SQLException e) {
            System.err.println("Error finding all orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void save(Order order) {
        String orderSql = "INSERT INTO Orders (userId, totalAmount, orderDate, status) VALUES (?, ?, ?, ?)";
        try {
            // Insert order
            int orderId = DBUtil.executeInsertAndGetKey(orderSql,
                    order.getUserId(),
                    order.getTotalAmount(),
                    order.getOrderDate() != null ? new Timestamp(order.getOrderDate().getTime())
                            : new Timestamp(System.currentTimeMillis()),
                    order.getStatus() != null ? order.getStatus() : "COMPLETED");

            if (orderId == -1) {
                System.err.println("Failed to get generated order ID");
                return;
            }

            order.setOrderId(orderId); // Set the generated order ID

            // Insert order items
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                String itemSql = "INSERT INTO OrderItems (orderId, gameId, priceAtPurchase) VALUES (?, ?, ?)";
                for (Map.Entry<Game, Float> entry : order.getOrderItems().entrySet()) {
                    Game game = entry.getKey();
                    Float price = entry.getValue();
                    DBUtil.executeUpdate(itemSql, orderId, game.getGameId(), price);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving order: " + e.getMessage());
        }
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE Orders SET userId = ?, totalAmount = ?, orderDate = ? WHERE orderId = ?";
        try {
            DBUtil.executeUpdate(sql,
                    order.getUserId(),
                    order.getTotalAmount(),
                    order.getOrderDate() != null ? new Timestamp(order.getOrderDate().getTime()) : null,
                    order.getOrderId());
        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer orderId) {
        // Delete order items first (due to foreign key constraint)
        String deleteItemsSql = "DELETE FROM OrderItems WHERE orderId = ?";
        // Then delete the order
        String deleteOrderSql = "DELETE FROM Orders WHERE orderId = ?";
        try {
            DBUtil.executeUpdate(deleteItemsSql, orderId);
            DBUtil.executeUpdate(deleteOrderSql, orderId);
        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
        }
    }

    public List<Order> findByUserId(Integer userId) {
        String sql = "SELECT * FROM Orders WHERE userId = ? ORDER BY orderDate DESC";
        try {
            List<Order> orders = DBUtil.executeQuery(sql, rs -> mapResultSetToOrder(rs), userId);
            // For each order, load its order items
            for (Order order : orders) {
                order.setOrderItems(findOrderItems(order.getOrderId()));
            }
            return orders;
        } catch (SQLException e) {
            System.err.println("Error finding orders by user ID: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private Map<Game, Float> findOrderItems(int orderId) {
        if (gameStorage == null) {
            throw new IllegalStateException("GameStorage not initialized. Please ensure proper initialization.");
        }

        String sql = "SELECT oi.*, g.* FROM OrderItems oi " +
                "JOIN Games g ON oi.gameId = g.gameId " +
                "WHERE oi.orderId = ?";
        try {
            Map<Game, Float> orderItems = new HashMap<>();
            List<Object[]> results = DBUtil.executeQuery(sql, rs -> {
                Game game = gameStorage.mapResultSetToGame(rs);
                float priceAtPurchase = rs.getFloat("priceAtPurchase");
                return new Object[] { game, priceAtPurchase };
            }, orderId);

            for (Object[] result : results) {
                Game game = (Game) result[0];
                Float price = (Float) result[1];
                orderItems.put(game, price);
            }
            return orderItems;
        } catch (SQLException e) {
            System.err.println("Error finding order items: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("orderId"));
        order.setUserId(rs.getInt("userId"));
        order.setTotalAmount(rs.getDouble("totalAmount"));
        order.setOrderDate(rs.getTimestamp("orderDate"));
        try {
            order.setStatus(rs.getString("status"));
        } catch (Exception e) {
            order.setStatus("COMPLETED");
        }
        return order;
    }
}