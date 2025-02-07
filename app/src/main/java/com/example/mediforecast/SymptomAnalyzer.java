package com.example.mediforecast;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SymptomAnalyzer extends AppCompatActivity {
    private JSONArray symptomsArray;
    private LinearLayout symptomLayout, questionLayout, diagnosisLayout, selectedSymptomsLayout;
    private EditText searchSymptomEditText;
    private Set<Integer> selectedSymptomIds = new HashSet<>();
    private List<JSONObject> combinedQuestions = new ArrayList<>();
    private Map<String, List<String>> diagnosisSymptomsMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_symptom_analyzer);
        symptomLayout = findViewById(R.id.symptomLayout);
        questionLayout = findViewById(R.id.questionLayout);
        diagnosisLayout = findViewById(R.id.diagnosisLayout);
        selectedSymptomsLayout = findViewById(R.id.selectedSymptomsLayout); // LinearLayout to display selected symptoms
        searchSymptomEditText = findViewById(R.id.searchSymptomEditText);

        loadSymptomsFromJson();

        searchSymptomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSymptoms(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadSymptomsFromJson() {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("symptoms_and_diagnoses.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            symptomsArray = jsonObject.getJSONArray("symptoms");

            // Initialize the mapping of symptoms to diagnoses
            diagnosisSymptomsMap = DiagnosisMapping.getDiagnosisSymptomsMap();

            displaySymptoms("");
        } catch (IOException | JSONException e) {
            Toast.makeText(this, "Failed to load symptoms.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void displaySymptoms(String query) {
        symptomLayout.removeAllViews();
        try {
            // Convert JSONArray to a List of JSONObject for sorting
            List<JSONObject> symptomList = new ArrayList<>();
            for (int i = 0; i < symptomsArray.length(); i++) {
                symptomList.add(symptomsArray.getJSONObject(i));
            }

            // Sort the list alphabetically by the "name" field
            symptomList.sort((symptom1, symptom2) -> {
                try {
                    return symptom1.getString("name").compareToIgnoreCase(symptom2.getString("name"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            // Iterate over the sorted list and display symptoms
            for (JSONObject symptom : symptomList) {
                String symptomName = symptom.getString("name");

                // Filter based on the query
                if (!query.isEmpty() && !symptomName.toLowerCase().contains(query.toLowerCase())) {
                    continue;
                }

                LinearLayout symptomItemLayout = new LinearLayout(this);
                symptomItemLayout.setOrientation(LinearLayout.HORIZONTAL);
                symptomItemLayout.setPadding(16, 16, 16, 16);

                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(symptomName);
                checkBox.setTextSize(18);
                checkBox.setTag(symptom.getInt("id"));
                checkBox.setPadding(0, 0, 16, 0);
                checkBox.setOnClickListener(v -> {
                    int symptomId = (int) checkBox.getTag();
                    if (checkBox.isChecked()) {
                        selectedSymptomIds.add(symptomId);
                        addSelectedSymptom(symptomId, symptomName);
                    } else {
                        selectedSymptomIds.remove(symptomId);
                        removeSelectedSymptom(symptomId);
                    }
                    updateQuestionsAndDiagnosis();
                });

                ImageView eyeIcon = new ImageView(this);
                eyeIcon.setImageResource(R.drawable.eye_ic);
                eyeIcon.setContentDescription("View Details");
                eyeIcon.setClickable(true);
                eyeIcon.setFocusable(true);

                // Add click listener for the eye icon
                eyeIcon.setOnClickListener(v -> {
                    Log.d("SymptomActivity", "Eye icon clicked for symptom: " + symptomName);
                    Toast.makeText(this, "Viewing details for: " + symptomName, Toast.LENGTH_SHORT).show();

                    // Navigate to the details page
                    Intent intent = new Intent(this, ViewDetails.class);
                    try {
                        intent.putExtra("symptomId", symptom.getInt("id"));
                        intent.putExtra("description", symptom.getString("description"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    intent.putExtra("symptomName", symptomName);

                    startActivity(intent);
                });

                symptomItemLayout.addView(checkBox);

                View spacer = new View(this);
                LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                spacer.setLayoutParams(spacerParams);
                symptomItemLayout.addView(spacer);

                symptomItemLayout.addView(eyeIcon);

                View divider = new View(this);
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);
                divider.setLayoutParams(dividerParams);
                divider.setBackgroundColor(getResources().getColor(R.color.gray));

                symptomLayout.addView(symptomItemLayout);
                symptomLayout.addView(divider);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void filterSymptoms(String query) {
        displaySymptoms(query);
    }

    private void updateQuestionsAndDiagnosis() {
        questionLayout.removeAllViews();
        diagnosisLayout.removeAllViews();
        combinedQuestions.clear();

        if (selectedSymptomIds.isEmpty()) {
            Toast.makeText(this, "Please select at least one symptom.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Set<String> selectedSymptoms = new HashSet<>();
            for (int symptomId : selectedSymptomIds) {
                for (int i = 0; i < symptomsArray.length(); i++) {
                    JSONObject symptom = symptomsArray.getJSONObject(i);
                    if (symptom.getInt("id") == symptomId) {
                        selectedSymptoms.add(symptom.getString("name"));
                        JSONArray questions = symptom.getJSONArray("questions");
                        for (int j = 0; j < questions.length(); j++) {
                            combinedQuestions.add(questions.getJSONObject(j));
                        }
                    }
                }
            }
            displayFollowUpQuestions();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayFollowUpQuestions() {
        for (JSONObject questionObj : combinedQuestions) {
            try {
                String question = questionObj.getString("question");
                JSONArray options = questionObj.getJSONArray("options");

                // Create and add the question text view
                TextView questionTextView = new TextView(this);
                questionTextView.setText(question);
                questionLayout.addView(questionTextView);

                // Create and add the radio group for options
                RadioGroup radioGroup = new RadioGroup(this);
                for (int i = 0; i < options.length(); i++) {
                    String option = options.getString(i);
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(option);
                    radioGroup.addView(radioButton);
                }
                questionLayout.addView(radioGroup);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Create and configure the submit button
        Button submitButton = new Button(this);
        submitButton.setText("Submit");

// Create a GradientDrawable to set background color and corner radius
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#00897B")); // Set the background color (example: orange)
        drawable.setCornerRadius(40); // Set corner radius in pixels
        ColorStateList rippleColor = ColorStateList.valueOf(Color.parseColor("#28bca9")); // Example: Semi-transparent white

// Create a RippleDrawable with the ripple color and base background
        RippleDrawable rippleDrawable = new RippleDrawable(rippleColor, drawable, null);
        submitButton.setTextColor(Color.parseColor("#FFFFFFFF")); // Example: White color
        submitButton.setTextSize(17);
// Apply the drawable as the background of the button
        submitButton.setBackground(drawable);

        submitButton.setOnClickListener(v -> {
            // Collect selected symptoms
            Set<String> selectedSymptoms = new HashSet<>();
            for (int symptomId : selectedSymptomIds) {
                for (int i = 0; i < symptomsArray.length(); i++) {
                    try {
                        JSONObject symptom = symptomsArray.getJSONObject(i);
                        if (symptom.getInt("id") == symptomId) {
                            selectedSymptoms.add(symptom.getString("name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Navigate to the result page with the selected symptoms
            navigateToDiagnosisResultPage(selectedSymptoms);
        });


        // Add the submit button to the layout
        questionLayout.addView(submitButton);
    }
    private void navigateToDiagnosisResultPage(Set<String> selectedSymptoms) {
        // Fetch diagnoses with their accuracies
        Map<String, Integer> diagnosesWithAccuracy = DiagnosisMapping.getDiagnosesWithAccuracy(selectedSymptoms);

        // Sort the entries in descending order of accuracy
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(diagnosesWithAccuracy.entrySet());
        sortedEntries.sort((entry1, entry2) -> entry2.getValue() - entry1.getValue());

        // Separate into two lists: top 5 and the rest
        List<String> topDiagnoses = new ArrayList<>();
        List<Integer> topAccuracyRates = new ArrayList<>();
        List<String> otherDiagnoses = new ArrayList<>();
        List<Integer> otherAccuracyRates = new ArrayList<>();

        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            if (count < 5) {
                topDiagnoses.add(entry.getKey());
                topAccuracyRates.add(entry.getValue());
            } else {
                otherDiagnoses.add(entry.getKey());
                otherAccuracyRates.add(entry.getValue());
            }
            count++;
        }

        // Create an intent to navigate to DiagnosisResultActivity
        Intent intent = new Intent(this, DiagnosisResult.class);

        // Pass the top 5 diagnoses and their accuracies
        intent.putStringArrayListExtra("topDiagnoses", new ArrayList<>(topDiagnoses));
        intent.putIntegerArrayListExtra("topAccuracyRates", new ArrayList<>(topAccuracyRates));

        // Pass the remaining diagnoses and their accuracies for the "See More" feature
        intent.putStringArrayListExtra("otherDiagnoses", new ArrayList<>(otherDiagnoses));
        intent.putIntegerArrayListExtra("otherAccuracyRates", new ArrayList<>(otherAccuracyRates));

        // Pass the selected symptoms
        intent.putStringArrayListExtra("selectedSymptoms", new ArrayList<>(selectedSymptoms));

        // Start the DiagnosisResult activity
        startActivity(intent);
    }





//    private void displayPossibleDiagnoses(Set<String> selectedSymptoms) {
//        diagnosisLayout.removeAllViews();
//        List<String> possibleDiagnoses = DiagnosisMapping.getDiagnosesWithAccuracy(selectedSymptoms);
//
//        if (possibleDiagnoses.isEmpty()) {
//            TextView noDiagnosisText = new TextView(this);
//            noDiagnosisText.setText("No diagnosis found for the selected symptoms.");
//            diagnosisLayout.addView(noDiagnosisText);
//        } else {
//            TextView diagnosisTextView = new TextView(this);
//            diagnosisTextView.setText("Possible Diagnoses:\n");
//
//            StringBuilder diagnosisBuilder = new StringBuilder();
//            int count = 0;
//            for (String diagnosis : possibleDiagnoses) {
//                if (count == 5) break;
//                diagnosisBuilder.append("- ").append(diagnosis).append("\n");
//                count++;
//            }
//
//            diagnosisTextView.append(diagnosisBuilder.toString());
//            diagnosisLayout.addView(diagnosisTextView);
//        }
//    }


    private void addSelectedSymptom(int symptomId, String symptomName) {
        LinearLayout selectedItemLayout = new LinearLayout(this);
        selectedItemLayout.setOrientation(LinearLayout.HORIZONTAL);
        selectedItemLayout.setBackgroundResource(R.drawable.rounded_border_symptom);
        selectedItemLayout.setPadding(16, 8, 16, 8);  // Keeps the padding for inside content
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 16); // Set margin bottom
        selectedItemLayout.setLayoutParams(layoutParams); // Apply margin to the layout

        TextView symptomTextView = new TextView(this);
        symptomTextView.setText(symptomName);
        symptomTextView.setTextSize(16);
        symptomTextView.setPadding(1, 5, 3, 0);
        symptomTextView.setTextColor(Color.WHITE);

        ImageView removeIcon = new ImageView(this);
        removeIcon.setImageResource(R.drawable.remove_ic);
        removeIcon.setLayoutParams(new LinearLayout.LayoutParams(62, 62));
        removeIcon.setPadding(0, 0, 8, 0);
        removeIcon.setTag(symptomId); // Set the tag for later reference

        // Set remove icon click listener
        removeIcon.setOnClickListener(v -> {
            selectedSymptomIds.remove(symptomId);
            selectedSymptomsLayout.removeView(selectedItemLayout);
            uncheckSymptomCheckbox(symptomId);
            updateQuestionsAndDiagnosis();
        });

        selectedItemLayout.addView(removeIcon);
        selectedItemLayout.addView(symptomTextView);
        selectedSymptomsLayout.addView(selectedItemLayout);
    }


    private void removeSelectedSymptom(int symptomId) {
        for (int i = 0; i < selectedSymptomsLayout.getChildCount(); i++) {
            View child = selectedSymptomsLayout.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout selectedItemLayout = (LinearLayout) child;
                ImageView removeIcon = (ImageView) selectedItemLayout.getChildAt(0);
                if (removeIcon.getTag() != null && removeIcon.getTag().equals(symptomId)) {
                    selectedSymptomsLayout.removeViewAt(i);
                    break;
                }
            }
        }
    }

    private void uncheckSymptomCheckbox(int symptomId) {
        for (int i = 0; i < symptomLayout.getChildCount(); i++) {
            View child = symptomLayout.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout itemLayout = (LinearLayout) child;
                for (int j = 0; j < itemLayout.getChildCount(); j++) {
                    View innerChild = itemLayout.getChildAt(j);
                    if (innerChild instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) innerChild;
                        if (checkBox.getTag() != null && checkBox.getTag().equals(symptomId)) {
                            checkBox.setChecked(false);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void setCheckboxListener(CheckBox checkBox, int symptomId) {
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Add symptom to the selected layout if checked
                addSelectedSymptom(symptomId, checkBox.getText().toString());
            } else {
                // Remove symptom from the selected layout if unchecked
                removeSelectedSymptom(symptomId);
            }
        });
    }
}