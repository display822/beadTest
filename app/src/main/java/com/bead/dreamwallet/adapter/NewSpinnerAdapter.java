package com.bead.dreamwallet.adapter;

import android.content.Context;


import com.bead.dreamwallet.R;

import java.util.List;

/**
 * Created by DIY on 2016/10/27.
 */
public class NewSpinnerAdapter extends MyBaseAdapter<String> {


    public NewSpinnerAdapter(Context context, List<String> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    public void bindData(ViewHolder viewHolder, String item, int position) {
        viewHolder.setText(R.id.spinner_item_label,list.get(position));
    }

}
