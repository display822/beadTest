package com.dreamwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.util.SpUtil;
import com.dreamwallet.R;
import com.dreamwallet.widget.OnNoDoubleClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 * 新手指南主界面
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vpGuide;
    private List<View> pagers = new ArrayList<>();
    private List<View> pagers_img = new ArrayList<>();
    private View dot1;
    private View dot2;
    private View dot3;
    private TextView tv_entry;
    private TextView tv_tip;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, GuideActivity.class));
    }


    @Override
    public void initVar() {
        int[] resID = new int[]{R.drawable.ic_guide1, R.drawable.ic_guide2, R.drawable.ic_guide3};
        for (int i = 0; i < resID.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.widget_guide_page, null);
            ImageView imageView = ((ImageView) view.findViewById(R.id.img_page));
            imageView.setImageResource(resID[i]);
            pagers.add(view);
            pagers_img.add(imageView);
        }
    }

    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_guide);
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_entry = (TextView) findViewById(R.id.tv_entry);
        tv_entry.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                MainActivity.startActivity(mActivity, 0);
                finish();
            }
        });

        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);

        dot1.setSelected(true);
        tv_entry.setVisibility(View.GONE);

        bindAdapter();
        SpUtil.setData("firstUse", true);
    }

    @Override
    protected void initData() {

    }


    private void bindAdapter() {
        vpGuide.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pagers.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(pagers.get(position));
                return pagers.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(pagers.get(position));
            }
        });
        vpGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_entry.setVisibility(View.GONE);
                if (position == 0) {
                    tv_tip.setText("海量产品  精准定位");
                    dot1.setSelected(true);
                    dot2.setSelected(false);
                    dot3.setSelected(false);
                } else if (position == 1) {
                    tv_tip.setText("数据安全  滴水不漏");
                    dot1.setSelected(false);
                    dot2.setSelected(true);
                    dot3.setSelected(false);
                } else if (position == 2) {
                    tv_tip.setText("海量产品  精准定位");
                    dot1.setSelected(false);
                    dot2.setSelected(false);
                    dot3.setSelected(true);
                    tv_entry.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        pagers = null;
        pagers_img = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }
}
