package com.smartexpense;

public class Transaction {

    private int id;
    private int userId;
    private String type;
    private double amount;
    private String category;
    private String description;
    private String date;

    public Transaction(int id, int userId, String type, double amount,
                       String category, String description, String date) {

        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public Transaction(int userId, String type, double amount,
                       String category, String description, String date) {

        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}