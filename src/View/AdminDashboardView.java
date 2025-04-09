package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboardView {

    private JFrame adminFrame;
    
    public AdminDashboardView() {
        // ==== Frame Setup ====
        adminFrame = new JFrame("Admin Dashboard");
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setSize(1000, 700);
        adminFrame.setLocationRelativeTo(null); // Center the frame

        // ==== Main Panel (with layout) ====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // ==== Navigation Panel ====
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 5)); // Horizontal buttons for navigation
        JButton usersButton = new JButton("Users");
        JButton violationsButton = new JButton("Violations");
        JButton contentButton = new JButton("Content");
        JButton settingsButton = new JButton("Settings");
        JButton analyticsButton = new JButton("Analytics");

        navPanel.add(usersButton);
        navPanel.add(violationsButton);
        navPanel.add(contentButton);
        navPanel.add(settingsButton);
        navPanel.add(analyticsButton);

        // ==== Dashboard Panel (Display Sections) ====
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BorderLayout());

        // ==== JTable for User List ====
        String[] columnNames = {"Username", "Email", "Status", "Action"};
        Object[][] data = {
            {"JohnDoe", "john@example.com", "Active", "Edit | Delete | Ban"},
            {"JaneDoe", "jane@example.com", "Inactive", "Edit | Delete | Ban"}
        };

        // JTable Setup
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        userTable.setFillsViewportHeight(true);

        // Create buttons in the Action column
        userTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        userTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Add user table to dashboard panel
        dashboardPanel.add(new JLabel("Users Management"), BorderLayout.NORTH);
        dashboardPanel.add(scrollPane, BorderLayout.CENTER);

        // ==== Add components to main panel ====
        mainPanel.add(navPanel, BorderLayout.NORTH);
        mainPanel.add(dashboardPanel, BorderLayout.CENTER);

        adminFrame.setContentPane(mainPanel);
        adminFrame.setVisible(true);

        // Handle Users Button click
        usersButton.addActionListener(e -> {
            // Show user management (currently the JTable)
            JOptionPane.showMessageDialog(adminFrame, "Displaying user management", "Users", JOptionPane.INFORMATION_MESSAGE);
        });

        // Handle Violations Button click
        violationsButton.addActionListener(e -> {
            // Add logic to show violation management panel here
            JOptionPane.showMessageDialog(adminFrame, "Displaying violations", "Violations", JOptionPane.INFORMATION_MESSAGE);
        });

        // Handle Analytics Button click
        analyticsButton.addActionListener(e -> {
            // Show analytics (could be charts or stats)
            JOptionPane.showMessageDialog(adminFrame, "Displaying analytics", "Analytics", JOptionPane.INFORMATION_MESSAGE);
        });
    }

  
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    // Custom ButtonEditor to handle button click actions
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    if (label.equals("Edit")) {
                        JOptionPane.showMessageDialog(adminFrame, "Edit User");
                    } else if (label.equals("Delete")) {
                        JOptionPane.showMessageDialog(adminFrame, "Delete User");
                    } else if (label.equals("Ban")) {
                        JOptionPane.showMessageDialog(adminFrame, "Ban User");
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            return label;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboardView());
    }
}
