
package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import Controller.UserController;
import model.User;
import model.Issue;
import java.awt.image.BufferedImage;
import java.io.File;
public class SearchResultsView {
    private JFrame resultsFrame;
    private UserController userController;
    private JTabbedPane tabbedPane;
    private String searchQuery;
    private int currentLoggedInUserId;


    public SearchResultsView(String searchQuery, UserController controller) {
        this.searchQuery = searchQuery;
        this.userController = controller;
        this.searchQuery = searchQuery;
        this.userController = controller;
        this.currentLoggedInUserId = controller.getCurrentLoggedInUserId();
        // Create and set up the frame
        resultsFrame = new JFrame("Search Results for: " + searchQuery);
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setSize(800, 600);
        resultsFrame.setLocationRelativeTo(null);
        
        // Create tabbed pane for users and issues
        tabbedPane = new JTabbedPane();
        
        // Load and display results
        loadResults();
        
        resultsFrame.add(tabbedPane);
        resultsFrame.setVisible(true);
    }
    
    private void loadResults() {
        // Search for users
        ArrayList<User> userResults = userController.searchUsers(searchQuery);
        JPanel usersPanel = createUsersPanel(userResults);
        tabbedPane.addTab("Users (" + userResults.size() + ")", new ImageIcon("Assets/user_icon.png"), usersPanel);
        
        // Search for issues
        ArrayList<model.Issue> issueResults = userController.searchIssues(searchQuery);
        JPanel issuesPanel = createIssuesPanel(issueResults);
        tabbedPane.addTab("Issues (" + issueResults.size() + ")", new ImageIcon("Assets/issue_icon.png"), issuesPanel);
    }
    
    private JPanel createUsersPanel(ArrayList<User> users) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        System.out.println("Creating users panel with " + users.size() + " users");
        // Create header with search info
        JLabel headerLabel = new JLabel("Found " + users.size() + " users matching \"" + searchQuery + "\"");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
         
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (User user : users) {
            String displayText = user.getUsername() + " (" + user.getEmail() + ")";
            System.out.println("Adding to list model: " + displayText);
            listModel.addElement(displayText);
        }

        if (users.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No users found matching your search.");
            noResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noResultsLabel, BorderLayout.CENTER);
            
            return panel;
        }
        




        












   
        
        // Create JList with users
        JList<String> userList = new JList<>(listModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setCellRenderer(new UserListCellRenderer());
        
        // Add mouse listener for clicking on users
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = userList.locationToIndex(e.getPoint());
                    User selectedUser = users.get(index);
                    openUserProfile(selectedUser);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(userList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add instruction label
        JLabel instructionLabel = new JLabel("Double-click a user to view their profile");
        instructionLabel.setForeground(Color.GRAY);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(instructionLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createIssuesPanel(ArrayList<model.Issue> issues) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create header with search info
        JLabel headerLabel = new JLabel("Found " + issues.size() + " issues matching \"" + searchQuery + "\"");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Debug output
        System.out.println("Creating issues panel with " + issues.size() + " issues");
        for (model.Issue issue : issues) {
            System.out.println("Issue in panel: " + issue.getTitle() + " - " + issue.getStatus());
        }
        
        if (issues.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No issues found matching your search.");
            noResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noResultsLabel, BorderLayout.CENTER);
            return panel;
        }
        
        // Create table model with issues data
        String[] columnNames = {"Title", "Status", "Creator", "Likes", "Created At"};
        Object[][] data = new Object[issues.size()][5];
        
        for (int i = 0; i < issues.size(); i++) {
            model.Issue issue = issues.get(i);
            User creator = userController.getUserById(issue.getUserId());
            String creatorName = (creator != null) ? creator.getUsername() : "Unknown";
            
            data[i][0] = issue.getTitle();
            data[i][1] = issue.getStatus();
            data[i][2] = creatorName;
            data[i][3] = issue.getLikes();
            data[i][4] = issue.getCreatedAt();
        }
        
        // Create JTable with issues
        JTable issuesTable = new JTable(data, columnNames);
        issuesTable.setFillsViewportHeight(true);
        
        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(issuesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
// In the SearchResultsView class, modify the openUserProfile method:

private void openUserProfile(User user) {
    // Calculate stats for user
    int issuesSubmitted = userController.countIssuesByUserId(user.getId());
    int likesReceived = userController.countLikesReceivedByUserId(user.getId());
    int commentsReceived = userController.countCommentsReceivedByUserId(user.getId());
    int commentsMade = userController.countCommentsMadeByUserId(user.getId());
    
    // Create a default profile picture 
    BufferedImage fallbackImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = fallbackImg.createGraphics();
    g2d.setColor(new Color(0, 102, 204));
    g2d.fillRect(0, 0, 100, 100);
    g2d.dispose();
    ImageIcon profilePic = new ImageIcon(fallbackImg);
    
    // Get the currently logged in user's ID (you need to pass this to SearchResultsView)
    int currentLoggedInUserId = userController.getCurrentLoggedInUserId();
    
    // Open the profile view in read-only mode (no edit button) if it's not the current user
    boolean isCurrentUser = (currentLoggedInUserId == user.getId());
    
    new ProfileView(user.getUsername(), user.getEmail(), profilePic, 
                   issuesSubmitted, likesReceived, commentsReceived, commentsMade, isCurrentUser);
}
    
    private  void openIssueDetails(model.Issue issue) {
        // Build a more comprehensive dialog with better formatting
        User creator = userController.getUserById(issue.getUserId());
        String creatorName = (creator != null) ? creator.getUsername() : "Unknown";
        
        String message = "<html><body style='width: 400px'>" +
                        "<h2>" + issue.getTitle() + "</h2>" +
                        "<p><b>Status:</b> " + issue.getStatus() + "</p>" +
                        "<p><b>Created by:</b> " + creatorName + "</p>" +
                        "<p><b>Posted on:</b> " + issue.getCreatedAt() + "</p>" +
                        "<p><b>Description:</b><br>" + issue.getDescription() + "</p>" +
                        "</body></html>";
        
        // Try creating a custom dialog instead of JOptionPane
        JDialog detailDialog = new JDialog(resultsFrame, "Issue Details", true);
        detailDialog.setLayout(new BorderLayout());
        
        JEditorPane contentPane = new JEditorPane("text/html", message);
        contentPane.setEditable(false);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(contentPane);
        detailDialog.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> detailDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        detailDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        detailDialog.setSize(500, 400);
        detailDialog.setLocationRelativeTo(resultsFrame);
        detailDialog.setVisible(true);
    }
    // Custom cell renderer for user list
    class UserListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                     int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            // Create user icon
            BufferedImage userIcon = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = userIcon.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(0, 102, 204));
            g2d.fillOval(0, 0, 20, 20);
            g2d.dispose();
            
            label.setIcon(new ImageIcon(userIcon));
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            return label;
        }
    }
    
}