package Controller;

import model.User;
import java.sql.*;
import java.util.ArrayList;

public class UserController {
    private Connection connection;
    private static int currentLoggedInUserId = -1;
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

    public boolean loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPass = rs.getString("password");
                if (password.equals(storedPass)) {
                    // If login successful, set the current user ID
                    User user = getUserByUsername(username);
                    if (user != null) {
                        currentLoggedInUserId = user.getId();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if login fails
    }

    public int getCurrentLoggedInUserId() {
        return currentLoggedInUserId;
    }

    public void logout() {
        currentLoggedInUserId = -1;
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
    // Count likes where the user's issues were liked
    String sql = "SELECT COUNT(*) FROM likes l JOIN issues i ON l.issue_id = i.id WHERE i.user_id = ?";
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
            System.out.println("Attempting to update user ID: " + userId);
            System.out.println("New username: " + username);
            System.out.println("New email: " + email);
            // After executing the statement:
            System.out.println("Rows affected: " + rows);
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

// 🔍 Search for users by username or email pattern
public ArrayList<User> searchUsers(String searchQuery) {
    System.out.println("Searching for users with query: " + searchQuery);
    
    // Use ILIKE for case-insensitive search in PostgreSQL
    String sql = "SELECT * FROM users WHERE username ILIKE ? OR email ILIKE ? OR full_name ILIKE ? OR id::text = ?";
    ArrayList<User> results = new ArrayList<>();
    
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        String pattern = "%" + searchQuery + "%";
        stmt.setString(1, pattern);
        stmt.setString(2, pattern);
        stmt.setString(3, pattern);
        stmt.setString(4, searchQuery); // Exact match for ID
        
        ResultSet rs = stmt.executeQuery();
        System.out.println("User query executed");
        
        while (rs.next()) {
            User user = new User(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password")
            );
            
            user.setCreatedAt(rs.getTimestamp("created_at"));
            user.setUpdatedAt(rs.getTimestamp("updated_at"));
            
            results.add(user);
            System.out.println("Found user: " + user.getUsername() + " (ID: " + user.getId() + ")");
        }
    } catch (SQLException e) {
        System.out.println("⚠️ Search users failed: " + e.getMessage());
        e.printStackTrace();
    }
    
    System.out.println("Total users found: " + results.size());
    return results;
}

// Add this debugging method to your UserController
public void debugDatabaseStructure() {
    try {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        
        System.out.println("Available tables in database:");
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            System.out.println(" - " + tableName);
            
            // List columns in this table
            ResultSet columns = metaData.getColumns(null, null, tableName, null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                System.out.println("   * " + columnName + " (" + dataType + ")");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
// Add this method to UserController or IssueController
public void debugTableData(String tableName) {
    try {
        String sql = "SELECT * FROM " + tableName + " LIMIT 5";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        System.out.println("Sample data from " + tableName + ":");
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        // Print column names
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + " | ");
        }
        System.out.println();
        
        // Print data rows
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rs.getString(i) + " | ");
            }
            System.out.println();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



public void debugGetUserById(int userId) {
    System.out.println("Looking up user with ID: " + userId);
    User user = getUserById(userId);
    if (user != null) {
        System.out.println("Found user: " + user.getUsername() + " (ID: " + user.getId() + ")");
    } else {
        System.out.println("No user found with ID: " + userId);
        
        // Check if user exists in database
        try {
            String sql = "SELECT id, username FROM users WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Database has user: " + rs.getString("username") + " (ID: " + rs.getInt("id") + ")");
            } else {
                System.out.println("Database has no user with ID: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// 🔍 Search for issues by title or description
// Check if your search SQL is correct and the pattern is being set properly
public ArrayList<model.Issue> searchIssues(String searchQuery) {
    System.out.println("Searching for issues with query: " + searchQuery);
    
   
    
    ArrayList<model.Issue> results = new ArrayList<>();
    String sql = "SELECT i.*, COUNT(l.id) as like_count FROM issues i " +
             "LEFT JOIN likes l ON i.id = l.issue_id AND l.reaction_type = 'like' " +
             "WHERE i.title ILIKE ? OR i.description ILIKE ? OR i.id::text = ? " +
             "GROUP BY i.id";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        String pattern = "%" + searchQuery + "%";
        stmt.setString(1, pattern);
        stmt.setString(2, pattern);
        stmt.setString(3, searchQuery); // Exact match for ID
        
        ResultSet rs = stmt.executeQuery();
        System.out.println("Query executed");
        
        while (rs.next()) {
            model.Issue issue = new model.Issue(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("status"),
                rs.getTimestamp("created_at")
            );
            
            // Explicitly set likes from the count
            issue.setLikes(rs.getInt("like_count"));
            
            // Debug
            System.out.println("Found issue: " + issue.getTitle() + " with " + issue.getLikes() + " likes");
            
            results.add(issue);
        }
    } catch (SQLException e) {
        System.out.println("⚠️ Search issues failed: " + e.getMessage());
        e.printStackTrace();
    }
    
    System.out.println("Total issues found: " + results.size());
    return results;
}


// Add this method to your UserController class
public void debugSampleData() {
    try {
        // Check users table
        PreparedStatement userStmt = connection.prepareStatement("SELECT COUNT(*) FROM users");
        ResultSet userRs = userStmt.executeQuery();
        userRs.next();
        int userCount = userRs.getInt(1);
        System.out.println("Total users in database: " + userCount);
        
        // Check issues table
        PreparedStatement issueStmt = connection.prepareStatement("SELECT COUNT(*) FROM issues");
        ResultSet issueRs = issueStmt.executeQuery();
        issueRs.next();
        int issueCount = issueRs.getInt(1);
        System.out.println("Total issues in database: " + issueCount);
        
        // Show sample data if available
        if (userCount > 0) {
            PreparedStatement sampleUserStmt = connection.prepareStatement("SELECT id, username, email FROM users LIMIT 3");
            ResultSet sampleUsers = sampleUserStmt.executeQuery();
            System.out.println("Sample users:");
            while (sampleUsers.next()) {
                System.out.println("  - ID: " + sampleUsers.getInt("id") + 
                                   ", Username: " + sampleUsers.getString("username") + 
                                   ", Email: " + sampleUsers.getString("email"));
            }
        }
        
        if (issueCount > 0) {
            PreparedStatement sampleIssueStmt = connection.prepareStatement("SELECT id, title, user_id FROM issues LIMIT 3");
            ResultSet sampleIssues = sampleIssueStmt.executeQuery();
            System.out.println("Sample issues:");
            while (sampleIssues.next()) {
                System.out.println("  - ID: " + sampleIssues.getInt("id") + 
                                   ", Title: " + sampleIssues.getString("title") + 
                                   ", User ID: " + sampleIssues.getInt("user_id"));
            }
        }
    } catch (SQLException e) {
        System.out.println("Error running debug sample data: " + e.getMessage());
        e.printStackTrace();
    }
}

// Add this method to your UserController class


// Add this to test with multiple search terms
public void testMultipleSearchTerms() {
    System.out.println("\n===== TESTING MULTIPLE SEARCH TERMS =====");
    String[] testTerms = {"4", "a", "e", "test", "issue", "user"};
    
    for (String term : testTerms) {
        System.out.println("\nTesting search with term: \"" + term + "\"");
        
        // Test user search
        ArrayList<User> users = searchUsers(term);
        System.out.println("  Found " + users.size() + " users with term \"" + term + "\"");
        
        // Test issue search
        ArrayList<model.Issue> issues = searchIssues(term);
        System.out.println("  Found " + issues.size() + " issues with term \"" + term + "\"");
    }
}
// Add this to your main method or somewhere it will be executed
public void testSearch() {
    System.out.println("\n----- TESTING SEARCH FUNCTIONALITY -----");
    
    // Test with known values that should be in your database
    String testQuery = "4"; // The query you're having trouble with
    
    System.out.println("Direct SQL test for users with query: " + testQuery);
    try {
        String pattern = "%" + testQuery + "%";
        String sql = "SELECT id, username, email FROM users WHERE username ILIKE ? OR email ILIKE ? OR full_name ILIKE ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, pattern);
        stmt.setString(2, pattern);
        stmt.setString(3, pattern);
        
        ResultSet rs = stmt.executeQuery();
        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.println("  Found user: ID=" + rs.getInt("id") + 
                              ", Username=" + rs.getString("username") + 
                              ", Email=" + rs.getString("email"));
        }
        if (!found) {
            System.out.println("  No users found with query: " + testQuery);
        }
    } catch (SQLException e) {
        System.out.println("Error in direct SQL test: " + e.getMessage());
        e.printStackTrace();
    }
    
    // Similar test for issues
    System.out.println("Direct SQL test for issues with query: " + testQuery);
    try {
        String pattern = "%" + testQuery + "%";
        String sql = "SELECT id, title FROM issues WHERE title ILIKE ? OR description ILIKE ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, pattern);
        stmt.setString(2, pattern);
        
        ResultSet rs = stmt.executeQuery();
        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.println("  Found issue: ID=" + rs.getInt("id") + 
                              ", Title=" + rs.getString("title"));
        }
        if (!found) {
            System.out.println("  No issues found with query: " + testQuery);
        }
    } catch (SQLException e) {
        System.out.println("Error in direct SQL test: " + e.getMessage());
        e.printStackTrace();
    }
}
}