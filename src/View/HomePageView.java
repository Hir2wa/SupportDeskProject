package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePageView {

    private JFrame homeFrame;
    private JPanel postsPanel;

    public HomePageView(String username, ImageIcon profilePic) {
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
        centerPanel.add(postButton);
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
                    // Create a new post panel
                    JPanel newPostPanel = createPostPanel(postText, username, "Just Now");
                    postsPanel.add(newPostPanel);
                    postsPanel.revalidate();
                    postsPanel.repaint();

                    // Clear the text area after posting
                    newPostTextArea.setText("");
                }
            }
        });
    }

    private void saveToDB(String post, int likes, int dislikes) {
        System.out.println("Saving post stats to DB → " + post);
        System.out.println("Likes: " + likes + " | Dislikes: " + dislikes);
    }

    private void saveCommentToDB(String post, String comment) {
        System.out.println("Saving comment to DB for post: " + post);
        System.out.println("Comment: " + comment);
    }

    // Helper method to create individual post panels
    // Helper method to create individual post panels
    private JPanel createPostPanel(String postText, String username, String timeAgo) {
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBackground(Color.WHITE);
        postPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        postPanel.setPreferredSize(new Dimension(700, 180));
    
        // ==== Post Content ====
        JLabel postContent = new JLabel("<html>" + postText + "</html>");
        postContent.setFont(new Font("Arial", Font.PLAIN, 14));
    
        // ==== Info ====
        JLabel postInfo = new JLabel("Posted by: " + username + " • " + timeAgo);
        postInfo.setFont(new Font("Arial", Font.ITALIC, 12));
    
        // ==== Like/Dislike Buttons and Logic ====
        JButton likeButton = new JButton("Like 👍");
        JButton dislikeButton = new JButton("Dislike 👎");
    
        JLabel likeLabel = new JLabel("0 Likes");
        JLabel dislikeLabel = new JLabel("0 Dislikes");
    
        final boolean[] liked = {false};
        final boolean[] disliked = {false};
        final int[] likeCount = {0};
        final int[] dislikeCount = {0};
    
        likeButton.addActionListener(e -> {
            if (!liked[0]) {
                likeCount[0]++;
                if (disliked[0]) {
                    dislikeCount[0]--;
                    disliked[0] = false;
                }
                liked[0] = true;
            } else {
                likeCount[0]--;
                liked[0] = false;
            }
            likeLabel.setText(likeCount[0] + " Likes");
            dislikeLabel.setText(dislikeCount[0] + " Dislikes");
            saveToDB(postText, likeCount[0], dislikeCount[0]); // mock save
        });
    
        dislikeButton.addActionListener(e -> {
            if (!disliked[0]) {
                dislikeCount[0]++;
                if (liked[0]) {
                    likeCount[0]--;
                    liked[0] = false;
                }
                disliked[0] = true;
            } else {
                dislikeCount[0]--;
                disliked[0] = false;
            }
            likeLabel.setText(likeCount[0] + " Likes");
            dislikeLabel.setText(dislikeCount[0] + " Dislikes");
            saveToDB(postText, likeCount[0], dislikeCount[0]); // mock save
        });
    
        JPanel interactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interactionPanel.setOpaque(false);
        interactionPanel.add(likeButton);
        interactionPanel.add(likeLabel);
        interactionPanel.add(dislikeButton);
        interactionPanel.add(dislikeLabel);
    
        // ==== Comment Section ====
        JLabel commentsTitle = new JLabel("Comments:");
        JTextArea commentsArea = new JTextArea(3, 60);
        commentsArea.setEditable(false);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        JScrollPane commentScroll = new JScrollPane(commentsArea);
    
        JTextField commentInput = new JTextField(50);
        JButton submitComment = new JButton("Post Comment");
    
        submitComment.addActionListener(e -> {
            String comment = commentInput.getText().trim();
            if (!comment.isEmpty()) {
                commentsArea.append(username + ": " + comment + "\n");
                commentInput.setText("");
                saveCommentToDB(postText, comment); // mock save
            }
        });
    
        JPanel commentInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        commentInputPanel.add(commentInput);
        commentInputPanel.add(submitComment);
    
        // ==== Add to Post Panel ====
        postPanel.add(postContent);
        postPanel.add(postInfo);
        postPanel.add(interactionPanel);
        postPanel.add(commentsTitle);
        postPanel.add(commentScroll);
        postPanel.add(commentInputPanel);
    
        return postPanel;
    }
    

    public static void main(String[] args) {
        // Test the HomePageView with dummy data
        new HomePageView("John Doe", new ImageIcon("Assets/LogoSupportDesk.png"));
    }
}
