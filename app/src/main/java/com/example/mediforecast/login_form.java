package com.example.mediforecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in, navigate to the main activity directly
            navigateToMainActivity();
            return; // Exit onCreate early to prevent showing the login form
        }
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

        loadLoginCredentials();
//        // Check if the user had selected "Remember Me"
//        if (sharedPreferences.getBoolean("CHECKBOX", false)) {
//            String savedEmail = sharedPreferences.getString("EMAIL", "");
//            String savedPassword = sharedPreferences.getString("PASSWORD", "");
//
//            if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
//                // Automatically log in if credentials are saved
//                autoLogin(savedEmail, savedPassword);
//            }
//        }
        // Set up listeners
        setupListeners();
    }
//    // Function to perform automatic login
//    private void autoLogin(String email, String password) {
//        // Show loading dialog
//        loading1.show();
//
//        // Attempt to sign in using saved credentials
//        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
//            // Hide loading dialog
//            loading1.cancel();
//
//            if (task.isSuccessful()) {
//                // User is successfully logged in, navigate to the home screen
//                Intent intent = new Intent(login_form.this, Menubar.class);
//                startActivity(intent);
//                finish();
//            } else {
//                // In case of failure, show login form and let the user log in manually
//                Toast.makeText(login_form.this, "Auto-login failed. Please log in manually.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

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
        String email = logEmail.getText().toString().trim();
        String password = logPassword.getText().toString().trim();

        // Show loading dialog
        loading1.show();

        // Sign in with Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            // Hide loading dialog
            loading1.cancel();

            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // First, try fetching user data from the cache
                    firestore.collection("MobileUsers").document(user.getUid())
                            .get(Source.CACHE)
                            .addOnCompleteListener(task1 -> {
                                // Save login credentials if "Remember Me" is checked
                                    saveLoginCredentials(email, password);

                                Toast.makeText(login_form.this, "Login successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(login_form.this, Menubar.class);
                                startActivity(intent);
                                finish();
//                                if (task1.isSuccessful() && task1.getResult() != null) {
//                                    // Successfully retrieved from cache
//                                    setUserGlobalData(task1.getResult());
//                                } else {
//
//                                    // Fallback: try fetching from the server if the cache fails
//                                    firestore.collection("MobileUsers").document(user.getUid())
//                                            .get(Source.SERVER) // Fetch from server if cache fails
//                                            .addOnCompleteListener(task2 -> {
//                                                if (task2.isSuccessful() && task2.getResult() != null) {
//
//                                                    setUserGlobalData(task2.getResult());
//                                                }
//                                                else {
//                                                    Toast.makeText(login_form.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }).addOnFailureListener(e -> {
//                                                Toast.makeText(login_form.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                            });
//                                }
                            });
                }
            } else {
                // Handle login failure
                validationboth.setVisibility(View.VISIBLE);
//                handleLoginFailure(task.getException());
            }
        });
    }
    private void saveLoginCredentials(String email, String password) {
        // Save email and password to SharedPreferences
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
    private void setUserGlobalData(DocumentSnapshot document) {
        String name = document.getString("fname") + " " + document.getString("lname");
        String email = document.getString("email");
        String contact = document.getString("number");
        String location = document.getString("location");
        String birthday = document.getString("birthday");
        String gender = document.getString("gender");
        String profileImage = document.getString("profileImage");

        // Update GlobalUserData
        GlobalUserData.setName(name);
        GlobalUserData.setEmail(email);
        GlobalUserData.setContact(contact);
        GlobalUserData.setLocation(location);
        GlobalUserData.setBirthday(birthday);
        GlobalUserData.setGender(gender);
        GlobalUserData.setProfileImage(profileImage);


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