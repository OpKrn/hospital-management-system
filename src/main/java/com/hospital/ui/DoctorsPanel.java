package com.hospital.ui;

import com.hospital.dao.DoctorDAO;
import com.hospital.model.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorsPanel extends JPanel {
    private JTextField nameField, specField, phoneField;
    private JTable table;
    private DefaultTableModel tableModel;
    private DoctorDAO doctorDAO;
    private int selectedId = -1;

    public DoctorsPanel() {
        doctorDAO = new DoctorDAO();
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Form Container (Centered)
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBorder(BorderFactory.createTitledBorder(" Add / Update Doctor "));

        JPanel formPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        formPanel.add(new JLabel("Name:")); formPanel.add(new JLabel("Specialization:")); formPanel.add(new JLabel("Phone:"));

        nameField = new JTextField(15); specField = new JTextField(15); phoneField = new JTextField(15);
        formPanel.add(nameField); formPanel.add(specField); formPanel.add(phoneField);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addBtn = new JButton("Add Doctor");
        addBtn.setBackground(new Color(0, 122, 255)); addBtn.setForeground(Color.WHITE);
        JButton delBtn = new JButton("Delete");
        delBtn.setBackground(new Color(220, 53, 69)); delBtn.setForeground(Color.WHITE);
        JButton clearBtn = new JButton("Clear");

        btnPanel.add(addBtn); btnPanel.add(delBtn); btnPanel.add(clearBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; formContainer.add(formPanel, gbc);
        gbc.gridy = 1; formContainer.add(btnPanel, gbc);

        add(formContainer, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Specialization", "Phone"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(" Doctor Directory "));
        add(scrollPane, BorderLayout.CENTER);

        // Wiring
        addBtn.addActionListener(e -> {
            if (selectedId == -1) { // Only add if not updating
                doctorDAO.addDoctor(new Doctor(nameField.getText(), specField.getText(), phoneField.getText()));
                clearForm(); refreshTable();
            }
        });

        delBtn.addActionListener(e -> {
            if (selectedId != -1) {
                doctorDAO.deleteDoctor(selectedId);
                clearForm(); refreshTable();
            } else JOptionPane.showMessageDialog(this, "Select a doctor first!");
        });

        clearBtn.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (table.getSelectedRow() != -1) {
                selectedId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
                nameField.setText(tableModel.getValueAt(table.getSelectedRow(), 1).toString());
                specField.setText(tableModel.getValueAt(table.getSelectedRow(), 2).toString());
                phoneField.setText(tableModel.getValueAt(table.getSelectedRow(), 3).toString());
            }
        });

        refreshTable();
    }

    private void clearForm() {
        nameField.setText(""); specField.setText(""); phoneField.setText("");
        selectedId = -1; table.clearSelection();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Doctor d : doctorDAO.getAllDoctors()) {
            tableModel.addRow(new Object[]{d.getId(), d.getName(), d.getSpecialization(), d.getPhone()});
        }
    }
}