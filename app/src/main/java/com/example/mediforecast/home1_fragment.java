package com.example.mediforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import kr.co.prnd.readmore.ReadMoreTextView;


public class home1_fragment extends Fragment {
    private static final String TAG = "home1_fragment";
    private FirebaseFirestore db;
    private ReadMoreTextView readmore;
    private ImageView noDataImage;
    private RecyclerView taskRecycler;
    private FirebaseAuth mAuth;
    private HomeAdapter adapter;
    private ArrayList<Home> homeList = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home1_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        noDataImage = view.findViewById(R.id.noDataImage);
        taskRecycler = view.findViewById(R.id.taskRecycler);
        mAuth = FirebaseAuth.getInstance();

        taskRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomeAdapter(getContext(), homeList);
        taskRecycler.setAdapter(adapter);
//        sharedPreferences = requireActivity().getSharedPreferences("TapTargetsPrefs", Context.MODE_PRIVATE);
//
//
//        if(sharedPreferences.getBoolean("finishTabBarTutorial", false) && !isCommunityTutorialFinished()){
//            showCommunityTutorial();
//        }

        fetch_communityPost();

        return view;
    }
//    private boolean isCommunityTutorialFinished() {
//        // Check if the dashboard tutorial is finished
//        return sharedPreferences.getBoolean("finishCommunityTutorial", false);
//    }
////
//    private void showCommunityTutorial() {
//        TapTargetView.showFor(getActivity(),
//                TapTarget.forView(getView().findViewById(R.id.taskRecycler),
//                                "Community Post",
//                                "These are the post's of the RHU's and Barangay Health Centers")
//                        .outerCircleColor(R.color.colorAccent) // Outer circle color
//                        .targetCircleColor(android.R.color.white) // Target circle color
//                        .titleTextSize(20) // Title text size
//                        .descriptionTextSize(16) // Description text size
//                        .outerCircleAlpha(0.96f) // Outer circle alpha
//                        .transparentTarget(false) // Show the target fully
//                        .cancelable(true) // Allow user to cancel
//                        .drawShadow(true) // Show shadow
//                        .tintTarget(true) // Tint the target
//                        .dimColor(android.R.color.black), // Dim the background
//                new TapTargetView.Listener() {
//                    @Override
//                    public void onTargetClick(TapTargetView view) {
//                        super.onTargetClick(view);
//                        // Mark the dashboard tutorial as finished after it is clicked
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean("finishCommunityTutorial", true);
//                        editor.apply();
//                    }
//                });
//    }
    private void fetch_communityPost() {
        // Set up real-time listener for the "CommunityPost" collection with ordering
        CollectionReference communityPostRef = db.collection("CommunityPost");
        communityPostRef.orderBy("created_by", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Error fetching posts: " + e.getMessage());
                            Toast.makeText(getContext(), "Failed to fetch community posts: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (snapshot != null) {
                            homeList.clear();
                            for (QueryDocumentSnapshot document : snapshot) {
//                                Log.d(TAG, "Community post: " + document.getId() + " => " + document.getData());
                                String rhu = document.getString("rhu");
                                String createdBy = document.getString("created_by");
                                String postImg = document.getString("postImg");
                                String postMessage = document.getString("postMessage");
                                String fileType = document.getString("fileType");

                                if (rhu != null && createdBy != null && postMessage != null && postImg != null && fileType != null) {
                                    Home home = new Home(rhu, createdBy, postMessage, postImg, fileType);
                                    homeList.add(home);
                                } else {
                                    Log.w(TAG, "Missing fields in document: " + document.getId());
                                }
                            }
                            adapter.notifyDataSetChanged();
                            if (homeList.isEmpty()) {
                                noDataImage.setVisibility(View.VISIBLE);
                                taskRecycler.setVisibility(View.GONE);
                            } else {
                                noDataImage.setVisibility(View.GONE);
                                taskRecycler.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.d(TAG, "No community posts found.");
                        }
                    }
                });
    }


}

