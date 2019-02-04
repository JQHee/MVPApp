package com.example.testmvpapp.sections.common.activities;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.testmvpapp.R;
import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.SimpleActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author ChayChan
 * @description: 加载网页的activity
 * @date 2017/7/4  22:01
 */

public class WebViewActivity extends SimpleActivity {

    public static final String URL = "url";

    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;

    @BindView(R.id.wv_content)
    WebView mWvContent;

    private void initData() {
        String url = getIntent().getStringExtra(URL);
        mWvContent.loadUrl(url);
    }

    private void initListener() {
        WebSettings settings = mWvContent.getSettings();
        settings.setJavaScriptEnabled(true);


        mWvContent.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mPbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mPbLoading.setVisibility(View.GONE);
            }
        });

        mWvContent.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mPbLoading.setProgress(newProgress);
            }
        });

        mWvContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWvContent.canGoBack()) {  //表示按返回键
                        mWvContent.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected Object getLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initEventAndData() {
        initData();
        initListener();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
