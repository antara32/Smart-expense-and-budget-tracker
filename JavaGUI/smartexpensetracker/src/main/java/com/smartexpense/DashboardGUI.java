
package com.smartexpense;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class DashboardGUI extends JFrame {

    private final User user;
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final BudgetDAO budgetDAO = new BudgetDAO();

    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel balanceLabel;

    private JLabel budgetLabel;
    private JLabel monthlyExpenseLabel;
    private JLabel budgetStatusLabel;
    private JProgressBar budgetProgressBar;

    private DefaultTableModel tableModel;
    private JTable transactionTable;

    private final Color backgroundColor = new Color(245, 247, 251);
    private final Color primaryColor = new Color(79, 70, 229);
    private final Color greenColor = new Color(22, 163, 74);
    private final Color redColor = new Color(220, 38, 38);

    public DashboardGUI(User user) {
        this.user = user;

        setTitle("Smart Expense Tracker - Dashboard");
        setSize(1100, 760);
        setMinimumSize(new Dimension(900, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);

        add(createSidebar(), BorderLayout.WEST);
        add(createMainPanel(), BorderLayout.CENTER);

        refreshDashboard();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBackground(new Color(30, 41, 59));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("Smart Expense");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 21));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("TRACK YOUR MONEY");
        subtitle.setForeground(new Color(203, 213, 225));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(40));

        addSidebarButton(sidebar, "Dashboard", this::refreshDashboard);
        addSidebarButton(sidebar, "Add Income",
                () -> openTransactionForm("Income"));
        addSidebarButton(sidebar, "Add Expense",
                () -> openTransactionForm("Expense"));
        addSidebarButton(sidebar, "View Transactions",
                this::showAllTransactions);

        sidebar.add(Box.createVerticalGlue());

        JButton logoutButton = new JButton("Logout");
        styleSidebarButton(logoutButton);

        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new LoginGUI().setVisible(true);
            }
        });

        sidebar.add(logoutButton);
        return sidebar;
    }

    private void addSidebarButton(
            JPanel sidebar, String text, Runnable action) {

        JButton button = new JButton(text);
        styleSidebarButton(button);
        button.addActionListener(e -> action.run());

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(10));
    }

    private void styleSidebarButton(JButton button) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setPreferredSize(new Dimension(180, 42));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(51, 65, 85));
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(
                BorderFactory.createEmptyBorder(8, 12, 8, 8));
        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private JPanel createMainPanel() {
        JPanel main = new JPanel(new BorderLayout(0, 15));
        main.setBackground(backgroundColor);
        main.setBorder(
                BorderFactory.createEmptyBorder(20, 22, 20, 22));

        // Dashboard heading and quick action buttons
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(30, 41, 59));

        JLabel welcome = new JLabel("Welcome, " + user.getName());
        welcome.setFont(new Font("SansSerif", Font.PLAIN, 15));
        welcome.setForeground(new Color(100, 116, 139));

        heading.add(title);
        heading.add(Box.createVerticalStrut(6));
        heading.add(welcome);

        JButton addIncomeButton = new JButton("+ Add Income");
        addIncomeButton.setBackground(greenColor);
        addIncomeButton.setForeground(Color.WHITE);
        addIncomeButton.setFont(
                new Font("SansSerif", Font.BOLD, 12));
        addIncomeButton.setFocusPainted(false);
        addIncomeButton.addActionListener(
                e -> openTransactionForm("Income"));

        JButton addExpenseButton = new JButton("+ Add Expense");
        addExpenseButton.setBackground(redColor);
        addExpenseButton.setForeground(Color.WHITE);
        addExpenseButton.setFont(
                new Font("SansSerif", Font.BOLD, 12));
        addExpenseButton.setFocusPainted(false);
        addExpenseButton.addActionListener(
                e -> openTransactionForm("Expense"));

        JPanel quickButtons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 6, 0));
        quickButtons.setOpaque(false);
        quickButtons.add(addIncomeButton);
        quickButtons.add(addExpenseButton);

        top.add(heading, BorderLayout.WEST);
        top.add(quickButtons, BorderLayout.EAST);

        // Summary cards
        JPanel summaryCards = new JPanel(
                new GridLayout(1, 3, 12, 0));
        summaryCards.setOpaque(false);

        incomeLabel = new JLabel("₹0.00");
        expenseLabel = new JLabel("₹0.00");
        balanceLabel = new JLabel("₹0.00");

        summaryCards.add(createSummaryCard(
                "Total Income", incomeLabel, greenColor));
        summaryCards.add(createSummaryCard(
                "Total Expenses", expenseLabel, redColor));
        summaryCards.add(createSummaryCard(
                "Current Balance", balanceLabel, primaryColor));

        // Monthly budget panel
        JPanel budgetPanel = createBudgetPanel();

        // Recent transactions table
        String[] columns = {
                "Date", "Type", "Category", "Amount", "Description"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);
        transactionTable.setRowHeight(26);
        transactionTable.setFont(
                new Font("SansSerif", Font.PLAIN, 12));
        transactionTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12));
        transactionTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        transactionTable.setFillsViewportHeight(true);

        JScrollPane tableScroll = new JScrollPane(transactionTable);
        tableScroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(226, 232, 240)));

        JPanel recentPanel = new JPanel(
                new BorderLayout(0, 8));
        recentPanel.setBackground(Color.WHITE);
        recentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));

        JLabel recentTitle = new JLabel("Recent Transactions");
        recentTitle.setFont(
                new Font("SansSerif", Font.BOLD, 16));
        recentTitle.setForeground(new Color(30, 41, 59));

        JButton viewAllButton = new JButton("View All");
        viewAllButton.addActionListener(
                e -> showAllTransactions());

        JPanel tableHeading = new JPanel(new BorderLayout());
        tableHeading.setOpaque(false);
        tableHeading.add(recentTitle, BorderLayout.WEST);
        tableHeading.add(viewAllButton, BorderLayout.EAST);

        recentPanel.add(tableHeading, BorderLayout.NORTH);
        recentPanel.add(tableScroll, BorderLayout.CENTER);

        // Arrange the dashboard vertically
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        summaryCards.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 105));
        summaryCards.setPreferredSize(new Dimension(700, 105));

        budgetPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 155));
        budgetPanel.setPreferredSize(new Dimension(700, 155));

        recentPanel.setMinimumSize(new Dimension(400, 180));
        recentPanel.setPreferredSize(new Dimension(700, 260));

        center.add(summaryCards);
        center.add(Box.createVerticalStrut(12));
        center.add(budgetPanel);
        center.add(Box.createVerticalStrut(12));
        center.add(recentPanel);

        main.add(top, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        return main;
    }

    private JPanel createSummaryCard(
            String title, JLabel amountLabel, Color color) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(15, 14, 15, 14)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(100, 116, 139));

        amountLabel.setFont(
                new Font("SansSerif", Font.BOLD, 23));
        amountLabel.setForeground(color);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(amountLabel);

        return card;
    }

    private JPanel createBudgetPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel title = new JLabel("Monthly Budget");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(30, 41, 59));

        JButton setBudgetButton = new JButton("Set Monthly Budget");
        setBudgetButton.addActionListener(e -> setMonthlyBudget());

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(title, BorderLayout.WEST);
        header.add(setBudgetButton, BorderLayout.EAST);

        budgetLabel = new JLabel("Monthly Budget: ₹0.00");
        monthlyExpenseLabel = new JLabel(
                "This Month's Expenses: ₹0.00");

        budgetLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 12));
        monthlyExpenseLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        budgetProgressBar = new JProgressBar(0, 100);
        budgetProgressBar.setValue(0);
        budgetProgressBar.setStringPainted(true);
        budgetProgressBar.setString("0%");
        budgetProgressBar.setForeground(greenColor);
        budgetProgressBar.setPreferredSize(
                new Dimension(200, 20));

        budgetStatusLabel = new JLabel(
                "Set a monthly budget to track your spending.");
        budgetStatusLabel.setFont(
                new Font("SansSerif", Font.BOLD, 12));
        budgetStatusLabel.setForeground(
                new Color(100, 116, 139));

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        details.add(budgetLabel);
        details.add(Box.createVerticalStrut(4));
        details.add(monthlyExpenseLabel);
        details.add(Box.createVerticalStrut(7));
        details.add(budgetProgressBar);
        details.add(Box.createVerticalStrut(5));
        details.add(budgetStatusLabel);

        panel.add(header, BorderLayout.NORTH);
        panel.add(details, BorderLayout.CENTER);

        return panel;
    }

    private void setMonthlyBudget() {
        String input = JOptionPane.showInputDialog(
                this,
                "Enter your budget for this month (₹):",
                "Set Monthly Budget",
                JOptionPane.QUESTION_MESSAGE
        );

        if (input == null) {
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(input.trim());

            if (!Double.isFinite(amount) || amount <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter an amount greater than zero.",
                        "Invalid Budget",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid amount, such as 10000 or 1250.50.",
                    "Invalid Budget",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean saved = budgetDAO.saveBudget(user.getId(), amount);

        if (saved) {
            JOptionPane.showMessageDialog(
                    this,
                    "Monthly budget saved successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            refreshDashboard();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not save the budget. Please check the database connection.",
                    "Save Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateBudgetPanel() {
        double budget = budgetDAO.getMonthlyBudget(user.getId());
        double monthlyExpenses =
                budgetDAO.getCurrentMonthExpenses(user.getId());

        budgetLabel.setText(
                String.format("Monthly Budget: ₹%.2f", budget));

        monthlyExpenseLabel.setText(
                String.format(
                        "This Month's Expenses: ₹%.2f",
                        monthlyExpenses));

        if (budget <= 0) {
            budgetProgressBar.setValue(0);
            budgetProgressBar.setString("0%");
            budgetProgressBar.setForeground(greenColor);

            budgetStatusLabel.setText(
                    "Set a monthly budget to track your spending.");
            budgetStatusLabel.setForeground(
                    new Color(100, 116, 139));
            return;
        }

        int percentage = (int) Math.min(
                100, Math.round(monthlyExpenses / budget * 100));

        budgetProgressBar.setValue(percentage);
        budgetProgressBar.setString(percentage + "%");

        if (monthlyExpenses > budget) {
            budgetProgressBar.setForeground(redColor);
            budgetStatusLabel.setText(
                    String.format(
                            "Budget exceeded by ₹%.2f.",
                            monthlyExpenses - budget));
            budgetStatusLabel.setForeground(redColor);
        } else if (monthlyExpenses >= budget * 0.80) {
            budgetProgressBar.setForeground(
                    new Color(217, 119, 6));
            budgetStatusLabel.setText(
                    "Warning: You have used at least 80% of your budget.");
            budgetStatusLabel.setForeground(
                    new Color(217, 119, 6));
        } else {
            budgetProgressBar.setForeground(greenColor);
            budgetStatusLabel.setText(
                    String.format(
                            "You have ₹%.2f remaining in your budget.",
                            budget - monthlyExpenses));
            budgetStatusLabel.setForeground(greenColor);
        }
    }

    private void showBudgetAlertAfterExpense() {
        double budget = budgetDAO.getMonthlyBudget(user.getId());

        if (budget <= 0) {
            return;
        }

        double monthlyExpenses =
                budgetDAO.getCurrentMonthExpenses(user.getId());

        if (monthlyExpenses > budget) {
            JOptionPane.showMessageDialog(
                    this,
                    String.format(
                            "Your monthly budget has been exceeded by ₹%.2f.",
                            monthlyExpenses - budget),
                    "Budget Exceeded",
                    JOptionPane.WARNING_MESSAGE
            );
        } else if (monthlyExpenses >= budget * 0.80) {
            JOptionPane.showMessageDialog(
                    this,
                    String.format(
                            "You have used %.0f%% of your monthly budget.",
                            monthlyExpenses / budget * 100),
                    "Budget Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void openTransactionForm(String type) {
        JTextField amountField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dateField =
                new JTextField(LocalDate.now().toString());

        String[] incomeCategories = {
                "Salary", "Business", "Investment", "Gift", "Other"
        };

        String[] expenseCategories = {
                "Food", "Transport", "Shopping", "Bills",
                "Health", "Education", "Entertainment", "Other"
        };

        JComboBox<String> categoryBox = new JComboBox<>(
                type.equalsIgnoreCase("Income")
                        ? incomeCategories
                        : expenseCategories
        );

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 12));
        form.setBorder(
                BorderFactory.createEmptyBorder(10, 5, 10, 5));

        form.add(new JLabel("Type:"));
        form.add(new JLabel(type));

        form.add(new JLabel("Amount (₹):"));
        form.add(amountField);

        form.add(new JLabel("Category:"));
        form.add(categoryBox);

        form.add(new JLabel("Description:"));
        form.add(descriptionField);

        form.add(new JLabel("Date (yyyy-MM-dd):"));
        form.add(dateField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Add " + type,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountField.getText().trim());

            if (!Double.isFinite(amount) || amount <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter an amount greater than zero.",
                        "Invalid Amount",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid amount, such as 500 or 1250.50.",
                    "Invalid Amount",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String date = dateField.getText().trim();

        try {
            LocalDate.parse(date);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the date in yyyy-MM-dd format, such as 2026-10-09.",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Transaction transaction = new Transaction(
                user.getId(),
                type,
                amount,
                (String) categoryBox.getSelectedItem(),
                descriptionField.getText().trim(),
                date
        );

        boolean saved = transactionDAO.addTransaction(transaction);

        if (saved) {
            JOptionPane.showMessageDialog(
                    this,
                    type + " added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refreshDashboard();

            if (type.equalsIgnoreCase("Expense")) {
                showBudgetAlertAfterExpense();
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Transaction could not be saved. Please check the database connection and console output.",
                    "Save Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshDashboard() {
        double income =
                transactionDAO.getTotalByType(user.getId(), "Income");

        double expense =
                transactionDAO.getTotalByType(user.getId(), "Expense");

        double balance = income - expense;

        incomeLabel.setText(String.format("₹%.2f", income));
        expenseLabel.setText(String.format("₹%.2f", expense));
        balanceLabel.setText(String.format("₹%.2f", balance));

        updateBudgetPanel();

        List<Transaction> transactions =
                transactionDAO.getTransactions(user.getId());

        tableModel.setRowCount(0);

        int count = 0;

        for (Transaction transaction : transactions) {
            if (count >= 5) {
                break;
            }

            tableModel.addRow(new Object[]{
                    transaction.getDate(),
                    transaction.getType(),
                    transaction.getCategory(),
                    String.format("₹%.2f", transaction.getAmount()),
                    transaction.getDescription()
            });

            count++;
        }
    }

    private void showAllTransactions() {
        List<Transaction> transactions =
                transactionDAO.getTransactions(user.getId());

        String[] columns = {
                "Date", "Type", "Category", "Amount", "Description"
        };

        DefaultTableModel allModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Transaction transaction : transactions) {
            allModel.addRow(new Object[]{
                    transaction.getDate(),
                    transaction.getType(),
                    transaction.getCategory(),
                    String.format("₹%.2f", transaction.getAmount()),
                    transaction.getDescription()
            });
        }

        JTable allTable = new JTable(allModel);
        allTable.setRowHeight(28);
        allTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(allTable);
        scrollPane.setPreferredSize(new Dimension(750, 350));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "All Transactions",
                JOptionPane.PLAIN_MESSAGE
        );
    }
}
