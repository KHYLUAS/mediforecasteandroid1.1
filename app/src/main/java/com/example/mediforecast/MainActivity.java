package com.example.mediforecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button move;
    private loading1 loading1;
    public SharedPreferences sharedPreferences;
    private boolean clickOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        move = findViewById(R.id.move);
        loading1 = new loading1(this);
        sharedPreferences = getSharedPreferences("SPLASH_PREF", MODE_PRIVATE);
        // Check if the button has been clicked before from shared preferences
        clickOnce = sharedPreferences.getBoolean("move_clicked", false);

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the loading dialog
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("move_clicked", true);
                editor.apply();

                loading1.show();

                // Start the new activity
                Intent intent = new Intent(MainActivity.this, login_form.class);
                startActivity(intent);

                // Schedule the loading dialog to cancel after 3 seconds
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading1.cancel();
                    }
                }, 2000);
            }
        });
    }
}
