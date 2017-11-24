package com.dreamwallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.dreamwallet.R;
import com.dreamwallet.activity.MainActivity;
import com.dreamwallet.adapter.LoansTitleAdapter;
import com.dreamwallet.entity.LoansTitleEntity;
import com.dreamwallet.util.Global;
import com.dreamwallet.util.UrlService;
import com.example.skn.framework.util.ToolBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DOY on 2017/9/8.
 */
public class BaseLoansFragment extends BaseFragment {


    private RecyclerView rlTitle;
    private ViewPager vpMain;
    private List<LoansTitleEntity> data = new ArrayList<>();
    private LoansTitleAdapter titleAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_loans, container, false);
        ((MainActivity) mActivity).toolbar.setVisibility(View.VISIBLE);
        ToolBarUtil.getInstance(mActivity)
                .setTitle("贷款")
                .isShow(false)
                .build();
        initView(view);
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((MainActivity) mActivity).toolbar.setVisibility(View.VISIBLE);
            ToolBarUtil.getInstance(mActivity)
                    .setTitle("贷款")
                    .isShow(false)
                    .build();
            if (titleAdapter != null && titleAdapter.getItemCount() > 0) {
                rlTitle.scrollToPosition(Global.loansPosition);
                titleAdapter.setCurrentPosition(Global.loansPosition);//刷新头部
                vpMain.setCurrentItem(Global.loansPosition);
            }else {
                getTitleTab();
            }
        }
    }

    public static BaseLoansFragment getInstance() {
        return new BaseLoansFragment();
    }

    private void initView(View view) {
        rlTitle = view.findViewById(R.id.rl_title);
        rlTitle.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayout.HORIZONTAL, false));
        vpMain = view.findViewById(R.id.vp_main);
        getTitleTab();
    }

    private void getTitleTab() {
        Api.getDefault(UrlService.class).getPlatformSortName().compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<LoansTitleEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<LoansTitleEntity> loansTitleEntities) {
                        initTitle(loansTitleEntities);
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {

                    }
                });
    }


    private void initTitle(List<LoansTitleEntity> loansTitleEntities) {
        data.clear();
        if (loansTitleEntities != null) {
            data.addAll(loansTitleEntities);
        }
        titleAdapter = new LoansTitleAdapter(mActivity, data, Global.loansPosition, position -> {
            vpMain.setCurrentItem(Global.loansPosition);
        });
        rlTitle.setAdapter(titleAdapter);
        initMain();
    }

    private void initMain() {
        if (data.size() == 0) return;
        List<Fragment> fragmentList = new ArrayList<>();
        //通过头部 创建main布局
        for (int i = 0; i < data.size(); i++) {
            LoansFragment itemFragment = LoansFragment.getInstance();
            itemFragment.sortInfoId = i + 1;
            fragmentList.add(itemFragment);
        }
        vpMain.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        vpMain.setCurrentItem(Global.loansPosition);
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                rlTitle.scrollToPosition(Global.loansPosition);
                Global.loansPosition = position;
                titleAdapter.setCurrentPosition(Global.loansPosition);//刷新头部
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
