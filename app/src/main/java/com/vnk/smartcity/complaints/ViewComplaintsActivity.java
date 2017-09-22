package com.vnk.smartcity.complaints;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vnk.smartcity.Config;
import com.vnk.smartcity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewComplaintsActivity extends AppCompatActivity {

    //Creating a List of superheroes
    private List<Complaint> complaints;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaints);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        complaints = new ArrayList<>();

        //Calling method to get data
        getData();
    }

    //This method will get data from the web api
    private void getData() {
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Loading Data", "Please wait...", false, false);

        //Creating a json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.GET_COMPLAINTS_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();

                        //calling method to parse json array
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

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
        adapter = new CardAdapter(complaints, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
