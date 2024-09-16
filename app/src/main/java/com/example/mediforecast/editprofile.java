package com.example.mediforecast;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Arrays;
import java.util.Calendar;

public class editprofile extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private EditText prFname, prMname, prLname, prUsername, prEmail, prNumber;
    private Button prUpdate;
    private TextView prBirthday;
    private AutoCompleteTextView prGender, prLocation;

    String[] Location = {"Balucuc", "Calantipe", "Cansinala", "Capalangan", "Colgante", "Paligui", "Sampaloc", "San Juan", "San Vincete", "Sucad", "Sulipan", "Tabuyuc"};
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
        prUsername = findViewById(R.id.prUsername);
        prEmail = findViewById(R.id.prEmail);
        prGender = findViewById(R.id.prGender);
        prBirthday = findViewById(R.id.prBirthday);
        prLocation = findViewById(R.id.prLocation);
        prNumber = findViewById(R.id.prNumber);
        prUpdate = findViewById(R.id.prUpdate);

        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Location);
        prLocation.setAdapter(locationAdapter);
        prLocation.setThreshold(1);
        Log.d("LocationArray", Arrays.toString(Location));
        Log.d("GenderArray", Arrays.toString(Gender));
        // Initialize ArrayAdapter for Gender
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Gender);
        prGender.setAdapter(genderAdapter);

        prGender.setThreshold(1);


        prGender.setOnClickListener(v -> {
            prGender.showDropDown();
        });



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

        if (currentUser != null) {
            firestore.collection("MobileUsers").document(currentUser.getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Toast.makeText(editprofile.this, "Failed to load profile data.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (value != null && value.exists()) {
                                // Fetch the user data from the document snapshot
                                String firstName = value.getString("fname");
                                String middleName = value.getString("mname");
                                String lastName = value.getString("lname");
                                String username = value.getString("username");
                                String email = value.getString("email");
                                String gender = value.getString("gender");
                                String birthday = value.getString("birthday");
                                String location = value.getString("location");
                                String number = value.getString("number");

                                // Set the data to the EditText and AutoCompleteTextView fields
                                prFname.setText(firstName);
                                prMname.setText(middleName);
                                prLname.setText(lastName);
                                prUsername.setText(username);
                                prEmail.setText(email);
                                prGender.setText(gender);
                                prBirthday.setText(birthday);
                                prLocation.setText(location);
                                prNumber.setText(number);

                                Log.d("FetchedGender", gender);
                                Log.d("FetchedLocation", location);
                            } else {
                                Toast.makeText(editprofile.this, "No profile data found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "User is not logged in.", Toast.LENGTH_SHORT).show();
        }



        // Set up OnClickListener for Update button
        prUpdate.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {
        if (currentUser != null) {
            // Collect data from fields
            String firstName = prFname.getText().toString();
            String middleName = prMname.getText().toString();
            String lastName = prLname.getText().toString();
            String username = prUsername.getText().toString();
            String email = prEmail.getText().toString();
            String gender = prGender.getText().toString();
            String birthday = prBirthday.getText().toString();
            String location = prLocation.getText().toString();
            String number = prNumber.getText().toString();

            // Update Firestore document
            firestore.collection("MobileUsers").document(currentUser.getUid())
                    .update("fname", firstName,
                            "mname", middleName,
                            "lname", lastName,
                            "username", username,
                            "email", email,
                            "gender", gender,
                            "birthday", birthday,
                            "location", location,
                            "number", number)
                    .addOnSuccessListener(aVoid -> Toast.makeText(editprofile.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(editprofile.this, "Failed to update profile.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User is not logged in.", Toast.LENGTH_SHORT).show();
        }
    }
}
