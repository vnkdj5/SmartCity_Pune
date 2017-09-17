package com.vnk.smartcity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComplaintsFragment extends Fragment {

    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL =Config.SERVER_URL+"/upload.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    private ImageView imageView;
   private  Button btnuploadComplaint,btnChooseImg;
    public ComplaintsFragment() {
        // Required empty public constructor

    }
////===============Change static to dynan=mic data loading
EditText editTextaddress;
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

        editTextaddress=(EditText) v.findViewById(R.id.editTextLocation);
        imageView=(ImageView) v.findViewById(R.id.imageViewComplaint);
        btnChooseImg=(Button) v.findViewById(R.id.buttonPhoto);
        btnuploadComplaint =(Button) v.findViewById(R.id.submit_complaint_button);
        btnuploadComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });



        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Complaints");
    }

    //Image chooser code starts here
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this.getActivity(),"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getActivity(), s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        Log.d("COMPLAINTS FRAGMENT",volleyError.toString());
                        //Showing toast
                        Toast.makeText(getActivity().getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
               // String name = editTextName.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, "test");
//Add other parameters here and create schema
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
