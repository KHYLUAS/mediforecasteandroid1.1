package com.example.mediforecast;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediforecast.databinding.ActivityMainBinding;

public class everyhour extends AppCompatActivity {
    private ImageView medicinearrow;
    private TextView timeValue; // Initialize timeValue
    private TextView doseValue;
    private ActivityMainBinding binding;
    private int currentDose = 1;
    private Button Register;
    private AutoCompleteTextView hoursACTV;
    String[] hoursA = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    ArrayAdapter<String> hoursAAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_everyhour);

        // Initialize views
        timeValue = findViewById(R.id.timeValue); // Bind the TextView
        doseValue = findViewById(R.id.doseValue);
        Register = findViewById(R.id.Register);
        medicinearrow = findViewById(R.id.medicinearrow);
        hoursACTV = findViewById(R.id.hoursACTV);

        hoursAAdapter = new ArrayAdapter<>(this, R.layout.list_item, hoursA);
        hoursACTV.setAdapter(hoursAAdapter);


        // Handle item click
        hoursACTV.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedHours = adapterView.getItemAtPosition(position).toString();
            Toast.makeText(everyhour.this, "Selected Hour: " + selectedHours, Toast.LENGTH_SHORT).show();
            // Set the selected hour value in timeValue TextView
        });
        hoursACTV.setOnClickListener(v -> {
            hoursACTV.showDropDown(); // Show the dropdown menu
        });

        // Load unit type
        String unitType = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getString("selectedUnitType", ""); // Default dose value
        Log.e("This is the unitType", unitType);
        if(unitType.isEmpty()){
            doseValue.setText("Unit");
        } else {
            doseValue.setText(unitType);
        }

        // Handle the back arrow click
        medicinearrow.setOnClickListener(v -> {
            Intent intent = new Intent(everyhour.this, medicineschedule.class);
            startActivity(intent);
            finish();
        });

        // Handle dose value click to show modal
        doseValue.setOnClickListener(v -> showDosePicker());

        // Handle register button click
        Register.setOnClickListener(v -> {
            String time = timeValue.getText().toString();
            String dose = doseValue.getText().toString();
            String onceDaily = "Once Daily";

            // Navigate to another activity or show a confirmation message
            Intent intent = new Intent(everyhour.this, medicine_signin.class);
            intent.putExtra("time", time);
            intent.putExtra("dose", dose);
            intent.putExtra("onceDaily", onceDaily);
            startActivity(intent);
            finish();
        });
    }



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
                    .getString("selectedUnitType", ""); // Default dose value

            doseValue.setText(String.valueOf(currentDose) + " " + unitType);  // Update the doseValue TextView
            doseDialog.dismiss();  // Close the dialog
        });

        doseDialog.show();  // Display the dialog
    }
}
