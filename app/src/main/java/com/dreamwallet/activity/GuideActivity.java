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

import com.dreamwallet.R;
import com.dreamwallet.widget.OnNoDoubleClickListener;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.util.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 * 新手指南主界面
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vpGuide;
    private List<View> pagers = new ArrayList<>();
    private ImageView dot;
    private ImageView tv_entry;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, GuideActivity.class));
    }


    @Override
    public void initVar() {
        int[] resImg = new int[]{R.drawable.ic_guide1, R.drawable.ic_guide2, R.drawable.ic_guide3};
        int[] resTv = new int[]{R.drawable.tv_guid_1, R.drawable.tv_guid_2, R.drawable.tv_guid_3};
        for (int i = 0; i < resImg.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.widget_guide_page, null);
            ImageView imageView = ((ImageView) view.findViewById(R.id.img_page));
            ImageView tvView = ((ImageView) view.findViewById(R.id.tv_page));
            imageView.setImageResource(resImg[i]);
            tvView.setImageResource(resTv[i]);
            pagers.add(view);
        }
    }

    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_guide);
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        dot = (ImageView) findViewById(R.id.tips_dot);
        tv_entry = (ImageView) findViewById(R.id.tv_entry);
        tv_entry.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                MainActivity.startActivity(mActivity, 0);
                finish();
            }
        });


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
                    dot.setImageResource(R.drawable.ic_dot_one);
                } else if (position == 1) {
                    dot.setImageResource(R.drawable.ic_dot_two);
                } else if (position == 2) {
                    dot.setImageResource(R.drawable.ic_dot_three);
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
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }
}
