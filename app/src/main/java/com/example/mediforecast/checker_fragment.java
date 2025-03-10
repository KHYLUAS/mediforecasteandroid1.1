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

        sharedPreferences = requireActivity().getSharedPreferences("TapTargetsPrefs", Context.MODE_PRIVATE);
        boolean isAnalyzerTutorialFinished = sharedPreferences.getBoolean("finishAnalyzerTutorial", false);
        // Show TapTarget tutorial only once
        if (!isAnalyzerTutorialFinished) {
            showAnalyzerTutorial();
        }

        started.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), SymptomAnalyzer.class);
            startActivity(intent);
        });
        return view;
    }
    private void showAnalyzerTutorial() {
        TapTargetView.showFor(getActivity(),
                TapTarget.forView(started, "Symptom Analyzer",
                                "Tap ‘Get Started’ to analyze your symptoms and get insights.")
                        .outerCircleColor(R.color.black)
                        .targetCircleColor(android.R.color.white)
                        .titleTextSize(20)
                        .descriptionTextSize(16)
                        .outerCircleAlpha(0.96f)
                        .transparentTarget(true)
                        .cancelable(true)
                        .drawShadow(true)
                        .tintTarget(false)
                        .dimColor(R.color.black),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        // Save the flag in SharedPreferences to prevent showing again
                        sharedPreferences.edit().putBoolean("finishAnalyzerTutorial", true).apply();
                    }
                });
    }
}