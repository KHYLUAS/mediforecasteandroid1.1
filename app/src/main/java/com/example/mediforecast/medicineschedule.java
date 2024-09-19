package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class medicineschedule extends AppCompatActivity {
    private TextView once, twice, thrice, custom;
    private ImageView medicinearrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicineschedule);

        once = findViewById(R.id.once);
        twice = findViewById(R.id.twice);
        thrice = findViewById(R.id.thrice);
        custom = findViewById(R.id.custom);
        medicinearrow = findViewById(R.id.medicinearrow);

        once.setOnClickListener(v->{
            Intent intent = new Intent(medicineschedule.this, oncedaily.class);
            startActivity(intent);
            finish();
        });
        twice.setOnClickListener(v->{
            Intent intent = new Intent(medicineschedule.this, secondaily.class);
            startActivity(intent);
            finish();
        });
        thrice.setOnClickListener(v->{
            Intent intent = new Intent(medicineschedule.this, thirddaily.class);
            startActivity(intent);
            finish();
        });
        medicinearrow.setOnClickListener(v->{
            Intent intent = new Intent(medicineschedule.this, medicine_signin.class);
            startActivity(intent);
            finish();
        });
    }
}