package com.example.mediforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash extends AppCompatActivity {
    Handler handler = new Handler();
    public SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        
        preferences = getSharedPreferences("SPLASH_PREF", Context.MODE_PRIVATE);
        boolean onceClicked = preferences.getBoolean("move_clicked", false);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(onceClicked){
                    Intent intent = new Intent(splash.this, login_form.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        },2500);
    }
}