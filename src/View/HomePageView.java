package View;

import javax.swing.*;

import Controller.IssueController;
import Controller.UserController;
import model.Issue;
import model.Like;
import model.Comment;
import model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HomePageView {

    private JFrame homeFrame;
    private JPanel postsPanel;
    private String username;
    private IssueController issueController;
    private UserController userController;
    private int userId;

    public HomePageView(String username, ImageIcon profilePic) {
        this.username = username;
        this.issueController = new IssueController();
        this.userController = new UserController();
        this.userId = getUserIdFromUsername(username);
        
        // ==== Frame setup ====
        homeFrame = new JFrame("Support Desk - Home");
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setSize(800, 700);
        homeFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ==== Top Section ====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        topPanel.setBackground(new Color(0, 102, 204));

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);

        JLabel profileLabel = new JLabel();
        profileLabel.setIcon(profilePic);
        profileLabel.setPreferredSize(new Dimension(50, 50));

        JButton profileButton = new JButton("View Profile");
        profileButton.setFont(new Font("Arial", Font.PLAIN, 12));
        profileButton.setForeground(Color.WHITE);
        profileButton.setBackground(new Color(0, 102, 204));
        profileButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JTextField searchField = new JTextField("Search issues or users...");
        searchField.setPreferredSize(new Dimension(250, 30));

        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Text area for new post input
        JTextArea newPostTextArea = new JTextArea(4, 50);
        newPostTextArea.setWrapStyleWord(true);
        newPostTextArea.setLineWrap(true);
        newPostTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(newPostTextArea);

        // Post Button
        JButton postButton = new JButton("Post New Issue");
        postButton.setFont(new Font("Arial", Font.BOLD, 14));
        postButton.setBackground(new Color(0, 102, 204));
        postButton.setForeground(Color.WHITE);
        postButton.setPreferredSize(new Dimension(200, 50));
        postButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel for posts (initially empty)
        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);
        postsPanel.setBorder(BorderFactory.createTitledBorder("Trending Issues"));

        // Add components to center panel
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(postButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(postsPanel);

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
            noIssuesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsPanel.add(noIssuesLabel);
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
            }
        }
        
        postsPanel.revalidate();
        postsPanel.repaint();
    }

    // Helper method to create individual post panels
    // Replace the createPostPanel method in HomePageView with this improved version
private JPanel createPostPanel(String postText, String username, String timeAgo, int issueId) {
    JPanel postPanel = new JPanel();
    postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
    postPanel.setBackground(Color.WHITE);
    postPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    postPanel.setMaximumSize(new Dimension(700, 300));
    postPanel.setPreferredSize(new Dimension(700, 180));

    // ==== Post Content ====
    JLabel postContent = new JLabel("<html><body width='650'>" + postText + "</body></html>");
    postContent.setFont(new Font("Arial", Font.PLAIN, 14));
    postContent.setAlignmentX(Component.LEFT_ALIGNMENT);

    // ==== Info ====
    JLabel postInfo = new JLabel("Posted by: " + username + " • " + timeAgo + " (Issue #" + issueId + ")");
    postInfo.setFont(new Font("Arial", Font.ITALIC, 12));
    postInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Add padding
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
    contentPanel.setBackground(Color.WHITE);
    contentPanel.add(postContent);
    contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    contentPanel.add(postInfo);

    // ==== Like/Dislike Buttons and Logic ====
    JButton likeButton = new JButton("Like 👍");
    JButton dislikeButton = new JButton("Dislike 👎");

    // Get initial like count from database
    int initialLikeCount = issueController.getLikeCount(issueId);
    JLabel likeLabel = new JLabel(initialLikeCount + " Likes");
    JLabel dislikeLabel = new JLabel("0 Dislikes"); // Assuming dislikes aren't implemented yet

    // Check if current user has already liked this issue
    boolean hasLiked = issueController.hasUserLikedIssue(userId, issueId);
    final boolean[] liked = {hasLiked};
    final boolean[] disliked = {false};
    final int[] likeCount = {initialLikeCount};
    final int[] dislikeCount = {0};
    
    // Update button appearance based on like status
    if (liked[0]) {
        likeButton.setText("Unlike 👍");
        likeButton.setBackground(new Color(0, 102, 204));
        likeButton.setForeground(Color.WHITE);
    }

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
                    likeButton.setBackground(new Color(0, 102, 204));
                    likeButton.setForeground(Color.WHITE);
                    
                    // Handle dislike if necessary
                    if (disliked[0]) {
                        dislikeCount[0]--;
                        disliked[0] = false;
                        dislikeButton.setText("Dislike 👎");
                        dislikeButton.setBackground(null);
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
                    likeButton.setBackground(null);
                    likeButton.setForeground(null);
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

    JPanel interactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    interactionPanel.setOpaque(false);
    interactionPanel.add(likeButton);
    interactionPanel.add(likeLabel);
    interactionPanel.add(dislikeButton);
    interactionPanel.add(dislikeLabel);
    interactionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    // ==== Comment Section ====
    JLabel commentsTitle = new JLabel("Comments:");
    commentsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Load existing comments for this issue
    List<Comment> comments = issueController.getCommentsForIssue(issueId);
    
    JTextArea commentsArea = new JTextArea(3, 60);
    commentsArea.setEditable(false);
    commentsArea.setLineWrap(true);
    commentsArea.setWrapStyleWord(true);
    
    // Add existing comments to the text area
    if (comments != null && !comments.isEmpty()) {
        for (Comment comment : comments) {
            // Get username for this comment
            User commentUser = userController.getUserById(comment.getUserId());
            String commentUsername = commentUser != null ? commentUser.getUsername() : "Unknown User";
            
            commentsArea.append(commentUsername + ": " + comment.getContent() + "\n");
        }
    }
    
    JScrollPane commentScroll = new JScrollPane(commentsArea);
    commentScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

    JTextField commentInput = new JTextField(50);
    JButton submitComment = new JButton("Post Comment");

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
                commentsArea.append(this.username + ": " + commentText + "\n");
                commentInput.setText("");
            } else {
                JOptionPane.showMessageDialog(homeFrame, 
                    "Failed to post comment. Please try again.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    JPanel commentInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    commentInputPanel.add(commentInput);
    commentInputPanel.add(submitComment);
    commentInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    // ==== Add to Post Panel ====
    postPanel.add(contentPanel);
    postPanel.add(interactionPanel);
    postPanel.add(commentsTitle);
    postPanel.add(commentScroll);
    postPanel.add(commentInputPanel);

    return postPanel;
}
}