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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vnk.smartcity.Config;
import com.vnk.smartcity.R;
import com.vnk.smartcity.complaints.Complaint;
import com.vnk.smartcity.complaints.CustomVolleyRequest;

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
public class UpdateComMainFragment extends Fragment {

    List<String> comList;
    Complaint complaint;
    NetworkImageView imageView;
    TextView textViewComplId;
    TextView textViewCategory;
    TextView textViewTitle;
    TextView textViewDescription;
    TextView textViewAddress;
    Spinner spinnerStatus;
    TextView textViewSubmitDate;
    private Spinner complaintSpinner;
    private FrameLayout contentFrame;
    private Button buttonUpdateStatus;
    private String complaint_id;
    private ImageLoader imageLoader;

    public UpdateComMainFragment() {
        // Required empty public constructor
        comList = new ArrayList<>();
        complaint = new Complaint();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_com_main, container, false);

        complaintSpinner = v.findViewById(R.id.spinner3);


        imageView = v.findViewById(R.id.imageViewCMP);
        textViewComplId = v.findViewById(R.id.textViewComId);
        textViewCategory = v.findViewById(R.id.textViewCat);
        textViewTitle = v.findViewById(R.id.textViewTitle);
        textViewDescription = v.findViewById(R.id.textViewDESCR);
        textViewAddress = v.findViewById(R.id.textViewADDRESS);
        contentFrame = v.findViewById(R.id.frame_content);
        spinnerStatus = v.findViewById(R.id.spinnerSTATUS11);
        textViewSubmitDate = v.findViewById(R.id.textViewSubmitDate);
        buttonUpdateStatus = v.findViewById(R.id.buttonUpStatus1);
        getComplaintsList();

        complaintSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                complaint_id = adapterView.getSelectedItem().toString();
                contentFrame.setVisibility(View.VISIBLE);
                getComplaint();
                loadContent();
                try {
                    imageLoader = CustomVolleyRequest.getInstance(getContext()).getImageLoader();
                    imageLoader.get(complaint.getImageurl(), ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
                    imageView.setImageUrl(complaint.getImageurl(), imageLoader);
                } catch (Exception e) {
                    imageView.setDefaultImageResId(R.mipmap.logo);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.status_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerStatus.setAdapter(adapter);
        spinnerStatus.setSelection(UpdateComplaintFragment.getSpinnerIndex(spinnerStatus, complaint.getStatus()));
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                complaint.setStatus(spinnerStatus.getItemAtPosition(i).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getActivity(),"Status:"+complaint.getStatus(),Toast.LENGTH_SHORT).show();
                updateStatus();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Update Complaints");
    }

    public void loadContent() {
        /*
        imageLoader = CustomVolleyRequest.getInstance(getContext()).getImageLoader();
        imageLoader.get(complaint.getImageurl(), ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        imageView.setImageUrl(complaint.getImageurl(), imageLoader);
        */
        Toast.makeText(getActivity(), complaint.getImageurl() + "", Toast.LENGTH_SHORT).show();
        textViewComplId.setText(complaint.getComplaintId());
        textViewCategory.setText(String.valueOf(complaint.getCat_id()));
        textViewTitle.setText(complaint.getSubCategory());
        textViewDescription.setText(complaint.getDescription());
        textViewAddress.setText(complaint.getAddress());
        textViewSubmitDate.setText(complaint.getSubmitDate());
    }

    private void getComplaintsList() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading Complaints", "Please wait...", false, false);

        //Creating a json array request
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.GET_COMPLAINTS_IDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        JSONArray arrayResponse = null;
                        try {
                            arrayResponse = new JSONArray(response);

                            for (int i = 0; i < arrayResponse.length(); i++) {
                                JSONObject jobj = arrayResponse.getJSONObject(i);

                                comList.add(jobj.getString("complaint_id"));
                            }

                            complaintSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, comList));
                            //calling method to parse json array
                            //  parseData(arrayResponse);
                            complaintSpinner.setSelection(UpdateComplaintFragment.getSpinnerIndex(complaintSpinner, comList.get(1)));
                            getComplaint();
                            loadContent();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        requestQueue.add(jsonArrayRequest);
    }

    private void getComplaint() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading Complaints", "Please wait...", false, false);

        //Creating a json array request
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.GET_COMPLAINT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            complaint.setImageurl(json.getString(Config.TAG_IMAGE_URL));
                            complaint.setComplaintId(json.getString(Config.TAG_COMPLAINT_ID));
                            complaint.setCat_id(json.getInt(Config.TAG_CAT_ID));
                            complaint.setSubCategory(json.getString(Config.TAG_SUB_CAT));
                            complaint.setAddress(json.getString(Config.TAG_ADDRESS));
                            complaint.setDescription(json.getString(Config.TAG_DESCRIPTION));
                            complaint.setStatus(json.getString(Config.TAG_STATUS));
                            complaint.setSubmitDate(json.getString(Config.TAG_SUBMIT_DATE));

                            //  parseData(arrayResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
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

                params.put("complaint_id", complaint_id);


                //returning parameter
                return params;
            }
        };

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    private void updateStatus() {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Updating Status", "Please wait...", false, false);

        //Creating a json array request
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.UPDATE_STATUS_URL,
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
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                showDialog(obj.getString("message"), obj.getBoolean("error"));
                            } else {
                                showDialog(obj.getString("message"), obj.getBoolean("error"));

                            }
                        } catch (JSONException e) {
                            Log.d("UpdateComplaint", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("UpdateCOmplaintStatus", error.toString());
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
                params.put(Config.TAG_COMPLAINT_ID, complaint.getComplaintId());
                params.put(Config.TAG_STATUS, complaint.getStatus());


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
