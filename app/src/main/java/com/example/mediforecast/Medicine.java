package com.example.mediforecast;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity(tableName = "medicines")
public class Medicine {
    @PrimaryKey(autoGenerate = true)
    public int id;

    private String medicineName;
    private String startDate;
    private String endDate;
    private String medicineType;
    private String unitType;
    private String schedule;
    private String selectedDays;

    private String time1;
    @Nullable private String time2;
    @Nullable private String time3;
    private String dose1;
    @Nullable private String dose2;
    @Nullable private String dose3;

    private static final SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/yyyy", Locale.US);
    private static final SimpleDateFormat outputDateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);

    public Medicine() {
    }

    public Medicine(String medicineName, String startDate, String endDate, String medicineType,
                    String unitType, String schedule, String selectedDays, String time1,
                    String dose1, @Nullable String time2, @Nullable String dose2,
                    @Nullable String time3,  @Nullable String dose3 ) {
        this.medicineName = medicineName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.medicineType = medicineType;
        this.unitType = unitType;
        this.schedule = schedule;
        this.selectedDays = selectedDays;
        this.time1 = time1;
        this.dose1 = dose1;
        this.time2 = time2;
        this.dose2 = dose2;
        this.time3 = time3;
        this.dose3 = dose3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(String selectedDays) {
        this.selectedDays = selectedDays;
    }

    public List<String> getDays() {
        if (selectedDays != null && !selectedDays.isEmpty()) {
            return Arrays.asList(selectedDays.split(",")); // Convert to a list of days
        }
        return Collections.emptyList(); // Return an empty list if no days are selected
    }


    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getTime1() {
        return time1;
    }

    public void setTime1( String time1) {
        this.time1 = time1;
    }
    @Nullable
    public String getDose1() {
        return dose1;
    }

    public void setDose1(String dose1) {
        this.dose1 = dose1;
    }
    @Nullable
    public String getTime2() {
        return time2;
    }

    public void setTime2(@Nullable String time2) {
        this.time2 = time2;
    }
    @Nullable
    public String getDose2() {
        return dose2;
    }

    public void setDose2(@Nullable String dose2) {
        this.dose2 = dose2;
    }

    @Nullable
    public String getTime3() {
        return time3;
    }

    public void setTime3(@Nullable String time3) {
        this.time3 = time3;
    }
    @Nullable
    public String getDose3() {
        return dose3;
    }

    public void setDose3(@Nullable String dose3) {
        this.dose3 = dose3;
    }


    public String getFormattedStartDate() {
        if (startDate == null || startDate.isEmpty()) {
            Log.w("Reminder", "StartDate is null or empty");
            return "";
        }
        try {
            // Parse the startDate using the input format ("dd/MM/yyyy")
            Date date = inputDateFormat.parse(startDate);
            if (date != null) {
                // Format the date into the desired format ("EEE, dd MMM yyyy")
                String formattedDate = outputDateFormat.format(date);
                Log.d("Reminder", "Formatted date: " + formattedDate);
                return formattedDate;  // This will return a format like "Wed, 18 Oct 2024"
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDay() {
        return getFormattedDatePart("EEE");  // This will return "Wed", "Mon", etc.
    }

    public String getDate() {
        return getFormattedDatePart("dd");  // This will return the day as a number, e.g., "18"
    }

    public String getMonth() {
        return getFormattedDatePart("MMM");  // This will return the month in words, e.g., "Oct"
    }

    private String getFormattedDatePart(String format) {
        if (startDate == null || startDate.isEmpty()) {
            Log.w("Reminder", "StartDate is null or empty");
            return "";
        }
        try {
            Date date = inputDateFormat.parse(startDate);  // Parse the input date
            if (date != null) {
                SimpleDateFormat partFormat = new SimpleDateFormat(format, Locale.US);  // Format date
                return partFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    @NonNull
    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", medicineName='" + medicineName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", medicineType='" + medicineType + '\'' +
                ", unitType='" + unitType + '\'' +
                ", schedule='" + schedule + '\'' +
                ", selectedDays='" + selectedDays + '\'' +
                ", time1='" + time1 + '\'' +
                ", time2=" + (time2 != null ? "'" + time2 + "'" : "null") +
                ", time3=" + (time3 != null ? "'" + time3 + "'" : "null") +
                ", dose1=" + (dose1 != null ? "'" + dose1 + "'" : "null") +
                ", dose2=" + (dose2 != null ? "'" + dose2 + "'" : "null") +
                ", dose3=" + (dose3 != null ? "'" + dose3 + "'" : "null") +
                '}';
    }

}
