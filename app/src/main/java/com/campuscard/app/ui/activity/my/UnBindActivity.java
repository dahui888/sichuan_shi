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
import com.campuscard.app.ui.activity.RegistActivity;
import com.campuscard.app.ui.entity.CardUserInfoEntity;
import com.campuscard.app.ui.entity.DuanXinEntity;
import com.campuscard.app.ui.entity.ItemEvent;
import com.campuscard.app.ui.entity.TxyamEntity;
import com.campuscard.app.ui.entity.UserInfoEntity;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.utils.TimeCountUtil;
import com.campuscard.app.utils.UserInfoUtils;
import com.campuscard.app.utils.YzmCallBack;
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
 *    desc   : 解绑
 */
@ContentView(R.layout.activity_unbind)
public class UnBindActivity extends BaseActivity implements TextWatcher {


    @ViewInject(R.id.tv_phone)
    protected TextView tvPhone;
    @ViewInject(R.id.et_code)
    protected EditText etCode;
    @ViewInject(R.id.bt_code)
    protected Button btCode;
    @ViewInject(R.id.bt_next)
    protected Button btNext;
    private String phone;
    private TimeCountUtil timeCountUtil;
    private String smsToken;
    @ViewInject(R.id.view_one)
    private View viewOne;


    @Override
    public void initView() {
        UserInfoEntity userInfoEntity = UserInfoUtils.getUserInfo();
        if (userInfoEntity != null && !TextUtils.isEmpty(userInfoEntity.getToken())) {
            tvPhone.setText("当前手机为" + userInfoEntity.getLoginPhone());
            phone = userInfoEntity.getLoginPhone();
        }
        timeCountUtil = new TimeCountUtil(this, 60 * 1000, 1000, btCode);
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
        etCode.addTextChangedListener(this);
    }

    @Event(value = {R.id.bt_next, R.id.bt_code}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_code:
                //验证码
                getTxyam();
                break;
            case R.id.bt_next:
                //解绑
                ParamsMap paramsMap = new ParamsMap();
                paramsMap.put("loginPhone", phone);
                paramsMap.put("smsCode", etCode.getText().toString());
                paramsMap.put("smsToken", smsToken);
                HttpUtils.post(this, Constant.UNBIND_CARD, paramsMap, new HttpResponseCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ResultData>() {
                        }.getType();
                        ResultData resultData = gson.fromJson(result, type);
                        if (resultData.getStatus() == 200) {
                            EventBus.getDefault().post(new ItemEvent(ItemEvent.ACTIVITY.ACTIVITY_UNBIND));
                            EventBus.getDefault().removeStickyEvent(CardUserInfoEntity.class);
                            finish();
                        } else {
                            XToastUtil.showToast(UnBindActivity.this, resultData.getDesc());
                        }
                    }

                    @Override
                    public void onFailed(int code, String failedMsg) {
                        if (code == 400) {
                            XToastUtil.showToast(UnBindActivity.this, "请先获取验证码");
                        }
                    }

                    @Override
                    public void onFinished() {
                    }
                });
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
                    DialogUtils.showImageValidateDialog(UnBindActivity.this, resultData.getData().getImgBase64(), new YzmCallBack() {
                        @Override
                        public void callback(String yzm) {
                            getDuanXin(yzm, resultData.getData().getUuid());
                        }
                    });
                } else {
                    XToastUtil.showToast(UnBindActivity.this, resultData.getDesc());
                }
            }

            @Override
            public void onFailed(int code, String failedMsg) {
                if (code == 400) {
                    XToastUtil.showToast(UnBindActivity.this, "图形验证码验证码错误");
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
        params.put("msgCodeType", "UNBIND_ECARD");
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
                XToastUtil.showToast(UnBindActivity.this, resultData.getDesc());
            }

            @Override
            public void onFailed(int code, String failedMsg) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            viewOne.setBackgroundColor(getResources().getColor(R.color.color_line));
            btNext.setTextColor(getResources().getColor(R.color.trance99));
        } else {
            viewOne.setBackgroundColor(getResources().getColor(R.color.color_00b464));
            btNext.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
