package com.hospital.ui;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientsPanel extends JPanel {
    private JTextField nameField, ageField, genderField, phoneField, addressField;
    private JTable table;
    private DefaultTableModel tableModel;
    private PatientDAO patientDAO;

    public PatientsPanel() {
        patientDAO = new PatientDAO();
        setLayout(new BorderLayout(10, 10)); // 10px gaps

        // --- 1. TOP SECTION: The Form ---
        JPanel formPanel = new JPanel(new GridLayout(2, 5, 5, 5)); // 2 rows, 5 columns
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Patient"));

        formPanel.add(new JLabel("Name:"));
        formPanel.add(new JLabel("Age:"));
        formPanel.add(new JLabel("Gender:"));
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(new JLabel("Address:"));

        nameField = new JTextField();
        ageField = new JTextField();
        genderField = new JTextField();
        phoneField = new JTextField();
        addressField = new JTextField();

        formPanel.add(nameField);
        formPanel.add(ageField);
        formPanel.add(genderField);
        formPanel.add(phoneField);
        formPanel.add(addressField);

        JButton addButton = new JButton("Add Patient");
        addButton.addActionListener(e -> addPatient());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // --- 2. BOTTOM SECTION: The Table ---
        String[] columns = {"ID", "Name", "Age", "Gender", "Phone", "Address"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Patient List"));

        add(scrollPane, BorderLayout.CENTER);

        // Load existing patients into the table when the app starts
        refreshTable();
    }

    private void addPatient() {
        try {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText()); // Convert text to number
            String gender = genderField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Phone cannot be empty!");
                return;
            }

            Patient newPatient = new Patient(name, age, gender, phone, address);
            patientDAO.addPatient(newPatient);

            // Clear the text fields after adding
            nameField.setText("");
            ageField.setText("");
            genderField.setText("");
            phoneField.setText("");
            addressField.setText("");

            refreshTable(); // Update the table to show the new patient

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
        }
    }

    private void refreshTable() {
        // Clear existing rows
        tableModel.setRowCount(0);

        // Fetch from DB via DAO
        List<Patient> patients = patientDAO.getAllPatients();
        for (Patient p : patients) {
            Object[] row = {p.getId(), p.getName(), p.getAge(), p.getGender(), p.getPhone(), p.getAddress()};
            tableModel.addRow(row);
        }
    }
}