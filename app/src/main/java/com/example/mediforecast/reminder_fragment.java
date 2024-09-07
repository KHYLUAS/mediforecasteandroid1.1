        package com.example.mediforecast;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;

        import androidx.fragment.app.Fragment;


        public class reminder_fragment extends Fragment {

            private ImageView addButton;

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);



            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                // Inflate the layout for this fragment
                View view = inflater.inflate(R.layout.fragment_reminder_fragment, container, false);

                addButton = view.findViewById(R.id.Addimage);

                addButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), medicine_signin.class);
                    startActivity(intent);

                });

             return view;
            }
        }