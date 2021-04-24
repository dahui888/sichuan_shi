package com.campuscard.app.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XToastUtil;
import com.base.frame.weigt.Toolbar;
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
import com.campuscard.app.ui.entity.UserInfoEntity;
import com.campuscard.app.ui.entity.YzmLoginEntity;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.utils.TimeCountUtil;
import com.campuscard.app.utils.UserInfoUtils;
import com.campuscard.app.utils.YzmCallBack;
import com.campuscard.app.view.ClearEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 验证码登录
 */
@ContentView(R.layout.activity_login_code)
public class LoginCodeActivity extends BaseActivity {

    @ViewInject(R.id.btn_right)
    protected TextView btnRight;
    @ViewInject(R.id.et_mobile)
    protected ClearEditText etMobile;
    @ViewInject(R.id.et_code)
    protected EditText etCode;
    @ViewInject(R.id.bt_login)
    protected Button btLogin;
    @ViewInject(R.id.bt_advice)
    protected TextView btAdvice;
    @ViewInject(R.id.iv_qq)
    protected ImageView ivQq;
    @ViewInject(R.id.iv_wechat)
    protected ImageView ivWechat;
    @ViewInject(R.id.iv_sina)
    protected ImageView ivSina;
    @ViewInject(R.id.bt_code)
    private Button btCode;


    @ViewInject(R.id.view_two)
    private View viewTwo;
    @ViewInject(R.id.view_one)
    private View viewOne;

    private String smsToken;
    private TimeCountUtil timeCountUtil;

    @Override
    public void initView() {
        timeCountUtil = new TimeCountUtil(this, 60 * 1000, 1000, btCode);
        toolbar.setNavigationIcon(null);
        btnRight.setText("密码登录");
        btnRight.setTextColor(getResources().getColor(R.color.color_666666));
        toolbar.setNavigationIcon(null);
        ivBack.setVisibility(View.GONE);
    }

    @Override
    public void getData() {
        setEditAttribute();
    }

    @Event(value = {R.id.bt_code, R.id.bt_login, R.id.iv_qq, R.id.iv_sina, R.id.iv_wechat, R.id.btn_right, R.id.bt_advice}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.bt_login:
                //验证码登录
                regist(smsToken);
                break;
            case R.id.iv_qq:
                //qq登录
                XToastUtil.showToast(this, getString(R.string.kafahong));
                break;
            case R.id.iv_sina:
                //新浪登录
                XToastUtil.showToast(this, getString(R.string.kafahong));
                break;
            case R.id.iv_wechat:
                //微信登录
                XToastUtil.showToast(this, getString(R.string.kafahong));
                break;
            case R.id.btn_right:
                finish();
                break;
            case R.id.bt_advice:
                //注册协议
                Bundle bundle = new Bundle();
                bundle.putString(WebHtmlActivity.TITLE, "用户使用协议");
                IntentUtil.redirectToNextActivity(this, WebHtmlActivity.class, bundle);
                break;
                default:
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
                    DialogUtils.showImageValidateDialog(LoginCodeActivity.this, resultData.getData().getImgBase64(), new YzmCallBack() {
                        @Override
                        public void callback(String yzm) {
                            getDuanXin(yzm, resultData.getData().getUuid());
                        }
                    });
                } else {
                    XToastUtil.showToast(LoginCodeActivity.this, resultData.getDesc());
                }
            }

            @Override
            public void onFailed(int code, String failedMsg) {
                if (code == 400) {
                    XToastUtil.showToast(LoginCodeActivity.this, "图形验证码验证码错误");
                }
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
        params.put("msgCodeType", "LOGIN_CODE");
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
                XToastUtil.showToast(LoginCodeActivity.this, resultData.getDesc());
            }

            @Override
            public void onFailed(int code, String failedMsg) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    //登录
    public void regist(String smsToken) {
        if (TextUtils.isEmpty(etMobile.getText().toString())) {
            XToastUtil.showToast(LoginCodeActivity.this, getString(R.string.input_mobile));
            return;
        }
        if (etMobile.getText().toString().length() != 11 || !etMobile.getText().toString().startsWith("1")) {
            XToastUtil.showToast(this, getString(R.string.input_mobile_true));
            return;
        }
        if (TextUtils.isEmpty(etCode.getText().toString()) || TextUtils.isEmpty(smsToken)) {
            XToastUtil.showToast(LoginCodeActivity.this, getString(R.string.input_code));
            return;
        }
        ParamsMap paramsLogin = new ParamsMap();
        paramsLogin.put("deviceToken", "dsfasgerger");
        paramsLogin.put("messageCode", etCode.getText().toString());
        paramsLogin.put("loginPhone", etMobile.getText().toString());
        paramsLogin.put("msgToken", smsToken);
        final Dialog dialog = DialogUtils.showLoadDialog(this, getString(R.string.logining));
        dialog.show();
        HttpUtils.post(this, Constant.YZMLOGIN, paramsLogin, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<UserInfoEntity>>() {
                }.getType();
                ResultData<UserInfoEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null) {
                        UserInfoUtils.cacheUserInfo(resultData.getData());
                        UserInfoUtils.cacheToken(resultData.getData().getToken());
                        IntentUtil.redirectToNextActivity(LoginCodeActivity.this, MainActivity.class);
                        XToastUtil.showToast(LoginCodeActivity.this, "登录成功");
                        //通知登录界面关闭
                        EventBus.getDefault().post(new YzmLoginEntity());
                        finish();
                    }
                } else {
                    XToastUtil.showToast(LoginCodeActivity.this, resultData.getDesc());

                }
            }

            @Override
            public void onFailed(int code, String failedMsg) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFinished() {
                if (dialog != null) {
                    dialog.dismiss();
                }
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
                boolean isAll = !TextUtils.isEmpty(etMobile.getText().toString()) && !TextUtils.isEmpty(etCode.getText().toString());
                if (isAll) {
                    btLogin.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btLogin.setTextColor(getResources().getColor(R.color.trance99));
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
                boolean isAll = !TextUtils.isEmpty(etMobile.getText().toString()) && !TextUtils.isEmpty(etCode.getText().toString());
                if (isAll) {
                    btLogin.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btLogin.setTextColor(getResources().getColor(R.color.trance99));
                }
                if (!TextUtils.isEmpty(s)) {
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.color_00b464));
                } else {
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.color_line));
                }
            }
        });

    }
}
