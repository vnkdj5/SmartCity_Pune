package com.vnk.smartcity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
    private int type_id_spinner;

    private String UPLOAD_URL =Config.SERVER_URL+"/upload.php";

    private String KEY_IMAGE = "image";
    private String KEY_CATID = "cat_id";
    private  String KEY_SUBCAT="subcategory";
    private String KEY_ADDRESS="address";
    private  String KEY_PIN="pincode";
    private  String KEY_DESC="description";
    private  String KEY_USERID="user_id";

    private int categoryId,pincode;
    private  String subcategory,address,description,user_email,complaintId;

    private ImageView imageView;
    private  Button btnuploadComplaint,btnChooseImg;
    private EditText editTextaddress,editTextPincode,editTextDescription;
    private Spinner spinner;
    private Spinner subCatagorySpinner;
    public ComplaintsFragment() {
        // Required empty public constructor
        categoryId=0;//By default first Item is displayed

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        type_id_spinner = this.getArguments().getInt("spinner_id");
       final View v= inflater.inflate(R.layout.fragment_complaints, container, false);

        //---------Declartion of resources in XML
        spinner = (Spinner) v.findViewById(R.id.spinner);
        subCatagorySpinner=(Spinner) v.findViewById(R.id.spinner2);
        editTextaddress=(EditText) v.findViewById(R.id.editTextLocation);
        editTextPincode=(EditText) v.findViewById(R.id.editTextPincode);
        editTextDescription=(EditText) v.findViewById(R.id.editTextDescription);
        imageView=(ImageView) v.findViewById(R.id.imageViewComplaint);
        btnChooseImg=(Button) v.findViewById(R.id.buttonPhoto);
        btnuploadComplaint =(Button) v.findViewById(R.id.submit_complaint_button);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.main_category, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setSelection(type_id_spinner);


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
                categoryId=pos; //Assign catagory id for DB Operation
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
/*
        subCatagorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
            subcategory=subCatagorySpinner.getItemAtPosition(pos).toString(); //Assign subcatagory here
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       */ // Inflate the layout for this fragment


        btnuploadComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address=editTextaddress.getText().toString();
                pincode=Integer.parseInt(editTextPincode.getText().toString());
                description=editTextDescription.getText().toString();
                subcategory=subCatagorySpinner.getSelectedItem().toString();
                SharedPreferences sp=getActivity().getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                user_email=sp.getString(Config.USER_EMAIL_SHARED_PREF, null);
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
                        complaintId=s;
                        //Showing toast message of the response
                        Toast.makeText(getActivity(), "Submitted Successfully." , Toast.LENGTH_LONG).show();
                        showSuccessDialog();
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
               params.put(KEY_CATID, String.valueOf(categoryId));
                params.put(KEY_SUBCAT,subcategory);
                params.put(KEY_ADDRESS,address);
                params.put(KEY_PIN, String.valueOf(pincode));
                params.put(KEY_DESC,description);
                params.put(KEY_USERID,user_email);
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

    public void showSuccessDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());
        builder.setTitle("Submitted Successfully.");
        builder.setMessage("Your Complaint Id: "+complaintId+" .");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, new FeedbackFragment(), "SubmitedFrags");
                        ft.commit();
                    }
                });
        builder.show();
    }
}
