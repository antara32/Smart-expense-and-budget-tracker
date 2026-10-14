
package com.smartexpense;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
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

public class LoginGUI extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;

    private final Color backgroundColor = new Color(244, 247, 252);
    private final Color primaryColor = new Color(76, 110, 245);
    private final Color textColor = new Color(45, 55, 75);
    private final Color mutedColor = new Color(120, 130, 150);

    public LoginGUI() {

        setTitle("Smart Expense Tracker | Login");
        setSize(900, 700);
        setMinimumSize(new Dimension(520, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main login card
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 35, 25, 35));
        mainPanel.setPreferredSize(new Dimension(430, 510));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(
                new BoxLayout(headerPanel, BoxLayout.Y_AXIS)
        );

        JLabel logoLabel = new JLabel("SmartExpense");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        logoLabel.setForeground(primaryColor);
        logoLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Welcome Back!");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 23));
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(new EmptyBorder(15, 0, 5, 0));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
                "Sign in to manage your finances"
        );
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(mutedColor);
        subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);

        headerPanel.add(logoLabel);
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        // Form
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 0, 8));
        formPanel.setBackground(Color.WHITE);

        JLabel emailLabel = new JLabel("Email Address");
        styleLabel(emailLabel);

        emailField = new JTextField();
        styleTextField(emailField);
        emailField.setToolTipText("Enter your registered email");

        JLabel passwordLabel = new JLabel("Password");
        styleLabel(passwordLabel);

        passwordField = new JPasswordField();
        styleTextField(passwordField);
        passwordField.setToolTipText("Enter your password");

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

        JButton loginButton = new JButton("Sign In");
        stylePrimaryButton(loginButton);

        JButton registerButton = new JButton("Create Account");
        styleSecondaryButton(registerButton);

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(showPasswordCheckBox);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(loginButton);
        formPanel.add(registerButton);

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

        // Center the fixed-size card, even when maximized
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(backgroundColor);
        backgroundPanel.add(mainPanel);

        setContentPane(backgroundPanel);

        // Login button action
        loginButton.addActionListener(e -> login());

        // Open registration window
        registerButton.addActionListener(e -> {
            RegisterGUI registerGUI = new RegisterGUI();
            registerGUI.setLocationRelativeTo(this);
            registerGUI.setVisible(true);
        });

        getRootPane().setDefaultButton(loginButton);
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(textColor);
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

    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(primaryColor);
        button.setBackground(new Color(238, 242, 255));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void login() {

        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your email and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.loginUser(email, password);

        if (user != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login successful! Welcome, " + user.getName(),
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE
            );

            DashboardGUI dashboard = new DashboardGUI(user);
            dashboard.setVisible(true);
            dispose();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginGUI gui = new LoginGUI();
            gui.setVisible(true);
        });
    }
}
