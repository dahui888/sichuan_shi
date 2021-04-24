package com.campuscard.app.ui.activity.my;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.base.frame.utils.XToastUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/*
 *
 * 设置密码
 * */
@ContentView(R.layout.activity_set_password)
public class SetPasswordActivity extends BaseActivity {

    @ViewInject(R.id.et_pw)
    protected EditText etPw;
    @ViewInject(R.id.et_surepw)
    protected EditText etSurepw;
    @ViewInject(R.id.bt_sure)
    protected Button btSure;
    private String tell, code, smsToken;

    @Override
    public void initView() {
        tell = getIntent().getStringExtra("tell");
        code = getIntent().getStringExtra("code");
        smsToken = getIntent().getStringExtra("smsToken");
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
    }

    @Event(value = {R.id.bt_sure}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure:
                //提交
                if (TextUtils.isEmpty(etPw.getText().toString()) || TextUtils.isEmpty(etSurepw.getText().toString())) {
                    XToastUtil.showToast(SetPasswordActivity.this, getString(R.string.input_pwd));
                    return;
                }
                ParamsMap paramsLogin = new ParamsMap();
                paramsLogin.put("loginPhone", tell);
                paramsLogin.put("smsCode", code);
                paramsLogin.put("loginPassword", etPw.getText().toString());
                paramsLogin.put("smsToken", smsToken);
                HttpUtils.post(this, Constant.FIND_PWD, paramsLogin, new HttpResponseCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ResultData>() {
                        }.getType();
                        ResultData resultData = gson.fromJson(result, type);
                        if (resultData.getStatus() == 200) {
                            XToastUtil.showToast(SetPasswordActivity.this, "修改成功");
                            finish();
                        } else {
                            XToastUtil.showToast(SetPasswordActivity.this, resultData.getDesc());
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
