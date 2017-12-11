package com.dreamwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.dreamwallet.R;
import com.dreamwallet.databinding.ActivityLoansDetailsBinding;
import com.dreamwallet.entity.LoansDetailsEntity;
import com.dreamwallet.util.StatisticsUtil;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.util.UserInfo;
import com.dreamwallet.widget.ObservableScrollView;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.BaseEntity;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.AppUtil;
import com.example.skn.framework.util.ToolBarUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by hf
 */
public class LoansDetailsActivity extends BaseActivity implements ObservableScrollView.ScrollViewListener {

    private int imageHeight;
    private Toolbar toolbar;

    public static void startActivity(Context context, String platformId) {
        Intent intent = new Intent(context, LoansDetailsActivity.class);
        intent.putExtra("platformId", platformId);
        context.startActivity(intent);
    }

    private String platformId;
    private ActivityLoansDetailsBinding binding;

    @Override
    protected void initVar() {
        platformId = getIntent().getStringExtra("platformId");
    }

    @Override
    protected void init() {
        setFlagTranslucentStatus();
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_loans_details);
        toolbar = ToolBarUtil.getInstance(mActivity).setTitle("追梦宝").build();
        toolbar.setBackgroundColor(Color.argb((int) 0, 228, 208, 55));
        ViewTreeObserver observer = binding.llTop.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.llTop.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                imageHeight = binding.llTop.getHeight() - 10;
                binding.scrollView.setScrollViewListener(LoansDetailsActivity.this);
            }
        });
    }

    @Override
    protected void initData() {
        Observable<BaseEntity<LoansDetailsEntity>> platformDetail;
        if (TextUtils.isEmpty(platformId)) {
            platformDetail = Api.getDefault(UrlService.class).getPlatformDetail("1");
        } else {
            platformDetail = Api.getDefault(UrlService.class).getPlatformDetail("2", platformId);
        }
        platformDetail.compose(Api.handlerResult())
                .subscribe(new RequestCallBack<LoansDetailsEntity>(mActivity, true) {
                    @Override
                    public void onSuccess(LoansDetailsEntity loansDetailsEntity) {
                        if (loansDetailsEntity != null) {
                            binding.setData(loansDetailsEntity);
                            ToolBarUtil.getInstance(mActivity).setTitle(loansDetailsEntity.getAbbreviation()).build();
                            binding.setApplyCLick(view -> {
                                if (UserInfo.isLogin()) {
                                    addApplyReord(platformId);
                                    StatisticsUtil.clickCount(mActivity, platformId);
//                                     BaseWebViewActivity.show(mActivity, loansDetailsEntity.getLink(), loansDetailsEntity.getAbbreviation(), true);
                                    AppUtil.startWeb(mActivity, loansDetailsEntity.getLink());
                                } else {
                                    LoginActivity.startActivity(mActivity, LoginActivity.FINISH);
                                }
                            });
                        }

                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                    }
                });
    }

    private void addApplyReord(String productId) {
        Api.getDefault(UrlService.class).addApply(UserInfo.loginToken, productId).compose(Api.handlerResult())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("beadwallet", "addApply------>error");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e("beadwallet", "addApply------>" + s);
                    }
                });
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            toolbar.setBackgroundColor(Color.argb((int) 0, 228, 208, 55));  //AGB由相关工具获得，或者美工提供
        } else if (y > 0 && y <= imageHeight) {
            float scale = (float) y / imageHeight;
            // 只是layout背景透明(仿知乎滑动效果)
            toolbar.setBackgroundColor(Color.argb((int) (255 * scale), 228, 208, 55));
        } else {
            toolbar.setBackgroundColor(Color.argb((int) 255, 228, 208, 55));
        }
    }


}
