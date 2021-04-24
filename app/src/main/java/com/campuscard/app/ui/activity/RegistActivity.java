package com.campuscard.app.ui.activity;

import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XAppUtil;
import com.base.frame.utils.XToastUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.DuanXinEntity;
import com.campuscard.app.ui.entity.TxyamEntity;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.utils.TimeCountUtil;
import com.campuscard.app.utils.YzmCallBack;
import com.campuscard.app.view.ClearEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;

import java.lang.reflect.Type;

/**
 * 注册
 */
@ContentView(R.layout.activity_register)
public class RegistActivity extends BaseActivity implements View.OnClickListener {

    protected TextView btnRight;
    protected ClearEditText etMobile;
    protected ClearEditText etCode;
    protected Button btCode;
    protected ClearEditText etPwd;
    protected Button btSure;
    protected TextView mCheckBox;
    protected TextView btAdvice;
    protected AppBarLayout appBar;
    protected View viewOne, viewTwo, viewSan;
    private TimeCountUtil timeCountUtil;
    private String smsToken;

    @Override
    public void initView() {
        btnRight = (TextView) findViewById(R.id.btn_right);
        etMobile = (ClearEditText) findViewById(R.id.et_mobile);
        etCode = (ClearEditText) findViewById(R.id.et_code);
        btCode = (Button) findViewById(R.id.bt_code);
        etPwd = (ClearEditText) findViewById(R.id.et_pwd);
        btSure = (Button) findViewById(R.id.bt_sure);
        mCheckBox = (TextView) findViewById(R.id.mCheckBox);
        btAdvice = (TextView) findViewById(R.id.bt_advice);
        timeCountUtil = new TimeCountUtil(this, 60 * 1000, 1000, btCode);
        btCode.setOnClickListener(this);
        btSure.setOnClickListener(this);
//        btnRight.setText("直接登录");
        btnRight.setTextColor(getResources().getColor(R.color.color_666666));
        btnRight.setOnClickListener(this);
        btAdvice.setOnClickListener(this);
        viewOne = (View) findViewById(R.id.view_one);
        viewTwo = (View) findViewById(R.id.view_two);
        viewSan = (View) findViewById(R.id.view_san);
        toolbar.setNavigationIcon(null);
    }

    @Override
    public void getData() {
        setEditAttribute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_code:
                //验证码
                if (TextUtils.isEmpty(etMobile.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_mobile));
                    return;
                }
                if (etMobile.getText().toString().length() != 11 || !etMobile.getText().toString().startsWith("1")) {
                    XToastUtil.showToast(this, getString(R.string.input_mobile_true));
                    return;
                }
                getTxyam();
                break;
            case R.id.bt_sure:
                XAppUtil.closeSoftInput(this);
                //注册
                regist(smsToken);
                break;
            case R.id.btn_right:
                //登录
                finish();
                break;
            case R.id.bt_advice:
                //注册协议
                Bundle bundle = new Bundle();
                bundle.putString(WebHtmlActivity.TITLE, "用户使用协议");
                IntentUtil.redirectToNextActivity(this, WebHtmlActivity.class, bundle);
                break;
        }
    }

    //获取图形验证码
    public void getTxyam() {
        HttpRequestParams params = new HttpRequestParams(Constant.GET_TXYZM);
        HttpUtils.get(this, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<TxyamEntity>>() {
                }.getType();
                final ResultData<TxyamEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    DialogUtils.showImageValidateDialog(RegistActivity.this, resultData.getData().getImgBase64(), new YzmCallBack() {
                        @Override
                        public void callback(String yzm) {
                            getDuanXin(yzm, resultData.getData().getUuid());
                        }
                    });
                } else {
                    XToastUtil.showToast(RegistActivity.this, resultData.getDesc());
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


    //获取短信
    public void getDuanXin(String code, String uuid) {
        ParamsMap params = new ParamsMap();
        params.put("phone", etMobile.getText().toString());
        params.put("code", code);
        params.put("msgCodeType", "REGISTRY_CODE");
        params.put("uuid", uuid);
        HttpUtils.post(this, Constant.GET_YZM, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<DuanXinEntity>>() {
                }.getType();
                ResultData<DuanXinEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    timeCountUtil.start();
                    smsToken = resultData.getData().getMsgToken();
                }
                XToastUtil.showToast(RegistActivity.this, resultData.getDesc());
            }

            @Override
            public void onFailed(int code, String failedMsg) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    //注册
    public void regist(String smsToken) {
        if (TextUtils.isEmpty(etMobile.getText().toString())) {
            XToastUtil.showToast(RegistActivity.this, getString(R.string.input_mobile));
            return;
        }
        if (etMobile.getText().toString().length() != 11 || !etMobile.getText().toString().startsWith("1")) {
            XToastUtil.showToast(this, getString(R.string.input_mobile_true));
            return;
        }
        if (TextUtils.isEmpty(etCode.getText().toString()) || TextUtils.isEmpty(smsToken)) {
            XToastUtil.showToast(RegistActivity.this, getString(R.string.input_code));
            return;
        }
        if (TextUtils.isEmpty(etPwd.getText().toString())) {
            XToastUtil.showToast(RegistActivity.this, getString(R.string.input_pwd));
            return;
        }
        if (etPwd.getText().toString().length() < 8 || etPwd.getText().toString().length() > 18) {
            XToastUtil.showToast(RegistActivity.this, getString(R.string.set_pwd));
            return;
        }
        ParamsMap paramsLogin = new ParamsMap();
        paramsLogin.put("loginPhone", etMobile.getText().toString());
        paramsLogin.put("smsCode", etCode.getText().toString());
        paramsLogin.put("loginPassword", etPwd.getText().toString());
        paramsLogin.put("smsToken", smsToken);
        HttpUtils.post(this, Constant.REGISTER, paramsLogin, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData>() {
                }.getType();
                ResultData resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    finish();
                }
                XToastUtil.showToast(RegistActivity.this, resultData.getDesc());
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
     * 输入框效果
     */
    private void setEditAttribute() {
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isAll = !TextUtils.isEmpty(etMobile.getText().toString()) && !TextUtils.isEmpty(etCode.getText().toString()) && !TextUtils.isEmpty(etPwd.getText().toString());
                if (isAll) {
                    btSure.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btSure.setTextColor(getResources().getColor(R.color.trance99));
                }
                if (!TextUtils.isEmpty(s)) {
                    viewOne.setBackgroundColor(getResources().getColor(R.color.color_00b464));
                } else {
                    viewOne.setBackgroundColor(getResources().getColor(R.color.color_line));
                }
            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isAll = !TextUtils.isEmpty(etMobile.getText().toString()) && !TextUtils.isEmpty(etCode.getText().toString()) && !TextUtils.isEmpty(etPwd.getText().toString());
                if (isAll) {
                    btSure.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btSure.setTextColor(getResources().getColor(R.color.trance99));
                }
                if (!TextUtils.isEmpty(s)) {
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.color_00b464));
                } else {
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.color_line));
                }
            }
        });
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isAll = !TextUtils.isEmpty(etMobile.getText().toString()) && !TextUtils.isEmpty(etCode.getText().toString()) && !TextUtils.isEmpty(etPwd.getText().toString());
                if (isAll) {
                    btSure.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btSure.setTextColor(getResources().getColor(R.color.trance99));
                }
                if (!TextUtils.isEmpty(s)) {
                    viewSan.setBackgroundColor(getResources().getColor(R.color.color_00b464));
                } else {
                    viewSan.setBackgroundColor(getResources().getColor(R.color.color_line));
                }
            }
        });

    }
}
