package com.beadwallet.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.skn.framework.base.BaseFragment;
import com.beadwallet.R;
import com.beadwallet.activity.MainActivity;
import com.beadwallet.databinding.FragmentFindBinding;
import com.beadwallet.util.Global;

import org.greenrobot.eventbus.EventBus;

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
        ((MainActivity) mActivity).toolbar.setVisibility(View.GONE);
        return binding.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((MainActivity) mActivity).toolbar.setVisibility(View.GONE);
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
        binding.tvInformation.setTextColor(flag ? getResources().getColor(R.color.toolbar) : getResources().getColor(R.color.color_FFFFFF));
        binding.tvInformation.setBackgroundColor(flag ? getResources().getColor(R.color.color_FFFFFF) : getResources().getColor(R.color.toolbar));
        binding.tvForum.setBackgroundColor(flag ? getResources().getColor(R.color.toolbar) : getResources().getColor(R.color.color_FFFFFF));
        binding.tvForum.setTextColor(flag ? getResources().getColor(R.color.color_FFFFFF) : getResources().getColor(R.color.toolbar));
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
