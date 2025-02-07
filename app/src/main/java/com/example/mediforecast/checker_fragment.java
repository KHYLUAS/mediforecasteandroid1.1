package com.example.mediforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.button.MaterialButton;


public class checker_fragment extends Fragment {
    private MaterialButton started;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checker_fragment, container, false);
        started = view.findViewById(R.id.started);

//        sharedPreferences = requireActivity().getSharedPreferences("TapTargetsPrefs", Context.MODE_PRIVATE);
//        boolean isMainTutorialFinished = sharedPreferences.getBoolean("finishTabBarTutorial", false);
//
//        if(isMainTutorialFinished && !isAnalyzerTutorialFinished()){
//            showAnalyzerTutorial();
//        }

        started.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), SymptomAnalyzer.class);
            startActivity(intent);
        });
        return view;
    }
//    private boolean isAnalyzerTutorialFinished() {
//        // Check if the dashboard tutorial is finished
//        return sharedPreferences.getBoolean("finishAnalyzerTutorial", false);
//    }
//
//    private void showAnalyzerTutorial() {
//        TapTargetView.showFor(getActivity(),
//                TapTarget.forView(getView().findViewById(R.id.started),
//                                "Symptom Analyzer",
//                                "To analyze your symptoms, tap this ‘Get Started’ button. Simply enter your symptoms, and our system will provide insights to help you understand possible causes and next steps.")
//                        .outerCircleColor(R.color.colorAccent) // Outer circle color
//                        .targetCircleColor(android.R.color.white) // Target circle color
//                        .titleTextSize(20) // Title text size
//                        .descriptionTextSize(16) // Description text size
//                        .outerCircleAlpha(0.96f) // Outer circle alpha
//                        .transparentTarget(false) // Show the target fully
//                        .cancelable(true) // Allow user to cancel
//                        .drawShadow(true) // Show shadow
//                        .tintTarget(true) // Tint the target
//                        .dimColor(android.R.color.black), // Dim the background
//                new TapTargetView.Listener() {
//                    @Override
//                    public void onTargetClick(TapTargetView view) {
//                        super.onTargetClick(view);
//                        // Mark the dashboard tutorial as finished after it is clicked
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean("finishAnalyzerTutorial", true);
//                        editor.apply();
//                    }
//                });
//    }
}