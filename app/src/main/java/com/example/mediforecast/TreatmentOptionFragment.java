package com.example.mediforecast;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class TreatmentOptionFragment extends Fragment {
    private TextView tvFragment, trText, preventText;
    public static String textUrl = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treatment_option, container, false);


        trText = view.findViewById(R.id.trText);
        preventText = view.findViewById(R.id.preventText);

        Bundle arguments = getArguments();
        if(arguments != null){
            String diagnoseName = arguments.getString("diagnoseName");
            loadDiagnoseDetails(diagnoseName);
        }

        return view;
    }
    private void loadDiagnoseDetails(String diagnoseName) {
        try {
            // Load JSON file from assets
            String json = loadJSONFromAsset("diagnose_details.json");

            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray diagnosisArray = jsonObject.getJSONArray("diagnosis");

                // Iterate through the JSON array
                for (int i = 0; i < diagnosisArray.length(); i++) {
                    JSONObject diagnose = diagnosisArray.getJSONObject(i);

                    // Check if the diagnose name matches
                    if (diagnose.getString("name").equalsIgnoreCase(diagnoseName)) {
                        trText.setText(diagnose.getString("treatment option"));
                        preventText.setText(diagnose.getString("prevention"));

                        return;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TreatmentOption", "Error loading JSON", e);
        }
    }
    private String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            AssetManager assetManager = requireActivity().getAssets();
            InputStream inputStream = assetManager.open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e("TreatmentOption", "Error reading JSON file", e);
        }
        return json;
    }
}