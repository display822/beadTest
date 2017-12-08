package com.example.skn.framework.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.skn.framework.R;

/**
 * Created by hf
 */
public class BaseWebViewActivity extends BaseActivity {

    /**
     * 默认浏览器打开
     *
     * @param context
     * @param url
     * @param title
     */
    public static void show(Context context, String url, String title) {
        show(context, url, title, false);
    }

    /**
     * @param context
     * @param url
     * @param title
     * @param isInnerOpen 是否使用浏览器打开
     */
    public static void show(Context context, String url, String title, boolean isInnerOpen) {
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("isInnerOpen", isInnerOpen);
        context.startActivity(intent);
    }

    protected WebView webView;
    protected String url;
    protected String title;
    private ProgressBar pb;
    private boolean isInnerOpen;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void init() {
        setFlagTranslucentStatus();
        setContentView(R.layout.activity_web_view);

        findViewById(R.id.title_back).setOnClickListener(v -> finish());
        webView = (WebView) findViewById(R.id.web);
        pb = (ProgressBar) findViewById(R.id.pb);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //WebView加载页面优先使用缓存加载
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
    }

    @Override
    protected void initVar() {
        isInnerOpen = getIntent().getBooleanExtra("isInnerOpen", true);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
    }

    @Override
    protected void initData() {

    }


    class MyWebViewClient extends WebViewClient {
        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (isInnerOpen) {
                view.loadUrl(url);
            }
            return isInnerOpen;
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
