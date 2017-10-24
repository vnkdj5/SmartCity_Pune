package com.vnk.smartcity.emergency;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vnk.smartcity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyFragment extends Fragment {

    public static final int REQUEST_PHONE_CALL = 223;
    ListView list;
    String[] itemname;
    String[] contacts;
    Integer imgid = R.drawable.ic_menu_manage;
    int emergency_dept;

    public EmergencyFragment() {
        // Required empty public constructor
        emergency_dept = 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.emergency_contacts, container, false);
        emergency_dept = this.getArguments().getInt("emergency_dept");
        loadData(emergency_dept);

        CustomListAdapter adapter = new CustomListAdapter(getActivity(), itemname, contacts, imgid);
        list = v.findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String Slecteditem = contacts[position];
                call(Slecteditem);

            }
        });
        return v;
    }


    public void loadData(int deptId) {
        switch (deptId) {
            case 0:
                itemname = getResources().getStringArray(R.array.policeNames);
                contacts = getResources().getStringArray(R.array.policeNumber);
                imgid = R.drawable.police;
                break;
            case 1:
                itemname = getResources().getStringArray(R.array.hospitalNames);
                contacts = getResources().getStringArray(R.array.hopsitalNumber);
                imgid = R.drawable.hospitals;
                break;
            case 2:
                itemname = getResources().getStringArray(R.array.fireNames);
                contacts = getResources().getStringArray(R.array.fireNumber);
                imgid = R.drawable.firebrigade;
                break;
        }

    }

    public void call(final String number) {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(number);
        alertDialogBuilder.setMessage("Do you want to call ?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent call_intent = new Intent(Intent.ACTION_CALL);
                        call_intent.setData(Uri.parse("tel:" + number));
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                            startActivity(call_intent);
                        } else {
                            startActivity(call_intent);
                        }
                        Toast.makeText(getActivity(), number, Toast.LENGTH_SHORT).show();

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
