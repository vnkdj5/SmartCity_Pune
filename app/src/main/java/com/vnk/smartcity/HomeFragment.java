package com.vnk.smartcity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vnk.smartcity.complaints.ViewComplaintsActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    Button btnComplaint, btnWater, btnGarbage, btnRoad, btnViewComplaints;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        btnComplaint = (Button) v.findViewById(R.id.buttonComplaint);
        btnWater = (Button) v.findViewById(R.id.buttonWater);
        btnGarbage = (Button) v.findViewById(R.id.buttonGarbage);
        btnRoad = (Button) v.findViewById(R.id.buttonRoad);
        btnViewComplaints = (Button) v.findViewById(R.id.buttonViewComplaints);


        btnComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("spinner_id", 0);
                Fragment fragment = new ComplaintsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("complaintsfrag");
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });

        btnRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("spinner_id", 1);
                Fragment fragment = new ComplaintsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("complaintsRoad");
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });
        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("spinner_id", 2);
                Fragment fragment = new ComplaintsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("complaintsWater");
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });
        btnGarbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("spinner_id", 4);
                Fragment fragment = new ComplaintsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("complaintsGarbage");
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });
        btnViewComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewComplaintsActivity.class));
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

}
