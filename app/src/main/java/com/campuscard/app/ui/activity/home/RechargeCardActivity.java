package com.campuscard.app.ui.activity.home;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XAppUtil;
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
import com.campuscard.app.ui.DataFactory.OnResult;
import com.campuscard.app.ui.entity.CardUserInfoEntity;
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
 * 校园卡充值
 */
@ContentView(R.layout.activity_recharge_card)
public class RechargeCardActivity extends BaseActivity implements EditText.OnEditorActionListener, TextWatcher {
    @ViewInject(R.id.btn_right)
    protected TextView btnRight;
    @ViewInject(R.id.tv_card_name)
    protected TextView tvCardName;
    @ViewInject(R.id.tv_card_num)
    protected EditText tvCardNum;
    @ViewInject(R.id.view_one)
    protected View viewOne;
    @ViewInject(R.id.view_two)
    protected View viewTwo;
    protected TextView tv;
    protected TextView tvDanwei;
    @ViewInject(R.id.lin_data)
    private LinearLayout linData;
    @ViewInject(R.id.et_money)
    protected EditText etMoney;
    @ViewInject(R.id.tv_card_state)
    protected TextView tvCardState;
    @ViewInject(R.id.tv_card_money)
    protected TextView tvCardMoney;
    @ViewInject(R.id.radioGroup)
    protected RadioGroup radioGroup;
    @ViewInject(R.id.tv_money)
    protected TextView tvMoney;
    @ViewInject(R.id.tv_danwei)
    private TextView tvDanWei;
    @ViewInject(R.id.bt_pay)
    protected TextView btPay;
    @ViewInject(R.id.mGridView)
    private XGridViewForScrollView mGridView;
    private MoneyGridAdapter gridAdapter;
    private String payType = PayUtils.JIAN_HANG_PAY;
    private double money = 0, totalMoney = 0;//默认支付金额
    private String Ip = "";//IP
    private CardUserInfoEntity cardUserInfoEntity;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        EventBus.getDefault().register(this);
        btnRight.setText("充值记录");
        etMoney.addTextChangedListener(this);
        IPUtil.getIPAddress(new IPUtil.GetPhoneIPListener() {
            @Override
            public void success(String ip) {
                Ip = ip;
            }

            @Override
            public void failed(String msg) {
            }
        });
        toolbar.setNavigationIcon(null);
    }

    @Override
    public void getData() {
        gridAdapter = new MoneyGridAdapter(this);
        mGridView.setAdapter(gridAdapter);
        tvCardNum.setOnEditorActionListener(this);
        //支付方式
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    default:
                        break;
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
        tvCardNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    viewOne.setBackgroundColor(getResources().getColor(R.color.color_line));
                } else {
                    viewOne.setBackgroundColor(getResources().getColor(R.color.color_00b464));
                }
            }
        });
        //默认展示卡信息
        cardUserInfoEntity = EventBus.getDefault().getStickyEvent(CardUserInfoEntity.class);
        if (cardUserInfoEntity != null) {
            tvCardNum.setText(cardUserInfoEntity.getEcardNo());
            DataFactory.getCardState(RechargeCardActivity.this, cardUserInfoEntity.getEcardNo(), linData, tvCardName, tvCardMoney, tvCardState);
        }
    }

    @Event(value = {R.id.btn_right, R.id.bt_pay})
    private void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
            case R.id.btn_right:
                //充值记录
                IntentUtil.redirectToNextActivity(this, CardRecordActivity.class);
                break;
            case R.id.bt_pay:
                //充值
                if (TextUtils.isEmpty(tvCardNum.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_card_num));
                    return;
                }
                if (money == 0 && TextUtils.isEmpty(etMoney.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_money_info));
                    return;
                }
                try {
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
                ParamsMap params = new ParamsMap();
                params.put("amount", StringUtil.douToString(totalMoney));
                params.put("description", "一卡通充值");
                params.put("eCardId", tvCardNum.getText().toString());
                params.put("endUserName", tvCardName.getText().toString());
                HttpUtils.post(this, Constant.CARD_RECHARGE, params, DialogUtils.showLoadDialog(this, getString(R.string.submitting)), new HttpResponseCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ResultData<OrderEntity>>() {
                        }.getType();
                        final ResultData<OrderEntity> resultData = gson.fromJson(result, type);
                        if (resultData.getStatus() == 200) {
                            PayUtils.startPay(RechargeCardActivity.this, PayUtils.payParams(resultData.getData().getOrderNo(), StringUtil.doubleToString(totalMoney), resultData.getData().getDescription(), Ip), payType, new CcbPayResultListener() {
                                @Override
                                public void onSuccess(Map<String, String> map) {
                                    DataFactory.updateOrder(RechargeCardActivity.this, resultData.getData().getId(), payType, new OnResult() {
                                        @Override
                                        public void onSuccess() {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("money", totalMoney + "");
                                            IntentUtil.redirectToNextActivity(RechargeCardActivity.this, PaySucceedActivity.class, bundle);
                                        }
                                    });
                                }

                                @Override
                                public void onFailed(String s) {
                                }
                            });
                        } else {
                            XToastUtil.showToast(RechargeCardActivity.this, resultData.getDesc());
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
                showMoney(totalMoney);
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (TextUtils.isEmpty(tvCardNum.getText().toString())) {
            XToastUtil.showToast(this, getString(R.string.input_card_num));
            return false;
        }
        XAppUtil.closeSoftInput(this);
        DataFactory.getCardState(RechargeCardActivity.this, tvCardNum.getText().toString(), linData, tvCardName, tvCardMoney, tvCardState);
        return false;
    }
}
