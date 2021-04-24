package com.campuscard.app.ui.activity.home;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.utils.StringUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * 支付成功
 */
@ContentView(R.layout.activity_pay_succee)
public class PaySucceedActivity extends BaseActivity {
    protected TextView tvMoney;
    protected Button btLogin;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        btLogin = (Button) findViewById(R.id.bt_login);
    }

    @Override
    public void getData() {
        tvMoney.setText(StringUtil.isNumeric(getIntent().getStringExtra("money")));
    }

    @Event(value = {R.id.bt_login}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                //完成
                finish();
                break;
        }
    }
}
