package com.example.mediforecast;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class edit_password extends AppCompatActivity {
    private EditText forgetpass, forget;
    private ImageView passwordeye,forgetconfirm,img4;
    private Button update;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private boolean forgetPassVisible,forgetVisible = false;
    private String email;
    private static final String TAG = "Firestore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        update = findViewById(R.id.update);
        forgetpass = findViewById(R.id.forgetpass);
        forget = findViewById(R.id.forget);
        passwordeye = findViewById(R.id.passwordeye);
        forgetconfirm = findViewById(R.id.forgetconfirm);
        img4 = findViewById(R.id.img4);


        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        // Password eye toggle



        passwordeye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleForgetPassVisibility();
            }
        });

        forgetconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleForgetVisibility();
            }
        });

//        setupListeners();

        img4.setOnClickListener(v -> {
            Intent intent = new Intent(edit_password.this, login_form.class);
            startActivity(intent);
            finish();
        });

        update.setOnClickListener(v -> {
            String newPassword = forgetpass.getText().toString().trim();
            String confirmPassword = forget.getText().toString().trim();


            email = getIntent().getStringExtra("email");

            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(edit_password.this, "Please enter the password in both fields", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(edit_password.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (!isValidPassword(newPassword)) {
                Toast.makeText(edit_password.this, "Password does not meet the criteria", Toast.LENGTH_SHORT).show();
            } else {
//                updatePassword(newPassword);
                firestore.collection("users")
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
                                    Log.d(TAG, "Name: " + name);

                                    // Update the document
                                    firestore.collection("users")
                                            .document(documentId)
                                            .update("password", newPassword)  // Update the "password" field with a new value
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");

                                                // Start the splashscreenupdate activity
                                                startActivity(new Intent(edit_password.this, splashscreenupdate.class));

                                                // Finish the current activity
                                                finish();
                                            })
                                            .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
                                }
                            }
                        })
                        .addOnFailureListener(e -> Log.w(TAG, "Error getting documents: ", e));

            }
        });
    }

//    private void setupListeners() {
//
//
//
//        update.setOnClickListener(v -> {
//            String newPassword = forgetpass.getText().toString().trim();
//            String confirmPassword = forget.getText().toString().trim();
//
//            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
//                Toast.makeText(edit_password.this, "Please enter the password in both fields", Toast.LENGTH_SHORT).show();
//            } else if (!newPassword.equals(confirmPassword)) {
//                Toast.makeText(edit_password.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//            } else if (!isValidPassword(newPassword)) {
//                Toast.makeText(edit_password.this, "Password does not meet the criteria", Toast.LENGTH_SHORT).show();
//            } else {
//                updatePassword(newPassword);
//            }
//        });
//
//
//
//    }

    private boolean isValidPassword(String password) {
        return password.length() >= 7 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*].*");
    }

//    private void updatePassword(String newPassword) {
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
//                if (updateTask.isSuccessful()) {
//                    updateFirestorePassword(user.getUid(), newPassword);
//                }
////                else {
////                    handleUpdatePasswordFailure(updateTask.getException());
////                }
//            }).addOnFailureListener(e -> {
//                Toast.makeText(edit_password.this, "Error updating password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            });
//        } else {
//            Toast.makeText(edit_password.this, "User not authenticated.", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void handleUpdatePasswordFailure(Exception exception) {
//        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
//            Toast.makeText(edit_password.this, "Invalid credentials. Please try again.", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(edit_password.this, "Failed to update password. Please try again later.", Toast.LENGTH_LONG).show();
//        }
//    }

//    private void updateFirestorePassword(String userId, String newPassword) {
//        firestore.collection("users").document(userId)
//                .update("password", newPassword)
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(edit_password.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(edit_password.this, splashscreenupdate.class);
//                    startActivity(intent);
//                    finish();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(edit_password.this, "Failed to update password in Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }


    // Method to toggle password visibility for forgetpass field
    private void toggleForgetPassVisibility() {
        if (forgetPassVisible) {
            // Hide password
            forgetpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordeye.setImageResource(R.drawable.removeeye); // Set eye-off icon
        } else {
            // Show password
            forgetpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordeye.setImageResource(R.drawable.removeeye); // Set eye-on icon
        }

        // Toggle the boolean flag
        forgetPassVisible = !forgetPassVisible;

        // Move the cursor to the end of the text in forgetpass field
        forgetpass.setSelection(forgetpass.getText().length());
    }

    // Method to toggle password visibility for forget field
    private void toggleForgetVisibility() {
        if (forgetVisible) {
            // Hide password
            forget.setTransformationMethod(PasswordTransformationMethod.getInstance());
            forgetconfirm.setImageResource(R.drawable.removeeye); // Set eye-off icon
        } else {
            // Show password
            forget.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            forgetconfirm.setImageResource(R.drawable.removeeye); // Set eye-on icon
        }

        // Toggle the boolean flag
        forgetVisible = !forgetVisible;

        // Move the cursor to the end of the text in forget field
        forget.setSelection(forget.getText().length());
    }

}


