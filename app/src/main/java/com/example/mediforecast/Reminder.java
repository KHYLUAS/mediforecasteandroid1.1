package com.example.mediforecast;

public class Reminder {
    private String medicineName;
    private String medicineDosage;
    private String startDate;

    public Reminder(){

    }
    public Reminder(String medicineName, String medicineDosage, String startDate){
        this.medicineName = medicineName;
        this.medicineDosage = medicineDosage;
        this.startDate = startDate;
    }
    public String getMedicineName(){
        return medicineName;
    }
    public void setMedicineName(String medicineName){
        this.medicineName = medicineName;
    }
    public String getMedicineDosage(){
        return medicineDosage;
    }
    public void setMedicineDosage(String medicineDosage){
        this.medicineDosage = medicineDosage;
    }
    public String getStartDate(){
        return startDate;
    }
    public void setStartDate(String startDate){
        this.startDate = startDate;
    }
}
