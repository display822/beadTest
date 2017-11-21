package com.beadwallet.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.dialog.DialogUtil;
import com.example.skn.framework.util.ToolBarUtil;
import com.beadwallet.R;
import com.beadwallet.util.DialogHelper;

/**
 * Created by hf
 */
public class ForumWebViewActivity extends BaseActivity {


    public static void show(Context context, String url, String title) {
        Intent intent = new Intent(context, ForumWebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    private WebView webView;
    private String url;
    private String title;
    protected Toolbar toolbar;
    private TextView tvSend;
    private RecyclerView rlComment;
    private TextView tvShowAllComment;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void init() {
        setContentView(R.layout.activity_forum_web_view);
        toolbar = ToolBarUtil.getInstance(this).setTitle(title).build();
        webView = (WebView) findViewById(R.id.web);
        tvSend = (TextView) findViewById(R.id.tv_send);
        rlComment = (RecyclerView) findViewById(R.id.rl_comment);
        tvShowAllComment= (TextView) findViewById(R.id.tv_show_all_comment);
        rlComment.setLayoutManager(new LinearLayoutManager(mActivity));
       // rlComment.setAdapter(new CommentAdapter(mActivity));
        tvSend.setOnClickListener(view -> sendMessage());
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //WebView加载页面优先使用缓存加载
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
//        DialogUtil.showLoading(mActivity);
    }

    @Override
    protected void initVar() {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
    }

    @Override
    protected void initData() {

    }

    private void sendMessage() {
        new DialogHelper.Builder(mActivity).setFlag(true).build().showCommentSuccess();
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
            if (newProgress == 100)
                DialogUtil.cancel();
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
