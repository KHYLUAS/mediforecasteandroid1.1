package com.example.mediforecast;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.util.Map;

public class medicine_signin extends AppCompatActivity {
    private AutoCompleteTextView reminderMedicineType;
    private EditText medicinemameET, medicinedosageET;
    private TextView medicinestartdate, medicineenddate;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    String[] medicineType = {"Tablet", "Capsule", "Syrup", "Injection"};
    ArrayAdapter<String> medicineTypeAdapter;
    private Button buttonAdd;
    private TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday;
    private CheckBox everyDayCheckBox;
    // List to store selected days
    private ArrayList<String> selectedDays;

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
        medicinedosageET = findViewById(R.id.medicinedosage);
        medicineenddate = findViewById(R.id.medicineenddate);

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
    private void addingReminder() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        LocalDateTime dateTime = LocalDateTime.now();

        if (currentUser != null) {
            String email = currentUser.getEmail();

        String medicineName = medicinemameET.getText().toString().trim();
        String medicineDosage = medicinedosageET.getText().toString().trim();
        String medicineType = reminderMedicineType.getText().toString().trim();
        String dateAndTime = dateTime.toString().trim();
        String medicineStartDate = medicinestartdate.getText().toString().trim();
        String medicineEndDate = medicineenddate.getText().toString().trim();
        boolean isEveryDay = everyDayCheckBox.isChecked();
        // Validation logic
            if (medicineName.isEmpty() || medicineDosage.isEmpty()  || selectedDays.isEmpty()) {
                Toast.makeText(this, "Please fill all fields and select at least one day", Toast.LENGTH_SHORT).show();
                return;
            }

        Map<String, Object> reminder = new HashMap<>();
        reminder.put("email", email);
        reminder.put("medicineName", medicineName);
        reminder.put("medicineDosage", medicineDosage);
        reminder.put("medicineType", medicineType);
        reminder.put("created_by", dateAndTime);
        reminder.put("startDate", medicineStartDate);
        reminder.put("endDate", medicineEndDate);
        reminder.put("isEveryDay", isEveryDay);
        reminder.put("selectedDays", selectedDays);

        firestore.collection("MedicineReminder")
                .add(reminder)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(medicine_signin.this, "Reminder Added Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(medicine_signin.this, splashscreenaddalarm.class);
                        startActivity(intent);
                        finish();
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


}