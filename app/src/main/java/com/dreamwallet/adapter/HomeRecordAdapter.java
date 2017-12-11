package com.dreamwallet.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dreamwallet.R;
import com.dreamwallet.databinding.RecordItemHomeBinding;
import com.dreamwallet.entity.MoneyRecord;

import java.util.List;

/**
 * Created by hf
 */
public class HomeRecordAdapter extends RecyclerView.Adapter<HomeRecordAdapter.RecordViewHolder> {
    private List<MoneyRecord> data;
    private Context context;

    public HomeRecordAdapter(List<MoneyRecord> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecordItemHomeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.record_item_home, parent, false);
        return new RecordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        MoneyRecord moneyRecord = data.get(position);

        holder.binding.setMoneyRecord( moneyRecord);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecordViewHolder extends RecyclerView.ViewHolder {

        private RecordItemHomeBinding binding;

        public RecordViewHolder(RecordItemHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
