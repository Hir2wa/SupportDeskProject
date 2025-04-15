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
    public boolean postIssue(Issue issue, int userId) {
        String sql = "INSERT INTO issues (user_id, title, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            System.out.println("Attempting to insert issue: " + issue.getTitle());
            System.out.println("User ID: " + userId);
            stmt.setInt(1, userId);
            stmt.setString(2, issue.getTitle());
            stmt.setString(3, issue.getDescription());
            int result = stmt.executeUpdate();
            System.out.println("Insert result: " + result);
            return result > 0;
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
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
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at"),
                    rs.getInt("likes")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return issues;
    }

    // Get issue by ID
    public Issue getIssueById(int issueId) {
        String sql = "SELECT * FROM issues WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, issueId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Issue(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at"),
                    rs.getInt("likes")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all comments for an issue
     * @param issueId The issue ID
     * @return List of comments
     */
    public List<Comment> getCommentsForIssue(int issueId) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM comments WHERE issue_id = ? ORDER BY created_at ASC";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, issueId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getInt("id"));
                comment.setIssueId(rs.getInt("issue_id"));
                comment.setUserId(rs.getInt("user_id"));
                comment.setContent(rs.getString("comment"));
                comment.setCreatedAt(rs.getTimestamp("created_at"));
                
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return comments;
    }

    // Comment on an issue (updated to include user_id)
    public boolean addComment(Comment comment, int userId) {
        String sql = "INSERT INTO comments (issue_id, user_id, comment) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, comment.getIssueId());
            stmt.setInt(2, userId);
            stmt.setString(3, comment.getContent());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a comment
    public boolean deleteComment(int commentId) {
        String sql = "DELETE FROM comments WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commentId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete an issue and its associated comments and likes
    public boolean deleteIssue(int issueId) {
        // First, delete associated comments
        String deleteCommentsSql = "DELETE FROM comments WHERE issue_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteCommentsSql)) {
            stmt.setInt(1, issueId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Then, delete associated likes
        String deleteLikesSql = "DELETE FROM likes WHERE issue_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteLikesSql)) {
            stmt.setInt(1, issueId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Finally, delete the issue
        String deleteIssueSql = "DELETE FROM issues WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteIssueSql)) {
            stmt.setInt(1, issueId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a user has already liked an issue
     * @param userId The user ID
     * @param issueId The issue ID
     * @return true if the user has already liked the issue, false otherwise
     */
    public boolean hasUserLikedIssue(int userId, int issueId) {
        String query = "SELECT COUNT(*) FROM likes WHERE user_id = ? AND issue_id = ? AND reaction_type = 'like'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, issueId);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if a user has already disliked an issue
     * @param userId The user ID
     * @param issueId The issue ID
     * @return true if the user has already disliked the issue, false otherwise
     */
    public boolean hasUserDislikedIssue(int userId, int issueId) {
        String query = "SELECT COUNT(*) FROM likes WHERE user_id = ? AND issue_id = ? AND reaction_type = 'dislike'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, issueId);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get the number of likes for an issue
     * @param issueId The issue ID
     * @return The number of likes
     */
    public int getLikeCount(int issueId) {
        String query = "SELECT COUNT(*) FROM likes WHERE issue_id = ? AND reaction_type = 'like'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, issueId);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get the number of dislikes for an issue
     * @param issueId The issue ID
     * @return The number of dislikes
     */
    public int getDislikeCount(int issueId) {
        String query = "SELECT COUNT(*) FROM likes WHERE issue_id = ? AND reaction_type = 'dislike'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, issueId);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Add a like to an issue if not already liked
     * @param like The like object (used for issue_id)
     * @param userId The user ID
     * @return true if the like was added, false if already liked or on error
     */
    public boolean likeIssue(Like like, int userId) {
        if (hasUserLikedIssue(userId, like.getIssueId())) {
            return false;
        }
        
        String deleteQuery = "DELETE FROM likes WHERE user_id = ? AND issue_id = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, userId);
            deleteStmt.setInt(2, like.getIssueId());
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        String query = "INSERT INTO likes (user_id, issue_id, reaction_type) VALUES (?, ?, 'like')";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, like.getIssueId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add a dislike to an issue if not already disliked
     * @param like The like object (used for issue_id)
     * @param userId The user ID
     * @return true if the dislike was added, false if already disliked or on error
     */
    public boolean dislikeIssue(Like like, int userId) {
        if (hasUserDislikedIssue(userId, like.getIssueId())) {
            return false;
        }
        
        String deleteQuery = "DELETE FROM likes WHERE user_id = ? AND issue_id = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, userId);
            deleteStmt.setInt(2, like.getIssueId());
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        String query = "INSERT INTO likes (user_id, issue_id, reaction_type) VALUES (?, ?, 'dislike')";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, like.getIssueId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Remove a dislike from an issue
     * @param userId The user ID
     * @param issueId The issue ID
     * @return true if the dislike was removed, false otherwise
     */
    public boolean undislikeIssue(int userId, int issueId) {
        String query = "DELETE FROM likes WHERE user_id = ? AND issue_id = ? AND reaction_type = 'dislike'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, issueId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Remove a like from an issue
     * @param userId The user ID
     * @param issueId The issue ID
     * @return true if the like was removed, false otherwise
     */
    public boolean unlikeIssue(int userId, int issueId) {
        String query = "DELETE FROM likes WHERE user_id = ? AND issue_id = ? AND reaction_type = 'like'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, issueId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🧮 Count total issues submitted by a user
    public int countIssuesByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM issues WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ❤️ Count likes received on user's issues
    public int countLikesReceivedByUserId(int userId) {
        String sql = "SELECT SUM(likes) FROM issues WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 💬 Count comments received on user's issues
    public int countCommentsReceivedByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM comments WHERE issue_id IN (SELECT id FROM issues WHERE user_id = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 💭 Count comments made by the user on any issue
    public int countCommentsMadeByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM comments WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}