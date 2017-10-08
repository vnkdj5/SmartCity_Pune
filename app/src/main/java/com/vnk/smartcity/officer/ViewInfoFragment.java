package com.vnk.smartcity.officer;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vnk.smartcity.Config;
import com.vnk.smartcity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewInfoFragment extends Fragment {

    List<String> pincodes;
    private String officerEmail, officerMobile, officerId, officerDept, officerName;
    private EditText etDept, etOffId, etOffName, etEmail, etMobile;
    private TextView textViewAreas;
    private Button updateProfile;

    public ViewInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_info, container, false);
        findViewByIds(v);
        pincodes = new ArrayList<>();
        getOfficer();

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile1();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void findViewByIds(View v) {
        etDept = v.findViewById(R.id.etDeptId);
        etOffId = v.findViewById(R.id.etId);
        etOffName = v.findViewById(R.id.etName);
        etEmail = v.findViewById(R.id.etEmail);
        etMobile = v.findViewById(R.id.etMobile);
        textViewAreas = v.findViewById(R.id.tvAreas);
        updateProfile = v.findViewById(R.id.bntupdateProfile);

    }

    private void getOfficer() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...", false, false);

        //Creating a json array request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_OFFICER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            officerEmail = jsonObject.getString("officer_email");
                            officerMobile = jsonObject.getString("officer_mobile");
                            officerId = jsonObject.getString("officer_id");
                            officerName = jsonObject.getString("officer_name");
                            officerDept = jsonObject.getString("officer_dept_id");
                            JSONArray pincode = new JSONArray(jsonObject.getString("pincode"));
                            for (int j = 0; j < pincode.length(); j++) {
                                pincodes.add(pincode.get(j) + "\n");
                            }
                            loadOfficer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("ViewFragInfo", ": " + e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), "COnnection ERROR", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                SharedPreferences sp = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                String officer_email = sp.getString(Config.USER_EMAIL_SHARED_PREF, null);
                params.put("officer_email", officer_email);


                //returning parameter
                return params;
            }
        };

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void loadOfficer() {
        etDept.setText(officerDept);
        etOffId.setText(officerId);
        etOffName.setText(officerName);
        etMobile.setText(officerMobile);
        etEmail.setText(officerEmail);
        String areas = "";
        for (int i = 0; i < pincodes.size(); i++) {
            areas = areas + pincodes.get(i);
        }
        textViewAreas.setText(areas);
    }

    private void updateProfile1() {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Updating Profile", "Please wait...", false, false);

        //Creating a json array request
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.UPDATE_OFFICER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //  Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                showDialog(obj.getString("message"), obj.getBoolean("error"));
                            } else {
                                showDialog(obj.getString("message"), obj.getBoolean("error"));

                            }
                        } catch (JSONException e) {
                            Log.d("UpdateOfficer", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ViewOfficerProfile", error.toString());
                        loading.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                SharedPreferences sp = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                String officer_email = sp.getString(Config.USER_EMAIL_SHARED_PREF, null);
                params.put("officer_email", officer_email);
                params.put("officer_name", etOffName.getText().toString());
                params.put("officer_mobile", etMobile.getText().toString());

                //returning parameter
                return params;
            }
        };

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    public void showDialog(final String msg, final boolean error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());
        builder.setTitle("Update Status");
        builder.setMessage(msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        if (!error) {
                            ft.replace(R.id.content_officer, new OfficerHomeFragment(), "O_HOME_FRAG");
                            ft.commit();
                        } else {
                            //do nothing
                        }

                    }
                });
        builder.show();
    }
}
