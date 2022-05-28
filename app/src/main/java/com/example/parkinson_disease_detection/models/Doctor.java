package com.example.parkinson_disease_detection.models;

import java.util.HashMap;
import java.util.Map;

public class Doctor extends User {
    private Map<String, String> patients;

    public Doctor() {
        super();
        this.patients = new HashMap<>();
    }

    public Doctor(String uid, String fullName, String email) {
        super(uid, fullName, email, Type.DOCTOR);
        this.patients = new HashMap<>();
    }

    public Map<String, String> getPatients() {
        return patients;
    }

    public void setPatients(Map<String, String> patients) {
        this.patients = patients;
    }

    public void addPatient(String patientId, String patientName) {
        this.patients.put(patientId, patientName);
    }
}
