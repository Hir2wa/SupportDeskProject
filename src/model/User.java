package model;

public class User {
    private int id;
    private String fullName;
    private String username;
    private String email;
    private String password;
    private int issuesSubmitted;
    private int likesReceived;
    private int commentsReceived;
    private int commentsMade;
    private String likeStatus;

    // === Constructors ===

    public User() {}

    public User(int id, String fullName, String username, String email, String password,
                int issuesSubmitted, int likesReceived, int commentsReceived, int commentsMade, String likeStatus) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.issuesSubmitted = issuesSubmitted;
        this.likesReceived = likesReceived;
        this.commentsReceived = commentsReceived;
        this.commentsMade = commentsMade;
        this.likeStatus = likeStatus;
    }

    public User(String fullName, String username, String email, String password) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // === Getters and Setters ===

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }
}
