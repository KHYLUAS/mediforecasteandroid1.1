package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class reminder_fragment extends Fragment {

    private static final String TAG = "ReminderFragment";
    private ImageView addButton;
    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private ArrayList<Reminder> reminderList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private ImageView noDataImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_fragment, container, false);

        addButton = view.findViewById(R.id.Addimage);
        recyclerView = view.findViewById(R.id.taskRecycler);
        noDataImage = view.findViewById(R.id.noDataImage);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReminderAdapter(getContext(), reminderList);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), medicine_signin.class);
            startActivity(intent);
        });

        fetchReminders();

        return view;
    }

    private void fetchReminders() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            firestore.collection("MedicineReminder")
                    .whereEqualTo("email", email)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot snapshots, @javax.annotation.Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e(TAG, "Error fetching data", error);
                                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (snapshots != null) {
                                reminderList.clear();
                                for (QueryDocumentSnapshot document : snapshots) {
                                    String medicineName = document.getString("medicineName");
                                    String medicineDosage = document.getString("medicineDosage");
                                    String startDate = document.getString("startDate");
                                    Reminder reminder = new Reminder(medicineName, medicineDosage, startDate);
                                    reminderList.add(reminder);
                                }
                                adapter.notifyDataSetChanged();
                                if (reminderList.isEmpty()) {
                                    noDataImage.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                } else {
                                    noDataImage.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Log.d(TAG, "No documents found");
                                Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
