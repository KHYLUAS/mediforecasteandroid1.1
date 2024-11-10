package com.example.mediforecast;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class twicedaily extends AppCompatActivity {
    private TextView timeValue, timeValue1, doseValue, doseValue1;
    private ImageView medicinearrow;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private int currentDose1 = 1;  // Use a separate dose variable for dose 1
    private int currentDose2 = 1;
    private Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_twicedaily);
        Register = findViewById(R.id.Register);
        timeValue = findViewById(R.id.timeValue);
        timeValue1 = findViewById(R.id.timeValue1);
        doseValue = findViewById(R.id.doseValue);
        doseValue1 = findViewById(R.id.doseValue1);
        medicinearrow = findViewById(R.id.medicinearrow);
        boolean isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        medicinearrow.setOnClickListener(v->{
            // I Change this with update 1:23 AM nov 10
            if (isUpdate){
                Intent intent = new Intent(twicedaily.this, medicineschedule.class);
                intent.putExtra("isUpdating", true);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(twicedaily.this, medicineschedule.class);
                startActivity(intent);
                finish();
            }
        });
        // Set click listeners for both timeValue and timeValue1 to show time pickers
        timeValue.setOnClickListener(v -> showTimePicker(timeValue));
        timeValue1.setOnClickListener(v -> showTimePicker(timeValue1));
        doseValue.setOnClickListener(v -> showDosePicker(doseValue, 1));
        doseValue1.setOnClickListener(v -> showDosePicker(doseValue1,2));
        if (isUpdate){
            String newUnitType = getIntent().getStringExtra("newUnitType");
            doseValue.setText(newUnitType);
            doseValue1.setText(newUnitType);
            Log.d("MedicineSchedule", "Update mode2");
        }else{
            String unitType = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    .getString("selectedUnitType", "");
            doseValue.setText(unitType);
            doseValue1.setText(unitType);
            Log.d("MedicineSchedule", "Add mode2");
        }


        Register.setOnClickListener(v->{
            String time1 = timeValue.getText().toString();
            String time2 = timeValue1.getText().toString();
            String dose1 = doseValue.getText().toString();
            String dose2 = doseValue1.getText().toString();
            Log.d("time1", time1);
            Log.d("time2", time2);
            Log.d("dose1", dose1);
            Log.d("dose2", dose2);
            String twiceDaily = "Twice Daily";
            if(time1.isEmpty() && dose1.isEmpty() && time2.isEmpty() && dose2.isEmpty()
                    || time1.isEmpty() || dose1.isEmpty() || time2.isEmpty() || dose2.isEmpty()){
                Toast.makeText(this, "Time and Dose is required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isUpdate){
                // Pass both times and doses to the next activity
                Intent intent = new Intent(twicedaily.this, UpdateReminder.class);
                intent.putExtra("time1", time1);
                intent.putExtra("time2", time2);
                intent.putExtra("dose1", dose1);
                intent.putExtra("dose2", dose2);
                intent.putExtra("updating", true);
                intent.putExtra("twiceDaily", twiceDaily);
                startActivity(intent);
                finish();
            }else{
                // Pass both times and doses to the next activity
                Intent intent = new Intent(twicedaily.this, medicine_signin.class);
                intent.putExtra("time1", time1);
                intent.putExtra("time2", time2);
                intent.putExtra("dose1", dose1);
                intent.putExtra("dose2", dose2);
                intent.putExtra("twiceDaily", twiceDaily);
                startActivity(intent);
                finish();
            }

        });
    }
    private void showTimePicker(TextView timeTextView) {
        // Initialize the MaterialTimePicker
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H) // 12-hour clock format
                .setHour(12) // Default hour
                .setMinute(0) // Default minute
                .setTitleText("Select Time")
                .build();

        // Show the time picker
        timePicker.show(getSupportFragmentManager(), "MediForecastTimePicker");

        // Handle time selection
        timePicker.addOnPositiveButtonClickListener(view -> {
            // Get the selected hour and minute
            int selectedHour = timePicker.getHour();
            int selectedMinute = timePicker.getMinute();

            // Format the selected time and set it in the clicked TextView
            String formattedTime = String.format("%02d:%02d %s",
                    (selectedHour % 12 == 0 ? 12 : selectedHour % 12),
                    selectedMinute,
                    (selectedHour >= 12) ? "PM" : "AM");

            // Set the formatted time in the respective TextView (timeValue or timeValue1)
            timeTextView.setText(formattedTime);
        });
    }
    private void showDosePicker(TextView doseTextView, int doseNumber) {
        Dialog doseDialog = new Dialog(this);
        doseDialog.setContentView(R.layout.dialog_dose_picker);

        // Initialize dialog components
        TextView doseNumberView = doseDialog.findViewById(R.id.doseNumber);
        ImageView buttonMinus = doseDialog.findViewById(R.id.buttonMinus);
        ImageView buttonPlus = doseDialog.findViewById(R.id.buttonPlus);
        Button buttonSetDose = doseDialog.findViewById(R.id.buttonSetDose);

        // Set the initial dose value based on the dose number
        if (doseNumber == 1) {
            doseNumberView.setText(String.valueOf(currentDose1));
        } else {
            doseNumberView.setText(String.valueOf(currentDose2));
        }

        // Increment the dose
        buttonPlus.setOnClickListener(v -> {
            if (doseNumber == 1) {
                currentDose1++;
                doseNumberView.setText(String.valueOf(currentDose1));
            } else {
                currentDose2++;
                doseNumberView.setText(String.valueOf(currentDose2));
            }
        });

        // Decrement the dose, but ensure it doesn't go below 1
        buttonMinus.setOnClickListener(v -> {
            if (doseNumber == 1 && currentDose1 > 1) {
                currentDose1--;
                doseNumberView.setText(String.valueOf(currentDose1));
            } else if (doseNumber == 2 && currentDose2 > 1) {
                currentDose2--;
                doseNumberView.setText(String.valueOf(currentDose2));
            }
        });

        // Set the selected dose when "Set Dose" is clicked
        buttonSetDose.setOnClickListener(v -> {
            boolean isUpdate = getIntent().getBooleanExtra("isUpdate", false);
            if (isUpdate) {
                String newUnitType = getIntent().getStringExtra("newUnitType");
                if (doseNumber == 1) {
                    doseValue.setText(String.valueOf(currentDose1) + " " + newUnitType);
                } else {
                    doseValue1.setText(String.valueOf(currentDose2) + " " + newUnitType);
                }
            } else {
                String unitType = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        .getString("selectedUnitType", "");// Default dose value
                if (doseNumber == 1) {
                    doseTextView.setText(String.valueOf(currentDose1) + " " + unitType);
                } else {
                    doseTextView.setText(String.valueOf(currentDose2) + " " + unitType);
                }
            }
            doseDialog.dismiss();  // Close the dialog
        });

        doseDialog.show();  // Display the dialog
    }
}