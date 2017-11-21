package com.example.skn.framework.util;

import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.skn.framework.R;


public class ToolBarUtil {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private AppCompatActivity activity;
    private boolean isShow = true;

    private ToolBarUtil(AppCompatActivity activity) {
        this.toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        this.toolbar_title = (TextView) activity.findViewById(R.id.tv_title);
        this.activity = activity;
    }

    public static ToolBarUtil getInstance(AppCompatActivity activity) {
        return new ToolBarUtil(activity);
    }

    public Toolbar build() {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(isShow);
        if (isShow)
            toolbar.setNavigationIcon(activity.getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(view -> activity.onBackPressed());
        return toolbar;
    }

    public ToolBarUtil isShow(boolean isShow) {
        this.isShow = isShow;
        return this;
    }

    public ToolBarUtil addView(@LayoutRes int layoutId) {
        View view = LayoutInflater.from(activity).inflate(layoutId, null);
        toolbar.addView(view);
        return this;
    }

    public ToolBarUtil removeView(@LayoutRes int layoutId) {
        View view = LayoutInflater.from(activity).inflate(layoutId, null);
        toolbar.removeView(view);
        return this;
    }

    public ToolBarUtil setLogo(int id) {
        toolbar.setLogo(id);
        return this;
    }

    public ToolBarUtil setTitle(int id) {
        toolbar.setTitle("");
        toolbar_title.setVisibility(View.VISIBLE);
        toolbar_title.setText(id);
        return this;
    }

    public ToolBarUtil setTitle(String title) {
        toolbar.setTitle("");
        toolbar_title.setVisibility(View.VISIBLE);
        toolbar_title.setText(title);
        return this;
    }

    public ToolBarUtil setSubtitle(int id) {
        toolbar.setSubtitle(id);
        return this;
    }

    public ToolBarUtil setSubtitle(String subtitle) {
        toolbar.setSubtitle(subtitle);
        return this;
    }

}
