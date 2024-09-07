package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class update_password extends AppCompatActivity {

    private EditText oldPasswordField, newPasswordField, confirmPasswordField;
    private ImageView oldPasswordEye, newPasswordEye, confirmPasswordEye;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private boolean oldPasswordVisible = false, newPasswordVisible = false, confirmPasswordVisible = false;
    private static final String TAG = "UpdatePassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        // Initialize views
        oldPasswordField = findViewById(R.id.Recently);
        newPasswordField = findViewById(R.id.update_password);
        confirmPasswordField = findViewById(R.id.update_confirm);
        oldPasswordEye = findViewById(R.id.recent);
        newPasswordEye = findViewById(R.id.passwordeye);
        confirmPasswordEye = findViewById(R.id.forgetconfirm);
        Button resetButton = findViewById(R.id.reset);

        // Firebase initialization
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Set click listeners for password visibility toggling
        oldPasswordEye.setOnClickListener(v -> toggleOldPasswordVisibility());
        newPasswordEye.setOnClickListener(v -> toggleNewPasswordVisibility());
        confirmPasswordEye.setOnClickListener(v -> toggleConfirmPasswordVisibility());

        resetButton.setOnClickListener(v -> updatePassword());
    }

    private void toggleOldPasswordVisibility() {
        if (oldPasswordVisible) {
            oldPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            oldPasswordEye.setImageResource(R.drawable.removeeye); // Set eye-off icon
        } else {
            oldPasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            oldPasswordEye.setImageResource(R.drawable.removeeye); // Set eye-on icon
        }
        oldPasswordVisible = !oldPasswordVisible;
        oldPasswordField.setSelection(oldPasswordField.getText().length());
    }

    private void toggleNewPasswordVisibility() {
        if (newPasswordVisible) {
            newPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            newPasswordEye.setImageResource(R.drawable.removeeye); // Set eye-off icon
        } else {
            newPasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            newPasswordEye.setImageResource(R.drawable.removeeye); // Set eye-on icon
        }
        newPasswordVisible = !newPasswordVisible;
        newPasswordField.setSelection(newPasswordField.getText().length());
    }

    private void toggleConfirmPasswordVisibility() {
        if (confirmPasswordVisible) {
            confirmPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            confirmPasswordEye.setImageResource(R.drawable.removeeye); // Set eye-off icon
        } else {
            confirmPasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            confirmPasswordEye.setImageResource(R.drawable.removeeye); // Set eye-on icon
        }
        confirmPasswordVisible = !confirmPasswordVisible;
        confirmPasswordField.setSelection(confirmPasswordField.getText().length());
    }

    private void updatePassword() {
        String oldPassword = oldPasswordField.getText().toString().trim();
        String newPassword = newPasswordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(update_password.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(update_password.this, "New passwords do not match", Toast.LENGTH_SHORT).show();
        } else if (!isValidPassword(newPassword)) {
            Toast.makeText(update_password.this, "Password does not meet the criteria", Toast.LENGTH_SHORT).show();
        } else {
            // Get current user ID
            String userId = mAuth.getCurrentUser().getUid();

            // Fetch user document from Firestore
            firestore.collection("MobileUsers").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String email = document.getString("email");
                        String currentPassword = document.getString("password");

                        // Verify the old password using FirebaseAuth's reauthentication method
                        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
                        mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(reauthTask -> {
                            if (reauthTask.isSuccessful()) {
                                // Old password verified, proceed to update new password via FirebaseAuth
                                mAuth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(passwordUpdateTask -> {
                                    if (passwordUpdateTask.isSuccessful()) {
                                        Log.d(TAG, "Password updated successfully in FirebaseAuth!");
                                        Toast.makeText(update_password.this, "Password updated successfully", Toast.LENGTH_SHORT).show();

                                        // Update password field in Firestore document
                                        firestore.collection("MobileUsers").document(userId).update("password", newPassword)
                                                .addOnCompleteListener(firestoreUpdateTask -> {
                                                    if (firestoreUpdateTask.isSuccessful()) {

                                                        startActivity(new Intent(update_password.this, updatesplashscreen.class));
                                                        finish();
//                                                        Log.d(TAG, "Password updated successfully in Firestore!");
                                                        // Optionally update non-sensitive user data in Firestore here
//                                                        firestore.collection("MobileUsers").document(userId).update("lastPasswordChange", System.currentTimeMillis())
//                                                                .addOnCompleteListener(firestoreUpdateTask1 -> {
//                                                                    if (firestoreUpdateTask1.isSuccessful()) {
//                                                                        Log.d(TAG, "Firestore updated with last password change timestamp");
//                                                                    } else {
//                                                                        Log.w(TAG, "Error updating Firestore", firestoreUpdateTask1.getException());
//                                                                    }
//                                                                });

                                                    } else {
                                                        Log.w(TAG, "Error updating password in Firestore", firestoreUpdateTask.getException());
                                                        Toast.makeText(update_password.this, "Error updating password", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Log.w(TAG, "Error updating password in FirebaseAuth", passwordUpdateTask.getException());
                                        Toast.makeText(update_password.this, "Error updating password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Log.w(TAG, "Error reauthenticating user", reauthTask.getException());
                                Toast.makeText(update_password.this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(update_password.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.w(TAG, "Error fetching user data", task.getException());
                    Toast.makeText(update_password.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private boolean isValidPassword(String password) {
        return password.length() >= 7 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*].*");
    }
}
