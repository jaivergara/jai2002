package src.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

import src.objects.User;
import src.services.TestData;
import src.services.UserDomain;

public class ViewUsersAttendance extends JPanel {

    public ViewUsersAttendance(User user) {

        // 🎨 Colors
        Color lightBlue = new Color(173, 216, 230);
        Color softBlue = new Color(135, 206, 250);

        setLayout(new BorderLayout());
        setBackground(lightBlue);

        List<User> users = UserDomain.getUsers();

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);

        JPanel headers = new JPanel(new GridLayout(1, 4));
        headers.setBorder(border);
        headers.setPreferredSize(new Dimension(300, 30));
        headers.setBackground(lightBlue);

        headers.add(new JLabel("Student ID", JLabel.CENTER));
        headers.add(new JLabel("Name", JLabel.CENTER));
        headers.add(new JLabel("Role", JLabel.CENTER));
        headers.add(new JLabel("", JLabel.CENTER));

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(lightBlue);

        users.forEach(u -> {

            JPanel row = new JPanel(new GridLayout(1, 4));
            row.setPreferredSize(new Dimension(300, 30));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            row.setBackground(lightBlue);

            JButton viewButton = new JButton("View Record");
            viewButton.setBackground(softBlue);
            viewButton.setFocusPainted(false);

            viewButton.addActionListener(e -> {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

                if (!new ConfirmationDialog(
                        frame,
                        "View Records",
                        "View " + u.name + "'s record?").isConfirmed())
                    return;

                new RecordModal(frame, u);
            });

            JLabel id = new JLabel(u.studentId, JLabel.CENTER);
            JLabel name = new JLabel(u.name, JLabel.CENTER);
            JLabel role = new JLabel(String.valueOf(u.role), JLabel.CENTER);

            id.setOpaque(true);
            name.setOpaque(true);
            role.setOpaque(true);

            id.setBackground(lightBlue);
            name.setBackground(lightBlue);
            role.setBackground(lightBlue);

            row.add(id);
            row.add(name);
            row.add(role);
            row.add(viewButton);

            body.add(row);
        });

        JScrollPane scrollPane = new JScrollPane(body);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(lightBlue);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(lightBlue);

        JButton backButton = new JButton("Back");
        backButton.setBackground(softBlue);
        backButton.setFocusPainted(false);

        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

            if (!new ConfirmationDialog(
                    frame,
                    "Back",
                    "Go back to dashboard?").isConfirmed())
                return;

            frame.setContentPane(new Dashboard(user));
            frame.revalidate();
            frame.repaint();
        });

        footer.add(backButton);

        add(headers, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }
}