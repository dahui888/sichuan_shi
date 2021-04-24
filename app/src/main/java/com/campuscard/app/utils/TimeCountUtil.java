package com.campuscard.app.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.Button;

import com.campuscard.app.R;


/**
 * 创建时间：2018/1/9
 * 类说明：短信倒计时
 */
public class TimeCountUtil extends CountDownTimer {

    private Activity mActivity;
    private Button btn;//按钮

    public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn = btn;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击
        btn.setText(millisUntilFinished / 1000 + "s");//设置倒计时时间
        //设置按钮为灰色，这时是不能点击的
        btn.setTextColor(mActivity.getResources().getColor(R.color.color_98a1a8));
    }

    @Override
    public void onFinish() {
        btn.setText(mActivity.getResources().getString(R.string.getCode));
        btn.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
        btn.setClickable(true);//重新获得点击
        cancel();
    }
}