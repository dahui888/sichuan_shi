package com.campuscard.app.base;

import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.base.frame.utils.NetworkStateReceiver;
import com.base.frame.utils.XAppUtil;
import com.base.frame.weigt.Toolbar;
import com.campuscard.app.R;
import com.base.frame.utils.StatusBarUtil;

import org.xutils.x;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 基本Activity
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    private NetworkStateReceiver networkStateReceiver;
    protected ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        BaseApp.getActivities().add(this);
        StatusBarUtil.setImmersiveStatusBar(this, true);
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_fanhui);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ivBack = findViewById(R.id.iv_btn_back);
        if (ivBack != null) {
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        initView();
        getData();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        //网络监听
        networkStateReceiver = new NetworkStateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkStateReceiver, intentFilter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 页面渲染
     */
    public abstract void initView();

    /**
     * 获取数据
     */
    public abstract void getData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApp.getActivities().remove(this);
        try {
            if (null != networkStateReceiver) {
                unregisterReceiver(networkStateReceiver);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (XAppUtil.isShouldHideKeyboard(v, ev)) { //判断用户点击的是否是输入框以外的区域
                XAppUtil.closeSoftInput(this);   //收起键盘
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}