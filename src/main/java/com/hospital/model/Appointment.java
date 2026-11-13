package com.hospital.model;

public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private String appointmentDate;
    private String status;

    // Extra fields for display purposes
    private String patientName;
    private String doctorName;

    public Appointment(int patientId, int doctorId, String appointmentDate) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.status = "Booked";
    }

    public Appointment(int id, int patientId, int doctorId, String appointmentDate, String status, String patientName, String doctorName) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.patientName = patientName;
        this.doctorName = doctorName;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getStatus() { return status; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
}