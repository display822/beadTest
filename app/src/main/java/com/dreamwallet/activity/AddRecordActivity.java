package com.dreamwallet.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;

import com.dreamwallet.R;
import com.dreamwallet.databinding.ActivityAddMoneyrecordBinding;
import com.dreamwallet.entity.MoneyRecord;
import com.dreamwallet.util.RecordDao;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.util.ToastUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/8 0008.
 *
 */

public class AddRecordActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    public static int MONEY_IN = 1;
    public static int MONEY_OUT = 2;
    ActivityAddMoneyrecordBinding binding;
    private int type = MONEY_OUT;
    private int mYear,mMonth,mDay;

    @Override
    protected void initVar() {
        setFlagTranslucentStatus();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_moneyrecord);

        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void init() {
        binding.titleBack.setOnClickListener(v -> finish());

        binding.setSaveRecord( v -> {
            if(TextUtils.isEmpty(binding.moneyNum.getText().toString())){
                ToastUtil.show("金额不能为空");
            }else{
                saveRecord();
            }
        });

        binding.moneyDate.setText(DateFormat.format("yyyy年-MM月-dd日", System.currentTimeMillis()));
        binding.moneyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出日期选择
                new DatePickerDialog(AddRecordActivity.this, AddRecordActivity.this, mYear, mMonth, mDay).show();
            }
        });

        binding.moneyType.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(AddRecordActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle("请选择账目类型：").setPositiveButton("支出", (out, outType)->{
                            out.dismiss();
                            type = MONEY_OUT;
                            binding.moneyType.setText("支出");
                    }).setNegativeButton("收入", (in, inType) -> {
                        in.dismiss();
                        type = MONEY_IN;
                        binding.moneyType.setText("收入");
                    }).create();
            dialog.show();
        });


    }

    private void saveRecord(){
        RecordDao dao = new RecordDao(this);
        MoneyRecord m = new MoneyRecord();
        m.setType(type);
        m.setMoney(Integer.valueOf(binding.moneyNum.getText().toString()));
        m.setRecord_date(DateFormat.format("yyyy-MM-dd", new Date(mYear-1900, mMonth, mDay)).toString());
        m.setComment(binding.comment.getText().toString());
        dao.insertRecord(m);

        ToastUtil.show("添加成功");
        finish();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;
        binding.moneyDate.setText(DateFormat.format("yyyy年-MM月-dd日", new Date(year-1900, month, dayOfMonth)));
    }
}
