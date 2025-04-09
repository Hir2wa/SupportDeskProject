package Controller;

import model.Issue;
import model.Comment;
import model.Like;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IssueController {
    private Connection connection;

    public IssueController() {
        try {
            String url = "jdbc:postgresql://localhost:5432/support_desk";
            String user = "postgres";
            String pass = "2003";
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Issue DB connected!");
        } catch (Exception e) {
            System.out.println("❌ Issue DB connection failed");
            e.printStackTrace();
        }
    }

    // Post new issue (updated to include user_id)
    public boolean postIssue(Issue issue, int userId) {
        String sql = "INSERT INTO issues (user_id, title, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId); // Using user_id from method argument
            stmt.setString(2, issue.getTitle());
            stmt.setString(3, issue.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all issues
    public List<Issue> getAllIssues() {
        List<Issue> issues = new ArrayList<>();
        String sql = "SELECT * FROM issues ORDER BY id DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                issues.add(new Issue(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("user_id") // Capture the user_id as well
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return issues;
    }

    // Comment on an issue (updated to include user_id)
    public boolean addComment(Comment comment, int userId) {
        String sql = "INSERT INTO comments (issue_id, user_id, comment) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, comment.getIssueId());
            stmt.setInt(2, userId); // Pass user_id
            stmt.setString(3, comment.getContent());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Like an issue (updated to include user_id)
    public boolean likeIssue(Like like, int userId) {
        String sql = "INSERT INTO likes (issue_id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, like.getIssueId());
            stmt.setInt(2, userId); // Pass user_id
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
