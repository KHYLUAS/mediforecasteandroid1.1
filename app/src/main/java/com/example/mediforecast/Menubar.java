package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mediforecast.databinding.ActivityMenubarBinding;

public class Menubar extends AppCompatActivity {
    private ActivityMenubarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenubarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String fragmentToDisplay = intent.getStringExtra("EXTRA_FRAGMENT");

        // Replace fragment on start
        if (savedInstanceState == null){
            if("PROFILE".equals(fragmentToDisplay)){
                replaceFragment(new profile_fragment());
            } else {
                replaceFragment(new home1_fragment());
            }
        }


        // Set up navigation listener
        binding.buttonNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new home1_fragment());
            } else if (itemId == R.id.patientchecker) {
                replaceFragment(new checker_fragment());
            } else if (itemId == R.id.reminder) {
                replaceFragment(new reminder_fragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new profile_fragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Framelayout, fragment); // Ensure R.id.fragment_container matches your layout ID
        fragmentTransaction.commit();
    }
}
