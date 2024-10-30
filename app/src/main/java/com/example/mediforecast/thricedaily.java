package com.example.mediforecast;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private int currentDose = 1;
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
            Intent intent = new Intent(thricedaily.this, medicineschedule.class);
            startActivity(intent);
            finish();
        });
        timeValue.setOnClickListener(v -> showTimePicker(timeValue));
        timeValue2.setOnClickListener(v -> showTimePicker(timeValue2));
        timeValue3.setOnClickListener(v -> showTimePicker(timeValue3));
        doseValue.setOnClickListener(v -> showDosePicker(doseValue));
        doseValue2.setOnClickListener(v -> showDosePicker(doseValue2));
        doseValue3.setOnClickListener(v -> showDosePicker(doseValue3));
        String unitType = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getString("selectedUnitType", "");// Default dose value
        doseValue.setText(unitType);
        doseValue2.setText(unitType);
        doseValue3.setText(unitType);
        Register.setOnClickListener(v->{
            String time1 = timeValue.getText().toString();
            String time2 = timeValue2.getText().toString();
            String time3 = timeValue3.getText().toString();
            String dose1 = doseValue.getText().toString();
            String dose2 = doseValue2.getText().toString();
            String dose3 = doseValue3.getText().toString();
            String thriceDaily = "Thrice Daily";
            if (isUpdate){
                Intent intent = new Intent(thricedaily.this, UpdateReminder.class);
                intent.putExtra("time1", time1);
                intent.putExtra("time2", time2);
                intent.putExtra("time3", time3);
                intent.putExtra("dose1", dose1);
                intent.putExtra("dose2", dose2);
                intent.putExtra("dose3", dose3);
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
    private void showDosePicker(TextView doseTextView) {
        Dialog doseDialog = new Dialog(this);
        doseDialog.setContentView(R.layout.dialog_dose_picker);

        // Initialize dialog components
        TextView doseNumber = doseDialog.findViewById(R.id.doseNumber);
        ImageView buttonMinus = doseDialog.findViewById(R.id.buttonMinus);
        ImageView buttonPlus = doseDialog.findViewById(R.id.buttonPlus);
        Button buttonSetDose = doseDialog.findViewById(R.id.buttonSetDose);

        // Set the initial dose value
        doseNumber.setText(String.valueOf(currentDose));

        // Increment the dose
        buttonPlus.setOnClickListener(v -> {
            currentDose++;
            doseNumber.setText(String.valueOf(currentDose));
        });

        // Decrement the dose, but ensure it doesn't go below 1
        buttonMinus.setOnClickListener(v -> {
            if (currentDose > 1) {
                currentDose--;
                doseNumber.setText(String.valueOf(currentDose));
            }
        });

        // Set the selected dose when "Set Dose" is clicked
        buttonSetDose.setOnClickListener(v -> {
            String unitType = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    .getString("selectedUnitType", "");// Default dose value

            doseTextView.setText(String.valueOf(currentDose) + " " + unitType);  // Update the doseValue TextView
            doseDialog.dismiss();  // Close the dialog
        });

        doseDialog.show();  // Display the dialog
    }
    }
