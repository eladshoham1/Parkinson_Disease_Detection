package com.example.parkinson_disease_detection.models;

public class Patient extends User {
    private String doctor;

    public Patient() {
        super();
        doctor = null;
    }

    public Patient(String uid, String fullName, String email) {
        super(uid, fullName, email, Type.PATIENT);
        this.doctor = null;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
