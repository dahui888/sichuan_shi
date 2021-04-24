package com.yykj.mob.share.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class LoginApi implements Callback {
    private static final int MSG_AUTH_CANCEL = 1;
    private static final int MSG_AUTH_ERROR = 2;
    private static final int MSG_AUTH_COMPLETE = 3;
    private OnLoginListener loginListener;
    private String platform;
    private Context context;
    private Handler handler;
    private UserInfo userInfo = new UserInfo();
    private Dialog dialog;

    public LoginApi() {
        handler = new Handler(Looper.getMainLooper(), this);
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setOnLoginListener(OnLoginListener login) {
        this.loginListener = login;
    }

    public void login(Context context) {
        this.context = context.getApplicationContext();
        dialog = CustomDialogUtil.showLoadDialog(context, "登录中...");
        if (platform == null) {
            dialog.dismiss();
            return;
        }
        //初始化SDK
        ShareSDK.initSDK(context);
        Platform plat = ShareSDK.getPlatform(platform);
        if (plat == null) {
            dialog.dismiss();
            return;
        }
        if (plat.isAuthValid()) {
            plat.removeAccount(true);
            dialog.dismiss();
            return;
        }
        //使用SSO授权，通过客户单授权
        plat.SSOSetting(false);
        plat.setPlatformActionListener(new PlatformActionListener() {
            public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
                dialog.dismiss();
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_COMPLETE;
                    msg.arg2 = action;
                    msg.obj = new Object[]{plat.getName(), res};
                    handler.sendMessage(msg);
                }
            }

            public void onError(Platform plat, int action, Throwable t) {
                dialog.dismiss();
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_ERROR;
                    msg.arg2 = action;
                    msg.obj = t;
                    handler.sendMessage(msg);
                }
                t.printStackTrace();
            }

            public void onCancel(Platform plat, int action) {
                dialog.dismiss();
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_CANCEL;
                    msg.arg2 = action;
                    msg.obj = plat;
                    handler.sendMessage(msg);
                }
            }
        });
        plat.showUser(null);
    }

    /**
     * 处理操作结果
     */
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                dialog.dismiss();
                // 取消
                Toast.makeText(context, "登录取消", Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                dialog.dismiss();
                // 失败
                Throwable t = (Throwable) msg.obj;
                Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                dialog.dismiss();
                // 成功
                Object[] objs = (Object[]) msg.obj;
                String plat = (String) objs[0];
                HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
                if (loginListener != null && loginListener.onLogin(plat, res)) {
                    //获取用户信息
                    Platform platform = ShareSDK.getPlatform(plat);
                    userInfo.setUserIcon(platform.getDb().getUserIcon());//用户头像
                    userInfo.setUserName(platform.getDb().getUserName());//用户名
                    userInfo.setUserId(platform.getDb().getUserId());//用户Id
                    userInfo.setPlatform(plat);//用户平台
                    EventBus.getDefault().post(userInfo);
                    dialog.dismiss();
                }
            }
            break;
        }
        return false;
    }
}
