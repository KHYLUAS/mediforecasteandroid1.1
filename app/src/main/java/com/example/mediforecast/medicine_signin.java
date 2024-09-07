package com.example.mediforecast;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class medicine_signin extends AppCompatActivity {
    private AutoCompleteTextView reminderMedicineType;
    private EditText medicinemameET, medicinedosageET;

    String[] medicineType = {"Tablet", "Capsule", "Syrup", "Injection"};
    ArrayAdapter<String> medicineTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine_signin);

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
    }
}