package com.example.mc_project;

public class Medicine {
    public String MedName;
    public boolean[] days;

    public Medicine(){
        MedName="no name";
        days= new boolean[7];       // 0: sun, ...., 6: sat
    }
}
