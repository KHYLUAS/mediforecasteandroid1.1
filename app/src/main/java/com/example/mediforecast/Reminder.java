package com.example.mediforecast;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Reminder {
    private String medicineName;
    private String medicineDosage;
    private String startDate;

    private static final SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/yyyy", Locale.US);
    private static final SimpleDateFormat outputDateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);

    public Reminder() {
    }

    public Reminder(String medicineName, String medicineDosage, String startDate) {
        this.medicineName = medicineName;
        this.medicineDosage = medicineDosage;
        this.startDate = startDate;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineDosage() {
        return medicineDosage;
    }

    public void setMedicineDosage(String medicineDosage) {
        this.medicineDosage = medicineDosage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDay() {
        // Extract day from formatted startDate (e.g., "Mon 09 Sep 2024")
        String[] parts = startDate.split(" ");
        return parts.length > 0 ? parts[0] : "";
    }

    public String getDate() {
        // Extract date from formatted startDate (e.g., "09")
        String[] parts = startDate.split(" ");
        return parts.length > 1 ? parts[1] : "";
    }

    public String getMonth() {
        // Extract month from formatted startDate (e.g., "Sep")
        String[] parts = startDate.split(" ");
        return parts.length > 2 ? parts[2] : "";
    }

    private String getFormattedDatePart(String format) {
        if (startDate == null || startDate.isEmpty()) {
            Log.w("Reminder", "StartDate is null or empty");  // Add logging here
            return "";
        }
        try {
            Date date = inputDateFormat.parse(startDate);
            if (date != null) {
                SimpleDateFormat partFormat = new SimpleDateFormat(format, Locale.US);
                String result = partFormat.format(date);
                Log.d("Reminder", "Formatted date part (" + format + "): " + result);  // Log the formatted part
                return result;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}