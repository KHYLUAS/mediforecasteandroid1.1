package com.example.mediforecast;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class thricedaily extends AppCompatActivity {
    private TextView timeValue, timeValue2, timeValue3, doseValue, doseValue2, doseValue3;
    private ImageView medicinearrow;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private int currentDose1 = 1;  // Use a separate dose variable for dose 1
    private int currentDose2 = 1;
    private int currentDose3 = 1;  // Use a separate dose variable for dose 1
    private Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thricedaily);
        timeValue = findViewById(R.id.timeValue);
        timeValue2 = findViewById(R.id.timeValue2);
        timeValue3 = findViewById(R.id.timeValue3);
        doseValue = findViewById(R.id.doseValue);
        doseValue2 = findViewById(R.id.doseValue2);
        doseValue3 = findViewById(R.id.doseValue3);
        medicinearrow = findViewById(R.id.medicinearrow);
        boolean isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        Register = findViewById(R.id.Register);
        medicinearrow.setOnClickListener(v->{
            if (isUpdate){
                // I Change this with update 1:23 AM nov 10
                Intent intent = new Intent(thricedaily.this, medicineschedule.class);
                intent.putExtra("isUpdating", true);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(thricedaily.this, medicineschedule.class);
                startActivity(intent);
                finish();
            }
        });
        timeValue.setOnClickListener(v -> showTimePicker(timeValue));
        timeValue2.setOnClickListener(v -> showTimePicker(timeValue2));
        timeValue3.setOnClickListener(v -> showTimePicker(timeValue3));
        doseValue.setOnClickListener(v -> showDosePicker(doseValue, 1));
        doseValue2.setOnClickListener(v -> showDosePicker(doseValue2, 2));
        doseValue3.setOnClickListener(v -> showDosePicker(doseValue3, 3));
        if (isUpdate){
            String newUnitType = getIntent().getStringExtra("newUnitType");
            doseValue.setText(newUnitType);
            doseValue2.setText(newUnitType);
            doseValue3.setText(newUnitType);
        }else{
            String unitType = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    .getString("selectedUnitType", "");// Default dose value
            doseValue.setText(unitType);
            doseValue2.setText(unitType);
            doseValue3.setText(unitType);
        }

        Register.setOnClickListener(v->{
            String time1 = timeValue.getText().toString();
            String time2 = timeValue2.getText().toString();
            String time3 = timeValue3.getText().toString();
            String dose1 = doseValue.getText().toString();
            String dose2 = doseValue2.getText().toString();
            String dose3 = doseValue3.getText().toString();
            String thriceDaily = "Thrice Daily";

            if(time1.isEmpty() && dose1.isEmpty() && time2.isEmpty() && dose2.isEmpty()
                    && time3.isEmpty() && dose3.isEmpty()
                    || time1.isEmpty() || dose1.isEmpty() || time2.isEmpty() || dose2.isEmpty()
                    || time3.isEmpty() || dose3.isEmpty()){
                Toast.makeText(this, "Time and Dose is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isUpdate){
                Intent intent = new Intent(thricedaily.this, UpdateReminder.class);
                intent.putExtra("time1", time1);
                intent.putExtra("time2", time2);
                intent.putExtra("time3", time3);
                intent.putExtra("dose1", dose1);
                intent.putExtra("dose2", dose2);
                intent.putExtra("dose3", dose3);
                intent.putExtra("updating", true);
                intent.putExtra("thriceDaily", thriceDaily);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(thricedaily.this, medicine_signin.class);
                intent.putExtra("time1", time1);
                intent.putExtra("time2", time2);
                intent.putExtra("time3", time3);
                intent.putExtra("dose1", dose1);
                intent.putExtra("dose2", dose2);
                intent.putExtra("dose3", dose3);
                intent.putExtra("thriceDaily", thriceDaily);
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

            // Set the formatted time in the respective TextView (timeValue, timeValue2, or timeValue3)
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

        // Set the initial dose number based on doseNumber
        if (doseNumber == 1) {
            doseNumberView.setText(String.valueOf(currentDose1));
        } else if (doseNumber == 2) {
            doseNumberView.setText(String.valueOf(currentDose2));
        } else {
            doseNumberView.setText(String.valueOf(currentDose3));
        }

        // Increment the dose
        buttonPlus.setOnClickListener(v -> {
            if (doseNumber == 1) {
                currentDose1++;
                doseNumberView.setText(String.valueOf(currentDose1));
            } else if (doseNumber == 2) {
                currentDose2++;
                doseNumberView.setText(String.valueOf(currentDose2));
            } else {
                currentDose3++;
                doseNumberView.setText(String.valueOf(currentDose3));
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
            } else if (doseNumber == 3 && currentDose3 > 1) {
                currentDose3--;
                doseNumberView.setText(String.valueOf(currentDose3));
            }
        });

        // Set the selected dose when "Set Dose" is clicked
        buttonSetDose.setOnClickListener(v -> {
            boolean isUpdate = getIntent().getBooleanExtra("isUpdate", false);
            String unitType = isUpdate ? getIntent().getStringExtra("newUnitType") :
                    getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("selectedUnitType", "");

            if (doseNumber == 1) {
                doseTextView.setText(currentDose1 + " " + unitType);
            } else if (doseNumber == 2) {
                doseTextView.setText(currentDose2 + " " + unitType);
            } else {
                doseTextView.setText(currentDose3 + " " + unitType);
            }

            // Close the dialog
            doseDialog.dismiss();
        });

        doseDialog.show();  // Display the dialog
    }

}
