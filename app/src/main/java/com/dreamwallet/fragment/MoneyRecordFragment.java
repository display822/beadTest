package com.dreamwallet.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamwallet.R;
import com.dreamwallet.activity.AddRecordActivity;
import com.dreamwallet.databinding.FragmentMoneyRecordBinding;
import com.dreamwallet.entity.MoneyRecord;
import com.dreamwallet.util.RecordDao;
import com.example.skn.framework.base.BaseFragment;
import com.hankkin.library.RefreshSwipeMenuListView;
import com.hankkin.library.SwipeMenu;
import com.hankkin.library.SwipeMenuCreator;
import com.hankkin.library.SwipeMenuItem;

import java.text.SimpleDateFormat;
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
    private List<MoneyRecord> datas = new ArrayList<>();

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

        List<MoneyRecord> list = getRecords();
        if(list.size() == 0){
            updateEmptyOrNetErrorView(false, true);
        }else{
            datas.clear();
            datas.addAll(list);
        }
        adapter = new MyAdapter(getActivity(), datas);
        binding.refresh.setAdapter(adapter);
        binding.refresh.setListViewMode(RefreshSwipeMenuListView.HEADER);
        binding.refresh.setOnRefreshListener(this);
        binding.refresh.setMenuCreator(creator);
        binding.refresh.setOnMenuItemClickListener((position, menu, index) ->
        {
            switch (index) {
                case 0: //删除
                    RecordDao dao = new RecordDao(getActivity());
                    dao.deleteRecord(datas.get(position));
                    datas.remove(position);
                    adapter.setDatas(datas);
                    adapter.notifyDataSetChanged();
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
            rejectItem.setWidth(160);
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
        List<MoneyRecord> list = getRecords();

        if(list.size() == 0){
            updateEmptyOrNetErrorView(false, true);
        }else{
            datas.clear();
            datas.addAll(list);
            adapter.setDatas(datas);
            adapter.notifyDataSetChanged();
            updateEmptyOrNetErrorView(true, true);
        }
        binding.refresh.complete();
    }

    private List<MoneyRecord> getRecords(){
        RecordDao dao = new RecordDao(getActivity());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, mYear);
        c.set(Calendar.MONTH, mMonth);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR, mYear);
        ca.set(Calendar.MONTH, mMonth);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());

        List<MoneyRecord> moneyRecords = dao.selectRecordByDate(first, last);
        int moneyIn=0;
        int moneyOut=0;
        for(MoneyRecord m: moneyRecords){
            if(m.getType()==1){
                //收入
                moneyIn += m.getMoney();
            }else{
                moneyOut += m.getMoney();
            }
        }

        binding.incomeNum.setText(String.valueOf(moneyIn));
        binding.outpayNum.setText(String.valueOf(moneyOut));
        return moneyRecords;
    }

    @Override
    public void onLoadMore() {
        binding.refresh.complete();
    }

    class MyAdapter extends BaseAdapter {

        List<MoneyRecord> datas;
        Context mContext;

        public MyAdapter(Context context, List<MoneyRecord> data){
            mContext = context;
            datas = data;
        }

        public void setDatas(List<MoneyRecord> data){
            datas = data;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.record_item_two, parent, false);

                holder = new ViewHolder();
                holder.comment = convertView.findViewById(R.id.record_type);
                holder.money = convertView.findViewById(R.id.record_money);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            MoneyRecord m = datas.get(position);
            holder.comment.setText(m.getComment());
            if(m.getType() == 2){
                holder.money.setText("-"+m.getMoney());
            }else{
                holder.money.setText(""+m.getMoney());
            }

            return convertView;
        }

        class ViewHolder {
            ImageView iv;
            TextView comment;
            TextView money;
        }
    }
}
