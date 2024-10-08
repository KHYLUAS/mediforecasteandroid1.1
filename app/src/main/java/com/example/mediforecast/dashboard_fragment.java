package com.example.mediforecast;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class dashboard_fragment extends Fragment {

    private ImageView communitypostIV, medicinereminderIV, selfchecker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_fragment, container, false);

        // Initialize the views
        communitypostIV = view.findViewById(R.id.communitypost);
        medicinereminderIV = view.findViewById(R.id.medicinereminder);
        selfchecker = view.findViewById(R.id.selfchecker);

        // Set onClickListeners for the ImageViews
        communitypostIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Menubar.class);
                intent.putExtra("EXTRA_FRAGMENT", "COMMUNITYDB");
                startActivity(intent);
            }
        });

        medicinereminderIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Menubar.class);
                intent.putExtra("EXTRA_FRAGMENT", "REMINDERDB");
                startActivity(intent);
            }
        });

        selfchecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Menubar.class);
                intent.putExtra("EXTRA_FRAGMENT", "SELFCHECKERDB");
                startActivity(intent);
            }
        });

        return view;
    }
}
