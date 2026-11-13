package com.hospital.util;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;

public class TestPatientDAO {
    public static void main(String[] args) {
        // Initialize tables first (just in case)
        com.hospital.db.DatabaseInitializer.init();

        PatientDAO dao = new PatientDAO();

        // 1. Add a patient
        Patient p1 = new Patient("Karan Ji", 24, "Male", "555-1234", "123 Main St");
        dao.addPatient(p1);

        // 2. Read them back from the database
        System.out.println("--- Patient List ---");
        for (Patient p : dao.getAllPatients()) {
            System.out.println("ID: " + p.getId() + " | Name: " + p.getName() + " | Age: " + p.getAge() + " | Phone: " + p.getPhone() + " | Gender: " + p.getGender() + " | Address: " + p.getAddress());
        }
    }
}