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
import java.util.Locale;

public class reminder_fragment extends Fragment {

    private static final String TAG = "ReminderFragment";
    private ImageView addButton;
    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private ArrayList<Reminder> reminderList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private ImageView noDataImage;
    private SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/yyyy", Locale.US);
    private SimpleDateFormat outputDateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);

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
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
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

                                    Log.d(TAG, "Fetched startDate: " + startDate);  // Add this to check startDate

                                    if (medicineName != null && medicineDosage != null && startDate != null) {
                                        String formattedDate = formatStartDate(startDate);
                                        Log.d(TAG, "Formatted startDate: " + formattedDate);  // Check the formatted date

                                        Reminder reminder = new Reminder(medicineName, medicineDosage, formattedDate);
                                        reminderList.add(reminder);
                                    } else {
                                        Log.w(TAG, "One or more fields are null for document: " + document.getId());
                                    }
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

    private String formatStartDate(String startDate) {
        if (startDate == null || startDate.isEmpty()) {
            return "";
        }

        try {
            Date date = inputDateFormat.parse(startDate);
            if (date != null) {
                return outputDateFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
