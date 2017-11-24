package com.bead.dreamwallet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bead.dreamwallet.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

/**
 * Created by DOY on 2017/7/24.
 */
public class MyTagAdapter extends TagAdapter<String> {

    public MyTagAdapter(String[] datas) {
        super(datas);
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        TextView tvHeader = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow_text, parent, false);
        tvHeader.setText(s);
        return tvHeader;
    }
}
