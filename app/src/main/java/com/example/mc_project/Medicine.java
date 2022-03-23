package com.example.mc_project;

import java.util.Arrays;
import java.util.List;

public class Medicine {
    private String MedName;
    private List<Boolean> days;
    private int hr,min;
    private float dosage;

    public Medicine(){
        MedName="medicine ";
        days= Arrays.asList(new Boolean[7]);       // 0: sun, ...., 6: sat
    }

    public Medicine(String mName){
        MedName=mName;
        days= Arrays.asList(new Boolean[7]);       // 0: sun, ...., 6: sat
    }

    public String getMedName() {
        return MedName;
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public float getDosage() {
        return dosage;
    }

    public void setDosage(float dosage) {
        this.dosage = dosage;
    }
//    public String getPatient_email() {
//        return patient_email;
//    }

    public void setMedName(String medName) {
        MedName = medName;
    }


    public void setDays(List days) {
        this.days = days;
    }

    public List getDays() {
        return days;
    }
}
