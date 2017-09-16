package com.vnk.smartcity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComplaintsFragment extends Fragment {


    public ComplaintsFragment() {
        // Required empty public constructor
    }
////===============Change static to dynan=mic data loading

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View v= inflater.inflate(R.layout.fragment_complaints, container, false);
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.main_category, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final Spinner subCatagorySpinner=(Spinner) v.findViewById(R.id.spinner2);
        final ArrayAdapter<CharSequence> eleAdapter=ArrayAdapter.createFromResource(this.getActivity(),
                R.array.electrical,android.R.layout.simple_spinner_item); eleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCatagorySpinner.setAdapter(eleAdapter);

        final ArrayAdapter<CharSequence> waterAdapter=ArrayAdapter.createFromResource(this.getActivity(),
                R.array.water,android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> roadAdapter=ArrayAdapter.createFromResource(this.getActivity(),
                R.array.road,android.R.layout.simple_spinner_item);


        final ArrayAdapter<CharSequence> noiseAdapter=ArrayAdapter.createFromResource(this.getActivity(),
                R.array.noise,android.R.layout.simple_spinner_item);

        final ArrayAdapter<CharSequence> garbageAdapter=ArrayAdapter.createFromResource(this.getActivity(),
                R.array.garbage,android.R.layout.simple_spinner_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                switch (pos)
                {
                    case 0:
                        eleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subCatagorySpinner.setAdapter(eleAdapter);
                        break;
                    case 1:
                        roadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subCatagorySpinner.setAdapter(roadAdapter);
                        break;
                    case 2:
                        waterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subCatagorySpinner.setAdapter(waterAdapter);
                        break;
                    case 3:
                        noiseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subCatagorySpinner.setAdapter(noiseAdapter);
                        break;
                    case 4:
                        garbageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subCatagorySpinner.setAdapter(garbageAdapter);
                        break;
                    case 5:
                        eleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subCatagorySpinner.setAdapter(eleAdapter);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Inflate the layout for this fragment
        return v;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Complaints");
    }

}
