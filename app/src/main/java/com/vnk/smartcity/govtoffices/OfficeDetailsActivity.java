package com.vnk.smartcity.govtoffices;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnk.smartcity.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OfficeDetailsActivity extends AppCompatActivity {
    private String office_name;
    private int position;
    private List<String> addresses, contacts, times, latitudes, longitudes;
    private TextView officeName, officeAddress, officeContact, officeTime;
    private ImageView mapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_details);

        officeName = (TextView) findViewById(R.id.officeName);
        officeAddress = (TextView) findViewById(R.id.officeAddress);
        officeContact = (TextView) findViewById(R.id.officeContact);
        officeContact = (TextView) findViewById(R.id.officeContact);
        officeTime = (TextView) findViewById(R.id.officeTime);
        mapImage = (ImageView) findViewById(R.id.imageViewOfficeMap);

        office_name = getIntent().getStringExtra("office_name");
        position = getIntent().getIntExtra("position", 0);

        addresses = new ArrayList<>();
        contacts = new ArrayList<>();
        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        longitudes = new ArrayList<>();

        addresses = Arrays.asList(getResources().getStringArray(R.array.office_address));
        contacts = Arrays.asList(getResources().getStringArray(R.array.office_contacts));
        times = Arrays.asList(getResources().getStringArray(R.array.office_times));
        latitudes = Arrays.asList(getResources().getStringArray(R.array.latitudes));
        longitudes = Arrays.asList(getResources().getStringArray(R.array.longitudes));

        officeName.setText(office_name);
        officeAddress.setText(addresses.get(position));
        officeContact.setText(contacts.get(position));
        officeTime.setText(times.get(position));
        new GetMapImage().execute(latitudes.get(position), longitudes.get(position));

        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String urlbegin = "geo:" + latitudes.get(position) + "," + longitudes.get(position);
                String urlend = "?q=" + Uri.encode(latitudes.get(position) + "," + longitudes.get(position) + "(" + office_name + ")");

                Uri mapUri = Uri.parse(urlbegin + urlend);

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);

                mapIntent.setPackage("com.google.android.apps.maps");

                startActivity(mapIntent);
            }
        });

    }

    public void onBackPressed() {
        Intent back = new Intent(OfficeDetailsActivity.this, GovtOfficeActivity.class);
        startActivity(back);
        finish();
    }

    private class GetMapImage extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = "http://maps.google.com/maps/api/staticmap?center=" + urls[0] + "," + urls[1] +
                    "&zoom=17&size=600x500&sensoe=false&markers=size:large|color:red|" + urls[0] + "," + urls[1];

            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mapImage.setImageBitmap(bitmap);
        }
    }

}
