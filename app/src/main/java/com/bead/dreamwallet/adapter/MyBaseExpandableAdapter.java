package com.bead.dreamwallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * 基类适配器 MyBaseExpandableAdapter
 *
 * @author zj
 */
public abstract class MyBaseExpandableAdapter<T, V> extends BaseExpandableListAdapter {

    protected Context mContext;
    protected List<T> list;
    protected LayoutInflater mInflater;
    protected int groupLayoutId;
    protected int childLayoutId;

    public MyBaseExpandableAdapter(Context context, List<T> list, int groupLayoutId, int childLayoutId) {
        this.mContext = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
        this.groupLayoutId = groupLayoutId;
        this.childLayoutId = childLayoutId;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public T getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    public abstract V getChild(int groupPosition, int childPosition);


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, groupLayoutId, mInflater);
        bindGroupData(viewHolder, getGroup(groupPosition), groupPosition, isExpanded);
        return viewHolder.getConvertView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, childLayoutId, mInflater);
        bindChildData(viewHolder, getChild(groupPosition, childPosition), groupPosition, childPosition);
        return viewHolder.getConvertView();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 父控件中绑定数据
     *
     * @param viewHolder    view
     * @param item          实体数据
     * @param groupPosition
     */
    public abstract void bindGroupData(ViewHolder viewHolder, T item, int groupPosition, boolean isExpanded);

    /**
     * 子控件中绑定数据
     *
     * @param viewHolder    view
     * @param item          实体数据
     * @param groupPosition
     * @param childPosition
     */
    public abstract void bindChildData(ViewHolder viewHolder, V item, int groupPosition, int childPosition);

    /**
     * 外部传入的数据，刷新数据
     */
    public void refreshData(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
