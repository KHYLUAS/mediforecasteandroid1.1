package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private MedicineRepository medicineRepository;
    private RecyclerView recyclerView;
    private ImageView addButton;
    private MedicineAdapter adapter;
    Medicine medicine;
    private Map<String, Integer> dayOfWeekMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        medicineRepository = new MedicineRepository(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_fragment, container, false);
        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.taskRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with empty list
        adapter = new MedicineAdapter(getContext(), null, medicineRepository);
        recyclerView.setAdapter(adapter);

        // Set up Add button
        addButton = view.findViewById(R.id.Addimage);
        addButton.setOnClickListener(v -> {
            // Start activity to add a new medicine
            Intent intent = new Intent(getContext(), medicine_signin.class);
            startActivity(intent);
        });

        // Observe the LiveData and update the adapter when data changes
        medicineRepository.getAllMedicines().observe(getViewLifecycleOwner(), new Observer<List<Medicine>>() {
            @Override
            public void onChanged(List<Medicine> medicines) {
                adapter.setMedicines(medicines);
            }
        });

        return view;
    }

}