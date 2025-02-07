package com.example.mediforecast;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DiagnosisResult extends AppCompatActivity {
    private TextView allSymptoms;
    private boolean showAll = false;
    private ImageView arrow;
    private MaterialButton save, discard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diagnosis_result);

        discard = findViewById(R.id.discard);
        arrow = findViewById(R.id.arrow);
        allSymptoms = findViewById(R.id.all_symptoms);
        save = findViewById(R.id.save);


        AlertDialog.Builder ab = new AlertDialog.Builder(DiagnosisResult.this);
        ab.setIcon(R.drawable.error_icon).setTitle("Disclaimer").setMessage(R.string.note)
                .setNegativeButton("Ok", (dialog, which)->{

                });
        AlertDialog dialog = ab.create();
        dialog.show();

        // Get the passed data
        ArrayList<String> topDiagnoses = getIntent().getStringArrayListExtra("topDiagnoses");
        ArrayList<Integer> topAccuracyRates = getIntent().getIntegerArrayListExtra("topAccuracyRates");
        ArrayList<String> otherDiagnoses = getIntent().getStringArrayListExtra("otherDiagnoses");
        ArrayList<Integer> otherAccuracyRates = getIntent().getIntegerArrayListExtra("otherAccuracyRates");
        ArrayList<String> selectedSymptoms = getIntent().getStringArrayListExtra("selectedSymptoms");

        if (selectedSymptoms != null && !selectedSymptoms.isEmpty()) {
            String formattedSymptoms = String.join(", ", selectedSymptoms);
            allSymptoms.setText(formattedSymptoms);
        } else {
            allSymptoms.setText("No symptoms selected.");
        }
        arrow.setOnClickListener(v->{
            Intent intents = new Intent(DiagnosisResult.this, SymptomAnalyzer.class);
            startActivity(intents);
            finish();
        });
        // Log the data to check if it's null or missing
        Log.d("DiagnosisResult", "Top Diagnoses: " + topDiagnoses);
        Log.d("DiagnosisResult", "Top Accuracy Rates: " + topAccuracyRates);
        Log.d("DiagnosisResult", "Other Diagnoses: " + otherDiagnoses);
        Log.d("DiagnosisResult", "Other Accuracy Rates: " + otherAccuracyRates);

        // Reference to the layout and button
        LinearLayout diagnosisLayout = findViewById(R.id.diagnosisLayout);
        Button seeMoreButton = findViewById(R.id.seeMoreButton);

        // Method to display diagnoses
        if (topDiagnoses != null && topAccuracyRates != null) {
            displayDiagnoses(diagnosisLayout, topDiagnoses, topAccuracyRates, otherDiagnoses, otherAccuracyRates);
        } else {
            Log.e("DiagnosisResult", "Error: Data is null.");
        }

        // Toggle "See More" functionality
        seeMoreButton.setOnClickListener(v -> {
            showAll = !showAll;
            if (topDiagnoses != null && topAccuracyRates != null) {
                displayDiagnoses(diagnosisLayout, topDiagnoses, topAccuracyRates, otherDiagnoses, otherAccuracyRates);
            }
            seeMoreButton.setText(showAll ? "See Less" : "See More");
        });
        discard.setOnClickListener(v->{
            new AlertDialog.Builder(DiagnosisResult.this)
                    .setTitle("Discard")
                    .setMessage("Do you want to discard this diagnosis?")
                    .setPositiveButton("Yes", (dialogs, which) -> {
                        Intent intent = new Intent(DiagnosisResult.this, SymptomAnalyzer.class);
                        startActivity(intent);
                        finish();
                    }).setNegativeButton("No", (dialogs, which) ->{

                    }).show();
        });
        save.setOnClickListener(v -> {
            // Show confirmation dialog before saving
            new AlertDialog.Builder(DiagnosisResult.this)
                    .setTitle("Confirm Save")
                    .setMessage("Do you want to save this diagnosis?")
                    .setPositiveButton("Yes", (dialogs, which) -> {
                        // Initialize Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        SharedPreferences sharedPreferences = getSharedPreferences("MyInfo", Context.MODE_PRIVATE);
                        // Retrieve email and password
                        String savedEmail = sharedPreferences.getString("EMAIL", null);

                        // Get current date and time
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
                        String currentDateTime = dateFormat.format(new Date());

                        // Convert selected symptoms list to a string
                        String symptomsString = selectedSymptoms != null ? String.join(", ", selectedSymptoms) : "No symptoms selected";

                        // Prepare a list of diagnoses with accuracy rates
                        List<Map<String, Object>> diagnosisResultsList = new ArrayList<>();
                        if (topDiagnoses != null && topAccuracyRates != null) {
                            for (int i = 0; i < topDiagnoses.size(); i++) {
                                Map<String, Object> diagnosisEntry = new HashMap<>();
                                diagnosisEntry.put("diagnosis", topDiagnoses.get(i));
                                diagnosisEntry.put("accuracyRate", topAccuracyRates.get(i)); // Save accuracy rate as a number
                                diagnosisResultsList.add(diagnosisEntry);
                            }
                        }

                        // Create a data map for Firestore
                        HashMap<String, Object> diagnosisData = new HashMap<>();
                        diagnosisData.put("email", savedEmail);
                        diagnosisData.put("dateTime", currentDateTime);
                        diagnosisData.put("symptoms", symptomsString);
                        diagnosisData.put("diagnosisResults", diagnosisResultsList); // Store diagnoses with accuracy

                        // Save to Firestore
                        db.collection("SymptomAnalyzer")
                                .add(diagnosisData)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(DiagnosisResult.this, "Diagnosis saved successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(DiagnosisResult.this, "Error saving diagnosis", Toast.LENGTH_SHORT).show();
                                    Log.e("FirestoreError", "Error saving data", e);
                                });
                    })
                    .setNegativeButton("No", (dialogs, which) -> {
                        dialog.dismiss(); // Close dialog
                    })
                    .show();

        });

    }
    private void displayDiagnoses(LinearLayout layout,
                                  ArrayList<String> topDiagnoses,
                                  ArrayList<Integer> topAccuracyRates,
                                  ArrayList<String> otherDiagnoses,
                                  ArrayList<Integer> otherAccuracyRates) {
        layout.removeAllViews(); // Clear existing views

        try {
            // Add top diagnoses
            if (topDiagnoses != null && topAccuracyRates != null) {
                for (int i = 0; i < topDiagnoses.size(); i++) {
                    LinearLayout diagnosisItem = createDiagnosisView(topDiagnoses.get(i), topAccuracyRates.get(i));
                    layout.addView(diagnosisItem);
                }
            }

            // Show more diagnoses if toggled
            if (showAll && otherDiagnoses != null && otherAccuracyRates != null) {
                for (int i = 0; i < otherDiagnoses.size(); i++) {
                    LinearLayout diagnosisItem = createDiagnosisView(otherDiagnoses.get(i), otherAccuracyRates.get(i));
                    layout.addView(diagnosisItem);
                }
            }
        } catch (Exception e) {
            Log.e("DiagnosisResult", "Error while displaying diagnoses: ", e);
        }
    }

    private LinearLayout createDiagnosisView(String diagnosis, int accuracy) {
        // Parent layout for the entire item
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        itemLayout.setPadding(16, 16, 16, 16); // Padding for layout
        itemLayout.setGravity(Gravity.CENTER_VERTICAL);

        // Vertical layout for diagnosis and strength
        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f // Weight to take up available space
        ));

        // Diagnosis TextView
        TextView diagnosisView = new TextView(this);
        diagnosisView.setText(diagnosis);
        diagnosisView.setTextSize(18); // Larger font for diagnosis
        diagnosisView.setTypeface(null, Typeface.BOLD); // Bold text
        diagnosisView.setTextColor(getResources().getColor(android.R.color.black));
        diagnosisView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Accuracy TextView
        TextView accuracyView = new TextView(this);
        accuracyView.setText("Match: " + accuracy + "%");
        accuracyView.setTextSize(14); // Smaller font for accuracy
        accuracyView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        accuracyView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Add diagnosis and strength to the vertical layout
        textLayout.addView(diagnosisView);
        textLayout.addView(accuracyView);

        // Icon ImageView
        ImageView iconView = new ImageView(this);
        iconView.setImageResource(R.drawable.arrow_right); // Use your drawable resource
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        iconParams.setMargins(16, 0, 0, 0); // Add margin to the left of the icon
        iconView.setLayoutParams(iconParams);

        // Add text layout and icon to the horizontal parent layout
        itemLayout.addView(textLayout);
        itemLayout.addView(iconView);

        // Bottom border for separation
        View bottomBorder = new View(this);
        LinearLayout.LayoutParams borderParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 2 // Full width, 2px height
        );
        bottomBorder.setLayoutParams(borderParams);
        bottomBorder.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        // Final layout with bottom border
        LinearLayout containerLayout = new LinearLayout(this);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.addView(itemLayout);
        containerLayout.addView(bottomBorder);

        // Apply background selector for click feedback
        containerLayout.setBackgroundResource(R.drawable.diagnosis_item_selector);

        // Make the whole layout clickable
        containerLayout.setClickable(true);
        containerLayout.setFocusable(true);
        containerLayout.setOnClickListener(v -> {
            Toast.makeText(DiagnosisResult.this, "Diagnosis: " + diagnosis, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DiagnoseAndTreatment.class);
            intent.putExtra("diagnoseName", diagnosis);
            startActivity(intent);
        });

        return containerLayout;
    }
}