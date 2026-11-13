package com.hospital.model;

public class Patient {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String address;

    //Constructor for new patient
    public Patient(String name, int age, String gender, String phone, String address){
        this.name = name;
        this.age =age;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

    // Constructor for reading from DB
    public Patient(int id,String name, int age, String gender, String phone, String address){
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

    public int getId(){ return id; }
    public String getName(){ return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}
