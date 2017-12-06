package com.comtempwallet.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.comtempwallet.R;
import com.comtempwallet.adapter.BannerAdapter;
import com.comtempwallet.databinding.FragmentFloansBinding;
import com.comtempwallet.entity.BannerEntity;
import com.comtempwallet.http.UrlService;
import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4 0004.
 *
 */

public class LoansFragment extends BaseFragment {

    private FragmentFloansBinding binding;
    private boolean bannerComplete;
    private boolean productComplete;

    public static LoansFragment getInstance(){
        return new LoansFragment();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_floans, container, false);

        binding.refresh.setOnRefreshListener(r -> init());
        binding.refresh.autoRefresh();

        return binding.getRoot();
    }

    private void closeRefresh(int type) {
        if (binding.refresh.isRefreshing()) {
            switch (type) {
                case 1:
                    bannerComplete = true;
                    break;
                case 2:
                    productComplete = true;
                    break;
            }
            if (bannerComplete && productComplete) {
                binding.refresh.finishRefresh();
                bannerComplete = false;
                productComplete = false;
            }
        }
    }

    /**
     * 下拉刷新后执行的方法
     */
    private void init(){

        initBanner();

        initProduct();

        initClickListener();
    }

    private void initClickListener(){

        binding.setGoLoanClick(v -> {

        });

        binding.seekBar.setMax(100);
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                binding.borrowNum.setText(String.valueOf(progress/10*50+500));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initProduct(){

        closeRefresh(2);

    }

    private void initBanner(){
        Api.getDefault(UrlService.class).getBannar().compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<BannerEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<BannerEntity> bannerEntities) {

                        if(bannerEntities != null && bannerEntities.size() > 0){
                            binding.loanBanner.setData(bannerEntities, null);
                            binding.loanBanner.setAdapter(new BannerAdapter());
                        }

                        closeRefresh(1);
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {

                        closeRefresh(1);
                    }
                });
    }


}
