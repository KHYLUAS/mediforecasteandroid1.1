package com.example.mediforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class reminder_fragment extends Fragment {

    private FirebaseFirestore firestore;
    private MedicineRepository medicineRepository;
    private RecyclerView recyclerView;
    private ImageView addButton, noDataImage;
    private MedicineAdapter adapter;
    Medicine medicine;
    private Map<String, Integer> dayOfWeekMap;
    private TextView  noDataText;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicines;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        medicineRepository = new MedicineRepository(requireActivity().getApplication());
        medicines = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_fragment, container, false);


        noDataImage = view.findViewById(R.id.noDataImage);
        noDataText = view.findViewById(R.id.noDataText);
        addButton = view.findViewById(R.id.Addimage);
        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.taskRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with empty list
        adapter = new MedicineAdapter(getContext(), null, medicineRepository);
        recyclerView.setAdapter(adapter);

        sharedPreferences = requireActivity().getSharedPreferences("TapTargetsPrefs", Context.MODE_PRIVATE);
        boolean isReminderTutorialFinished = sharedPreferences.getBoolean("finishReminderTutorial", false);
        // Set up Add button
        addButton.setOnClickListener(v -> {
            // Start activity to add a new medicine
            Intent intent = new Intent(getContext(), medicine_signin.class);
            startActivity(intent);
        });

        // Show TapTarget tutorial only once
        if (!isReminderTutorialFinished) {
            showReminderTutorial();
        }



        // Observe the LiveData and update the adapter when data changes
        medicineRepository.getAllMedicines().observe(getViewLifecycleOwner(), new Observer<List<Medicine>>() {
            @Override
            public void onChanged(List<Medicine> updatedMedicines) {
                medicines.clear(); // Clear the previous list
                medicines.addAll(updatedMedicines); // Add the new data
                adapter.setMedicines(medicines);
                updateEmptyState(); // Check if the list is empty
            }
        });
        return view;
    }
    private void showReminderTutorial() {
        if (addButton == null) {
            Log.e("ReminderFragment", "addButton is still null, skipping tutorial.");
            return;
        }

        TapTargetView.showFor(getActivity(),
                TapTarget.forView(addButton, "Medicine Reminder",
                                "Tap the ‘+ Add Medicine Reminder’ button to set up notifications for your medications.")
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
                        sharedPreferences.edit().putBoolean("finishReminderTutorial", true).apply();
                    }
                });
    }

    private void updateEmptyState() {
        if (medicines == null || medicines.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noDataImage.setVisibility(View.VISIBLE); // Show the logo
            noDataText.setVisibility(View.VISIBLE); // Show the no data text
        } else {
            recyclerView.setVisibility(View.VISIBLE); // Show the RecyclerView
            noDataImage.setVisibility(View.GONE); // Hide the logo
            noDataText.setVisibility(View.GONE); // Hide the no data text
        }
    }

}