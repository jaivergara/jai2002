package src.components;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import src.objects.AuthenticateUser;
import src.services.UserDomain;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;

public class LoginPanel extends JPanel {
    public LoginPanel() {

        // 🎨 Colors
        Color lightBlue = new Color(173, 216, 230);
        Color softBlue = new Color(135, 206, 250);

        setLayout(new GridBagLayout());
        setBackground(lightBlue);

        JLabel title = new JLabel("Attendance Tracker", JLabel.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(lightBlue);

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setOpaque(true);
        studentIdLabel.setBackground(lightBlue);

        JTextField studentIdField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setOpaque(true);
        passwordLabel.setBackground(lightBlue);

        JPasswordField passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(softBlue);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> {
            UserDomain user = new UserDomain();

            AuthenticateUser auth = user.authenticateUser(studentIdField.getText(),
                    passwordField.getPassword());

            if (auth.success) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                frame.setContentPane(new Dashboard(auth.user));
                frame.revalidate();
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        auth.message,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;

        JPanel form = new JPanel();
        form.setPreferredSize(new Dimension(250, 250));
        form.setLayout(new GridBagLayout());
        form.setBackground(lightBlue);

        gbc.gridy = 0;
        form.add(title, gbc);

        gbc.gridy++;
        add(form, gbc);

        gbc.gridy++;
        form.add(studentIdLabel, gbc);

        gbc.gridy++;
        form.add(studentIdField, gbc);

        gbc.gridy++;
        form.add(passwordLabel, gbc);

        gbc.gridy++;
        form.add(passwordField, gbc);

        gbc.gridy++;
        form.add(loginButton, gbc);

    }
}