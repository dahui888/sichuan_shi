package com.campuscard.app.ui.activity;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XToastUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.UserInfoEntity;
import com.campuscard.app.ui.entity.YzmLoginEntity;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.utils.NetUtil;
import com.campuscard.app.utils.UserInfoUtils;
import com.campuscard.app.view.ClearEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 密码登录
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.btn_right)
    protected TextView btnRight;
    @ViewInject(R.id.et_mobile)
    protected ClearEditText etMobile;
    @ViewInject(R.id.et_code)
    protected ClearEditText etCode;
    @ViewInject(R.id.bt_code)
    protected ImageView btCode;
    @ViewInject(R.id.bt_find)
    protected TextView btFind;
    @ViewInject(R.id.bt_zhuce)
    protected TextView btZhuce;
    @ViewInject(R.id.iv_qq)
    protected ImageView ivQq;
    @ViewInject(R.id.iv_wechat)
    protected ImageView ivWechat;
    @ViewInject(R.id.iv_sina)
    protected ImageView ivSina;
    @ViewInject(R.id.bt_login)
    private Button btLogin;
    @ViewInject(R.id.view_two)
    private View viewTwo;
    @ViewInject(R.id.view_one)
    private View viewOne;
    private int xsoryc = 1;//控制密码的显示和隐藏

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        setTitle("");
        btnRight.setText("验证码登录");
        btnRight.setTextColor(getResources().getColor(R.color.color_666666));
        toolbar.setNavigationIcon(null);
        ivBack.setVisibility(View.GONE);
        //TODO 测试数据
//        etMobile.setText("15198285465");
//        etCode.setText("PCC960628");

    }

    @Override
    public void getData() {
        UserInfoEntity userInfoEntity = UserInfoUtils.getUserInfo();
        if (userInfoEntity != null && !TextUtils.isEmpty(UserInfoUtils.getToken())) {
            IntentUtil.redirectToNextActivity(LoginActivity.this, MainActivity.class);
            finish();
        }
        setEditAttribute();
    }

    @Event(value = {R.id.bt_find, R.id.btn_right, R.id.bt_code, R.id.bt_login, R.id.bt_zhuce, R.id.iv_qq, R.id.iv_sina, R.id.iv_wechat}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
            case R.id.btn_right:
                //验证码登录
                IntentUtil.redirectToNextActivity(this, LoginCodeActivity.class);
                break;
            case R.id.bt_find:
                //忘记密码
                IntentUtil.redirectToNextActivity(this, FindPwdActivity.class);
                break;
            case R.id.bt_code:
                //密码显示隐藏
                if (xsoryc == 1) {
                    //如果选中，显示密码
                    etCode.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btCode.setImageResource(R.mipmap.ic_open_close_t);
                    xsoryc = 2;
                } else {
                    //否则隐藏密码
                    etCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btCode.setImageResource(R.mipmap.ic_open_close);
                    xsoryc = 1;
                }
                break;
            case R.id.bt_login:
                //登录
                if (!NetUtil.isNetworkAvalible(this)) {
                    XToastUtil.showToast(this, "无可用网络");
                } else {
                    login();
                }

                break;
            case R.id.bt_zhuce:
                //注册
                IntentUtil.redirectToNextActivity(this, RegistActivity.class);
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
        }
    }

    /**
     * 登录
     */
    public void login() {
        if (TextUtils.isEmpty(etMobile.getText().toString())) {
            XToastUtil.showToast(LoginActivity.this, getString(R.string.input_mobile));
            return;
        }
        if (etMobile.getText().toString().length() != 11 || !etMobile.getText().toString().startsWith("1")) {
            XToastUtil.showToast(LoginActivity.this, getString(R.string.input_mobile_true));
            return;
        }
        if (TextUtils.isEmpty(etCode.getText().toString())) {
            XToastUtil.showToast(LoginActivity.this, getString(R.string.input_pwd));
            return;
        }
        if (etCode.getText().toString().length() < 8 || etCode.getText().toString().length() > 18) {
            XToastUtil.showToast(LoginActivity.this, getString(R.string.set_pwd));
            return;
        }
        ParamsMap paramsMap = new ParamsMap();
        paramsMap.put("loginPhone", etMobile.getText().toString());
        paramsMap.put("loginPassword", etCode.getText().toString());
        paramsMap.put("deviceToken", "dsfasgerger");
        final Dialog dialog = DialogUtils.showLoadDialog(this, getString(R.string.logining));
        dialog.show();
        HttpUtils.post(this, Constant.LOGIN, paramsMap, new HttpResponseCallBack() {
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
                        IntentUtil.redirectToNextActivity(LoginActivity.this, MainActivity.class);
                        XToastUtil.showToast(LoginActivity.this, "登录成功");
                        finish();
                    }
                } else {
                    XToastUtil.showToast(LoginActivity.this, resultData.getDesc());
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

    //验证码登录成功直接关闭
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(YzmLoginEntity messageEvent) {
        finish();
    }
}
