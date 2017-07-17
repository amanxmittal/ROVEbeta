package com.me.sam.rove;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;


public class Experience extends Fragment {

    private CardView cvTourist, cvTraveller, cvExplorer;

    public Experience() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_experience, container, false);

        cvTourist = (CardView) view.findViewById(R.id.cvTourist);
        cvTraveller = (CardView) view.findViewById(R.id.cvTraveller);
        cvExplorer = (CardView) view.findViewById(R.id.cvExplorer);


        cvTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touristFragment abc = new touristFragment();

                getChildFragmentManager().beginTransaction().replace(R.id.layoutExperience,abc).commit();
            }
        });

        cvTraveller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travellerFragment abc = new travellerFragment();

                getChildFragmentManager().beginTransaction().replace(R.id.layoutExperience,abc).commit();

            }
        });

        cvExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explorerFragment abc = new explorerFragment();

                getChildFragmentManager().beginTransaction().replace(R.id.layoutExperience,abc).commit();
            }
        });


        return view;


    }


}
