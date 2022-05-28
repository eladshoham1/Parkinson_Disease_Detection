package com.example.parkinson_disease_detection.models;

public class User {
    public enum Type {
        PATIENT,
        DOCTOR
    }

    private String uid;
    private String fullName;
    private String email;
    private Type type;

    public User() {
        this.uid = null;
        this.fullName = null;
        this.email = null;
        this.type = null;
    }

    public User(String uid, String fullName, String email, Type type) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
