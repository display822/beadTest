package com.comtempwallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.comtempwallet.R;
import com.comtempwallet.databinding.ActivityMainBinding;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.util.AppUtil;
import com.example.skn.framework.util.ToastUtil;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private static MyHandler myHandler;
    private static boolean isExit = false;
    private ActivityMainBinding binding;


    public static void startActivity(Activity activity, int index) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("index", index);
        activity.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int index = intent.getIntExtra("index", -1);
        if (index != -1) {
            ((RadioButton) binding.rgMain.getChildAt(index)).setChecked(true);
        }
    }

    @Override
    protected void initVar() {

        setFlagTranslucentStatus();
        myHandler = new MyHandler();

    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.rgMain.setOnCheckedChangeListener(this);

        ((RadioButton) binding.rgMain.getChildAt(0)).setChecked(true);
    }

    @Override
    protected void initData() {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtil.show("再按一次退出程序");
            // 利用handler延迟发送更改状态信息
            myHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            AppUtil.destroyApp();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        if(checkedId == R.id.rb_loans){

        }else if(checkedId == R.id.rb_repay){

        }else if(checkedId == R.id.rb_mine){

        }

    }

    static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    }
}
