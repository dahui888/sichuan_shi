package com.campuscard.app.ui.activity.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XDateUtil;
import com.base.frame.utils.XToastUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.StatisticalItem;
import com.campuscard.app.ui.activity.card.ScreeningByTimeActivity;
import com.campuscard.app.ui.entity.BillEntity;
import com.campuscard.app.ui.entity.CardUserInfoEntity;
import com.campuscard.app.utils.DateTimeUtils;
import com.campuscard.app.utils.StringUtil;
import com.campuscard.app.view.CircleStatisticalView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计统单--图表
 */
@ContentView(R.layout.activity_tongji_chart)
public class TongJiChartActivity extends BaseActivity {

    @ViewInject(R.id.tv_time)
    protected TextView tvTime;
    @ViewInject(R.id.iv_rl)
    protected ImageView ivRl;
    @ViewInject(R.id.tv_money)
    protected TextView tvMoney;
    @ViewInject(R.id.mPieChartView)
    protected CircleStatisticalView mPieChartView;
    @ViewInject(R.id.tv_cy_money)
    protected TextView tvCyMoney;
    @ViewInject(R.id.tv_sc_money)
    protected TextView tvScMoney;
    @ViewInject(R.id.tv_sf_money)
    protected TextView tvSfMoney;
    @ViewInject(R.id.tv_qt_money)
    protected TextView tvQtMoney;
    private CardUserInfoEntity infoEntity;
    List<StatisticalItem> mStatisticalItems;


    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        infoEntity = EventBus.getDefault().getStickyEvent(CardUserInfoEntity.class);
        mPieChartView.setUseAnimation(false);
    }

    @Override
    public void getData() {
        ParamsMap params = EventBus.getDefault().getStickyEvent(ParamsMap.class);
        if (params != null) {
            loadData(params);
            EventBus.getDefault().removeStickyEvent(params);
        } else {
            ParamsMap paramsMap = new ParamsMap();
            if (infoEntity != null) {
                paramsMap.put("customerId", infoEntity.getEcardNo());
            }
            paramsMap.put("month", DateTimeUtils.getInstance().newTime(XDateUtil.dateFormatYM));//月账单的月份
            paramsMap.put("type", "MONTH");
            HttpUtils.post(this, Constant.BILL, paramsMap, new HttpResponseCallBack() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResultData<BillEntity>>() {
                    }.getType();
                    ResultData<BillEntity> resultData = gson.fromJson(result, type);
                    if (resultData.getStatus() == 200) {
                        tvTime.setText(DateTimeUtils.getInstance().newTime(XDateUtil.dateFormatYM));
                        setData(resultData.getData());
                    }
                }

                @Override
                public void onFailed(int code, String failedMsg) {
                }

                @Override
                public void onFinished() {
                }
            });
        }
    }


    /**
     * 数据请求
     */
    private void loadData(ParamsMap paramsMap) {
        HttpUtils.post(this, Constant.BILL, paramsMap, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<BillEntity>>() {
                }.getType();
                ResultData<BillEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null) {
                        setData(resultData.getData());
                    }
                } else {
                    XToastUtil.showToast(TongJiChartActivity.this, resultData.getDesc());
                }
            }

            @Override
            public void onFailed(int code, String failedMsg) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    @Event(value = {R.id.iv_rl}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_rl:
                Bundle bundle = new Bundle();
                bundle.putString("type", "1");
                IntentUtil.redirectToNextActivity(this, ScreeningByTimeActivity.class, bundle);
                break;
        }
    }

    /**
     * 展示数据
     *
     * @param billEntity
     */
    private void setData(BillEntity billEntity) {
        tvMoney.setText("-" + StringUtil.floatToString(billEntity.getTotalAmount()));
        tvCyMoney.setText("-" + StringUtil.floatToString(billEntity.getMealBill()));
        tvScMoney.setText("-" + StringUtil.floatToString(billEntity.getShoppingBill()));
        tvSfMoney.setText("-" + StringUtil.floatToString(billEntity.getElectricityBill()));
        tvQtMoney.setText("-" + StringUtil.floatToString(billEntity.getOther()));


        //统计图二
        int[] color = {Color.parseColor("#009eff"), Color.parseColor("#ff695a"), Color.parseColor("#feb370"), Color.parseColor("#CD3700")};
        String[] markBottom = {"餐饮", "商超", "电费", "其他"};

        mStatisticalItems = new ArrayList<>();
        mStatisticalItems.add(new StatisticalItem(getFlot(billEntity.getMealBill() / billEntity.getTotalAmount()), getFlot(billEntity.getMealBill() / billEntity.getTotalAmount()) * 100 + "", markBottom[0], color[0]));
        mStatisticalItems.add(new StatisticalItem(getFlot(billEntity.getShoppingBill() / billEntity.getTotalAmount()), getFlot(billEntity.getShoppingBill() / billEntity.getTotalAmount()) * 100 + "", markBottom[1], color[1]));
        mStatisticalItems.add(new StatisticalItem(getFlot(billEntity.getElectricityBill() / billEntity.getTotalAmount()), getFlot(billEntity.getElectricityBill() / billEntity.getTotalAmount()) * 100 + "", markBottom[2], color[2]));
        mStatisticalItems.add(new StatisticalItem(getFlot(billEntity.getOther() / billEntity.getTotalAmount()), getFlot(billEntity.getOther() / billEntity.getTotalAmount()) * 100 + "", markBottom[3], color[3]));
        //设置数据方法
        mPieChartView.setStatisticalItems(mStatisticalItems);
    }

    private float getFlot(float num) {
        BigDecimal b = new BigDecimal(num);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
