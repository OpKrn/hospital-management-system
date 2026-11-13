package com.hospital.dao;

import com.hospital.db.DBConnection;
import com.hospital.model.Billing;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {

    public void addBill(Billing bill) {
        String sql = "INSERT INTO bills(patient_id, amount, payment_status, bill_date) VALUES(?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bill.getPatientId());
            pstmt.setDouble(2, bill.getAmount());
            pstmt.setString(3, bill.getPaymentStatus());
            pstmt.setString(4, bill.getBillDate());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markAsPaid(int billId) {
        String sql = "UPDATE bills SET payment_status = 'Paid' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, billId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Billing> getAllBills() {
        List<Billing> list = new ArrayList<>();
        String sql = "SELECT b.*, p.name as p_name FROM bills b JOIN patients p ON b.patient_id = p.id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Billing(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getDouble("amount"),
                        rs.getString("payment_status"),
                        rs.getString("bill_date"),
                        rs.getString("p_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void deleteBill(int id) {
        String sql = "DELETE FROM bills WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}