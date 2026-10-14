package com.smartexpense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Save income or expense
    public boolean addTransaction(Transaction transaction) {

        String sql = "INSERT INTO transactions " +
                "(user_id, type, amount, category, description, transaction_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return false;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, transaction.getUserId());
                pst.setString(2, transaction.getType());
                pst.setDouble(3, transaction.getAmount());
                pst.setString(4, transaction.getCategory());
                pst.setString(5, transaction.getDescription());
                pst.setString(6, transaction.getDate());

                return pst.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all transactions for a user
    public List<Transaction> getTransactions(int userId) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM transactions " +
                "WHERE user_id = ? ORDER BY transaction_date DESC, id DESC";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return transactions;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, userId);

                try (ResultSet rs = pst.executeQuery()) {

                    while (rs.next()) {
                        transactions.add(new Transaction(
                                rs.getInt("id"),
                                rs.getInt("user_id"),
                                rs.getString("type"),
                                rs.getDouble("amount"),
                                rs.getString("category"),
                                rs.getString("description"),
                                rs.getString("transaction_date")
                        ));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }

    // Calculate total income or expenses
    public double getTotalByType(int userId, String type) {

        String sql = "SELECT COALESCE(SUM(amount), 0) AS total " +
                "FROM transactions WHERE user_id = ? AND LOWER(type) = LOWER(?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return 0.0;
            }

            try (PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, userId);
                pst.setString(2, type);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble("total");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    // Print transactions in the terminal
    public void showTransactions(int userId) {

        for (Transaction transaction : getTransactions(userId)) {
            System.out.println(
                    transaction.getId() + " | " +
                    transaction.getType() + " | ₹" +
                    transaction.getAmount() + " | " +
                    transaction.getCategory() + " | " +
                    transaction.getDescription() + " | " +
                    transaction.getDate()
            );
        }
    }
}
