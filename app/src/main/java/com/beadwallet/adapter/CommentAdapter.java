package com.beadwallet.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beadwallet.R;
import com.beadwallet.databinding.ItemCommentBinding;
import com.beadwallet.entity.CommentBean;
import com.beadwallet.entity.ForumDetailsEntity;

import java.util.List;

/**
 * Created by hf
 */
public class CommentAdapter extends MyBaseAdapter<CommentBean> {

    private boolean isHideLastLine;

    public CommentAdapter(Context context, List<CommentBean> list) {
        super(context, list, R.layout.item_comment);
    }

    public void setHideLastLine(boolean hideLastLine) {
        this.isHideLastLine = hideLastLine;
    }

    @Override
    public void bindData(ViewHolder viewHolder, CommentBean item, int position) {
        viewHolder.setText(R.id.tv_name, item.getName());
        viewHolder.setText(R.id.tv_content, item.getContent());
        View line = viewHolder.getView(R.id.line);
        if (isHideLastLine && position == list.size() - 1) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
        }
    }

}
