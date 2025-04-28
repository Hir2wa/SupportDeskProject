package Controller;

import model.User;
import model.Report;
import model.Issue;
import model.Notice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    private Connection connection;
    private UserController userController;
    private ReportController reportController;
    private IssueController issueController;

    public AdminController() {
        try {
            String url = "jdbc:postgresql://localhost:5432/support_desk";
            String user = "postgres";
            String pass = "2003";
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Admin DB connected!");
            
            // Initialize other controllers
            userController = new UserController();
            reportController = new ReportController();
            issueController = new IssueController();
        } catch (Exception e) {
            System.out.println("❌ Admin DB connection failed");
            e.printStackTrace();
        }
    }
    
    /**
     * Get all reports in the system
     * @return List of reports
     */
    public List<Report> getReports() {
        return reportController.getAllReports();
    }
    
    /**
     * Block a user by setting their status to 'blocked'
     * @param userId The user ID to block
     * @return true if successful, false otherwise
     */
    public boolean blockUser(int userId) {
        String sql = "UPDATE users SET status = 'blocked', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("❌ Failed to block user");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Unblock a user by setting their status to 'active'
     * @param userId The user ID to unblock
     * @return true if successful, false otherwise
     */
    public boolean unblockUser(int userId) {
        String sql = "UPDATE users SET status = 'active', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("❌ Failed to unblock user");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete an issue and all associated comments and likes
     * @param issueId The issue ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteIssue(int issueId) {
        try {
            // Start transaction
            connection.setAutoCommit(false);
            
            // Delete comments associated with the issue
            String deleteComments = "DELETE FROM comments WHERE issue_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteComments)) {
                stmt.setInt(1, issueId);
                stmt.executeUpdate();
            }
            
            // Delete likes associated with the issue
            String deleteLikes = "DELETE FROM likes WHERE issue_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteLikes)) {
                stmt.setInt(1, issueId);
                stmt.executeUpdate();
            }
            
            // Delete reports associated with the issue
            String deleteReports = "DELETE FROM reports WHERE issue_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteReports)) {
                stmt.setInt(1, issueId);
                stmt.executeUpdate();
            }
            
            // Delete the issue
            String deleteIssue = "DELETE FROM issues WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteIssue)) {
                stmt.setInt(1, issueId);
                int result = stmt.executeUpdate();
                
                // Commit transaction
                connection.commit();
                connection.setAutoCommit(true);
                
                return result > 0;
            }
        } catch (SQLException e) {
            try {
                // Rollback transaction in case of error
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("❌ Failed to delete issue");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Post a new notice for all users
     * @param title Notice title
     * @param content Notice content
     * @param postedBy User ID of the admin posting the notice
     * @return true if successful, false otherwise
     */
    public boolean postNotice(String title, String content, int postedBy) {
        String sql = "INSERT INTO notices (title, content, posted_by) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setInt(3, postedBy);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("❌ Failed to post notice");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get all notices ordered by creation date
     * @return List of notices
     */
    public List<Notice> getAllNotices() {
        List<Notice> notices = new ArrayList<>();
        String sql = "SELECT * FROM notices ORDER BY created_at DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Notice notice = new Notice();
                notice.setId(rs.getInt("id"));
                notice.setTitle(rs.getString("title"));
                notice.setContent(rs.getString("content"));
                notice.setPostedBy(rs.getInt("posted_by"));
                notice.setCreatedAt(rs.getTimestamp("created_at"));
                notices.add(notice);
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to get notices");
            e.printStackTrace();
        }
        
        return notices;
    }
    
    /**
     * Delete a notice
     * @param noticeId The notice ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteNotice(int noticeId) {
        String sql = "DELETE FROM notices WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, noticeId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("❌ Failed to delete notice");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get all users in the system
     * @return List of users
     */

     /**
 * Get all users in the system
 * @return List of users
 */




 public model.AdminStats getSystemStats() {
    model.AdminStats stats = new model.AdminStats();
    
    try {
        // Total users
        String userSql = "SELECT COUNT(*) FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(userSql)) {
            if (rs.next()) {
                stats.setTotalUsers(rs.getInt(1));
            }
        }
        
        // Total issues
        String issueSql = "SELECT COUNT(*) FROM issues";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(issueSql)) {
            if (rs.next()) {
                stats.setTotalIssues(rs.getInt(1));
            }
        }
        
        // Total comments
        String commentSql = "SELECT COUNT(*) FROM comments";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(commentSql)) {
            if (rs.next()) {
                stats.setTotalComments(rs.getInt(1));
            }
        }
        
        // Active reports
        String reportSql = "SELECT COUNT(*) FROM reports";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(reportSql)) {
            if (rs.next()) {
                stats.setActiveReports(rs.getInt(1));
            }
        }
        
        // Recent activity - issues created in the last 7 days
        String recentActivitySql = "SELECT COUNT(*) FROM issues WHERE created_at >= NOW() - INTERVAL '7 days'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(recentActivitySql)) {
            if (rs.next()) {
                stats.setRecentActivity(rs.getInt(1));
            }
        }
        
    } catch (SQLException e) {
        System.out.println("❌ Failed to get system stats");
        e.printStackTrace();
    }
    
    return stats;
}



public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM users ORDER BY id";
    
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            User user = new User(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getBoolean("is_admin"),
                rs.getBoolean("is_blocked")
            );
            user.setCreatedAt(rs.getTimestamp("created_at"));
            user.setUpdatedAt(rs.getTimestamp("updated_at"));
            users.add(user);
        }
    } catch (SQLException e) {
        System.out.println("❌ Failed to get users");
        e.printStackTrace();
    }
    
    return users;
}
  
    
    // Removed duplicate method to resolve the issue
    
    // Inner class to hold system statistics
    public class AdminStats {
        private int totalUsers;
        private int totalIssues;
        private int totalComments;
        private int activeReports;
        private int recentActivity;
        
        public int getTotalUsers() {
            return totalUsers;
        }
        
        public void setTotalUsers(int totalUsers) {
            this.totalUsers = totalUsers;
        }
        
        public int getTotalIssues() {
            return totalIssues;
        }
        
        public void setTotalIssues(int totalIssues) {
            this.totalIssues = totalIssues;
        }
        
        public int getTotalComments() {
            return totalComments;
        }
        
        public void setTotalComments(int totalComments) {
            this.totalComments = totalComments;
        }
        
        public int getActiveReports() {
            return activeReports;
        }
        
        public void setActiveReports(int activeReports) {
            this.activeReports = activeReports;
        }
        
        public int getRecentActivity() {
            return recentActivity;
        }
        
        public void setRecentActivity(int recentActivity) {
            this.recentActivity = recentActivity;
        }
    }


    

        
         
        
    
}