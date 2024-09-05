package com.example.mediforecast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class home1_fragment extends Fragment  {
    private static final String TAG = "home1_fragment";
    private FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home1_fragment, container, false);
        db = FirebaseFirestore.getInstance();

        // Set up real-time listener for the "CommunityPost" collection
        CollectionReference communityPostRef = db.collection("CommunityPost");

        communityPostRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null) {
                    // Loop through all documents in the collection
                    for (QueryDocumentSnapshot document : snapshot) {
                        Log.d(TAG, "Community post: " + document.getId() + " => " + document.getData());
                        // You can now handle each document, e.g., update the UI with the document data
                    }
                } else {
                    Log.d(TAG, "Current data: null or empty");
                }
            }
        });

        // Initialize views
//        drawerLayout = view.findViewById(R.id.drawer_layout);
//        navigationView = view.findViewById(R.id.nav_view);
////        imageButton = view.findViewById(R.id.imageButton);
//
//        // Set up the ImageButton to toggle the navigation drawer
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                } else {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                }
//            }
//        });
//
//        navigationView.bringToFront();
//        navigationView.setNavigationItemSelectedListener(this);

        return view;
    }



//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        // Handle navigation view item clicks here.
//        // You can handle different menu items and their actions here.
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
