package com.example.mc_project;

import java.util.Arrays;
import java.util.List;

public class Medicine {
    private String MedName;
    private List<Boolean> days;
//    private List<Integer> hr,min;
    private List<String> time;
    private List<Float> dosage;

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

    public void setMedName(String medName) {
        MedName = medName;
    }


//
//    public List<Integer> getHr() {
//        return hr;
//    }
//
//    public void setHr(List<Integer> hr) {
//        this.hr = hr;
//    }
//
//    public List<Integer> getMin() {
//        return min;
//    }
//
//    public void setMin(List<Integer> min) {
//        this.min = min;
//    }


    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<Float> getDosage() {
        return dosage;
    }

    public void setDosage(List<Float> dosage) {
        this.dosage = dosage;
    }

//
//    public List getHr() {
//        return hr;
//    }
//
//    public void setHr(List hr) {
//        this.hr = hr;
//    }
//
//    public List getMin() {
//        return min;
//    }
//
//    public void setMin(int min) {
//        this.min = min;
//    }
//
//    public List getDosage() {
//        return dosage;
//    }
//
//    public void setDosage(float dosage) {
//        this.dosage = dosage;
//    }

    public void setDays(List days) {
        this.days = days;
    }

    public List getDays() {
        return days;
    }
}
