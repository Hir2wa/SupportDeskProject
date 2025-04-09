package model;

import java.sql.Timestamp;

public class Comment {
    private int id;
    private int userId;
    private String comment;
    private Timestamp createdAt;
    private Integer issueId;  // Using Integer to allow null
    
    // Constructors
    public Comment() {}
    
    public Comment(int userId, String comment, Integer issueId) {
        this.userId = userId;
        this.comment = comment;
        this.issueId = issueId;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String getContent() {
        return comment; // Alias for getComment() for compatibility
    }
    
    public void setContent(String content) {
        this.comment = content; // Alias for setComment() for compatibility
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Integer getIssueId() {
        return issueId;
    }
    
    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }
}