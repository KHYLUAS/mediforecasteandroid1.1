package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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

public class forget_password extends AppCompatActivity {
    private TextView otpEmail, resendBtn;
    private String getEmail,email,password;
    private Button verify;
    private PinView pinview;
    private String generatedOTP;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);

        otpEmail = findViewById(R.id.otpEmail);
        verify = findViewById(R.id.verify); // Ensure this ID matches your layout file
        pinview = findViewById(R.id.pinview);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        resendBtn = findViewById(R.id.resendBtn);
        image = findViewById(R.id.img4);

        // Retrieve the email from the intent
        email = getIntent().getStringExtra("email");


        if (email != null) {
            otpEmail.setText(email);
            getEmail = otpEmail.getText().toString();
            generatedOTP = generateOTP();
            SendOTP(getEmail, generatedOTP);
        }
        verify.setOnClickListener(v -> {
            String enteredOTP = pinview.getText().toString();
            if (enteredOTP.equals(generatedOTP)) {
                Intent intent = new Intent(forget_password.this, edit_password.class);
                intent.putExtra("email",email);
                startActivity(intent);
                finish();
            } else {
                // OTP does not match
                Toast.makeText(forget_password.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
        image.setOnClickListener(v -> {
            Intent intent = new Intent(forget_password.this, login_form.class);
            startActivity(intent);
            finish();
        });

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatedOTP = generateOTP();
                SendOTP(getEmail, generatedOTP);
                startCountdown(60*1000);
            }
        });


    }

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
    private String generateOTP() {
        int otpLength = 6;
        Random random = new Random();
        StringBuilder otp = new StringBuilder(otpLength);
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10)); // Generates digits between 0-9
        }
        return otp.toString();
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