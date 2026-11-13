package com.hospital.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Hospital Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Add the tabs
        tabbedPane.addTab("Patients", new PatientsPanel());
        tabbedPane.addTab("Doctors", new DoctorsPanel());
        tabbedPane.addTab("Appointments", new AppointmentsPanel());
        tabbedPane.addTab("Billing", new BillingPanel());

        add(tabbedPane);
    }

    // THIS IS THE ONLY MAIN METHOD IN YOUR APP
    public static void main(String[] args) {
        try {
            // SWITCH TO DARK THEME
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            UIManager.put("Component.focusColor", new Color(0, 122, 255));
            UIManager.put("Button.default.background", new Color(0, 122, 255));
            UIManager.put("Button.default.foreground", Color.WHITE);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", true);
            UIManager.put("Table.intercellSpacing", new Dimension(1, 1));
        } catch (Exception ex) {
            System.err.println("Failed to initialize theme");
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}