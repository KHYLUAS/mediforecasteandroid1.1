package com.example.mediforecast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.navigation.NavigationView;

public class home1_fragment extends Fragment  {

//    private DrawerLayout drawerLayout;
//    private NavigationView navigationView;
//    private ImageButton imageButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home1_fragment, container, false);

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
