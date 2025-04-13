package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import Controller.IssueController;
import Controller.UserController;
import model.Issue;
import model.Comment;
import model.User;

public class IssueDetailView {
    private JFrame detailFrame;
    private Issue issue;
    private IssueController issueController;
    private UserController userController;
    private int userId;
    private JTextArea commentField;
    private JPanel commentsPanel;

    public IssueDetailView(Issue issue, int currentUserId) {
        this.issue = issue;
        this.userId = currentUserId;
        this.issueController = new IssueController();
        this.userController = new UserController();
        
        // Create and set up the frame
        detailFrame = new JFrame("Issue: " + issue.getTitle());
        detailFrame.setSize(800, 600);
        detailFrame.setLocationRelativeTo(null);
        detailFrame.setLayout(new BorderLayout());
        
        // Issue details panel at the top
        JPanel detailsPanel = createDetailsPanel();
        
        // Comments section in the middle
        JPanel commentsSection = createCommentsSection();
        
        // Comment input at the bottom
        JPanel inputPanel = createCommentInputPanel();
        
        // Add all components to the frame
        detailFrame.add(detailsPanel, BorderLayout.NORTH);
        detailFrame.add(commentsSection, BorderLayout.CENTER);
        detailFrame.add(inputPanel, BorderLayout.SOUTH);
        
        detailFrame.setVisible(true);
    }
    
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Title and status
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(issue.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JLabel statusLabel = new JLabel("Status: " + issue.getStatus());
        statusLabel.setForeground(getStatusColor(issue.getStatus()));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(statusLabel, BorderLayout.EAST);
        titlePanel.setOpaque(false);
        
        // Creator info
        User creator = userController.getUserById(issue.getUserId());
        String creatorName = (creator != null) ? creator.getUsername() : "Unknown";
        
        JLabel creatorLabel = new JLabel("Posted by: " + creatorName + " on " + issue.getCreatedAt());
        creatorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        creatorLabel.setForeground(Color.GRAY);
        
        // Description
        JTextArea descriptionArea = new JTextArea(issue.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Reactions
        JPanel reactionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reactionsPanel.setOpaque(false);
        
        JButton likeButton = new JButton("👍 " + issueController.getLikeCount(issue.getId()));
        likeButton.setFocusPainted(false);
        
        JButton dislikeButton = new JButton("👎 " + issueController.getDislikeCount(issue.getId()));
        dislikeButton.setFocusPainted(false);
        
        reactionsPanel.add(likeButton);
        reactionsPanel.add(dislikeButton);
        
        // Add action listeners for reaction buttons
        likeButton.addActionListener(e -> {
            if (!issueController.hasUserLikedIssue(userId, issue.getId())) {
                // 👇 Get the username from the userController
                String username = userController.getUserById(userId).getUsername();
        
                // 👇 Now use the correct constructor
                issueController.likeIssue(new model.Like(issue.getId(), username), userId);
        
                likeButton.setText("👍 " + issueController.getLikeCount(issue.getId()));
                dislikeButton.setText("👎 " + issueController.getDislikeCount(issue.getId()));
            } else {
                JOptionPane.showMessageDialog(detailFrame, "You've already liked this issue.");
            }
        });
        
        
        dislikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!issueController.hasUserDislikedIssue(userId, issue.getId())) {
                    String username = userController.getUserById(userId).getUsername(); // 👈 fix here
                    model.Like like = new model.Like(issue.getId(), username); // ✅ now it's all good
                    issueController.dislikeIssue(like, userId);
        
                    likeButton.setText("👍 " + issueController.getLikeCount(issue.getId()));
                    dislikeButton.setText("👎 " + issueController.getDislikeCount(issue.getId()));
                } else {
                    JOptionPane.showMessageDialog(detailFrame, "You've already disliked this issue.");
                }
            }
        });
        
        
        
        // Assemble the details panel
        panel.add(titlePanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(creatorLabel, BorderLayout.NORTH);
        centerPanel.add(descriptionArea, BorderLayout.CENTER);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(reactionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCommentsSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel commentsLabel = new JLabel("Comments");
        commentsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
        
        // Load comments
        loadComments();
        
        JScrollPane scrollPane = new JScrollPane(commentsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(commentsLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadComments() {
        commentsPanel.removeAll();
        
        List<Comment> comments = issueController.getCommentsForIssue(issue.getId());
        
        if (comments.isEmpty()) {
            JLabel noCommentsLabel = new JLabel("No comments yet. Be the first to comment!");
            noCommentsLabel.setForeground(Color.GRAY);
            noCommentsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            commentsPanel.add(Box.createVerticalStrut(20));
            commentsPanel.add(noCommentsLabel);
        } else {
            for (Comment comment : comments) {
                commentsPanel.add(createCommentPanel(comment));
            }
        }
        
        commentsPanel.revalidate();
        commentsPanel.repaint();
    }
    
    private JPanel createCommentPanel(Comment comment) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));
        
        User commenter = userController.getUserById(comment.getUserId());
        String commenterName = (commenter != null) ? commenter.getUsername() : "Unknown User";
        
        JLabel userLabel = new JLabel(commenterName);
        userLabel.setFont(new Font("Arial", Font.BOLD, 13));
        
        JLabel dateLabel = new JLabel(comment.getCreatedAt().toString());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        dateLabel.setForeground(Color.GRAY);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(userLabel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        JTextArea contentArea = new JTextArea(comment.getContent());
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);
        contentArea.setOpaque(false);
        contentArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCommentInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel addCommentLabel = new JLabel("Add a comment:");
        addCommentLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        commentField = new JTextArea(3, 20);
        commentField.setLineWrap(true);
        commentField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(commentField);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        JButton postButton = new JButton("Post Comment");
        postButton.setBackground(new Color(0, 102, 204));
        postButton.setForeground(Color.WHITE);
        postButton.setFocusPainted(false);
        
        postButton.addActionListener(e -> postComment());
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(postButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(addCommentLabel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void postComment() {
        String content = commentField.getText().trim();
        if (!content.isEmpty()) {
            Comment comment = new Comment();
            comment.setIssueId(issue.getId());
            comment.setContent(content);
            
            if (issueController.addComment(comment, userId)) {
                commentField.setText("");
                loadComments();
            } else {
                JOptionPane.showMessageDialog(detailFrame, 
                    "Failed to post comment. Please try again.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(detailFrame, 
                "Comment cannot be empty.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Color getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "open":
                return new Color(0, 150, 0);  // Green
            case "in progress":
                return new Color(255, 165, 0);  // Orange
            case "closed":
                return new Color(200, 0, 0);  // Red
            default:
                return Color.BLACK;
        }
    }
}