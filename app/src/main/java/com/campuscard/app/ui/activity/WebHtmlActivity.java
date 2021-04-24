package com.campuscard.app.ui.activity;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 通用Web加载字符串
 */
@ContentView(R.layout.activity_web)
public class WebHtmlActivity extends BaseActivity {

    public static final String TITLE = "TITLE";
    public static final String INFO = "INFO";
    @ViewInject(R.id.mWebView)
    private WebView mWebView;

    @Override
    public void initView() {
        setTitle(getIntent().getStringExtra(TITLE));
        toolbar.setNavigationIcon(null);
    }

    @Override
    public void getData() {
        setWeb(getIntent().getStringExtra(INFO));
    }

    private void setWeb(final String content) {
        WebSettings webseting = mWebView.getSettings();
        webseting.setJavaScriptEnabled(true);
        webseting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webseting.setLoadWithOverviewMode(true);
        webseting.setDefaultTextEncodingName("UTF-8");
        webseting.setLoadsImagesAutomatically(true);    //设置自动加载图片
        webseting.setLoadWithOverviewMode(true);
        webseting.setDefaultTextEncodingName("UTF-8");
        webseting.setDefaultFontSize(20);
        webseting.setTextSize(WebSettings.TextSize.LARGEST);
        webseting.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webseting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        if (!TextUtils.isEmpty(content)) {
            mWebView.loadDataWithBaseURL(null, "<!DOCTYPE html><html><style>img{width:100%;}body{line-height:1.5;}</style><body>" + content + "</body></html>", "text/html", "utf-8",
                    null);
        } else {
            mWebView.loadUrl(Constant.ZHUCE_XIEYI);
        }
    }
}
