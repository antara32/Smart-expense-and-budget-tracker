package com.smartexpense;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class RegisterGUI extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;

    private final Color backgroundColor = new Color(244, 247, 252);
    private final Color primaryColor = new Color(76, 110, 245);
    private final Color textColor = new Color(45, 55, 75);
    private final Color mutedColor = new Color(120, 130, 150);

    public RegisterGUI() {

        setTitle("Smart Expense Tracker | Create Account");
        setSize(520, 650);
        setMinimumSize(new Dimension(450, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(new EmptyBorder(25, 45, 25, 45));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(
                new BoxLayout(headerPanel, BoxLayout.Y_AXIS)
        );

        JLabel logoLabel = new JLabel("SmartExpense");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 29));
        logoLabel.setForeground(primaryColor);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(new EmptyBorder(12, 0, 5, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
                "Start managing your money smarter"
        );
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(mutedColor);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(logoLabel);
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        // Registration form
        JPanel formPanel = new JPanel(
                new GridLayout(0, 1, 0, 8)
        );
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(22, 25, 22, 25));

        JLabel nameLabel = createLabel("Full Name");
        nameField = new JTextField();
        styleTextField(nameField);
        nameField.setToolTipText("Enter your full name");

        JLabel emailLabel = createLabel("Email Address");
        emailField = new JTextField();
        styleTextField(emailField);
        emailField.setToolTipText("Enter a valid email address");

        JLabel passwordLabel = createLabel("Password");
        passwordField = new JPasswordField();
        styleTextField(passwordField);
        passwordField.setToolTipText(
                "Use at least 8 characters"
        );

        showPasswordCheckBox = new JCheckBox("Show password");
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.setForeground(mutedColor);
        showPasswordCheckBox.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        showPasswordCheckBox.setFocusPainted(false);

        char defaultEchoChar = passwordField.getEchoChar();

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar(defaultEchoChar);
            }
        });

        JButton registerButton = new JButton("Create Account");
        stylePrimaryButton(registerButton);

        JLabel noteLabel = new JLabel(
                "Your account details will be stored in the database."
        );
        noteLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        noteLabel.setForeground(mutedColor);
        noteLabel.setHorizontalAlignment(SwingConstants.CENTER);

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(showPasswordCheckBox);
        formPanel.add(Box.createVerticalStrut(3));
        formPanel.add(registerButton);
        formPanel.add(noteLabel);

        // Footer
        JLabel footerLabel = new JLabel(
                "Smart Expense Tracker | Personal Finance Manager",
                SwingConstants.CENTER
        );
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footerLabel.setForeground(mutedColor);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        registerButton.addActionListener(e -> registerUser());
        getRootPane().setDefaultButton(registerButton);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(textColor);
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 225, 235)
                ),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }

    private void stylePrimaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(primaryColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 42));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void registerUser() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Invalid Email",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (password.length() < 8) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least 8 characters.",
                    "Weak Password",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        User user = new User(name, email, password);
        UserDAO userDAO = new UserDAO();

        if (userDAO.registerUser(user)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Registration successful! You can now log in.",
                    "Account Created",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed. This email may already be registered.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegisterGUI gui = new RegisterGUI();
            gui.setVisible(true);
        });
    }
}
