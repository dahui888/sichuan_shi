package com.campuscard.app.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.ItemEvent;
import com.campuscard.app.ui.entity.MsgNumEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.List;

/*
 * 消息中心
 * */
@ContentView(R.layout.activity_message_center)
public class MessageCenterActivity extends BaseActivity {


    @ViewInject(R.id.tv_my_num)
    protected TextView tvMyNum;
    @ViewInject(R.id.bt_my_msg)
    protected LinearLayout btMyMsg;
    @ViewInject(R.id.tv_sys_num)
    protected TextView tvSysNum;
    @ViewInject(R.id.bt_sys_msg)
    protected LinearLayout btSysMsg;
    @ViewInject(R.id.tv_card_num)
    protected TextView tvCardNum;
    @ViewInject(R.id.bt_card_msg)
    protected LinearLayout btCardMsg;

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.NO_READ_MSG_NUM);
        HttpUtils.get(this, httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<MsgNumEntity>>() {
                }.getType();
                ResultData<MsgNumEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData().getSystem() > 0) {
                        tvSysNum.setVisibility(View.VISIBLE);
                        tvSysNum.setText(resultData.getData().getSystem() + "");
                    } else {
                        tvSysNum.setVisibility(View.GONE);
                    }
                    if (resultData.getData().getPersonal() > 0) {
                        tvMyNum.setVisibility(View.VISIBLE);
                        tvMyNum.setText(resultData.getData().getPersonal() + "");
                    } else {
                        tvMyNum.setVisibility(View.GONE);
                    }
                    if (resultData.getData().getPickup_notice() > 0) {
                        tvCardNum.setVisibility(View.VISIBLE);
                        tvCardNum.setText(resultData.getData().getPickup_notice() + "");
                    } else {
                        tvCardNum.setVisibility(View.GONE);
                    }
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

    @Event(type = View.OnClickListener.class, value = {R.id.bt_my_msg, R.id.bt_sys_msg, R.id.bt_card_msg})
    private void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.bt_my_msg:
                //我的
                bundle.putString("title", "我的消息");
                IntentUtil.redirectToNextActivity(this, MessageListActivity.class, bundle);
                break;
            case R.id.bt_sys_msg:
                //系统
                bundle.putString("title", "系统消息");
                IntentUtil.redirectToNextActivity(this, MessageListActivity.class, bundle);
                break;
            case R.id.bt_card_msg:
                //捡卡
                bundle.putString("title", "捡卡通知");
                IntentUtil.redirectToNextActivity(this, MessageListActivity.class, bundle);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ItemEvent itemEvent) {
        if (itemEvent != null && itemEvent.getActivity() == ItemEvent.ACTIVITY.ACTIVITY_MSG_NUM) {
            getData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
