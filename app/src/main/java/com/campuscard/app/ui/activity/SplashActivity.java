package com.campuscard.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.campuscard.app.utils.UserInfoUtils;

/**
 * 启动器
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));//跳转到首页
//
//                if (UserInfoUtils.getUserInfo() != null && !TextUtils.isEmpty(UserInfoUtils.getToken())) {
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));//跳转到首页
//                } else {
//                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));//跳转到登录
//                }
                finish();
            }
        }, 500);
    }
}