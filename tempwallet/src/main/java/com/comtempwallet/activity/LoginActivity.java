package com.comtempwallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.view.View;

import com.comtempwallet.R;
import com.comtempwallet.databinding.ActivityFloginBinding;
import com.example.skn.framework.base.BaseActivity;

/**
 * Created by Administrator on 2017/12/7 0007.
 *
 */

public class LoginActivity extends BaseActivity {


    ActivityFloginBinding binding;

    private boolean isLoginPage = true;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initVar() {

        setFlagTranslucentStatus();

    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_flogin);

        binding.titleBack.setOnClickListener(v -> finish());

        binding.registerBt.setOnClickListener(v -> {
            if(isLoginPage){
                isLoginPage = false;
                binding.pdLlyt.setVisibility(View.GONE);
                binding.registerLlyt.setVisibility(View.VISIBLE);
                binding.registerBt.setBackgroundResource(R.drawable.main_button_enable);
                binding.registerBt.setTextColor(Color.parseColor("#ffffff"));
                binding.protocolLlyt.setVisibility(View.VISIBLE);
            }else{
                //校验注册
            }
        });

        binding.protocolHtml.setOnClickListener(v->{ });


    }

    @Override
    protected void initData() {

    }
}
