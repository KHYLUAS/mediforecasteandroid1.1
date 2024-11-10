package com.example.mediforecast;

import static android.app.PendingIntent.getActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;

public class editprofile extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private EditText prFname, prMname, prLname, prUsername, prEmail, prNumber;
    private Button prUpdate;
    private TextView prBirthday;
    private AutoCompleteTextView prGender, prLocation;
    private ImageView prImage;
    String[] Location = {"Balucuc, Apalit Pampanga", "Calantipe, Apalit Pampanga", "Cansinala, Apalit Pampanga", "Capalangan, Apalit Pampanga","Colgante, Apalit Pampanga", "Paligui, Apalit Pampanga","Sampaloc, Apalit Pampanga","San Juan, Apalit Pampanga","San Vincete, Apalit Pampanga","Sucad, Apalit Pampanga","Sulipan, Apalit Pampanga","Tabuyuc, Apalit Pampanga"};
    String[] Gender = {"Male", "Female"};
    ArrayAdapter<String> locationAdapter;
    ArrayAdapter<String> genderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();
        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();
        // Get current logged-in user
        currentUser = auth.getCurrentUser();

        prFname = findViewById(R.id.prFname);
        prMname = findViewById(R.id.prMname);
        prLname = findViewById(R.id.prLname);
//        prUsername = findViewById(R.id.prUsername);
        prEmail = findViewById(R.id.prEmail);
        prGender = findViewById(R.id.prGender);
        prBirthday = findViewById(R.id.prBirthday);
        prLocation = findViewById(R.id.prLocation);
        prNumber = findViewById(R.id.prNumber);
        prUpdate = findViewById(R.id.prUpdate);
        prImage = findViewById(R.id.profile_button);

        // Initialize ArrayAdapter for Gender
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Gender);
        prGender.setAdapter(genderAdapter);

        // Initialize ArrayAdapter for Location
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Location);
        prLocation.setAdapter(locationAdapter);

        prImage.setOnClickListener(v -> {
            Intent intent = new Intent(editprofile.this, Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "UPDATE");
            startActivity(intent);
            finish();
        });
        locationAdapter = new ArrayAdapter<>(this, R.layout.list_item, Location);



        prBirthday.setOnClickListener(v -> {
            // Get the current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create and show the DatePickerDialog
            new DatePickerDialog(editprofile.this, (view, year1, month1, dayOfMonth) -> {
                // Set the selected date to the TextView
                String selectedDate = String.format("%02d/%02d/%d", month1 + 1, dayOfMonth, year1);
                prBirthday.setText(selectedDate);
            }, year, month, day).show();
        });


        SharedPreferences sharedPreferences = this.getSharedPreferences("MyInfo", Context.MODE_PRIVATE);
        // Retrieve email and password
        String savedEmail = sharedPreferences.getString("EMAIL", null);  // Default is null if not found

        firestore.collection("MobileUsers").whereEqualTo("email", savedEmail)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Failed to load profile data.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (snapshot != null && !snapshot.isEmpty()) {
                        // Loop through all matched documents (in case of multiple)
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            if (document.exists()) {
                                prFname.setText(document.getString("fname"));
                                prMname.setText(document.getString("mname"));
                                prLname.setText(document.getString("lname"));
                                prEmail.setText(document.getString("email"));
                                prGender.setText(document.getString("gender"), false);
                                prBirthday.setText(document.getString("birthday"));
                                prLocation.setText(document.getString("location"), false);
                                prNumber.setText(document.getString("number"));
                            }
                        }
                    } else {
                        Toast.makeText(this, "No profile data found.", Toast.LENGTH_SHORT).show();
                    }
                });



        // Set click listeners for showing the dropdown when the AutoCompleteTextViews are clicked
        prGender.setOnClickListener(v -> prGender.showDropDown());
        prLocation.setOnClickListener(v -> prLocation.showDropDown());

        // Set up OnClickListener for Update button
        prUpdate.setOnClickListener(v -> updateProfile(savedEmail));
    }

    private void updateProfile(String email) {
        firestore.collection("MobileUsers").whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        document.getReference().update(
                                "fname", prFname.getText().toString(),
                                "mname", prMname.getText().toString(),
                                "lname", prLname.getText().toString(),
                                "email", prEmail.getText().toString(),
                                "gender", prGender.getText().toString(),
                                "birthday", prBirthday.getText().toString(),
                                "location", prLocation.getText().toString(),
                                "number", prNumber.getText().toString()
                        ).addOnSuccessListener(aVoid -> {
                            Intent intent = new Intent(editprofile.this, Menubar.class);
                            intent.putExtra("EXTRA_FRAGMENT", "EDIT");
                            startActivity(intent);
                            finish();
                            Toast.makeText(editprofile.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> Toast.makeText(editprofile.this, "Failed to update profile.", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Profile not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show());
    }
}