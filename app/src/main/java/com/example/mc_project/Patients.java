package com.example.mc_project;

public class Patients {
    public String fullName;
    public String email;
    public String password;
    public String phone;
    public String address;
    public String city;

    public Patients(){

    }

    public Patients(String fullName, String email, String password, String phone, String address, String city){
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.city = city;
    }

}
