package gamevaultbase.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class User {
    private int userId;
    private String email;
    private String password;
    private String username;
    private float walletBalance;
    private Date createdAt;
    private boolean isAdmin;

    // No-args constructor required for JSON deserialization
    public User() {
        this.createdAt = new Date();
    }

    public User(String email, String password, String username, float walletBalance) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.walletBalance = walletBalance;
        this.createdAt = new Date();
        this.isAdmin = false; // Default to regular user
    }

    // New constructor that includes admin flag
    public User(String email, String password, String username, float walletBalance, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.walletBalance = walletBalance;
        this.createdAt = new Date();
        this.isAdmin = isAdmin;
    }

    public User(int userId, String email, String password, String username, float walletBalance, Date createdAt) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.username = username;
        this.walletBalance = walletBalance;
        this.createdAt = createdAt;
        this.isAdmin = false; // Default to regular user
    }

    // Constructor that includes all fields including admin flag
    public User(int userId, String email, String password, String username, float walletBalance, Date createdAt,
            boolean isAdmin) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.username = username;
        this.walletBalance = walletBalance;
        this.createdAt = createdAt;
        this.isAdmin = isAdmin;
    }

    @JsonProperty("userId")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("walletBalance")
    public float getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(float walletBalance) {
        this.walletBalance = walletBalance;
    }

    @JsonProperty("createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("admin")
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}