package Controller;

import model.User;
import java.sql.*;

public class UserController {
    private Connection connection;

    public UserController() {
        try {
            String url = "jdbc:postgresql://localhost:5432/support_desk";
            String user = "postgres";
            String pass = "2003";

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ DB connected successfully!");
        } catch (Exception e) {
            System.out.println("❌ Failed to connect to DB");
            e.printStackTrace();
        }
    }

    // 🔐 Register a new user
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (full_name, username, email, password) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("⚠️ Registration failed");
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Login (returns userId after successful login)
// In UserController
public boolean loginUser(String username, String password) {
    String sql = "SELECT * FROM users WHERE username = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String storedPass = rs.getString("password");
            if (password.equals(storedPass)) {
                return true; // Return true if login is successful
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Return false if login fails
}


    // 🛠️ Update user stats
    public boolean updateUserStats(User user) {
        String sql = "UPDATE users SET issues_submitted = ?, likes_received = ?, comments_received = ?, comments_made = ? WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getIssuesSubmitted());
            stmt.setInt(2, user.getLikesReceived());
            stmt.setInt(3, user.getCommentsReceived());
            stmt.setInt(4, user.getCommentsMade());
            stmt.setString(5, user.getUsername());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 📦 Fetch a user object (e.g., for profile or dashboard)
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                return new User(
                    rs.getInt("id"), // Return the userId
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("issues_submitted"),
                    rs.getInt("likes_received"),
                    rs.getInt("comments_received"),
                    rs.getInt("comments_made"),
                    rs.getString("like_status") // Assuming `like_status` exists in the database
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
