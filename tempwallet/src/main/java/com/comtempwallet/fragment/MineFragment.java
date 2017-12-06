package com.comtempwallet.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comtempwallet.R;
import com.comtempwallet.databinding.FragmentFmineBinding;
import com.example.skn.framework.base.BaseFragment;

/**
 * Created by Administrator on 2017/12/5 0005.
 *
 */

public class MineFragment extends BaseFragment {

    FragmentFmineBinding binding;

    public static MineFragment getInstance(){
        return new MineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fmine, container, false);

        //请求个人信息

        initClickListener();
        return binding.getRoot();
    }


    private void initClickListener(){

        binding.setCardClick(v -> {});
        binding.setHelpClick(v -> {});
        binding.setClearClick(v -> {});
    }
}
