package com.campuscard.app.ui.activity.my;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.activity.FindPwdActivity;
import com.campuscard.app.ui.activity.LoginCodeActivity;
import com.campuscard.app.ui.activity.RegistActivity;
import com.campuscard.app.ui.entity.CardUserInfoEntity;
import com.campuscard.app.ui.entity.ItemEvent;
import com.campuscard.app.ui.entity.UserInfoEntity;
import com.campuscard.app.utils.UserInfoUtils;
import com.campuscard.app.utils.XImageUtils;
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
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 校园卡
 */
@ContentView(R.layout.activity_card)
public class CardListActivity extends BaseActivity {


    @ViewInject(R.id.tv_name)
    protected TextView tvName;
    @ViewInject(R.id.tv_xuehao)
    protected TextView tvXuehao;
    @ViewInject(R.id.iv_head)
    protected ImageView ivHead;
    @ViewInject(R.id.tv_from_info)
    protected TextView tvFromInfo;
    @ViewInject(R.id.bt_bind)
    private Button btBind;
    private CardUserInfoEntity infoEntity;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        EventBus.getDefault().register(this);
    }

    @Override
    public void getData() {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.CARD_USE_INFO);
        HttpUtils.get(this, httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<CardUserInfoEntity>>() {
                }.getType();
                ResultData<CardUserInfoEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null) {
                        infoEntity = resultData.getData();
                        tvName.setText(resultData.getData().getName());
                        tvXuehao.setText("学号：" + resultData.getData().getEcardNo());
                        tvFromInfo.setText("学院：" + resultData.getData().getAcademy() + "     " + "专业：" + resultData.getData().getMajor());
                    }
                } else {

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

    @Event(value = {R.id.bt_bind}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_bind:
                //解绑
                IntentUtil.redirectToNextActivity(this, UnBindActivity.class);
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ItemEvent itemEvent) {
        if (itemEvent != null && itemEvent.getActivity() == ItemEvent.ACTIVITY.ACTIVITY_UNBIND) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
