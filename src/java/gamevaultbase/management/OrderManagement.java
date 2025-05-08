// src/java/gamevaultbase/management/OrderManagement.java
package gamevaultbase.management;

import gamevaultbase.entities.Cart;
import gamevaultbase.entities.Game;
import gamevaultbase.entities.Order;
import gamevaultbase.entities.Transaction;
import gamevaultbase.entities.User;
import gamevaultbase.exceptions.CartEmptyException;
import gamevaultbase.exceptions.OrderNotFoundException;
import gamevaultbase.exceptions.UserNotFoundException; // Keep this
import gamevaultbase.storage.CartStorage;
import gamevaultbase.storage.OrderStorage;
import gamevaultbase.storage.UserStorage;
import gamevaultbase.helpers.DBUtil;
import java.io.IOException;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
// Remove unused loggers if any
// import java.util.logging.Level;
// import java.util.logging.Logger;

public class OrderManagement {

    private final OrderStorage orderStorage;
    private final CartStorage cartStorage;
    private final UserStorage userStorage;
    private final TransactionManagement transactionManagement;

    public OrderManagement(OrderStorage orderStorage, CartStorage cartStorage, UserStorage userStorage,
            TransactionManagement transactionManagement) {
        this.orderStorage = orderStorage;
        this.cartStorage = cartStorage;
        this.userStorage = userStorage;
        this.transactionManagement = transactionManagement;
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
    public void placeOrder(int userId) throws CartEmptyException, UserNotFoundException, IllegalStateException {
        Cart cart = cartStorage.findById(userId);
        // Cart might not exist explicitly in Carts table if user added item directly
        // Rely on getGamesInCart to determine if there's anything to order
        // if (cart == null) {
        //     throw new CartEmptyException("Cart does not exist for user: " + userId);
        // }

        List<Game> games = cartStorage.getGamesInCart(userId); // Get games from CartItems table
        if (games.isEmpty()) {
            throw new CartEmptyException("Cart is empty for user: " + userId);
        }

        double totalAmount = games.stream().mapToDouble(Game::getPrice).sum();

        // Check if user has sufficient balance
        User user = userStorage.findById(userId);
        if (user == null) {
            // This should ideally not happen if the user is logged in, but check anyway
             System.err.println("Error placing order: User not found with ID " + userId);
             throw new UserNotFoundException("User not found, cannot place order.");
        }
        if (user.getWalletBalance() < totalAmount) {
            throw new IllegalStateException("Insufficient balance. Your balance is " + user.getWalletBalance() + ", but the total is " + totalAmount);
        }

        Order savedOrder = null; // To hold the order after it gets an ID

        // Attempt to update the user's balance AND create the order atomically
        // NOTE: True atomicity requires database transactions, which DBUtil doesn't manage.
        // This is a best-effort approach without full transaction management.
        try {
            // 1. Update user wallet
            String sqlUpdateUser = "UPDATE Users SET walletBalance = walletBalance - ? WHERE userId = ? AND walletBalance >= ?";
            int rowsAffectedUser = DBUtil.executeUpdate(sqlUpdateUser, totalAmount, userId, totalAmount); // Add balance check
            if (rowsAffectedUser == 0) {
                 // Re-fetch user to see if balance was the issue
                 User checkUser = userStorage.findById(userId);
                 if(checkUser != null && checkUser.getWalletBalance() < totalAmount) {
                     throw new IllegalStateException("Insufficient balance. Your balance is " + checkUser.getWalletBalance() + ", but the total is " + totalAmount);
                 }
                 // Some other reason update failed
                throw new IllegalStateException("Failed to update user balance. Possibly concurrent update or user not found.");
            }

            // 2. Create order (after user update)
            Order order = new Order(userId, totalAmount, new Date());
            orderStorage.save(order); // This sets the orderId on the 'order' object
            savedOrder = order; // Keep track of the saved order with its ID

            if (savedOrder.getOrderId() <= 0) {
                 throw new SQLException("Failed to retrieve generated order ID after saving.");
            }


            // 3. Create OrderItems records for each game in the cart
            for (Game game : games) {
                // ***** FIX IS HERE *****
                String sqlInsertOrderItem = "INSERT INTO OrderItems (orderId, gameId, priceAtPurchase) VALUES (?, ?, ?)";
                // **********************
                DBUtil.executeUpdate(sqlInsertOrderItem, savedOrder.getOrderId(), game.getGameId(), game.getPrice());
            }

            // 4. Create a transaction for the order
            Transaction transaction = new Transaction(null, savedOrder.getOrderId(), userId, "Purchase", (float) totalAmount,
                    LocalDateTime.now());
            transactionManagement.addTransaction(transaction);

            // 5. Clear the cart items *after* everything else succeeds
            cartStorage.clearCart(userId);

        } catch (SQLException e) {
             System.err.println("Order processing failed: " + e.getMessage());
             e.printStackTrace(); // Log the full stack trace

             // Attempt to rollback manually (best effort without real transactions)
             // If user balance was deducted but order failed, try to add it back.
             if (savedOrder == null) { // Failed before or during order save
                 try {
                      System.err.println("Attempting to rollback user balance deduction for userId: " + userId);
                      String sqlRollbackUser = "UPDATE Users SET walletBalance = walletBalance + ? WHERE userId = ?";
                      DBUtil.executeUpdate(sqlRollbackUser, totalAmount, userId);
                 } catch (Exception rollbackEx) {
                      System.err.println("FATAL: Failed to rollback user balance! Manual correction needed for userId: " + userId + ", amount: " + totalAmount);
                      // Log this critical failure
                 }
             }
             // If order was saved but items/transaction failed, deleting the order might be an option,
             // but could leave dangling transactions/items depending on FK constraints.
             // This highlights the need for proper DB transaction management.

             // Rethrow a user-friendly exception
            throw new IllegalStateException("Order processing failed due to a database error. Please try again later.", e);
        } catch (IllegalStateException | CartEmptyException | UserNotFoundException e) {
             // Catch specific logical exceptions and rethrow them
             throw e;
        }
    }
}