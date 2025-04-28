package View;

import Controller.UserController;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView {

    private JFrame loginFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginView() {
        Color aucaBlue = new Color(0, 51, 153);
        loginFrame = new JFrame("Login - Support Desk");
        loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel loginLabel = new JLabel("<html><center><h2>Welcome Back!</h2><p>Login to your account.</p></center></html>");
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel welcomePanel = new JPanel();
        welcomePanel.add(loginLabel);
        welcomePanel.setOpaque(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        usernameField = new JTextField(15);
        addFormRow(formPanel, gbc, 0, "Username:", usernameField);

        passwordField = new JPasswordField(15);
        addFormRow(formPanel, gbc, 1, "Password:", passwordField);

        loginButton = new JButton("Login");
        loginButton.setBackground(aucaBlue);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        registerButton = new JButton("Register");
        registerButton.setForeground(aucaBlue);
        registerButton.setBorder(BorderFactory.createLineBorder(aucaBlue));
        registerButton.setBackground(Color.WHITE);

        gbc.gridy = 3;
        formPanel.add(registerButton, gbc);

        mainPanel.add(welcomePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(formPanel);

        loginFrame.getContentPane().add(mainPanel);
        loginFrame.setVisible(true);

        // ✅ Login Button Action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                UserController userController = new UserController();
                User loggedInUser = userController.loginAndGetUser(username, password);

                if (loggedInUser != null) {
                    loginFrame.dispose(); // Close current window
                    
                    if (loggedInUser.isAdmin()) {
                        // Open Admin Dashboard if user is an admin
                        new AdminDashboardView(loggedInUser);
                    } else {
                        // Open regular user home page
                        new HomePageView(username, null);
                    }
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials. Please try again.");
                }
            }
        });

        // ✅ Register Button Action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginFrame.dispose(); // Close Login window
                new RegisterView(); // Open Register View
            }
        });
    }

    // ✅ Helper Method to add form rows
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