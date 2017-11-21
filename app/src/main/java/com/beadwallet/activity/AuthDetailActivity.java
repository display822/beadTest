package com.beadwallet.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.base.BaseWebViewActivity;
import com.example.skn.framework.util.ToolBarUtil;
import com.beadwallet.R;


/**
 * 运营商认证、芝麻信用、我的公积金、我的社保、京东认证、淘宝认证、邮箱认证
 */
public class AuthDetailActivity extends BaseActivity {
    protected Toolbar toolbar;
    private ProgressBar pb;
    private WebView webView;
    private LinearLayout ll_container;
    private String url;
    private String title;
    private boolean isComplete;

    public static void startActivity(Context context, String url, String title, boolean isComplete) {
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("isComplete", isComplete);
        context.startActivity(intent);
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void init() {
        setContentView(R.layout.activity_auth_detail);
        toolbar = ToolBarUtil.getInstance(this).setTitle(title).build();
        webView = (WebView) findViewById(R.id.web);
        pb = (ProgressBar) findViewById(R.id.pb);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.requestFocus();
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
    }

    @Override
    protected void initVar() {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        isComplete = getIntent().getBooleanExtra("isComplete", false);
    }

    @Override
    protected void initData() {

    }


    class MyWebViewClient extends WebViewClient {
        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            pb.setProgress(newProgress);
            if (newProgress == 100)
                pb.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else
            super.onBackPressed();
    }
}
