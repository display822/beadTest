package com.dreamwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dreamwallet.R;
import com.example.skn.framework.base.BaseActivity;
import com.dreamwallet.adapter.AuthAdapter;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends BaseActivity {

    private ImageView iv_back;
    private RecyclerView listView;
    private List<String> nameDate;
    private List<Integer> imgResDate;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AuthActivity.class));
    }


    @Override
    protected void initVar() {
        setFlagTranslucentStatus();
        setContentView(R.layout.activity_auth);

        nameDate = new ArrayList<>();
        nameDate.add("实名认证");
        nameDate.add("个人信息");
        nameDate.add("运营商认证");
        nameDate.add("芝麻信用");
        nameDate.add("我的公积金");
        nameDate.add("我的社保");
        nameDate.add("京东认证");
        nameDate.add("淘宝认证");
        nameDate.add("邮箱认证");
        imgResDate = new ArrayList<>();
        imgResDate.add(R.drawable.ic_true_name);
        imgResDate.add(R.drawable.ic_person_info);
        imgResDate.add(R.drawable.ic_operator);
        imgResDate.add(R.drawable.ic_zhima);
        imgResDate.add(R.drawable.ic_my_accumulation);
        imgResDate.add(R.drawable.ic_social_security);
        imgResDate.add(R.drawable.ic_jd);
        imgResDate.add(R.drawable.ic_taobao);
        imgResDate.add(R.drawable.ic_email);
    }

    @Override
    protected void init() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        listView = (RecyclerView) findViewById(R.id.listView);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 3);
        listView.setLayoutManager(layoutManager);
        AuthAdapter authAdapter = new AuthAdapter(nameDate, imgResDate, this);
        authAdapter.setOnItemClickListener(new AuthAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onClickMethod(position);
            }
        });
        listView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                //不是第一个的格子都设一个左边和底部的间距
                outRect.left = 10;
                outRect.bottom = 10;
                //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
                if (parent.getChildLayoutPosition(view) % 3 == 0) {
                    outRect.left = 0;
                }
                super.getItemOffsets(outRect, view, parent, state);
            }
        });
        listView.setAdapter(authAdapter);

    }

    private void onClickMethod(int position) {
        switch (position) {
            case 0://实名认证
//                TrueNameInfoActivity.startActivity(mActivity);
                break;
            case 1://个人信息

                break;
            case 2://运营商认证

                break;
            case 3://芝麻信用

                break;
            case 4://我的公积金

                break;
            case 5://我的社保

                break;
            case 6://京东认证

                break;
            case 7://淘宝认证

                break;
            case 8://邮箱认证

                break;
        }
    }

    @Override
    protected void initData() {

    }

}
