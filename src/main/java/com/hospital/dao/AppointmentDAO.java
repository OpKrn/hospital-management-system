package com.hospital.dao;

import com.hospital.db.DBConnection;
import com.hospital.model.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public void addAppointment(Appointment appt) {
        String sql = "INSERT INTO appointments(patient_id, doctor_id, appointment_date, status) VALUES(?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appt.getPatientId());
            pstmt.setInt(2, appt.getDoctorId());
            pstmt.setString(3, appt.getAppointmentDate());
            pstmt.setString(4, appt.getStatus());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        // SQL JOIN to get names instead of just IDs
        String sql = "SELECT a.*, p.name as p_name, d.name as d_name " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "JOIN doctors d ON a.doctor_id = d.id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("appointment_date"),
                        rs.getString("status"),
                        rs.getString("p_name"),
                        rs.getString("d_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void deleteAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}