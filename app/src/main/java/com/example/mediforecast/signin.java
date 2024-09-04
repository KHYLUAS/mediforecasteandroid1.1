package com.example.mediforecast;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class signin extends AppCompatActivity {

    //    private ImageView img1;
    private EditText signupFname, signupMname, signupLname, signupUsername, signupEmail, signupNumber, signupPassword, signupRPassword;
    private AutoCompleteTextView signupGender, signupLocation;
    private Button Register;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private TextView txtview7, signLogin;
    private loading1 loading1;
    private ImageView  passwordeyes,passwordeyee;
    private boolean forgetPassVisible,forgetVisible = false;
    private CheckBox checkBox;
    private MaterialAlertDialogBuilder materialAlertDialogBuilder;


    String[] Location = {"Balucuc", "Calantipe", "Cansinala", "Capalangan","Colgante", "Paligui","Sampaloc","San Juan","San Vincete","Sucad","Sulipan","Tabuyuc"};
    String[] Gender = {"Male", "Female"};

    ArrayAdapter<String> locationAdapter;
    ArrayAdapter<String> genderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        // Initialize UI elements
//        img1 = findViewById(R.id.img1);
        signLogin = findViewById(R.id.signLogin);
        signupFname = findViewById(R.id.signupFname);
        signupMname = findViewById(R.id.signupMname);
        signupLname = findViewById(R.id.signupLname);
        signupUsername = findViewById(R.id.signupUsername);
        signupEmail = findViewById(R.id.signupEmail);
        txtview7 = findViewById(R.id.txtview7);
        signupNumber = findViewById(R.id.signupNumber);
        signupPassword = findViewById(R.id.signupPassword);
        signupRPassword = findViewById(R.id.signupRPassword);
        passwordeyes = findViewById(R.id.passwordeyes);
        passwordeyee = findViewById(R.id.passwordeyee);
        signupGender = findViewById(R.id.signupGender);
        signupLocation = findViewById(R.id.signupLocation);
        Register = findViewById(R.id.Register);
        loading1 = new loading1(this);
        checkBox = findViewById(R.id.signcheckbox);


        ///terms and conditon
        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        Register.setEnabled(false);

        ///error hide the action barr fix
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //////error hide the action barr fix


        //checkbox agreement
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // Create a SpannableString with the title text
                    SpannableString title = new SpannableString("Terms & Conditions");

                    // Apply bold style to the entire title
                    title.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Set the bold title in the MaterialAlertDialogBuilder
                    materialAlertDialogBuilder.setTitle(title);

                    materialAlertDialogBuilder.setMessage("Welcome to MediForecast. By using our app, you agree to be bound by the following Terms and Conditions. If you do not agree with any part of these Terms, you must not use our app.");

                    materialAlertDialogBuilder.setMessage("1. Eligibility\n" +
                            "You must be at least 18 years old and must be a resident of Apalit or associated with the Apalit Rural Health Unit to use MediForecast.\n" +
                            "\n" +
                            "2. Account Registration\n" +
                            "User Responsibility: Users must provide accurate and truthful information when registering and using the system.\n" +
                            "Profile Picture: Users can upload a profile picture. Any inappropriate or offensive content will result in account suspension or termination.\n" +
                            "Prohibited Activities: Define prohibited activities, such as unauthorized access, data tampering, misuse of the system for non-health-related activities, or violating privacy rights.\n" +
                            "Security: You are responsible for maintaining the confidentiality of your account credentials and are responsible for all activities under your account.\n" +
                            "\n" +
                            "3. Data Collection and Usage\n" +
                            "Collection of Information: We collect personal information during the registration process and through your use of the app, as outlined in our Privacy Policy.\n" +
                            "Use of Information: Your information is used to provide, maintain, and improve our services. We do not share your personal information with third parties without your consent, except as required by law.\n" +
                            "\n" +
                            "4. Use of the App\n" +
                            "Purpose: MediForecast is designed to provide medical forecasts, advice, and other health-related information. However, it does not replace professional medical advice, diagnosis, or treatment.\n" +
                            "Compliance: You agree to use the app in compliance with all applicable laws and regulations.\n" +
                            "Prohibited Activities: You must not misuse the app, including but not limited to:\n" +
                            "Attempting to hack or disrupt the app’s functionality.\n" +
                            "Uploading viruses or other harmful code.\n" +
                            "Using the app for any illegal activities.\n" +
                            "\n" +
                            "6. Intellectual Property\n" +
                            "Ownership: MediForecast and its content, features, and functionality are the exclusive property of the Rural Health Unit of Apalit and its Brgy. Health Centers. You may not copy, modify, distribute, or create derivative works based on our content without prior written consent.\n" +
                            "Trademarks: All trademarks, service marks, and logos are the property of their respective owners.\n" +
                            "\n" +
                            "7. Limitation of Liability\n" +
                            "No Warranty: MediForecast is provided \"as is\" and \"as available\" without any warranties, express or implied. We do not guarantee that the app will be available at all times or free from errors.\n" +
                            "Limitation: To the fullest extent permitted by law, we shall not be liable for any direct, indirect, incidental, or consequential damages arising from your use of the app.\n" +
                            "\n" +
                            "8. Governing Law\n" +
                            "These Terms are governed by and construed in accordance with the laws of [Your Jurisdiction], without regard to its conflict of laws principles.\n" +
                            "\n" +
                            "9. Contact Us\n" +
                            "If you have any questions about these Terms, please contact us at [Your Contact Information].");

                    materialAlertDialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Register.setEnabled(true);
                            dialogInterface.dismiss();
                        }
                    });
                    materialAlertDialogBuilder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Register.setEnabled(false);
                            checkBox.setChecked(false);
                            dialogInterface.dismiss();
                        }
                    });
                    materialAlertDialogBuilder.show();
                } else {
                    Register.setEnabled(false);
                }
            }
        });


        // Password eye toggle
        passwordeyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleForgetPassVisibility();
            }
        });

        passwordeyee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleForgetVisibility();
            }
        });
        // Set up the ImageView click listener for navigation
        signLogin.setOnClickListener(v -> {
            Intent intent = new Intent(signin.this, login_form.class);
            startActivity(intent);
            finish();
        });


        // Setup for Location dropdown
        locationAdapter = new ArrayAdapter<>(this, R.layout.list_item, Location);
        signupLocation.setAdapter(locationAdapter);
        signupLocation.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedLocation = adapterView.getItemAtPosition(position).toString();
            Toast.makeText(signin.this, "Barangay: " + selectedLocation, Toast.LENGTH_SHORT).show();
        });

        // Setup for Gender dropdown
        genderAdapter = new ArrayAdapter<>(this, R.layout.list_item, Gender);
        signupGender.setAdapter(genderAdapter);
        signupGender.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedGender = adapterView.getItemAtPosition(position).toString();
            Toast.makeText(signin.this, "Gender: " + selectedGender, Toast.LENGTH_SHORT).show();
        });

        // Initialize the birthday field with a DatePicker
        setupBirthdayPicker();

        // Set up the register button click listener
        Register.setOnClickListener(v -> {
            // Show loading dialog
            loading1.show();

            // Perform input validation
            if (validateInput()) {
                String email = signupEmail.getText().toString().trim();
                String username = signupUsername.getText().toString().trim();


                // Get the email from the input field

                checkUsernameAndEmailExists(username, email);
                // Start the OTPVerification activity


                // Dismiss the loading dialog
                loading1.cancel();
            } else {
                // Dismiss the loading dialog if validation fails
                loading1.cancel();

            }
        });



    }

    private boolean validateInput() {
        boolean isValid = true;

        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String fname = signupFname.getText().toString().trim();
        String mname = signupMname.getText().toString().trim();
        String lname = signupLname.getText().toString().trim();
        String username = signupUsername.getText().toString().trim();
        String gender = signupGender.getText().toString().trim();
        String number = signupNumber.getText().toString().trim();
        String location = signupLocation.getText().toString().trim();
        String rpassword = signupRPassword.getText().toString().trim();
        String birthday = txtview7.getText().toString().trim();

        // Validation for empty field
        if (email.isEmpty() && password.isEmpty() && fname.isEmpty() && mname.isEmpty() &&
                lname.isEmpty() && username.isEmpty() && gender.isEmpty() && number.isEmpty() &&
                location.isEmpty() && rpassword.isEmpty() && birthday.isEmpty()) {

            Toast.makeText(this, "All fields are empty, please fill up the form", Toast.LENGTH_LONG).show();
            isValid = false;
        }

        // Validation for each field
        if (fname.isEmpty()) {
            signupFname.setError("Field cannot be empty");
            signupFname.requestFocus();
            isValid = false;
        } else if (!fname.matches("[a-zA-Z]+(?: [a-zA-Z]+)*")) {
            signupFname.setError("Enter only Alphabetical Characters");
            signupFname.requestFocus();
            isValid = false;
        }

        else if (mname.isEmpty()) {
            signupMname.setError("Field cannot be empty");
            signupMname.requestFocus();
            isValid = false;
        }
        else if (!mname.matches("[a-zA-Z]+")) {
            signupMname.setError("Enter only Alphabetical Character");
            signupMname.requestFocus();
            isValid = false;
        }
        else if (lname.isEmpty()) {
            signupLname.setError("Field cannot be empty");
            signupLname.requestFocus();
            isValid = false;
        }
        else if (!lname.matches("[a-zA-Z]+")) {
            signupLname.setError("Enter only Alphabetical Character");
            signupLname.requestFocus();
            isValid = false;
        }
        else if (username.isEmpty()) {
            signupUsername.setError("Field cannot be empty");
            signupUsername.requestFocus();
            isValid = false;
        }
        else if (username.length() < 4) {
            signupUsername.setError("Username must be at least 3 characters");
            signupUsername.requestFocus();
            isValid = false;
        }
        else if (email.isEmpty()) {
            signupEmail.setError("Field cannot be empty");
            signupEmail.requestFocus();
            isValid = false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("Enter a valid email");
            signupEmail.requestFocus();
            isValid = false;
        }
        else if (!email.endsWith("@gmail.com")) { // New validation for @gmail.com
            signupEmail.setError("Email must be a @gmail.com address");
            signupEmail.requestFocus();
            isValid = false;

        }
        else if (gender.isEmpty()) {
            signupGender.setError("Field cannot be empty");
            signupGender.requestFocus();
            isValid = false;
        }
        //features date
        else if (birthday.isEmpty()) {
            txtview7.setError("Field cannot be empty");
            txtview7.requestFocus();

            isValid = false;
        }
        else if (location.isEmpty()) {
            signupLocation.setError("Field cannot be empty");
            signupLocation.requestFocus();
            isValid = false;
        }
        else if (number.isEmpty()) {
            signupNumber.setError("Field cannot be empty");
            signupNumber.requestFocus();
            isValid = false;
        }
        else if (!number.matches("^09[0-9]{9}$")) {
            signupNumber.setError("Number must be a 09123456789");
            signupNumber.requestFocus();
            isValid = false;
        }
        else if (password.isEmpty()) {
            signupPassword.setError("Field cannot be empty");
            signupPassword.requestFocus();
            isValid = false;
        }
        else if (password.length() < 7) {
            signupPassword.setError("Minimum 7 characters");
            signupPassword.requestFocus();
            isValid = false;
        }
        else if (!password.matches(".*[A-Z].*")) { // Check for at least one uppercase letter
            signupPassword.setError("Must contain at least one uppercase letter and one special symbol");
            signupPassword.requestFocus();
            isValid = false;
        }
//        else if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) { // Check for at least one special symbol
//            signupPassword.setError("Must contain at least one uppercase letter and one special symbol");
//            signupPassword.requestFocus();
//            isValid = false;
//        }
        else if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~`].*")) {
            signupPassword.setError("Must contain at least one uppercase letter and one special symbol");
            signupPassword.requestFocus();
            isValid = false;
        }

        else if (rpassword.isEmpty()) {
            signupRPassword.setError("Field cannot be empty");
            signupRPassword.requestFocus();
            isValid = false;

        }
        else if (!rpassword.equals(password)) { // Check if rpassword matches password
            signupRPassword.setError("Passwords do not match");
            signupRPassword.requestFocus();
            isValid = false;
        }


        return isValid;
    }


    ////username exist or not

    private void checkUsernameAndEmailExists(String username, String email) {
        loading1.show();

        firestore.collection("MobileUsers")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        loading1.cancel();
                        signupUsername.setError("Username already exists");
                        signupUsername.requestFocus();
                    } else {
                        firestore.collection("MobileUsers")
                                .whereEqualTo("email", email)
                                .get()
                                .addOnCompleteListener(emailTask -> {
                                    if (emailTask.isSuccessful() && !emailTask.getResult().isEmpty()) {
                                        loading1.cancel();
                                        signupEmail.setError("Email already exists");
                                        signupEmail.requestFocus();
                                    }else {
                                        registerUser();
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    loading1.cancel();
                    Toast.makeText(signin.this, "Error checking username/email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    ///////eye off and onn
    private void toggleForgetPassVisibility() {
        if (forgetPassVisible) {
            // Hide password
            signupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordeyes.setImageResource(R.drawable.removeeye); // Set eye-off icon
        } else {
            // Show password
            signupPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordeyes.setImageResource(R.drawable.removeeye); // Set eye-on icon
        }

        // Toggle the boolean flag
        forgetPassVisible = !forgetPassVisible;

        // Move the cursor to the end of the text in forgetpass field
        signupPassword.setSelection(signupPassword.getText().length());
    }

    // Method to toggle password visibility for forget field
    private void toggleForgetVisibility() {
        if (forgetVisible) {
            // Hide password
            signupRPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordeyee.setImageResource(R.drawable.removeeye); // Set eye-off icon
        } else {
            // Show password
            signupRPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordeyee.setImageResource(R.drawable.removeeye); // Set eye-on icon
        }

        // Toggle the boolean flag
        forgetVisible = !forgetVisible;

        // Move the cursor to the end of the text in forget field
        signupRPassword.setSelection(signupRPassword.getText().length());
    }


    ////birthday
    private void setupBirthdayPicker() {
        txtview7.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        // Get the current date as default
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                signin.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date and display it in the TextView
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    txtview7.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }
    private void registerUser() {
        // Add your user registration logic here, using the validated input
        // For example, create a new user document in Firestore

        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String fname = signupFname.getText().toString().trim();
        String mname = signupMname.getText().toString().trim();
        String lname = signupLname.getText().toString().trim();
        String username = signupUsername.getText().toString().trim();
        String gender = signupGender.getText().toString().trim();
        String number = signupNumber.getText().toString().trim();
        String location = signupLocation.getText().toString().trim();
        String birthday = txtview7.getText().toString().trim();



        // Create an intent and put the email as an extra
        Intent intent = new Intent(signin.this, OTPVerification.class);
        intent.putExtra("email", email);
        intent.putExtra("fname", fname);
        intent.putExtra("mname", mname);
        intent.putExtra("lname", lname);
        intent.putExtra("username", username);
        intent.putExtra("gender", gender);
        intent.putExtra("number", number);
        intent.putExtra("location", location);
        intent.putExtra("birthday", birthday);
        intent.putExtra("password", password);
        Toast.makeText(signin.this, "Registration successful", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        loading1.cancel();
    }

//    private void registerUser() {
//        String email = signupEmail.getText().toString().trim();
//        String password = signupPassword.getText().toString().trim();
//
//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // User registered successfully
//                FirebaseUser user = mAuth.getCurrentUser();
//                if (user != null) {
//                    saveUserDataToFirestore(user.getUid());
//                }
//            } else {
//                // Registration failed
//                Toast.makeText(signin.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private void saveUserDataToFirestore(String userId) {
//        String fname = signupFname.getText().toString().trim();
//        String mname = signupMname.getText().toString().trim();
//        String lname = signupLname.getText().toString().trim();
//        String username = signupUsername.getText().toString().trim();
//        String gender = signupGender.getText().toString().trim();
//        String number = signupNumber.getText().toString().trim();
//        String location = signupLocation.getText().toString().trim();
//        String birthday = txtview7.getText().toString().trim();
////        String email = signupEmail.getText().toString().trim();
//
//        // Create a map to store user data
//        Map<String, Object> userData = new HashMap<>();
//        userData.put("fname", fname);
//        userData.put("mname", mname);
//        userData.put("lname", lname);
//        userData.put("username", username);
//        userData.put("gender", gender);
//        userData.put("number", number);
//        userData.put("location", location);
//        userData.put("birthday", birthday);
////        userData.put("email", email);
//
//
//        // Save the data to Firestore
//        firestore.collection("users").document(userId).set(userData).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // Data saved successfully
//                Toast.makeText(signin.this, "Registration successful!", Toast.LENGTH_SHORT).show();
//                // Optionally navigate to another activity
//                startActivity(new Intent(signin.this, login_form.class));
//                finish();
//            } else {
//                // Data saving failed
//                Toast.makeText(signin.this, "Failed to save user data: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}
