package com.vnk.smartcity.officer;

import android.view.View;

/**
 * Created by root on 26/9/17.
 */
public interface RecyclerViewItemClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
