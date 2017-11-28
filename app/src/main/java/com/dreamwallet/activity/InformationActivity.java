package com.dreamwallet.activity;

import com.dreamwallet.R;
import com.dreamwallet.fragment.FindFragment;
import com.example.skn.framework.base.BaseActivity;

/**
 * Created by Administrator on 2017/11/28 0028.
 */

public class InformationActivity extends BaseActivity {


    @Override
    protected void initVar() {
        setFlagTranslucentStatus();
        setContentView(R.layout.activity_information);
    }

    @Override
    protected void init() {
        getSupportFragmentManager().beginTransaction().add(R.id.layout_information, FindFragment.getInstance()).commit();
    }

    @Override
    protected void initData() {

    }
}
