package com.campuscard.app.ui.activity.my;

import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.utils.XToastUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.ItemEvent;
import com.campuscard.app.ui.entity.MsgEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 消息详情
 */
@ContentView(R.layout.activity_msg_info)
public class MessageInfoActivity extends BaseActivity {

    @ViewInject(R.id.tv_title)
    protected TextView tvTitle;
    @ViewInject(R.id.iv_type)
    protected ImageView ivType;
    @ViewInject(R.id.tv_type)
    protected TextView tvType;
    @ViewInject(R.id.tv_time)
    protected TextView tvTime;
    @ViewInject(R.id.mWebVew)
    protected WebView mWebVew;
    private String msgId;

    @Override
    public void initView() {
        msgId = getIntent().getStringExtra("id");
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
        HttpRequestParams paramsMap = new HttpRequestParams(Constant.GET_MSG_DETAILS + msgId);
        HttpUtils.get(this, paramsMap, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<MsgEntity>>() {
                }.getType();
                ResultData<MsgEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null) {
                        tvTitle.setText(resultData.getData().getTitle());
                        tvTime.setText(resultData.getData().getSendDate());
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
        //标记已读
        HttpRequestParams params = new HttpRequestParams(Constant.MSG_IS_READE + msgId);
        HttpUtils.get(this, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData>() {
                }.getType();
                ResultData resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    EventBus.getDefault().post(new ItemEvent(ItemEvent.ACTIVITY.ACTIVITY_MSG_NUM));
                } else {
                    XToastUtil.showToast(MessageInfoActivity.this, resultData.getDesc());
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
