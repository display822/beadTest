package com.beadwallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 基类适配器
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> list;
    protected int layoutId;
    protected boolean isViewGroup = false;
    protected LayoutInflater mInflater;
    protected LayoutProvider layoutProvider;
    private int count = -1;

    public MyBaseAdapter(Context context, List<T> list, int layoutId) {
        this.mContext = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
    }

    public MyBaseAdapter(Context context, List<T> list, int count, int layoutId) {
        this(context, list, layoutId);
        this.count = count;
    }

    public MyBaseAdapter(Context context, boolean isViewGroup, List<T> list, int layoutId) {
        this(context, list, layoutId);
        this.isViewGroup = isViewGroup;

    }

    public MyBaseAdapter(Context context, List<T> list, LayoutProvider<T> layoutProvider) {
        this.mContext = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
        this.layoutProvider = layoutProvider;
    }

    @Override
    public int getCount() {
        if (count == -1) {
            return list == null ? 0 : list.size();
        } else {
            return list.size() - count;
        }
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, isViewGroup, parent, convertView, layoutProvider == null ? layoutId : layoutProvider.getLayout(layoutProvider.getLayoutType(position, getItem(position))), mInflater);
        bindData(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    /**
     * 控件中绑定数据
     *
     * @param viewHolder view
     * @param item       实体数据
     * @param position
     */
    public abstract void bindData(ViewHolder viewHolder, T item, int position);

    /**
     * 外部传入的数据，刷新数据
     */
    public void refreshData(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public synchronized List<T> getList() {
        return list;
    }

    @Override
    public int getViewTypeCount() {
        return layoutProvider == null ? 1 : layoutProvider.getLayoutCount();
    }

    @Override
    public int getItemViewType(int position) {
        return layoutProvider == null ? 0 : layoutProvider.getLayoutType(position, getItem(position));
    }
}
