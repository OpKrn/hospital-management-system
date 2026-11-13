package com.hospital.db;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void init() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String schemaSql = Files.readString(Paths.get("schema.sql"));

            String[] queries = schemaSql.split(";");
            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    stmt.execute(query);
                }
            }
            System.out.println("Database tables initialized successfully.");

        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        init();
    }
}