package View;

import javax.swing.*;
import java.awt.*;

public class ProfileView {

    private JFrame profileFrame;

    public ProfileView(String username, String email, ImageIcon profilePic,
                       int issuesSubmitted, int likesReceived,
                       int commentsReceived, int commentsMade) {

        // ==== Frame Setup ====
        profileFrame = new JFrame("Profile - " + username);
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(600, 600);
        profileFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ==== Profile Panel ====
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                "User Profile",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.DARK_GRAY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // ==== Profile Picture ====
        gbc.gridy = 0;
        JLabel profileLabel = new JLabel();
        Image image = profilePic.getImage();
        Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        profileLabel.setIcon(new ImageIcon(scaledImage));
        profilePanel.add(profileLabel, gbc);

        // ==== Username ====
        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username: " + username);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        profilePanel.add(usernameLabel, gbc);

        // ==== Email ====
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email: " + email);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        profilePanel.add(emailLabel, gbc);

        // ==== Stats Panel ====
        gbc.gridy++;
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Activity Summary",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(0, 102, 204)
        ));

        statsPanel.add(new JLabel("Issues Submitted: " + issuesSubmitted));
        statsPanel.add(new JLabel("Likes Received: " + likesReceived));
        statsPanel.add(new JLabel("Comments Received: " + commentsReceived));
        statsPanel.add(new JLabel("Comments Made: " + commentsMade));

        profilePanel.add(statsPanel, gbc);

        // ==== Edit Profile Button ====
        gbc.gridy++;
        JButton editProfileButton = new JButton("Edit Profile");
        editProfileButton.setFont(new Font("Arial", Font.PLAIN, 14));
        editProfileButton.setBackground(new Color(0, 102, 204));
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.setPreferredSize(new Dimension(150, 40));
        profilePanel.add(editProfileButton, gbc);

        // ==== Add Profile Panel to Main Panel ====
        mainPanel.add(profilePanel, BorderLayout.CENTER);

        profileFrame.setContentPane(mainPanel);
        profileFrame.setVisible(true);

        // ==== Edit Button Action ====
        editProfileButton.addActionListener(e -> {
            new EditProfilePageView(username, email, profilePic);
        });
    }
}
