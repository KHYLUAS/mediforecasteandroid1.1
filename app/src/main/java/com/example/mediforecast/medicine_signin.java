package com.example.mediforecast;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class medicine_signin extends AppCompatActivity {
    private AutoCompleteTextView reminderMedicineType;
    private EditText medicinemameET, medicinedosageET;
    private TextView medicinestartdate;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    String[] medicineType = {"Tablet", "Capsule", "Syrup", "Injection"};
    ArrayAdapter<String> medicineTypeAdapter;
    private Button buttonAdd;


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

        Map<String, Object> reminder = new HashMap<>();
        reminder.put("email", email);
        reminder.put("medicineName", medicineName);
        reminder.put("medicineDosage", medicineDosage);
        reminder.put("medicineType", medicineType);
        reminder.put("created_by", dateAndTime);
        reminder.put("startDate", medicineStartDate);

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


}