package com.dreamwallet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dreamwallet.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

/**
 * Created by DOY on 2017/7/24.
 *
 */
public class MyTagAdapter extends TagAdapter<String> {

    public MyTagAdapter(String[] datas) {
        super(datas);
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        int tagPosition = position % 3;
        int resXml = R.layout.item_flow_text;
        if(tagPosition == 1){
            //红色
            resXml = R.layout.item_flow_textred;
        }else if(tagPosition == 2){
            //蓝色
            resXml = R.layout.item_flow_textblue;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(resXml, parent, false);
        TextView tvHeader = (TextView) view.findViewById(R.id.loans_label);
        tvHeader.setText(s);
        return view;
    }
}
