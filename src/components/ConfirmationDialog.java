package src.components;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Color;

public class ConfirmationDialog {
    private boolean isConfirmed = false;
    private JDialog dialog;

    public ConfirmationDialog(Frame frame, String title, String text) {
        dialog = new JDialog(frame, title, true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(new Dimension(400, 200));
        dialog.setLocationRelativeTo(frame);

        Color lightBlue = new Color(173, 216, 230);
        Color softBlue = new Color(135, 206, 250);

        dialog.getContentPane().setBackground(lightBlue);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton confirm = new JButton("Confirm");
        confirm.setBackground(softBlue);
        confirm.setFocusPainted(false);
        confirm.addActionListener(e -> {
            isConfirmed = true;
            dialog.dispose();
        });

        JButton cancel = new JButton("Cancel");
        cancel.setBackground(softBlue);
        cancel.setFocusPainted(false);
        cancel.addActionListener(e -> {
            isConfirmed = false;
            dialog.dispose();
        });

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new GridLayout(1, 2, 5, 0));
        buttonContainer.setBackground(lightBlue);

        gbc.gridy = 0;
        gbc.gridx = 0;

        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(lightBlue);

        dialog.add(label, gbc);

        gbc.gridy++;
        dialog.add(buttonContainer, gbc);

        buttonContainer.add(confirm);
        buttonContainer.add(cancel);

        dialog.setVisible(true);
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

}
