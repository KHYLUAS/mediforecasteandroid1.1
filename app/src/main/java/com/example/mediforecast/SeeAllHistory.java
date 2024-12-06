package com.example.mediforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SeeAllHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    private List<History> historyList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private SharedPreferences sharedPreferences;
    private String savedEmail;
    private ImageView backB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_history);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("MyInfo", Context.MODE_PRIVATE);
        savedEmail = sharedPreferences.getString("EMAIL", null);
        backB = findViewById(R.id.backButton);

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeAllHistory.this, Menubar.class);
                intent.putExtra("EXTRA_FRAGMENT", "");
                startActivity(intent);
                finish();
            }
        });
        if (savedEmail != null) {
            fetchSymptomHistory();
        } else {
            Toast.makeText(this, "No user email found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSymptomHistory() {
        firestore.collection("SymptomAnalyzer")
                .whereEqualTo("email", savedEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            historyList.clear(); // Clear the list first
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String documentId = document.getId();
                                String painLocation = document.getString("painLocation");
                                Timestamp timestamp = document.getTimestamp("dateAndTime");
                                String formattedDate = formatDate(timestamp);

                                History history = new History(formattedDate, painLocation, documentId);
                                historyList.add(history);
                            }

                            // Set up the adapter
                            tableAdapter = new TableAdapter(historyList, this::onDeleteClick, this::onRowClick);
                            recyclerView.setAdapter(tableAdapter);
                        } else {
                            Toast.makeText(this, "No symptom records found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error fetching data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onDeleteClick(int position) {
        History history = historyList.get(position);
        deleteSymptom(history.getDocumentId(), position);
    }

    private void deleteSymptom(String documentId, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deleting");
        builder.setMessage("Are you sure you want to delete this record?");
        builder.setPositiveButton("Delete", ((dialog, which) -> {
            firestore.collection("SymptomAnalyzer")
                    .document(documentId)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Remove from list and notify adapter
                            historyList.remove(position);
                            tableAdapter.notifyItemRemoved(position);
                            Toast.makeText(this, "Symptom record deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error deleting record: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }));
        builder.setNegativeButton("Cancel", ((dialog, which) -> {
            dialog.dismiss();
        }));
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void onRowClick(int position) {
        History history = historyList.get(position);
        // Open a new activity or show details in the current activity
        Toast.makeText(this, "Row clicked: " + history.getDate(), Toast.LENGTH_SHORT).show();

        // Example: Open a new activity with details
        Intent intent = new Intent(this, SymptomActivity.class);
        intent.putExtra("documentId", history.getDocumentId());
        intent.putExtra("isViewing", true);
        startActivity(intent);
    }
    // Helper function to format the timestamp
    private String formatDate(Timestamp timestamp) {
        if (timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy hh:mm a", Locale.getDefault());
            return sdf.format(timestamp.toDate());
        }
        return "";
    }
}