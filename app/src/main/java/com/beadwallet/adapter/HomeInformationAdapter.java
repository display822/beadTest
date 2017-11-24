package com.beadwallet.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beadwallet.R;
import com.beadwallet.activity.DetailsActivity;
import com.beadwallet.databinding.ItemHomeInformationBinding;
import com.beadwallet.entity.HomeInformationEntity;
import com.beadwallet.widget.OnNoDoubleClickListener;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by hf
 */
public class HomeInformationAdapter extends RecyclerView.Adapter<HomeInformationAdapter.HomeInformationViewHolder> {
    private List<HomeInformationEntity> data;
    private Context context;

    public HomeInformationAdapter(List<HomeInformationEntity> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public HomeInformationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHomeInformationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_home_information, parent, false);
        return new HomeInformationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(HomeInformationViewHolder holder, int position) {
        HomeInformationEntity homeInformationEntity = data.get(position);
        holder.binding.setData(homeInformationEntity);

        holder.binding.textView.setText(DateFormat.format("yyyy-MM-dd", data.get(position).getCreate_time()));
        Glide.with(holder.binding.iv.getContext()).load(homeInformationEntity.getImg())
                .placeholder(R.drawable.ic_home_infoprefer).error(R.drawable.ic_home_infoprefer).into(holder.binding.iv);
        holder.binding.getRoot().setOnClickListener(new OnNoDoubleClickListener() {
                                                        @Override
                                                        public void onNoDoubleClick(View v) {
                                                            DetailsActivity.startActivity(context, homeInformationEntity.getId());
                                                        }
                                                    }
        );
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class HomeInformationViewHolder extends RecyclerView.ViewHolder {

        private ItemHomeInformationBinding binding;

        public HomeInformationViewHolder(ItemHomeInformationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
