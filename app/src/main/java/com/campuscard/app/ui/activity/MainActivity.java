package com.campuscard.app.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import android.view.KeyEvent;

import com.base.frame.utils.XToastUtil;
import com.base.frame.weigt.XBottomTabView;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.base.BaseApp;
import com.campuscard.app.ui.fragment.main.CardFragment;
import com.campuscard.app.ui.fragment.main.HomeFragment;
import com.campuscard.app.ui.fragment.main.MyFragment;
import com.campuscard.app.ui.fragment.main.SchoolNewFragment;
import com.campuscard.app.utils.SharedCacheUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private ArrayList<Fragment> xFragment = new ArrayList<>();
    private ArrayList<String> tabTexts = new ArrayList<String>();
    @ViewInject(R.id.mBottomTabView)
    private XBottomTabView mBottomTabView;
    private static boolean isExit = false;

    @Override
    public void initView() {
        setTab();
    }

    @Override
    public void getData() {

    }

    /**
     * 设置底部tab
     */
    private void setTab() {
        //设置fragment
        xFragment.add(new HomeFragment());//主页
        xFragment.add(new SchoolNewFragment());//新闻
        xFragment.add(new CardFragment());//主页
        xFragment.add(new MyFragment());//我的

        tabTexts.add(getString(R.string.home));
        tabTexts.add(getString(R.string.notice_notify));
        tabTexts.add(getString(R.string.card));
        tabTexts.add(getString(R.string.my));
        //设置样式
        mBottomTabView.setTabTextColor(getResources().getColor(R.color.color_999999));
        mBottomTabView.setTabSelectColor(getResources().getColor(R.color.colorPrimary));
        mBottomTabView.setTabBackgroundResource(Color.TRANSPARENT);
        mBottomTabView.setTabLayoutBackgroundResource(R.drawable.bottom_bar_bg);
        mBottomTabView.setTabSlidingColor(Color.TRANSPARENT);
        mBottomTabView.setTabTextSize(20);
        //注意图片的顺序
        ArrayList<Drawable> tabDrawables = new ArrayList<Drawable>();
        tabDrawables.add(this.getResources().getDrawable(R.mipmap.ic_home));
        tabDrawables.add(this.getResources().getDrawable(R.mipmap.ic_home_t));
        tabDrawables.add(this.getResources().getDrawable(R.mipmap.ic_new));
        tabDrawables.add(this.getResources().getDrawable(R.mipmap.ic_new_t));
        tabDrawables.add(this.getResources().getDrawable(R.mipmap.ic_card));
        tabDrawables.add(this.getResources().getDrawable(R.mipmap.ic_card_t));
        tabDrawables.add(this.getResources().getDrawable(R.mipmap.ic_my));
        tabDrawables.add(this.getResources().getDrawable(R.mipmap.ic_my_t));

        mBottomTabView.setTabCompoundDrawablesBounds(0, 2, 50, 50);
        //演示增加一组
        mBottomTabView.addItemViews(tabTexts, xFragment, tabDrawables);
        mBottomTabView.setTabPadding(10, 16, 10, 8);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 定义一个变量，来标识是否退出
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private void exit() {
        if (!isExit) {
            isExit = true;
            XToastUtil.showToast(this, getString(R.string.exit_toast));
            SharedCacheUtils.put(Constant.BIND_SHOW, false);
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            for (Activity activity : BaseApp.getActivities()) {
                activity.finish();
            }
        }
    }
}