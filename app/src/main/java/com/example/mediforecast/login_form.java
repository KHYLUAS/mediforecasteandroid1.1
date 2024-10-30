package com.example.mediforecast;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

public class login_form extends AppCompatActivity {
    public EditText logEmail, logPassword;
    private FirebaseFirestore firestore;
    private MaterialButton loginButton;
    private TextView signUpLink, forgetPasswordLink, validationboth, validationemail, validationpass;
    private ImageView passwordeye, emailchecker;
    private FirebaseAuth mAuth;
    private boolean isPasswordVisible = false;
    private loading1 loading1;
    private String email;
    public CheckBox rememberMeCB;
    public SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        // Get email from intent
        email = getIntent().getStringExtra("email");

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
//        // Check if user is already logged in
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            // User is logged in, navigate to the main activity directly
//            navigateToMainActivity();
//            return; // Exit onCreate early to prevent showing the login form
//        }
        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        logEmail = findViewById(R.id.logemail);
        logPassword = findViewById(R.id.logpassword);
        loginButton = findViewById(R.id.button4);
        signUpLink = findViewById(R.id.txtview1);
        forgetPasswordLink = findViewById(R.id.forgetPassword);
        passwordeye = findViewById(R.id.passwordeye);
        emailchecker = findViewById(R.id.checkbox);
        validationboth = findViewById(R.id.validationboth);
        validationemail = findViewById(R.id.validationemail);
        validationpass = findViewById(R.id.validationpass);


        // Initialize loading dialog
        loading1 = new loading1(this);
        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
// Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyInfo", MODE_PRIVATE);

        // Load saved login credentials from SharedPreferences
        String savedEmail = sharedPreferences.getString("EMAIL", null);
        String savedPassword = sharedPreferences.getString("PASSWORD", null);

        if (savedEmail != null && savedPassword != null) {
            // Credentials exist, auto-navigate to Menubar
            navigateToMainActivity();
            return;
        }
        loadLoginCredentials();
        // Set up listeners
        setupListeners();
    }

    private void setupListeners() {
        // Email validation
        emailchecker.setVisibility(View.INVISIBLE);
        logEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Password eye toggle
        passwordeye.setOnClickListener(v -> togglePasswordVisibility());

        // Login button click
        loginButton.setOnClickListener(v -> {
            if (validateInput()) {
                loginUser();
            }
        });

        // Sign-up link click
        signUpLink.setOnClickListener(v -> {
            loading1.show();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(login_form.this, signin.class);
                startActivity(intent);
                finish();
                loading1.cancel(); // Dismiss loading dialog after navigation
            }, 2000);
        });

        // Forget password link click
        forgetPasswordLink.setOnClickListener(v -> {
            if (inputEmail()) {
                String email = logEmail.getText().toString().trim();
                checkEmailExists(email);
            }
        });
    }

    private boolean validateInput() {
        String email = logEmail.getText().toString().trim();
        String password = logPassword.getText().toString().trim();
        if(password.isEmpty() && email.isEmpty()){
            validationemail.setVisibility(View.VISIBLE);
            validationpass.setVisibility(View.VISIBLE);
            logEmail.requestFocus();
            return false;
        }
        // Validate email
        if (email.isEmpty()) {
//            logEmail.setError("Field cannot be empty");
            logEmail.requestFocus();
            validationemail.setVisibility(View.VISIBLE);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            logEmail.setError("Enter a valid email");
            validationemail.setText("Enter a valid email");
            validationemail.setVisibility(View.VISIBLE);
            logEmail.requestFocus();
            return false;
        }

        // Validate password
        if (password.isEmpty()) {
//            logPassword.setError("Field cannot be empty");
            validationpass.setVisibility(View.VISIBLE);
            logPassword.requestFocus();
            return false;
        } else if (password.length() < 8) {
//            logPassword.setError("Minimum 6 characters");
            validationpass.setText("Minimum 8 characters");
            validationpass.setVisibility(View.VISIBLE);
            logPassword.requestFocus();
            return false;
        }

        return true;
    }

    //
    private void loginUser() {
        loading1.show();
        String email = logEmail.getText().toString().trim();
        String password = logPassword.getText().toString().trim();
        firestore.collection("MobileUsers")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        Log.d(TAG, "No matching documents found.");
                    } else {
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            // Retrieve the document ID
                            String documentId = documentSnapshot.getId();
                            Log.d(TAG, "Found document with ID: " + documentId);

                            // Retrieve other fields from the document if needed
                            String name = documentSnapshot.getString("password");
                            String Email = documentSnapshot.getString("email");
                            Log.d(TAG, "Name: " + name + Email);

                            if (email.equals(Email) && password.equals(name)){
                                saveLoginCredentials(email, password);
                                loading1.cancel();

                                Intent intent = new Intent(login_form.this, Menubar.class);
                                startActivity(intent);
                                finish();

                            }else{
                                loading1.cancel();
                                validationboth.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error getting documents: ", e));
    }
    private void saveLoginCredentials(String email, String password) {
        // Save email and password to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EMAIL", email);
        editor.putString("PASSWORD", password);
        editor.apply();
    }
    private void loadLoginCredentials() {
        // Load saved email and password from SharedPreferences
        String savedEmail = sharedPreferences.getString("EMAIL", "");
        String savedPassword = sharedPreferences.getString("PASSWORD", "");
            logEmail.setText(savedEmail);
            logPassword.setText(savedPassword);
    }
    private void navigateToMainActivity() {
        Intent intent = new Intent(login_form.this, Menubar.class);
        startActivity(intent);
        finish(); // Close the login activity
    }

    private void handleLoginFailure(Exception exception) {
        if (exception != null) {
            String message = exception.getMessage();
            if (message != null && message.contains("There is no user record corresponding to this identifier")) {
                Toast.makeText(login_form.this, "Email does not exist", Toast.LENGTH_SHORT).show();
            } else if (message != null && message.contains("The password is invalid")) {
                Toast.makeText(login_form.this, "The password is incorrect", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(login_form.this, "Login failed: " + message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean inputEmail() {
        String email = logEmail.getText().toString().trim();

        if (email.isEmpty()) {
            logEmail.setError("Please enter your email address");
            logEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            logEmail.setError("Enter a valid email address");
            logEmail.requestFocus();
            return false;
        }
        return true;
    }

    private void validateEmail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailchecker.setVisibility(View.VISIBLE);  // Show checkmark if email is valid
        } else {
            emailchecker.setVisibility(View.INVISIBLE);  // Hide checkmark if email is invalid
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            logPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordeye.setImageResource(R.drawable.removeeye); // Set eye-off icon
        } else {
            logPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordeye.setImageResource(R.drawable.removeeye); // Set eye-on icon
        }
        isPasswordVisible = !isPasswordVisible;
        logPassword.setSelection(logPassword.getText().length());
    }

    private void checkEmailExists(String email) {
        loading1.show();

        firestore.collection("MobileUsers")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    loading1.cancel();
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        Intent intent = new Intent(login_form.this, forget_password.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                        // Create an intent and put the email as an extra

                        loading1.cancel();
                    }else{
                        logEmail.setError("Email does not exist ");
                        logEmail.requestFocus();

                    }
                })
                .addOnFailureListener(e -> {
                    loading1.cancel();
                    Toast.makeText(login_form.this, "Error checking email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}