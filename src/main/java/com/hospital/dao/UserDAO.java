package com.hospital.dao;

import com.hospital.db.DBConnection;
import com.hospital.util.PasswordUtil;
import java.sql.*;

public class UserDAO {

    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users(username, password_hash) VALUES(?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, PasswordUtil.hashPassword(password));
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // If the username already exists, SQLite throws an error
            return false;
        }
    }

    public boolean login(String username, String password) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String inputHash = PasswordUtil.hashPassword(password);
                // Compare the hashes
                return storedHash.equals(inputHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}