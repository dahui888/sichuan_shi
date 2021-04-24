package com.campuscard.app.ui.activity.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XToastUtil;
import com.base.frame.weigt.Toolbar;
import com.base.frame.weigt.XGridViewForScrollView;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.pay.PayUtils;
import com.campuscard.app.ui.DataFactory;
import com.campuscard.app.ui.entity.MoneyEntity;
import com.campuscard.app.ui.entity.OrderEntity;
import com.campuscard.app.ui.holder.MoneyGridAdapter;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.utils.StringUtil;
import com.ccb.ccbnetpay.message.CcbPayResultListener;
import com.ccb.ccbnetpay.util.IPUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 电费充值
 */
@ContentView(R.layout.activity_recharge_elect)
public class RechargeElectricActivity extends BaseActivity implements TextWatcher {


    @ViewInject(R.id.tv_yu_dl)
    protected TextView tvYuDl;
    @ViewInject(R.id.tv_yu_df)
    protected TextView tvYuDf;
    @ViewInject(R.id.et_money)
    protected EditText etMoney;
    @ViewInject(R.id.radioGroup)
    protected RadioGroup radioGroup;
    @ViewInject(R.id.tv_money)
    protected TextView tvMoney;
    @ViewInject(R.id.bt_pay)
    protected TextView btPay;
    @ViewInject(R.id.tv_danwei)
    private TextView tvDanWei;
    private MoneyGridAdapter gridAdapter;
    @ViewInject(R.id.mGridView)
    private XGridViewForScrollView mGridView;

    private String room, schoool, fooer, Ip = "", payType = PayUtils.JIAN_HANG_PAY;
    private double sydl, sydf, money = 0, totalMoney = 0;//默认支付金额;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        EventBus.getDefault().register(this);
        room = getIntent().getStringExtra("room");
        schoool = getIntent().getStringExtra("shcool");
        fooer = getIntent().getStringExtra("fooler");
        sydl = getIntent().getDoubleExtra("sydl", 0);
        sydf = getIntent().getDoubleExtra("sydf", 0);
        tvYuDf.setText(sydf + "元");
        tvYuDl.setText(sydl + "度");
        IPUtil.getIPAddress(new IPUtil.GetPhoneIPListener() {
            @Override
            public void success(String ip) {
                Ip = ip;
            }

            @Override
            public void failed(String msg) {
            }
        });
        etMoney.addTextChangedListener(this);
    }

    @Override
    public void getData() {
        gridAdapter = new MoneyGridAdapter(this);
        mGridView.setAdapter(gridAdapter);
        //支付方式
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_blank:
                        payType = PayUtils.JIAN_HANG_PAY;
                        break;
                    case R.id.rb_weixin:
                        payType = PayUtils.WX;
                        break;
                    case R.id.rb_appliy:
                        payType = PayUtils.ALIPAY;
                        break;
                }
            }
        });
    }

    @Event(value = {R.id.bt_pay}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_pay:
                try {
                    if (money == 0 && TextUtils.isEmpty(etMoney.getText().toString())) {
                        XToastUtil.showToast(this, getString(R.string.input_money_info));
                        return;
                    }
                    if (!TextUtils.isEmpty(etMoney.getText().toString()) && Double.parseDouble(etMoney.getText().toString()) > 1000) {
                        XToastUtil.showToast(this, getString(R.string.input_money_info));
                        return;
                    }
                    if (totalMoney > 1000) {
                        XToastUtil.showToast(this, getString(R.string.input_money_info));
                        return;
                    }
                } catch (Exception e) {
                }
                //支付
                ParamsMap paramsMap = new ParamsMap();
                paramsMap.put("building", fooer);
                paramsMap.put("campusName", schoool);
                paramsMap.put("roomNo", room);
                paramsMap.put("amount", StringUtil.douToString(totalMoney));
                paramsMap.put("oldBattery", sydl);
                paramsMap.put("description", "电费充值");
                HttpUtils.post(this, Constant.ELECT_SAVE, paramsMap, DialogUtils.showLoadDialog(this, getString(R.string.submitting)), new HttpResponseCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ResultData<OrderEntity>>() {
                        }.getType();
                        final ResultData<OrderEntity> resultData = gson.fromJson(result, type);
                        if (resultData.getStatus() == 200) {
                            PayUtils.startPay(RechargeElectricActivity.this, PayUtils.payParams(resultData.getData().getOrderNo(), StringUtil.doubleToString(totalMoney), resultData.getData().getDescription(), Ip), payType, new CcbPayResultListener() {
                                @Override
                                public void onSuccess(Map<String, String> map) {
                                    DataFactory.updateOrder(RechargeElectricActivity.this, resultData.getData().getId(), payType, new DataFactory.OnResult() {
                                        @Override
                                        public void onSuccess() {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("money", totalMoney + "");
                                            IntentUtil.redirectToNextActivity(RechargeElectricActivity.this, PaySucceedActivity.class, bundle);
                                        }
                                    });
                                }

                                @Override
                                public void onFailed(String s) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailed(int code, String failedMsg) {
                    }

                    @Override
                    public void onFinished() {
                    }
                });
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s)) {
            try {
                if (Integer.parseInt(String.valueOf(s)) > 1000) {
                    etMoney.setBackgroundResource(R.drawable.bg_red_line);
                } else {
                    etMoney.setBackgroundResource(R.drawable.bg_green_line);
                }
                totalMoney = Double.parseDouble(etMoney.getText().toString());
                showMoney(totalMoney);
                //清楚选中
                gridAdapter.setClean(true);
            } catch (Exception e) {
            }
        } else {
            etMoney.setBackgroundResource(R.drawable.bg_gray_line);
            //清楚
            gridAdapter.setClean(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MoneyEntity moneyEntity) {
        if (moneyEntity != null) {
            if (!TextUtils.isEmpty(etMoney.getText().toString())) {
                money = moneyEntity.getMoney();
                totalMoney = moneyEntity.getMoney();
                showMoney(totalMoney);
                etMoney.setText("");
            } else {
                money = moneyEntity.getMoney();
                totalMoney = moneyEntity.getMoney();
                showMoney(money);
            }
        }
    }

    /**
     * 展示价格
     */
    private void showMoney(double totalMoney) {
        String money = StringUtil.douToString(totalMoney);
        if (!TextUtils.isEmpty(money)) {
            String[] moneyArrys = money.split("\\.");
            if (moneyArrys.length > 0) {
                tvMoney.setText("￥" + moneyArrys[0]);
                tvDanWei.setText("." + moneyArrys[1] + "元");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
