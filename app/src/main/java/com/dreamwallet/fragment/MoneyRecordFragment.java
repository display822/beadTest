package com.dreamwallet.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamwallet.R;
import com.dreamwallet.databinding.FragmentMoneyRecordBinding;
import com.example.skn.framework.base.BaseFragment;

/**
 * Created by Administrator on 2017/12/7 0007.
 */

public class MoneyRecordFragment extends BaseFragment {

    FragmentMoneyRecordBinding binding;

    public static MoneyRecordFragment getInstance(){
        return new MoneyRecordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_money_record, container, false);


        return binding.getRoot();
    }
}
