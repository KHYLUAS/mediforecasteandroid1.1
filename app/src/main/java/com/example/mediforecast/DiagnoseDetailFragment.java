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


public class DiagnoseDetailFragment extends Fragment {
    private TextView tvFragment, diagnoseNames, descriptionTitle, descriptionText, title1, text1, title2, text2, title3, text3, title4, text4;
    private String url;
    public static String textUrl = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diagnose_detail, container, false);


        diagnoseNames = view.findViewById(R.id.symptomName);
        descriptionTitle = view.findViewById(R.id.descriptionTitle);
        descriptionText = view.findViewById(R.id.descriptionText);
        title1 = view.findViewById(R.id.title1);
        text1 = view.findViewById(R.id.text1);
        title2 = view.findViewById(R.id.title2);
        text2 = view.findViewById(R.id.text2);
        title3 = view.findViewById(R.id.title3);
        text3 = view.findViewById(R.id.text3);
        title4 = view.findViewById(R.id.title4);
        text4 = view.findViewById(R.id.text4);

        Bundle arguments = getArguments();
        if (arguments != null) {
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
                        // Populate the UI components
                        diagnoseNames.setText(diagnose.getString("name"));
                        descriptionText.setText(diagnose.getString("description"));
                        title1.setText(diagnose.getString("title1"));
                        text1.setText(diagnose.getString("text1"));
                        title2.setText(diagnose.getString("title2"));
                        text2.setText(diagnose.getString("text2"));
                        title3.setText(diagnose.getString("title3"));
                        text3.setText(diagnose.getString("text3"));
                        title4.setText(diagnose.getString("title4"));
                        text4.setText(diagnose.getString("text4"));
                        url = diagnose.getString("link");

                        diagnoseNames.setOnClickListener(v->{
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        });
                        return;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("DiagnoseDetail", "Error loading JSON", e);
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
            Log.e("DiagnoseDetail", "Error reading JSON file", e);
        }
        return json;
    }
}