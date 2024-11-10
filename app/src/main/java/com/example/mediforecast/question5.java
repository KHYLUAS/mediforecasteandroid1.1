package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class question5 extends AppCompatActivity {
    private MaterialButton next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question5);
        next = findViewById(R.id.next);

        next.setOnClickListener(v->{
            Intent intent = new Intent(question5.this, question6.class);
            startActivity(intent);
            finish();
        });
    }
}