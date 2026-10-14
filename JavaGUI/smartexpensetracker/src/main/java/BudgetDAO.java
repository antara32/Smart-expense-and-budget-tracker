package com.smartexpense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BudgetDAO {

    public boolean saveBudget(int userId, double amount) {
        String sql = "INSERT INTO monthly_budgets (user_id, budget_month, budget_amount) " +
                     "VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE budget_amount = ?";

        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setDate(2, java.sql.Date.valueOf(monthStart));
                ps.setDouble(3, amount);
                ps.setDouble(4, amount);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getMonthlyBudget(int userId) {
        String sql = "SELECT budget_amount FROM monthly_budgets " +
                     "WHERE user_id = ? AND budget_month = ?";

        LocalDate monthStart = LocalDate.now().withDayOfMonth(1);

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return 0;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setDate(2, java.sql.Date.valueOf(monthStart));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble("budget_amount");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getCurrentMonthExpenses(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total " +
                     "FROM transactions " +
                     "WHERE user_id = ? " +
                     "AND LOWER(type) = 'expense' " +
                     "AND transaction_date >= ? " +
                     "AND transaction_date < ?";

        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate nextMonth = monthStart.plusMonths(1);

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return 0;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setDate(2, java.sql.Date.valueOf(monthStart));
                ps.setDate(3, java.sql.Date.valueOf(nextMonth));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble("total");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
