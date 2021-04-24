package com.campuscard.app.ui.activity.my;

import android.view.View;
import android.widget.LinearLayout;

import com.base.frame.utils.IntentUtil;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 通用
 */
@ContentView(R.layout.activity_currency)
public class CurrencyActivity extends BaseActivity {

    @ViewInject(R.id.ll_czzn)
    protected LinearLayout llCzzn;
    @ViewInject(R.id.ll_bzyfk)
    protected LinearLayout llBzyfk;
    @ViewInject(R.id.ll_gywm)
    protected LinearLayout llGywm;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
    }

    @Event(value = {R.id.ll_czzn, R.id.ll_bzyfk, R.id.ll_gywm}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_czzn:
                //一卡通操作指南
                IntentUtil.redirectToNextActivity(this, OperationGuideActivity.class);
                break;
            case R.id.ll_bzyfk:
                //帮助与反馈
                IntentUtil.redirectToNextActivity(this, HelpFeedbackActivity.class);
                break;
            case R.id.ll_gywm:
                //关于我们
                IntentUtil.redirectToNextActivity(this, AboutUsActivity.class);
                break;
        }
    }
}