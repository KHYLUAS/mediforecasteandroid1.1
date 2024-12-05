package com.example.mediforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class dashboard_fragment extends Fragment {

    private ImageView communitypostIV, medicinereminderIV, selfchecker;
    private LinearLayout symptomHistoryLayout;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_fragment, container, false);

        // Initialize the views
        communitypostIV = view.findViewById(R.id.communitypost);
        medicinereminderIV = view.findViewById(R.id.medicinereminder);
        selfchecker = view.findViewById(R.id.selfchecker);
        symptomHistoryLayout = view.findViewById(R.id.symptomHistoryLayout);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set onClickListeners for the ImageViews
        communitypostIV.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "COMMUNITYDB");
            startActivity(intent);
        });

        medicinereminderIV.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "REMINDERDB");
            startActivity(intent);
        });

        selfchecker.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Menubar.class);
            intent.putExtra("EXTRA_FRAGMENT", "ANALYZER");
            startActivity(intent);
        });

        // Fetch and display the symptom history
        createSymptomHeader(); // Create the header first
        fetchSymptomHistory(); // Fetch and display records
        return view;
    }

    private void fetchSymptomHistory() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyInfo", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("EMAIL", null);

        // Check if email is available
        if (savedEmail == null) {
            Toast.makeText(getActivity(), "No user email found", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("SymptomAnalyzer")
                .whereEqualTo("email", savedEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String documentId = document.getId();
                                String painLocation = document.getString("painLocation");
                                Timestamp timestamp = document.getTimestamp("dateAndTime");
                                String formattedDate = formatDate(timestamp);
                                createSymptomRow(formattedDate, painLocation, documentId);
                            }
                        } else {
                            Toast.makeText(getActivity(), "No symptom records found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error fetching data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String formatDate(Timestamp timestamp) {
        if (timestamp == null) {
            return "Unknown Date"; // Handle null timestamp gracefully
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy hh:mm a", Locale.getDefault());
        Date date = timestamp.toDate();
        return sdf.format(date);
    }

    private void createSymptomHeader() {
        if (getActivity() == null) {
            return; // Exit if context is not available
        }

        LinearLayout headerLayout = new LinearLayout(getActivity());
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        headerLayout.setPadding(20, 20, 20, 20);

        // Add TextView for Date Header
        TextView dateHeader = new TextView(getActivity());
        dateHeader.setText("Date");
        dateHeader.setTextSize(18f);
        dateHeader.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f)); // Weight 1

        // Add TextView for Pain Location Header
        TextView painLocationHeader = new TextView(getActivity());
        painLocationHeader.setText("Pain Location");
        painLocationHeader.setTextSize(18f);
        painLocationHeader.setTextColor(getResources().getColor(R.color.primaryColor));
        painLocationHeader.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1)); // Weight 1

        // Add to the header layout
        headerLayout.addView(dateHeader);
        headerLayout.addView(painLocationHeader);

        // Add to the parent layout
        symptomHistoryLayout.addView(headerLayout);
    }

    private void createSymptomRow(String date, String painLocation, String documentId) {
        // Create a container for the row
        LinearLayout rowLayout = new LinearLayout(getActivity());
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setPadding(20, 20, 20, 20);
        rowLayout.setBackgroundResource(R.drawable.border_hover);
        rowLayout.setClickable(true);

        // Add a TextView for the date
        TextView dateView = new TextView(getActivity());
        dateView.setText(date);
        dateView.setTextSize(16f);
        dateView.setSingleLine(true);
        dateView.setEllipsize(TextUtils.TruncateAt.END); // Optional: add ellipsis
        dateView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f)); // Assign more weight to date

        // Add a TextView for the pain location
        TextView painLocationView = new TextView(getActivity());
        painLocationView.setText(painLocation);
        painLocationView.setTextSize(16f);
        painLocationView.setSingleLine(true);
        painLocationView.setEllipsize(TextUtils.TruncateAt.END);
        painLocationView.setTextColor(getResources().getColor(R.color.primaryColor));
        painLocationView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)); // Assign slightly less weight to pain location

        // Add TextViews to the row
        rowLayout.addView(dateView);
        rowLayout.addView(painLocationView);

        // Add the row to the parent layout
        rowLayout.setOnClickListener(v -> {
            // Handle row click (e.g., open a detailed view or perform other actions)
            Toast.makeText(getActivity(), "Clicked: " + documentId, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), SymptomActivity.class);
            intent.putExtra("documentId", documentId);
            intent.putExtra("isViewing", true);
            startActivity(intent);
        });

        symptomHistoryLayout.addView(rowLayout);
    }

}
