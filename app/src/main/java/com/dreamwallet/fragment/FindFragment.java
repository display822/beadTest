package com.dreamwallet.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamwallet.R;
import com.dreamwallet.databinding.FragmentFindBinding;
import com.dreamwallet.util.Global;
import com.example.skn.framework.base.BaseFragment;

/**
 * Created by hf
 * 发现
 */
public class FindFragment extends BaseFragment {

    private FragmentFindBinding binding;
    private ForumFragment forumFragment;
    private InformationFragment informationFragment;
    private Fragment currentFragment;

    public static FindFragment getInstance() {
        return new FindFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find, container, false);
        init();

        return binding.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            changeTab(Global.findPosition == 0);
        }

    }

    private void init() {
        forumFragment = ForumFragment.getInstance();
        informationFragment = InformationFragment.getInstance();
        currentFragment = informationFragment;//默认选择是0 当前fragment是informationFragment
        binding.tvInformation.setOnClickListener(view -> changeTab(true));
        binding.tvForum.setOnClickListener(view -> changeTab(false));
        changeTab(Global.findPosition == 0);
    }

    /**
     * true =0 资讯
     */
    private void changeTab(boolean flag) {
        binding.tvInformation.setTextColor(flag ? getResources().getColor(R.color.main_color) : getResources().getColor(R.color.color_FFFFFF));
        binding.tvInformation.setBackground(flag ? getResources().getDrawable(R.drawable.shape_corner5_trans_left) : getResources().getDrawable(R.drawable.shape_corner5_left_no));

        binding.tvForum.setBackground(flag ? getResources().getDrawable(R.drawable.shape_corner5_right_no) : getResources().getDrawable(R.drawable.shape_corner5_trans_right));
        binding.tvForum.setTextColor(flag ? getResources().getColor(R.color.color_FFFFFF) : getResources().getColor(R.color.main_color));
        if (flag) {
            if (!informationFragment.isAdded())
                getFragmentManager().beginTransaction().add(R.id.fl_main, informationFragment).commit();
            getFragmentManager().beginTransaction().hide(currentFragment)
                    .show(informationFragment)
                    .commit();
            currentFragment = informationFragment;
            Global.findPosition = 0;

        } else {
            if (!forumFragment.isAdded())
                getFragmentManager().beginTransaction().add(R.id.fl_main, forumFragment).commit();
            getFragmentManager().beginTransaction().hide(currentFragment)
                    .show(forumFragment)
                    .commit();
            currentFragment = forumFragment;
            Global.findPosition = 1;

        }


    }
}
