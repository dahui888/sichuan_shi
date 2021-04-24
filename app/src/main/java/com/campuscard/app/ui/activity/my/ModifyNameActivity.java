package com.campuscard.app.ui.activity.my;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.campuscard.app.ui.entity.UserInfoEntity;
import com.campuscard.app.utils.UserInfoUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

@ContentView(R.layout.activity_modify_name)
public class ModifyNameActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    protected TextView btnRight;
    protected EditText etNecheng;
    private String name;
    @ViewInject(R.id.view_one)
    private View view;

    @Override
    public void initView() {
        btnRight = (TextView) findViewById(R.id.btn_right);
        etNecheng = (EditText) findViewById(R.id.et_necheng);
        btnRight.setOnClickListener(this);
        btnRight.setText("保存");
        etNecheng.addTextChangedListener(this);
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
        name = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(name)) {
            etNecheng.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                updataname();
                break;
        }
    }

    /**
     * 更换昵称
     */
    public void updataname() {
        if (TextUtils.isEmpty(etNecheng.getText().toString())) {
            XToastUtil.showToast(this, getString(R.string.input_nikname));
            return;
        }
        if (etNecheng.getText().toString().length() > 12) {
            XToastUtil.showToast(this, getString(R.string.input_nikname_length));
            return;
        }
        ParamsMap paramsMap = new ParamsMap();
        paramsMap.put("userName", etNecheng.getText().toString());
        HttpUtils.post(this, Constant.CHANGNICKNAME, paramsMap, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData>() {
                }.getType();
                ResultData resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    UserInfoEntity userInfo = UserInfoUtils.getUserInfo();
                    userInfo.setUserName(etNecheng.getText().toString());
                    UserInfoUtils.cacheUserInfo(userInfo);
                    XToastUtil.showToast(ModifyNameActivity.this, "设置成功");
                    finish();
                } else {
                    XToastUtil.showToast(ModifyNameActivity.this, resultData.getDesc());
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            view.setBackgroundColor(getResources().getColor(R.color.color_line));
        } else {
            view.setBackgroundColor(getResources().getColor(R.color.color_00b464));
        }

    }
}
