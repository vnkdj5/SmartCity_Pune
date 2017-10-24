package com.vnk.smartcity.emergency;

/**
 * Created by root on 9/10/17.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnk.smartcity.R;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] descr;
    private final Integer imgid;

    public CustomListAdapter(Activity context, String[] itemname, String[] descr, Integer imgid) {
        super(context, R.layout.emergency_list, itemname);


        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
        this.descr = descr;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.emergency_list, null, true);

        TextView txtTitle = rowView.findViewById(R.id.item);
        ImageView imageView = rowView.findViewById(R.id.icon);
        TextView extratxt = rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid);
        extratxt.setText("" + descr[position]);
        return rowView;

    }
}