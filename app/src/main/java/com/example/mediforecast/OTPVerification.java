package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class OTPVerification extends AppCompatActivity {
    private TextView otpEmail, resendBtn;
    private String getEmail,email, fname,mname,lname,username,gender,birthday,location,number,password;
    private String generatedOTP;
    private Button verify;
    private PinView pinview;
    private loading1 loading1;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpverification);

        //////email set to authentication
        otpEmail = findViewById(R.id.otpEmail);
        verify = findViewById(R.id.verify); // Ensure this ID matches your layout file
        pinview = findViewById(R.id.pinview);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        resendBtn = findViewById(R.id.resendBtn);


        // Retrieve the email from the intent
         email = getIntent().getStringExtra("email");
         fname = getIntent().getStringExtra("fname");
         mname = getIntent().getStringExtra("mname");
        lname = getIntent().getStringExtra("lname");
        username = getIntent().getStringExtra("username");
         gender = getIntent().getStringExtra("gender");
        birthday = getIntent().getStringExtra("birthday");
        location = getIntent().getStringExtra("location");
         number = getIntent().getStringExtra("number");
        password = getIntent().getStringExtra("password");

        // set email into textview authentiction


        if (email != null) {
            otpEmail.setText(email);
            getEmail = otpEmail.getText().toString();
            generatedOTP = generateOTP();
            SendOTP(getEmail, generatedOTP);
        }
        verify.setOnClickListener(v -> {
            String enteredOTP = pinview.getText().toString();
            if (enteredOTP.equals(generatedOTP)) {
                // OTP matches, register the user
                registerUserInDatabase(getEmail);
            } else {
                // OTP does not match
                Toast.makeText(OTPVerification.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });


        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatedOTP = generateOTP();
                SendOTP(getEmail, generatedOTP);
                startCountdown(60*1000);
            }
        });


        startCountdown(60*1000);
    }

    // Method to send OTP via email
    private void SendOTP(String recieverEmail, String otp) {
        try {
            String senderEmail = "kramtrasoc@gmail.com";
            String email = recieverEmail;
            String passwordSenderEmail = "hids oxjb sdij uwsj";
            String stringHost = "smtp.gmail.com";


            Properties properties = System.getProperties();
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
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setSubject("MediForecast OTP Verification");
            mimeMessage.setText("Hello, \n\nHere is your OTP: " + otp);

            // Send email in a new thread
            new Thread(() -> {
                try {
                    Transport.send(mimeMessage);
                    runOnUiThread(() -> Toast.makeText(this, "OTP sent to your email", Toast.LENGTH_SHORT).show());
                } catch (MessagingException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Failed to send OTP", Toast.LENGTH_SHORT).show());
                }
            }).start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Method to generate a 6-digit OTP
    private String generateOTP() {
        int otpLength = 6;
        Random random = new Random();
        StringBuilder otp = new StringBuilder(otpLength);
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10)); // Generates digits between 0-9
        }
        return otp.toString();
    }
    private void registerUserInDatabase(String email) {

        // Implement your user registration logic here
        registerUser();
//        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(OTPVerification.this, SplashScreen.class);
//        startActivity(intent);
//        finish();
    }

    private void registerUser() {
//        String email = signupEmail.getText().toString().trim();
//        String password = signupPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // User registered successfully
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    saveUserDataToFirestore(user.getUid());
                }
            } else {
                // Registration failed
                Toast.makeText(OTPVerification.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveUserDataToFirestore(String userId) {


        // Create a map to store user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("fname", fname);
        userData.put("mname", mname);
        userData.put("lname", lname);
        userData.put("username", username);
        userData.put("gender", gender);
        userData.put("number", number);
        userData.put("location", location);
        userData.put("birthday", birthday);
        userData.put("password", password);
        userData.put("email", email);


        // Save the data to Firestore
        firestore.collection("users").document(userId).set(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Data saved successfully
                Toast.makeText(OTPVerification.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                // Optionally navigate to another activity
                startActivity(new Intent(OTPVerification.this, SplashScreen.class));
                finish();
            } else {
                // Data saving failed
                Toast.makeText(OTPVerification.this, "Failed to save user data: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void startCountdown(long millisInFuture) {
        new CountDownTimer(millisInFuture, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Calculate minutes and seconds
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                // Update the TextView
                resendBtn.setText(String.format("%02d",seconds));
                resendBtn.setEnabled(false);
            }

            @Override
            public void onFinish() {
                // When the timer finishes
                resendBtn.setText("Resend");
                resendBtn.setEnabled(true);
            }
        }.start();
    }
}
