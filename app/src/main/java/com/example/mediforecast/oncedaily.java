package com.example.mediforecast;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class oncedaily extends AppCompatActivity {
    private ImageView medicinearrow;
    private TextView timeValue;
    private TextView doseValue;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private int currentDose = 1;
    private Button Register;
    private int doseNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_oncedaily);

        timeValue = findViewById(R.id.timeValue);
        doseValue = findViewById(R.id.doseValue);
        Register = findViewById(R.id.Register);
        medicinearrow = findViewById(R.id.medicinearrow);

        boolean isUpdate = getIntent().getBooleanExtra("isUpdate", false);

        String unitType = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getString("selectedUnitType", "");// Default dose value
        Log.e("This is the unityType", unitType);
        if(unitType.isEmpty()){
            doseValue.setText("Unit");
        }else{
            doseValue.setText(unitType);
        }


        // Handle the back arrow click
        medicinearrow.setOnClickListener(v -> {
            Intent intent = new Intent(oncedaily.this, medicineschedule.class);
            startActivity(intent);
            finish();
        });

        // Handle time picker
        timeValue.setOnClickListener(v -> showTimePicker());

        // Handle dose value click to show modal
        doseValue.setOnClickListener(v -> showDosePicker());
        Register.setOnClickListener(v -> {
            String time1 = timeValue.getText().toString();
            String dose1 = doseValue.getText().toString();
            String onceDaily = "Once Daily";
            if(isUpdate){
                Intent intent = new Intent(oncedaily.this, UpdateReminder.class);
                intent.putExtra("time1", time1);
                intent.putExtra("dose1", dose1);
                intent.putExtra("onceDaily", onceDaily);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(oncedaily.this, medicine_signin.class);
                intent.putExtra("time1", time1);
                intent.putExtra("dose1", dose1);
                intent.putExtra("onceDaily", onceDaily);
                startActivity(intent);
                finish();
            }
        });
    }

    // Time picker method
    private void showTimePicker() {
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Time")
                .build();

        timePicker.show(getSupportFragmentManager(), "MediForecastTimePicker");

        timePicker.addOnPositiveButtonClickListener(view -> {
            int selectedHour = timePicker.getHour();
            int selectedMinute = timePicker.getMinute();

            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            String formattedTime = String.format("%02d:%02d %s",
                    (selectedHour % 12 == 0 ? 12 : selectedHour % 12),
                    selectedMinute,
                    (selectedHour >= 12) ? "PM" : "AM");
            timeValue.setText(formattedTime);
        });
    }

    // Dose picker modal dialog
    private void showDosePicker() {
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

            doseValue.setText(String.valueOf(currentDose) + " " + unitType);  // Update the doseValue TextView
            doseDialog.dismiss();  // Close the dialog
        });

        doseDialog.show();  // Display the dialog
    }
}
