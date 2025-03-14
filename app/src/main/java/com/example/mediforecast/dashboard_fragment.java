package com.example.mediforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class dashboard_fragment extends Fragment {

    private ImageView communitypostIV, medicinereminderIV, selfchecker;
    private LinearLayout symptomHistoryLayout;
    private FirebaseFirestore firestore;
    private TextView seeAll;
    private SharedPreferences sharedPreferences;
    private final Handler handler = new Handler();
    private boolean isChecking = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_fragment, container, false);

        // Initialize the views
        communitypostIV = view.findViewById(R.id.communitypost);
        medicinereminderIV = view.findViewById(R.id.medicinereminder);
        selfchecker = view.findViewById(R.id.selfchecker);
        symptomHistoryLayout = view.findViewById(R.id.symptomHistoryLayout);
        seeAll = view.findViewById(R.id.seeAll);
        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

//
//        // If main tutorial is finished, check if the dashboard tutorial is not finished
//        if (sharedPreferences.getBoolean("finishTabBarTutorial", false) && !isDashboardTutorialFinished()) {
//            showDashboardTutorial();
//        }
        checkTutorialCompletion();

        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SeeAllHistory.class);
                startActivity(intent);
            }
        });
        // Set onClickListeners for the ImageViews
        communitypostIV.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "COMMUNITYDB");
            startActivity(intent);
        });

        medicinereminderIV.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "REMINDERDB");
            startActivity(intent);
        });

        selfchecker.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "ANALYZER");
            startActivity(intent);
        });

        // Fetch and display the symptom history
        createSymptomHeader(); // Create the header first
        fetchSymptomHistory(); // Fetch and display records
        return view;
    }
    private boolean isDashboardTutorialFinished() {
        // Check if the dashboard tutorial is finished
        return sharedPreferences.getBoolean("finishDashboardTutorial", false);
    }

    private void checkTutorialCompletion() {
        sharedPreferences = requireActivity().getSharedPreferences("TapTargetsPrefs", Context.MODE_PRIVATE);

        boolean isTutorialShown = sharedPreferences.getBoolean("dashboardTutorialShown", false);
        if (!isTutorialShown) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean finishTabBarTutorial = sharedPreferences.getBoolean("finishTabBarTutorial", false);

                    if (finishTabBarTutorial) {
                        Log.d("TutorialCheck", "Tutorial finished detected in another page! Performing action...");
                        performAction(); // Show tutorial only once after installation
                        isChecking = false; // Stop checking
                        return;
                    }

                    // Continue checking every second if not finished
                    if (isChecking) {
                        handler.postDelayed(this, 1000); // Repeat every second
                    }
                }
            }, 1000); // Start after 1 second
        }
    }

    private void showDashboardTutorial() {
        if (getActivity() == null || seeAll == null) return;

        TapTargetView.showFor(getActivity(),
                TapTarget.forView(seeAll,
                                "See All Button",
                                "Tap to view all symptom history")
                        .outerCircleColor(R.color.colorAccent) // Outer circle color
                        .targetCircleColor(android.R.color.white) // Target circle color
                        .titleTextSize(20) // Title text size
                        .descriptionTextSize(16) // Description text size
                        .outerCircleAlpha(0.96f) // Outer circle alpha
                        .transparentTarget(false) // Show the target fully
                        .cancelable(true) // Allow user to cancel
                        .drawShadow(true) // Show shadow
                        .tintTarget(true) // Tint the target
                        .dimColor(android.R.color.black), // Dim the background
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        // Mark the tutorial as finished permanently
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("dashboardTutorialShown", true);
                        editor.apply();
                    }
                });
    }

    private void performAction() {
      showDashboardTutorial();

    }
    private void fetchSymptomHistory() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyInfo", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("EMAIL", null);

        // Check if email is available
        if (savedEmail == null) {
            Toast.makeText(getActivity(), "No user email found", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("SymptomAnalyzer")
                .whereEqualTo("email", savedEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String documentId = document.getId();
                                String symptoms = document.getString("symptoms");
                                Timestamp timestamp = document.getTimestamp("dateAndTime");
                                String formattedDate = formatDate(timestamp);
                                createSymptomRow(formattedDate, symptoms, documentId);
                            }
                        } else {
                            Toast.makeText(getActivity(), "No symptom records found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error fetching data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String formatDate(Timestamp timestamp) {
        if (timestamp == null) {
            return "Unknown Date"; // Handle null timestamp gracefully
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy hh:mm a", Locale.getDefault());
        Date date = timestamp.toDate();
        return sdf.format(date);
    }

    private void createSymptomHeader() {
        if (getActivity() == null) {
            return; // Exit if context is not available
        }

        LinearLayout headerLayout = new LinearLayout(getActivity());
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        headerLayout.setPadding(20, 20, 20, 20);

        // Add TextView for Date Header
        TextView dateHeader = new TextView(getActivity());
        dateHeader.setText("Date");
        dateHeader.setTextSize(18f);
        dateHeader.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f)); // Weight 1

        // Add TextView for Pain Location Header
        TextView painLocationHeader = new TextView(getActivity());
        painLocationHeader.setText("Symptoms");
        painLocationHeader.setTextSize(18f);
        painLocationHeader.setTextColor(getResources().getColor(R.color.primaryColor));
        painLocationHeader.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1)); // Weight 1

        painLocationHeader.setTypeface(null, Typeface.BOLD);
        painLocationHeader.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        // Add to the header layout
        headerLayout.addView(dateHeader);
        headerLayout.addView(painLocationHeader);

        // Add to the parent layout
        symptomHistoryLayout.addView(headerLayout);
    }

    private void createSymptomRow(String date, String symptoms, String documentId) {
        // Create a container for the row
        LinearLayout rowLayout = new LinearLayout(getActivity());
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setPadding(20, 20, 20, 20);
        rowLayout.setBackgroundResource(R.drawable.border_hover);
        rowLayout.setClickable(true);

        // Add a TextView for the date
        TextView dateView = new TextView(getActivity());
        dateView.setText(date);
        dateView.setTextSize(16f);
        dateView.setSingleLine(true);
        dateView.setEllipsize(TextUtils.TruncateAt.END); // Optional: add ellipsis
        dateView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f)); // Assign more weight to date

        // Add a TextView for the pain location
        TextView painLocationView = new TextView(getActivity());
        painLocationView.setText(symptoms);
        painLocationView.setTextSize(16f);
        painLocationView.setSingleLine(true);
        painLocationView.setEllipsize(TextUtils.TruncateAt.END);
        painLocationView.setTextColor(getResources().getColor(R.color.primaryColor));
        painLocationView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)); // Assign slightly less weight to pain location

        painLocationView.setTypeface(null, Typeface.BOLD);

        painLocationView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        // Add TextViews to the row
        rowLayout.addView(dateView);
        rowLayout.addView(painLocationView);
    // Set margin at the bottom of each row
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rowLayout.getLayoutParams();
            if (params == null) {
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        params.setMargins(0, 0, 0, 7);  // Set bottom margin of 10dp
        rowLayout.setLayoutParams(params);
        // Add the row to the parent layout
        rowLayout.setOnClickListener(v -> {
            // Handle row click (e.g., open a detailed view or perform other actions)
            Toast.makeText(getActivity(), "Clicked: " + documentId, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), DiagnosisResult.class);
            intent.putExtra("documentId", documentId);
            intent.putExtra("isViewing", true);
            startActivity(intent);
        });

        symptomHistoryLayout.addView(rowLayout);
    }

}
