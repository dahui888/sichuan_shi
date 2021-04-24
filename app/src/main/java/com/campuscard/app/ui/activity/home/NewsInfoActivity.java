package com.campuscard.app.ui.activity.home;

import android.os.Build;
import android.text.TextUtils;
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
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.ItemEvent;
import com.campuscard.app.ui.entity.NewsEntity;
import com.campuscard.app.ui.entity.NoticeEntity;
import com.campuscard.app.ui.entity.NoticeInfoEntity;
import com.campuscard.app.utils.XImageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 新闻详情
 */
@ContentView(R.layout.activity_news_info)
public class NewsInfoActivity extends BaseActivity {

    @ViewInject(R.id.tv_title)
    protected TextView tvTitle;
    @ViewInject(R.id.tv_time)
    protected TextView tvTime;
    @ViewInject(R.id.tv_look)
    protected TextView tvLook;
    @ViewInject(R.id.mWebVew)
    protected WebView mWebVew;
    @ViewInject(R.id.iv_pic)
    private ImageView ivPic;
    private String newsId, type;//type:公告

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        newsId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
    }

    @Override
    public void getData() {

        if (TextUtils.isEmpty(type)) {
            HttpRequestParams params = new HttpRequestParams(Constant.NEWS_INFO + newsId);
            HttpUtils.get(this, params, new HttpResponseCallBack() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResultData<NewsEntity>>() {
                    }.getType();
                    ResultData<NewsEntity> resultData = gson.fromJson(result, type);
                    if (resultData.getStatus() == 200) {
                        if (resultData.getData() != null) {
                            tvTitle.setText(resultData.getData().getTitle());
                            tvTime.setText(resultData.getData().getOnlineTime());
                            tvLook.setText(resultData.getData().getViewCount() + "人阅读");
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
            //公告
            HttpRequestParams params = new HttpRequestParams(Constant.NOTICE_INFO + newsId);
            HttpUtils.get(this, params, new HttpResponseCallBack() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResultData<NoticeInfoEntity>>() {
                    }.getType();
                    ResultData<NoticeInfoEntity> resultData = gson.fromJson(result, type);
                    if (resultData.getStatus() == 200) {
                        if (resultData.getData() != null) {
                            tvTitle.setText(resultData.getData().getTitle());
                            tvTime.setText(resultData.getData().getOnlineTime().replace('T', ' ').replace('Z', ' '));
                            tvLook.setText(resultData.getData().getViewCount() + "人阅读");
                            setWeb(resultData.getData().getContent());
                            XImageUtils.loadFitImage(NewsInfoActivity.this, resultData.getData().getImgUrl(), ivPic);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new ItemEvent(ItemEvent.ACTIVITY.ACTIVITY_NEWS));
    }
}
