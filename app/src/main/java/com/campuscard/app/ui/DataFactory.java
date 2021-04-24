package com.campuscard.app.ui;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.utils.XDateUtil;
import com.base.frame.utils.XToastUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.pay.PayUtils;
import com.campuscard.app.ui.activity.my.SetPasswordCodeActivity;
import com.campuscard.app.ui.entity.CarSateEntity;
import com.campuscard.app.ui.entity.DeleteSuccessEntity;
import com.campuscard.app.ui.entity.TxyamEntity;
import com.campuscard.app.ui.entity.VersionEntity;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.utils.YzmCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 数据处理
 */
public class DataFactory {

    /**
     * 得到卡的状态
     *
     * @param cardId
     */
    public static void getCardState(final Context context, String cardId, final LinearLayout linData, final TextView tvName, final TextView tvCardMoney, final TextView tvCardState) {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.PRE_RECHARGE + cardId);
        HttpUtils.get(context, httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<CarSateEntity>>() {
                }.getType();
                ResultData<CarSateEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null) {
                        linData.setVisibility(View.VISIBLE);
                        tvName.setText(resultData.getData().getName());
                        tvCardMoney.setText(resultData.getData().getBalance() + "元");
                        switch (resultData.getData().getStatus()) {
                            case "NORMAL":
                                tvCardState.setText("正常");
                                break;
                            case "LOSS":
                                tvCardState.setText("已挂失");
                                break;
                            case "FROZEN":
                                tvCardState.setText("冻结");
                                break;
                            case "DISCONTINUATION":
                                tvCardState.setText("停用");
                                break;
                            case "NOCARD":
                                tvCardState.setText("未发卡");
                                break;
                        }
                    }
                } else {
                    linData.setVisibility(View.GONE);
                    tvName.setText("");
                    tvCardMoney.setText("");
                    tvCardState.setText("");
                    XToastUtil.showToast(context, resultData.getDesc());
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

    /**
     * 更新订单
     *
     * @param id        订单id
     * @param payMethod 支付方式
     */
    public static void updateOrder(Context context, String id, String payMethod, final OnResult onResult) {
        ParamsMap paramsMap = new ParamsMap();
        paramsMap.put("payMethod", payMethod);
        switch (payMethod) {
            case PayUtils.JIAN_HANG_PAY:
                paramsMap.put("payMethodName", PayUtils.JIAN_HANG_PAY_NAME);
                break;
            case PayUtils.WX:
                paramsMap.put("payMethodName", PayUtils.WX_NAME);
                break;
            case PayUtils.ALIPAY:
                paramsMap.put("payMethodName", PayUtils.ALIPAY_NAME);
                break;
        }
        paramsMap.put("id", id);
        HttpUtils.post(context, Constant.UPDATE_ORDER, paramsMap, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData>() {
                }.getType();
                ResultData resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    onResult.onSuccess();
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

    /**
     * 删除失物招领
     *
     * @param context
     * @param ids
     */
    public static void deleteRelease(final Context context, String ids, final OnResult onResult) {
        ParamsMap params = new ParamsMap();
        params.put("ids", ids);
        HttpUtils.post(context, Constant.DELETE, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData>() {
                }.getType();
                ResultData resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    onResult.onSuccess();
                } else {
                    XToastUtil.showToast(context, resultData.getDesc());
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


    /**
     * 版本信息
     *
     * @param context
     * @param callback
     */
    public static void versionUdate(final Context context, final OnResultEntityCallback callback) {
        ParamsMap params = new ParamsMap();
        params.put("clientType", "ANDROID");
        HttpUtils.post(context, Constant.VERSION, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<VersionEntity>>() {
                }.getType();
                ResultData<VersionEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    callback.onSuccess(resultData.getData());
                } else {
                    XToastUtil.showToast(context, resultData.getDesc());
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

    /**
     * 回掉
     */
    public interface OnResult {
        void onSuccess();
    }

    public interface OnResultEntityCallback {
        void onSuccess(VersionEntity versionEntity);
    }
}
