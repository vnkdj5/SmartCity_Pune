package com.vnk.smartcity.complaints;

/**
 * Created by root on 21/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.vnk.smartcity.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    //List of complaints
    List<Complaint> complaints;
    private ImageLoader imageLoader;
    private Context context;


    public CardAdapter(List<Complaint> complaints, Context context) {
        super();
        //Getting all the superheroes
        this.complaints = complaints;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complaint_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Complaint complaint = complaints.get(position);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(complaint.getImageurl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.imageView.setImageUrl(complaint.getImageurl(), imageLoader);
        holder.textViewComplId.setText(complaint.getComplaintId());
        holder.textViewCategory.setText(String.valueOf(complaint.getCat_id()));
        holder.textViewTitle.setText(complaint.getSubCategory());
        holder.textViewDescription.setText(complaint.getDescription());
        holder.textViewAddress.setText(complaint.getAddress());
        holder.textViewSubmitDate.setText(complaint.getSubmitDate());


        holder.textViewStatus.setText(complaint.getStatus());
    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView imageView;
        public TextView textViewComplId;
        public TextView textViewCategory;
        public TextView textViewTitle;
        public TextView textViewDescription;
        public TextView textViewAddress;
        public TextView textViewStatus;
        public TextView textViewSubmitDate;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCMP);
            textViewComplId = itemView.findViewById(R.id.textViewComId);
            textViewCategory = itemView.findViewById(R.id.textViewCat);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDESCR);
            textViewAddress = itemView.findViewById(R.id.textViewADDRESS);
            textViewStatus = itemView.findViewById(R.id.textViewSTATUS);
            textViewSubmitDate = itemView.findViewById(R.id.textViewSubmitDate);
        }
    }

}