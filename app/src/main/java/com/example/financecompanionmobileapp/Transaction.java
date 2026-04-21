package com.example.financecompanionmobileapp;

public class Transaction {
    private int id;
    private String title;
    private double amount;
    private String type; // "Income" or "Expense"
    private String date; // Format: YYYY-MM-DD

    public Transaction(int id, String title, double amount, String type, String date) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getDate() { return date; }
}
