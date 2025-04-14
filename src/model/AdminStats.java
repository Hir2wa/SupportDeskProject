package model;

public class AdminStats {
    private int totalUsers;
    private int totalIssues;
    private int totalComments;
    private int activeReports;

    public AdminStats(int users, int issues, int comments, int reports) {
        this.totalUsers = users;
        this.totalIssues = issues;
        this.totalComments = comments;
        this.activeReports = reports;
    }

    public int getTotalUsers() { return totalUsers; }
    public int getTotalIssues() { return totalIssues; }
    public int getTotalComments() { return totalComments; }
    public int getActiveReports() { return activeReports; }

    
}
