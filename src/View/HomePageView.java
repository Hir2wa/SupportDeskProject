package View;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import Controller.ReportController;
import Controller.IssueController;
import Controller.UserController;
import model.Issue;
import model.Like;
import model.Comment;
import model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

public class HomePageView {

    private JFrame homeFrame;
    private JPanel postsPanel;
    private String username;
    private IssueController issueController;
    private UserController userController;
    private int userId;
    private Color primaryColor = new Color(0, 102, 204);
    private Color accentColor = new Color(51, 153, 255);
    private Color lightGray = new Color(245, 245, 245);
    private ReportController reportController;

    public HomePageView(String username, ImageIcon profilePic) {
        this.username = username;
        this.issueController = new IssueController();
        this.userController = new UserController();
        this.userId = getUserIdFromUsername(username);
        this.reportController = new ReportController();
        // ==== Frame setup ====
        homeFrame = new JFrame("Support Desk - Home");
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setSize(900, 800);
        homeFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE);

        // ==== Top Section ====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(primaryColor);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);

        JLabel profileLabel = new JLabel();
        if (profilePic != null) {
            // Scale profile picture if too large
            if (profilePic.getIconWidth() > 40 || profilePic.getIconHeight() > 40) {
                Image img = profilePic.getImage();
                Image newImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                profileLabel.setIcon(new ImageIcon(newImg));
            } else {
                profileLabel.setIcon(profilePic);
            }
        }
        profileLabel.setPreferredSize(new Dimension(40, 40));

        JButton profileButton = createStyledButton("View Profile", primaryColor);
        profileButton.setFont(new Font("Arial", Font.PLAIN, 12));
        profileButton.setForeground(Color.WHITE);

        JTextField searchField = new JTextField("Search issues or users...");
        styleTextField(searchField);
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search issues or users...")) {
                    searchField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search issues or users...");
                }
            }
        });

        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftTop.setOpaque(false);
        leftTop.add(profileLabel);
        leftTop.add(welcomeLabel);
        leftTop.add(profileButton);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightTop.setOpaque(false);
        rightTop.add(searchField);

        topPanel.add(leftTop, BorderLayout.WEST);
        topPanel.add(rightTop, BorderLayout.EAST);

        // ==== Center Section - Issue Post Button ====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel for new post input with title
        JPanel newPostPanel = new JPanel();
        newPostPanel.setLayout(new BoxLayout(newPostPanel, BoxLayout.Y_AXIS));
        newPostPanel.setBorder(new CompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), 
                "Create New Issue"
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        newPostPanel.setBackground(Color.WHITE);
        newPostPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Text area for new post input
        JTextArea newPostTextArea = new JTextArea(4, 50);
        newPostTextArea.setWrapStyleWord(true);
        newPostTextArea.setLineWrap(true);
        newPostTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        newPostTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JScrollPane postScrollPane = new JScrollPane(newPostTextArea);
        postScrollPane.setBorder(BorderFactory.createEmptyBorder());
        postScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Post Button
        JButton postButton = createStyledButton("Post New Issue", accentColor);
        postButton.setFont(new Font("Arial", Font.BOLD, 14));
        postButton.setForeground(Color.WHITE);
        postButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        newPostPanel.add(postScrollPane);
        newPostPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        newPostPanel.add(postButton);

        // Panel for posts with scroll
        JPanel postsContainerPanel = new JPanel();
        postsContainerPanel.setLayout(new BorderLayout());
        postsContainerPanel.setBackground(Color.WHITE);
        postsContainerPanel.setBorder(new CompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Trending Issues"
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Create posts panel with vertical BoxLayout
        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);
        
        // Add postsPanel to a JScrollPane for scrolling
        JScrollPane postsScrollPane = new JScrollPane(postsPanel);
        postsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        postsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        postsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        postsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        postsContainerPanel.add(postsScrollPane, BorderLayout.CENTER);

        // Add components to center panel
        centerPanel.add(newPostPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(postsContainerPanel);

        // ==== Final Setup ====
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        homeFrame.setContentPane(mainPanel);
        homeFrame.setVisible(true);

        // Add action listener to post button
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String postText = newPostTextArea.getText().trim();
                if (!postText.isEmpty()) {
                    // Create Issue object
                    Issue newIssue = new Issue();
                    newIssue.setTitle("New Issue by " + username); // Default title
                    newIssue.setDescription(postText);
                    newIssue.setUserId(userId);
                    
                    // Save to database using controller
                    boolean posted = issueController.postIssue(newIssue, userId);
                    
                    if (posted) {
                        // If successfully posted, load all issues again
                        loadIssues();
                        
                        // Clear the text area after posting
                        newPostTextArea.setText("");
                        JOptionPane.showMessageDialog(homeFrame, "Issue posted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(homeFrame, 
                            "Failed to post issue. Please check database connection.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // Load existing issues on startup
        loadIssues();
    }
    
    // Helper method to create styled buttons without borders
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    // Helper method to style text fields
    private void styleTextField(JTextField textField) {
        textField.setPreferredSize(new Dimension(250, 30));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(new Color(100, 100, 100));
    }
    
    // Updated method to get user ID from username using the database
    private int getUserIdFromUsername(String username) {
        User user = userController.getUserByUsername(username);
        if (user != null) {
            return user.getId();
        }
        System.out.println("Warning: User not found for username: " + username);
        return -1;
    }
    
    // Method to load existing issues from database
    private void loadIssues() {
        // Clear existing posts
        postsPanel.removeAll();
        
        // Get issues from controller
        List<Issue> issues = issueController.getAllIssues();
        
        if (issues.isEmpty()) {
            JLabel noIssuesLabel = new JLabel("No issues found. Be the first to post one!");
            noIssuesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noIssuesLabel.setForeground(new Color(100, 100, 100));
            noIssuesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsPanel.add(Box.createVerticalGlue());
            postsPanel.add(noIssuesLabel);
            postsPanel.add(Box.createVerticalGlue());
        } else {
            // For each issue, create a post panel
            for (Issue issue : issues) {
                // Get the actual username for each post based on user_id
                String posterUsername;
                if (issue.getUserId() == userId) {
                    posterUsername = username; // Current user's post
                } else {
                    // Fetch the username from the database based on the user_id
                    User posterUser = userController.getUserById(issue.getUserId());
                    posterUsername = posterUser != null ? posterUser.getUsername() : "Unknown User #" + issue.getUserId();
                }
                
                JPanel postPanel = createPostPanel(issue.getDescription(), posterUsername, "Earlier", issue.getId());
                postsPanel.add(postPanel);
                postsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        
        postsPanel.revalidate();
        postsPanel.repaint();
    }

    // Helper method to create individual post panels with improved UI
    private JPanel createPostPanel(String postText, String username, String timeAgo, int issueId) {
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBackground(Color.WHITE);
        postPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        postPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        postPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ==== Post Content ====
        JTextArea postContent = new JTextArea(postText);
        postContent.setFont(new Font("Arial", Font.PLAIN, 14));
        postContent.setLineWrap(true);
        postContent.setWrapStyleWord(true);
        postContent.setEditable(false);
        postContent.setBackground(Color.WHITE);
        postContent.setBorder(BorderFactory.createEmptyBorder());

        // ==== Info ====
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        infoPanel.setBackground(lightGray);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel userIconLabel = new JLabel("👤");
        userIconLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel postInfo = new JLabel(" " + username + " • " + timeAgo + " (Issue #" + issueId + ")");
        postInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        
        infoPanel.add(userIconLabel);
        infoPanel.add(postInfo);
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // ==== Like/Dislike Buttons and Logic ====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton likeButton = createStyledButton("Like 👍", primaryColor);
        JButton dislikeButton = createStyledButton("Dislike 👎", new Color(150, 150, 150));

        // Get initial like count from database
        int initialLikeCount = issueController.getLikeCount(issueId);
        JLabel likeLabel = new JLabel(initialLikeCount + " Likes");
        likeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel dislikeLabel = new JLabel("0 Dislikes");
        dislikeLabel.setFont(new Font("Arial", Font.BOLD, 12));

        // Check if current user has already liked this issue
        boolean hasLiked = issueController.hasUserLikedIssue(userId, issueId);
        final boolean[] liked = {hasLiked};
        final boolean[] disliked = {false};
        final int[] likeCount = {initialLikeCount};
        final int[] dislikeCount = {0};
        
        // Update button appearance based on like status
        if (liked[0]) {
            likeButton.setText("Unlike 👍");
        } else {
            likeButton.setBackground(new Color(240, 240, 240));
            likeButton.setForeground(new Color(60, 60, 60));
        }

        dislikeButton.setBackground(new Color(240, 240, 240));
        dislikeButton.setForeground(new Color(60, 60, 60));

        likeButton.addActionListener(e -> {
            try {
                if (!liked[0]) {
                    // Try to like the issue
                    Like like = new Like(issueId, this.username);
                    boolean success = issueController.likeIssue(like, userId);
                    
                    if (success) {
                        // Like was successful
                        likeCount[0]++;
                        liked[0] = true;
                        likeButton.setText("Unlike 👍");
                        likeButton.setBackground(primaryColor);
                        likeButton.setForeground(Color.WHITE);
                        
                        // Handle dislike if necessary
                        if (disliked[0]) {
                            dislikeCount[0]--;
                            disliked[0] = false;
                            dislikeButton.setText("Dislike 👎");
                            dislikeButton.setBackground(new Color(240, 240, 240));
                            dislikeButton.setForeground(new Color(60, 60, 60));
                        }
                    } else {
                        // Already liked (shouldn't happen with our initial check, but just in case)
                        JOptionPane.showMessageDialog(homeFrame, 
                            "You've already liked this issue.", 
                            "Notice", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    // Unlike the issue
                    boolean success = issueController.unlikeIssue(userId, issueId);
                    
                    if (success) {
                        likeCount[0]--;
                        liked[0] = false;
                        likeButton.setText("Like 👍");
                        likeButton.setBackground(new Color(240, 240, 240));
                        likeButton.setForeground(new Color(60, 60, 60));
                    } else {
                        JOptionPane.showMessageDialog(homeFrame, 
                            "Failed to unlike the issue. Please try again.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                // Update like count label
                likeLabel.setText(likeCount[0] + " Likes");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(homeFrame, 
                    "Error: " + ex.getMessage(), 
                    "Like Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dislikeButton.addActionListener(e -> {
            // Placeholder for dislike functionality
            JOptionPane.showMessageDialog(homeFrame, 
                "Dislike functionality not implemented yet.", 
                "Notice", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(likeButton);
        buttonPanel.add(likeLabel);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(dislikeButton);
        buttonPanel.add(dislikeLabel);

        // ==== Comment Section ====
        JPanel commentSectionPanel = new JPanel();
        commentSectionPanel.setLayout(new BoxLayout(commentSectionPanel, BoxLayout.Y_AXIS));
        commentSectionPanel.setBackground(Color.WHITE);
        commentSectionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 0, 0, 0)
        ));
        commentSectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel commentsTitle = new JLabel("Comments:");
        commentsTitle.setFont(new Font("Arial", Font.BOLD, 14));
        commentsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Load existing comments for this issue
        List<Comment> comments = issueController.getCommentsForIssue(issueId);
        
        JTextArea commentsArea = new JTextArea(3, 60);
        commentsArea.setEditable(false);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        commentsArea.setFont(new Font("Arial", Font.PLAIN, 13));
        commentsArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        commentsArea.setBackground(lightGray);
        
        // Add existing comments to the text area
        if (comments != null && !comments.isEmpty()) {
            for (Comment comment : comments) {
                // Get username for this comment
                User commentUser = userController.getUserById(comment.getUserId());
                String commentUsername = commentUser != null ? commentUser.getUsername() : "Unknown User";
                
                commentsArea.append(commentUsername + ": " + comment.getContent() + "\n\n");
            }
        }
        
        JScrollPane commentScroll = new JScrollPane(commentsArea);
        commentScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        commentScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Comment input section
        JPanel commentInputPanel = new JPanel();
        commentInputPanel.setLayout(new BoxLayout(commentInputPanel, BoxLayout.X_AXIS));
        commentInputPanel.setBackground(Color.WHITE);
        commentInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JTextField commentInput = new JTextField();
        styleTextField(commentInput);
        commentInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        JButton submitComment = createStyledButton("Post", accentColor);
        submitComment.setFont(new Font("Arial", Font.BOLD, 12));

        submitComment.addActionListener(e -> {
            String commentText = commentInput.getText().trim();
            if (!commentText.isEmpty()) {
                // Create Comment object
                Comment comment = new Comment();
                comment.setIssueId(issueId);
                comment.setContent(commentText);
                
                // Save to database with the actual userId
                boolean commentAdded = issueController.addComment(comment, userId);
                
                if (commentAdded) {
                    commentsArea.append(this.username + ": " + commentText + "\n\n");
                    commentInput.setText("");
                } else {
                    JOptionPane.showMessageDialog(homeFrame, 
                        "Failed to post comment. Please try again.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        commentInputPanel.add(commentInput);
        commentInputPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        commentInputPanel.add(submitComment);

        commentSectionPanel.add(commentsTitle);
        commentSectionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        commentSectionPanel.add(commentScroll);
        commentSectionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        commentSectionPanel.add(commentInputPanel);

        // ==== Add Components to Post Panel ====
        postPanel.add(infoPanel);
        postPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        postPanel.add(postContent);
        postPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        postPanel.add(buttonPanel);
        postPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        postPanel.add(commentSectionPanel);

        return postPanel;
    }
}