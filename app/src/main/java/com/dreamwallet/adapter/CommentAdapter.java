package com.dreamwallet.adapter;

import android.content.Context;
import android.view.View;

import com.dreamwallet.R;
import com.dreamwallet.entity.CommentBean;

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
