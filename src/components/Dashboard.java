package src.components;

import java.awt.*;

import javax.swing.*;

import src.enums.UserRole;
import src.objects.User;

public class Dashboard extends JPanel {

    public Dashboard(User user) {

        Color lightBlue = new Color(173, 216, 230);
        Color softBlue = new Color(135, 206, 250);

        setLayout(new GridBagLayout());
        setBackground(lightBlue);

        JLabel title = new JLabel("Dashboard", JLabel.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(lightBlue);

        JLabel displayName = new JLabel("Welcome " + user.name, JLabel.CENTER);
        displayName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        displayName.setOpaque(true);
        displayName.setBackground(lightBlue);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(6, 1, 5, 5));
        actionPanel.setPreferredSize(new Dimension(200, 200));
        actionPanel.setBackground(lightBlue);

        JButton mark = new JButton("Mark Attendance");
        mark.setBackground(softBlue);
        mark.setFocusPainted(false);
        mark.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

            if (!new ConfirmationDialog(frame, "Mark Attendance", "Mark todays attendance?").isConfirmed())
                return;
            user.markAttendance();
            JOptionPane.showMessageDialog(null, "Attendance Marked");
        });

        JButton view = new JButton("View Attendance");
        view.setBackground(softBlue);
        view.setFocusPainted(false);
        view.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new RecordModal(frame, user);
        });

        JButton viewUsersAttandance = new JButton("View Users Attendence");
        viewUsersAttandance.setBackground(softBlue);
        viewUsersAttandance.setFocusPainted(false);
        viewUsersAttandance.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new ViewUsersAttendance(user));
            frame.revalidate();
            frame.repaint();
        });

        JButton manage = new JButton("Manage Users");
        manage.setBackground(softBlue);
        manage.setFocusPainted(false);
        manage.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new ManageAccount(user));
            frame.revalidate();
            frame.repaint();
        });

        JButton report = new JButton("Generate Report");
        report.setBackground(softBlue);
        report.setFocusPainted(false);
        report.addActionListener(e -> generateReport(user));

        JButton logout = new JButton("Logout");
        logout.setBackground(softBlue);
        logout.setFocusPainted(false);

        logout.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ConfirmationDialog cd = new ConfirmationDialog(frame, "Logout", "You sure you want to logout?");

            if (!cd.isConfirmed())
                return;
            frame.setContentPane(new LoginPanel());
            frame.revalidate();
            frame.repaint();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy++;
        add(displayName, gbc);

        gbc.gridy++;
        add(actionPanel, gbc);

        actionPanel.add(mark);
        actionPanel.add(view);
        actionPanel.add(report);

        if (user.role == UserRole.ADMIN) {
            actionPanel.add(viewUsersAttandance);
            actionPanel.add(manage);
        }

        actionPanel.add(logout);
    }

    private void generateReport(User user) {
        StringBuilder reportText = new StringBuilder();
        reportText.append("Attendance Report for ").append(user.name).append("\n\n");

        if (user.getRecords().isEmpty()) {
            reportText.append("No attendance records available.");
        } else {
            reportText.append("Total days marked: ").append(user.getRecords().size()).append("\n\n");
            reportText.append("Records:\n");
            user.getRecords().forEach(record -> reportText.append(" - ").append(record).append("\n"));
        }

        JTextArea reportArea = new JTextArea(reportText.toString());
        reportArea.setEditable(false);
        reportArea.setCaretPosition(0);
        reportArea.setBackground(new Color(173, 216, 230));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(360, 260));

        JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                scrollPane,
                "Attendance Report",
                JOptionPane.INFORMATION_MESSAGE);
    }

}
