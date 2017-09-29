package com.vnk.smartcity.officer;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
 * Use the {@link UpdateComplaintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateComplaintFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    NetworkImageView imageView;
    TextView textViewComplId;
    TextView textViewCategory;
    TextView textViewTitle;
    TextView textViewDescription;
    TextView textViewAddress;
    Spinner spinnerStatus;
    TextView textViewSubmitDate;
    //Creating a List of complaints
    private List<Complaint> complaints;
    private List<String> complaintIds;
    private Complaint complaint;
    private ImageLoader imageLoader;
    private Button buttonUpdateStatus;
    private String officer_email;

    public UpdateComplaintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UpdateComplaintFragment.
     */

    public static UpdateComplaintFragment newInstance(Complaint param1) {
        UpdateComplaintFragment fragment = new UpdateComplaintFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    public static int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            complaint = (Complaint) getArguments().getSerializable(ARG_PARAM1);
            //  Toast.makeText(getActivity(), complaint.getSubCategory(),Toast.LENGTH_SHORT).show();
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_update_complaint, container, false);
        complaints = new ArrayList<>();
        complaintIds = new ArrayList<>();
        findViewByIds(itemView);
        loadContent();
        getData();


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.status_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerStatus.setAdapter(adapter);
        spinnerStatus.setSelection(getSpinnerIndex(spinnerStatus, complaint.getStatus()));
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
        return itemView;
    }

    public void loadContent() {
        imageLoader = CustomVolleyRequest.getInstance(getContext()).getImageLoader();
        imageLoader.get(complaint.getImageurl(), ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        imageView.setImageUrl(complaint.getImageurl(), imageLoader);
        textViewComplId.setText(complaint.getComplaintId());
        textViewCategory.setText(String.valueOf(complaint.getCat_id()));
        textViewTitle.setText(complaint.getSubCategory());
        textViewDescription.setText(complaint.getDescription());
        textViewAddress.setText(complaint.getAddress());
        textViewSubmitDate.setText(complaint.getSubmitDate());
    }

    private void findViewByIds(View itemView) {
        imageView = itemView.findViewById(R.id.imageViewCMP1);
        textViewComplId = itemView.findViewById(R.id.textViewComId1);
        textViewCategory = itemView.findViewById(R.id.textViewCat1);
        textViewTitle = itemView.findViewById(R.id.textViewTitle1);
        textViewDescription = itemView.findViewById(R.id.textViewDESCR1);
        textViewAddress = itemView.findViewById(R.id.textViewADDRESS1);
        spinnerStatus = itemView.findViewById(R.id.spinnerSTATUS1);
        textViewSubmitDate = itemView.findViewById(R.id.textViewSubmitDate1);
        buttonUpdateStatus = itemView.findViewById(R.id.buttonUpStatus);
        // spinnerComplaintID=(Spinner) itemView.findViewById(R.id.spinnerCID);
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

                officer_email = sp.getString(Config.USER_EMAIL_SHARED_PREF, null);
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

    //This method will get data from the web api
    private void getData() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading Complaints", "Please wait...", false, false);

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
                complaintIds.add(complaint.getComplaintId()); //create list for spinner content
            } catch (JSONException e) {
                e.printStackTrace();
            }

            complaints.add(complaint);
        }
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
