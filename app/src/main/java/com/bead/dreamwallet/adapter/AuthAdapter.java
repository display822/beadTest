package com.bead.dreamwallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bead.dreamwallet.R;

import java.util.List;

/**
 * Created by DIY on 2017/8/31.
 */

public class AuthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> nameData;
    private List<Integer> ImgResData;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public AuthAdapter(List<String> nameData, List<Integer> imgResData, Context context) {
        this.nameData = nameData;
        this.ImgResData = imgResData;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_auth_layout, parent, false);
        return new AuthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AuthViewHolder) holder).tv.setText(nameData.get(position));
        ((AuthViewHolder) holder).iv.setImageResource(ImgResData.get(position));
        ((AuthViewHolder) holder).itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return nameData.size();
    }

    private class AuthViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView tv;
        public ImageView iv;

        public AuthViewHolder(View itemView) {
            super(itemView);
            this.iv = itemView.findViewById(R.id.iv);
            this.tv = itemView.findViewById(R.id.tv);
            this.itemView = itemView.findViewById(R.id.ll_item);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
