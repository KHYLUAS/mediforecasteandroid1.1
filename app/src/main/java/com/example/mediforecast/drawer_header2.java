package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;

public class drawer_header2 extends AppCompatActivity {

    private TextView txtemail;
    private FirebaseAuth auth;
    private AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drawer_header2); // Ensure this matches your XML layout file name

        txtemail = findViewById(R.id.txtemail);  // Make sure this ID matches your XML file
        auth = FirebaseAuth.getInstance();

        // Set up the AuthStateListener to listen for authentication state changes
        authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in, display the email
                String email = user.getEmail();
                if (email != null) {
                    txtemail.setText(email);
                } else {
                    txtemail.setText("Email not available");
                }
            } else {
                // No user is signed in, redirect to login screen
                Intent intent = new Intent(getApplicationContext(), login_form.class);
                startActivity(intent);
                finish();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Attach the listener in onStart
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Detach the listener in onStop to avoid memory leaks
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
