package com.campuscard.app.ui.entity;

import android.os.Bundle;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : item事件
 */
public class ItemEvent {

    public enum ACTIVITY {
        ACTIVITY_UNBIND, ACTIVITY_RELEASE, ACTIVITY_NEWS, ACTIVITY_LOST, ACTIVITY_MSG_NUM, ACTIVITY_LOST_ALL, ACTIVITY_FOUND_ALL, ACTIVITY_FOUND
    }

    public ItemEvent() {
    }

    public ItemEvent(ACTIVITY activity) {
        this.activity = activity;
    }

    public ItemEvent(ACTIVITY activity, Bundle bundle) {
        this.activity = activity;
        this.bundle = bundle;
    }

    private ACTIVITY activity;
    private Bundle bundle;

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public ACTIVITY getActivity() {
        return activity;
    }

    public void setActivity(ACTIVITY activity) {
        this.activity = activity;
    }


}
