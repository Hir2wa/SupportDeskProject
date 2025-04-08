package View;

import Controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterView {

    private JFrame registerFrame;
    private JTextField fullNameField, usernameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton registerButton, backToLoginButton;

    public RegisterView() {
        Color aucaBlue = new Color(0, 51, 153);

        registerFrame = new JFrame("Register - Support Desk");
        registerFrame.setSize(550, 650);
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel welcomeLabel = new JLabel("<html><center><h2>Welcome to Support Desk!</h2><p>Register below to get started.</p></center></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel welcomePanel = new JPanel();
        welcomePanel.add(welcomeLabel);
        welcomePanel.setOpaque(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        fullNameField = new JTextField(15);
        addFormRow(formPanel, gbc, 0, "Full Name:", fullNameField);

        usernameField = new JTextField(15);
        addFormRow(formPanel, gbc, 1, "Username:", usernameField);

        emailField = new JTextField(15);
        addFormRow(formPanel, gbc, 2, "Email:", emailField);

        passwordField = new JPasswordField(15);
        addFormRow(formPanel, gbc, 3, "Password:", passwordField);

        confirmPasswordField = new JPasswordField(15);
        addFormRow(formPanel, gbc, 4, "Confirm Password:", confirmPasswordField);

        registerButton = new JButton("Register");
        registerButton.setBackground(aucaBlue);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(registerButton, gbc);

        backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setForeground(aucaBlue);
        backToLoginButton.setBorder(BorderFactory.createLineBorder(aucaBlue));
        backToLoginButton.setBackground(Color.WHITE);

        gbc.gridy = 6;
        formPanel.add(backToLoginButton, gbc);

        mainPanel.add(welcomePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(formPanel);

        registerFrame.getContentPane().add(mainPanel);
        registerFrame.setVisible(true);

        // 🔐 Register Button Logic
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fullName = fullNameField.getText().trim();
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

                // ✅ Simple Validations
                if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(registerFrame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    JOptionPane.showMessageDialog(registerFrame, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(registerFrame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(registerFrame, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
              UserController userController = new UserController();
                User user = new User(fullName, username, email, password);
               boolean success = userController.registerUser(user);

            


                if (success) {
                    JOptionPane.showMessageDialog(registerFrame, "Account created successfully! 🎉");
                    registerFrame.dispose();
                    new LoginView();
                } else {
                    JOptionPane.showMessageDialog(registerFrame, "Username or Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 🔄 Back to Login Button
        backToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerFrame.dispose();
                new LoginView();
            }
        });
    }

    // 🔧 Helper
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int y, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(field, gbc);
    }
}
