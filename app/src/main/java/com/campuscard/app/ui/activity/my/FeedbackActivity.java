package com.campuscard.app.ui.activity.my;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.base.frame.utils.XToastUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.http.ResultPageData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/*
 * 反馈意见
 * */
@ContentView(R.layout.activity_feedback)
public class FeedbackActivity extends BaseActivity {


    @ViewInject(R.id.btn_right)
    protected TextView btnRight;
    @ViewInject(R.id.tv_content)
    protected EditText tvContent;
    @ViewInject(R.id.et_lxfs)
    protected EditText etLxfs;

    @Override
    public void initView() {
        btnRight.setText("提交");
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
    }

    @Event(value = {R.id.btn_right}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                //去反馈
                if (TextUtils.isEmpty(tvContent.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_advice));
                    return;
                }
                if (TextUtils.isEmpty(etLxfs.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_lxfs));
                    return;
                }
                if (etLxfs.getText().toString().length() > 501) {
                    XToastUtil.showToast(this, getString(R.string.input_advice_len));
                    return;
                }
                ParamsMap params = new ParamsMap();
                params.put("contactInfo", etLxfs.getText().toString());
                params.put("content", tvContent.getText().toString());
                HttpUtils.post(this, Constant.ADVICE, params, new HttpResponseCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ResultData>() {
                        }.getType();
                        ResultData resultData = gson.fromJson(result, type);
                        if (resultData.getStatus() == 200) {
                            XToastUtil.showToast(FeedbackActivity.this, "反馈成功");
                            finish();
                        }
                    }
                    @Override
                    public void onFailed(int code, String failedMsg) {
                    }
                    @Override
                    public void onFinished() {
                    }
                });
                break;
        }
    }
}