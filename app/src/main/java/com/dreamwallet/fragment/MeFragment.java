package com.dreamwallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamwallet.R;
import com.dreamwallet.activity.ApplyRecordActivity;
import com.dreamwallet.activity.HelpCenterActivity;
import com.dreamwallet.activity.LoginActivity;
import com.dreamwallet.activity.MyFindActivity;
import com.dreamwallet.activity.SettingActivity;
import com.dreamwallet.util.Global;
import com.dreamwallet.util.UserInfo;
import com.example.skn.framework.base.BaseFragment;

/**
 * Created by DOY on 2017/7/18.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {
    private ImageView iv_head;
    private TextView tv_user;
    private ImageView iv_edit;

    public static MeFragment getInstance() {
        return new MeFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);

        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    private void initView(View view) {
        ImageView iv_setting = view.findViewById(R.id.iv_setting);
        iv_head = view.findViewById(R.id.iv_head);
        tv_user = view.findViewById(R.id.tv_user);
        iv_edit = view.findViewById(R.id.iv_edit);
        LinearLayout ll_borrow_record = view.findViewById(R.id.ll_borrow_record);
//        LinearLayout ll_credit_auth = view.findViewById(R.id.ll_credit_auth);
//        ll_credit_auth.setVisibility(View.GONE);
        LinearLayout ll_my_find = view.findViewById(R.id.ll_my_find);
        LinearLayout ll_help = view.findViewById(R.id.ll_help);

        iv_setting.setOnClickListener(this);
        ll_borrow_record.setOnClickListener(this);
        if(Global.hideLoans == 0){
            ll_borrow_record.setVisibility(View.GONE);
            view.findViewById(R.id.mine_line).setVisibility(View.GONE);
        }
//        ll_credit_auth.setOnClickListener(this);
        ll_my_find.setOnClickListener(this);
        ll_help.setOnClickListener(this);
        iv_edit.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserInfo.isLogin()) {
            iv_head.setImageResource(R.drawable.ic_user);
//            iv_edit.setVisibility(View.VISIBLE);
            iv_edit.setOnClickListener(this);
            iv_head.setOnClickListener(null);
            if (TextUtils.isEmpty(UserInfo.name)) {
                tv_user.setText(UserInfo.phone);
            } else {
                tv_user.setText(UserInfo.name);
            }
        } else {
            iv_head.setImageResource(R.drawable.ic_default_head);
            iv_edit.setVisibility(View.GONE);
            iv_head.setOnClickListener(this);
            iv_edit.setOnClickListener(null);
            tv_user.setText(getResources().getString(R.string.please_login));
        }
    }

    @Override
    public void onClick(View view) {
        boolean isLogin = UserInfo.isLogin();
        switch (view.getId()) {
            case R.id.iv_head://头像（未登录进入登录界面）
                if (!isLogin) {
                    LoginActivity.startActivity(this.getActivity(), LoginActivity.FINISH);
                }
                break;
            case R.id.iv_edit://编辑
//                if (isLogin) {
//                    getUserInfo();
//                } else {
//                    LoginActivity.startActivity(mActivity, LoginActivity.FINISH);
//                }
                break;
            case R.id.iv_setting://设置
                if (isLogin) {
                    SettingActivity.startActivity(this.getContext());
                } else {
                    LoginActivity.startActivity(this.getActivity(), LoginActivity.FINISH);
                }
                break;
            case R.id.ll_borrow_record://申请记录
                if (isLogin) {
                    ApplyRecordActivity.startActivity(this.getContext());
                } else {
                    LoginActivity.startActivity(this.getActivity(), LoginActivity.FINISH);
                }
                break;
//            case R.id.ll_credit_auth:
//                if (isLogin) {
//                    AuthActivity.startActivity(this.getContext());
//                } else {
//                    LoginActivity.startActivity(this.getActivity(), LoginActivity.FINISH);
//                }
//                break;
            case R.id.ll_my_find://我的发现
                if (isLogin) {
                    MyFindActivity.startActivity(this.getActivity());
                } else {
                    LoginActivity.startActivity(this.getActivity(), LoginActivity.FINISH);
                }
                break;
            case R.id.ll_help://帮助中心
                HelpCenterActivity.startActivity(this.getContext());
                break;
        }

    }


}
