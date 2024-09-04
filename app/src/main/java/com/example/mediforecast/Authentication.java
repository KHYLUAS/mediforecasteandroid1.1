package com.example.mediforecast;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Authentication extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Example data for user registration and email sending
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "password123";
        String birthday = "Jan 01, 2000";
        String contactNumber = "1234567890";
        String gender = "Male";
        String location = "Tabuyuc";

        registerUser(firstName, lastName, email, password, birthday, contactNumber, gender, location);
    }

    private void registerUser(String firstName, String lastName, String email, String password, String birthday, String contactNumber, String gender, String location) {
        // Register the user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Successfully registered
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Store additional user details in Firestore
                            storeUserDetailsInFirestore(firebaseUser.getUid(), firstName, lastName, email, birthday, contactNumber, gender, location);

                            // Send an email to the user
                            sendEmail(email);
                        }
                    } else {
                        // If registration fails, display a message to the user.
                        Toast.makeText(Authentication.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void storeUserDetailsInFirestore(String userId, String firstName, String lastName, String email, String birthday, String contactNumber, String gender, String location) {
        // Create a map to store user details
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("email", email);
        user.put("birthday", birthday);
        user.put("contactNumber", contactNumber);
        user.put("gender", gender);
        user.put("location", location);

        // Store user details in Firestore under the UID of the authenticated user
        firestore.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Authentication.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Authentication.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void sendEmail(String receiverEmail) {
        new Thread(() -> {
            try {
                String senderEmail = "kramtrasoc@gmail.com";
                String passwordSenderEmail = "hids oxjb sdij uwsj";
                String stringHost = "smtp.gmail.com";

                Properties properties = new Properties();
                properties.put("mail.smtp.host", stringHost);
                properties.put("mail.smtp.port", "587");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.auth", "true");

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, passwordSenderEmail);
                    }
                });

                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
                mimeMessage.setSubject("Welcome to MediForecast!");
                mimeMessage.setText("Hello " + receiverEmail + ",\n\nThank you for registering with MediForecast!\n\nBest regards,\nMediForecast Team");

                Transport.send(mimeMessage);

                runOnUiThread(() -> Toast.makeText(Authentication.this, "Email Sent Successfully", Toast.LENGTH_SHORT).show());

            } catch (AddressException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Authentication.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } catch (MessagingException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Authentication.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
