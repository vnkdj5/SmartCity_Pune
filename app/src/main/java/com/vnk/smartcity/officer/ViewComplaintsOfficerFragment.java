package com.vnk.smartcity.officer;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vnk.smartcity.Config;
import com.vnk.smartcity.R;
import com.vnk.smartcity.complaints.CardAdapter;
import com.vnk.smartcity.complaints.Complaint;

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
public class ViewComplaintsOfficerFragment extends Fragment {

    //Creating a List of complaints
    private List<Complaint> complaints;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private String officer_email;
    public ViewComplaintsOfficerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_complaints_officer, container, false);
        recyclerView = v.findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        //Initializing our superheroes list
        complaints = new ArrayList<>();

        //Calling method to get data
        getData();

        return v;
    }

    //This method will get data from the web api
    private void getData() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...", false, false);

        //Creating a json array request
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.GET_COMPLAINTS_OFFICER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        JSONArray arrayResponse = null;
                        try {
                            arrayResponse = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //calling method to parse json array
                        parseData(arrayResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                SharedPreferences sp = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                officer_email = sp.getString(Config.USER_EMAIL_SHARED_PREF, null);
                params.put("officer_email", officer_email);


                //returning parameter
                return params;
            }
        };

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    //This method will parse json data
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            Complaint complaint = new Complaint();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                complaint.setImageurl(json.getString(Config.TAG_IMAGE_URL));
                complaint.setComplaintId(json.getString(Config.TAG_COMPLAINT_ID));
                complaint.setCat_id(json.getInt(Config.TAG_CAT_ID));
                complaint.setSubCategory(json.getString(Config.TAG_SUB_CAT));
                complaint.setAddress(json.getString(Config.TAG_ADDRESS));
                complaint.setDescription(json.getString(Config.TAG_DESCRIPTION));
                complaint.setStatus(json.getString(Config.TAG_STATUS));
                complaint.setSubmitDate(json.getString(Config.TAG_SUBMIT_DATE));
             /*   ArrayList<String> powers = new ArrayList<String>();

                JSONArray jsonArray = json.getJSONArray(Config.TAG_POWERS);

                for(int j = 0; j<jsonArray.length(); j++){
                    powers.add(((String) jsonArray.get(j))+"\n");
                }
                complaint.setPowers(powers);
*/

            } catch (JSONException e) {
                e.printStackTrace();
            }
            complaints.add(complaint);
        }

        //Finally initializing our adapter
        adapter = new CardAdapter(complaints, getActivity());

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(getActivity(), recyclerView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //     Toast.makeText(getActivity(),"CLICK "+position,Toast.LENGTH_SHORT).show();
                final Complaint com = complaints.get(position);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = UpdateComplaintFragment.newInstance(com);
                ft.addToBackStack("home");
                ft.replace(R.id.content_officer, fragment);
                ft.commit();

            }

            @Override
            public void onLongClick(View view, final int position) {
                final Complaint com = complaints.get(position);

                final CharSequence[] items = {
                        "Update Status", "Close"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Option");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        Fragment fragment = UpdateComplaintFragment.newInstance(com);
                        ft.addToBackStack("home");
                        ft.replace(R.id.content_officer, fragment);
                        ft.commit();
                        //  Toast.makeText(getActivity(), "Long Click "+item, Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Complaints");
    }
}
