package View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EditProfilePageView {

    private JFrame editFrame;

    public EditProfilePageView(String username, String email, ImageIcon profilePic) {

        // ==== Frame Setup ====
        editFrame = new JFrame("Edit Profile");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setSize(450, 500);
        editFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ==== Panel ====
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

        // ==== Profile Picture ====
        JLabel profileLabel = new JLabel();
        profileLabel.setIcon(resizeAndRoundIcon(profilePic, 100, 100));
        profileLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        editPanel.add(profileLabel, gbc);

        // ==== Username ====
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        editPanel.add(new JLabel("Username:"), gbc);

        JTextField usernameField = new JTextField(username, 20);
        gbc.gridx = 1;
        editPanel.add(usernameField, gbc);

        // ==== Email ====
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

            // 🎯 PLACE TO ADD: Save to database, file, or controller
            System.out.println("Saving new profile info:");
            System.out.println("Username: " + newUsername);
            System.out.println("Email: " + newEmail);
            System.out.println("Password: " + (newPassword.isEmpty() ? "[unchanged]" : "[updated]"));

            // 💬 Show confirmation
            JOptionPane.showMessageDialog(editFrame, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Optionally close or refresh the view
            editFrame.dispose();
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
}
