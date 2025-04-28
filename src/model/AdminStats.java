package model;

public class AdminStats {
    private int totalUsers;
    private int totalIssues;
    private int totalComments;
    private int activeReports;
    private int recentActivity;

    public AdminStats() {}

    public AdminStats(int users, int issues, int comments, int reports, int recentActivity) {
        this.totalUsers = users;
        this.totalIssues = issues;
        this.totalComments = comments;
        this.activeReports = reports;
        this.recentActivity = recentActivity;
    }

    public int getTotalUsers() { return totalUsers; }
    public int getTotalIssues() { return totalIssues; }
    public int getTotalComments() { return totalComments; }
    public int getActiveReports() { return activeReports; }
    public int getRecentActivity() { return recentActivity; }

    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }
    public void setTotalIssues(int totalIssues) { this.totalIssues = totalIssues; }
    public void setTotalComments(int totalComments) { this.totalComments = totalComments; }
    public void setActiveReports(int activeReports) { this.activeReports = activeReports; }
    public void setRecentActivity(int recentActivity) { this.recentActivity = recentActivity; }
}