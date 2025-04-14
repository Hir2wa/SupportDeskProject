package View;

import Controller.AdminController;
import model.AdminStats;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardSwing extends JFrame {

    public AdminDashboardSwing() {
        setTitle("Admin Dashboard | Support Desk");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        AdminController controller = new AdminController();
      //  AdminStats stats = controller.getSystemStats()

        JPanel sidebar = createSidebar();
       // JPanel dashboardPanel = createDashboardPanel(stats);

        // JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, dashboardPanel);
        // splitPane.setDividerLocation(200);
        // add(splitPane);

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(52, 58, 64));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        JLabel title = new JLabel("Admin Panel");
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(20));

        String[] tabs = {"Dashboard", "Users", "Reports", "Notices", "Back to Home"};
        for (String tab : tabs) {
            JButton btn = new JButton(tab);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(10));
        }

        return sidebar;
    }

    private JPanel createDashboardPanel(AdminStats stats) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(createStatCard("Total Users", stats.getTotalUsers(), new Color(78, 115, 223)));
        panel.add(createStatCard("Total Issues", stats.getTotalIssues(), new Color(28, 200, 138)));
        panel.add(createStatCard("Total Comments", stats.getTotalComments(), new Color(54, 185, 204)));
        panel.add(createStatCard("Active Reports", stats.getActiveReports(), new Color(246, 194, 62)));

        return panel;
    }

    private JPanel createStatCard(String label, int value, Color borderColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(borderColor, 5));
        card.setBackground(Color.WHITE);

        JLabel title = new JLabel(label);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel number = new JLabel(String.valueOf(value));
        number.setFont(new Font("SansSerif", Font.BOLD, 24));
        number.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(title, BorderLayout.NORTH);
        card.add(number, BorderLayout.CENTER);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboardSwing());
    }
}
