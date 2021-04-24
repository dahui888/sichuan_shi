package com.campuscard.app.ui.activity.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.frame.weigt.Toolbar;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.http.ResultPageData;
import com.campuscard.app.pay.PayUtils;
import com.campuscard.app.ui.entity.CardRecordEntity;
import com.campuscard.app.ui.entity.EelInfoEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 消费账单详情
 */
@ContentView(R.layout.activity_record_info)
public class RecordInfoActivity extends BaseActivity {

    @ViewInject(R.id.iv_tag)
    protected ImageView ivTag;
    @ViewInject(R.id.tv_title)
    protected TextView tvTitle;
    @ViewInject(R.id.tv_money)
    protected TextView tvMoney;
    @ViewInject(R.id.tv_pay_type)
    protected TextView tvPayType;
    @ViewInject(R.id.tv_name_school)
    protected TextView tvNameSchool;
    @ViewInject(R.id.tv_name)
    protected TextView tvName;
    @ViewInject(R.id.tv_num_floor)
    protected TextView tvNumFloor;
    @ViewInject(R.id.tv_card_num)
    protected TextView tvCardNum;
    @ViewInject(R.id.tv_qsh)
    protected TextView tvQsh;
    @ViewInject(R.id.rela_qsh)
    protected RelativeLayout relaQsh;
    @ViewInject(R.id.tv_pay_money)
    protected TextView tvPayMoney;
    @ViewInject(R.id.tv_time)
    protected TextView tvTime;
    @ViewInject(R.id.tv_order_num)
    protected TextView tvOrderNum;
    private String type, id;
    private HttpRequestParams params;
    @ViewInject(R.id.tv_pay_info)
    private TextView tvPayInfo;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");

        //校园卡记录详情
        if (TextUtils.equals("1", type)) {
            relaQsh.setVisibility(View.GONE);
            tvTitle.setText("校园卡充值");
            ivTag.setImageResource(R.mipmap.ic_car);
        } else {
            //电费记录详情
            relaQsh.setVisibility(View.VISIBLE);
            tvNameSchool.setText("校区");
            tvNumFloor.setText("楼栋");
            tvTitle.setText("电费充值");
            ivTag.setImageResource(R.mipmap.ic_dfcz);
        }
    }

    @Override
    public void getData() {

        //校园卡记录详情
        if (TextUtils.equals("1", type)) {
            params = new HttpRequestParams(Constant.CARD_RECORD_INFO + id);
            HttpUtils.get(this, params, new HttpResponseCallBack() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResultData<EelInfoEntity>>() {
                    }.getType();
                    ResultData<EelInfoEntity> resultData = gson.fromJson(result, type);
                    if (resultData.getStatus() == 200) {
                        if (resultData.getData() != null) {
                            tvMoney.setText(resultData.getData().getAmount());

                            if (!TextUtils.isEmpty(resultData.getData().getPayMethod())) {
                                switch (resultData.getData().getPayMethod()) {
                                    case PayUtils.JIAN_HANG_PAY:
                                        tvPayType.setText("建行支付");
                                        break;
                                    case PayUtils.WX:
                                        tvPayType.setText("微信支付");
                                        break;
                                    case PayUtils.ALIPAY:
                                        tvPayType.setText("支付宝支付");
                                        break;
                                }
                            }
                            tvName.setText(resultData.getData().getEndUserName());
                            tvCardNum.setText(resultData.getData().geteCardId());
                            tvPayMoney.setText(resultData.getData().getAmount());
                            tvTime.setText(resultData.getData().getCreateDate());
                            tvOrderNum.setText(resultData.getData().getOrderNo());
                            tvPayInfo.setText(resultData.getData().getResult());
                        }
                    }
                }

                @Override
                public void onFailed(int code, String failedMsg) {
                }

                @Override
                public void onFinished() {
                }
            });

        } else {
            //电费记录详情
            params = new HttpRequestParams(Constant.ELC_RECORD_INFO + id);
            HttpUtils.get(this, params, new HttpResponseCallBack() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResultData<EelInfoEntity>>() {
                    }.getType();
                    ResultData<EelInfoEntity> resultData = gson.fromJson(result, type);
                    if (resultData.getStatus() == 200) {
                        if (resultData.getData() != null) {
                            tvMoney.setText(resultData.getData().getAmount());
                            if (!TextUtils.isEmpty(resultData.getData().getPayMethod())) {
                                switch (resultData.getData().getPayMethod()) {
                                    case PayUtils.JIAN_HANG_PAY:
                                        tvPayType.setText("建行支付");
                                        break;
                                    case PayUtils.WX:
                                        tvPayType.setText("微信支付");
                                        break;
                                    case PayUtils.ALIPAY:
                                        tvPayType.setText("支付宝支付");
                                        break;
                                }
                            }
                            tvName.setText(resultData.getData().getCampusName());
                            tvCardNum.setText(resultData.getData().getBuilding());
                            tvQsh.setText(resultData.getData().getRoomNo());
                            tvPayMoney.setText(resultData.getData().getAmount());
                            tvTime.setText(resultData.getData().getCreateDate());
                            tvOrderNum.setText(resultData.getData().getOrderNo());
                            tvPayInfo.setText(resultData.getData().getResult());
                        }
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
}
