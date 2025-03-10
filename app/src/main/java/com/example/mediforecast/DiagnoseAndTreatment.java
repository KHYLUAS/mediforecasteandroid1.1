package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class DiagnoseAndTreatment extends AppCompatActivity {
    private TabLayout tablayout;
    private String diagnoseName;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diagnose_and_treatment);
        diagnoseName = getIntent().getStringExtra("diagnoseName");
        back = findViewById(R.id.back);
        tablayout = findViewById(R.id.tablayout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fragmentImplement(new DiagnoseDetailFragment(), diagnoseName);

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    fragmentImplement(new DiagnoseDetailFragment(), diagnoseName);
                    Toast.makeText(getApplicationContext(), tab.getText().toString(), Toast.LENGTH_SHORT).show();
                }  else if(tab.getPosition() == 1){
                    fragmentImplement(new TreatmentOptionFragment(), diagnoseName);
                    Toast.makeText(getApplicationContext(), tab.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        back.setOnClickListener(v->{
            Intent intent = new Intent(DiagnoseAndTreatment.this, Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "");
            startActivity(intent);
            finish();
        });
    }
    private void fragmentImplement(Fragment fragment, String diagnoseName){
        Bundle bundle = new Bundle();
        bundle.putString("diagnoseName", diagnoseName);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}