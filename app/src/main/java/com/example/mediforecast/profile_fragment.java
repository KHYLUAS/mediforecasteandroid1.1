package com.example.mediforecast;

import static com.google.android.material.color.MaterialColors.getColor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class profile_fragment extends Fragment {

    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private TextView nameTextView, emailTextView, contactTextView, locationTextView, usernameTextView, birthdayTextView, genderTextView, logoutTextView, editprofileTextView;
    //    private ShapeableImageView profileImageView;
    private FirebaseAuth auth;
    private ImageView profileImageView, cameraTextView;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private FirebaseStorage storage;
    private ImageView imageView;
    private loading1 loading1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Get current logged-in user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

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
        usernameTextView = view.findViewById(R.id.username_text_view);
        birthdayTextView = view.findViewById(R.id.birthdayprofile);
        genderTextView = view.findViewById(R.id.genderprofile);
        logoutTextView = view.findViewById(R.id.logoutprofile);
        editprofileTextView = view.findViewById(R.id.editprofile);
        cameraTextView = view.findViewById(R.id.imageView5);


        nameTextView.setText(GlobalUserData.getName());
        emailTextView.setText(GlobalUserData.getEmail());
        contactTextView.setText(GlobalUserData.getContact());
        locationTextView.setText(GlobalUserData.getLocation());
        usernameTextView.setText(GlobalUserData.getUsername());
        birthdayTextView.setText(GlobalUserData.getBirthday());
        genderTextView.setText(GlobalUserData.getGender());


        // Set up Firestore listener for real-time updates
        if (currentUser != null) {
            firestore.collection("MobileUsers").document(currentUser.getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Toast.makeText(getActivity(), "Failed to load profile data.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (snapshot != null && snapshot.exists()) {
                                String profileImageUrl = snapshot.getString("profileImage");
                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    Glide.with(profile_fragment.this)
                                            .load(profileImageUrl)
                                            .into(profileImageView);
                                }
                            }
                        }
                    });
        }

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

        // Handle camera icon click to open Image Picker
//        cameraTextView.setOnClickListener(v -> {
//            ImagePicker.with(requireActivity())  // Use requireActivity() to get the hosting Activity's context
//                    .crop()                      // Crop image (Optional)
//                    .compress(1024)              // Final image size will be less than 1 MB (Optional)
//                    .maxResultSize(1080, 1080)   // Final image resolution will be less than 1080 x 1080 (Optional)
//                    .start();
//        });

        cameraTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);  // Launch the image picker intent with the result launcher
        });
        return view;
    }
//    @Override
//    public void onResume(){
//        super.onResume();
//
//        loadUserProfile();
//    }
//    private void loadUserProfile() {
//        Glide.with(this)
//                .load(GlobalUserData.getProfileImage())
//                .into(profileImageView);
//    }


    private void uploadImageToFirebase(Uri uri) {
        if (currentUser != null) {
            // Creating Storage reference
            loading1.show();
            StorageReference storageReference = storage.getReference()
                    .child("MobileUsersProfile/" + currentUser.getUid() + ".jpg");

            // Upload to Firebase Storage
            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                // Get the download URL
                storageReference.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    // Update Firestore with the profile image URL
                    firestore.collection("MobileUsers").document(currentUser.getUid())
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
            Toast.makeText(getActivity(), "No user logged in", Toast.LENGTH_SHORT).show();
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
                // Sign out the user
                auth.signOut();

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


//    private void fetchUserData(String uid) {
//        firestore.collection("MobileUsers").document(uid).get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            String firstname = document.getString("fname");
//                            String lastname = document.getString("lname");
//                            String name = firstname + " " + lastname;
//                            String email = document.getString("email");
//                            String contact = document.getString("number");
//                            String location = document.getString("location");
//                            String username = document.getString("username");
//
//                            // Populate the UI with the fetched data
////                            nameTextView.setText(name);
////                            emailTextView.setText(email);
////                            contactTextView.setText(contact);
////                            locationTextView.setText(location);
////                            usernameTextView.setText(username);
//
//                            // Optionally, set the profile image if you have it stored in Firestore
//                            // Load image into profileImageView (You can use a library like Glide)
//
//                        } else {
//                            Toast.makeText(getActivity(), "No user data found", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(getActivity(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//    }

