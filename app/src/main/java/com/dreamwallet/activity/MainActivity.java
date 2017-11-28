package com.dreamwallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.base.BaseApplication;
import com.example.skn.framework.update.UpdateHelper;
import com.example.skn.framework.util.AppUtil;
import com.example.skn.framework.util.ToastUtil;
import com.dreamwallet.R;
import com.dreamwallet.fragment.BaseLoansFragment;
import com.dreamwallet.fragment.FindFragment;
import com.dreamwallet.fragment.HomeFragment;
import com.dreamwallet.fragment.MeFragment;
import com.dreamwallet.util.Global;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.util.UserInfo;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private HomeFragment homeFragment;
    private BaseLoansFragment loansFragment;
    private MeFragment meFragment;
    private FindFragment findFragment;
    private RadioGroup rgMain;
    private static boolean isExit = false;
    private static MyHandler myHandler;
    public Toolbar toolbar;
    private Fragment currentFragment;


    public static void startActivity(Activity activity, int index) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("index", index);
        activity.startActivity(intent);
    }


    public void showLoansByPosition(int position) {
        Global.loansPosition = position;
//        ((RadioButton) rgMain.getChildAt(1)).setChecked(true);

        //打开loanActivity
        Intent intent = new Intent(this, LoansActivity.class);
        startActivity(intent);
    }

    public void showFindByPosition(int position) {
        Global.findPosition = position;
//        ((RadioButton) rgMain.getChildAt(2)).setChecked(true);

        Intent intent = new Intent(this, InformationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void initVar() {

        setFlagTranslucentStatus();
        myHandler = new MyHandler();
        homeFragment = HomeFragment.getInstance();
        loansFragment = BaseLoansFragment.getInstance();
        meFragment = MeFragment.getInstance();
        findFragment = FindFragment.getInstance();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int index = intent.getIntExtra("index", -1);
        if (index != -1) {
            ((RadioButton) rgMain.getChildAt(index)).setChecked(true);
        }
    }

    @Override
    protected void init() {
        UserInfo.initUserInfo();
        setContentView(R.layout.activity_main);

        rgMain = (RadioGroup) findViewById(R.id.rg_main);
        rgMain.setOnCheckedChangeListener(this);
        currentFragment = homeFragment;
        ((RadioButton) rgMain.getChildAt(0)).setChecked(true);//默认第一个radioButton被选中

    }

    @Override
    protected void initData() {
        UpdateHelper.update(mActivity, UrlService.VERSION_URL, BaseApplication.getApp().getChannel());
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.rb_home) {
            if (!homeFragment.isAdded())
                getSupportFragmentManager().beginTransaction().add(R.id.layout_main, homeFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(homeFragment).commit();
            currentFragment = homeFragment;
        } else if (i == R.id.rb_loans) {
            if (!loansFragment.isAdded())
                getSupportFragmentManager().beginTransaction().add(R.id.layout_main, loansFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(loansFragment).commit();
            currentFragment = loansFragment;
        } else if (i == R.id.rb_find) {
            if (!findFragment.isAdded())
                getSupportFragmentManager().beginTransaction().add(R.id.layout_main, findFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(findFragment).commit();
            currentFragment = findFragment;
        } else if (i == R.id.rb_me) {
            if (!meFragment.isAdded())
                getSupportFragmentManager().beginTransaction().add(R.id.layout_main, meFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(meFragment).commit();
            currentFragment = meFragment;
        }
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

    static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    }


}
