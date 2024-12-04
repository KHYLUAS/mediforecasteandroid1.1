package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
//        custom = findViewById(R.id.everyHours);
        medicinearrow = findViewById(R.id.medicinearrow);

        Intent updateIntent = getIntent();
        boolean isUpdate = updateIntent.getBooleanExtra("isUpdate", false);
        String newUnitType = updateIntent.getStringExtra("unitType");

        if (isUpdate) {
            Log.d("MedicineSchedule", "Update mode");
        } else {
            Log.d("MedicineSchedule", "Add mode");
        }
        once.setOnClickListener(v->{
            Intent intent = new Intent(medicineschedule.this, oncedaily.class);
            intent.putExtra("isUpdate", isUpdate);
            intent.putExtra("newUnitType", newUnitType);
            startActivity(intent);
            finish();
        });
        twice.setOnClickListener(v->{
            Intent intent = new Intent(medicineschedule.this, twicedaily.class);
            intent.putExtra("isUpdate", isUpdate);
            intent.putExtra("newUnitType", newUnitType);
            startActivity(intent);
            finish();
        });
        thrice.setOnClickListener(v->{
            Intent intent = new Intent(medicineschedule.this, thricedaily.class);
            intent.putExtra("isUpdate", isUpdate);
            intent.putExtra("newUnitType", newUnitType);
            startActivity(intent);
            finish();
        });
        medicinearrow.setOnClickListener(v->{
            if(isUpdate){
                Intent intent = new Intent(medicineschedule.this, UpdateReminder.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(medicineschedule.this, medicine_signin.class);
                startActivity(intent);
                finish();
            }

        });
//        custom.setOnClickListener(v->{
//            Intent intent = new Intent(medicineschedule.this, everyhour.class);
//            intent.putExtra("isUpdate", isUpdate);
//            intent.putExtra("newUnitType", newUnitType);
//            startActivity(intent);
//            finish();
//        });
    }
}