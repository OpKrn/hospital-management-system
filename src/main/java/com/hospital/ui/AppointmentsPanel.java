package com.hospital.ui;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.model.Appointment;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentsPanel extends JPanel {
    private JComboBox<Patient> patientCombo;
    private JComboBox<Doctor> doctorCombo;

    // New Date and Time components
    private JSpinner dateSpinner;
    private JComboBox<String> hourCombo, minuteCombo, ampmCombo;

    private JTable table;
    private DefaultTableModel tableModel;
    private AppointmentDAO appointmentDAO;
    private int selectedId = -1;

    public AppointmentsPanel() {
        appointmentDAO = new AppointmentDAO();
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Form
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBorder(BorderFactory.createTitledBorder(" Book / Cancel Appointment "));

        // 2 rows, 4 columns for the labels and inputs
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.add(new JLabel("Select Patient:"));
        formPanel.add(new JLabel("Select Doctor:"));
        formPanel.add(new JLabel("Select Date:"));
        formPanel.add(new JLabel("Select Time:"));

        patientCombo = new JComboBox<>();
        doctorCombo = new JComboBox<>();

        // Date Spinner (Calendar)
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        // Time Dropdowns
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        String[] hours = new String[12];
        for(int i=0; i<12; i++) hours[i] = String.format("%02d", i+1);
        String[] mins = {"00", "15", "30", "45"};

        hourCombo = new JComboBox<>(hours);
        minuteCombo = new JComboBox<>(mins);
        ampmCombo = new JComboBox<>(new String[]{"AM", "PM"});

        timePanel.add(hourCombo);
        timePanel.add(new JLabel(":"));
        timePanel.add(minuteCombo);
        timePanel.add(ampmCombo);

        formPanel.add(patientCombo);
        formPanel.add(doctorCombo);
        formPanel.add(dateSpinner);
        formPanel.add(timePanel);

        // Buttons (Medium size, centered)
        JButton bookBtn = new JButton("Book Appointment");
        bookBtn.setBackground(new Color(0, 122, 255));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setPreferredSize(new Dimension(200, 40));
        bookBtn.putClientProperty("JButton.buttonType", "roundRect");

        JButton delBtn = new JButton("Cancel Appointment");
        delBtn.setBackground(new Color(220, 53, 69));
        delBtn.setForeground(Color.WHITE);
        delBtn.setPreferredSize(new Dimension(180, 40));
        delBtn.putClientProperty("JButton.buttonType", "roundRect");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.add(delBtn);
        btnPanel.add(bookBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; formContainer.add(formPanel, gbc);
        gbc.gridy = 1; formContainer.add(btnPanel, gbc);
        add(formContainer, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Patient Name", "Doctor Name", "Date & Time", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(" Appointment Schedule "));
        add(scrollPane, BorderLayout.CENTER);

        // Wiring
        bookBtn.addActionListener(e -> bookAppointment());
        delBtn.addActionListener(e -> {
            if (selectedId != -1) {
                appointmentDAO.deleteAppointment(selectedId);
                clearForm(); refreshTable();
            } else JOptionPane.showMessageDialog(this, "Select an appointment from the table first!");
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (table.getSelectedRow() != -1) selectedId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
        });

        loadComboBoxes();
        refreshTable();
    }

    private void clearForm() {
        selectedId = -1;
        table.clearSelection();
    }

    private void loadComboBoxes() {
        patientCombo.removeAllItems();
        doctorCombo.removeAllItems();
        for (Patient p : new PatientDAO().getAllPatients()) patientCombo.addItem(p);
        for (Doctor d : new DoctorDAO().getAllDoctors()) doctorCombo.addItem(d);
    }

    private void bookAppointment() {
        Patient p = (Patient) patientCombo.getSelectedItem();
        Doctor d = (Doctor) doctorCombo.getSelectedItem();

        if (p == null || d == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient and doctor."); return;
        }

        // Build the Date and Time strings from the UI components
        String dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(dateSpinner.getValue());
        String timeStr = hourCombo.getSelectedItem() + ":" + minuteCombo.getSelectedItem() + " " + ampmCombo.getSelectedItem();
        String finalDateTime = dateStr + " " + timeStr;

        appointmentDAO.addAppointment(new Appointment(p.getId(), d.getId(), finalDateTime));
        clearForm();
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Appointment a : appointmentDAO.getAllAppointments()) {
            // It will now perfectly display "John Doe (ID: 1)" and "Dr. Smith (ID: 2)"
            tableModel.addRow(new Object[]{a.getId(), a.getPatientName(), a.getDoctorName(), a.getAppointmentDate(), a.getStatus()});
        }
    }
}