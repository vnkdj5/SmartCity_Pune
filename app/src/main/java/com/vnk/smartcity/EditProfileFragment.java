package com.vnk.smartcity;


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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {
    private String userEmail, userMobile, officerId, u_address, userName;
    private EditText etAddress, etUserName, etEmail, etMobile;
    private Button updateProfile;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        findViewByIds(v);

        getProfile();

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
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(" Update profile");
    }

    private void findViewByIds(View v) {

        etUserName = v.findViewById(R.id.etUserName);
        etEmail = v.findViewById(R.id.etUserEmail);
        etMobile = v.findViewById(R.id.etUserMobile);
        etAddress = v.findViewById(R.id.etUserAddress);
        updateProfile = v.findViewById(R.id.bntupdateUserProfile);

    }

    private void getProfile() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...", false, false);

        //Creating a json array request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            userEmail = jsonObject.getString("user_email");
                            userMobile = jsonObject.getString("user_mobile");
                            u_address = jsonObject.getString("user_address");
                            userName = jsonObject.getString("user_name");


                            loadOfficer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("EditProfile", ": " + e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), "Connection ERROR", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                SharedPreferences sp = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                String officer_email = sp.getString(Config.USER_EMAIL_SHARED_PREF, null);
                params.put("user_email", officer_email);


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
        etUserName.setText(userName);
        etMobile.setText(userMobile);
        etEmail.setText(userEmail);
        etAddress.setText(u_address);
    }

    private void updateProfile1() {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Updating Profile", "Please wait...", false, false);

        //Creating a json array request
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.UPDATE_USER_URL,
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

                String _email = sp.getString(Config.USER_EMAIL_SHARED_PREF, null);
                params.put("user_email", _email);
                params.put("user_name", etUserName.getText().toString());
                params.put("user_mobile", etMobile.getText().toString());
                params.put("user_address", etAddress.getText().toString());

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
                            ft.replace(R.id.content_frame, new HomeFragment(), "O_HOME_FRAG");
                            ft.commit();
                        } else {
                            //do nothing
                        }

                    }
                });
        builder.show();
    }
}
