package com.campuscard.app.ui.activity.my;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.campuscard.app.ui.entity.UserInfoEntity;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.utils.TimeCountUtil;
import com.campuscard.app.utils.UserInfoUtils;
import com.campuscard.app.utils.YzmCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/*
 *
 * 设置密码验证码
 * */
@ContentView(R.layout.activity_set_password_code)
public class SetPasswordCodeActivity extends BaseActivity {

    @ViewInject(R.id.tv_phone)
    protected TextView tvPhone;
    @ViewInject(R.id.et_code)
    protected EditText etCode;
    @ViewInject(R.id.bt_code)
    protected Button btCode;
    @ViewInject(R.id.bt_sure)
    protected Button btNext;
    @ViewInject(R.id.et_pw)
    protected EditText etPw;
    @ViewInject(R.id.et_surepw)
    protected EditText etSurepw;
    private TimeCountUtil timeCountUtil;
    private String smsToken, phone;
    @ViewInject(R.id.view_one)
    protected View viewOne;
    @ViewInject(R.id.view_two)
    protected View viewTwo;
    @ViewInject(R.id.view_san)
    protected View viewSan;
    @ViewInject(R.id.bt_sure)
    protected Button btSure;


    @Override
    public void initView() {
        timeCountUtil = new TimeCountUtil(this, 60 * 1000, 1000, btCode);
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
        UserInfoEntity userInfoEntity = UserInfoUtils.getUserInfo();
        if (userInfoEntity != null && !TextUtils.isEmpty(userInfoEntity.getToken())) {
            tvPhone.setText("当前手机为 " + userInfoEntity.getLoginPhone());
            phone = userInfoEntity.getLoginPhone();
        }
        setEditAttribute();
    }

    @Event(value = {R.id.bt_sure, R.id.bt_code}, type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure://下一步
                if (TextUtils.isEmpty(etCode.getText().toString()) || TextUtils.isEmpty(smsToken)) {
                    XToastUtil.showToast(this, getString(R.string.input_code));
                    return;
                }
                if (TextUtils.isEmpty(etPw.getText().toString()) || TextUtils.isEmpty(etSurepw.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_pwd));
                    return;
                }
                if (etPw.getText().toString().length() < 8 || etPw.getText().toString().length() > 18 || etSurepw.getText().toString().length() < 8 || etSurepw.getText().toString().length() > 18) {
                    XToastUtil.showToast(this, getString(R.string.set_pwd));
                    return;
                }
                if (!TextUtils.equals(etPw.getText().toString(), etSurepw.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.set_pwd_error));
                    return;
                }
                ParamsMap paramsLogin = new ParamsMap();
                paramsLogin.put("loginPhone", phone);
                paramsLogin.put("smsCode", etCode.getText().toString());
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
                            XToastUtil.showToast(SetPasswordCodeActivity.this, "修改成功");
                            finish();
                        } else {
                            XToastUtil.showToast(SetPasswordCodeActivity.this, resultData.getDesc());
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
            case R.id.bt_code:
                //验证码
                getTxyam();
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
                    DialogUtils.showImageValidateDialog(SetPasswordCodeActivity.this, resultData.getData().getImgBase64(), new YzmCallBack() {
                        @Override
                        public void callback(String yzm) {
                            getDuanXin(yzm, resultData.getData().getUuid());
                        }
                    });
                } else {
                    XToastUtil.showToast(SetPasswordCodeActivity.this, resultData.getDesc());
                }
            }

            @Override
            public void onFailed(int code, String failedMsg) {
                if (code == 400) {
                    XToastUtil.showToast(SetPasswordCodeActivity.this, "图形验证码验证码错误");
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
        params.put("phone", phone);
        params.put("code", code);
        params.put("msgCodeType", "UPDATE_PASSWORD_CODE");
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
                XToastUtil.showToast(SetPasswordCodeActivity.this, resultData.getDesc());
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
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isAll = !TextUtils.isEmpty(etCode.getText().toString()) && !TextUtils.isEmpty(etPw.getText().toString()) && !TextUtils.isEmpty(etSurepw.getText().toString());
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

        etPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isAll = !TextUtils.isEmpty(etCode.getText().toString()) && !TextUtils.isEmpty(etPw.getText().toString()) && !TextUtils.isEmpty(etSurepw.getText().toString());
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
        etSurepw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isAll = !TextUtils.isEmpty(etCode.getText().toString()) && !TextUtils.isEmpty(etPw.getText().toString()) && !TextUtils.isEmpty(etSurepw.getText().toString());
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
