package com.dreamwallet.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用适配器的ViewHolder写法
 */
public class ViewHolder {
    //android系统建议我们用SparseArray<E>来代替HashMap<Integer, E>
    private SparseArray<View> views; //key为int情况的特殊处理
    private View convertView;
    private Map<String, Object> objectMap;

    private ViewHolder(int resId, View convertView) {
        this.views = new SparseArray<>();
        this.convertView = convertView;
        convertView.setTag(resId, this);
    }

    public static ViewHolder getViewHolder(Context context, View convertView, int layoutId, LayoutInflater inflater) {
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, null);
            return new ViewHolder(layoutId, convertView);
        }
        return (ViewHolder) convertView.getTag(layoutId);
    }

    public static ViewHolder getViewHolder(Context context, boolean isViewGroup, ViewGroup parent, View convertView, int layoutId, LayoutInflater inflater) {
        if (convertView == null) {
            if (isViewGroup) {
                convertView = inflater.inflate(layoutId, parent, false);
            } else {
                convertView = inflater.inflate(layoutId, null);
            }
            return new ViewHolder(layoutId, convertView);
        }
        return (ViewHolder) convertView.getTag(layoutId);
    }


    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }


    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public void setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
    }

    /**
     * 为TextView设置颜色
     *
     * @param viewId
     * @param color
     * @return
     */
    public void setColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
    }

    /**
     * 设置可见
     *
     * @param viewId
     * @param statu
     */
    public void setVisible(int viewId, int statu) {
        TextView view = getView(viewId);
        view.setVisibility(statu);
    }

    public View getConvertView() {
        return convertView;
    }

    public void putObject(String key, Object value) {
        if (objectMap == null) {
            objectMap = new HashMap<>();
        }
        objectMap.put(key, value);
    }

    public Object getObject(String key) {
        if (objectMap == null) {
            return null;
        }
        return objectMap.get(key);
    }
}
