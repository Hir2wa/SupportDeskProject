package model;

import java.sql.Timestamp;

public class Report {
    private int id;
    private int reportedBy;
    private Integer commentId;  // Using Integer to allow null
    private Integer issueId;    // Using Integer to allow null
    private String reason;
    private Timestamp createdAt;
    
    // Constructors
    public Report() {}
    
    public Report(int reportedBy, Integer commentId, Integer issueId, String reason) {
        this.reportedBy = reportedBy;
        this.commentId = commentId;
        this.issueId = issueId;
        this.reason = reason;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getReportedBy() {
        return reportedBy;
    }
    
    public void setReportedBy(int reportedBy) {
        this.reportedBy = reportedBy;
    }
    
    public Integer getCommentId() {
        return commentId;
    }
    
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
    
    public Integer getIssueId() {
        return issueId;
    }
    
    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
