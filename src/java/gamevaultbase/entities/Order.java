package gamevaultbase.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private int orderId;
    private int userId;
    private double totalAmount;
    private Date orderDate;
    private Map<Game, Float> orderItems; // Added to store the order items

    public Order(int userId, double totalAmount, Date orderDate) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.orderItems = new HashMap<>();
    }

    public Order() {
        this.orderItems = new HashMap<>();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Map<Game, Float> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Map<Game, Float> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(Game game, Float price) {
        this.orderItems.put(game, price);
    }

    public void removeOrderItem(Game game) {
        this.orderItems.remove(game);
    }
}