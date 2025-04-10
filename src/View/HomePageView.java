package View;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import Controller.IssueController;
import Controller.ReportController;
import Controller.UserController;
import model.Issue;
import model.Like;
import model.Comment;
import model.Report;
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
    private ReportController reportController;
    private JTextField commentInput;
    private Integer issueId;
    private JPanel commentSectionPanel;
    private int userId;
    private Color primaryColor = new Color(0, 102, 204);
    private Color accentColor = new Color(51, 153, 255);
    private Color lightGray = new Color(245, 245, 245);

    public HomePageView(String username, ImageIcon profilePic) {
        this.username = username;
        this.issueController = new IssueController();
        this.userController = new UserController();
        this.reportController = new ReportController();
        this.userId = getUserIdFromUsername(username);
        
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

    
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    
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

   
        JButton postButton = createStyledButton("Post New Issue", accentColor);
        postButton.setFont(new Font("Arial", Font.BOLD, 14));
        postButton.setForeground(Color.WHITE);
        postButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        newPostPanel.add(postScrollPane);
        newPostPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        newPostPanel.add(postButton);

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

    
        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);
        
  
        JScrollPane postsScrollPane = new JScrollPane(postsPanel);
        postsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        postsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        postsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        postsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        postsContainerPanel.add(postsScrollPane, BorderLayout.CENTER);

     
        centerPanel.add(newPostPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(postsContainerPanel);


        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        homeFrame.setContentPane(mainPanel);
        homeFrame.setVisible(true);

        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String postText = newPostTextArea.getText().trim();
                if (!postText.isEmpty()) {
                 
                    Issue newIssue = new Issue();
                    newIssue.setTitle("New Issue by " + username); 
                    newIssue.setDescription(postText);
                    newIssue.setUserId(userId);
                    
         
                    boolean posted = issueController.postIssue(newIssue, userId);
                    
                    if (posted) {
                
                        loadIssues();
                        
                        
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
        
  
        loadIssues();
    }
    

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
    

    private void styleTextField(JTextField textField) {
        textField.setPreferredSize(new Dimension(250, 30));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(new Color(100, 100, 100));
    }
    

    private int getUserIdFromUsername(String username) {
        User user = userController.getUserByUsername(username);
        if (user != null) {
            return user.getId();
        }
        System.out.println("Warning: User not found for username: " + username);
        return -1;
    }
    

    private void loadIssues() {
    
        postsPanel.removeAll();
        
 
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
         
            for (Issue issue : issues) {
         
                String posterUsername;
                if (issue.getUserId() == userId) {
                    posterUsername = username;
                } else {
               
                    User posterUser = userController.getUserById(issue.getUserId());
                    posterUsername = posterUser != null ? posterUser.getUsername() : "Unknown User #" + issue.getUserId();
                }
                
         
                String timeDisplay = formatTimestamp(issue.getCreatedAt());
                
                JPanel postPanel = createPostPanel(issue.getDescription(), posterUsername, timeDisplay, issue.getId());
                postsPanel.add(postPanel);
                postsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        
        postsPanel.revalidate();
        postsPanel.repaint();
    }
    
    /**
     * Format timestamp into a user-friendly string
     * @param timestamp The timestamp to format
     * @return A formatted string representation of the timestamp
     */
    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return "Unknown time";
        }
        
      
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - timestamp.getTime();
        
   
        if (timeDiff < 60000) { 
            return "Just now";
        } else if (timeDiff < 3600000) { 
            long minutes = timeDiff / 60000;
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else if (timeDiff < 86400000) { 
            long hours = timeDiff / 3600000;
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (timeDiff < 604800000) { 
            long days = timeDiff / 86400000;
            return days + (days == 1 ? " day ago" : " days ago");
        } else {
          
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            return sdf.format(timestamp);
        }
    }

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

  
        JTextArea postContent = new JTextArea(postText);
        postContent.setFont(new Font("Arial", Font.PLAIN, 14));
        postContent.setLineWrap(true);
        postContent.setWrapStyleWord(true);
        postContent.setEditable(false);
        postContent.setBackground(Color.WHITE);
        postContent.setBorder(BorderFactory.createEmptyBorder());

      
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
        
   
JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
buttonPanel.setBackground(Color.WHITE);
buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

JButton likeButton = createStyledButton("Like 👍", primaryColor);
JButton dislikeButton = createStyledButton("Dislike 👎", new Color(150, 150, 150));
JButton reportButton = createStyledButton("Report ⚠️", new Color(220, 53, 69));


int initialLikeCount = issueController.getLikeCount(issueId);
int initialDislikeCount = issueController.getDislikeCount(issueId);


boolean hasLiked = issueController.hasUserLikedIssue(userId, issueId);
boolean hasDisliked = issueController.hasUserDislikedIssue(userId, issueId);


JLabel likeLabel = new JLabel(initialLikeCount + " Likes");
likeLabel.setFont(new Font("Arial", Font.BOLD, 12));

JLabel dislikeLabel = new JLabel(initialDislikeCount + " Dislikes");
dislikeLabel.setFont(new Font("Arial", Font.BOLD, 12));


final boolean[] liked = {hasLiked};
final boolean[] disliked = {hasDisliked};
final int[] likeCount = {initialLikeCount};
final int[] dislikeCount = {initialDislikeCount};


if (liked[0]) {
    likeButton.setText("Unlike 👍");
    likeButton.setBackground(primaryColor);
    likeButton.setForeground(Color.WHITE);
} else {
    likeButton.setBackground(new Color(240, 240, 240));
    likeButton.setForeground(new Color(60, 60, 60));
}

if (disliked[0]) {
    dislikeButton.setText("Undislike 👎");
    dislikeButton.setBackground(primaryColor);
    dislikeButton.setForeground(Color.WHITE);
} else {
    dislikeButton.setBackground(new Color(240, 240, 240));
    dislikeButton.setForeground(new Color(60, 60, 60));
}

reportButton.setBackground(new Color(240, 240, 240));
reportButton.setForeground(new Color(60, 60, 60));
reportButton.setFont(new Font("Arial", Font.PLAIN, 12));


likeButton.addActionListener(e -> {
    try {
        if (!liked[0]) {
           
            Like like = new Like(issueId, this.username);
            boolean success = issueController.likeIssue(like, userId);
            
            if (success) {
            
                likeCount[0]++;
                liked[0] = true;
                likeButton.setText("Unlike 👍");
                likeButton.setBackground(primaryColor);
                likeButton.setForeground(Color.WHITE);
               
                if (disliked[0]) {
                    dislikeCount[0]--;
                    disliked[0] = false;
                    dislikeButton.setText("Dislike 👎");
                    dislikeButton.setBackground(new Color(240, 240, 240));
                    dislikeButton.setForeground(new Color(60, 60, 60));
                    dislikeLabel.setText(dislikeCount[0] + " Dislikes");
                }
            } else {
         
                JOptionPane.showMessageDialog(homeFrame, 
                    "You've already liked this issue.", 
                    "Notice", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
    
            boolean success = issueController.undislikeIssue(userId, issueId);
            
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
        

        likeLabel.setText(likeCount[0] + " Likes");
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(homeFrame, 
            "Error: " + ex.getMessage(), 
            "Like Error", JOptionPane.ERROR_MESSAGE);
    }
});


dislikeButton.addActionListener(e -> {
    try {
        if (!disliked[0]) {
        
            Like like = new Like(issueId, this.username);
            boolean success = issueController.dislikeIssue(like, userId);
            
            if (success) {
             
                dislikeCount[0]++;
                disliked[0] = true;
                dislikeButton.setText("Undislike 👎");
                dislikeButton.setBackground(primaryColor);
                dislikeButton.setForeground(Color.WHITE);
                
                // Handle like if necessary
                if (liked[0]) {
                    likeCount[0]--;
                    liked[0] = false;
                    likeButton.setText("Like 👍");
                    likeButton.setBackground(new Color(240, 240, 240));
                    likeButton.setForeground(new Color(60, 60, 60));
                    likeLabel.setText(likeCount[0] + " Likes");
                }
            } else {
         
                JOptionPane.showMessageDialog(homeFrame, 
                    "You've already disliked this issue.", 
                    "Notice", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
         
            boolean success = issueController.undislikeIssue(userId, issueId);
            
            if (success) {
                dislikeCount[0]--;
                disliked[0] = false;
                dislikeButton.setText("Dislike 👎");
                dislikeButton.setBackground(new Color(240, 240, 240));
                dislikeButton.setForeground(new Color(60, 60, 60));
            } else {
                JOptionPane.showMessageDialog(homeFrame, 
                    "Failed to remove your dislike. Please try again.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        

        dislikeLabel.setText(dislikeCount[0] + " Dislikes");
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(homeFrame, 
            "Error: " + ex.getMessage(), 
            "Dislike Error", JOptionPane.ERROR_MESSAGE);
    }
});


reportButton.addActionListener(e -> {

    if (reportController.hasUserReportedIssue(userId, issueId)) {
        JOptionPane.showMessageDialog(homeFrame, 
            "You have already reported this issue.", 
            "Notice", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    

    showReportDialog(issueId, null);
});


buttonPanel.add(likeButton);
buttonPanel.add(likeLabel);
buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
buttonPanel.add(dislikeButton);
buttonPanel.add(dislikeLabel);
buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
buttonPanel.add(reportButton);


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
        
 
        List<Comment> comments = issueController.getCommentsForIssue(issueId);
    
        JPanel commentsContainer = new JPanel();
        commentsContainer.setLayout(new BoxLayout(commentsContainer, BoxLayout.Y_AXIS));
        commentsContainer.setBackground(Color.WHITE);
        commentsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
    
        if (comments != null && !comments.isEmpty()) {
            for (Comment comment : comments) {
            
                User commentUser = userController.getUserById(comment.getUserId());
                String commentUsername = commentUser != null ? commentUser.getUsername() : "Unknown User";
                
            
                JPanel commentPanel = createCommentPanel(comment, commentUsername);
                commentsContainer.add(commentPanel);
                commentsContainer.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        
        JScrollPane commentScroll = new JScrollPane(commentsContainer);
        commentScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        commentScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

     
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

 
submitComment.addActionListener(e -> extracted(username, issueId, commentSectionPanel, commentInput));

        commentInputPanel.add(commentInput);
        commentInputPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        commentInputPanel.add(submitComment);

        commentSectionPanel.add(commentsTitle);
        commentSectionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        commentSectionPanel.add(commentScroll);
        commentSectionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        commentSectionPanel.add(commentInputPanel);

    
        postPanel.add(infoPanel);
        postPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        postPanel.add(postContent);
        postPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        postPanel.add(buttonPanel);
        postPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        postPanel.add(commentSectionPanel);

        return postPanel;
    }

    private void extracted(String username, int issueId, JPanel commentSectionPanel, JTextField commentInput) {
        String commentText = commentInput.getText().trim();
        if (!commentText.isEmpty()) {
      
            Comment comment = new Comment();
            comment.setIssueId(issueId);
            comment.setContent(commentText);
            
        
            boolean commentAdded = issueController.addComment(comment, userId);
            
            if (commentAdded) {
             
                JPanel commentPanel = createCommentPanel(comment, username);
                
                // Make sure commentSectionPanel exists and has the right components
                if (commentSectionPanel != null && commentSectionPanel.getComponentCount() >= 3) {
                    JScrollPane commentScroll = (JScrollPane) commentSectionPanel.getComponent(2);
                    JPanel commentsContainer = (JPanel) commentScroll.getViewport().getView();
                    commentsContainer.add(commentPanel);
                    commentsContainer.add(Box.createRigidArea(new Dimension(0, 5)));
                    commentsContainer.revalidate();
                    commentsContainer.repaint();
                }
                
                // Clear the input field
                commentInput.setText("");
            } else {
                JOptionPane.showMessageDialog(homeFrame, 
                    "Failed to post comment. Please try again.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // New method to create a panel for a single comment with a report button
    private JPanel createCommentPanel(Comment comment, String username) {
        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BorderLayout());
        commentPanel.setBackground(lightGray);
        commentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(lightGray);
        
        JLabel usernameLabel = new JLabel(username + ": ");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        
        JTextArea commentContent = new JTextArea(comment.getContent());
        commentContent.setEditable(false);
        commentContent.setLineWrap(true);
        commentContent.setWrapStyleWord(true);
        commentContent.setFont(new Font("Arial", Font.PLAIN, 13));
        commentContent.setBackground(lightGray);
        commentContent.setBorder(BorderFactory.createEmptyBorder());
        
        contentPanel.add(usernameLabel, BorderLayout.WEST);
        contentPanel.add(commentContent, BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(lightGray);
        
        JButton reportCommentButton = new JButton("⚠️");
        reportCommentButton.setBorderPainted(false);
        reportCommentButton.setFocusPainted(false);
        reportCommentButton.setContentAreaFilled(false);
        reportCommentButton.setToolTipText("Report Comment");
        reportCommentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        reportCommentButton.addActionListener(e -> {
            // Check if the user has already reported this comment
            if (reportController.hasUserReportedComment(userId, comment.getId())) {
                JOptionPane.showMessageDialog(homeFrame, 
                    "You have already reported this comment.", 
                    "Notice", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Show report dialog
            showReportDialog(null, comment.getId());
        });
        
        actionPanel.add(reportCommentButton);
        
        commentPanel.add(contentPanel, BorderLayout.CENTER);
        commentPanel.add(actionPanel, BorderLayout.EAST);
        
        return commentPanel;
    }
    
    // Method to display the report dialog
    private void showReportDialog(Integer issueId, Integer commentId) {
        JDialog reportDialog = new JDialog(homeFrame, "Report", true);
        reportDialog.setSize(400, 300);
        reportDialog.setLocationRelativeTo(homeFrame);
        
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Report " + (issueId != null ? "Issue" : "Comment"));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel instructionLabel = new JLabel("Please provide a reason for your report:");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] reportReasons = {
            "Select a reason...",
            "Inappropriate content",
            "Spam",
            "Harassment",
            "Misinformation",
            "Other"
        };
        
        JComboBox<String> reasonComboBox = new JComboBox<>(reportReasons);
        reasonComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        reasonComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea detailsArea = new JTextArea(5, 20);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel detailsLabel = new JLabel("Additional details (optional):");
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        detailsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton submitButton = new JButton("Submit Report");
        submitButton.setBackground(primaryColor);
        submitButton.setForeground(Color.WHITE);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);
        
        dialogPanel.add(titleLabel);
        dialogPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        dialogPanel.add(instructionLabel);
        dialogPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        dialogPanel.add(reasonComboBox);
        dialogPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        dialogPanel.add(detailsLabel);
        dialogPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        dialogPanel.add(detailsScroll);
        dialogPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        dialogPanel.add(buttonPanel);
        cancelButton.addActionListener(e -> reportDialog.dispose());
        submitButton.addActionListener(e -> {
            String selectedReason = (String) reasonComboBox.getSelectedItem();
            
            // Check if a valid reason is selected
            if (selectedReason == null || selectedReason.equals("Select a reason...")) {
                JOptionPane.showMessageDialog(reportDialog, 
                    "Please select a reason for your report.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            // Build the full reason
            String fullReason = selectedReason;
            String additionalDetails = detailsArea.getText().trim();
            
            if (!additionalDetails.isEmpty()) {
                fullReason += ": " + additionalDetails;
            }
            
            // Ensure that either issueId or commentId is provided
            if (issueId == null && commentId == null) {
                JOptionPane.showMessageDialog(reportDialog, 
                    "Invalid issue or comment ID.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create a report object
            Report report = new Report(userId, commentId, issueId, fullReason);
            
            try {
                boolean success = false;
        
                // Handle reporting logic based on which ID (issue or comment) is provided
                if (issueId != null) {
                    // Report an issue
                    success = reportController.reportIssue(userId, issueId, fullReason); // Ensure the correct method is called for issue reporting
                } else if (commentId != null) {
                    // Report a comment
                    success = reportController.reportComment(userId, commentId, fullReason); // Corrected to handle comment reporting
                }
        
                // Handle success or failure of the report submission
                if (success) {
                    JOptionPane.showMessageDialog(reportDialog, 
                        "Your report has been submitted successfully. Thank you for helping to keep our community safe.", 
                        "Report Submitted", JOptionPane.INFORMATION_MESSAGE);
                    reportDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(reportDialog, 
                        "Failed to submit your report. Please try again.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace(); // Log for debugging purposes
                JOptionPane.showMessageDialog(reportDialog, 
                    "An unexpected error occurred. Please try again.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        

reportDialog.setContentPane(dialogPanel);
reportDialog.setVisible(true);

       
    }
    
   
   
}
