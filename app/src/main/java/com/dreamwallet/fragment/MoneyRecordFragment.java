package com.dreamwallet.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;

import com.dreamwallet.R;
import com.dreamwallet.activity.AddRecordActivity;
import com.dreamwallet.databinding.FragmentMoneyRecordBinding;
import com.dreamwallet.entity.MoneyRecord;
import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.util.ToastUtil;
import com.hankkin.library.RefreshSwipeMenuListView;
import com.hankkin.library.SwipeMenu;
import com.hankkin.library.SwipeMenuCreator;
import com.hankkin.library.SwipeMenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/7 0007.
 */

public class MoneyRecordFragment extends BaseFragment implements RefreshSwipeMenuListView.OnRefreshListener {

    FragmentMoneyRecordBinding binding;
    private Date currDate;
    private MyAdapter adapter;
    private int mYear,mMonth,mDay;

    public static MoneyRecordFragment getInstance(){
        return new MoneyRecordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_money_record, container, false);


        initEmptyOrNetErrorView(binding.getRoot(), R.id.viewStub, v -> {});

        initView();
        return binding.getRoot();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            //查询数据库
        }
    }

    private void initView(){
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        adapter = new MyAdapter();
        binding.refresh.setAdapter(adapter);
        binding.refresh.setListViewMode(RefreshSwipeMenuListView.HEADER);
        binding.refresh.setOnRefreshListener(this);
        binding.refresh.setMenuCreator(creator);
        binding.refresh.setOnMenuItemClickListener((position, menu, index) ->
        {
            switch (index) {
                case 0: //删除
                    ToastUtil.show("position:"+position+"-index:"+index);
                    break;
            }
        });

        binding.setAddClick(v -> {
            Intent it = new Intent(getActivity(), AddRecordActivity.class);
            startActivity(it);
        });

        binding.setDateClick(v -> {

            //弹出日期选择
            new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mYear = year;
                    mMonth = month;
                    mDay = dayOfMonth;
                    binding.selectYear.setText(String.valueOf(year));
                    binding.selectMonth.setText(String.valueOf(month+1));
                }
            }, mYear, mMonth, mDay).show();
        });
    }


    SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {
            // 创建滑动选项
            SwipeMenuItem rejectItem = new SwipeMenuItem(getActivity());
            // 设置选项背景
            rejectItem.setBackground(new ColorDrawable(getResources().getColor(R.color.color_FFFA7868)));
            // 设置选项宽度
            rejectItem.setWidth(100);
            // 设置选项标题
            rejectItem.setTitle("删除");
            // 设置选项标题
            rejectItem.setTitleSize(16);
            // 设置选项标题颜色
            rejectItem.setTitleColor(Color.parseColor("#ffffff"));
            // 添加选项
            menu.addMenuItem(rejectItem);
        }
    };

    @Override
    public void onRefresh() {



    }

    private List<MoneyRecord> getRecords(){
        return new ArrayList<MoneyRecord>();
    }

    @Override
    public void onLoadMore() {

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
