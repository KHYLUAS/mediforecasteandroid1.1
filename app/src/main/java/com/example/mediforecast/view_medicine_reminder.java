package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

// view_medicine_reminder.java
public class view_medicine_reminder extends AppCompatActivity {

    private EditText medicineNameEditText;
    private AutoCompleteTextView unitAutoComplete;
    private AutoCompleteTextView medicineTypeAutoComplete;
    private TextView scheduleTextView,startDateTextView,endDateTextView,dv_sunday,dv_monday,dv_tuesday,dv_wednesday,dv_thursday,dv_friday,dv_saturday;
    private Button viewbutton;
    private AppCompatActivity every_day;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicine_reminder);

        // Initialize UI elements
        viewbutton = findViewById(R.id.buttonadd);
        medicineNameEditText = findViewById(R.id.medicinename);
        unitAutoComplete = findViewById(R.id.unit);
        medicineTypeAutoComplete = findViewById(R.id.reminderMedicineType);
        scheduleTextView = findViewById(R.id.schedule);
        startDateTextView = findViewById(R.id.medicinestartdate);
        endDateTextView = findViewById(R.id.medicineenddate);

        // Get the medicine name from the intent
        String medicineName = getIntent().getStringExtra("medicineName");

        // Fetch medicine details from Firestore
        if (medicineName != null) {
            fetchMedicineDetails(medicineName);
        }

        viewbutton.setOnClickListener(v -> {
            Intent intent = new Intent(view_medicine_reminder.this, Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "VIEW");
            startActivity(intent);
            finish();
        });
    }

    private void fetchMedicineDetails(String medicineName) {
        FirebaseFirestore.getInstance().collection("MedicineReminder")
                .whereEqualTo("medicineName", medicineName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Fetch the fields you need from Firestore
                            String details = document.getString("details"); // Example field
                            String dosageUnit = document.getString("dosageValue"); // Example field
                            String medicineType = document.getString("medicineType"); // Example field
                            String schedule = document.getString("schedule"); // Example field
                            String startDate = document.getString("startDate"); // Example field
                            String endDate = document.getString("endDate"); // Example field

                            // Set the fetched data to the UI elements
                            medicineNameEditText.setText(medicineName);
                            unitAutoComplete.setText(dosageUnit);
                            medicineTypeAutoComplete.setText(medicineType);
                            scheduleTextView.setText(schedule);
                            startDateTextView.setText(startDate);
                            endDateTextView.setText(endDate);
                        }
                    } else {
                        Toast.makeText(this, "Error fetching details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
