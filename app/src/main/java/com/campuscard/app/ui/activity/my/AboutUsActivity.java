package com.campuscard.app.ui.activity.my;

import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.ui.DataFactory;
import com.campuscard.app.ui.entity.VersionEntity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 关于我们
 */
@ContentView(R.layout.activity_about_us)
public class AboutUsActivity extends BaseActivity {

    @ViewInject(R.id.iv_tubiao)
    protected ImageView ivTubiao;
    @ViewInject(R.id.tv_name)
    protected TextView tvName;
    @ViewInject(R.id.tv_version)
    protected TextView tvVersion;
    @ViewInject(R.id.tv_jianjie)
    protected WebView mWebView;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
    }

    @Override
    public void getData() {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.ABOUT);
        HttpUtils.get(this, httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.optInt("status") == 200) {
                        setWeb(jsonObject.optJSONObject("data").optString("content"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code, String failedMsg) {
            }

            @Override
            public void onFinished() {
            }
        });
        DataFactory.versionUdate(this, new DataFactory.OnResultEntityCallback() {
            @Override
            public void onSuccess(VersionEntity versionEntity) {
                tvVersion.setText("V" + versionEntity.getVersionCode());
            }
        });
    }

    /**
     * 设置数据
     *
     * @param content
     */
    private void setWeb(final String content) {
        WebSettings webseting = mWebView.getSettings();
        webseting.setJavaScriptEnabled(true);
        webseting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webseting.setDefaultTextEncodingName("UTF-8");
        webseting.setLoadsImagesAutomatically(true);    //设置自动加载图片
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
        mWebView.loadDataWithBaseURL(Constant.BASE_HOST, "<!DOCTYPE html><html><style>img{width:100%;}body{line-height:1.5;}</style><body>" + content + "</body></html>", "text/html", "utf-8",
                null);
    }
}
