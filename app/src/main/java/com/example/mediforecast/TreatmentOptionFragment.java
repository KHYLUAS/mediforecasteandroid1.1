package com.example.mediforecast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TreatmentOptionFragment extends Fragment {
    private TextView tvFragment;
    public static String textUrl = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treatment_option, container, false);


        tvFragment.setText(textUrl);

        return view;
    }
}