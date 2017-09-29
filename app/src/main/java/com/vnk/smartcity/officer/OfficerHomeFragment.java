package com.vnk.smartcity.officer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vnk.smartcity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfficerHomeFragment extends Fragment {

    private Button btnComplaints, btnUpdateComplaints;
    public OfficerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_officer_home, container, false);


        btnComplaints = v.findViewById(R.id.buttonComplaintOff);
        btnUpdateComplaints = v.findViewById(R.id.buttonUpdateOff);

        btnComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ViewComplaintsOfficerFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                        .add(fragment, "frag1")
                        .addToBackStack("frag");
                ft.replace(R.id.content_officer, fragment);
                ft.commit();
            }
        });


        btnUpdateComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UpdateComMainFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                        .add(fragment, "frag2")
                        .addToBackStack("frag2");
                ft.replace(R.id.content_officer, fragment);
                ft.commit();
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
