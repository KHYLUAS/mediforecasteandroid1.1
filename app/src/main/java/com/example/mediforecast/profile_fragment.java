package com.example.mediforecast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class profile_fragment extends Fragment {

    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private TextView nameTextView, emailTextView, contactTextView, locationTextView, usernameTextView, birthdayTextView, genderTextView, logoutTextView, editprofileTextView,updateTextView;
    //    private ShapeableImageView profileImageView;
    private FirebaseAuth auth;
    private ImageView profileImageView, cameraTextView;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private FirebaseStorage storage;
    private ImageView imageView;
    private loading1 loading1;
    public SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Get current logged-in user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        preferences = getActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        //Initialize FirebaseStorage
        storage = FirebaseStorage.getInstance();

        // Initialize loading dialog
        loading1 = new loading1(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_fragment, container, false);

        // Initialize UI elements
        nameTextView = view.findViewById(R.id.name_text_view);
        emailTextView = view.findViewById(R.id.gmailprofile);
        contactTextView = view.findViewById(R.id.numberprofile);
        locationTextView = view.findViewById(R.id.locationprofile);
        profileImageView = view.findViewById(R.id.profile_image);
//        usernameTextView = view.findViewById(R.id.username_text_view);
        birthdayTextView = view.findViewById(R.id.birthdayprofile);
        genderTextView = view.findViewById(R.id.genderprofile);
        logoutTextView = view.findViewById(R.id.logoutprofile);
        editprofileTextView = view.findViewById(R.id.editprofile);
        cameraTextView = view.findViewById(R.id.imageView5);
        updateTextView = view.findViewById(R.id.updatepass);

        // Load cached data from SharedPreferences
        loadCachedProfileData();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyInfo", Context.MODE_PRIVATE);
        // Retrieve email and password
        String savedEmail = sharedPreferences.getString("EMAIL", null);  // Default is null if not found

        // Set up Firestore listener for real-time updates
        // Set up Firestore listener for real-time updates using email from SharedPreferences
        firestore.collection("MobileUsers").whereEqualTo("email", savedEmail)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(getActivity(), "Failed to load profile data.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (snapshot != null && !snapshot.isEmpty()) {
                        // Loop through all matched documents (in case of multiple)
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            if (document.exists()) {
                                String profileImageUrl = document.getString("profileImage");
                                String profileEmail = document.getString("email");
                                String profileFname = document.getString("fname");
                                String profileLname = document.getString("lname");
                                String profileBirthday = document.getString("birthday");
                                String profileNumber = document.getString("number");
                                String profileGender = document.getString("gender");
                                String profileLocation = document.getString("location");

                                // Format the birthday if needed
                                birthdayFormatDate(profileBirthday);

                                // Save profile data to SharedPreferences
                                saveProfileToSharedPreferences(profileImageUrl, profileEmail, profileFname, profileLname, profileBirthday, profileNumber, profileGender, profileLocation);

                                // Update UI with the fetched data
                                updateProfileUI(profileImageUrl, profileEmail, profileFname, profileLname, profileBirthday, profileNumber, profileGender, profileLocation);

                                // Load profile image using Glide
                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    Glide.with(profile_fragment.this)
                                            .load(profileImageUrl)
                                            .into(profileImageView);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "No profile data found.", Toast.LENGTH_SHORT).show();
                    }
                });




        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();

            }
        });

        if (requireActivity().getActionBar() != null) {
            Objects.requireNonNull(requireActivity().getActionBar()).setBackgroundDrawable(
                    new ColorDrawable(requireContext().getColor(R.color.primaryColor))
            );
        }

        // Initialize the activityResultLauncher
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) {
//                    profileImageView.setImageURI(uri); //display image
                    uploadImageToFirebase(uri); //Upload image to Firebase

                }
            }
        });

        cameraTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);  // Launch the image picker intent with the result launcher
        });


        updateTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), update_password.class);
            startActivity(intent);
        });

        editprofileTextView.setOnClickListener(v-> {
            Intent intent = new Intent(getActivity(), editprofile.class);
            startActivity(intent);
        });

        return view;
    }
    // Load cached profile data from SharedPreferences
    private void loadCachedProfileData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        String profileImageUrl = preferences.getString("profileImage", "");
        String profileEmail = preferences.getString("profileEmail", "");
        String profileFname = preferences.getString("profileFname", "");
        String profileLname = preferences.getString("profileLname", "");
        String profileBirthday = preferences.getString("profileBirthday", "");
        String profileNumber = preferences.getString("profileNumber", "");
        String profileGender = preferences.getString("profileGender", "");
        String profileLocation = preferences.getString("profileLocation", "");

        // If there's cached data, update the UI
        if (!profileEmail.isEmpty()) {
            birthdayFormatDate(profileBirthday);
            updateProfileUI(profileImageUrl, profileEmail, profileFname, profileLname, profileBirthday, profileNumber, profileGender, profileLocation);
        }
    }

    // Save profile data to SharedPreferences
    private void saveProfileToSharedPreferences(String profileImageUrl, String profileEmail, String profileFname, String profileLname, String profileBirthday, String profileNumber, String profileGender, String profileLocation) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("profileImage", profileImageUrl);
        editor.putString("profileEmail", profileEmail);
        editor.putString("profileFname", profileFname);
        editor.putString("profileLname", profileLname);
        editor.putString("profileBirthday", profileBirthday);
        editor.putString("profileNumber", profileNumber);
        editor.putString("profileGender", profileGender);
        editor.putString("profileLocation", profileLocation);
        editor.apply(); // Save changes
    }

    // Update UI with profile data
    private void updateProfileUI(String profileImageUrl, String profileEmail, String profileFname, String profileLname, String profileBirthday, String profileNumber, String profileGender, String profileLocation) {
        nameTextView.setText(profileFname + " " + profileLname);
        emailTextView.setText(profileEmail);
        contactTextView.setText(profileNumber);
        genderTextView.setText(profileGender);
        locationTextView.setText(profileLocation);

        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            Glide.with(profile_fragment.this)
                    .load(profileImageUrl)
                    .into(profileImageView);
        }
    }

    private void birthdayFormatDate(String profileBirthday){
        String birthdayDate = profileBirthday;

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/M   M/yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(birthdayDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            String formattedDate = outputFormat.format(date);

            birthdayTextView.setText(formattedDate);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void uploadImageToFirebase(Uri uri) {
        // Retrieve email from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyInfo", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("EMAIL", null);  // Default is null if not found

        if (savedEmail != null) {
            // Show loading dialog
            loading1.show();

            // Query Firestore to find the user by email
            firestore.collection("MobileUsers").whereEqualTo("email", savedEmail)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Assuming the query returns a single document (unique email)
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String userId = documentSnapshot.getId();

                            // Creating Storage reference for profile image
                            StorageReference storageReference = storage.getReference()
                                    .child("MobileUsersProfile/" + userId + ".jpg");

                            // Upload image to Firebase Storage
                            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                                // Get the download URL after upload
                                storageReference.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                    // Update Firestore document with the profile image URL
                                    firestore.collection("MobileUsers").document(userId)
                                            .update("profileImage", downloadUri.toString())
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(getActivity(), "Profile image updated", Toast.LENGTH_SHORT).show();
                                                loading1.dismiss();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(getActivity(), "Failed to update profile image", Toast.LENGTH_SHORT).show();
                                                loading1.dismiss();
                                            });
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(getActivity(), "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                    loading1.dismiss();
                                });
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                                loading1.dismiss();
                            });
                        } else {
                            Toast.makeText(getActivity(), "No user found with this email", Toast.LENGTH_SHORT).show();
                            loading1.dismiss();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Failed to query user by email", Toast.LENGTH_SHORT).show();
                        loading1.dismiss();
                    });

        } else {
            Toast.makeText(getActivity(), "No email found in SharedPreferences", Toast.LENGTH_SHORT).show();
        }
    }




    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set dialog properties
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        // Positive button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit(); // Use sharedPreferences here
                editor.clear();
                editor.apply();

                // Show a message to the user
                Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Redirect to the login screen
                Intent intent = new Intent(getActivity(), login_form.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // Negative button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User cancels the dialog
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}



