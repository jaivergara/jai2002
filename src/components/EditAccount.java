package src.components;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;

import src.enums.UserRole;
import src.objects.User;
import src.objects.ResponseObject;
import src.services.UserDomain;

public class EditAccount extends JPanel {
    private int currentPage = 1;
    private int prevStartIndex = 0;

    private JPanel container;
    private JPanel header;
    private Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

    public EditAccount(User user) {

        Color lightBlue = new Color(173, 216, 230);
        Color softBlue = new Color(135, 206, 250);

        setLayout(new GridBagLayout());
        setBackground(lightBlue);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel title = new JLabel("Delete Account", JLabel.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        title.setOpaque(true);
        title.setBackground(lightBlue);

        header = new JPanel(new GridLayout(1, 4));
        header.setBorder(border);
        header.setBackground(lightBlue);

        container = new JPanel(new GridLayout(10, 1));
        container.setPreferredSize(new Dimension(200, 300));
        container.setBackground(lightBlue);

        JPanel paginationContainer = new JPanel(new GridBagLayout());
        paginationContainer.setBackground(lightBlue);

        JButton dashboard = new JButton("Dashboard");
        dashboard.setBackground(softBlue);
        dashboard.setFocusPainted(false);
        dashboard.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new Dashboard(user));
            frame.revalidate();
            frame.repaint();
        });

        JButton next = new JButton("Next");
        next.setBackground(softBlue);
        next.setFocusPainted(false);
        next.addActionListener(e -> handleNext());

        JButton prev = new JButton("Prev");
        prev.setBackground(softBlue);
        prev.setFocusPainted(false);
        prev.addActionListener(e -> handlePrev());

        gbc.gridx = 0;
        gbc.gridy = 0;
        paginationContainer.add(prev, gbc);

        gbc.gridx++;
        paginationContainer.add(next, gbc);

        refreshTable(getTenUsers(prevStartIndex));

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy++;
        add(header, gbc);

        gbc.gridy++;
        add(container, gbc);

        gbc.gridy++;
        add(paginationContainer, gbc);

        gbc.gridy++;
        add(dashboard, gbc);
    }

    private void updateHeader() {
        header.removeAll();

        JLabel h1 = new JLabel("Student ID", JLabel.CENTER);
        JLabel h2 = new JLabel("Username", JLabel.CENTER);
        JLabel h3 = new JLabel("Role", JLabel.CENTER);
        JLabel h4 = new JLabel("", JLabel.CENTER);
        JLabel h5 = new JLabel("Page: " + currentPage, JLabel.CENTER);

        h1.setOpaque(true); h2.setOpaque(true); h3.setOpaque(true); h4.setOpaque(true); h5.setOpaque(true);

        Color lightBlue = new Color(173, 216, 230);
        h1.setBackground(lightBlue);
        h2.setBackground(lightBlue);
        h3.setBackground(lightBlue);
        h4.setBackground(lightBlue);
        h5.setBackground(lightBlue);

        header.add(h1);
        header.add(h2);
        header.add(h3);
        header.add(h4);
        header.add(h5);

        header.revalidate();
        header.repaint();
    }

    private JPanel createRow(User user) {
        JPanel wrapper = new JPanel(new GridLayout(1, 5));
        wrapper.setBorder(border);
        wrapper.setBackground(new Color(173, 216, 230));

        JButton edit = new JButton("Edit");
        edit.setBackground(new Color(135, 206, 250));
        edit.setFocusPainted(false);
        edit.addActionListener(e -> {
            openEditDialog(user);
        });

        JButton delete = new JButton("Delete");
        delete.setBackground(new Color(135, 206, 250));
        delete.setFocusPainted(false);
        delete.addActionListener(e -> {
            handleDelete(user);
        });

        JLabel id = new JLabel(user.studentId, JLabel.CENTER);
        JLabel name = new JLabel(user.name, JLabel.CENTER);
        JLabel role = new JLabel(String.valueOf(user.role), JLabel.CENTER);

        id.setOpaque(true);
        name.setOpaque(true);
        role.setOpaque(true);

        Color lightBlue = new Color(173, 216, 230);
        id.setBackground(lightBlue);
        name.setBackground(lightBlue);
        role.setBackground(lightBlue);

        wrapper.add(id);
        wrapper.add(name);
        wrapper.add(role);
        wrapper.add(edit);
        wrapper.add(delete);

        return wrapper;
    }

    private void refreshTable(List<User> users) {
        container.removeAll();
        updateHeader();

        for (User u : users) {
            container.add(createRow(u));
        }

        container.revalidate();
        container.repaint();
    }

    private void handleNext() {
        int startIndex = (prevStartIndex == 0) ? 11 : prevStartIndex + 10;
        List<User> newUsers = getTenUsers(startIndex);

        if (newUsers.size() == 0) {
            JOptionPane.showMessageDialog(null, "No next page");
            return;
        }

        prevStartIndex = startIndex;
        currentPage++;

        refreshTable(newUsers);
    }

    private void handlePrev() {
        int startIndex = (prevStartIndex == 11) ? 0 : prevStartIndex - 10;
        List<User> prevUsers = getTenUsers(startIndex);

        if (prevUsers.size() == 0) {
            JOptionPane.showMessageDialog(null, "No previous page");
            return;
        }

        prevStartIndex = startIndex;
        currentPage--;

        refreshTable(prevUsers);
    }

    private void handleDelete(User user) {

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (!new ConfirmationDialog(frame, "Delete user", "Are you sure you want to delete this user?").isConfirmed())
            return;

        ResponseObject response = UserDomain.deleteUser(user.studentId);

        if (response.success) {
            JOptionPane.showMessageDialog(null, response.message);
            refreshTable(getTenUsers(prevStartIndex));
            return;
        }

        JOptionPane.showMessageDialog(null, response.message, "Erorr", JOptionPane.ERROR_MESSAGE);
    }

    private void openEditDialog(User user) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit User", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(5, 2, 5, 5));
        dialog.setLocationRelativeTo(this);

        dialog.getContentPane().setBackground(new Color(173, 216, 230));

        JTextField nameField = new JTextField(user.name);
        JPasswordField passwordField = new JPasswordField(String.valueOf(user.password));
        JComboBox<UserRole> roleBox = new JComboBox<>(UserRole.values());
        roleBox.setSelectedItem(user.role);

        roleBox.setSelectedItem(user.role.toString());

        JButton save = new JButton("Save");
        save.setBackground(new Color(135, 206, 250));
        save.setFocusPainted(false);

        JButton cancel = new JButton("Cancel");
        cancel.setBackground(new Color(135, 206, 250));
        cancel.setFocusPainted(false);

        dialog.add(new JLabel("Student ID:"));
        dialog.add(new JLabel(user.studentId));

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);

        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);

        dialog.add(new JLabel("Role:"));
        dialog.add(roleBox);

        dialog.add(save);
        dialog.add(cancel);

        save.addActionListener(e -> {
            String newName = nameField.getText();
            char[] newPassword = passwordField.getPassword();
            UserRole role = (UserRole) roleBox.getSelectedItem();

            ResponseObject response = UserDomain.editUser(
                    user.studentId,
                    newName,
                    newPassword,
                    role);

            JOptionPane.showMessageDialog(dialog, response.message);

            if (response.success) {
                dialog.dispose();
                refreshTable(getTenUsers(prevStartIndex));
            }
        });

        cancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private List<User> getTenUsers(int startIndex) {
        List<User> users = UserDomain.getUsers();
        List<User> selected = new LinkedList<>();

        if (startIndex > users.size())
            return selected;

        for (int i = startIndex; i < startIndex + 10; i++) {
            try {
                selected.add(users.get(i));
            } catch (Exception e) {
                break;
            }
        }

        return selected;
    }
}
