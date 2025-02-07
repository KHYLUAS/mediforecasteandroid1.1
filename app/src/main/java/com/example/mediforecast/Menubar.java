package com.example.mediforecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mediforecast.databinding.ActivityMenubarBinding;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.navigation.NavigationBarView;

public class Menubar extends AppCompatActivity {
    private ActivityMenubarBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenubarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("TapTargetsPrefs", MODE_PRIVATE);
        binding.buttonNav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        Intent intent = getIntent();
        String fragmentToDisplay = intent.getStringExtra("EXTRA_FRAGMENT");

        // Replace fragment on start
        if (savedInstanceState == null){
            if("PROFILE".equals(fragmentToDisplay) || "UPDATE".equals(fragmentToDisplay)
                    || "EDIT".equals(fragmentToDisplay) || "BACK".equals(fragmentToDisplay)){
                replaceFragment(new profile_fragment());
                binding.buttonNav.setSelectedItemId(R.id.profile);
            } else if ("REMINDER".equals(fragmentToDisplay) || "REMINDERDB".equals(fragmentToDisplay)
                    || "SIGNIN".equals(fragmentToDisplay) || "VIEW".equals(fragmentToDisplay) ||
            "ADDREMINDER".equals(fragmentToDisplay) || "UPDATEREMINDER".equals(fragmentToDisplay)){
                replaceFragment(new reminder_fragment());
                binding.buttonNav.setSelectedItemId(R.id.reminder);
            }  else if("COMMUNITYDB".equals(fragmentToDisplay)){
                replaceFragment(new home1_fragment());
                binding.buttonNav.setSelectedItemId(R.id.home);
            }
            else if("ANALYZER".equals(fragmentToDisplay)){
                replaceFragment(new checker_fragment());
                binding.buttonNav.setSelectedItemId(R.id.patientchecker);
            }
            else{
                replaceFragment(new dashboard_fragment());
                binding.buttonNav.setSelectedItemId(R.id.Dashboard);
            }
        }

        // Set up navigation listener
        binding.buttonNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
           if(itemId == R.id.Dashboard){
               replaceFragment(new dashboard_fragment());
           } else if (itemId == R.id.home) {
                replaceFragment(new home1_fragment());
            }
           else if (itemId == R.id.patientchecker) {
                replaceFragment(new checker_fragment());
            }
           else if (itemId == R.id.reminder) {
                replaceFragment(new reminder_fragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new profile_fragment());
            }
            return true;
        });

        //add the condition here
        showNavigationTutorial();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Framelayout, fragment); // Ensure R.id.fragment_container matches your layout ID
        fragmentTransaction.commit();
    }
    private void showNavigationTutorial(){
      new TapTargetSequence(this)
              .targets(
                      TapTarget.forView(findViewById(R.id.Dashboard), "Dashboard", "View your dashboard.")
                              .outerCircleColor(R.color.colorAccent)
                              .targetCircleColor(android.R.color.white)
                              .titleTextSize(20)
                              .descriptionTextSize(16)
                              .cancelable(false)
                              .tintTarget(true)
                              .drawShadow(true),
                      TapTarget.forView(findViewById(R.id.home), "Community", "Stay updated with RHU's and Barangay Health Centers posts.")
                              .outerCircleColor(R.color.teal_700)
                              .targetCircleColor(android.R.color.white)
                              .titleTextSize(20)
                              .descriptionTextSize(16)
                              .cancelable(false)
                              .tintTarget(true)
                              .drawShadow(true),

                      TapTarget.forView(findViewById(R.id.patientchecker), "Analyzer", "Analyze symptoms and get suggestions.")
                              .outerCircleColor(R.color.red)
                              .targetCircleColor(android.R.color.white)
                              .titleTextSize(20)
                              .descriptionTextSize(16)
                              .cancelable(false)
                              .tintTarget(true)
                              .drawShadow(true),

                      TapTarget.forView(findViewById(R.id.reminder), "Reminders", "Manage your medicine reminders.")
                              .outerCircleColor(R.color.colorGreen)
                              .targetCircleColor(android.R.color.white)
                              .titleTextSize(20)
                              .descriptionTextSize(16)
                              .cancelable(false)
                              .tintTarget(true)
                              .drawShadow(true),

                      TapTarget.forView(findViewById(R.id.profile), "Profile", "Update your personal information.")
                              .outerCircleColor(R.color.teal_dark)
                              .targetCircleColor(android.R.color.white)
                              .titleTextSize(20)
                              .descriptionTextSize(16)
                              .cancelable(false)
                              .tintTarget(true)
                              .drawShadow(true)
              )
              .listener(new TapTargetSequence.Listener() {
                  @Override
                  public void onSequenceFinish() {
                      Log.e("Tutorial", "Tutorial finished. Shared preference updated.");
                      SharedPreferences.Editor editor = sharedPreferences.edit();
                      editor.putBoolean("finishTabBarTutorial", true);
                      editor.apply();
                  }

                  @Override
                  public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                  }

                  @Override
                  public void onSequenceCanceled(TapTarget lastTarget) {
                      Toast.makeText(Menubar.this, "Tutorial skipped!", Toast.LENGTH_SHORT).show();
                  }
              })
              .start();
    }
}
