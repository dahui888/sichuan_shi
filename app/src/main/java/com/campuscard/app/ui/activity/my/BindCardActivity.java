package com.campuscard.app.ui.activity.my;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.base.frame.utils.XToastUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.CardMsgEntity;
import com.campuscard.app.ui.entity.ItemEvent;
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
 *    desc   : 绑定一卡通
 */
@ContentView(R.layout.activity_bind_card)
public class BindCardActivity extends BaseActivity {

    @ViewInject(R.id.et_car_name)
    protected ClearEditText etCarName;
    @ViewInject(R.id.et_car_num)
    protected ClearEditText etCarNum;
    @ViewInject(R.id.et_car_pwd)
    protected ClearEditText etCarPwd;
    @ViewInject(R.id.bt_bind)
    protected Button btBind;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {

        etCarName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isAll = !TextUtils.isEmpty(etCarName.getText().toString()) && !TextUtils.isEmpty(etCarNum.getText().toString()) && !TextUtils.isEmpty(etCarPwd.getText().toString());
                if (isAll) {
                    btBind.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btBind.setTextColor(getResources().getColor(R.color.trance99));
                }
            }
        });

        etCarNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isAll = !TextUtils.isEmpty(etCarName.getText().toString()) && !TextUtils.isEmpty(etCarNum.getText().toString()) && !TextUtils.isEmpty(etCarPwd.getText().toString());
                if (isAll) {
                    btBind.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btBind.setTextColor(getResources().getColor(R.color.trance99));
                }
            }
        });

        etCarPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isAll = !TextUtils.isEmpty(etCarName.getText().toString()) && !TextUtils.isEmpty(etCarNum.getText().toString()) && !TextUtils.isEmpty(etCarPwd.getText().toString());
                if (isAll) {
                    btBind.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btBind.setTextColor(getResources().getColor(R.color.trance99));
                }
            }
        });
    }

    @Event(value = {R.id.bt_bind}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_bind:
                //绑定校园卡
                if (TextUtils.isEmpty(etCarName.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_names));
                    return;
                }
                if (TextUtils.isEmpty(etCarNum.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_zjhm));
                    return;
                }
                if (TextUtils.isEmpty(etCarPwd.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_cost_pwd));
                    return;
                }
                ParamsMap paramsMap = new ParamsMap();
                paramsMap.put("idCardNo", etCarNum.getText().toString());
                paramsMap.put("name", etCarName.getText().toString());
                paramsMap.put("pwd", etCarPwd.getText().toString());
                HttpUtils.post(this, Constant.BIND_CARD, paramsMap, new HttpResponseCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ResultData<CardMsgEntity>>() {
                        }.getType();
                        ResultData<CardMsgEntity> resultData = gson.fromJson(result, type);
                        if (resultData.getStatus() == 200) {
                            XToastUtil.showToast(BindCardActivity.this, "绑定成功");
                            EventBus.getDefault().post(new ItemEvent(ItemEvent.ACTIVITY.ACTIVITY_UNBIND));
                            finish();
                        } else {
                            XToastUtil.showToast(BindCardActivity.this, resultData.getDesc());
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