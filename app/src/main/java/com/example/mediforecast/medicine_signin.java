package com.example.mediforecast;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class medicine_signin extends AppCompatActivity {
    private AutoCompleteTextView reminderMedicineType, reminderUnitType;
    private EditText medicinemameET;
    private TextView medicinestartdate, medicineenddate;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    String[] unitType = {"IU", "ampoule(s)","application(s)","application(s)", "capsule(s)","drop(s)","gram(s)",
            "injection(s)", "milligram(s)", "milliliter(s)", "mm", "packet(s)", "packet(s)", "patch(es)",
            "pessary(ies)", "piece(s)","pill(s)", "portion(s)", "puff(s)", "spray(s)","suppository(ies)",
            "tablespoon(s)", "teaspoon(s)", "unit(s)", "vaginal capsule(s)", "vaginal insert(s)",
            "vaginal tablet(s)", "µg"};
    String[] medicineType = {"Tablet", "Capsule", "Syrup", "Injection", "Inhaler", "Drop", "Suppositories", "Tropical Medicines"};
    ArrayAdapter<String> medicineTypeAdapter;
    ArrayAdapter<String> reminderUnitTypeAdapter;
    private Button buttonAdd;
    private TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday, schedule;
    private CheckBox everyDayCheckBox;
    // List to store selected days
    private ArrayList<String> selectedDays;
    private ImageView medicinearrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine_signin);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        medicinestartdate = findViewById(R.id.medicinestartdate);
        buttonAdd = findViewById(R.id.buttonadd);
        reminderMedicineType = findViewById(R.id.reminderMedicineType);
        medicinemameET = findViewById(R.id.medicinename);
        medicineenddate = findViewById(R.id.medicineenddate);
        schedule = findViewById(R.id.schedule);
        medicinearrow = findViewById(R.id.medicinearrow);
        reminderUnitType = findViewById(R.id.unit);
        // Retrieve values from Intent

        Intent intents = getIntent();
        String onceDaily = intents.getStringExtra("onceDaily");
        schedule.setText(onceDaily);


        reminderUnitTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item, unitType);
        reminderUnitType.setAdapter(reminderUnitTypeAdapter);
        reminderUnitType.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedMedicineUnitType = adapterView.getItemAtPosition(position).toString();
            Toast.makeText(medicine_signin.this, "Medicine Unit Type: " + selectedMedicineUnitType, Toast.LENGTH_SHORT).show();
            getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("SelectedSchedule", selectedMedicineUnitType)
                    .apply();
        });

        medicineTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item, medicineType);
        reminderMedicineType.setAdapter(medicineTypeAdapter);
        reminderMedicineType.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedMedicineType = adapterView.getItemAtPosition(position).toString();
            Toast.makeText(medicine_signin.this, "Medicine Type: " + selectedMedicineType, Toast.LENGTH_SHORT).show();
        });
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        medicinearrow.setOnClickListener(v->{
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear(); // This will erase all saved data
            editor.apply();

            ClearField();
            // Confirm clearing
            Toast.makeText(this, "Data cleared", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(medicine_signin.this, Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "SIGNIN");
            startActivity(intent);
            finish();
        });
        schedule.setOnClickListener(v-> {
            Intent intent = new Intent(medicine_signin.this, medicineschedule.class);
            startActivity(intent);
            finish();
        });
        buttonAdd.setOnClickListener(v -> addingReminder());
        medicinestartdate.setOnClickListener(v -> {
            showDatePickerDialog();
        });
        medicineenddate.setOnClickListener(v->{
            showDatePickerDialogEnd();
        });
        everyDayCheckBox = findViewById(R.id.every_day);

        // Day TextViews
        sunday = findViewById(R.id.dv_sunday);
        monday = findViewById(R.id.dv_monday);
        tuesday = findViewById(R.id.dv_tuesday);
        wednesday = findViewById(R.id.dv_wednesday);
        thursday = findViewById(R.id.dv_thursday);
        friday = findViewById(R.id.dv_friday);
        saturday = findViewById(R.id.dv_saturday);

        // Initialize selectedDays list
        selectedDays = new ArrayList<>();

        // Add listeners to each day
        setupDayClickListeners();

        // Handling the "Every day" checkbox
        everyDayCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Select all days if "Every day" is checked
                selectAllDays();
            } else {
                // Deselect all days if "Every day" is unchecked
                deselectAllDays();
            }
        });
    }
    // Method to handle day selection click listeners
    private void setupDayClickListeners() {
        sunday.setOnClickListener(v -> toggleDaySelection(sunday, "Sunday"));
        monday.setOnClickListener(v -> toggleDaySelection(monday, "Monday"));
        tuesday.setOnClickListener(v -> toggleDaySelection(tuesday, "Tuesday"));
        wednesday.setOnClickListener(v -> toggleDaySelection(wednesday, "Wednesday"));
        thursday.setOnClickListener(v -> toggleDaySelection(thursday, "Thursday"));
        friday.setOnClickListener(v -> toggleDaySelection(friday, "Friday"));
        saturday.setOnClickListener(v -> toggleDaySelection(saturday, "Saturday"));
    }

    // Method to toggle day selection
    private void toggleDaySelection(TextView dayView, String dayName) {
        if (selectedDays.contains(dayName)) {
            // Deselect the day
            selectedDays.remove(dayName);
            dayView.setBackgroundColor(Color.TRANSPARENT); // Or set to original background
            dayView.setTextColor(getResources().getColor(R.color.primaryColor)); // Reset text color
        } else {
            // Select the day
            selectedDays.add(dayName);
            dayView.setBackgroundResource(R.drawable.rounded_selected_day);
            dayView.setTextColor(Color.WHITE); // Set text color to indicate selection
        }
    }

    // Method to select all days (when "Every day" checkbox is checked)
    private void selectAllDays() {
        selectedDays.clear();
        selectedDays.add("Sunday");
        selectedDays.add("Monday");
        selectedDays.add("Tuesday");
        selectedDays.add("Wednesday");
        selectedDays.add("Thursday");
        selectedDays.add("Friday");
        selectedDays.add("Saturday");

        // Set all TextViews as selected
        toggleDayViewState(sunday, true);
        toggleDayViewState(monday, true);
        toggleDayViewState(tuesday, true);
        toggleDayViewState(wednesday, true);
        toggleDayViewState(thursday, true);
        toggleDayViewState(friday, true);
        toggleDayViewState(saturday, true);
    }

    // Method to deselect all days (when "Every day" checkbox is unchecked)
    private void deselectAllDays() {
        selectedDays.clear();

        // Set all TextViews as unselected
        toggleDayViewState(sunday, false);
        toggleDayViewState(monday, false);
        toggleDayViewState(tuesday, false);
        toggleDayViewState(wednesday, false);
        toggleDayViewState(thursday, false);
        toggleDayViewState(friday, false);
        toggleDayViewState(saturday, false);
    }

    // Helper method to set the day view selected/unselected state
    private void toggleDayViewState(TextView dayView, boolean isSelected) {
        if (isSelected) {
            dayView.setBackgroundResource(R.drawable.rounded_selected_day);
            dayView.setTextColor(Color.WHITE);
        } else {
            dayView.setBackgroundColor(Color.TRANSPARENT);
            dayView.setTextColor(getResources().getColor(R.color.primaryColor));
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("medicineName", medicinemameET.getText().toString().trim());
        editor.putString("medicineType", reminderMedicineType.getText().toString().trim());
        editor.putString("medicineStartDate", medicinestartdate.getText().toString().trim());
        editor.putString("medicineEndDate", medicineenddate.getText().toString().trim());
        editor.putString("selectedUnitType", reminderUnitType.getText().toString().trim());
        editor.putBoolean("isEveryDay", everyDayCheckBox.isChecked());
        editor.putStringSet("selectedDays", new HashSet<>(selectedDays));
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        medicinemameET.setText(preferences.getString("medicineName", ""));
        reminderMedicineType.setText(preferences.getString("medicineType", ""));
        medicinestartdate.setText(preferences.getString("medicineStartDate", ""));
        medicineenddate.setText(preferences.getString("medicineEndDate", ""));
        reminderUnitType.setText(preferences.getString("selectedUnitType", ""));
        everyDayCheckBox.setChecked(preferences.getBoolean("isEveryDay", false));
        selectedDays.clear();
        selectedDays.addAll(preferences.getStringSet("selectedDays", new HashSet<>()));
        updateDaySelectionUI(); // Method to update the UI based on selectedDays
        }

    private void updateDaySelectionUI() {
        // Update the UI for the selected days based on the selectedDays list
        for (String day : selectedDays) {
            switch (day) {
                case "Sunday":
                    toggleDayViewState(sunday, true);
                    break;
                // Repeat for other days...
            }
        }
    }

    private void addingReminder() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        LocalDateTime dateTime = LocalDateTime.now();

        if (currentUser != null) {
            String email = currentUser.getEmail();

            Intent intents = getIntent();
            String time = intents.getStringExtra("time");
            String dose = intents.getStringExtra("dose");
            String onceDaily = intents.getStringExtra("onceDaily");

        String medicineName = medicinemameET.getText().toString().trim();
        String medicineType = reminderMedicineType.getText().toString().trim();
        String dateAndTime = dateTime.toString().trim();
        String medicineStartDate = medicinestartdate.getText().toString().trim();
        String medicineEndDate = medicineenddate.getText().toString().trim();
        boolean isEveryDay = everyDayCheckBox.isChecked();
        // Validation logic
            if (medicineName.isEmpty()  || selectedDays.isEmpty()) {
                Toast.makeText(this, "Please fill all fields and select at least one day", Toast.LENGTH_SHORT).show();
                return;
            }

        Map<String, Object> reminder = new HashMap<>();
        reminder.put("email", email);
        reminder.put("medicineName", medicineName);
        reminder.put("medicineType", medicineType);
        reminder.put("created_by", dateAndTime);
        reminder.put("startDate", medicineStartDate);
        reminder.put("endDate", medicineEndDate);
        reminder.put("isEveryDay", isEveryDay);
        reminder.put("selectedDays", selectedDays);
        reminder.put("alarmTime", time);
        reminder.put("medicineDosage", dose);
        reminder.put("schedule", onceDaily);
        reminder.put("status", false);

        firestore.collection("MedicineReminder")
                .add(reminder)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(medicine_signin.this, "Reminder Added Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(medicine_signin.this, splashscreenaddalarm.class);
                        startActivity(intent);
                        finish();
                        ClearField();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(medicine_signin.this, "Error adding", Toast.LENGTH_SHORT).show();
                    }
                });
    }else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

    }
    private void showDatePickerDialog() {
        // Get the current date as default
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                medicine_signin.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date and display it in the TextView
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    medicinestartdate.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }
    private void showDatePickerDialogEnd() {
        // Get the current date as default
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                medicine_signin.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date and display it in the TextView
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    medicineenddate.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void ClearField(){
        medicinestartdate.setText("");
        reminderMedicineType.setText("");
        medicinemameET.setText("");
        medicineenddate.setText("");
        schedule.setText("");
        reminderUnitType.setText("");
    }

}