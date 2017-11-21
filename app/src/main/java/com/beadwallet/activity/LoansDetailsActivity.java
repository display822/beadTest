package com.beadwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.base.BaseWebViewActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.BaseEntity;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.AppUtil;
import com.example.skn.framework.util.ToolBarUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.beadwallet.R;
import com.beadwallet.databinding.ActivityLoansDetailsBinding;
import com.beadwallet.entity.BannerEntity;
import com.beadwallet.entity.LoansDetailsEntity;
import com.beadwallet.util.ShareListener;
import com.beadwallet.util.ShareUtil;
import com.beadwallet.util.StatisticsUtil;
import com.beadwallet.util.UrlService;
import com.beadwallet.util.UserInfo;
import com.beadwallet.widget.ObservableScrollView;

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
        toolbar = ToolBarUtil.getInstance(mActivity).setTitle("水珠钱包").build();
        toolbar.setBackgroundColor(Color.argb((int) 0, 104, 230, 219));
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
                                    // BaseWebViewActivity.show(mActivity, loansDetailsEntity.getLink(), "水珠钱包");
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
            toolbar.setBackgroundColor(Color.argb((int) 0, 104, 230, 219));  //AGB由相关工具获得，或者美工提供
        } else if (y > 0 && y <= imageHeight) {
            float scale = (float) y / imageHeight;
            // 只是layout背景透明(仿知乎滑动效果)
            toolbar.setBackgroundColor(Color.argb((int) (255 * scale), 104, 230, 219));
        } else {
            toolbar.setBackgroundColor(Color.argb((int) 255, 104, 230, 219));
        }
    }


}
