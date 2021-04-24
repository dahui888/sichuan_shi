package com.campuscard.app.base;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;

import com.base.frame.XApplication;
import com.bumptech.glide.request.target.ViewTarget;
import com.campuscard.app.R;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import java.util.ArrayList;
import java.util.List;

/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 程序入口
 */
public class BaseApp extends XApplication {

    private static BaseApp instance;
    private static List<Activity> activities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setPush();
        //Glide已经默认为ImageView设置的Tag
        ViewTarget.setTagId(R.id.glide_tag);
    }

    public static BaseApp instance() {
        return instance;
    }

    public static List<Activity> getActivities() {
        return activities;
    }

    /**
     * 设置友盟推送
     */
    private void setPush() {
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "c3o66hv5oetq48y3l78is01tsuo6xq4b");
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });
        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 通知的回调方法（通知送达时会回调）
             */
            @Override
            public void dealWithNotificationMessage(Context context, UMessage msg) {
                //调用super，会展示通知，不调用super，则不展示通知。
                super.dealWithNotificationMessage(context, msg);
            }

            /**
             * 自定义消息的回调方法
             */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
            }

            /**
             * 自定义通知栏样式的回调方法
             */
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                return super.getNotification(context, msg);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
    }
}
