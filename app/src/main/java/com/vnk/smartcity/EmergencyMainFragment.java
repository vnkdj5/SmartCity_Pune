package com.vnk.smartcity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vnk.smartcity.emergency.EmergencyFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyMainFragment extends Fragment implements View.OnClickListener {

    private Button policebutton, hospitalBtn, fireBtn;

    public EmergencyMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_emergency_main, container, false);

        policebutton = v.findViewById(R.id.ebutton);
        hospitalBtn = v.findViewById(R.id.ebutton2);
        fireBtn = v.findViewById(R.id.ebutton3);
        policebutton.setOnClickListener(this);
        hospitalBtn.setOnClickListener(this);
        fireBtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ebutton:
                bundle.putInt("emergency_dept", 0);
                break;
            case R.id.ebutton2:
                bundle.putInt("emergency_dept", 1);
                break;
            case R.id.ebutton3:
                bundle.putInt("emergency_dept", 2);
                break;
            default:
                bundle.putInt("emergency_dept", 0);
                break;

        }
        Fragment fragment = new EmergencyFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack("emer");
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

    }
}
