package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;


public class checker_fragment extends Fragment {
    private MaterialButton started;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checker_fragment, container, false);
        started = view.findViewById(R.id.started);

        started.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), SymptomActivity.class);
            startActivity(intent);
        });
        return view;
    }
}