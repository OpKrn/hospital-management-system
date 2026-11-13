package com.hospital.util;

import com.hospital.db.DBConnection;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Connected successfully!");
            System.out.println("DB file: " + System.getProperty("user.dir") + "/hospital.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}