package model;

public class User {
    private String fullName;
    private String username;
    private String email;
    private String password;
    private int issuesSubmitted;
    private int likesReceived;
    private int commentsReceived;
    private int commentsMade;

    // === Constructors ===

    public User() {}

    public User(String fullName, String username, String email, String password) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String fullName, String username, String email, String password,
                int issuesSubmitted, int likesReceived,
                int commentsReceived, int commentsMade) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.issuesSubmitted = issuesSubmitted;
        this.likesReceived = likesReceived;
        this.commentsReceived = commentsReceived;
        this.commentsMade = commentsMade;
    }

    // === Getters and Setters ===

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getIssuesSubmitted() {
        return issuesSubmitted;
    }

    public void setIssuesSubmitted(int issuesSubmitted) {
        this.issuesSubmitted = issuesSubmitted;
    }

    public int getLikesReceived() {
        return likesReceived;
    }

    public void setLikesReceived(int likesReceived) {
        this.likesReceived = likesReceived;
    }

    public int getCommentsReceived() {
        return commentsReceived;
    }

    public void setCommentsReceived(int commentsReceived) {
        this.commentsReceived = commentsReceived;
    }

    public int getCommentsMade() {
        return commentsMade;
    }

    public void setCommentsMade(int commentsMade) {
        this.commentsMade = commentsMade;
    }
}
