package model;

public class Issue {
    private int id;
    private String title;
    private String description;
    private int userId; // Changed from String username to int userId

    public Issue() {}

    // Constructor with userId instead of username
    public Issue(int id, String title, String description, int userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    // Constructor without id, suitable for posting a new issue
    public Issue(String title, String description, int userId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() { // Getter for userId
        return userId;
    }

    public void setUserId(int userId) { // Setter for userId
        this.userId = userId;
    }
}
