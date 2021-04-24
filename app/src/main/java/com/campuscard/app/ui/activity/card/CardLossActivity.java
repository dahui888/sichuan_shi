package com.campuscard.app.ui.activity.card;

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
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.CardUserInfoEntity;
import com.campuscard.app.utils.DialogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 校园卡挂失
 */
@ContentView(R.layout.activity_card_loss)
public class CardLossActivity extends BaseActivity {

    @ViewInject(R.id.btn_right)
    protected TextView btnRight;
    @ViewInject(R.id.et_car_num)
    protected TextView etCarNum;
    @ViewInject(R.id.et_car_pwd)
    protected EditText etCarPwd;
    @ViewInject(R.id.bt_sure)
    protected Button btSure;
    @ViewInject(R.id.iv_gantan)
    private ImageView ivGanTan;
    private CardUserInfoEntity cardUserInfoEntity;

    @ViewInject(R.id.view_one)
    private View viewOne;
    @ViewInject(R.id.view_two)
    private View viewTwo;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        btnRight.setText("挂失记录");
    }

    @Override
    public void getData() {
        cardUserInfoEntity = EventBus.getDefault().getStickyEvent(CardUserInfoEntity.class);
        if (cardUserInfoEntity != null) {
            etCarNum.setText(cardUserInfoEntity.getEcardNo());
        }
        setEditAttribute();
    }

    @Event(value = {R.id.btn_right, R.id.bt_sure, R.id.iv_gantan}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                //挂失记录
                IntentUtil.redirectToNextActivity(this, CardLossRecordActivity.class);
                break;
            case R.id.iv_gantan:
                //提示
                DialogUtils.lossCardShow(this);
                break;
            case R.id.bt_sure:
                //提交
                if (TextUtils.isEmpty(etCarPwd.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_card_pwd));
                    return;
                }
                ParamsMap paramsMap = new ParamsMap();
                paramsMap.put("custormPwd", etCarPwd.getText().toString());
                HttpUtils.post(this, Constant.CARD_LOSS, paramsMap, new HttpResponseCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ResultData>() {
                        }.getType();
                        ResultData resultData = gson.fromJson(result, type);
                        if (resultData.getStatus() == 200) {
                            XToastUtil.showToast(CardLossActivity.this, "挂失成功");
                            finish();
                        } else {
                            XToastUtil.showToast(CardLossActivity.this, resultData.getDesc());
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

    /**
     * 输入框效果
     */
    private void setEditAttribute() {
        etCarNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isAll = !TextUtils.isEmpty(etCarNum.getText().toString()) && !TextUtils.isEmpty(etCarPwd.getText().toString());
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

        etCarPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isAll = !TextUtils.isEmpty(etCarNum.getText().toString()) && !TextUtils.isEmpty(etCarPwd.getText().toString());
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

    }
}
