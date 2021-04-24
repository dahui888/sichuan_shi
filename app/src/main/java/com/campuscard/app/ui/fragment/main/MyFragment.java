package com.campuscard.app.ui.fragment.main;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.SystemBarTintManager;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseApp;
import com.campuscard.app.base.BaseFragment;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.activity.LoginActivity;
import com.campuscard.app.ui.activity.my.BindCardActivity;
import com.campuscard.app.ui.activity.my.CardListActivity;
import com.campuscard.app.ui.activity.my.CurrencyActivity;
import com.campuscard.app.ui.activity.my.MessageCenterActivity;
import com.campuscard.app.ui.activity.my.MyReleaseActivity;
import com.campuscard.app.ui.activity.my.PersonalInfoActivity;
import com.campuscard.app.ui.activity.my.SetPasswordCodeActivity;
import com.campuscard.app.ui.entity.CardUserInfoEntity;
import com.campuscard.app.ui.entity.ItemEvent;
import com.campuscard.app.ui.entity.MsgNumEntity;
import com.campuscard.app.ui.entity.UserInfoEntity;
import com.campuscard.app.utils.SharedCacheUtils;
import com.campuscard.app.utils.UserInfoUtils;
import com.campuscard.app.utils.XImageUtils;
import com.campuscard.app.view.XRoundImageView;
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
 * 我的
 */
@ContentView(R.layout.fragment_my)
public class MyFragment extends BaseFragment {

    @ViewInject(R.id.tv_name)
    private TextView tvName;
    @ViewInject(R.id.tv_mobile)
    private TextView tvMobile;
    @ViewInject(R.id.iv_head)
    private XRoundImageView ivHead;
    @ViewInject(R.id.bt_edit)
    private TextView btEdit;
    @ViewInject(R.id.tv_is_bind)
    private TextView tvIsBind;
    @ViewInject(R.id.bt_bind_card)
    private LinearLayout btBindCard;
    @ViewInject(R.id.bt_release)
    private LinearLayout btRelease;
    @ViewInject(R.id.tv_msg_num)
    private TextView tvMsgNum;
    @ViewInject(R.id.iv1)
    private ImageView iv1;
    @ViewInject(R.id.bt_msg)
    private LinearLayout btMsg;
    @ViewInject(R.id.bt_set_pwd)
    private LinearLayout btSetPwd;
    @ViewInject(R.id.bt_tongyong)
    private LinearLayout btTongyong;
    @ViewInject(R.id.bt_exit)
    private LinearLayout btExit;
    @ViewInject(R.id.bt_user)
    private LinearLayout btUser;
    @ViewInject(R.id.statusBar)
    private View statusBar;
    private boolean isBind;//是否绑卡

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager XSystemBarTintManager = new SystemBarTintManager(getActivity());
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            layoutParams.height = XSystemBarTintManager.getStatusBarHeight(getActivity());
            statusBar.setLayoutParams(layoutParams);
            statusBar.setBackgroundColor(getResources().getColor(R.color.trance));
        }
    }

    @Override
    protected void lazyLoad() {
        UserInfoEntity userInfoEntity = UserInfoUtils.getUserInfo();
        if (userInfoEntity != null && !TextUtils.isEmpty(UserInfoUtils.getToken())) {
            tvName.setText(userInfoEntity.getUserName());
            tvMobile.setText(numberHide(userInfoEntity.getLoginPhone()));
            XImageUtils.loadCircle(getActivity(), userInfoEntity.getHeadPortrait(), ivHead, R.mipmap.ic_head);
        }
        getMsgNum();
        getBind();
    }

    private String numberHide(String str) {
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.replace(3, 7, "****");
        return stringBuffer.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        UserInfoEntity userInfoEntity = UserInfoUtils.getUserInfo();
        if (userInfoEntity != null && !TextUtils.isEmpty(UserInfoUtils.getToken())) {
            tvName.setText(userInfoEntity.getUserName());
            XImageUtils.loadCircle(getActivity(), userInfoEntity.getHeadPortrait(), ivHead, R.mipmap.ic_head);
        }
    }

    @Event(type = View.OnClickListener.class, value = {R.id.bt_bind_card, R.id.bt_release, R.id.bt_msg, R.id.bt_set_pwd,
            R.id.bt_tongyong, R.id.bt_user, R.id.bt_exit})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_user://个人资料
                IntentUtil.redirectToNextActivity(getActivity(), PersonalInfoActivity.class);
                break;
            case R.id.bt_bind_card://绑卡
                if (isBind) {
                    //校园卡
                    IntentUtil.redirectToNextActivity(getActivity(), CardListActivity.class);
                } else {
                    //未绑卡
                    IntentUtil.redirectToNextActivity(getActivity(), BindCardActivity.class);
                }
                break;
            case R.id.bt_release://我的发布
                IntentUtil.redirectToNextActivity(getActivity(), MyReleaseActivity.class);
                break;
            case R.id.bt_msg://消息中心
                IntentUtil.redirectToNextActivity(getActivity(), MessageCenterActivity.class);
                break;
            case R.id.bt_set_pwd://设置密码
                IntentUtil.redirectToNextActivity(getActivity(), SetPasswordCodeActivity.class);
                break;
            case R.id.bt_tongyong://通用
                IntentUtil.redirectToNextActivity(getActivity(), CurrencyActivity.class);
                break;
            case R.id.bt_exit:

                //退出程序
                UserInfoUtils.clearUserInfo();
                SharedCacheUtils.put(Constant.BIND_SHOW, true);
                IntentUtil.redirectToNextActivity(getActivity(), LoginActivity.class);
                for (Activity activity : BaseApp.getActivities()) {
                    activity.finish();
                }
                break;
        }
    }

    //获取未读消息数量
    public void getMsgNum() {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.NO_READ_MSG_NUM);
        HttpUtils.get(getActivity(), httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<MsgNumEntity>>() {
                }.getType();
                ResultData<MsgNumEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData().getPersonal() == 0 && resultData.getData().getPickup_notice() == 0 && resultData.getData().getSystem() == 0) {
                        tvMsgNum.setVisibility(View.GONE);
                    } else {
                        tvMsgNum.setVisibility(View.VISIBLE);
                        tvMsgNum.setText((resultData.getData().getPersonal() + resultData.getData().getPickup_notice() + resultData.getData().getSystem()) + "");
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

    /**
     * 得到绑卡信息
     */
    private void getBind() {
        HttpRequestParams params = new HttpRequestParams(Constant.CARD_USE_INFO);
        HttpUtils.get(getActivity(), params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<CardUserInfoEntity>>() {
                }.getType();
                ResultData<CardUserInfoEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null) {
                        tvIsBind.setText("已绑定");
                        tvIsBind.setTextColor(getResources().getColor(R.color.color_00b464));
                        isBind = true;
                    }
                } else {
                    tvIsBind.setText("未绑定");
                    tvIsBind.setTextColor(getResources().getColor(R.color.color_ff5a1e));
                    isBind = false;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ItemEvent itemEvent) {
        if (itemEvent != null) {
            if (itemEvent.getActivity() == ItemEvent.ACTIVITY.ACTIVITY_UNBIND) {
                getBind();
            } else if (itemEvent.getActivity() == ItemEvent.ACTIVITY.ACTIVITY_MSG_NUM) {
                getMsgNum();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
