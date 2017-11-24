package com.dreamwallet.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dreamwallet.R;
import com.dreamwallet.databinding.ItemTitleLoansBinding;
import com.dreamwallet.entity.LoansTitleEntity;
import com.dreamwallet.util.Global;

import java.util.List;

/**
 * Created by hf
 */
public class LoansTitleAdapter extends RecyclerView.Adapter<LoansTitleAdapter.LoansTitleViewHolder> {

    private Context context;
    private List<LoansTitleEntity> data;
    private int currentPosition = 0;
    private OnItemClickListener onItemClickListener;

    public LoansTitleAdapter(Context context, List<LoansTitleEntity> data, int currentPosition, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.data = data;
        this.currentPosition = currentPosition;
        this.onItemClickListener = onItemClickListener;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    @Override
    public LoansTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTitleLoansBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_title_loans, parent, false);
        return new LoansTitleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(LoansTitleViewHolder holder, int position) {
        holder.binding.setTitle(data.get(position).getSortAllName());
        if (position == currentPosition) {
            holder.binding.setIsShow(true);
            holder.binding.tvTitle.setTextColor(context.getResources().getColor(R.color.toolbar));
        } else {
            holder.binding.setIsShow(false);
            holder.binding.tvTitle.setTextColor(context.getResources().getColor(R.color.color_424242));

        }
        holder.binding.getRoot().setOnClickListener(view -> {
            Global.loansPosition = position;
            setCurrentPosition(position);//刷新头部
            onItemClickListener.onItemClick(position);
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class LoansTitleViewHolder extends RecyclerView.ViewHolder {
        private ItemTitleLoansBinding binding;

        public LoansTitleViewHolder(ItemTitleLoansBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
