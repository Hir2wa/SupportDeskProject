package model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String fullName;
    private String email;
    private String password;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean isAdmin;
    private boolean isBlocked;
    
    // Constructors
    public User() {}
    
    public User(int id, String fullName, String username, String email, String password) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    

    public User(int id, String fullName, String username, String email, String password, boolean isAdmin, boolean isBlocked) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isBlocked = isBlocked;
    }
    
    public User(String fullName, String username, String email, String password) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public boolean isBlocked() {
        return isBlocked;
    }
    
    public void setBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
}