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

    // ✅ Login (returns true for successful login)
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

    // 📦 Fetch a user object by username
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                
                // Optional: Set timestamps if you need them
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // 📦 Fetch a user object by ID
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                
                // Optional: Set timestamps if you need them
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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


// 🔄 Update user information
public boolean updateUser(int userId, String username, String email, String password) {
    // If password is empty, don't update it
    if (password.isEmpty()) {
        String sql = "UPDATE users SET username = ?, email = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setInt(3, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("⚠️ Update failed");
            e.printStackTrace();
            return false;
        }
    } else {
        // Update including password
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setInt(4, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("⚠️ Update failed");
            e.printStackTrace();
            return false;
        }
    }
}

}