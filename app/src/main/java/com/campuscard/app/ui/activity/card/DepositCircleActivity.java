package com.campuscard.app.ui.activity.card;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.utils.XDateUtil;
import com.base.frame.weigt.recycle.XRecyclerView;
import com.base.frame.weigt.recycle.XRefreshLayout;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.RechargeRecsBean;
import com.campuscard.app.ui.entity.TranerEntity;
import com.campuscard.app.ui.holder.DepositCircleHolder;
import com.campuscard.app.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 圈存-存款页面
 */
@ContentView(R.layout.activity_depostit_circle)
public class DepositCircleActivity extends BaseActivity implements XRefreshLayout.PullLoadMoreListener {

    @ViewInject(R.id.iv_back)
    protected TextView ivBack;
    @ViewInject(R.id.tv_title)
    protected TextView tvTitle;
    @ViewInject(R.id.tv_right)
    protected TextView tvRight;
    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;
    @ViewInject(R.id.tv_money_info)
    protected TextView tvMoneyInfo;
    @ViewInject(R.id.tv_money)
    protected TextView tvMoney;
    @ViewInject(R.id.tv_danwei)
    private TextView tvDanWei;
    @ViewInject(R.id.lin_data)
    private LinearLayout linearLayout;

    @Override
    public void initView() {
        tvTitle.setText("圈款记录");
        tvMoneyInfo.setText("待圈款余额");
        tvRight.setVisibility(View.GONE);
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new DepositCircleHolder());
        mXRecyclerView.setOnPullLoadMoreListener(this);
    }

    @Override
    public void getData() {
        //圈存记录
        ParamsMap params = new ParamsMap();
        params.put("waitTransferRecType", "RECHARGE");
        HttpUtils.post(this, Constant.TRANSFER_RECORD, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<TranerEntity>>() {
                }.getType();
                ResultData<TranerEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    String money = StringUtil.douToString(resultData.getData().getTotalMoney());
                    if (!TextUtils.isEmpty(money)) {
                        String[] moneyArrys = money.split("\\.");
                        if (moneyArrys.length > 0) {
                            tvMoney.setText(moneyArrys[0]);
                            tvDanWei.setText("." + moneyArrys[1] + "元");
                        }
                    }
                    if (resultData.getData().getRechargeRecs() != null && resultData.getData().getRechargeRecs().size() > 0) {
                        linearLayout.setVisibility(View.GONE);
                        mXRecyclerView.setVisibility(View.VISIBLE);
                        setData(resultData.getData().getRechargeRecs());
                    } else {
                        linearLayout.setVisibility(View.VISIBLE);
                        mXRecyclerView.setVisibility(View.GONE);
                    }
                }
                mXRecyclerView.setPullLoadMoreCompleted();
            }

            @Override
            public void onFailed(int code, String failedMsg) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 数据整合
     *
     * @param rechargeRecs
     */
    private void setData(List<RechargeRecsBean> rechargeRecs) {
        List<List<RechargeRecsBean>> list = new ArrayList<>();
        List<RechargeRecsBean> listnei = new ArrayList<>();
        String s = "";
        for (RechargeRecsBean specsBean : rechargeRecs) {
            //判断是否是相同
            if (s.equals(XDateUtil.getStringByFormat(specsBean.getTimeStamp(), XDateUtil.dateFormatYMD))) {
                listnei.add(specsBean);
            } else {
                listnei = new ArrayList<>();
                listnei.add(specsBean);
                list.add(listnei);
                s = XDateUtil.getStringByFormat(specsBean.getTimeStamp(), XDateUtil.dateFormatYMD);
            }
        }
        mXRecyclerView.getAdapter().setData(0, list);
    }

    @Event(value = {R.id.iv_back, R.id.tv_right}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public boolean onLoadMore() {
        return false;
    }
}
