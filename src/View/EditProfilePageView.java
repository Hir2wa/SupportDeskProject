package View;

import Controller.UserController;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class EditProfilePageView {

    private JFrame editFrame;
    private UserController userController;
    private User currentUser;

    public EditProfilePageView(User user, UserController controller) {
        this.currentUser = user;
        this.userController = controller;
        String username = user.getUsername();
        String email = user.getEmail();
    
        BufferedImage fallbackImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = fallbackImg.createGraphics();
        g2d.setColor(new Color(0, 102, 204));
        g2d.fillRect(0, 0, 100, 100);
        g2d.dispose();
        ImageIcon defaultPic = new ImageIcon(fallbackImg);
        
        
        try {
            File imageFile = new File("Assets/LogoSupportDesk.png");
            if (imageFile.exists()) {
                defaultPic = new ImageIcon(imageFile.getAbsolutePath());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        ImageIcon finalProfilePic = defaultPic;
        editFrame = new JFrame("Edit Profile");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setSize(450, 500);
        editFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBackground(Color.WHITE);
        editPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Edit Your Information",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.GRAY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel profileLabel = new JLabel();
        profileLabel.setIcon(resizeAndRoundIcon(finalProfilePic, 100, 100));
        profileLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        editPanel.add(profileLabel, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        editPanel.add(new JLabel("Username:"), gbc);

        JTextField usernameField = new JTextField(username, 20);
        gbc.gridx = 1;
        editPanel.add(usernameField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        editPanel.add(new JLabel("Email:"), gbc);

        JTextField emailField = new JTextField(email, 20);
        gbc.gridx = 1;
        editPanel.add(emailField, gbc);

        // ==== Password ====
        gbc.gridy++;
        gbc.gridx = 0;
        editPanel.add(new JLabel("Password (optional):"), gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        editPanel.add(passwordField, gbc);

        // ==== Save Button ====
        JButton saveButton = new JButton("💾 Save Changes");
        saveButton.setBackground(new Color(0, 102, 204));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        editPanel.add(saveButton, gbc);

        // ==== Finalize ====
        mainPanel.add(editPanel, BorderLayout.CENTER);
        editFrame.setContentPane(mainPanel);
        editFrame.setVisible(true);

        // ==== Save Logic ====
        saveButton.addActionListener(e -> {
            String newUsername = usernameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();

            // Validate inputs before saving
            if (newUsername.isEmpty() || newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, 
                    "Username and email cannot be empty!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Use the controller to update the user
            boolean updated = userController.updateUser(
                currentUser.getId(), 
                newUsername, 
                newEmail, 
                newPassword);
                
            if (updated) {
                // Update the user object with new values
                currentUser.setUsername(newUsername);
                currentUser.setEmail(newEmail);
                if (!newPassword.isEmpty()) {
                    currentUser.setPassword(newPassword);
                }
                
                // Show confirmation
                JOptionPane.showMessageDialog(editFrame, 
                    "Profile updated successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Close the window
                editFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(editFrame, 
                    "Failed to update profile. Please try again.", 
                    "Update Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

   

    // === Resize and round the icon ===
    private ImageIcon resizeAndRoundIcon(ImageIcon originalIcon, int width, int height) {
        Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage roundedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = roundedImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRoundRect(0, 0, width, height, width, height);
        g2d.setComposite(AlphaComposite.SrcIn);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(roundedImage);
    }
    
    // Create a default profile icon if none is available
    private ImageIcon createDefaultProfileIcon(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fill background with a color
        g2d.setColor(new Color(70, 130, 180)); // Steel blue
        g2d.fillOval(0, 0, width, height);
        
        // Add initials or icon
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, width/2));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "U"; // For User
        int textX = (width - fm.stringWidth(text)) / 2;
        int textY = ((height - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, textX, textY);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
}