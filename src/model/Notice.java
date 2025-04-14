package model;

import java.sql.Timestamp;

public class Notice {
    private int id;
    private String title;
    private String content;
    private int postedBy;
    private Timestamp createdAt;
    
    public Notice() {
    }
    
    public Notice(int id, String title, String content, int postedBy, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.postedBy = postedBy;
        this.createdAt = createdAt;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public int getPostedBy() {
        return postedBy;
    }
    
    public void setPostedBy(int postedBy) {
        this.postedBy = postedBy;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
