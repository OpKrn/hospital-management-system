package com.hospital.ui;

import com.hospital.dao.BillingDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.model.Billing;
import com.hospital.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BillingPanel extends JPanel {
    private JComboBox<Patient> patientCombo;
    private JTextField amountField;
    private JSpinner dateSpinner;
    private JComboBox<String> hourCombo, minuteCombo, ampmCombo;
    private JTable table;
    private DefaultTableModel tableModel;
    private BillingDAO billDAO;
    private int selectedId = -1;

    public BillingPanel() {
        billDAO = new BillingDAO();
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Form
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBorder(BorderFactory.createTitledBorder(" Generate Bill "));

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.add(new JLabel("Select Patient:")); formPanel.add(new JLabel("Amount (₹):"));
        formPanel.add(new JLabel("Date:")); formPanel.add(new JLabel("Time:"));

        patientCombo = new JComboBox<>();
        amountField = new JTextField();

        // Date Spinner
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        // Time Dropdowns (AM/PM)
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        String[] hours = new String[12]; for(int i=0; i<12; i++) hours[i] = String.format("%02d", i+1);
        String[] mins = {"00 to 15", "16 to 30", "31 to 45", "46 to 60"};
        hourCombo = new JComboBox<>(hours); minuteCombo = new JComboBox<>(mins); ampmCombo = new JComboBox<>(new String[]{"AM", "PM"});
        timePanel.add(hourCombo); timePanel.add(new JLabel(":")); timePanel.add(minuteCombo); timePanel.add(ampmCombo);

        formPanel.add(patientCombo); formPanel.add(amountField); formPanel.add(dateSpinner); formPanel.add(timePanel);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton genBtn = new JButton("Generate Bill");
        genBtn.setBackground(new Color(0, 122, 255)); genBtn.setForeground(Color.WHITE);
        genBtn.setPreferredSize(new Dimension(150, 35));

        JButton payBtn = new JButton("Mark as Paid");
        payBtn.setBackground(new Color(40, 167, 69)); payBtn.setForeground(Color.WHITE); // Green

        JButton delBtn = new JButton("Delete Bill");
        delBtn.setBackground(new Color(220, 53, 69)); delBtn.setForeground(Color.WHITE);

        btnPanel.add(genBtn); btnPanel.add(payBtn); btnPanel.add(delBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; formContainer.add(formPanel, gbc);
        gbc.gridy = 1; formContainer.add(btnPanel, gbc);
        add(formContainer, BorderLayout.NORTH);

        // Table
        String[] columns = {"Bill ID", "Patient Name", "Amount", "Date & Time", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(" Billing History "));
        add(scrollPane, BorderLayout.CENTER);

        // Wiring
        genBtn.addActionListener(e -> generateBill());
        payBtn.addActionListener(e -> markAsPaid());
        delBtn.addActionListener(e -> {
            if (selectedId != -1) {
                billDAO.deleteBill(selectedId); clearForm(); refreshTable();
            } else JOptionPane.showMessageDialog(this, "Select a bill first!");
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (table.getSelectedRow() != -1) selectedId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
        });

        loadPatients();
        refreshTable();
    }

    private void clearForm() { amountField.setText(""); selectedId = -1; table.clearSelection(); }

    private void loadPatients() {
        patientCombo.removeAllItems();
        for (Patient p : new PatientDAO().getAllPatients()) patientCombo.addItem(p);
    }

    private void generateBill() {
        Patient p = (Patient) patientCombo.getSelectedItem();
        if (p == null || amountField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a patient and enter an amount."); return;
        }
        try {
            double amount = Double.parseDouble(amountField.getText());
            // Format the date and time from the UI components
            String dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(dateSpinner.getValue());
            String timeStr = hourCombo.getSelectedItem() + ":" + minuteCombo.getSelectedItem() + " " + ampmCombo.getSelectedItem();
            String finalDateTime = dateStr + " " + timeStr;

            billDAO.addBill(new Billing(p.getId(), amount, finalDateTime));
            clearForm(); refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a number!");
        }
    }

    private void markAsPaid() {
        if (selectedId == -1) { JOptionPane.showMessageDialog(this, "Select a bill first!"); return; }
        billDAO.markAsPaid(selectedId);
        clearForm(); refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Billing b : billDAO.getAllBills()) {
            // We display the actual patient name here!
            tableModel.addRow(new Object[]{b.getId(), b.getPatientName(), "₹" + b.getAmount(), b.getBillDate(), b.getPaymentStatus()});
        }
    }
}