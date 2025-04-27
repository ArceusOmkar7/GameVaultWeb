package gamevaultbase.management;

import gamevaultbase.entities.Cart;
import gamevaultbase.entities.Game;
import gamevaultbase.entities.Order;
import gamevaultbase.entities.Transaction;
import gamevaultbase.entities.User;
import gamevaultbase.exceptions.CartEmptyException;
import gamevaultbase.exceptions.OrderNotFoundException;
import gamevaultbase.storage.CartStorage;
import gamevaultbase.storage.OrderStorage;
import gamevaultbase.storage.UserStorage;
import gamevaultbase.helpers.DBUtil;
import java.io.IOException;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderManagement {

    private final OrderStorage orderStorage;
    private final CartStorage cartStorage;
    private final UserStorage userStorage;
    private final TransactionManagement transactionManagement; // Added

    public OrderManagement(OrderStorage orderStorage, CartStorage cartStorage, UserStorage userStorage,
            TransactionManagement transactionManagement) {
        this.orderStorage = orderStorage;
        this.cartStorage = cartStorage;
        this.userStorage = userStorage;
        this.transactionManagement = transactionManagement; // Added
    }

    public Order getOrder(int orderId) throws OrderNotFoundException {
        Order order = orderStorage.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }

    public List<Order> getAllOrders() {
        return orderStorage.findAll();
    }

    // Places an order for a user, creating a transaction, clearing the cart, and
    // updating the user's wallet balance.
    public void placeOrder(int userId) throws CartEmptyException {
        Cart cart = cartStorage.findById(userId);
        if (cart == null) {
            throw new CartEmptyException("Cart does not exist for user: " + userId);
        }

        List<Game> games = cartStorage.getGamesInCart(userId); // Get games from CartItems table
        if (games.isEmpty()) {
            throw new CartEmptyException("Cart is empty for user: " + userId);
        }

        double totalAmount = games.stream().mapToDouble(Game::getPrice).sum();

        // Check if user has sufficient balance
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new IllegalStateException("User not found");
        }
        if (user.getWalletBalance() < totalAmount) {
            throw new IllegalStateException("Insufficient balance");
        }

        // Attempt to update the user's balance AND create the order atomically
        try {
            // 1. Update user wallet
            String sqlUpdateUser = "UPDATE Users SET walletBalance = walletBalance - ? WHERE userId = ?";
            int rowsAffectedUser = DBUtil.executeUpdate(sqlUpdateUser, totalAmount, userId);
            if (rowsAffectedUser == 0) {
                throw new IllegalStateException("Failed to update user balance. Possibly concurrent update.");
            }

            // 2. Create order (after user update)
            Order order = new Order(userId, totalAmount, new Date());
            orderStorage.save(order);
            // set all values for the order object
            List<Order> orders = orderStorage.findAll();
            order = orders.get(orders.size() - 1);

            // 3. Create OrderItems records for each game in the cart
            for (Game game : games) {
                String sqlInsertOrderItem = "INSERT INTO OrderItems (orderId, gameId, price) VALUES (?, ?, ?)";
                DBUtil.executeUpdate(sqlInsertOrderItem, order.getOrderId(), game.getGameId(), game.getPrice());
            }

            // 4. Create a transaction for the order
            Transaction transaction = new Transaction(null, order.getOrderId(), userId, "Purchase", (float) totalAmount,
                    LocalDateTime.now());
            transactionManagement.addTransaction(transaction);

            // 5. Clear the cart after placing the order
            cartStorage.clearCart(userId);

        } catch (SQLException | IOException e) {
            System.err.println("Transaction failed. Rolling back partially completed operations: " + e.getMessage());
            throw new IllegalStateException("Transaction failed: " + e.getMessage()); // Replace with custom exception
        }
    }
}