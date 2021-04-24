package com.campuscard.app.ui.activity.my;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.NewsEntity;
import com.campuscard.app.ui.entity.QuestionEntity;
import com.campuscard.app.ui.entity.ZhiNanEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/*
 * 操作指南--帮助反馈详情
 * */
@ContentView(R.layout.activity_operation_guide)
public class OperationGuideActivity extends BaseActivity {

    @ViewInject(R.id.mWebVew)
    protected WebView mWebVew;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    private String id, url, title;

    @Override
    public void initView() {
        id = getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
            tvTitle.setVisibility(View.VISIBLE);
        }
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
        if (TextUtils.isEmpty(title)) {
            HttpRequestParams params = new HttpRequestParams(Constant.USE_ZHINAN);
            HttpUtils.get(this, params, new HttpResponseCallBack() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResultData<ZhiNanEntity>>() {
                    }.getType();
                    ResultData<ZhiNanEntity> resultData = gson.fromJson(result, type);
                    if (resultData.getStatus() == 200) {
                        if (resultData.getData() != null) {
                            setWeb(resultData.getData().getContent());
                        }
                    }
                }

                @Override
                public void onFailed(int code, String failedMsg) {
                }

                @Override
                public void onFinished() {
                }
            });
        } else {
            //问题详情
            HttpRequestParams params = new HttpRequestParams(url + id);
            HttpUtils.get(this, params, new HttpResponseCallBack() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResultData<QuestionEntity>>() {
                    }.getType();
                    ResultData<QuestionEntity> resultData = gson.fromJson(result, type);
                    if (resultData.getStatus() == 200) {
                        if (resultData.getData() != null) {
                            tvTitle.setText(resultData.getData().getProblem());
                            setWeb(resultData.getData().getSolution());
                        }
                    }
                }
                @Override
                public void onFailed(int code, String failedMsg) {
                }
                @Override
                public void onFinished() {
                }
            });
        }
    }

    /**
     * 设置数据
     *
     * @param content
     */
    private void setWeb(final String content) {
        WebSettings webseting = mWebVew.getSettings();
        webseting.setJavaScriptEnabled(true);
        webseting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webseting.setDefaultTextEncodingName("UTF-8");
        webseting.setLoadsImagesAutomatically(true);    //设置自动加载图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webseting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebVew.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebVew.loadDataWithBaseURL(Constant.BASE_HOST, "<!DOCTYPE html><html><style>img{width:100%;}body{line-height:1.5;}</style><body>" + content + "</body></html>", "text/html", "utf-8",
                null);
    }
}