package com.example.mediforecast;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class signin extends AppCompatActivity {

    //    private ImageView img1;
    private EditText signupFname, signupMname, signupLname, signupEmail, signupNumber, signupPassword, signupRPassword;
    private AutoCompleteTextView signupGender, signupLocation;
    private Button Register;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private TextView txtview7, signLogin, validationfname, validationmname, validationbirthday, validationlname, validationgender, validationbarangay, validationemail, validationnumber, validationpassword, validationrpassword;
    private loading1 loading1;
    private ImageView  passwordeyes,passwordeyee,emailchecks;
    private boolean forgetPassVisible,forgetVisible = false;
    private CheckBox checkBox;
    private MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private boolean isAtleast8 = false, hasUpperCase = false, hasLowerCase = false, hasNumber = false, hasSymbol = false;
    private CardView frameOne, frameTwo, frameThree, frameFour, frameFive;

    String[] Location = {"Balucuc, Apalit Pampanga", "Calantipe, Apalit Pampanga", "Cansinala, Apalit Pampanga", "Capalangan, Apalit Pampanga","Colgante, Apalit Pampanga", "Paligui, Apalit Pampanga","Sampaloc, Apalit Pampanga","San Juan, Apalit Pampanga","San Vincete, Apalit Pampanga","Sucad, Apalit Pampanga","Sulipan, Apalit Pampanga","Tabuyuc, Apalit Pampanga"};
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
        emailchecks = findViewById(R.id.checkbox);
//        signupUsername = findViewById(R.id.signupUsername);
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
        validationfname = findViewById(R.id.validationfname);
        validationmname = findViewById(R.id.validationmname);
        validationlname = findViewById(R.id.validationlname);
        validationgender = findViewById(R.id.validationgender);
        validationbarangay = findViewById(R.id.validationbarangay);
        validationemail = findViewById(R.id.validationemail);
        validationnumber = findViewById(R.id.validationnumber);
        validationpassword = findViewById(R.id.validationpassword);
        validationrpassword = findViewById(R.id.validationrpassword);
        validationbirthday = findViewById(R.id.validationbirthday);
        frameOne = findViewById(R.id.frameOne);
        frameTwo = findViewById(R.id.frameTwo);
        frameThree = findViewById(R.id.frameThree);
        frameFour = findViewById(R.id.frameFour);
        frameFive = findViewById(R.id.frameFive);


        ///terms and conditon
        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        Register.setEnabled(false);

        ///error hide the action barr fix
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //////error hide the action barr fix
        emailchecks.setVisibility(View.INVISIBLE);
        signupEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
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
                            "\n");

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
//            Toast.makeText(signin.this, "Barangay: " + selectedLocation + ", Apalit Pampanga", Toast.LENGTH_SHORT).show();
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
//                String username = signupUsername.getText().toString().trim();


                // Get the email from the input field

//                checkUsernameAndEmailExists(username, email);
                // Start the OTPVerification activity
                checkEmailExists(email);

                // Dismiss the loading dialog
                loading1.cancel();
            } else {
                // Dismiss the loading dialog if validation fails
                loading1.cancel();

            }
        });

    inputChange();

    }

    private boolean validateInput() {
        boolean isValid = true;

        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String fname = signupFname.getText().toString().trim();
        String mname = signupMname.getText().toString().trim();
        String lname = signupLname.getText().toString().trim();
//        String username = signupUsername.getText().toString().trim();
        String gender = signupGender.getText().toString().trim();
        String number = signupNumber.getText().toString().trim();
        String location = signupLocation.getText().toString().trim();
        String rpassword = signupRPassword.getText().toString().trim();
        String birthday = txtview7.getText().toString().trim();

        // Validation for empty field
        if (email.isEmpty() && password.isEmpty() && fname.isEmpty() && mname.isEmpty() &&
                lname.isEmpty()  && gender.isEmpty() && number.isEmpty() &&
                location.isEmpty() && rpassword.isEmpty() && birthday.isEmpty()) {

            Toast.makeText(this, "All fields are empty, please fill up the form", Toast.LENGTH_LONG).show();
            isValid = false;
            validationfname.setVisibility(View.VISIBLE);
            validationmname.setVisibility(View.VISIBLE);
            validationlname.setVisibility(View.VISIBLE);
            validationgender.setVisibility(View.VISIBLE);
            validationbirthday.setVisibility(View.VISIBLE);
            validationnumber.setVisibility(View.VISIBLE);
            validationemail.setVisibility(View.VISIBLE);
            validationbarangay.setVisibility(View.VISIBLE);
        }
        // Validation for each field
        if (fname.isEmpty()) {
//            signupFname.setError("Field cannot be empty");
            signupFname.requestFocus();
            validationfname.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (!fname.matches("[a-zA-Z]+(?: [a-zA-Z]+)*")) {
//            signupFname.setError("Enter only Alphabetical Characters");
            signupFname.requestFocus();
            validationmname.setVisibility(View.VISIBLE);
            isValid = false;
            validationfname.setText("Enter only Alphabetical Character");
            validationfname.setVisibility(View.VISIBLE);
        }

        else if (mname.isEmpty()) {
//            signupMname.setError("Field cannot be empty");
            signupMname.requestFocus();
            isValid = false;
            validationmname.setVisibility(View.VISIBLE);
        }
        else if (!mname.matches("[a-zA-Z]+")) {
//            signupMname.setError("Enter only Alphabetical Character");
            signupMname.requestFocus();
            isValid = false;
            validationmname.setText("Enter only Alphabetical Character");
            validationmname.setVisibility(View.VISIBLE);
        }
        else if (lname.isEmpty()) {
//            signupLname.setError("Field cannot be empty");
            signupLname.requestFocus();
            isValid = false;
            validationlname.setVisibility(View.VISIBLE);
        }
        else if (!lname.matches("[a-zA-Z]+")) {
//            signupLname.setError("Enter only Alphabetical Character");
            signupLname.requestFocus();
            isValid = false;
            validationlname.setText("Enter only Alphabetical Character");
            validationlname.setVisibility(View.VISIBLE);
        }  else if (gender.isEmpty()) {
//            signupGender.setError("Field cannot be empty");
            signupGender.requestFocus();
            isValid = false;
            validationgender.setVisibility(View.VISIBLE);
        }    //features date
        else if (birthday.isEmpty()) {
//            txtview7.setError("Field cannot be empty");
            txtview7.requestFocus();
            validationbirthday.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (birthday.isEmpty()) {
            // If the birthday field is empty, show validation error
            txtview7.requestFocus();
            validationbirthday.setVisibility(View.VISIBLE);
            isValid = false;
        }


        else if (number.isEmpty()) {
//            signupNumber.setError("Field cannot be empty");
            signupNumber.requestFocus();
            isValid = false;
            validationnumber.setVisibility(View.VISIBLE);
        }
        else if (!number.matches("^09[0-9]{9}$")) {  // Checks for numbers starting with '09' and followed by 9 digits
//            signupNumber.setError("Number must be in the format 09123456789");
            signupNumber.requestFocus();
            isValid = false;
            validationnumber.setText("Number must be in the format 09123456789");
            validationnumber.setVisibility(View.VISIBLE);
        }
        else if (!number.matches("\\d+")) {  // Ensures the input contains only digits
//            signupNumber.setError("Only numeric characters are allowed");
            signupNumber.requestFocus();
            isValid = false;
            validationnumber.setText("Only numeric characters are allowed");
            validationnumber.setVisibility(View.VISIBLE);
        }
//        else if (username.isEmpty()) {
//            signupUsername.setError("Field cannot be empty");
//            signupUsername.requestFocus();
//            isValid = false;
//        }
//        else if (username.length() < 4) {
//            signupUsername.setError("Username must be at least 3 characters");
//            signupUsername.requestFocus();
//            isValid = false;
//        }
        else if (email.isEmpty()) {
//            signupEmail.setError("Field cannot be empty");
            signupEmail.requestFocus();
            isValid = false;
            validationemail.setVisibility(View.VISIBLE);
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            signupEmail.setError("Enter a valid email");
            signupEmail.requestFocus();
            isValid = false;
            validationemail.setText("Enter a valid email");
            validationemail.setVisibility(View.VISIBLE);
        }
        else if (!email.endsWith("@gmail.com")) { // New validation for @gmail.com
//            signupEmail.setError("Email must be a @gmail.com address");
            signupEmail.requestFocus();
            isValid = false;
            validationemail.setText("Email must be a @gmail.com address");
            validationemail.setVisibility(View.VISIBLE);
        }
        else if (location.isEmpty()) {
//            signupLocation.setError("Field cannot be empty");
            signupLocation.requestFocus();
            isValid = false;
            validationbarangay.setVisibility(View.VISIBLE);
        }
        else if (number.isEmpty()) {
//            signupNumber.setError("Field cannot be empty");
            signupNumber.requestFocus();
            isValid = false;
            validationnumber.setVisibility(View.VISIBLE);
        }
        else if (!number.matches("^09[0-9]{9}$")) {  // Checks for numbers starting with '09' and followed by 9 digits
//            signupNumber.setError("Number must be in the format 09123456789");
            signupNumber.requestFocus();
            isValid = false;
            validationnumber.setText("Number must be in the format 09123456789");
            validationnumber.setVisibility(View.VISIBLE);
        }
        else if (!number.matches("\\d+")) {  // Ensures the input contains only digits
//            signupNumber.setError("Only numeric characters are allowed");
            signupNumber.requestFocus();
            isValid = false;
            validationnumber.setText("Only numeric characters are allowed");
            validationnumber.setVisibility(View.VISIBLE);
        }

        else if (password.isEmpty()) {
            signupPassword.setError("Field cannot be empty");
            signupPassword.requestFocus();
            isValid = false;
            validationpassword.setVisibility(View.VISIBLE);
        }
        else if (password.length() < 8) {
//            signupPassword.setError("Minimum 7 characters");
            signupPassword.requestFocus();
            isValid = false;
        }
        else if (!password.matches(".*[A-Z].*")) { // Check for at least one uppercase letter
//            signupPassword.setError("Must contain at least one uppercase letter and one special symbol");
            signupPassword.requestFocus();
            isValid = false;
        }
//        else if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) { // Check for at least one special symbol
//            signupPassword.setError("Must contain at least one uppercase letter and one special symbol");
//            signupPassword.requestFocus();
//            isValid = false;
//        }
        else if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~`].*")) {
//            signupPassword.setError("Must contain at least one uppercase letter and one special symbol");
            signupPassword.requestFocus();
            isValid = false;
        }

        else if (rpassword.isEmpty()) {
//            signupRPassword.setError("Field cannot be empty");
            signupRPassword.requestFocus();
            isValid = false;
            validationrpassword.setVisibility(View.VISIBLE);
        }
        else if (!rpassword.equals(password)) { // Check if rpassword matches password
//            signupRPassword.setError("Passwords do not match");
            signupRPassword.requestFocus();
            isValid = false;
            validationrpassword.setText("Passwords do not match");
            validationrpassword.setVisibility(View.VISIBLE);
        }else  {
            // Parsing the birthday entered by the user
            SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
            sdf.setLenient(false); // To strictly enforce date format

            try {
                Date enteredBirthday = sdf.parse(birthday);  // Parse the birthday input
                Date today = new Date(); // Get today's date

                // Check if the entered birthday is after today's date
                if (enteredBirthday != null && enteredBirthday.after(today)) {
                    txtview7.requestFocus();
                    validationbirthday.setText("Birthday cannot be later than today.");
                    validationbirthday.setVisibility(View.VISIBLE);
                    isValid = false;
                } else {
                    validationbirthday.setVisibility(View.GONE); // Hide the error if valid
                }
            } catch (ParseException e) {
                // Handle parse exception if the date format is invalid
                validationbirthday.setText("Invalid date format. Use d/M/yyyy.");
                validationbirthday.setVisibility(View.VISIBLE);
                isValid = false;
            }
        }



        return isValid;
    }
    private void validateEmail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith(".com")) {
            emailchecks.setVisibility(View.VISIBLE);  // Show checkmark if email is valid and ends with .com
        } else {
            emailchecks.setVisibility(View.INVISIBLE);  // Hide checkmark if email is invalid or doesn't end with .com
        }
    }

    private void passwordCheck(){
     String password = signupPassword.getText().toString().trim();

     //for 8 characters
     if(password.length() >= 8){
         isAtleast8 = true;
         int color = ContextCompat.getColor(this, R.color.colorGreen);
         frameOne.setCardBackgroundColor(color);
     }else {
         isAtleast8 = false;
         int color = ContextCompat.getColor(this, R.color.red);
         frameOne.setCardBackgroundColor(color);
     }
    //for uppercase
     if (password.matches(".*[A-Z].*")){
         hasUpperCase = true;
         int color = ContextCompat.getColor(this, R.color.colorGreen);
         frameTwo.setCardBackgroundColor(color);
     }else {
         hasUpperCase = false;
         int color = ContextCompat.getColor(this, R.color.red);
         frameTwo.setCardBackgroundColor(color);
     }
     //for lowercase
     if(password.matches(".*[a-z].*")){
         hasLowerCase = true;
         int color = ContextCompat.getColor(this, R.color.colorGreen);
         frameThree.setCardBackgroundColor(color);
     }else{
         hasLowerCase = false;
         int color = ContextCompat.getColor(this, R.color.red);
         frameThree.setCardBackgroundColor(color);
     }
     //for number
     if(password.matches(".*[0-9].*")){
         hasNumber = true;
         int color = ContextCompat.getColor(this, R.color.colorGreen);
         frameFour.setCardBackgroundColor(color);
     }else{
         hasNumber = false;
         int color = ContextCompat.getColor(this, R.color.red);
         frameFour.setCardBackgroundColor(color);
     }
     //for symbol
     if(password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~`].*")){
         hasSymbol = true;
         int color = ContextCompat.getColor(this, R.color.colorGreen);
         frameFive.setCardBackgroundColor(color);
     }else{
         hasSymbol = false;
         int color = ContextCompat.getColor(this, R.color.red);
         frameFive.setCardBackgroundColor(color);
     }
     if(password.isEmpty()){
         int color = ContextCompat.getColor(this, R.color.colorDefault);
         frameOne.setCardBackgroundColor(color);
         frameTwo.setCardBackgroundColor(color);
         frameThree.setCardBackgroundColor(color);
         frameFour.setCardBackgroundColor(color);
         frameFive.setCardBackgroundColor(color);
     }
 }
 private void inputChange(){
        signupPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            passwordCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signupRPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = signupPassword.getText().toString().trim();
                String rpassword = signupRPassword.getText().toString().trim();
                if(!rpassword.equals(password)){
                    validationrpassword.setText("Passwords do not match");
                    validationrpassword.setVisibility(View.VISIBLE);
                }else{
                    validationrpassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
 }
    ////username exist or not

//    private void checkUsernameAndEmailExists(String username, String email) {
//        loading1.show();
//
//        firestore.collection("MobileUsers")
//                .whereEqualTo("username", username)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
//                        loading1.cancel();
//                        signupUsername.setError("Username already exists");
//                        signupUsername.requestFocus();
//                    } else {
//                        firestore.collection("MobileUsers")
//                                .whereEqualTo("email", email)
//                                .get()
//                                .addOnCompleteListener(emailTask -> {
//                                    if (emailTask.isSuccessful() && !emailTask.getResult().isEmpty()) {
//                                        loading1.cancel();
//                                        signupEmail.setError("Email already exists");
//                                        signupEmail.requestFocus();
//                                    }else {
//                                        registerUser();
//                                    }
//                                });
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    loading1.cancel();
//                    Toast.makeText(signin.this, "Error checking username/email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
private void checkEmailExists(String email) {
    loading1.show();

    firestore.collection("MobileUsers")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    loading1.cancel();
                    signupEmail.setError("Email already exists");
                    signupEmail.requestFocus();
                } else {
                    registerUser();
                }
            })
            .addOnFailureListener(e -> {
                loading1.cancel();
                Toast.makeText(signin.this, "Error checking email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    // Format the selected date as "dd/MM/yyyy"
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
//        String username = signupUsername.getText().toString().trim();
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
//        intent.putExtra("username", username);
        intent.putExtra("gender", gender);
        intent.putExtra("number", number);
        intent.putExtra("location", location);
        intent.putExtra("birthday", birthday);
        intent.putExtra("password", password);
        Toast.makeText(signin.this, "Registration successful", Toast.LENGTH_SHORT).show();

        startActivity(intent);
        validationfname.setVisibility(View.GONE);
        validationmname.setVisibility(View.GONE);
        validationlname.setVisibility(View.GONE);
        validationgender.setVisibility(View.GONE);
        validationbirthday.setVisibility(View.GONE);
        validationnumber.setVisibility(View.GONE);
        validationemail.setVisibility(View.GONE);
        validationbarangay.setVisibility(View.GONE);
        loading1.cancel();
    }


}
