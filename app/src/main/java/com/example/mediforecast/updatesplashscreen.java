package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


public class updatesplashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatesplashscreen);

        // Delay the navigation to the fragment
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(updatesplashscreen.this, Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "PROFILE");
            startActivity(intent);
            finish(); // Close the splash screen activity
        }, 1000);
    }
}
