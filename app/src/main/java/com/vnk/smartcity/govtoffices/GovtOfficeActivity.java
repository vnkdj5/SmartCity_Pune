package com.vnk.smartcity.govtoffices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vnk.smartcity.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GovtOfficeActivity extends AppCompatActivity {
    private List<String> office = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private String office_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_office);
        listView = (ListView) findViewById(R.id.listOfficeName);

        office = Arrays.asList(getResources().getStringArray(R.array.offices));
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, office);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                office_name = listView.getItemAtPosition(position).toString();
                Intent details = new Intent(GovtOfficeActivity.this, OfficeDetailsActivity.class);
                details.putExtra("office_name", office_name);
                details.putExtra("position", position);
                startActivity(details);
                finish();

            }
        });
    }
}
