package View;

import model.User;
import model.Report;
import model.Issue;
import model.AdminStats;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import Controller.UserController;
import Controller.AdminController;
import Controller.IssueController;

import java.util.List;

public class AdminDashboardView {
    private JFrame frame;
    private User adminUser;
    private JTabbedPane tabbedPane;
    private JTable usersTable;
    private JTable issuesTable;
    private JTable reportsTable;
    private AdminController adminController;
    private IssueController issueController; // Add IssueController
    private UserController userController;

    public AdminDashboardView(User adminUser) {
        this.adminUser = adminUser;
        
        // Validate that the user is an admin
        if (!adminUser.isAdmin()) {
            JOptionPane.showMessageDialog(null, "Access denied. Admin privileges required.");
            new LoginView();
            return;
        }
        
  
        
     // Initialize controllers
        adminController = new AdminController();
        issueController = new IssueController();
        userController = new UserController();
        
        initialize();
    }
    
    private void initialize() {
        frame = new JFrame("Admin Dashboard - Support Desk");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create tabbed pane for main content
        tabbedPane = new JTabbedPane();
        
        // Add tabs for different admin functions
        tabbedPane.addTab("Users Management", createUsersPanel());
        tabbedPane.addTab("Issues Management", createIssuesPanel());
        tabbedPane.addTab("Reports Management", createReportsPanel());
        tabbedPane.addTab("System Notices", createNoticesPanel());
        tabbedPane.addTab("Statistics", createStatsPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Add status bar at bottom
        JLabel statusBar = new JLabel(" Ready | Logged in as: " + adminUser.getUsername() + " (Admin)");
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        Color aucaBlue = new Color(0, 51, 153);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(aucaBlue);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JLabel adminLabel = new JLabel("Admin: " + adminUser.getUsername());
        adminLabel.setForeground(Color.WHITE);
        rightPanel.add(adminLabel);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginView();
            }
        });
        rightPanel.add(logoutButton);
        
        headerPanel.add(rightPanel, BorderLayout.EAST);
        return headerPanel;
    }
    
    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton refreshBtn = new JButton("Refresh");
        JButton addUserBtn = new JButton("Add User");
        JButton blockUserBtn = new JButton("Block/Unblock");
        JButton makeAdminBtn = new JButton("Toggle Admin");
        JButton deleteUserBtn = new JButton("Delete User");
        
        toolBar.add(refreshBtn);
        toolBar.add(addUserBtn);
        toolBar.add(blockUserBtn);
        toolBar.add(makeAdminBtn);
        toolBar.add(deleteUserBtn);
        
        panel.add(toolBar, BorderLayout.NORTH);
        
        // Create table for users
        String[] columns = {"ID", "Username", "Full Name", "Email", "Admin", "Blocked", "Created At"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        usersTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(usersTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchBtn);
        
        panel.add(searchPanel, BorderLayout.SOUTH);
        
        // Add button action listeners
        refreshBtn.addActionListener(e -> loadUsers());
        
        blockUserBtn.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (int) usersTable.getValueAt(selectedRow, 0);
                boolean isBlocked = (boolean) usersTable.getValueAt(selectedRow, 5);
                
                boolean success;
                if (isBlocked) {
                    success = adminController.unblockUser(userId);
                } else {
                    success = adminController.blockUser(userId);
                }
                
                if (success) {
                    JOptionPane.showMessageDialog(frame, "User status updated successfully!");
                    loadUsers(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update user status.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a user first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Load users initially
        loadUsers();
        
        return panel;
    }


    
    private void loadUsers() {
        DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
        model.setRowCount(0); // Clear existing data
        
        try {
            // Fetch all users from AdminController
            List<User> users = adminController.getAllUsers();
            
            // Populate the table with real user data
            for (User user : users) {
                model.addRow(new Object[]{
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.isAdmin(),
                    user.isBlocked(),
                    user.getCreatedAt()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading users: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createIssuesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton refreshBtn = new JButton("Refresh");
        JButton viewIssueBtn = new JButton("View Details");
        JButton deleteIssueBtn = new JButton("Delete Issue");
        
        toolBar.add(refreshBtn);
        toolBar.add(viewIssueBtn);
        toolBar.add(deleteIssueBtn);
        
        panel.add(toolBar, BorderLayout.NORTH);
        
        // Create table for issues
        String[] columns = {"ID", "Title", "Status", "Reported By", "Category", "Created At", "Updated At"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        issuesTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(issuesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add search and filter panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);
        
        searchPanel.add(new JLabel("Filter by Status: "));
        String[] statuses = {"All", "Open", "In Progress", "Resolved", "Closed"};
        JComboBox<String> statusFilter = new JComboBox<>(statuses);
        searchPanel.add(statusFilter);
        
        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchBtn);
        
        panel.add(searchPanel, BorderLayout.SOUTH);
        
        // Add button action listeners
        refreshBtn.addActionListener(e -> loadIssues());
        
        deleteIssueBtn.addActionListener(e -> {
            int selectedRow = issuesTable.getSelectedRow();
            if (selectedRow != -1) {
                int issueId = (int) issuesTable.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(
                    frame, 
                    "Are you sure you want to delete this issue? This action cannot be undone.",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = adminController.deleteIssue(issueId);
                    if (success) {
                        JOptionPane.showMessageDialog(frame, "Issue deleted successfully!");
                        loadIssues(); // Refresh the table
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to delete issue.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an issue first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        viewIssueBtn.addActionListener(e -> {
            int selectedRow = issuesTable.getSelectedRow();
            if (selectedRow != -1) {
                int issueId = (int) issuesTable.getValueAt(selectedRow, 0);
                String title = (String) issuesTable.getValueAt(selectedRow, 1);
                showIssueDetails(issueId, title);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an issue first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Load issues initially
        loadIssues();
        
        return panel;
    }
    
    private void loadIssues() {
        DefaultTableModel model = (DefaultTableModel) issuesTable.getModel();
        model.setRowCount(0); // Clear existing data
        
        try {
            // Fetch all issues from IssueController
            List<Issue> issues = issueController.getAllIssues();
            
            // Populate the table with real issue data
            for (Issue issue : issues) {
                // Fetch the username of the user who reported the issue
                User user = userController.getUserById(issue.getUserId());
                String reportedBy = (user != null) ? user.getUsername() : "Unknown";
                
                model.addRow(new Object[]{
                    issue.getId(),
                    issue.getTitle(),
                    issue.getStatus(),
                    reportedBy,
                    "General", // Category is not in your Issue model; using placeholder
                    issue.getCreatedAt(),
                    issue.getUpdatedAt()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading issues: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showIssueDetails(int issueId, String title) {
        JDialog dialog = new JDialog(frame, "Issue Details: " + title, true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(frame);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // TODO: Replace with actual issue details from database
        JTextArea issueContent = new JTextArea(
            "Issue ID: " + issueId + "\n" +
            "Title: " + title + "\n" +
            "Description: This is a sample issue description.\n\n" +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed commodo " +
            "tincidunt nisl, a aliquam massa vehicula in. Suspendisse potenti."
        );
        issueContent.setEditable(false);
        issueContent.setLineWrap(true);
        issueContent.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(issueContent);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.getContentPane().add(mainPanel);
        dialog.setVisible(true);
    }
    
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton refreshBtn = new JButton("Refresh");
        JButton viewReportBtn = new JButton("View Details");
        JButton resolveBtn = new JButton("Mark as Resolved");
        JButton dismissBtn = new JButton("Dismiss Report");
        JButton deleteContentBtn = new JButton("Delete Reported Content");
        
        toolBar.add(refreshBtn);
        toolBar.add(viewReportBtn);
        toolBar.add(resolveBtn);
        toolBar.add(dismissBtn);
        toolBar.add(deleteContentBtn);
        
        panel.add(toolBar, BorderLayout.NORTH);
        
        // Create table for reports
        String[] columns = {"ID", "Type", "Reported By", "Reason", "Status", "Created At"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class; // All columns contain String values
            }
        };
        
        reportsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(reportsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add search and filter panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);
        
        searchPanel.add(new JLabel("Filter by Status: "));
        String[] statuses = {"All", "Pending", "Resolved", "Dismissed"};
        JComboBox<String> statusFilter = new JComboBox<>(statuses);
        searchPanel.add(statusFilter);
        
        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchBtn);
        
        panel.add(searchPanel, BorderLayout.SOUTH);
        
        // Add button action listeners
        refreshBtn.addActionListener(e -> loadReports());
        
        viewReportBtn.addActionListener(e -> {
            int selectedRow = reportsTable.getSelectedRow();
            if (selectedRow != -1) {
                int reportId = Integer.parseInt(reportsTable.getValueAt(selectedRow, 0).toString());
                String type = reportsTable.getValueAt(selectedRow, 1).toString();
                showReportDetails(reportId, type);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a report first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        resolveBtn.addActionListener(e -> {
            int selectedRow = reportsTable.getSelectedRow();
            if (selectedRow != -1) {
                int reportId = Integer.parseInt(reportsTable.getValueAt(selectedRow, 0).toString());
                
                // Update the report status to "resolved" in the database
                // TODO: Implement this with AdminController
                JOptionPane.showMessageDialog(frame, "Report marked as resolved!");
                loadReports(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a report first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        dismissBtn.addActionListener(e -> {
            int selectedRow = reportsTable.getSelectedRow();
            if (selectedRow != -1) {
                int reportId = Integer.parseInt(reportsTable.getValueAt(selectedRow, 0).toString());
                
                // Update the report status to "dismissed" in the database
                // TODO: Implement this with AdminController
                JOptionPane.showMessageDialog(frame, "Report dismissed!");
                loadReports(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a report first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteContentBtn.addActionListener(e -> {
            int selectedRow = reportsTable.getSelectedRow();
            if (selectedRow != -1) {
                int reportId = Integer.parseInt(reportsTable.getValueAt(selectedRow, 0).toString());
                String type = reportsTable.getValueAt(selectedRow, 1).toString();
                
                int confirm = JOptionPane.showConfirmDialog(
                    frame, 
                    "Are you sure you want to delete the reported " + type.toLowerCase() + "? This action cannot be undone.",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Delete the reported content from the database
                    // TODO: Implement this with AdminController
                    
                    JOptionPane.showMessageDialog(frame, "Reported content deleted successfully!");
                    loadReports(); // Refresh the table
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a report first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Load reports initially
        loadReports();
        
        return panel;
    }
    
    private void loadReports() {
        DefaultTableModel model = (DefaultTableModel) reportsTable.getModel();
        model.setRowCount(0); // Clear existing data
        
        try {
            // Fetch all reports from AdminController
            List<Report> reports = adminController.getReports();
            
            // Populate the table with real report data
            for (Report report : reports) {
                // Determine report type
                String type = (report.getIssueId() != null) ? "Issue" : "Comment";
                
                // Fetch the username of the user who reported
                User user = userController.getUserById(report.getReportedBy());
                String reportedBy = (user != null) ? user.getUsername() : "Unknown";
                
                // Note: Your Report model doesn't have a status field; using "Pending" as default
                model.addRow(new Object[]{
                    report.getId(),
                    type,
                    reportedBy,
                    report.getReason(),
                    "Pending", // Placeholder since status isn't in Report model
                    report.getCreatedAt()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading reports: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showReportDetails(int reportId, String type) {
        JDialog dialog = new JDialog(frame, "Report Details: #" + reportId, true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(frame);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // TODO: Replace with actual report details from database
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add report details
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("Report ID:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel("#" + reportId), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        detailsPanel.add(new JLabel("Report Type:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel(type), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        detailsPanel.add(new JLabel("Reported By:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel("User #123 (username)"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        detailsPanel.add(new JLabel("Created At:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel("2023-01-05 14:30:00"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        detailsPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel("Pending"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        detailsPanel.add(new JLabel("Reason:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel("Inappropriate content"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        detailsPanel.add(new JLabel("Reported Content:"), gbc);
        
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JTextArea contentArea = new JTextArea("This is the reported content text that violated our guidelines...");
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setPreferredSize(new Dimension(600, 200));
        detailsPanel.add(contentScroll, gbc);
        
        mainPanel.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton resolveButton = new JButton("Resolve");
        JButton dismissButton = new JButton("Dismiss");
        JButton deleteButton = new JButton("Delete Content");
        JButton closeButton = new JButton("Close");
        
        resolveButton.addActionListener(e -> {
            // TODO: Implement resolve functionality
            dialog.dispose();
        });
        
        dismissButton.addActionListener(e -> {
            // TODO: Implement dismiss functionality
            dialog.dispose();
        });
        
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                dialog, 
                "Are you sure you want to delete the reported content? This action cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                // TODO: Implement delete functionality
                dialog.dispose();
            }
        });
        
        closeButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(resolveButton);
        buttonPanel.add(dismissButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.getContentPane().add(mainPanel);
        dialog.setVisible(true);
    }
    
    private JPanel createNoticesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel controlPanel = new JPanel(new BorderLayout());
        
        // Create toolbar for actions
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton refreshBtn = new JButton("Refresh");
        JButton newNoticeBtn = new JButton("New Notice");
        JButton editNoticeBtn = new JButton("Edit Notice");
        JButton deleteNoticeBtn = new JButton("Delete Notice");
        
        toolBar.add(refreshBtn);
        toolBar.add(newNoticeBtn);
        toolBar.add(editNoticeBtn);
        toolBar.add(deleteNoticeBtn);
        
        controlPanel.add(toolBar, BorderLayout.NORTH);
        
        // Create table for notices
        String[] columns = {"ID", "Title", "Posted By", "Created At"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        JTable noticesTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(noticesTable);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add preview panel at the bottom
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Notice Preview"));
        
        JTextArea previewArea = new JTextArea(5, 20);
        previewArea.setEditable(false);
        previewArea.setLineWrap(true);
        previewArea.setWrapStyleWord(true);
        previewPanel.add(new JScrollPane(previewArea), BorderLayout.CENTER);
        
        panel.add(previewPanel, BorderLayout.SOUTH);
        
        // Add sample data
        model.addRow(new Object[]{1, "System Maintenance", "admin", "2023-01-05"});
        model.addRow(new Object[]{2, "New Features Announcement", "admin", "2023-01-10"});
        
        // Add action listeners
        noticesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = noticesTable.getSelectedRow();
                if (selectedRow != -1) {
                    // TODO: Replace with actual notice content from database
                    String title = (String) noticesTable.getValueAt(selectedRow, 1);
                    previewArea.setText("Title: " + title + "\n\nThis is a sample notice content. The actual content would be loaded from the database.");
                }
            }
        });
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create refresh button at top
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshBtn = new JButton("Refresh Stats");
        topPanel.add(refreshBtn);
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Create stats display in center
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add stats cards
        statsPanel.add(createStatCard("Total Users", "125", new Color(41, 128, 185)));
        statsPanel.add(createStatCard("Total Issues", "347", new Color(39, 174, 96)));
        statsPanel.add(createStatCard("Open Issues", "42", new Color(211, 84, 0)));
        statsPanel.add(createStatCard("Total Comments", "1,283", new Color(142, 68, 173)));
        statsPanel.add(createStatCard("Active Reports", "8", new Color(231, 76, 60)));
        statsPanel.add(createStatCard("System Notices", "3", new Color(52, 152, 219)));
        
        // Create scrollable panel for stats
        JScrollPane scrollPane = new JScrollPane(statsPanel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add action listeners
        refreshBtn.addActionListener(e -> loadStats());
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(color);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    

    private void loadStats() {
        try {
            // Get the statistics from the AdminController
            model.AdminStats stats = adminController.getSystemStats();
            
            // Remove the existing stats panel and create a new one
            Component[] components = tabbedPane.getComponents();
            for (Component component : components) {
                if (component instanceof JPanel && tabbedPane.getTitleAt(tabbedPane.indexOfComponent(component)).equals("Statistics")) {
                    tabbedPane.remove(component);
                    break;
                }
            }
            
            // Create new stats panel
            JPanel statsPanel = new JPanel(new GridLayout(3, 2, 20, 20));
            statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Add stats cards with real data
            statsPanel.add(createStatCard("Total Users", String.valueOf(stats.getTotalUsers()), new Color(41, 128, 185)));
            statsPanel.add(createStatCard("Total Issues", String.valueOf(stats.getTotalIssues()), new Color(39, 174, 96)));
            statsPanel.add(createStatCard("Total Comments", String.valueOf(stats.getTotalComments()), new Color(142, 68, 173)));
            statsPanel.add(createStatCard("Active Reports", String.valueOf(stats.getActiveReports()), new Color(231, 76, 60)));
            statsPanel.add(createStatCard("Recent Activity", String.valueOf(stats.getRecentActivity()), new Color(52, 152, 219)));
            statsPanel.add(createStatCard("Open Issues", "N/A", new Color(211, 84, 0))); // Not available in AdminStats; placeholder
            
            // Create scrollable panel for stats
            JScrollPane scrollPane = new JScrollPane(statsPanel);
            scrollPane.setBorder(null);
            
            // Add the new stats panel to the tabbed pane
            tabbedPane.addTab("Statistics", scrollPane);
            
            JOptionPane.showMessageDialog(frame, "Statistics refreshed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error refreshing statistics: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Update the status of a report
     * @param reportId The report ID
     * @param status The new status (resolved, dismissed)
     */
    private void updateReportStatus(int reportId, String status) {
        try {
            // TODO: Implement status update in AdminController
            // boolean success = adminController.updateReportStatus(reportId, status);
            
            boolean success = true; // Temporary mock for demonstration
            
            if (success) {
                JOptionPane.showMessageDialog(frame, "Report status updated successfully!");
                loadReports(); // Refresh the reports table
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update report status.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating report status: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Show a dialog to create a new system notice
     */
    private void showCreateNoticeDialog() {
        JDialog dialog = new JDialog(frame, "Create New Notice", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(frame);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField titleField = new JTextField(20);
        formPanel.add(titleField, gbc);
        
        // Content field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Content:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JTextArea contentArea = new JTextArea(10, 20);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        formPanel.add(scrollPane, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Post Notice");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            
            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Title and content are required!", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                boolean success = adminController.postNotice(title, content, adminUser.getId());
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Notice posted successfully!");
                    dialog.dispose();
                    // Refresh notices list
                    // loadNotices();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to post notice.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error posting notice: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.getContentPane().add(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Show dialog to add a new user
     */
    private void showAddUserDialog() {
        JDialog dialog = new JDialog(frame, "Add New User", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(frame);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);
        
        // Full Name field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField fullNameField = new JTextField(15);
        formPanel.add(fullNameField, gbc);
        
        // Email field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField emailField = new JTextField(15);
        formPanel.add(emailField, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);
        
        // Admin checkbox
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JCheckBox adminCheckbox = new JCheckBox("Admin privileges");
        formPanel.add(adminCheckbox, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Add User");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            boolean isAdmin = adminCheckbox.isSelected();
            
            if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required!", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Create a new User object
                User newUser = new User(fullName, username, email, password);
                newUser.setAdmin(isAdmin);
                
                // Save the user (this would be handled by UserController in a real implementation)
                // boolean success = userController.addUser(newUser);
                boolean success = true; // Mock for demonstration
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "User added successfully!");
                    dialog.dispose();
                    loadUsers(); // Refresh users table
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add user.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error adding user: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.getContentPane().add(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Main method for testing the AdminDashboardView
     */
    public static void main(String[] args) {
        // Create a test admin user
        User admin = new User(1, "Admin User", "admin", "admin@example.com", "password", true, false);
        
        // Launch the admin dashboard
        SwingUtilities.invokeLater(() -> new AdminDashboardView(admin));
    }
}