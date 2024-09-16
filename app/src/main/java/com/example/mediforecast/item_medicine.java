package com.example.mediforecast;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class item_medicine extends AppCompatActivity {

    private ImageView option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_medicine);

        // Find the ImageView by ID
//        option = findViewById(R.id.options);
//
//        // Set click listener for the ImageView
//        option.setOnClickListener(v -> {
//            // Create a PopupMenu
//            PopupMenu popupMenu = new PopupMenu(item_medicine.this, v);
//            MenuInflater inflater = popupMenu.getMenuInflater();
//            inflater.inflate(R.menu.menu_reminder, popupMenu.getMenu());
//
//            // Set click listener for menu items
//            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    // If-else logic instead of switch-case
//                    int itemId = item.getItemId();
//
//                    if (itemId == R.id.menuComplete) {
//                        // Handle "Complete" action
//                        Toast.makeText(item_medicine.this, "Completed!", Toast.LENGTH_SHORT).show();
//                        return true;
//                    } else if (itemId == R.id.menuUpdate) {
//                        // Handle "Edit" action
//                        Toast.makeText(item_medicine.this, "Edit clicked!", Toast.LENGTH_SHORT).show();
//                        return true;
//                    } else if (itemId == R.id.menuDelete) {
//                        // Handle "Delete" action
//                        Toast.makeText(item_medicine.this, "Deleted!", Toast.LENGTH_SHORT).show();
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            });
//
//            // Show the PopupMenu
//            popupMenu.show();
//        });
    }
}
