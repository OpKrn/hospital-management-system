package com.hospital.model;

public class Billing {
    private int id;
    private int patientId;
    private double amount;
    private String paymentStatus;
    private String billDate;
    private String patientName; // For display

    public Billing(int patientId, double amount, String billDate) {
        this.patientId = patientId;
        this.amount = amount;
        this.billDate = billDate;
        this.paymentStatus = "Unpaid";
    }

    public Billing(int id, int patientId, double amount, String paymentStatus, String billDate, String patientName) {
        this.id = id;
        this.patientId = patientId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.billDate = billDate;
        this.patientName = patientName;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public double getAmount() { return amount; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getBillDate() { return billDate; }
    public String getPatientName() { return patientName; }
}