package com.campuscard.app.ui.activity.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XToastUtil;
import com.base.frame.weigt.Toolbar;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.CardUserInfoEntity;
import com.campuscard.app.ui.entity.QueryEntity;
import com.campuscard.app.view.BuildingDialog;
import com.campuscard.app.view.ClassTypeDialog;
import com.campuscard.app.view.SchoolDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 电费充值
 */
@ContentView(R.layout.activity_recharge_elect_one)
public class RechargeElectricOneActivity extends BaseActivity implements TextWatcher {

    @ViewInject(R.id.tv_school)
    protected TextView tvSchool;
    @ViewInject(R.id.lin_xx)
    protected LinearLayout linXx;
    @ViewInject(R.id.tv_fooer)
    protected TextView tvFooer;
    @ViewInject(R.id.lin_ld)
    protected LinearLayout linLd;
    @ViewInject(R.id.et_qs_num)
    protected EditText etQsNum;
    @ViewInject(R.id.bt_query)
    private Button bt_query;
    @ViewInject(R.id.btn_right)
    protected TextView btnRight;
    @ViewInject(R.id.view_one)
    private View viewOne;
    private SchoolDialog schoolDialog;
    private BuildingDialog buildingDialog;

    @Override
    public void initView() {
        btnRight.setText(getString(R.string.recharge_record));
        toolbar.setNavigationIcon(null);
    }

    @Override
    public void getData() {
        //校区
        schoolDialog = new SchoolDialog(this);
        schoolDialog.setOnClickDataListener(new SchoolDialog.OnClickDataListener() {
            @Override
            public void onSuccess() {
                tvSchool.setText(schoolDialog.getWork().toString());
                tvFooer.setText("");
                etQsNum.setText("");
            }

            @Override
            public void onDismiss() {
            }
        });
        etQsNum.addTextChangedListener(this);
    }

    @Event(value = {R.id.bt_query, R.id.btn_right, R.id.lin_xx, R.id.lin_ld}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
            case R.id.btn_right:
                //充值记录
                IntentUtil.redirectToNextActivity(this, ElectricRecordActivity.class);
                break;
            case R.id.bt_query:
                //充值
                if (TextUtils.isEmpty(tvSchool.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.choice_school));
                    return;
                }
                if (TextUtils.isEmpty(tvFooer.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.choice_looer));
                    return;
                }
                if (TextUtils.isEmpty(etQsNum.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_qinshi));
                    return;
                }
                getQuery();
                break;
            case R.id.lin_xx:
                //学校
                schoolDialog.show();
                break;
            case R.id.lin_ld:
                //楼栋
                if (TextUtils.isEmpty(tvSchool.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.choice_school));
                    return;
                }
                buildingDialog = new BuildingDialog(this, tvSchool.getText().toString());
                buildingDialog.setOnClickDataListener(new BuildingDialog.OnClickDataListener() {
                    @Override
                    public void onSuccess() {
                        tvFooer.setText(buildingDialog.getWork().toString());
                        etQsNum.setText("");
                    }

                    @Override
                    public void onDismiss() {
                    }
                });
                buildingDialog.show();
                break;
        }
    }

    /**
     * 查询电费信息
     */
    private void getQuery() {
        ParamsMap paramsMap = new ParamsMap();
        paramsMap.put("buildingName", tvFooer.getText().toString());
        paramsMap.put("campusName", tvSchool.getText().toString());
        paramsMap.put("roomName", etQsNum.getText().toString());
        HttpUtils.post(this, Constant.GET_DIANFEI, paramsMap, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<QueryEntity>>() {
                }.getType();
                ResultData<QueryEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200 && resultData.getData() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("shcool", tvSchool.getText().toString());
                    bundle.putString("fooler", tvFooer.getText().toString());
                    bundle.putString("room", etQsNum.getText().toString());
                    bundle.putDouble("sydf", resultData.getData().getSydf());
                    bundle.putDouble("sydl", resultData.getData().getSydl());
                    IntentUtil.redirectToNextActivity(RechargeElectricOneActivity.this, RechargeElectricActivity.class, bundle);
                } else {
                    XToastUtil.showToast(RechargeElectricOneActivity.this, resultData.getDesc());
                }
            }

            @Override
            public void onFailed(int code, String failedMsg) {
                XToastUtil.showToast(RechargeElectricOneActivity.this, "未查询到相关信息，请重试！");
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
        } else {
            viewOne.setBackgroundColor(getResources().getColor(R.color.color_00b464));
        }
    }
}
