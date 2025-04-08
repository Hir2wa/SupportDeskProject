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

    // Post new issue
    public boolean postIssue(Issue issue) {
        String sql = "INSERT INTO issues (title, description, username) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, issue.getTitle());
            stmt.setString(2, issue.getDescription());
            stmt.setString(3, issue.getUsername());
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
                    rs.getString("username")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return issues;
    }

    // Comment on an issue
    public boolean addComment(Comment comment) {
        String sql = "INSERT INTO comments (issue_id, username, content) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, comment.getIssueId());
            stmt.setString(2, comment.getUsername());
            stmt.setString(3, comment.getContent());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Like an issue
    public boolean likeIssue(Like like) {
        String sql = "INSERT INTO likes (issue_id, username) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, like.getIssueId());
            stmt.setString(2, like.getUsername());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
