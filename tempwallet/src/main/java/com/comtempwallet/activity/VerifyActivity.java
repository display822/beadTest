package com.comtempwallet.activity;

import android.databinding.DataBindingUtil;

import com.comtempwallet.R;
import com.comtempwallet.databinding.ActivityVerifyBinding;
import com.example.skn.framework.base.BaseActivity;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class VerifyActivity extends BaseActivity {

    ActivityVerifyBinding binding;



    @Override
    protected void initVar() {
        setFlagTranslucentStatus();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initData() {

    }
}
