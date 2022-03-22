package com.example.mc_project;

public class Medicine {
    public String MedName;
    public String patient_email;
    public boolean[] days;

    public Medicine(){
        MedName="no name";
        patient_email="";
        days= new boolean[7];       // 0: sun, ...., 6: sat
    }
}
